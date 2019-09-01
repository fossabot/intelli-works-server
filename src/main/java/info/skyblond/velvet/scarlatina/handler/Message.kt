package info.skyblond.velvet.scarlatina.handler

import com.google.gson.Gson
import info.skyblond.velvet.scarlatina.models.message.SubmitRequest
import info.skyblond.velvet.scarlatina.models.message.TeacherTokenRespond
import info.skyblond.velvet.scarlatina.models.message.TicketInfo
import info.skyblond.velvet.scarlatina.operator.DataBaseOP
import info.skyblond.velvet.scarlatina.operator.TokenOP
import io.javalin.http.Context
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.function.Consumer

object Message {
    private val tickets = ConcurrentHashMap<String, TicketInfo>()//thread-safe
    private val gson = Gson()

    init {
        //remove expired tickets every 12 minutes
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate({
            val expired = ArrayList<String>(tickets.size)
            tickets.forEach { (k, v) ->
                if (v.isExpired)
                    expired.add(k)
            }
            expired.forEach(Consumer<String> { tickets.remove(it) })
        }, 0, 12, TimeUnit.MINUTES)
    }

    fun submitMessage(ctx: Context) {
        ctx.contentType("application/json;charset=utf-8")
        //parse Json to Object
        val submitRequest = gson.fromJson(ctx.body(), SubmitRequest::class.java)

        //validate student information
        if (DataBaseOP.validateStudent(submitRequest.id, submitRequest.name)) {
            //validate ticket, valid in 10 minutes
            val ticketInfo = tickets[submitRequest.ticket]
            if (ticketInfo == null || ticketInfo.isExpired) {
                //respond: expired
                ctx.result("{\"status\": \"expired\"}")
            } else {//ticket is validated.
                if (DataBaseOP.insertMessage(
                                ticketInfo.teacher.uid,
                                submitRequest.id,
                                submitRequest.name,
                                submitRequest.level,
                                submitRequest.message)) {
                    //message write to DataBaseOP success
                    ctx.result("{\"status\": \"ok\"}")
                    //revoke ticket
                    tickets.remove(submitRequest.ticket)
                } else {//something wrong with db operator
                    ctx.result("{\"status\": \"failed\"}")
                }
            }
        } else {//no student found
            ctx.result("{\"status\": \"404\"}")
        }
    }

    fun verifySubmitUrlToken(ctx: Context) {
        ctx.contentType("application/json;charset=utf-8")
        //a standard token should be 12(uid) + 32(token) chars long
        if (ctx.body().length != 44) {
            ctx.result(gson.toJson(TeacherTokenRespond.getFailed()))
            return
        }
        //separate things
        val uid = ctx.body().substring(0, 12)
        val token = ctx.body().substring(12)

        //try to get teacher
        val teacher = DataBaseOP.fetchTeacherByUid(uid)
        if (teacher == null) {//if uid is correct, teacher shouldn't be null
            ctx.result(gson.toJson(TeacherTokenRespond.getFailed()))
            return
        }

        //prepare to validate token and generate ticket
        val operator = TokenOP(teacher.key)

        if (operator.validateToken(token)) {//validate token
            //generate ticket, shouldn't be null if nothing is wrong
            //otherwise the page will redirect to expired link page
            val ticket = TokenOP.generateTicket()
            if (ticket == null) {
                ctx.result(gson.toJson(TeacherTokenRespond.getFailed()))
                return
            }
            //recode ticket, a ticket has a 10 minutes validate time
            tickets[ticket] = TicketInfo(teacher)
            ctx.result(gson.toJson(TeacherTokenRespond.getSuccess(teacher.name, ticket)))
        } else {//token is invalidated
            ctx.result(gson.toJson(TeacherTokenRespond.getFailed()))
        }
    }

    fun fetchMessageWithToken(ctx: Context) {
        ctx.contentType("application/json;charset=utf-8")
        if(TokenOP.validateTeacherToken(ctx.pathParam("token")) != null){
            val uid = ctx.pathParam("token").substring(0,12)
            val messages = DataBaseOP.fetchMessagesByUid(uid)
            ctx.result(gson.toJson(messages))
        }else{
            ctx.result("[]")
        }
    }

    fun updateMessageWithId(ctx: Context) {
        ctx.contentType("application/json;charset=utf-8")
        if(TokenOP.validateTeacherToken(ctx.body()) != null){
            val msgId = ctx.pathParam("id")
            val status = when(ctx.pathParam("status")){
                "0" -> false
                "1" -> true
                else -> return
            }
            ctx.result(gson.toJson(DataBaseOP.updateMessage(msgId.toLong(), status)))
        }
    }


}