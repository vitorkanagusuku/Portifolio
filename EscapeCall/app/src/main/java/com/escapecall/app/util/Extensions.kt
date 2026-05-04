package com.escapecall.app.util

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.BounceInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import java.util.Locale

/**
 * Extensões e utilitários reutilizáveis para o EscapeCall.
 */

// ─── Extensões de View ────────────────────────────────────────────────────────

/**
 * Anima a entrada de uma view com efeito de "pop" (escala + fade).
 */
fun View.animatePopIn(delay: Long = 0) {
    alpha = 0f
    scaleX = 0.5f
    scaleY = 0.5f
    animate()
        .alpha(1f)
        .scaleX(1f)
        .scaleY(1f)
        .setStartDelay(delay)
        .setDuration(400)
        .setInterpolator(OvershootInterpolator(1.5f))
        .start()
}

/**
 * Anima a saída de uma view com efeito de "pop out".
 */
fun View.animatePopOut(onEnd: (() -> Unit)? = null) {
    animate()
        .alpha(0f)
        .scaleX(0.5f)
        .scaleY(0.5f)
        .setDuration(250)
        .setInterpolator(AccelerateDecelerateInterpolator())
        .withEndAction { onEnd?.invoke() }
        .start()
}

/**
 * Anima um shake horizontal (erro).
 */
fun View.animateShake() {
    val shake = ObjectAnimator.ofFloat(this, "translationX",
        0f, -20f, 20f, -15f, 15f, -10f, 10f, -5f, 5f, 0f)
    shake.duration = 600
    shake.start()
}

/**
 * Anima um bounce (sucesso).
 */
fun View.animateBounce() {
    val scaleX = ObjectAnimator.ofFloat(this, "scaleX", 1f, 1.2f, 0.9f, 1.05f, 1f)
    val scaleY = ObjectAnimator.ofFloat(this, "scaleY", 1f, 1.2f, 0.9f, 1.05f, 1f)
    val set = AnimatorSet()
    set.playTogether(scaleX, scaleY)
    set.duration = 500
    set.interpolator = BounceInterpolator()
    set.start()
}

/**
 * Pulsa a view continuamente (para chamar atenção).
 */
fun View.animatePulse() {
    animate()
        .scaleX(1.05f)
        .scaleY(1.05f)
        .setDuration(600)
        .withEndAction {
            animate()
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(600)
                .withEndAction { animatePulse() }
                .start()
        }
        .start()
}

/**
 * Para animação de pulso.
 */
fun View.stopPulse() {
    animate().cancel()
    scaleX = 1f
    scaleY = 1f
}

// ─── Extensões de Tempo ───────────────────────────────────────────────────────

/**
 * Converte segundos para formato MM:SS.
 */
fun Int.toTimeString(): String {
    val minutes = this / 60
    val seconds = this % 60
    return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
}

/**
 * Converte milissegundos para formato MM:SS.
 */
fun Long.toTimeString(): String = (this / 1000).toInt().toTimeString()

// ─── Extensões de Context ─────────────────────────────────────────────────────

/**
 * Exibe um Snackbar estilizado de sucesso.
 */
fun View.showSuccessSnackbar(message: String) {
    val snackbar = Snackbar.make(this, message, Snackbar.LENGTH_SHORT)
    snackbar.setBackgroundTint(ContextCompat.getColor(context, android.R.color.holo_green_dark))
    snackbar.setTextColor(ContextCompat.getColor(context, android.R.color.white))
    snackbar.show()
}

/**
 * Exibe um Snackbar estilizado de erro.
 */
fun View.showErrorSnackbar(message: String) {
    val snackbar = Snackbar.make(this, message, Snackbar.LENGTH_SHORT)
    snackbar.setBackgroundTint(ContextCompat.getColor(context, android.R.color.holo_red_dark))
    snackbar.setTextColor(ContextCompat.getColor(context, android.R.color.white))
    snackbar.show()
}

// ─── Constantes do App ────────────────────────────────────────────────────────

object AppConstants {
    const val JITSI_SERVER_URL = "https://meet.jit.si"
    const val GAME_TIME_SECONDS = 300  // 5 minutos por enigma
    const val MAX_HINTS_PER_PUZZLE = 3
    const val HINT_PENALTY_POINTS = 15
    const val WRONG_ANSWER_PENALTY = 10
    const val MIN_PLAYERS_TO_START = 1
    const val MAX_PLAYERS_PER_ROOM = 6
    const val PUZZLE_COUNT_DEFAULT = 3

    // Extras para Intents
    const val EXTRA_ROOM_CODE = "extra_room_code"
    const val EXTRA_PLAYER_NAME = "extra_player_name"
    const val EXTRA_IS_HOST = "extra_is_host"
    const val EXTRA_JITSI_ROOM = "extra_jitsi_room"
    const val EXTRA_GAME_RESULT = "extra_game_result"
    const val EXTRA_TOTAL_SCORE = "extra_total_score"
    const val EXTRA_PUZZLES_SOLVED = "extra_puzzles_solved"
    const val EXTRA_TIME_ELAPSED = "extra_time_elapsed"
}
