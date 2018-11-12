package fr.ekito.myweatherapp.view

open class ViewModelEvent

object Pending : ViewModelEvent()

object Success : ViewModelEvent()

data class Error(val error: Throwable) : ViewModelEvent()