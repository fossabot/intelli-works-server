package info.skyblond.velvet.scarlatina.handler

import com.google.gson.Gson
import info.skyblond.velvet.scarlatina.KotlinMain
import info.skyblond.velvet.scarlatina.models.note.CreateNoteRequest
import info.skyblond.velvet.scarlatina.operator.DataBaseOP
import info.skyblond.velvet.scarlatina.operator.TokenOP
import io.javalin.http.Context

object Note {
    private val gson = Gson()

    fun createNotes(ctx: Context) {
        ctx.contentType("application/json;charset=utf-8")
        val request = gson.fromJson(ctx.body(), CreateNoteRequest::class.java)
        if (request == null) {
            ctx.status(400)
            return
        }
        if(TokenOP.validateTeacherToken(request.token) != null){
            if(DataBaseOP.createNote(request.token.substring(0,12), request.sno, request.category, request.level, request.note, request.comment ))
                ctx.result(KotlinMain.OK_STRING)
            else
                ctx.result(KotlinMain.ERROR_STRING)
        }else{
            ctx.result(KotlinMain.ERROR_STRING)
        }
    }

    fun fetchNotesBySnoWithToken(ctx: Context) {
        ctx.contentType("application/json;charset=utf-8")
        if(TokenOP.validateTeacherToken(ctx.pathParam("token")) != null){
            val sno = ctx.pathParam("sno")
            ctx.result(gson.toJson(DataBaseOP.fetchNotesBySno(sno)))
        }else{
            ctx.result("[]")
        }
    }

    fun fetchNotesByToken(ctx: Context) {
        ctx.contentType("application/json;charset=utf-8")
        val token = ctx.pathParam("token")
        if(TokenOP.validateTeacherToken(token) != null){
            ctx.result(gson.toJson(DataBaseOP.fetchNotesByUid(token.substring(0, 12))))
        }else{
            ctx.result("[]")
        }
    }
}