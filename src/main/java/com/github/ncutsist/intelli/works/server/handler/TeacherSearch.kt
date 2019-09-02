package com.github.ncutsist.intelli.works.server.handler

import com.google.gson.Gson
import com.github.ncutsist.intelli.works.server.models.search.SearchRequest
import com.github.ncutsist.intelli.works.server.operator.DataBaseOP
import com.github.ncutsist.intelli.works.server.operator.TokenOP
import io.javalin.http.Context

object TeacherSearch {
    private val gson = Gson()

    fun fetchSearchFormParameter(ctx: Context) {
        ctx.contentType("application/json;charset=utf-8")
        val `object` = ctx.pathParam("object")
        when (`object`.toLowerCase()) {
            "grade" -> {
                ctx.result(gson.toJson(DataBaseOP.fetchGrade()))
                return
            }
            "class" -> {
                ctx.result(gson.toJson(DataBaseOP.fetchClass()))
                return
            }
            "building" -> {
                ctx.result(gson.toJson(DataBaseOP.fetchBuilding()))
                return
            }
            "nation" -> {
                ctx.result(gson.toJson(DataBaseOP.fetchNationHelp()))
                return
            }
            "sex" -> {
                ctx.result(gson.toJson(DataBaseOP.fetchSex()))
                return
            }
            "politic" -> {
                ctx.result(gson.toJson(DataBaseOP.fetchPolitic()))
                return
            }
            else -> ctx.status(400)
        }
    }

    fun doSearch(ctx: Context) {
        ctx.contentType("application/json;charset=utf-8")
        val searchRequest = gson.fromJson(ctx.body(), SearchRequest::class.java)
        if (searchRequest == null) {
            ctx.status(400)
            return
        }
        if(TokenOP.validateTeacherToken(searchRequest.token) != null){
            ctx.result(gson.toJson(DataBaseOP.search(searchRequest)))
        }else{
            ctx.result("[]")
        }
    }

}