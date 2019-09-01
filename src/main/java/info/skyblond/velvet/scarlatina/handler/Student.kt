package info.skyblond.velvet.scarlatina.handler

import com.google.gson.Gson
import info.skyblond.velvet.scarlatina.operator.DataBaseOP
import info.skyblond.velvet.scarlatina.operator.TokenOP
import io.javalin.http.Context
import java.io.ByteArrayInputStream

object Student {
    private val gson = Gson()
    fun fetchStudentFace(ctx: Context) {
        ctx.contentType("image/jpeg")
        ctx.result(ByteArrayInputStream(DataBaseOP.fetchStudentFace(ctx.pathParam("sno"))))
    }

    fun fetchStudentBySno(ctx: Context) {
        ctx.contentType("application/json;charset=utf-8")
        if(TokenOP.validateTeacherToken(ctx.body()) != null){
            val sno = ctx.pathParam("sno")
            ctx.result(gson.toJson(DataBaseOP.fetchStudentBySno(sno)))
        }else{
            ctx.result("{}")
        }
    }

    fun fetchRecordWithToken(ctx: Context) {
        ctx.contentType("application/json;charset=utf-8")
        if(TokenOP.validateTeacherToken(ctx.pathParam("token")) != null){
            val record = DataBaseOP.fetchRecord()
            ctx.result(gson.toJson(record))
        }else{
            ctx.result("[]")
        }
    }
}