package util

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*

object Parsers {
    fun parseDate(text: String): LocalDate? {
        val formattedText = text.lowercase(Locale.getDefault())
        var date: LocalDate? = null
        try {
            date = LocalDate.parse(formattedText)
        } catch (e: DateTimeParseException) {
            //ignored
        }
        for (dateFormat in InputMessages.getMessages("DateFormat")) {
            try {
                date = LocalDate.parse(formattedText, DateTimeFormatter.ofPattern(dateFormat))
            } catch (e: DateTimeParseException) {
                //ignored
            }
        }
        return date
    }
}