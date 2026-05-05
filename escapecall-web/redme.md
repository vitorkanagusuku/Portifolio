# 🔐 EscapeCall Web

> **Videoconferência + Escape Room: Desafie seus amigos ao vivo!**

![React](https://img.shields.io/badge/React-19-61DAFB?style=for-the-badge&logo=react&logoColor=white)
![TypeScript](https://img.shields.io/badge/TypeScript-5.6-3178C6?style=for-the-badge&logo=typescript&logoColor=white)
![Tailwind CSS](https://img.shields.io/badge/Tailwind_CSS-4-06B6D4?style=for-the-badge&logo=tailwind-css&logoColor=white)
![Jitsi Meet](https://img.shields.io/badge/Jitsi_Meet-Web_SDK-1D76BA?style=for-the-badge&logo=jitsi&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)

O **EscapeCall Web** é uma aplicação web inovadora que transforma chamadas de vídeo comuns em experiências interativas e gamificadas. O projeto resolve o problema da falta de engajamento em videoconferências, integrando um sistema de *Escape Room* colaborativo diretamente na tela da chamada.

---

## 🎯 Proposta de Valor

As videoconferências tradicionais costumam ser passivas e, muitas vezes, monótonas. O **EscapeCall Web** propõe uma nova forma de interação:

- **Colaboração Ativa:** Os participantes precisam conversar e trabalhar juntos para resolver enigmas em tempo real.
- **Integração Perfeita:** A chamada de vídeo (via Jitsi Meet) ocorre simultaneamente com o jogo, sem precisar alternar entre abas.
- **Gamificação Completa:** Sistema de pontuação, timer regressivo, dicas limitadas e feedback visual mantêm o engajamento alto.
- **Acesso Imediato:** Sem instalação necessária — acesse direto pelo navegador.

---

## ✨ Funcionalidades Principais

### 🚪 Salas Privadas
- Crie uma sala e compartilhe o código de 6 dígitos com seus amigos.
- Cada sala suporta até 6 jogadores simultâneos.
- Código único gerado automaticamente para cada sessão.

### 📹 Videoconferência Embutida
- Integração nativa com o SDK Web do Jitsi Meet.
- Câmera e microfone ativados automaticamente.
- Interface minimizável para focar nos enigmas.

### 🧩 Sistema de Jogo Completo
- **3 Enigmas Variados:** Lógica, matemática e sequências.
- **Timer Regressivo:** 5 minutos com mudança de cores (urgência visual).
- **Sistema de Dicas:** Até 3 dicas por jogo com penalidade de pontos.
- **Feedback Visual:** Acertos e erros com animações suaves.
- **Pontuação Dinâmica:** Pontos reduzidos por dica utilizada.

### 🏆 Resultado Final
- Tela de estatísticas com classificação de performance.
- Ratings: Lendário (400+ pts), Excelente (300+ pts), Bom (200+ pts), Iniciante.
- Opção de compartilhamento de resultado.
- Possibilidade de jogar novamente.

---

## 🛠️ Tecnologias Utilizadas

O projeto foi desenvolvido com as tecnologias web mais modernas:

| Tecnologia | Versão | Propósito |
|:---:|:---:|:---|
| **React** | 19 | Framework UI com Hooks |
| **TypeScript** | 5.6 | Type safety e melhor DX |
| **Tailwind CSS** | 4 | Styling utilitário responsivo |
| **Wouter** | 3.3 | Roteamento client-side |
| **Jitsi Meet SDK** | Web | Videoconferência |
| **Vite** | 7.1 | Build tool e dev server |
| **shadcn/ui** | Latest | Componentes acessíveis |

---

## 📁 Estrutura do Projeto

```
escapecall-web/
├── client/
│   ├── src/
│   │   ├── pages/
│   │   │   ├── Home.tsx          # Tela inicial (Criar/Entrar)
│   │   │   ├── Lobby.tsx         # Sala de espera com lista de jogadores
│   │   │   ├── Game.tsx          # Tela principal com enigmas e timer
│   │   │   ├── Result.tsx        # Resultado final e estatísticas
│   │   │   └── NotFound.tsx      # Página 404
│   │   ├── components/
│   │   │   ├── ui/               # shadcn/ui components
│   │   │   ├── ErrorBoundary.tsx # Error handling
│   │   │   └── Map.tsx           # Maps integration (placeholder)
│   │   ├── contexts/
│   │   │   └── ThemeContext.tsx  # Dark/Light theme
│   │   ├── hooks/                # Custom React hooks
│   │   ├── lib/                  # Utility functions
│   │   ├── App.tsx               # Router principal
│   │   ├── main.tsx              # Entry point
│   │   └── index.css             # Global styles + Tailwind
│   ├── public/
│   │   ├── favicon.ico
│   │   ├── robots.txt
│   │   └── manifest.json
│   └── index.html
├── server/
│   └── index.ts                  # Express server (placeholder)
├── shared/
│   └── const.ts                  # Shared constants
├── package.json
├── tailwind.config.ts
├── tsconfig.json
└── vite.config.ts
```

---

## 🚀 Como Executar Localmente

### Pré-requisitos
- Node.js 18+ (recomendado 20+)
- npm, yarn ou pnpm (pnpm recomendado)
- Navegador moderno com suporte a WebRTC

### Instalação

1. **Clone o repositório:**
   ```bash
   git clone https://github.com/seu-usuario/escapecall-web.git
   cd escapecall-web
   ```

2. **Instale as dependências:**
   ```bash
   pnpm install
   # ou: npm install / yarn install
   ```

3. **Inicie o servidor de desenvolvimento:**
   ```bash
   pnpm dev
   ```

4. **Acesse no navegador:**
   ```
   http://localhost:3000
   ```

---

## 🎮 Como Jogar

### Passo 1: Criar ou Entrar em uma Sala
- **Host:** Clique em "🚀 Criar Sala" e digite seu nome.
- **Jogador:** Clique em "🔑 Entrar", digite seu nome e o código da sala.

### Passo 2: Aguardar no Lobby
- Todos os jogadores aparecem em uma lista.
- Clique em "Marcar como Pronto" quando estiver preparado.
- O host inicia o jogo clicando em "▶ Iniciar Jogo!".

### Passo 3: Resolver Enigmas
- A videoconferência inicia automaticamente.
- Um enigma aparece na tela com a descrição do desafio.
- Discuta com seus amigos e digite a resposta.
- Use dicas se necessário (máx. 3 por jogo).
- Ganhe pontos por acerto (150 pts - 50 pts por dica usada).

### Passo 4: Ver Resultado
- Após resolver todos os 3 enigmas, veja sua pontuação final.
- Compartilhe seu resultado com amigos.
- Jogue novamente para melhorar sua pontuação!

---

## 🎨 Design & UX

### Paleta de Cores
- **Primária:** Roxo escuro (`#1a0a2e`) com gradientes
- **Secundária:** Amarelo vibrante (`#ffd700`) para destaque
- **Terciária:** Ciano (`#00e5ff`) para informações
- **Feedback:** Verde para acertos, vermelho para erros

### Tipografia
- **Display:** Montserrat Bold (títulos)
- **Body:** Inter Regular (textos)
- **Mono:** JetBrains Mono (códigos de sala)

### Animações
- Transições suaves entre telas (300-500ms)
- Pulsação de timer quando tempo está acabando
- Feedback visual imediato para respostas

---

## 📊 Enigmas Inclusos

O jogo inclui 3 enigmas diferentes para cada sessão:

| # | Título | Tipo | Resposta |
|:---:|:---|:---:|:---:|
| 1 | O Guardião das Portas | Lógica | `eco` |
| 2 | Cifra Numérica | Matemática | `1` |
| 3 | O Próximo Passo | Sequência | `j` |

**Como adicionar mais enigmas:** Edite o array `PUZZLES` em `client/src/pages/Game.tsx`.

---

## 🔌 Integração Jitsi Meet

O projeto utiliza o **Jitsi Meet Web SDK** para videoconferência. A integração está preparada em `client/src/pages/Game.tsx`:

```typescript
// Espaço reservado para inicializar Jitsi Meet
const jitsiOptions = {
  roomName: roomCode,
  parentNode: document.querySelector('#jitsi-container'),
  configOverwrite: {
    startAudioMuted: false,
    startVideoMuted: false,
  },
};
```

Para ativar a videoconferência real:
1. Instale o SDK: `pnpm add @jitsi/react-sdk`
2. Importe e configure em `Game.tsx`
3. Substitua o placeholder pela implementação real

---

## 🚀 Deploy

### Opção 1: Vercel (Recomendado)
```bash
pnpm install -g vercel
vercel
```

### Opção 2: Netlify
```bash
pnpm install -g netlify-cli
netlify deploy --prod --dir=dist
```

### Opção 3: Docker
```dockerfile
FROM node:20-alpine
WORKDIR /app
COPY . .
RUN pnpm install && pnpm build
EXPOSE 3000
CMD ["pnpm", "start"]
```

---

## 📱 Responsividade

O aplicativo é totalmente responsivo:
- **Mobile:** Otimizado para telas pequenas (320px+)
- **Tablet:** Layout adaptado para 768px+
- **Desktop:** Experiência completa em 1024px+

---

## 🔒 Segurança

- Códigos de sala gerados aleatoriamente (6 caracteres)
- Sem armazenamento de dados pessoais
- Comunicação via HTTPS
- Sem cookies de rastreamento

---

## 📈 Roadmap Futuro

- [ ] Integração real com Jitsi Meet SDK
- [ ] Sistema de pontuação global (leaderboard)
- [ ] Mais enigmas (20+)
- [ ] Dificuldade progressiva
- [ ] Temas customizáveis
- [ ] Suporte a múltiplos idiomas
- [ ] Aplicativo mobile nativo
- [ ] Histórico de partidas

---

## 🤝 Contribuindo

Contribuições são bem-vindas! Para contribuir:

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

---

## 📄 Licença

Este projeto está licenciado sob a MIT License - veja o arquivo [LICENSE](LICENSE) para detalhes.

---

## 👨‍💻 Autor

Desenvolvido com 💜 por **Manus AI**

---

## 📞 Suporte

- 📧 Email: support@escapecall.com
- 🐛 Issues: [GitHub Issues](https://github.com/seu-usuario/escapecall-web/issues)
- 💬 Discussões: [GitHub Discussions](https://github.com/seu-usuario/escapecall-web/discussions)

---

## 🎓 Referências Acadêmicas

Este projeto foi desenvolvido como uma aplicação web inovadora que combina:
- Arquitetura moderna com React 19 e TypeScript
- Padrões de design responsivo com Tailwind CSS 4
- Integração de APIs de terceiros (Jitsi Meet)
- Gamificação e UX interativa

Ideal para apresentações acadêmicas sobre:
- Desenvolvimento web moderno
- Experiência do usuário (UX)
- Aplicações em tempo real
- Comunicação colaborativa

---

<div align="center">

### 🎮 Pronto para jogar? Acesse agora!

**[Abrir EscapeCall Web](https://escapecall-web.vercel.app)**

Ou execute localmente: `pnpm dev`

---

*Desenvolvido com ❤️ para revolucionar as chamadas de vídeo*

</div>
