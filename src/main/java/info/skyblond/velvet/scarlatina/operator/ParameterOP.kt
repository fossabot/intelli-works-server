package info.skyblond.velvet.scarlatina.operator

import com.google.gson.Gson
import info.skyblond.velvet.scarlatina.models.Parameter

object ParameterOP {
    var parameter: Parameter
    private val gson = Gson()

    init {
        //TODO A simple test of using Json to set parameters.
        parameter = gson.fromJson("{" +
                "    \"fetchStudentBySno\": \"/fetch/student/:sno\"," +
                "    \"fetchRecordWithToken\": \"/fetch/record/:token\"," +
                "    \"submitMessage\": \"/submit\"," +
                "    \"verifySubmitUrlToken\": \"/token\"," +
                "    \"fetchSearchFormParameter\": \"/fetch/:object\"," +
                "    \"doSearch\": \"/fetch/search\"," +
                "    \"fetchMessageWithToken\": \"/fetch/message/:token\"," +
                "    \"updateMessageWithId\": \"/update/message/:id/:status\"," +
                "    \"verifyTeacherCode\": \"/code/:code\"," +
                "    \"verifyTeacherToken\": \"/code/verify/:token\"," +
                "    \"createNotes\": \"/notes/create\"," +
                "    \"fetchNotesBySnoWithToken\": \"/notes/fetch/:token/:sno\"," +
                "    \"fetchNotesByToken\": \"/notes/history/:token\"," +
                "    \"fetchStudentFace\": \"/fetch/face/:sno\"" +
                "}", Parameter::class.java)
    }
}