package com.escapecall.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escapecall.app.repository.RoomRepository
import kotlinx.coroutines.launch

/**
 * ViewModel da tela inicial do EscapeCall.
 *
 * Gerencia as operações de criação e entrada em salas de jogo,
 * expondo estados observáveis para a UI via LiveData.
 */
class HomeViewModel : ViewModel() {

    private val roomRepository = RoomRepository()

    // ─── Estados de UI ────────────────────────────────────────────────────────

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    /**
     * Triple: (roomCode, playerName, isHost)
     * Emite quando a navegação para o Lobby deve ocorrer.
     */
    private val _navigateToLobby = MutableLiveData<Triple<String, String, Boolean>?>()
    val navigateToLobby: LiveData<Triple<String, String, Boolean>?> = _navigateToLobby

    // ─── Ações do Usuário ─────────────────────────────────────────────────────

    /**
     * Cria uma nova sala de jogo com o nome do jogador fornecido.
     *
     * @param playerName Nome do jogador que será o host da sala
     */
    fun createRoom(playerName: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            roomRepository.createRoom(playerName)
                .onSuccess { room ->
                    _navigateToLobby.value = Triple(room.code, playerName, true)
                }
                .onFailure { error ->
                    _errorMessage.value = error.message ?: "Erro ao criar sala. Tente novamente."
                }

            _isLoading.value = false
        }
    }

    /**
     * Entra em uma sala existente com o código e nome fornecidos.
     *
     * @param roomCode Código de 6 caracteres da sala
     * @param playerName Nome do jogador
     */
    fun joinRoom(roomCode: String, playerName: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            roomRepository.joinRoom(roomCode, playerName)
                .onSuccess { room ->
                    _navigateToLobby.value = Triple(room.code, playerName, false)
                }
                .onFailure { error ->
                    _errorMessage.value = error.message ?: "Sala não encontrada. Verifique o código."
                }

            _isLoading.value = false
        }
    }

    /**
     * Limpa o evento de navegação após ser consumido.
     */
    fun onNavigationComplete() {
        _navigateToLobby.value = null
        _errorMessage.value = null
    }
}
