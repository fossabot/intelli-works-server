package com.github.ncutsist.intelli.works.server

import com.github.ncutsist.intelli.works.server.handler.Message
import com.github.ncutsist.intelli.works.server.handler.Note
import com.github.ncutsist.intelli.works.server.handler.TeacherSearch
import com.github.ncutsist.intelli.works.server.handler.TeacherVerification
import com.github.ncutsist.intelli.works.server.operator.DataBaseOP
import com.github.ncutsist.intelli.works.server.operator.ParameterOP
import com.github.ncutsist.intelli.works.server.operator.PropertiesOP
import com.github.ncutsist.intelli.works.server.operator.TokenOP
import io.javalin.Javalin
import org.eclipse.jetty.server.Connector
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.ServerConnector
import org.slf4j.LoggerFactory

object KotlinMain{
    private val logger = LoggerFactory.getLogger(KotlinMain::class.java)
    const val ERROR_STRING = "\"error\""
    const val OK_STRING = "\"ok\""

    fun main(){
        logger.info("Making face cache... This may take a lot of time.")
        DataBaseOP.loadFace()
        logger.info("Done.")
        val app = Javalin.create { config ->
            config.server {
                val server = Server()
                val serverConnector = ServerConnector(server)
                serverConnector.host = "localhost" //ip   localhost
                serverConnector.port = PropertiesOP.port
                server.connectors = arrayOf<Connector>(serverConnector)
                server
            }
            config.enableCorsForAllOrigins()
        }.start(PropertiesOP.port)

        //handle message submission
        app.post(ParameterOP.parameter.submitMessage, Message::submitMessage)
        //handle teacher token validation
        app.post(ParameterOP.parameter.verifySubmitUrlToken, Message::verifySubmitUrlToken)
        //verify code for teacher
        app.get(ParameterOP.parameter.verifyTeacherCode, TeacherVerification::verifyTeacherCode)
        //verify code for teacher
        app.get(ParameterOP.parameter.verifyTeacherToken, TeacherVerification::verifyTeacherToken)
        //fetch search form list
        app.get(ParameterOP.parameter.fetchSearchFormParameter, TeacherSearch::fetchSearchFormParameter)
        //do search
        app.post(ParameterOP.parameter.doSearch, TeacherSearch::doSearch)
        //fetch message
        app.get(ParameterOP.parameter.fetchMessageWithToken, Message::fetchMessageWithToken)
        //set message read status
        app.post(ParameterOP.parameter.updateMessageWithId, Message::updateMessageWithId)
        //create work notes
        app.post(ParameterOP.parameter.createNotes, Note::createNotes)
        app.get(ParameterOP.parameter.fetchNotesBySnoWithToken, Note::fetchNotesBySnoWithToken)
        app.get(ParameterOP.parameter.fetchNotesByToken, Note::fetchNotesByToken)
        //fetch face
        app.get(ParameterOP.parameter.fetchStudentFace, com.github.ncutsist.intelli.works.server.handler.Student::fetchStudentFace)
        //fetch a student
        app.post(ParameterOP.parameter.fetchStudentBySno, com.github.ncutsist.intelli.works.server.handler.Student::fetchStudentBySno)
        app.get(ParameterOP.parameter.fetchRecordWithToken, com.github.ncutsist.intelli.works.server.handler.Student::fetchRecordWithToken)
        
        //for test only
        app.get("/*") { ctx -> ctx.result(ctx.path()) }
    }

    fun generateCode(uid: String): String{
        val te = DataBaseOP.fetchTeacherByUid(uid, true)!!
        val tokenOp = TokenOP(te.key)
        val co = tokenOp.generateCode(te.counter + 1)
        return(te.uid + co)
    }
}
