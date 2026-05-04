package com.escapecall.app.repository

import com.escapecall.app.model.Player
import com.escapecall.app.model.Room
import com.escapecall.app.model.RoomStatus
import kotlinx.coroutines.delay
import java.util.UUID
import kotlin.random.Random

/**
 * Repositório responsável por gerenciar salas de jogo.
 *
 * Em produção, este repositório se comunicaria com um backend (Firebase, WebSocket, etc.).
 * Para fins de demonstração, simula operações assíncronas localmente.
 */
class RoomRepository {

    // Simulação de banco de dados em memória
    private val rooms = mutableMapOf<String, Room>()

    /**
     * Cria uma nova sala de jogo.
     *
     * @param hostName Nome do jogador que está criando a sala
     * @return A sala criada com código único
     */
    suspend fun createRoom(hostName: String): Result<Room> {
        return try {
            delay(500) // Simula latência de rede

            val roomCode = generateRoomCode()
            val hostId = UUID.randomUUID().toString()
            val host = Player(
                id = hostId,
                name = hostName,
                isHost = true,
                isReady = true,
                avatarIndex = Random.nextInt(0, 8)
            )

            val room = Room(
                id = UUID.randomUUID().toString(),
                code = roomCode,
                hostName = hostName,
                jitsiRoomName = "escapecall-${roomCode.lowercase()}",
                players = mutableListOf(host)
            )

            rooms[roomCode] = room
            Result.success(room)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Entra em uma sala existente com o código fornecido.
     *
     * @param roomCode Código de 6 caracteres da sala
     * @param playerName Nome do jogador que está entrando
     * @return A sala atualizada com o novo jogador
     */
    suspend fun joinRoom(roomCode: String, playerName: String): Result<Room> {
        return try {
            delay(600) // Simula latência de rede

            val code = roomCode.uppercase().trim()

            // Para demonstração: se a sala não existe, cria uma simulada
            val room = rooms[code] ?: createSimulatedRoom(code)

            if (room.status != RoomStatus.WAITING) {
                return Result.failure(Exception("Esta sala já está em jogo ou foi encerrada."))
            }

            if (room.players.size >= room.maxPlayers) {
                return Result.failure(Exception("Sala cheia! Máximo de ${room.maxPlayers} jogadores."))
            }

            val newPlayer = Player(
                id = UUID.randomUUID().toString(),
                name = playerName,
                isHost = false,
                isReady = false,
                avatarIndex = Random.nextInt(0, 8)
            )

            room.players.add(newPlayer)
            rooms[code] = room

            Result.success(room)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Atualiza o status de pronto de um jogador.
     */
    suspend fun setPlayerReady(roomCode: String, playerId: String, isReady: Boolean): Result<Room> {
        return try {
            delay(200)
            val room = rooms[roomCode.uppercase()]
                ?: return Result.failure(Exception("Sala não encontrada."))

            room.players.find { it.id == playerId }?.isReady = isReady
            Result.success(room)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Inicia o jogo em uma sala (apenas o host pode fazer isso).
     */
    suspend fun startGame(roomCode: String): Result<Room> {
        return try {
            delay(300)
            val room = rooms[roomCode.uppercase()]
                ?: return Result.failure(Exception("Sala não encontrada."))

            val updatedRoom = room.copy(status = RoomStatus.IN_GAME)
            rooms[roomCode.uppercase()] = updatedRoom
            Result.success(updatedRoom)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Gera um código único de 6 caracteres alfanuméricos para a sala.
     */
    private fun generateRoomCode(): String {
        val chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"
        var code: String
        do {
            code = (1..6).map { chars.random() }.joinToString("")
        } while (rooms.containsKey(code))
        return code
    }

    /**
     * Cria uma sala simulada para demonstração quando o código não existe localmente.
     * Em produção, isso buscaria no servidor remoto.
     */
    private fun createSimulatedRoom(code: String): Room {
        val room = Room(
            id = UUID.randomUUID().toString(),
            code = code,
            hostName = "Jogador Remoto",
            jitsiRoomName = "escapecall-${code.lowercase()}",
            players = mutableListOf(
                Player(
                    id = UUID.randomUUID().toString(),
                    name = "Jogador Remoto",
                    isHost = true,
                    isReady = true
                )
            )
        )
        rooms[code] = room
        return room
    }
}
