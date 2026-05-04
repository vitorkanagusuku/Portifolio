package com.escapecall.app.model

/**
 * Representa uma sala de jogo EscapeCall.
 *
 * @param id Identificador único da sala
 * @param code Código de 6 caracteres para entrar na sala
 * @param hostName Nome do jogador que criou a sala
 * @param players Lista de jogadores na sala
 * @param maxPlayers Número máximo de jogadores
 * @param status Status atual da sala
 * @param jitsiRoomName Nome da sala no Jitsi Meet
 * @param currentPuzzleIndex Índice do enigma atual
 * @param totalPuzzles Total de enigmas na sessão
 */
data class Room(
    val id: String,
    val code: String,
    val hostName: String,
    val players: MutableList<Player> = mutableListOf(),
    val maxPlayers: Int = 6,
    val status: RoomStatus = RoomStatus.WAITING,
    val jitsiRoomName: String = "escapecall-$code",
    val currentPuzzleIndex: Int = 0,
    val totalPuzzles: Int = 3,
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * Status possíveis de uma sala de jogo.
 */
enum class RoomStatus(val displayName: String) {
    WAITING("Aguardando jogadores"),
    STARTING("Iniciando..."),
    IN_GAME("Em jogo"),
    FINISHED("Finalizado"),
    ABANDONED("Abandonado")
}

/**
 * Representa um jogador na sala.
 *
 * @param id Identificador único do jogador
 * @param name Nome de exibição
 * @param isHost Se é o criador da sala
 * @param isReady Se está pronto para iniciar
 * @param score Pontuação acumulada
 * @param avatarIndex Índice do avatar selecionado
 */
data class Player(
    val id: String,
    val name: String,
    val isHost: Boolean = false,
    var isReady: Boolean = false,
    var score: Int = 0,
    val avatarIndex: Int = 0,
    val joinedAt: Long = System.currentTimeMillis()
)

/**
 * Resultado final de uma sessão de jogo.
 *
 * @param roomCode Código da sala
 * @param players Jogadores com pontuações finais
 * @param puzzlesSolved Enigmas resolvidos
 * @param totalPuzzles Total de enigmas
 * @param timeElapsedSeconds Tempo total gasto em segundos
 * @param completedAt Timestamp de conclusão
 */
data class GameResult(
    val roomCode: String,
    val players: List<Player>,
    val puzzlesSolved: Int,
    val totalPuzzles: Int,
    val timeElapsedSeconds: Int,
    val completedAt: Long = System.currentTimeMillis()
) {
    /**
     * Calcula a pontuação total do grupo.
     */
    fun totalGroupScore(): Int = players.sumOf { it.score }

    /**
     * Retorna o jogador com maior pontuação.
     */
    fun mvpPlayer(): Player? = players.maxByOrNull { it.score }

    /**
     * Calcula a porcentagem de conclusão.
     */
    fun completionPercentage(): Int =
        if (totalPuzzles > 0) (puzzlesSolved * 100) / totalPuzzles else 0

    /**
     * Retorna a classificação da performance do grupo.
     */
    fun performanceRating(): PerformanceRating {
        val pct = completionPercentage()
        return when {
            pct == 100 && timeElapsedSeconds < 300 -> PerformanceRating.LEGENDARY
            pct == 100 -> PerformanceRating.EXCELLENT
            pct >= 66 -> PerformanceRating.GOOD
            pct >= 33 -> PerformanceRating.AVERAGE
            else -> PerformanceRating.NEEDS_IMPROVEMENT
        }
    }
}

/**
 * Classificação de performance do grupo ao final do jogo.
 */
enum class PerformanceRating(
    val displayName: String,
    val emoji: String,
    val description: String,
    val colorHex: String
) {
    LEGENDARY("Lendário!", "🏆", "Vocês são incríveis! Tempo recorde!", "#FFD700"),
    EXCELLENT("Excelente!", "⭐", "Parabéns! Todos os enigmas resolvidos!", "#4CAF50"),
    GOOD("Muito Bom!", "👍", "Ótimo trabalho em equipe!", "#2196F3"),
    AVERAGE("Razoável", "🤔", "Podem melhorar com mais prática!", "#FF9800"),
    NEEDS_IMPROVEMENT("Continue tentando", "💪", "A prática leva à perfeição!", "#F44336")
}
