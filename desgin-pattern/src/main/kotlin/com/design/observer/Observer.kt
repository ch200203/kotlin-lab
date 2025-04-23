package com.design.observer

interface Observer {
    fun update(temp: Float?, humidity: Float?, pressure: Float?)
}
