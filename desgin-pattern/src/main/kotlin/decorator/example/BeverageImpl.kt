package decorator.example

class Espresso : Beverage() {
    override val description: String = "에스프레소"
    override fun cost(): Double = 1.99
}

class Mocha(beverage: Beverage) : CondimentDecorator(beverage) {
    override val description: String
        get() = beverage.description + ", 모카"

    override fun cost(): Double = beverage.cost() + 0.20
}

fun main() {
    val beverage: Beverage = Mocha(Espresso())
    println("${beverage.description} \$${beverage.cost()}")
}
