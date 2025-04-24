package com.design.ch5

// 1. 기본적인 생성방식
class MyClass private constructor() {
    companion object {
        private var uniqueInstance: MyClass? = null

        fun getInstance(): MyClass {
            if (null != uniqueInstance) {
                uniqueInstance = MyClass()
            }
            return uniqueInstance!!
        }
    }
}

class MyTest2 private constructor() {
    companion object {
        private var instance: MyTest2? = null

        fun getInstance(): MyTest2? {
            if (instance == null) {
                instance = MyTest2()
            }

            return instance
        }
    }
}

