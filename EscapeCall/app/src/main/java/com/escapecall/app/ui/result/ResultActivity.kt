package com.escapecall.app.ui.result

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.escapecall.app.R
import com.escapecall.app.databinding.ActivityResultBinding
import com.escapecall.app.model.PerformanceRating
import com.escapecall.app.ui.home.MainActivity
import com.escapecall.app.util.AppConstants
import com.escapecall.app.util.animateBounce
import com.escapecall.app.util.animatePopIn
import com.escapecall.app.util.toTimeString

/**
 * Activity da tela de resultado final do EscapeCall.
 *
 * Exibe:
 * - Pontuação total do grupo
 * - Número de enigmas resolvidos
 * - Tempo total gasto
 * - Classificação de performance
 * - Opções de jogar novamente ou sair
 */
class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Recupera os dados do resultado
        val totalScore = intent.getIntExtra(AppConstants.EXTRA_TOTAL_SCORE, 0)
        val puzzlesSolved = intent.getIntExtra(AppConstants.EXTRA_PUZZLES_SOLVED, 0)
        val timeElapsed = intent.getIntExtra(AppConstants.EXTRA_TIME_ELAPSED, 0)
        val roomCode = intent.getStringExtra(AppConstants.EXTRA_ROOM_CODE) ?: ""
        val playerName = intent.getStringExtra(AppConstants.EXTRA_PLAYER_NAME) ?: "Jogador"

        displayResults(totalScore, puzzlesSolved, timeElapsed, roomCode, playerName)
        setupButtons(playerName)
        startEntryAnimations()
    }

    /**
     * Exibe os resultados do jogo na tela.
     */
    private fun displayResults(
        totalScore: Int,
        puzzlesSolved: Int,
        timeElapsed: Int,
        roomCode: String,
        playerName: String
    ) {
        val totalPuzzles = AppConstants.PUZZLE_COUNT_DEFAULT

        // Calcula a classificação de performance
        val completionPct = if (totalPuzzles > 0) (puzzlesSolved * 100) / totalPuzzles else 0
        val rating = when {
            completionPct == 100 && timeElapsed < 300 -> PerformanceRating.LEGENDARY
            completionPct == 100 -> PerformanceRating.EXCELLENT
            completionPct >= 66 -> PerformanceRating.GOOD
            completionPct >= 33 -> PerformanceRating.AVERAGE
            else -> PerformanceRating.NEEDS_IMPROVEMENT
        }

        // Exibe o emoji e título da classificação
        binding.tvRatingEmoji.text = rating.emoji
        binding.tvRatingTitle.text = rating.displayName
        binding.tvRatingDescription.text = rating.description

        // Pontuação total
        binding.tvTotalScore.text = totalScore.toString()
        binding.tvScoreLabel.text = "pontos"

        // Estatísticas detalhadas
        binding.tvPuzzlesSolved.text = "$puzzlesSolved/$totalPuzzles"
        binding.tvTimeElapsed.text = timeElapsed.toTimeString()
        binding.tvPlayerName.text = playerName

        // Barra de progresso de conclusão
        binding.progressCompletion.progress = completionPct
        binding.tvCompletionPercent.text = "$completionPct% concluído"

        // Código da sala
        if (roomCode.isNotEmpty()) {
            binding.tvRoomCodeResult.text = "Sala: $roomCode"
            binding.tvRoomCodeResult.visibility = View.VISIBLE
        }

        // Aplica a cor do rating
        try {
            val color = android.graphics.Color.parseColor(rating.colorHex)
            binding.tvRatingTitle.setTextColor(color)
            binding.cardRating.setCardBackgroundColor(
                ContextCompat.getColor(this, R.color.card_background)
            )
        } catch (e: Exception) {
            // Cor padrão se houver erro
        }

        // Mensagem especial para pontuação máxima
        if (completionPct == 100) {
            binding.tvSpecialMessage.visibility = View.VISIBLE
            binding.tvSpecialMessage.text = "🏆 Todos os enigmas resolvidos! Incrível trabalho em equipe!"
        } else {
            binding.tvSpecialMessage.visibility = View.GONE
        }
    }

    /**
     * Configura os botões de ação da tela de resultado.
     */
    private fun setupButtons(playerName: String) {
        // Jogar novamente
        binding.btnPlayAgain.setOnClickListener {
            binding.btnPlayAgain.animateBounce()
            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in_slow, R.anim.fade_out)
            finish()
        }

        // Compartilhar resultado
        binding.btnShare.setOnClickListener {
            shareResult(playerName)
        }

        // Voltar ao início
        binding.btnHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)
            finish()
        }
    }

    /**
     * Compartilha o resultado do jogo via Intent de compartilhamento.
     */
    private fun shareResult(playerName: String) {
        val totalScore = intent.getIntExtra(AppConstants.EXTRA_TOTAL_SCORE, 0)
        val puzzlesSolved = intent.getIntExtra(AppConstants.EXTRA_PUZZLES_SOLVED, 0)
        val timeElapsed = intent.getIntExtra(AppConstants.EXTRA_TIME_ELAPSED, 0)

        val shareText = """
            🔐 EscapeCall - Resultado do Jogo!
            
            👤 Jogador: $playerName
            ⭐ Pontuação: $totalScore pts
            🧩 Enigmas resolvidos: $puzzlesSolved/${AppConstants.PUZZLE_COUNT_DEFAULT}
            ⏱️ Tempo: ${timeElapsed.toTimeString()}
            
            Baixe o EscapeCall e desafie seus amigos!
            #EscapeCall #EscapeRoom #Videoconferência
        """.trimIndent()

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
            putExtra(Intent.EXTRA_SUBJECT, "Meu resultado no EscapeCall!")
        }
        startActivity(Intent.createChooser(shareIntent, "Compartilhar resultado"))
    }

    /**
     * Inicia as animações de entrada dos elementos da tela.
     */
    private fun startEntryAnimations() {
        binding.cardRating.animatePopIn(0)
        binding.cardScore.animatePopIn(150)
        binding.cardStats.animatePopIn(300)
        binding.layoutButtons.animatePopIn(450)

        // Anima a pontuação com bounce
        binding.root.postDelayed({
            binding.tvTotalScore.animateBounce()
        }, 600)
    }

    override fun onBackPressed() {
        // Impede voltar para o jogo — vai para o início
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
        finish()
    }
}
