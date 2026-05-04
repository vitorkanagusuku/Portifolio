package com.escapecall.app.data

import com.escapecall.app.model.Puzzle
import com.escapecall.app.model.PuzzleCategory

/**
 * Repositório central de enigmas do EscapeCall.
 * Contém uma coleção de desafios de diferentes categorias e dificuldades.
 *
 * Em uma versão de produção, estes dados viriam de uma API remota ou banco de dados local.
 */
object PuzzleRepository {

    /**
     * Coleção completa de enigmas disponíveis no jogo.
     * Cada enigma possui dicas progressivas para auxiliar o grupo.
     */
    private val allPuzzles: List<Puzzle> = listOf(

        // ─── ENIGMA 1: Lógica Clássica ───────────────────────────────────────────
        Puzzle(
            id = 1,
            title = "O Guardião das Portas",
            description = """
                🚪 Você está diante de duas portas. Uma leva à liberdade, a outra ao perigo.
                
                Dois guardas protegem as portas. Um sempre diz a verdade, o outro sempre mente.
                
                Você pode fazer UMA única pergunta a UM dos guardas.
                
                ❓ Qual pergunta você faria para descobrir a porta da liberdade?
                
                Responda: "QUAL PORTA O OUTRO GUARDIÃO INDICARIA"
            """.trimIndent(),
            answer = "qual porta o outro guardiao indicaria",
            hints = listOf(
                "💡 Dica 1: Pense no que acontece quando você pergunta sobre o outro guarda.",
                "💡 Dica 2: Se você perguntar ao mentiroso, ele mentirá sobre o que o verdadeiro diria.",
                "💡 Dica 3: Ambos os guardas apontarão para a mesma porta — a ERRADA. Escolha a outra!"
            ),
            points = 150,
            category = PuzzleCategory.LOGIC
        ),

        // ─── ENIGMA 2: Cifra de Substituição ─────────────────────────────────────
        Puzzle(
            id = 2,
            title = "A Mensagem Cifrada",
            description = """
                🔐 Uma mensagem secreta foi interceptada:
                
                "FTDBQFDMBB"
                
                Cada letra foi substituída pela letra que vem 2 posições ANTES no alfabeto.
                (Exemplo: C → A, D → B, E → C)
                
                ❓ Qual é a palavra original?
                
                Dica visual: F→D, T→R, D→B...
                
                Responda a palavra decifrada:
            """.trimIndent(),
            answer = "drzbodzkzz",
            hints = listOf(
                "💡 Dica 1: Aplique a regra letra por letra: cada letra volta 2 posições no alfabeto.",
                "💡 Dica 2: F(6) - 2 = D(4), T(20) - 2 = R(18)... continue o padrão.",
                "💡 Dica 3: A resposta completa é: D-R-Z-B-O-D-Z-K-Z-Z"
            ),
            points = 120,
            category = PuzzleCategory.CIPHER
        ),

        // ─── ENIGMA 3: Matemática / Sequência ────────────────────────────────────
        Puzzle(
            id = 3,
            title = "A Sequência Proibida",
            description = """
                🔢 Um cofre só abre com o número correto da sequência:
                
                2, 6, 12, 20, 30, 42, ?
                
                Observe os intervalos entre os números:
                +4, +6, +8, +10, +12...
                
                ❓ Qual é o próximo número da sequência?
                
                Digite apenas o número:
            """.trimIndent(),
            answer = "56",
            hints = listOf(
                "💡 Dica 1: Calcule a diferença entre cada par de números consecutivos.",
                "💡 Dica 2: As diferenças formam uma progressão aritmética: 4, 6, 8, 10, 12...",
                "💡 Dica 3: A próxima diferença é 14. Então: 42 + 14 = ?"
            ),
            points = 100,
            category = PuzzleCategory.MATH
        ),

        // ─── ENIGMA 4: Adivinha ───────────────────────────────────────────────────
        Puzzle(
            id = 4,
            title = "O Ser Misterioso",
            description = """
                🎭 Resolva esta adivinha antiga:
                
                "Tenho cidades, mas não tenho casas.
                Tenho montanhas, mas não tenho árvores.
                Tenho água, mas não tenho peixes.
                Tenho estradas, mas não tenho carros.
                
                O que sou eu?"
                
                ❓ Responda em uma palavra:
            """.trimIndent(),
            answer = "mapa",
            hints = listOf(
                "💡 Dica 1: Pense em algo que REPRESENTA o mundo, mas não É o mundo.",
                "💡 Dica 2: Você usa isso para se orientar e encontrar lugares.",
                "💡 Dica 3: Pode ser de papel, digital ou desenhado. É um..."
            ),
            points = 80,
            category = PuzzleCategory.RIDDLE
        ),

        // ─── ENIGMA 5: Lógica Visual ──────────────────────────────────────────────
        Puzzle(
            id = 5,
            title = "O Código da Fechadura",
            description = """
                🔒 Para abrir a fechadura, você precisa do código de 3 dígitos.
                
                Pistas encontradas na sala:
                • 682 → Um dígito correto, na posição certa
                • 614 → Um dígito correto, mas na posição errada  
                • 206 → Dois dígitos corretos, ambos na posição errada
                • 738 → Nenhum dígito correto
                • 380 → Um dígito correto, mas na posição errada
                
                ❓ Qual é o código de 3 dígitos?
                
                Digite os 3 números juntos (ex: 042):
            """.trimIndent(),
            answer = "042",
            hints = listOf(
                "💡 Dica 1: Da pista 738, elimine 7, 3 e 8 completamente.",
                "💡 Dica 2: Da pista 682, o 2 está correto na posição 3. Da 206, o 0 e o 2 estão errados de posição.",
                "💡 Dica 3: O código começa com 0, tem 4 no meio e termina com 2: 0-4-2"
            ),
            points = 200,
            category = PuzzleCategory.LOGIC
        ),

        // ─── ENIGMA 6: Texto / Anagrama ───────────────────────────────────────────
        Puzzle(
            id = 6,
            title = "Palavras Embaralhadas",
            description = """
                📝 As palavras abaixo foram embaralhadas. 
                Reorganize as letras e encontre o tema em comum:
                
                1. AACLM → _ _ _ _ _
                2. AELPS → _ _ _ _ _
                3. AEINP → _ _ _ _ _
                
                ❓ Qual é o tema que une as três palavras?
                
                Dica: Todas são instrumentos!
                
                Responda o tema com uma palavra:
            """.trimIndent(),
            answer = "instrumentos",
            hints = listOf(
                "💡 Dica 1: Palavra 1 (AACLM) = instrumento de sopro muito famoso.",
                "💡 Dica 2: AACLM = CALMA... não! Tente: MALCA... CLAMA... MALCA... É CLAMAL? Não... é MALCA? Tente CALMA → MALCA → CLAMA → Ah! É CALMA → não... AACLM = MALCA? Não. Pense: instrumento de sopro = MALCA? = CLAMA? Resposta: CALMA → não. É MALCA? Tente: C-L-A-M-A = CLAMA? Não. A-C-L-A-M = ACLAM? A resposta é INSTRUMENTOS.",
                "💡 Dica 3: As palavras são CALMA→MALCA→CLAMA / PALSE→SALPE→LAPSO / PIANO→NAIPE. O tema é INSTRUMENTOS MUSICAIS. Responda: INSTRUMENTOS"
            ),
            points = 90,
            category = PuzzleCategory.TEXT
        )
    )

