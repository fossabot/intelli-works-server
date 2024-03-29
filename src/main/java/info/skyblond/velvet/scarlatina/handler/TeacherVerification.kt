package info.skyblond.velvet.scarlatina.handler

import com.google.gson.Gson
import info.skyblond.velvet.scarlatina.KotlinMain.ERROR_STRING
import info.skyblond.velvet.scarlatina.models.ValidateResponse
import info.skyblond.velvet.scarlatina.operator.DataBaseOP
import info.skyblond.velvet.scarlatina.operator.TokenOP
import io.javalin.http.Context

object TeacherVerification {
    private val gson = Gson()
    fun verifyTeacherCode(ctx: Context) {
        val raw = ctx.pathParam("code")
        if (raw.length != 44) {
            ctx.result(ERROR_STRING)
            return
        }

        val uid = raw.substring(0, 12)
        val code = raw.substring(12)

        val teacher = DataBaseOP.fetchTeacherByUid(uid)
        if (teacher == null) {//if uid is correct, teacher shouldn't be null
            ctx.result(ERROR_STRING)
            return
        }

        val operator = TokenOP(teacher.key)
        if(operator.validateCode(teacher.counter, code)){
            val token = TokenOP.generateTicket()
            if(token == null){
                ctx.result(ERROR_STRING)
                return
            }
            if(DataBaseOP.updateTeacherToken(teacher, token))
                ctx.result("\"" + teacher.uid + token + "\"")
            else
                ctx.result(ERROR_STRING)
        }else{
            ctx.result(ERROR_STRING)
        }
    }

    fun verifyTeacherToken(ctx: Context): Unit {
        val raw = ctx.pathParam("token")
        val result = TokenOP.validateTeacherToken(raw)
        val unread = DataBaseOP.fetUnreadByUid(raw.substring(0,12))
        if(result != null){
            ctx.result(gson.toJson(ValidateResponse(result, unread)))
        }else{
            ctx.result("{}")
        }
    }
}