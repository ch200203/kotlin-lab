package com.design.decorator.example

abstract class Beverage {
    open val description: String = "제목 없음"

    abstract fun cost(): Double
}
