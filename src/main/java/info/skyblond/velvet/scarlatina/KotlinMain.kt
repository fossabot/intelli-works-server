package info.skyblond.velvet.scarlatina

import info.skyblond.velvet.scarlatina.handler.Message
import info.skyblond.velvet.scarlatina.handler.Note
import info.skyblond.velvet.scarlatina.handler.TeacherSearch
import info.skyblond.velvet.scarlatina.handler.TeacherVerification
import info.skyblond.velvet.scarlatina.operator.DataBaseOP
import info.skyblond.velvet.scarlatina.operator.ParameterOP
import info.skyblond.velvet.scarlatina.operator.PropertiesOP
import info.skyblond.velvet.scarlatina.operator.TokenOP
import io.javalin.Javalin
import org.eclipse.jetty.server.Connector
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.ServerConnector
import org.mariadb.jdbc.MariaDbDataSource
import org.slf4j.LoggerFactory
import org.springframework.jdbc.core.JdbcTemplate
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.sql.SQLException

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
        app.get(ParameterOP.parameter.fetchStudentFace, info.skyblond.velvet.scarlatina.handler.Student::fetchStudentFace)
        //fetch a student
        app.post(ParameterOP.parameter.fetchStudentBySno, info.skyblond.velvet.scarlatina.handler.Student::fetchStudentBySno)
        app.get(ParameterOP.parameter.fetchRecordWithToken, info.skyblond.velvet.scarlatina.handler.Student::fetchRecordWithToken)

        //for test only
        app.get("/*") { ctx -> ctx.result(ctx.path()) }

    }

    fun generateCode(uid: String): String{
        val te = DataBaseOP.fetchTeacherByUid(uid, true)!!
        val tokenOp = TokenOP(te.key)
        val co = tokenOp.generateCode(te.counter + 1)
        return(te.uid + co)
    }

    fun pinyin(){
        val jdbcTemplate = JdbcTemplate()
        val dataSource = MariaDbDataSource(PropertiesOP.dbHost, PropertiesOP.dbPort, PropertiesOP.dbName)
        try {
            dataSource.user = PropertiesOP.dbUser
            dataSource.setPassword(PropertiesOP.dbPass)
        } catch (e: SQLException) {
            throw RuntimeException(e)
        }
        jdbcTemplate.dataSource = dataSource

        val pinyin = HashMap<String, String>()

        val file = File("./pinyin.db")
        if(file.exists()){
            try {
                val br = BufferedReader(FileReader(file))
                br.readLines().forEach {
                    val t = it.split("=")
                    pinyin[t[0]] = t[1]
                }
                br.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }else{
            println("No pinyin lib found.")
            return
        }

        try {
            jdbcTemplate.query("SELECT * FROM tt;"){
                val name = it.getString("name")
                if(name.isNotBlank()){
                    val sno = it.getString("sno")
                    val sb = StringBuilder()
                    for(i in name){
                        if(i != '·') {
                            if(pinyin[i.toString()] != null)
                                sb.append(pinyin[i.toString()])
                            else{
                                println("Error!!! $sno $name @ $i")
                                return@query
                            }
                        }
                    }
                    println("$sno $name $sb")
                    jdbcTemplate.update("UPDATE tt SET pinyin = ? WHERE sno = ?", sb.toString(), sno)
                }else{
                    return@query
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
