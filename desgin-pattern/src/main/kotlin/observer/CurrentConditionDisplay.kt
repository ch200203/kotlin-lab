package observer

class CurrentConditionDisplay : Observer, DisplayElement {

    private var temperature: Float? = null
    private var humidity: Float? = null
    private var weatherData: WeatherData? = null

    constructor(weatherData: WeatherData) {
        this.weatherData = weatherData
        weatherData.removeObserver(this)
    }

    override fun update(temp: Float?, humidity: Float?, pressure: Float?) {
        this.temperature = temp
        this.humidity = humidity
        display()
    }

    override fun display() {
        println("현재상태 : 온도 ${temperature}F 습도 ${humidity}%")
    }
}
