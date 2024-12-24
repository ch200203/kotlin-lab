package com.study.extension1

/**
 * Nullable 리시버
 */

class A

fun A?.toString(): String {
    if (this == null) return "null"
    return toString()
}

/**
 * 확장 프로퍼티
 */
class BackingField {
    var name: String = ""
        get() = field
        set(value) {
            field = value
        }
}

class ExtensionProperty(var name: String)

var ExtensionProperty.email: String
    get() = name.plus("@gmail.com")
    set(value) {
        name = value
    }


class NewA {
    companion object
}

fun NewA.Companion.say(): String {
    return "Hello World!"
}

/**
 * class 멤버로 확장 선언
 */
class D {
    fun bar() {
        println("D bar")
    }
}

class C {
    fun  baz() {
        println("C baz")
    }

    fun D.foo() {
        bar() // calls D.bar
        baz() // calls C.baz
    }

    fun caller(d: D) {
        d.foo() // call the extension function
    }

}