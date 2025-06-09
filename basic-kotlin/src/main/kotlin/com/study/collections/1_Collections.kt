package com.study.collections

import java.util.LinkedList
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.CopyOnWriteArraySet

fun main() {

    // immutable
    val currencyList = listOf("EUR", "USD", "JPY", "GBP")
    // currency.add() // add 함수가 존재하지 않음

    // mutable
    val mutableCurrency = mutableListOf<String>().apply {
        this.add("USD") // this 가 숨겨져 있음 -> 가독성 좋은 코드 작성가능
        add("JPY")
        add("GBP")
        add("EUR")
    }

    // immutable set
    val numberSet = setOf(1, 2, 3, 4)

    // mutable Set
    val mutableNumberSet = mutableSetOf<Int>().apply {
        add(1)
        add(2)
        add(2)
        add(4)
    }

    val numberMap = mapOf(1 to "one", 2 to "two", 3 to "three")

    // 다양한 방법의 Map 생성방법들
    val mutableNumberMap = mutableMapOf(1 to "one", 2 to "two", 3 to "three")
    val mutableNumberMap2 = mutableMapOf<Int, String>().apply {
        put(4, "four")
    }
    mutableNumberMap2.put(1, "one")
    mutableNumberMap2[2] = "two"
    mutableNumberMap2[3] = "three"

    // 컬렉션 빌더는 내부에서sms mutable 반환은 immutable
    val numberList = buildList {
        add(1)
        add(2)
        add(3)
    }

    // linkedList
    val linkedList = LinkedList<Int>().apply {
        addFirst(1)
        add(2)
        addLast(3)
    }

    // arrayList Iterable 하다
    val arrayList = ArrayList<Int>().apply {
        add(1)
        add(2)
        add(3)
    }

    // 그래서 iterable 패텬이 사용가능하다
    val iterator = currencyList.iterator()

    while (iterator.hasNext()) {
        println(iterator.next())
    }

    println("=========================")


    for (currency in currencyList) {
        println(currency)
    }

    println("=========================")

    currencyList.forEach { println(it) }

    // for loop -> map
    val lowerList = listOf("a", "b", "c")
//     val upperList = mutableListOf<String>()
//    for (lowerCase in lowerList) {
//        upperList.add(lowerCase.uppercase())
//    }

    val upperList = lowerList.map { it.uppercase() }

    println(upperList)

//    val filteredList = mutableListOf<String>()
//    for (upperCase in upperList) {
//        if (upperCase == "A" || upperCase == "C") {
//            filteredList.add(upperCase)
//        }
//    }

    val filteredList = upperList
        .asSequence()
        .filter { it == "A" || it == "C" }
        .filter { it == "B" }
        .toList() // 터미널 오퍼레이터

    // ArrayList 의 Thread-Safe 버전
    CopyOnWriteArrayList<String>().apply {}
}
