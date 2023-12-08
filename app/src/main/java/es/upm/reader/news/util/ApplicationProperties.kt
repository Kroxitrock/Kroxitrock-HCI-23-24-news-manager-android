package es.upm.reader.news.util

import java.io.InputStream
import java.util.Properties

object ApplicationProperties {
    private val properties = Properties()

    fun loadProperties(propertiesStream: InputStream) {
        properties.load(propertiesStream)
    }

    fun getProperty(propertyName: String): String {
        return properties.getProperty(propertyName)
    }
}