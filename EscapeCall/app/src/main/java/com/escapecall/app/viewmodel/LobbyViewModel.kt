package com.escapecall.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escapecall.app.model.Player
import com.escapecall.app.repository.RoomRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.random.Random

/**
 * ViewModel do lobby de espera do EscapeCall.
 *
 * Gerencia o estado dos jogadores na sala, status de prontidão
 * e o início do jogo.
 */
class LobbyViewModel : ViewModel() {

    private val roomRepository = RoomRepository()

    private var roomCode: String = ""
    private var playerName: String = ""
    private var isHost: Boolean = false
    private var currentPlayerId: String = UUID.randomUUID().toString()

    // ─── Estados de UI ────────────────────────────────────────────────────────

    private val _players = MutableLiveData<MutableList<Player>>(mutableListOf())
    val players: LiveData<MutableList<Player>> = _players

    private val _isReady = MutableLiveData(false)
    val isReady: LiveData<Boolean> = _isReady

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    /**
     * Triple: (roomCode, playerName, jitsiRoomName)
     */
    private val _navigateToGame = MutableLiveData<Triple<String, String, String>?>()
    val navigateToGame: LiveData<Triple<String, String, String>?> = _navigateToGame

    // ─── Inicialização ────────────────────────────────────────────────────────

    /**
     * Inicializa o ViewModel com os dados da sala.
     */
    fun initialize(roomCode: String, playerName: String, isHost: Boolean) {
        this.roomCode = roomCode
        this.playerName = playerName
        this.isHost = isHost

        // Adiciona o jogador atual à lista
        val currentPlayer = Player(
            id = currentPlayerId,
            name = playerName,
            isHost = isHost,
            isReady = isHost, // Host já começa como pronto
            avatarIndex = Random.nextInt(0, 8)
        )

        val playerList = mutableListOf(currentPlayer)

        // Simula outros jogadores já na sala (para demonstração)
        if (!isHost) {
            playerList.add(
                0, Player(
                    id = UUID.randomUUID().toString(),
                    name = "Host da Sala",
                    isHost = true,
                    isReady = true,
                    avatarIndex = Random.nextInt(0, 8)
                )
            )
        }

        _players.value = playerList

        // Simula chegada de jogadores adicionais após alguns segundos
        simulatePlayersJoining()
    }

    /**
     * Alterna o status de prontidão do jogador atual.
     */
    fun toggleReady() {
        val newReadyState = !(_isReady.value ?: false)
        _isReady.value = newReadyState

        // Atualiza o status na lista de jogadores
        val currentPlayers = _players.value ?: return
        currentPlayers.find { it.id == currentPlayerId }?.isReady = newReadyState
        _players.value = currentPlayers
    }

    /**
     * Inicia o jogo (apenas o host pode chamar esta função).
     */
    fun startGame() {
        viewModelScope.launch {
            _isLoading.value = true
            delay(800) // Simula comunicação com servidor

            val jitsiRoomName = "escapecall-${roomCode.lowercase()}"
            _navigateToGame.value = Triple(roomCode, playerName, jitsiRoomName)

            _isLoading.value = false
        }
    }

    /**
     * Limpa o evento de navegação após ser consumido.
     */
    fun onNavigationComplete() {
        _navigateToGame.value = null
    }

    /**
     * Simula a chegada de jogadores adicionais para demonstração.
     * Em produção, isso seria via WebSocket ou Firebase Realtime Database.
     */
    private fun simulatePlayersJoining() {
        if (!isHost) return // Apenas simula para o host

        val simulatedPlayers = listOf(
            "Ana Lima" to 2,
            "Carlos Dev" to 5,
            "Beatriz S." to 1
        )

        simulatedPlayers.forEachIndexed { index, (name, avatar) ->
            viewModelScope.launch {
                delay((2000 + index * 1500).toLong())
                val currentPlayers = _players.value ?: mutableListOf()
                if (currentPlayers.size < 6) {
                    currentPlayers.add(
                        Player(
                            id = UUID.randomUUID().toString(),
                            name = name,
                            isHost = false,
                            isReady = false,
                            avatarIndex = avatar
                        )
                    )
                    _players.value = currentPlayers

                    // Simula que o jogador fica pronto após alguns segundos
                    delay(2000)
                    currentPlayers.last().isReady = true
                    _players.value = currentPlayers
                }
            }
        }
    }
}
