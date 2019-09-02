package com.github.ncutsist.intelli.works.server.operator

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
                com.github.ncutsist.intelli.works.server.operator.PropertiesOP.port = com.github.ncutsist.intelli.works.server.operator.PropertiesOP.readIntProperty(properties.getProperty("port"), com.github.ncutsist.intelli.works.server.operator.PropertiesOP.port)
                com.github.ncutsist.intelli.works.server.operator.PropertiesOP.dbHost = com.github.ncutsist.intelli.works.server.operator.PropertiesOP.readStringProperty(properties.getProperty("dbhost"), com.github.ncutsist.intelli.works.server.operator.PropertiesOP.dbHost)
                com.github.ncutsist.intelli.works.server.operator.PropertiesOP.dbPort = com.github.ncutsist.intelli.works.server.operator.PropertiesOP.readIntProperty(properties.getProperty("dbport"), com.github.ncutsist.intelli.works.server.operator.PropertiesOP.dbPort)
                com.github.ncutsist.intelli.works.server.operator.PropertiesOP.dbName = com.github.ncutsist.intelli.works.server.operator.PropertiesOP.readStringProperty(properties.getProperty("dbname"), com.github.ncutsist.intelli.works.server.operator.PropertiesOP.dbName)
                com.github.ncutsist.intelli.works.server.operator.PropertiesOP.dbUser = com.github.ncutsist.intelli.works.server.operator.PropertiesOP.readStringProperty(properties.getProperty("dbuser"), com.github.ncutsist.intelli.works.server.operator.PropertiesOP.dbUser)
                com.github.ncutsist.intelli.works.server.operator.PropertiesOP.dbPass = com.github.ncutsist.intelli.works.server.operator.PropertiesOP.readStringProperty(properties.getProperty("dbpass"), com.github.ncutsist.intelli.works.server.operator.PropertiesOP.dbPass)
            }
        } catch (e: Exception) {
            com.github.ncutsist.intelli.works.server.operator.PropertiesOP.writeProperties()
        }
        com.github.ncutsist.intelli.works.server.operator.PropertiesOP.writeProperties()
    }

    private fun writeProperties() {
        val properties = Properties()
        try {
            FileOutputStream("config.properties").use { output ->
                properties.setProperty("port", com.github.ncutsist.intelli.works.server.operator.PropertiesOP.port.toString())
                properties.setProperty("dbhost", com.github.ncutsist.intelli.works.server.operator.PropertiesOP.dbHost)
                properties.setProperty("dbport", com.github.ncutsist.intelli.works.server.operator.PropertiesOP.dbPort.toString())
                properties.setProperty("dbname", com.github.ncutsist.intelli.works.server.operator.PropertiesOP.dbName)
                properties.setProperty("dbuser", com.github.ncutsist.intelli.works.server.operator.PropertiesOP.dbUser)
                properties.setProperty("dbpass", com.github.ncutsist.intelli.works.server.operator.PropertiesOP.dbPass)
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