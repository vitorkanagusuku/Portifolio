package com.escapecall.app.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.escapecall.app.R
import com.escapecall.app.databinding.ActivitySplashBinding
import com.escapecall.app.ui.home.MainActivity

/**
 * Tela de splash do EscapeCall.
 *
 * Exibe a identidade visual do app por 2.5 segundos com animações de entrada,
 * depois navega automaticamente para a tela principal.
 */
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicia animações dos elementos visuais
        startAnimations()

        // Navega para MainActivity após 2.5 segundos
        Handler(Looper.getMainLooper()).postDelayed({
            navigateToMain()
        }, 2500)
    }

    /**
     * Inicia as animações sequenciais dos elementos da splash screen.
     */
    private fun startAnimations() {
        // Animação do logo (fade + scale)
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in_scale)
        binding.ivLogo.startAnimation(fadeIn)

        // Animação do título com delay
        Handler(Looper.getMainLooper()).postDelayed({
            val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up_fade_in)
            binding.tvAppName.startAnimation(slideUp)
            binding.tvTagline.startAnimation(slideUp)
        }, 400)

        // Animação do indicador de carregamento
        Handler(Looper.getMainLooper()).postDelayed({
            val fadeInSlow = AnimationUtils.loadAnimation(this, R.anim.fade_in_slow)
            binding.progressBar.startAnimation(fadeInSlow)
            binding.tvLoading.startAnimation(fadeInSlow)
        }, 1000)
    }

    /**
     * Navega para a tela principal e encerra a splash.
     */
    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.fade_in_slow, R.anim.fade_out)
        finish()
    }
}
