package com.escapecall.app.model

/**
 * Representa um enigma/desafio do jogo EscapeCall.
 *
 * @param id Identificador único do enigma
 * @param title Título exibido na tela
 * @param description Descrição completa do enigma
 * @param answer Resposta correta (case-insensitive)
 * @param hints Lista de dicas disponíveis
 * @param points Pontuação base ao resolver corretamente
 * @param category Categoria do enigma (LOGIC, TEXT, MATH, RIDDLE)
 * @param imageRes Recurso de imagem opcional para o enigma
 */
data class Puzzle(
    val id: Int,
    val title: String,
    val description: String,
    val answer: String,
    val hints: List<String>,
    val points: Int = 100,
    val category: PuzzleCategory = PuzzleCategory.LOGIC,
    val imageRes: Int? = null
)

/**
 * Categorias de enigmas disponíveis no jogo.
 */
enum class PuzzleCategory(val displayName: String, val emoji: String) {
    LOGIC("Lógica", "🧠"),
    TEXT("Texto", "📝"),
    MATH("Matemática", "🔢"),
    RIDDLE("Adivinha", "🎭"),
    CIPHER("Cifra", "🔐")
}

/**
 * Estado atual de um enigma durante o jogo.
 */
data class PuzzleState(
    val puzzle: Puzzle,
    val hintsUsed: Int = 0,
    val isSolved: Boolean = false,
    val attempts: Int = 0,
    val timeSpent: Long = 0L
) {
    /**
     * Calcula a pontuação final com base no tempo, dicas e tentativas.
     */
    fun calculateScore(timeRemainingSeconds: Int): Int {
        if (!isSolved) return 0
        val basePoints = puzzle.points
        val hintPenalty = hintsUsed * 15
        val attemptPenalty = maxOf(0, (attempts - 1) * 10)
        val timeBonus = (timeRemainingSeconds / 10) * 5
        return maxOf(10, basePoints - hintPenalty - attemptPenalty + timeBonus)
    }

    /**
     * Verifica se ainda há dicas disponíveis.
     */
    fun hasHintsAvailable(): Boolean = hintsUsed < puzzle.hints.size

    /**
     * Retorna a próxima dica disponível.
     */
    fun getNextHint(): String? {
        return if (hasHintsAvailable()) puzzle.hints[hintsUsed] else null
    }
}
