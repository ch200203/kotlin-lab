package decorator.example

abstract class CondimentDecorator(
    protected val beverage: Beverage
) : Beverage() {
    abstract override val description: String
}
