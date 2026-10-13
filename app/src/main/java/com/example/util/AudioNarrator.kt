package com.example.util

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

class AudioNarrator(context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = TextToSpeech(context.applicationContext, this)
    private var isInitialized = false

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    private val _currentSpokenText = MutableStateFlow<String?>(null)
    val currentSpokenText: StateFlow<String?> = _currentSpokenText.asStateFlow()

    private var currentSpeechRate: Float = 0.9f

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isInitialized = true
            // Try Nigerian English / Yoruba locale
            val yoLocale = Locale("yo", "NG")
            val result = tts?.setLanguage(yoLocale)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Fallback to UK/US English with clear enunciations
                tts?.setLanguage(Locale.UK)
            }
            tts?.setSpeechRate(currentSpeechRate)
            tts?.setPitch(1.05f) // Friendly warm tone for primary school kids

            tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    _isSpeaking.value = true
                }

                override fun onDone(utteranceId: String?) {
                    _isSpeaking.value = false
                    _currentSpokenText.value = null
                }

                override fun onError(utteranceId: String?) {
                    _isSpeaking.value = false
                    _currentSpokenText.value = null
                }
            })
        }
    }

    fun setSpeechRate(rate: Float) {
        currentSpeechRate = rate
        tts?.setSpeechRate(rate)
    }

    fun speak(text: String, isYoruba: Boolean = false) {
        if (text.isBlank()) return
        stop()

        _currentSpokenText.value = text
        _isSpeaking.value = true

        if (isInitialized && tts != null) {
            if (isYoruba) {
                val yoLocale = Locale("yo", "NG")
                tts?.setLanguage(yoLocale)
            } else {
                tts?.setLanguage(Locale.UK)
            }
            val utteranceId = "cai_narration_${System.currentTimeMillis()}"
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
        }
    }

    fun stop() {
        tts?.stop()
        _isSpeaking.value = false
        _currentSpokenText.value = null
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
    }
}

object SoundPlayer {
    private var toneGenerator: ToneGenerator? = try {
        ToneGenerator(AudioManager.STREAM_MUSIC, 85)
    } catch (e: Exception) {
        null
    }

    fun playCorrectChime() {
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP2, 250)
        } catch (e: Exception) {
            // ignore
        }
    }

    fun playIncorrectTone() {
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_NACK, 200)
        } catch (e: Exception) {
            // ignore
        }
    }

    fun playStarEarned() {
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 300)
        } catch (e: Exception) {
            // ignore
        }
    }

    fun playSuccess() = playStarEarned()
    fun playIncorrect() = playIncorrectTone()
    fun playClick() = playCorrectChime()
}
