package com.study.com.study.functional_interface

import java.util.function.BiFunction

class FunctionalCalculator {
    // As-IS If-Else Calculator
    internal fun calculator(operation: String, a: Int, b: Int): Int {
        var result = 0
        result = if (operation == "add") {
            a + b
        } else if (operation == "subtract") {
            a - b
        } else if (operation == "multiply") {
            a * b
        } else if (operation == "divide") {
            a / b
        } else {
            throw UnsupportedOperationException("Invalid operation")
        }

        return result
    }

    // To-Be functional interface calculator
    internal fun functionalCalculator(operation: String, a: Int, b: Int): Int {
        var result = 0

        // 연산을 저장할 Map 생성
        val operations: MutableMap<String, OperationInterface> = mutableMapOf(
            "add" to { x: Int, y: Int -> x + y },
            "subtract" to { x: Int, y: Int -> x - y },
            "multiply" to { x: Int, y: Int -> x * y },
            "divide" to { x: Int, y: Int -> x / y },

            )

        val selectedOperation = operations[operation]
        if (selectedOperation != null) {
            result = selectedOperation(a, b)
        } else {
            throw UnsupportedOperationException("Invalid operation")
        }

        return result
    }

    // To-Be 2 functional interface + Enum Calculator
    internal fun functionalEnumCalculator(inputOperation: String, a: Int, b: Int): Int {
        var result = 0
        try {
            val operation: Operation = Operation.valueOf(inputOperation)
            result = operation.apply(a, b)
        } catch (e: IllegalArgumentException) {
            println("Invalid OperationInterface!")
        }

        return result
    }

    private enum class Operation(private val operation: BiFunction<Int, Int, Int>) {
        ADD(BiFunction { a: Int, b: Int -> a + b }),
        SUBTRACT(BiFunction { a: Int, b: Int -> a - b }),
        MULTIPLY(BiFunction { a: Int, b: Int -> a * b }),
        DIVIDE(BiFunction { a: Int, b: Int -> a / b });

        fun apply(a: Int, b: Int): Int {
            return operation.apply(a, b)
        }
    }

}

// private fun interface OperationInterface : BiFunction<Int?, Int?, Int?>
// typealias 를 사용하면 좀더 간결하게 구현이 가능하다.
typealias OperationInterface = (Int, Int) -> Int

fun main() {
    val fc = FunctionalCalculator()

    println("1. As-IS If-Else Calculator  : " + fc.calculator("add", 1, 2))

    println("2.To-Be functional interface Calculator: " + fc.functionalCalculator("multiply", 10, 15))

    println("3. To-Be 2 functional interface + Enum Calculator : " + fc.functionalEnumCalculator("DIVIDE", 10, 2))
}
/*
 그래서 이렇게하면 뭐가좋은데?!
* 동작을 별도의 기능적 인터페이스로 정의하면 많은 if-else 문 없이도 논리를 캡슐화할 수 있습니다.
* 조건을 특정 함수에 매핑하면 코드를 더 쉽게 읽고 이해할 수 있습니다.
* 기존 코드를 변경하지 않고도 새로운 람다 표현식이나 함수형 인터페이스 구현을 만드는 것처럼 간단하게 새로운 동작을 추가할 수 있습니다.
* 함수형 인터페이스를 사용하면 고차 함수를 사용할 수 있고, 동작을 매개변수로 전달할 수 있습니다.
*
* 이 기능은 함수로 캡슐화할 수 있는 조건부 동작이 많은 경우 매우 유용할 수 있습니다.
/
 */
