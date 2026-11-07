package uz.learningtracker.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import uz.learningtracker.domain.usecase.parseHours
import uz.learningtracker.domain.usecase.parseTasks

class EntryValidationTest {

    @Test
    fun `parseHours accepts integer`() {
        assertEquals(2.0, parseHours("2"))
    }

    @Test
    fun `parseHours accepts decimal with dot`() {
        assertEquals(2.5, parseHours("2.5"))
    }

    @Test
    fun `parseHours accepts decimal with comma`() {
        assertEquals(2.5, parseHours("2,5"))
    }

    @Test
    fun `parseHours accepts zero`() {
        assertEquals(0.0, parseHours("0"))
    }

    @Test
    fun `parseHours accepts 24`() {
        assertEquals(24.0, parseHours("24"))
    }

    @Test
    fun `parseHours rejects negative`() {
        assertNull(parseHours("-1"))
    }

    @Test
    fun `parseHours rejects above 24`() {
        assertNull(parseHours("24.1"))
    }

    @Test
    fun `parseHours rejects blank`() {
        assertNull(parseHours(""))
    }

    @Test
    fun `parseHours rejects non-numeric`() {
        assertNull(parseHours("abc"))
    }

    @Test
    fun `parseHours trims whitespace`() {
        assertEquals(3.0, parseHours("  3  "))
    }

    @Test
    fun `parseTasks accepts zero`() {
        assertEquals(0, parseTasks("0"))
    }

    @Test
    fun `parseTasks accepts positive`() {
        assertEquals(5, parseTasks("5"))
    }

    @Test
    fun `parseTasks rejects negative`() {
        assertNull(parseTasks("-1"))
    }

    @Test
    fun `parseTasks rejects blank`() {
        assertNull(parseTasks(""))
    }

    @Test
    fun `parseTasks rejects non-numeric`() {
        assertNull(parseTasks("abc"))
    }

    @Test
    fun `parseTasks trims whitespace`() {
        assertEquals(3, parseTasks("  3  "))
    }
}
