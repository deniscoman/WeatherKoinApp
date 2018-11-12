package fr.ekito.myweatherapp.view.splash

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.animation.AnimationUtils
import fr.ekito.myweatherapp.R
import fr.ekito.myweatherapp.view.Pending
import fr.ekito.myweatherapp.view.Success
import fr.ekito.myweatherapp.view.weather.WeatherActivity
import kotlinx.android.synthetic.main.activity_splash.*
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.clearTop
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask
import org.koin.android.viewmodel.ext.android.viewModel
import java.lang.Error

/**
 * Search Weather View
 */
class SplashActivity : AppCompatActivity() {
    private val viewModel : SplashViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        listenToEvents()
        viewModel.getLastWeather()
    }

    private fun listenToEvents() {
        viewModel.events.observe(this, Observer {event ->
            when(event){
                is Pending -> showIsLoading()
                is Success -> showIsLoaded()
                is Error -> showError(event)
            }
        })
    }

    private fun showIsLoading() {
        val animation =
            AnimationUtils.loadAnimation(applicationContext, R.anim.infinite_blinking_animation)
        splashIcon.startAnimation(animation)
    }

    private fun showIsLoaded() {
        startActivity(intentFor<WeatherActivity>().clearTop().clearTask().newTask())
    }

    fun showError(error: Throwable) {
        splashIcon.visibility = View.GONE
        splashIconFail.visibility = View.VISIBLE
        Snackbar.make(splash, "SplashActivity got error : $error", Snackbar.LENGTH_INDEFINITE)
            .setAction(R.string.retry) {
                viewModel.getLastWeather()
            }
            .show()
    }
}
