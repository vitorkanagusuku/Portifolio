# 🔐 EscapeCall

> **Videoconferência + Escape Room: Desafie seus amigos ao vivo!**

![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white)
![Jitsi Meet](https://img.shields.io/badge/Jitsi_Meet-1D76BA?style=for-the-badge&logo=jitsi&logoColor=white)
![MVVM](https://img.shields.io/badge/Architecture-MVVM-orange?style=for-the-badge)

O **EscapeCall** é um aplicativo Android inovador que transforma chamadas de vídeo comuns em experiências interativas e gamificadas. O projeto resolve o problema da falta de engajamento em videoconferências, integrando um sistema de *Escape Room* colaborativo diretamente na tela da chamada.

---

## 🎯 Proposta de Valor

As videoconferências tradicionais costumam ser passivas e, muitas vezes, monótonas. O **EscapeCall** propõe uma nova forma de interação:
- **Colaboração Ativa:** Os participantes precisam conversar e trabalhar juntos para resolver enigmas.
- **Integração Perfeita:** A chamada de vídeo (via Jitsi Meet) ocorre simultaneamente com o jogo, sem precisar alternar entre aplicativos.
- **Gamificação:** Sistema de pontuação, timer regressivo, dicas limitadas e feedback visual/sonoro mantêm o engajamento alto.

---

## ✨ Funcionalidades Principais

* **Salas Privadas:** Crie uma sala e compartilhe o código de 6 dígitos com seus amigos.
* **Videoconferência Embutida:** Integração nativa com o SDK do Jitsi Meet (câmera e microfone).
* **Overlay de Enigmas:** Os desafios aparecem na tela enquanto você vê e ouve seus amigos.
* **Sistema de Jogo Completo:**
  * Timer regressivo com mudança de cores (urgência).
  * Sistema de dicas com penalidade de pontos.
  * Feedback visual e háptico (vibração) para acertos e erros.
* **Múltiplas Categorias:** Enigmas de lógica, matemática, cifras e texto.
* **Resultado Final:** Tela de estatísticas com classificação de performance (Lendário, Excelente, etc.) e opção de compartilhamento.

---

## 🛠️ Tecnologias e Arquitetura

O projeto foi desenvolvido seguindo as melhores práticas de desenvolvimento Android moderno:

* **Linguagem:** Kotlin
* **Arquitetura:** MVVM (Model-View-ViewModel)
* **UI/UX:** XML Layouts com Material Design Components e animações customizadas.
* **Navegação:** Jetpack Navigation Component (Single Activity Architecture para o fluxo principal).
* **Assincronismo:** Kotlin Coroutines.
* **Videoconferência:** Jitsi Meet Android SDK.
* **Feedback:** SoundPool e VibratorManager.

### Estrutura do Projeto

```text
com.escapecall.app
├── data/           # Repositórios de dados (PuzzleRepository)
├── model/          # Modelos de domínio (Room, Player, Puzzle)
├── repository/     # Lógica de acesso a dados (RoomRepository)
├── ui/             # Camada de visualização (Activities e Fragments)
│   ├── game/       # Tela principal do jogo com Jitsi
│   ├── home/       # Tela inicial (Criar/Entrar)
│   ├── lobby/      # Sala de espera
│   ├── result/     # Tela de resultado final
│   └── splash/     # Splash screen animada
├── util/           # Extensões e gerenciadores (SoundManager, Constants)
└── viewmodel/      # ViewModels (HomeViewModel, LobbyViewModel, GameViewModel)
```

---

## 🚀 Como Instalar e Rodar

### Pré-requisitos
- Android Studio Iguana (ou superior)
- JDK 17
- Dispositivo Android ou Emulador com API 24+ (Android 7.0+)

### Passos
1. Clone o repositório:
   ```bash
   git clone https://github.com/seu-usuario/EscapeCall.git
   ```
2. Abra o projeto no Android Studio.
3. Aguarde o Gradle sincronizar as dependências (incluindo o Jitsi Meet SDK).
4. Conecte seu dispositivo físico ou inicie um emulador.
5. Clique em **Run 'app'** (Shift + F10).

> **Nota sobre o Emulador:** Para testar a videoconferência, é recomendado usar um dispositivo físico ou garantir que o emulador tenha acesso à câmera e microfone do seu computador.

---

## 📱 Telas do Aplicativo

| Splash & Home | Lobby de Espera | Em Jogo (Overlay) | Resultado Final |
|:---:|:---:|:---:|:---:|
| Animações fluidas e interface intuitiva para criar ou entrar em salas. | Lista de jogadores em tempo real e botão de "Pronto". | Enigmas sobrepostos à chamada de vídeo com timer e dicas. | Estatísticas completas, pontuação e classificação do grupo. |

---

## 🔗 Links e Recursos

* **Apresentação Acadêmica:** O código está estruturado de forma limpa e comentada, ideal para bancas de avaliação.
* **Jitsi Meet SDK:** [Documentação Oficial](https://jitsi.github.io/handbook/docs/dev-guide/dev-guide-android-sdk)

---

<div align="center">
  
### 📲 Baixe o APK de Demonstração

*(Espaço reservado para o QR Code do APK gerado)*

```text
[ QR CODE AQUI ]
```

</div>

---
*Desenvolvido com 💜 para revolucionar as chamadas de vídeo.*
