package com.ahugey.scattools

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.random.Random

class ScatViewModel(
    private val viewState: MutableLiveData<ScatViewState> = MutableLiveData()
) : ViewModel() {
        init {
            viewState.postValue(ScatViewState())
        }

    private val alphabet = listOf('A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z')

    fun getViewState() : LiveData<ScatViewState> {
        return viewState
    }

    fun onDieTapped() {
        var state = viewState.value
        if (state == null) {
            state = ScatViewState()
        }
        when (state.dieState) {
            is ScatDieState.Stopped -> {
                state.dieState = ScatDieState.Rolling()
                val timer = object: CountDownTimer(1000, 100) {
                    override fun onTick(p0: Long) {
                        state.dieText = getNewDieText(state.dieText)
                        viewState.postValue(state)
                    }

                    override fun onFinish() {
                        state.dieState = ScatDieState.Stopped()
                        viewState.postValue(state)
                    }
                }
                for (die in 0..10) {
                    timer.start()
                }
            }
            is ScatDieState.Rolling -> { }
        }
    }

    private fun getNewDieText(text: Char): Char {
        val index: Int = alphabet.indexOf(text)
        var rng = index
        if (index >= 0) {
            while (rng == index) {
                rng = Random.nextInt(alphabet.size)
            }
            return alphabet[rng]
        } else {
            rng = Random.nextInt(alphabet.size)
        }
        return alphabet[rng]
    }

}

data class ScatViewState(
    var dieText : Char = '?',
    var timerText : String = "0:00",
    var timerState : ScatTimerState = ScatTimerState.Stopped(),
    var dieState: ScatDieState = ScatDieState.Stopped()
)

sealed class ScatTimerState{
    class Ready : ScatTimerState()
    class Ticking : ScatTimerState()
    class Paused : ScatTimerState()
    class Stopped : ScatTimerState()
}

sealed class ScatDieState{
    class Rolling: ScatDieState()
    class Stopped: ScatDieState()
}