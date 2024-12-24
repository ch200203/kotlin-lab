package sample

import com.study.sample.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime


class StringExtensionsTest {

    @Test
    fun testMaskSSN() {
        assertEquals("123456*******", "1234561234567".maskSSN())
        assertEquals("12345", "12345".maskSSN()) // 길이가 짧은 경우
        assertEquals("123456123456X", "123456123456X".maskSSN()) // 비정상 주민등록번호
    }

    @Test
    fun testMaskName() {
        assertEquals("홍**", "홍길동".maskName())
        assertEquals("김", "김".maskName()) // 한 글자 이름
        assertEquals("", "".maskName()) // 빈 문자열
    }

    @Test
    fun testMaskEmail() {
        assertEquals("a**@example.com", "abc@example.com".maskEmail())
        assertEquals("a@b.c", "a@b.c".maskEmail()) // 짧은 이메일
        assertEquals("notAnEmail", "notAnEmail".maskEmail()) // 이메일 형식이 아닌 경우
    }

    @Test
    fun testMaskPhoneNumber() {
        assertEquals("010****5678", "01012345678".maskPhoneNumber())
        assertEquals("01234", "01234".maskPhoneNumber()) // 길이가 짧은 경우
        assertEquals("02012345678", "02012345678".maskPhoneNumber()) // 다른 형식의 전화번호
    }

    @Test
    fun testReverse() {
        assertEquals("dcba", "abcd".reverse())
        assertEquals("987654321", "123456789".reverse())
        assertEquals("", "".reverse()) // 빈 문자열
    }

    @Test
    fun testRemoveWord() {
        assertEquals("Hello World!", "Hello Kotlin World!".removeWord("Kotlin "))
        assertEquals("Hello!", "Hello!".removeWord("World"))
        assertEquals("Hello", "Hello".removeWord("NotExist")) // 존재하지 않는 단어
    }

    @Test
    fun testToGender() {
        assertEquals("남", "1".toGender())
        assertEquals("여", "2".toGender())
        assertEquals("", null.toGender())
        assertEquals("", "".toGender())
        assertEquals("남", "9".toGender())
    }

    @Test
    fun testFormatToString() {
        val dateTime = LocalDateTime.of(2023, 12, 24, 15, 30, 45)
        val date = LocalDate.of(2023, 12, 24)

        assertEquals("2023-12-24 15:30:45", dateTime.formatToString("yyyy-MM-dd HH:mm:ss"))
        assertEquals("2023년 12월 24일", dateTime.formatToString("yyyy년 MM월 dd일"))
        assertEquals("2023-12-24", date.formatToString("yyyy-MM-dd"))
        assertEquals("24/12/2023", date.formatToString("dd/MM/yyyy"))
    }
}
