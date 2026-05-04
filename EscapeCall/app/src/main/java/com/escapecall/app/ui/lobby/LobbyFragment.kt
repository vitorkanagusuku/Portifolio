package com.escapecall.app.ui.lobby

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.escapecall.app.databinding.FragmentLobbyBinding
import com.escapecall.app.ui.game.GameActivity
import com.escapecall.app.util.AppConstants
import com.escapecall.app.util.animateBounce
import com.escapecall.app.util.animatePopIn
import com.escapecall.app.viewmodel.LobbyViewModel

/**
 * Fragment do lobby de espera do EscapeCall.
 *
 * Exibe:
 * - Código da sala para compartilhar
 * - Lista de jogadores conectados
 * - Botão de iniciar (apenas para o host)
 * - Status de prontidão de cada jogador
 */
class LobbyFragment : Fragment() {

    private var _binding: FragmentLobbyBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LobbyViewModel by viewModels()
    private lateinit var playersAdapter: PlayersAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLobbyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Recupera os dados passados pelo HomeFragment
        val roomCode = arguments?.getString(AppConstants.EXTRA_ROOM_CODE) ?: ""
        val playerName = arguments?.getString(AppConstants.EXTRA_PLAYER_NAME) ?: ""
        val isHost = arguments?.getBoolean(AppConstants.EXTRA_IS_HOST) ?: false

        viewModel.initialize(roomCode, playerName, isHost)

        setupRecyclerView()
        setupUI(roomCode, isHost)
        observeViewModel()
        startEntryAnimations()
    }

    /**
     * Configura o RecyclerView de jogadores.
     */
    private fun setupRecyclerView() {
        playersAdapter = PlayersAdapter()
        binding.rvPlayers.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = playersAdapter
        }
    }

    /**
     * Configura os elementos de UI.
     */
    private fun setupUI(roomCode: String, isHost: Boolean) {
        // Exibe o código da sala com formatação
        binding.tvRoomCode.text = roomCode
        binding.tvRoomCodeLabel.text = if (isHost) "Compartilhe este código:" else "Sala:"

        // Botão de copiar código
        binding.btnCopyCode.setOnClickListener {
            val clipboard = requireContext().getSystemService(android.content.Context.CLIPBOARD_SERVICE)
                    as android.content.ClipboardManager
            val clip = android.content.ClipData.newPlainText("Código EscapeCall", roomCode)
            clipboard.setPrimaryClip(clip)
            binding.btnCopyCode.animateBounce()
            com.google.android.material.snackbar.Snackbar
                .make(binding.root, "Código copiado! 📋", com.google.android.material.snackbar.Snackbar.LENGTH_SHORT)
                .show()
        }

        // Configura o botão de iniciar (apenas para o host)
        if (isHost) {
            binding.btnStartGame.visibility = View.VISIBLE
            binding.tvWaitingForHost.visibility = View.GONE
            binding.btnStartGame.setOnClickListener {
                viewModel.startGame()
            }
        } else {
            binding.btnStartGame.visibility = View.GONE
            binding.tvWaitingForHost.visibility = View.VISIBLE
        }

        // Botão de pronto
        binding.btnReady.setOnClickListener {
            viewModel.toggleReady()
        }
    }

    /**
     * Observa os estados do ViewModel.
     */
    private fun observeViewModel() {
        viewModel.players.observe(viewLifecycleOwner) { players ->
            playersAdapter.submitList(players.toList())
            binding.tvPlayerCount.text = "${players.size}/6 jogadores"

            // Habilita o botão de iniciar se houver pelo menos 1 jogador
            binding.btnStartGame.isEnabled = players.isNotEmpty()
        }

        viewModel.isReady.observe(viewLifecycleOwner) { isReady ->
            binding.btnReady.text = if (isReady) "✓ Pronto!" else "Marcar como Pronto"
            binding.btnReady.isSelected = isReady
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.btnStartGame.isEnabled = !isLoading
        }

        viewModel.navigateToGame.observe(viewLifecycleOwner) { gameData ->
            gameData?.let { (roomCode, playerName, jitsiRoom) ->
                navigateToGame(roomCode, playerName, jitsiRoom)
                viewModel.onNavigationComplete()
            }
        }
    }

    /**
     * Navega para a GameActivity com os dados necessários.
     */
    private fun navigateToGame(roomCode: String, playerName: String, jitsiRoom: String) {
        val intent = Intent(requireContext(), GameActivity::class.java).apply {
            putExtra(AppConstants.EXTRA_ROOM_CODE, roomCode)
            putExtra(AppConstants.EXTRA_PLAYER_NAME, playerName)
            putExtra(AppConstants.EXTRA_JITSI_ROOM, jitsiRoom)
        }
        startActivity(intent)
        requireActivity().overridePendingTransition(
            android.R.anim.fade_in,
            android.R.anim.fade_out
        )
    }

    /**
     * Inicia as animações de entrada dos elementos.
     */
    private fun startEntryAnimations() {
        binding.cardRoomCode.animatePopIn(0)
        binding.cardPlayers.animatePopIn(150)
        binding.layoutActions.animatePopIn(300)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
