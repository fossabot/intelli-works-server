package info.skyblond.velvet.scarlatina.operator

import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

//TODO Use Json instead of Properties

object PropertiesOP {
    var port = 7000
    var dbPort = 3306
    var dbHost = "localhost"
    var dbName = "velvet"
    var dbUser = "velvet"
    var dbPass = "password"

    init {
        val properties = Properties()
        try {
            FileInputStream("config.properties").use { input ->
                properties.load(input)
                port = readIntProperty(properties.getProperty("port"), port)
                dbHost = readStringProperty(properties.getProperty("dbhost"), dbHost)
                dbPort = readIntProperty(properties.getProperty("dbport"), dbPort)
                dbName = readStringProperty(properties.getProperty("dbname"), dbName)
                dbUser = readStringProperty(properties.getProperty("dbuser"), dbUser)
                dbPass = readStringProperty(properties.getProperty("dbpass"), dbPass)
            }
        } catch (e: Exception) {
            writeProperties()
        }
        writeProperties()
    }

    private fun writeProperties() {
        val properties = Properties()
        try {
            FileOutputStream("config.properties").use { output ->
                properties.setProperty("port", port.toString())
                properties.setProperty("dbhost", dbHost)
                properties.setProperty("dbport", dbPort.toString())
                properties.setProperty("dbname", dbName)
                properties.setProperty("dbuser", dbUser)
                properties.setProperty("dbpass", dbPass)
                properties.store(output, "Velvet settings")
            }
        } catch (io: IOException) {
            io.printStackTrace()
        }

    }

    private fun readStringProperty(raw: String?, defaultValue: String): String {
        return raw ?: defaultValue
    }

    private fun readIntProperty(raw: String?, defaultValue: Int): Int {
        if (raw == null)
            return defaultValue
        return try {
            Integer.parseInt(raw)
        } catch (e: Exception) {
            defaultValue
        }

    }
}