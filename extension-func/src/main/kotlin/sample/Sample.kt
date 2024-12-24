package com.study.sample

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * 주민등록번호를 마스킹합니다.
 * 예: 1234561234567 → 123456*******
 */
fun String.maskSSN(): String {
    if (this.length != 13) return this
    return this.replaceRange(6, 13, "*******")
}


/**
 * 이름을 마스킹합니다.
 * 예: 홍길동 → 홍**
 */
fun String.maskName(): String {
    if (this.length <= 1) return this // 한 글자는 마스킹하지 않음
    return this.first() + "*".repeat(this.length - 1)
}

/**
 * 이메일을 마스킹합니다.
 * 예: abc@example.com → a**@example.com
 */
fun String.maskEmail(): String {
    val atIndex = this.indexOf('@')
    if (atIndex == -1) return this
    return this.first() + "*".repeat(atIndex - 1) + this.substring(atIndex)
}

/**
 * 휴대전화 번호를 마스킹합니다.
 * 예: 01012345678 → 010****5678
 */
fun String.maskPhoneNumber(): String {
    return if (this.length < 10) this else this.replaceRange(3, this.length - 4, "*".repeat(this.length - 7))
}


/**
 * 문자열을 역순으로 변환합니다.
 * 예: abcd → dcba
 */
fun String.reverse(): String = this.reversed()

/**
 * 특정 단어를 문자열에서 제거합니다.
 * 예: "Hello Kotlin World!" → "Hello World!" (word="Kotlin ")
 */
fun String.removeWord(word: String): String = this.replace(word, "")


/**
 * 성별 코드를 기반으로 "남" 또는 "여"를 반환합니다.
 * 1, 3, 5, 7, 9 → "남"
 * 그 외 → "여"
 */
fun String?.toGender(): String {
    if (this.isNullOrEmpty()) {
        return ""
    }
    return if (this in listOf("1", "3", "5", "7", "9")) "남" else "여"
}

/**
 * LocalDateTime을 주어진 패턴으로 문자열로 변환합니다.
 */
fun LocalDateTime.formatToString(pattern: String): String {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return this.format(formatter)
}

/**
 * LocalDate를 주어진 패턴으로 문자열로 변환합니다.
 */
fun LocalDate.formatToString(pattern: String): String {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return this.format(formatter)
}
