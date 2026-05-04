package com.escapecall.app.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.escapecall.app.R
import com.escapecall.app.databinding.FragmentHomeBinding
import com.escapecall.app.util.AppConstants
import com.escapecall.app.util.animatePopIn
import com.escapecall.app.util.animateShake
import com.escapecall.app.util.showErrorSnackbar
import com.escapecall.app.viewmodel.HomeViewModel

/**
 * Fragment da tela inicial do EscapeCall.
 *
 * Permite ao usuário:
 * - Inserir seu nome de jogador
 * - Criar uma nova sala de jogo
 * - Entrar em uma sala existente via código
 */
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        observeViewModel()
        startEntryAnimations()
    }

    /**
     * Configura os elementos de UI e listeners.
     */
    private fun setupUI() {
        // Alterna entre os modos Criar e Entrar
        binding.tabCreate.setOnClickListener { switchToCreateMode() }
        binding.tabJoin.setOnClickListener { switchToJoinMode() }

        // Botão de criar sala
        binding.btnCreateRoom.setOnClickListener {
            val playerName = binding.etPlayerName.text.toString().trim()
            if (validatePlayerName(playerName)) {
                viewModel.createRoom(playerName)
            }
        }

        // Botão de entrar na sala
        binding.btnJoinRoom.setOnClickListener {
            val playerName = binding.etPlayerName.text.toString().trim()
            val roomCode = binding.etRoomCode.text.toString().trim().uppercase()
            if (validatePlayerName(playerName) && validateRoomCode(roomCode)) {
                viewModel.joinRoom(roomCode, playerName)
            }
        }

        // Formata o código da sala automaticamente para maiúsculas
        binding.etRoomCode.doOnTextChanged { text, _, _, _ ->
            val upper = text.toString().uppercase()
            if (text.toString() != upper) {
                binding.etRoomCode.setText(upper)
                binding.etRoomCode.setSelection(upper.length)
            }
        }

        // Ação de teclado no campo de código
        binding.etRoomCode.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.btnJoinRoom.performClick()
                true
            } else false
        }
    }

    /**
     * Observa os estados do ViewModel e reage às mudanças.
     */
    private fun observeViewModel() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.btnCreateRoom.isEnabled = !isLoading
            binding.btnJoinRoom.isEnabled = !isLoading
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            if (!error.isNullOrEmpty()) {
                binding.root.showErrorSnackbar(error)
                binding.etPlayerName.animateShake()
            }
        }

        viewModel.navigateToLobby.observe(viewLifecycleOwner) { lobbyData ->
            lobbyData?.let { (roomCode, playerName, isHost) ->
                val bundle = Bundle().apply {
                    putString(AppConstants.EXTRA_ROOM_CODE, roomCode)
                    putString(AppConstants.EXTRA_PLAYER_NAME, playerName)
                    putBoolean(AppConstants.EXTRA_IS_HOST, isHost)
                }
                findNavController().navigate(R.id.action_homeFragment_to_lobbyFragment, bundle)
                viewModel.onNavigationComplete()
            }
        }
    }

    /**
     * Alterna para o modo de criação de sala.
     */
    private fun switchToCreateMode() {
        binding.tabCreate.isSelected = true
        binding.tabJoin.isSelected = false
        binding.layoutJoinRoom.visibility = View.GONE
        binding.btnCreateRoom.visibility = View.VISIBLE
        binding.btnJoinRoom.visibility = View.GONE
        binding.tvModeTitle.text = getString(R.string.create_room_title)
        binding.tvModeSubtitle.text = getString(R.string.create_room_subtitle)
    }

    /**
     * Alterna para o modo de entrada em sala existente.
     */
    private fun switchToJoinMode() {
        binding.tabCreate.isSelected = false
        binding.tabJoin.isSelected = true
        binding.layoutJoinRoom.visibility = View.VISIBLE
        binding.btnCreateRoom.visibility = View.GONE
        binding.btnJoinRoom.visibility = View.VISIBLE
        binding.tvModeTitle.text = getString(R.string.join_room_title)
        binding.tvModeSubtitle.text = getString(R.string.join_room_subtitle)
        binding.layoutJoinRoom.animatePopIn()
    }

    /**
     * Valida o nome do jogador.
     */
    private fun validatePlayerName(name: String): Boolean {
        return if (name.length < 2) {
            binding.tilPlayerName.error = getString(R.string.error_name_too_short)
            binding.etPlayerName.animateShake()
            false
        } else if (name.length > 20) {
            binding.tilPlayerName.error = getString(R.string.error_name_too_long)
            false
        } else {
            binding.tilPlayerName.error = null
            true
        }
    }

    /**
     * Valida o código da sala.
     */
    private fun validateRoomCode(code: String): Boolean {
        return if (code.length != 6) {
            binding.tilRoomCode.error = getString(R.string.error_invalid_code)
            binding.etRoomCode.animateShake()
            false
        } else {
            binding.tilRoomCode.error = null
            true
        }
    }

    /**
     * Inicia as animações de entrada dos elementos da tela.
     */
    private fun startEntryAnimations() {
        binding.ivHeroImage.animatePopIn(0)
        binding.tvWelcomeTitle.animatePopIn(100)
        binding.tvWelcomeSubtitle.animatePopIn(200)
        binding.cardMain.animatePopIn(300)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
