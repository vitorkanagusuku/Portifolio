package com.escapecall.app.ui.game

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.escapecall.app.R
import com.escapecall.app.databinding.ActivityGameBinding
import com.escapecall.app.model.Puzzle
import com.escapecall.app.ui.result.ResultActivity
import com.escapecall.app.util.AppConstants
import com.escapecall.app.util.SoundManager
import com.escapecall.app.util.animateBounce
import com.escapecall.app.util.animatePopIn
import com.escapecall.app.util.animateShake
import com.escapecall.app.util.showErrorSnackbar
import com.escapecall.app.util.showSuccessSnackbar
import com.escapecall.app.util.toTimeString
import com.escapecall.app.viewmodel.GameViewModel
import org.jitsi.meet.sdk.JitsiMeet
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import org.jitsi.meet.sdk.JitsiMeetUserInfo

/**
 * Activity principal do jogo EscapeCall.
 *
 * Integra:
 * - Videoconferência via Jitsi Meet SDK
 * - Overlay com enigmas interativos
 * - Timer regressivo
 * - Sistema de dicas
 * - Feedback visual de acerto/erro
 */
class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding
    private val viewModel: GameViewModel by viewModels()
    private lateinit var soundManager: SoundManager

    private var countDownTimer: CountDownTimer? = null
    private var isJitsiLaunched = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        soundManager = SoundManager(this)

        // Recupera dados da Intent
        val roomCode = intent.getStringExtra(AppConstants.EXTRA_ROOM_CODE) ?: ""
        val playerName = intent.getStringExtra(AppConstants.EXTRA_PLAYER_NAME) ?: "Jogador"
        val jitsiRoom = intent.getStringExtra(AppConstants.EXTRA_JITSI_ROOM) ?: "escapecall-demo"

        viewModel.initialize(roomCode, playerName)

        setupUI()
        observeViewModel()

        // Inicia a videoconferência Jitsi Meet
        launchJitsiMeet(jitsiRoom, playerName)
    }

    // ─── Configuração da UI ───────────────────────────────────────────────────

    /**
     * Configura todos os elementos de UI e listeners.
     */
    private fun setupUI() {
        // Botão de submeter resposta
        binding.btnSubmitAnswer.setOnClickListener {
            val answer = binding.etAnswer.text.toString().trim()
            if (answer.isNotEmpty()) {
                viewModel.submitAnswer(answer)
                binding.etAnswer.text?.clear()
            } else {
                binding.tilAnswer.error = "Digite sua resposta!"
                binding.etAnswer.animateShake()
            }
        }

        // Botão de dica
        binding.btnHint.setOnClickListener {
            viewModel.requestHint()
        }

        // Botão de toggle da videoconferência
        binding.btnToggleVideo.setOnClickListener {
            toggleVideoOverlay()
        }

        // Botão de sair do jogo
        binding.btnExitGame.setOnClickListener {
            showExitConfirmation()
        }

        // Limpa erro ao digitar
        binding.etAnswer.setOnFocusChangeListener { _, _ ->
            binding.tilAnswer.error = null
        }
    }

    // ─── Observadores do ViewModel ────────────────────────────────────────────

    /**
     * Observa todos os estados do GameViewModel.
     */
    private fun observeViewModel() {
        // Enigma atual
        viewModel.currentPuzzle.observe(this) { puzzle ->
            puzzle?.let { displayPuzzle(it) }
        }

        // Progresso do jogo
        viewModel.gameProgress.observe(this) { (current, total) ->
            binding.tvPuzzleProgress.text = "Enigma $current de $total"
            binding.progressGame.progress = ((current.toFloat() / total) * 100).toInt()
        }

        // Timer
        viewModel.timeRemaining.observe(this) { seconds ->
            updateTimer(seconds)
        }

        // Pontuação
        viewModel.score.observe(this) { score ->
            binding.tvScore.text = "⭐ $score pts"
            binding.tvScore.animateBounce()
        }

        // Dicas disponíveis
        viewModel.hintsRemaining.observe(this) { hints ->
            binding.btnHint.text = "💡 Dica ($hints)"
            binding.btnHint.isEnabled = hints > 0
        }

        // Mensagem de dica
        viewModel.hintMessage.observe(this) { hint ->
            hint?.let {
                showHintDialog(it)
                soundManager.playHint()
                viewModel.onHintShown()
            }
        }

        // Feedback de resposta
        viewModel.answerFeedback.observe(this) { feedback ->
            feedback?.let {
                when (it) {
                    GameViewModel.AnswerFeedback.CORRECT -> showCorrectFeedback()
                    GameViewModel.AnswerFeedback.WRONG -> showWrongFeedback()
                    GameViewModel.AnswerFeedback.GAME_COMPLETE -> navigateToResult()
                }
                viewModel.onFeedbackShown()
            }
        }

        // Tempo esgotado
        viewModel.timeExpired.observe(this) { expired ->
            if (expired == true) {
                showTimeExpiredDialog()
                viewModel.onTimeExpiredHandled()
            }
        }
    }

    // ─── Exibição de Enigmas ──────────────────────────────────────────────────

    /**
     * Exibe o enigma atual na tela com animação.
     */
    private fun displayPuzzle(puzzle: Puzzle) {
        binding.cardPuzzle.animatePopIn()

        binding.tvPuzzleTitle.text = puzzle.title
        binding.tvPuzzleCategory.text = "${puzzle.category.emoji} ${puzzle.category.displayName}"
        binding.tvPuzzleDescription.text = puzzle.description
        binding.tvPuzzlePoints.text = "${puzzle.points} pts"

        // Limpa a resposta anterior
        binding.etAnswer.text?.clear()
        binding.tilAnswer.error = null
        binding.tvFeedback.visibility = View.GONE

        // Inicia o timer para este enigma
        startTimer(AppConstants.GAME_TIME_SECONDS)
    }

    // ─── Timer ────────────────────────────────────────────────────────────────

    /**
     * Inicia o timer regressivo para o enigma atual.
     */
    private fun startTimer(seconds: Int) {
        countDownTimer?.cancel()
        viewModel.startTimer(seconds)

        countDownTimer = object : CountDownTimer(seconds * 1000L, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = (millisUntilFinished / 1000).toInt()
                viewModel.updateTimer(secondsLeft)

                // Som de tick nos últimos 10 segundos
                if (secondsLeft <= 10) {
                    soundManager.playTick()
                }
            }

            override fun onFinish() {
                viewModel.onTimerExpired()
            }
        }.start()
    }

    /**
     * Atualiza a exibição do timer com cores dinâmicas.
     */
    private fun updateTimer(seconds: Int) {
        binding.tvTimer.text = seconds.toTimeString()

        val color = when {
            seconds > 60 -> ContextCompat.getColor(this, R.color.timer_green)
            seconds > 30 -> ContextCompat.getColor(this, R.color.timer_yellow)
            seconds > 10 -> ContextCompat.getColor(this, R.color.timer_orange)
            else -> ContextCompat.getColor(this, R.color.timer_red)
        }
        binding.tvTimer.setTextColor(color)
        binding.timerProgress.setIndicatorColor(color)

        // Anima o timer nos últimos 10 segundos
        if (seconds <= 10 && seconds > 0) {
            val pulse = AnimationUtils.loadAnimation(this, R.anim.pulse)
            binding.tvTimer.startAnimation(pulse)
        }

        // Atualiza a barra de progresso do timer
        val maxTime = AppConstants.GAME_TIME_SECONDS
        binding.timerProgress.progress = ((seconds.toFloat() / maxTime) * 100).toInt()
    }

    // ─── Feedback Visual ──────────────────────────────────────────────────────

    /**
     * Exibe feedback visual e sonoro de resposta correta.
     */
    private fun showCorrectFeedback() {
        soundManager.playCorrect()
        countDownTimer?.cancel()

        binding.tvFeedback.apply {
            text = "✅ CORRETO!"
            setTextColor(ContextCompat.getColor(this@GameActivity, R.color.success_green))
            visibility = View.VISIBLE
            animateBounce()
        }

        binding.cardPuzzle.setCardBackgroundColor(
            ContextCompat.getColor(this, R.color.success_bg)
        )

        binding.root.showSuccessSnackbar("🎉 Resposta correta! +${viewModel.lastScoreEarned} pontos")

        // Avança para o próximo enigma após 1.5 segundos
        binding.root.postDelayed({
            binding.cardPuzzle.setCardBackgroundColor(
                ContextCompat.getColor(this, R.color.card_background)
            )
            viewModel.advanceToNextPuzzle()
        }, 1500)
    }

    /**
     * Exibe feedback visual e sonoro de resposta errada.
     */
    private fun showWrongFeedback() {
        soundManager.playWrong()

        binding.tvFeedback.apply {
            text = "❌ Tente novamente!"
            setTextColor(ContextCompat.getColor(this@GameActivity, R.color.error_red))
            visibility = View.VISIBLE
        }

        binding.cardPuzzle.animateShake()
        binding.root.showErrorSnackbar("Resposta incorreta. Continue tentando!")

        // Esconde o feedback após 2 segundos
        binding.root.postDelayed({
            binding.tvFeedback.visibility = View.GONE
        }, 2000)
    }

    // ─── Jitsi Meet ──────────────────────────────────────────────────────────

    /**
     * Lança a videoconferência Jitsi Meet.
     * O Jitsi abre em uma Activity separada que pode ser minimizada.
     */
    private fun launchJitsiMeet(roomName: String, displayName: String) {
        try {
            // Configura as opções da conferência
            val serverUrl = Uri.parse(AppConstants.JITSI_SERVER_URL)

            // Informações do usuário
            val userInfo = JitsiMeetUserInfo().apply {
                this.displayName = displayName
                // Avatar pode ser configurado com uma URL de imagem
            }

            // Opções da conferência
            val options = JitsiMeetConferenceOptions.Builder()
                .setServerURL(serverUrl)
                .setRoom(roomName)
                .setUserInfo(userInfo)
                .setAudioMuted(false)
                .setVideoMuted(false)
                .setAudioOnly(false)
                .setFeatureFlag("add-people.enabled", false)
                .setFeatureFlag("calendar.enabled", false)
                .setFeatureFlag("call-integration.enabled", false)
                .setFeatureFlag("car-mode.enabled", false)
                .setFeatureFlag("close-captions.enabled", false)
                .setFeatureFlag("conference-timer.enabled", true)
                .setFeatureFlag("chat.enabled", true)
                .setFeatureFlag("filmstrip.enabled", true)
                .setFeatureFlag("fullscreen.enabled", true)
                .setFeatureFlag("help.enabled", false)
                .setFeatureFlag("invite.enabled", false)
                .setFeatureFlag("kick-out.enabled", false)
                .setFeatureFlag("live-streaming.enabled", false)
                .setFeatureFlag("meeting-name.enabled", true)
                .setFeatureFlag("meeting-password.enabled", false)
                .setFeatureFlag("notifications.enabled", false)
                .setFeatureFlag("overflow-menu.enabled", true)
                .setFeatureFlag("pip.enabled", true)
                .setFeatureFlag("raise-hand.enabled", true)
                .setFeatureFlag("recording.enabled", false)
                .setFeatureFlag("reactions.enabled", true)
                .setFeatureFlag("security-options.enabled", false)
                .setFeatureFlag("server-url-change.enabled", false)
                .setFeatureFlag("settings.enabled", false)
                .setFeatureFlag("tile-view.enabled", true)
                .setFeatureFlag("toolbox.alwaysVisible", false)
                .setFeatureFlag("video-share.enabled", false)
                .setFeatureFlag("welcomepage.enabled", false)
                .build()

            // Lança a Activity do Jitsi
            JitsiMeetActivity.launch(this, options)
            isJitsiLaunched = true

            // Mostra instrução ao usuário
            Toast.makeText(
                this,
                "📹 Videoconferência iniciada! Use o botão de minimizar para voltar ao jogo.",
                Toast.LENGTH_LONG
            ).show()

        } catch (e: Exception) {
            // Fallback: exibe mensagem de erro mas continua o jogo
            Toast.makeText(
                this,
                "⚠️ Não foi possível iniciar a videoconferência. O jogo continua normalmente.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    /**
     * Alterna a visibilidade do painel de videoconferência.
     */
    private fun toggleVideoOverlay() {
        if (!isJitsiLaunched) {
            val jitsiRoom = intent.getStringExtra(AppConstants.EXTRA_JITSI_ROOM) ?: "escapecall-demo"
            val playerName = intent.getStringExtra(AppConstants.EXTRA_PLAYER_NAME) ?: "Jogador"
            launchJitsiMeet(jitsiRoom, playerName)
        } else {
            Toast.makeText(this, "A videoconferência está ativa em segundo plano.", Toast.LENGTH_SHORT).show()
        }
    }

    // ─── Diálogos ─────────────────────────────────────────────────────────────

    /**
     * Exibe o diálogo com a dica do enigma atual.
     */
    private fun showHintDialog(hint: String) {
        AlertDialog.Builder(this, R.style.Theme_EscapeCall_Dialog)
            .setTitle("💡 Dica")
            .setMessage(hint)
            .setPositiveButton("Entendido!") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    /**
     * Exibe o diálogo de confirmação para sair do jogo.
     */
    private fun showExitConfirmation() {
        AlertDialog.Builder(this, R.style.Theme_EscapeCall_Dialog)
            .setTitle("⚠️ Sair do Jogo?")
            .setMessage("Se você sair agora, o progresso do grupo será perdido. Tem certeza?")
            .setPositiveButton("Sair") { _, _ ->
                countDownTimer?.cancel()
                finish()
            }
            .setNegativeButton("Continuar Jogando") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    /**
     * Exibe o diálogo quando o tempo se esgota.
     */
    private fun showTimeExpiredDialog() {
        soundManager.playGameOver()
        AlertDialog.Builder(this, R.style.Theme_EscapeCall_Dialog)
            .setTitle("⏰ Tempo Esgotado!")
            .setMessage("O tempo para este enigma acabou. Avançando para o próximo...")
            .setPositiveButton("OK") { _, _ ->
                viewModel.advanceToNextPuzzle()
            }
            .setCancelable(false)
            .show()
    }

    // ─── Navegação ────────────────────────────────────────────────────────────

    /**
     * Navega para a tela de resultado final.
     */
    private fun navigateToResult() {
        soundManager.playVictory()
        countDownTimer?.cancel()

        val intent = Intent(this, ResultActivity::class.java).apply {
            putExtra(AppConstants.EXTRA_TOTAL_SCORE, viewModel.score.value ?: 0)
            putExtra(AppConstants.EXTRA_PUZZLES_SOLVED, viewModel.puzzlesSolved)
            putExtra(AppConstants.EXTRA_TIME_ELAPSED, viewModel.timeElapsed)
            putExtra(AppConstants.EXTRA_ROOM_CODE, intent.getStringExtra(AppConstants.EXTRA_ROOM_CODE))
            putExtra(AppConstants.EXTRA_PLAYER_NAME, intent.getStringExtra(AppConstants.EXTRA_PLAYER_NAME))
        }
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        finish()
    }

    // ─── Ciclo de Vida ────────────────────────────────────────────────────────

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
        soundManager.release()
    }

    override fun onBackPressed() {
        showExitConfirmation()
    }
}
