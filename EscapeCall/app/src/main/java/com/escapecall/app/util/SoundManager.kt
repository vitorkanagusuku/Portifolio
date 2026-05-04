package com.escapecall.app.util

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.os.Build
import com.escapecall.app.R

/**
 * Gerenciador de sons e feedback háptico do EscapeCall.
 * Utiliza SoundPool para sons de baixa latência e feedback imediato.
 */
class SoundManager(private val context: Context) {

    private var soundPool: SoundPool? = null
    private var soundCorrect: Int = 0
    private var soundWrong: Int = 0
    private var soundHint: Int = 0
    private var soundTick: Int = 0
    private var soundVictory: Int = 0
    private var soundGameOver: Int = 0
    private var isLoaded = false

    init {
        initSoundPool()
    }

    private fun initSoundPool() {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(6)
            .setAudioAttributes(audioAttributes)
            .build()

        // Nota: Em um projeto real, os arquivos de som estariam em res/raw/
        // Para este projeto de demonstração, os sons são simulados via vibração
        isLoaded = true
    }

    /**
     * Toca o som de resposta correta e vibra com padrão de sucesso.
     */
    fun playCorrect() {
        vibrate(longArrayOf(0, 100, 50, 100), -1)
    }

    /**
     * Toca o som de resposta errada e vibra com padrão de erro.
     */
    fun playWrong() {
        vibrate(longArrayOf(0, 200, 100, 200), -1)
    }

    /**
     * Toca o som de dica utilizada.
     */
    fun playHint() {
        vibrate(longArrayOf(0, 50), -1)
    }

    /**
     * Toca o som de tick do timer (últimos 10 segundos).
     */
    fun playTick() {
        vibrate(longArrayOf(0, 30), -1)
    }

    /**
     * Toca o som de vitória ao completar todos os enigmas.
     */
    fun playVictory() {
        vibrate(longArrayOf(0, 100, 100, 100, 100, 300), -1)
    }

    /**
     * Toca o som de game over ao esgotar o tempo.
     */
    fun playGameOver() {
        vibrate(longArrayOf(0, 500, 200, 500), -1)
    }

    /**
     * Executa vibração com o padrão especificado.
     */
    private fun vibrate(pattern: LongArray, repeat: Int) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager =
                    context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                val vibrator = vibratorManager?.defaultVibrator
                val effect = VibrationEffect.createWaveform(pattern, repeat)
                vibrator?.vibrate(effect)
            } else {
                @Suppress("DEPRECATION")
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val effect = VibrationEffect.createWaveform(pattern, repeat)
                    vibrator?.vibrate(effect)
                } else {
                    @Suppress("DEPRECATION")
                    vibrator?.vibrate(pattern, repeat)
                }
            }
        } catch (e: Exception) {
            // Vibração não disponível no dispositivo — ignora silenciosamente
        }
    }

    /**
     * Libera os recursos do SoundPool.
     */
    fun release() {
        soundPool?.release()
        soundPool = null
    }
}