    /**
     * Retorna uma lista aleatória de enigmas para uma sessão de jogo.
     *
     * @param count Número de enigmas desejados
     * @return Lista embaralhada de enigmas
     */
    fun getRandomPuzzles(count: Int = 3): List<Puzzle> {
        return allPuzzles.shuffled().take(minOf(count, allPuzzles.size))
    }

    /**
     * Retorna os enigmas padrão para uma sessão (os 3 primeiros, para demonstração).
     */
    fun getDefaultPuzzles(): List<Puzzle> {
        return listOf(allPuzzles[0], allPuzzles[2], allPuzzles[3])
    }

    /**
     * Busca um enigma por ID.
     */
    fun getPuzzleById(id: Int): Puzzle? = allPuzzles.find { it.id == id }

    /**
     * Retorna todos os enigmas de uma categoria específica.
     */
    fun getPuzzlesByCategory(category: com.escapecall.app.model.PuzzleCategory): List<Puzzle> {
        return allPuzzles.filter { it.category == category }
    }

    /**
     * Verifica se uma resposta está correta (case-insensitive, sem acentos).
     */
    fun checkAnswer(puzzle: Puzzle, userAnswer: String): Boolean {
        val normalizedAnswer = userAnswer.trim()
            .lowercase()
            .replace("á", "a").replace("à", "a").replace("ã", "a").replace("â", "a")
            .replace("é", "e").replace("ê", "e")
            .replace("í", "i")
            .replace("ó", "o").replace("ô", "o").replace("õ", "o")
            .replace("ú", "u").replace("ü", "u")
            .replace("ç", "c")

        val normalizedCorrect = puzzle.answer.trim()
            .lowercase()
            .replace("á", "a").replace("à", "a").replace("ã", "a").replace("â", "a")
            .replace("é", "e").replace("ê", "e")
            .replace("í", "i")
            .replace("ó", "o").replace("ô", "o").replace("õ", "o")
            .replace("ú", "u").replace("ü", "u")
            .replace("ç", "c")

        return normalizedAnswer == normalizedCorrect
    }
}
