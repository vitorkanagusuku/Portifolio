package com.escapecall.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.escapecall.app.data.PuzzleRepository
import com.escapecall.app.model.Puzzle
import com.escapecall.app.model.PuzzleState
import com.escapecall.app.util.AppConstants

/**
 * ViewModel da tela de jogo do EscapeCall.
 *
 * Gerencia toda a lógica de jogo:
 * - Sequência de enigmas
 * - Timer regressivo
 * - Verificação de respostas
 * - Sistema de dicas
 * - Cálculo de pontuação
 */
class GameViewModel : ViewModel() {

    // ─── Estado do Jogo ───────────────────────────────────────────────────────

    private var puzzles: List<Puzzle> = emptyList()
    private var currentPuzzleIndex = 0
    private var currentPuzzleState: PuzzleState? = null
    private var currentTimeRemaining = AppConstants.GAME_TIME_SECONDS
    private var totalScore = 0
    var puzzlesSolved = 0
        private set
    var timeElapsed = 0
        private set
    var lastScoreEarned = 0
        private set

    private var roomCode: String = ""
    private var playerName: String = ""

    // ─── LiveData ─────────────────────────────────────────────────────────────

    private val _currentPuzzle = MutableLiveData<Puzzle?>()
    val currentPuzzle: LiveData<Puzzle?> = _currentPuzzle

    private val _gameProgress = MutableLiveData(Pair(1, AppConstants.PUZZLE_COUNT_DEFAULT))
    val gameProgress: LiveData<Pair<Int, Int>> = _gameProgress

    private val _timeRemaining = MutableLiveData(AppConstants.GAME_TIME_SECONDS)
    val timeRemaining: LiveData<Int> = _timeRemaining

    private val _score = MutableLiveData(0)
    val score: LiveData<Int> = _score

    private val _hintsRemaining = MutableLiveData(AppConstants.MAX_HINTS_PER_PUZZLE)
    val hintsRemaining: LiveData<Int> = _hintsRemaining

    private val _hintMessage = MutableLiveData<String?>()
    val hintMessage: LiveData<String?> = _hintMessage

    private val _answerFeedback = MutableLiveData<AnswerFeedback?>()
    val answerFeedback: LiveData<AnswerFeedback?> = _answerFeedback

    private val _timeExpired = MutableLiveData<Boolean?>()
    val timeExpired: LiveData<Boolean?> = _timeExpired

    // ─── Inicialização ────────────────────────────────────────────────────────

    /**
     * Inicializa o jogo com os dados da sala.
     */
    fun initialize(roomCode: String, playerName: String) {
        this.roomCode = roomCode
        this.playerName = playerName

        // Carrega os enigmas padrão para a sessão
        puzzles = PuzzleRepository.getDefaultPuzzles()
        currentPuzzleIndex = 0

        loadCurrentPuzzle()
    }

    /**
     * Carrega e exibe o enigma atual.
     */
    private fun loadCurrentPuzzle() {
        if (currentPuzzleIndex >= puzzles.size) {
            // Todos os enigmas foram completados
            _answerFeedback.value = AnswerFeedback.GAME_COMPLETE
            return
        }

        val puzzle = puzzles[currentPuzzleIndex]
        currentPuzzleState = PuzzleState(puzzle)
        _currentPuzzle.value = puzzle
        _gameProgress.value = Pair(currentPuzzleIndex + 1, puzzles.size)
        _hintsRemaining.value = AppConstants.MAX_HINTS_PER_PUZZLE
        currentTimeRemaining = AppConstants.GAME_TIME_SECONDS
    }

    // ─── Ações do Jogador ─────────────────────────────────────────────────────

    /**
     * Processa a resposta enviada pelo jogador.
     *
     * @param answer Texto da resposta digitada pelo usuário
     */
    fun submitAnswer(answer: String) {
        val puzzle = _currentPuzzle.value ?: return
        val state = currentPuzzleState ?: return

        // Incrementa o contador de tentativas
        currentPuzzleState = state.copy(attempts = state.attempts + 1)

        if (PuzzleRepository.checkAnswer(puzzle, answer)) {
            // Resposta correta!
            val hintsUsed = AppConstants.MAX_HINTS_PER_PUZZLE - (_hintsRemaining.value ?: 0)
            val updatedState = currentPuzzleState!!.copy(
                isSolved = true,
                hintsUsed = hintsUsed
            )

            // Calcula a pontuação
            val earned = updatedState.calculateScore(currentTimeRemaining)
            lastScoreEarned = earned
            totalScore += earned
            puzzlesSolved++

            _score.value = totalScore
            _answerFeedback.value = AnswerFeedback.CORRECT

        } else {
            // Resposta errada
            _answerFeedback.value = AnswerFeedback.WRONG
        }
    }

    /**
     * Solicita uma dica para o enigma atual.
     */
    fun requestHint() {
        val puzzle = _currentPuzzle.value ?: return
        val state = currentPuzzleState ?: return

        if (!state.hasHintsAvailable()) {
            _hintMessage.value = "Não há mais dicas disponíveis para este enigma!"
            return
        }

        val hint = state.getNextHint()
        currentPuzzleState = state.copy(hintsUsed = state.hintsUsed + 1)

        val newHintsRemaining = (_hintsRemaining.value ?: 0) - 1
        _hintsRemaining.value = newHintsRemaining
        _hintMessage.value = hint
    }

    /**
     * Avança para o próximo enigma da sequência.
     */
    fun advanceToNextPuzzle() {
        currentPuzzleIndex++

        // Acumula o tempo gasto
        timeElapsed += AppConstants.GAME_TIME_SECONDS - currentTimeRemaining

        loadCurrentPuzzle()
    }

    // ─── Timer ────────────────────────────────────────────────────────────────

    /**
     * Inicia o timer com o tempo especificado.
     */
    fun startTimer(seconds: Int) {
        currentTimeRemaining = seconds
        _timeRemaining.value = seconds
    }

    /**
     * Atualiza o timer com o tempo restante atual.
     */
    fun updateTimer(seconds: Int) {
        currentTimeRemaining = seconds
        _timeRemaining.value = seconds
    }

    /**
     * Chamado quando o timer expira.
     */
    fun onTimerExpired() {
        timeElapsed += AppConstants.GAME_TIME_SECONDS
        _timeExpired.value = true
    }

    // ─── Limpeza de Eventos ───────────────────────────────────────────────────

    fun onHintShown() {
        _hintMessage.value = null
    }

    fun onFeedbackShown() {
        _answerFeedback.value = null
    }

    fun onTimeExpiredHandled() {
        _timeExpired.value = null
    }

    // ─── Enums ────────────────────────────────────────────────────────────────

    /**
     * Tipos de feedback de resposta.
     */
    enum class AnswerFeedback {
        CORRECT,
        WRONG,
        GAME_COMPLETE
    }
}
