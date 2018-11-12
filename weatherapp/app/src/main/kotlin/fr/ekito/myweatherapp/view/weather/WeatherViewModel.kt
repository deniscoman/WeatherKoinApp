package fr.ekito.myweatherapp.view.weather

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import fr.ekito.myweatherapp.domain.entity.DailyForecast
import fr.ekito.myweatherapp.domain.repository.DailyForecastRepository
import fr.ekito.myweatherapp.util.mvvm.RxViewModel
import fr.ekito.myweatherapp.util.rx.SchedulerProvider
import fr.ekito.myweatherapp.util.rx.with
import fr.ekito.myweatherapp.view.Failed
import fr.ekito.myweatherapp.view.Loading
import fr.ekito.myweatherapp.view.ViewModelState

class WeatherViewModel(
        private val dailyForecastRepository: DailyForecastRepository,
        private val schedulerProvider: SchedulerProvider
): RxViewModel() {
    private val _states = MutableLiveData<ViewModelState>()
    val states: LiveData<ViewModelState>
        get() = _states

    fun getWeather(){
        _states.value = Loading
        launch {
            dailyForecastRepository.getWeather()
                    .map { it.first() }
                    .with(schedulerProvider)
                    .subscribe(
                            { weather -> _states.value = WeatherListLoaded.from(listOf(weather)) },
                            { error -> _states.value = Failed(error) })
        }
    }

    fun loadNewLocation(location: String) {
//        launch {
//            dailyForecastRepository.getWeather(location).toCompletable()
//                    .with(schedulerProvider)
//                    .subscribe(
//                            { view?.showLocationSearchSucceed(location) },
//                            { error -> view?.showLocationSearchFailed(location, error) })
//        }
    }
}

data class WeatherListLoaded (
        val location: String,
        val first: DailyForecast,
        val lasts: List<DailyForecast>
) : ViewModelState() {
    companion object {
        fun from(list: List<DailyForecast>): WeatherListLoaded {
            return if (list.isEmpty()) error("weather list should not be empty")
            else {
                val first = list.first()
                val location = first.location
                WeatherListLoaded(location, first, list.takeLast(list.size - 1))
            }
        }
    }
}