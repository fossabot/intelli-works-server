package com.github.ncutsist.intelli.works.server.handler

import com.google.gson.Gson
import com.github.ncutsist.intelli.works.server.operator.DataBaseOP
import com.github.ncutsist.intelli.works.server.operator.TokenOP
import io.javalin.http.Context
import java.io.ByteArrayInputStream

object Student {
    private val gson = Gson()
    fun fetchStudentFace(ctx: Context) {
        ctx.contentType("image/jpeg")
        ctx.result(ByteArrayInputStream(com.github.ncutsist.intelli.works.server.operator.DataBaseOP.fetchStudentFace(ctx.pathParam("sno"))))
    }

    fun fetchStudentBySno(ctx: Context) {
        ctx.contentType("application/json;charset=utf-8")
        if(com.github.ncutsist.intelli.works.server.operator.TokenOP.validateTeacherToken(ctx.body()) != null){
            val sno = ctx.pathParam("sno")
            ctx.result(com.github.ncutsist.intelli.works.server.handler.Student.gson.toJson(com.github.ncutsist.intelli.works.server.operator.DataBaseOP.fetchStudentBySno(sno)))
        }else{
            ctx.result("{}")
        }
    }

    fun fetchRecordWithToken(ctx: Context) {
        ctx.contentType("application/json;charset=utf-8")
        if(com.github.ncutsist.intelli.works.server.operator.TokenOP.validateTeacherToken(ctx.pathParam("token")) != null){
            val record = com.github.ncutsist.intelli.works.server.operator.DataBaseOP.fetchRecord()
            ctx.result(com.github.ncutsist.intelli.works.server.handler.Student.gson.toJson(record))
        }else{
            ctx.result("[]")
        }
    }
}