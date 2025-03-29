package observer

class WeatherData : Subject {

    private val observers = mutableListOf<Observer>()
    var temperature: Float? = null
    var humidity: Float? = null
    var pressure: Float? = null

    override fun registerObserver(o: Observer) {
        observers.add(o)
    }

    override fun removeObserver(observer: Observer) {
        observers.remove(observer)
    }

    // 옵저버의 상태 변화를 알려주는 부분
    override fun notifyObservers() {
        observers.forEach { it.update(temperature, humidity, pressure) }
    }

    // 상태가 변경되면 알림
    fun measurementsChanged() {
        notifyObservers()
    }

    // 새로운 날씨 데이터 설정
    fun setMeasurements(temperature: Float, humidity: Float, pressure: Float) {
        this.temperature = temperature
        this.humidity = humidity
        this.pressure = pressure
        measurementsChanged()
    }
}
