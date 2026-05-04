package com.escapecall.app.ui.lobby

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.escapecall.app.R
import com.escapecall.app.model.Player

/**
 * Adapter para exibir a lista de jogadores no lobby.
 * Utiliza ListAdapter com DiffUtil para atualizações eficientes.
 */
class PlayersAdapter : ListAdapter<Player, PlayersAdapter.PlayerViewHolder>(PlayerDiffCallback()) {

    // Emojis de avatar disponíveis para os jogadores
    private val avatarEmojis = listOf("🦊", "🐺", "🦁", "🐯", "🦅", "🐉", "🦄", "🤖")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_player, parent, false)
        return PlayerViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        holder.bind(getItem(position), avatarEmojis)
    }

    /**
     * ViewHolder para cada item de jogador na lista.
     */
    class PlayerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvAvatar: TextView = itemView.findViewById(R.id.tvAvatar)
        private val tvPlayerName: TextView = itemView.findViewById(R.id.tvPlayerName)
        private val tvPlayerStatus: TextView = itemView.findViewById(R.id.tvPlayerStatus)
        private val ivHostBadge: ImageView = itemView.findViewById(R.id.ivHostBadge)
        private val ivReadyBadge: ImageView = itemView.findViewById(R.id.ivReadyBadge)

        fun bind(player: Player, avatarEmojis: List<String>) {
            // Define o avatar emoji
            tvAvatar.text = avatarEmojis.getOrElse(player.avatarIndex) { "🎮" }

            // Nome do jogador
            tvPlayerName.text = player.name

            // Status do jogador
            tvPlayerStatus.text = when {
                player.isReady -> "✓ Pronto"
                else -> "Aguardando..."
            }
            tvPlayerStatus.setTextColor(
                if (player.isReady)
                    itemView.context.getColor(R.color.success_green)
                else
                    itemView.context.getColor(R.color.text_secondary)
            )

            // Badge de host
            ivHostBadge.visibility = if (player.isHost) View.VISIBLE else View.GONE

            // Badge de pronto
            ivReadyBadge.visibility = if (player.isReady) View.VISIBLE else View.GONE

            // Animação de entrada
            itemView.alpha = 0f
            itemView.animate()
                .alpha(1f)
                .setDuration(300)
                .setStartDelay(adapterPosition * 80L)
                .start()
        }
    }

    /**
     * DiffCallback para comparação eficiente de itens.
     */
    class PlayerDiffCallback : DiffUtil.ItemCallback<Player>() {
        override fun areItemsTheSame(oldItem: Player, newItem: Player): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Player, newItem: Player): Boolean =
            oldItem == newItem
    }
}
