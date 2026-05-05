import { useLocation, useSearch } from "wouter";
import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import { Share2, RotateCcw, Home } from "lucide-react";

export default function Result() {
  const [, setLocation] = useLocation();
  const search = useSearch();
  const params = new URLSearchParams(search);
  const score = parseInt(params.get("score") || "0");
  const time = parseInt(params.get("time") || "0");
  const player = params.get("player") || "Jogador";
  const code = params.get("code") || "ABCD12";

  const getRating = (score: number) => {
    if (score >= 400) return { title: "LENDÁRIO", emoji: "🏆", color: "from-yellow-400 to-yellow-600" };
    if (score >= 300) return { title: "EXCELENTE", emoji: "⭐", color: "from-purple-400 to-pink-600" };
    if (score >= 200) return { title: "BOM", emoji: "👍", color: "from-blue-400 to-cyan-600" };
    return { title: "INICIANTE", emoji: "🎮", color: "from-green-400 to-emerald-600" };
  };

  const rating = getRating(score);
  const formatTime = (seconds: number) => {
    const mins = Math.floor(seconds / 60);
    const secs = seconds % 60;
    return `${mins.toString().padStart(2, "0")}:${secs.toString().padStart(2, "0")}`;
  };

  const handleShare = () => {
    const text = `🎮 Eu consegui ${score} pontos no EscapeCall em ${formatTime(time)}! 🔐\nVocê consegue me vencer? Código da sala: ${code}`;
    navigator.clipboard.writeText(text);
    alert("Resultado copiado para compartilhar!");
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-purple-900 via-purple-800 to-indigo-900 flex items-center justify-center p-4">
      <div className="w-full max-w-md">
        {/* Rating Card */}
        <Card className={`bg-gradient-to-br ${rating.color} backdrop-blur mb-6 border-0`}>
          <div className="p-8 text-center text-white">
            <div className="text-7xl mb-4">{rating.emoji}</div>
            <h1 className="text-4xl font-bold mb-2">{rating.title}</h1>
            <p className="text-lg opacity-90">Parabéns, {player}!</p>
          </div>
        </Card>

        {/* Score Card */}
        <Card className="bg-purple-800/50 border-purple-600/50 backdrop-blur mb-6">
          <div className="p-8 text-center">
            <p className="text-purple-200 text-sm mb-2">Pontuação Total</p>
            <div className="text-6xl font-bold text-yellow-400 mb-4">{score}</div>
            <div className="flex justify-around text-center">
              <div>
                <p className="text-purple-300 text-xs">Tempo</p>
                <p className="text-white font-bold">{formatTime(time)}</p>
              </div>
              <div className="border-l border-purple-600"></div>
              <div>
                <p className="text-purple-300 text-xs">Sala</p>
                <p className="text-yellow-400 font-mono font-bold">{code}</p>
              </div>
            </div>
          </div>
        </Card>

        {/* Stats Card */}
        <Card className="bg-purple-800/50 border-purple-600/50 backdrop-blur mb-6">
          <div className="p-6">
            <h2 className="text-white font-bold mb-4">📊 Estatísticas</h2>
            <div className="grid grid-cols-3 gap-3">
              <div className="bg-purple-900/50 rounded-lg p-3 text-center">
                <p className="text-purple-300 text-xs">Enigmas</p>
                <p className="text-white font-bold text-lg">3/3 ✓</p>
              </div>
              <div className="bg-purple-900/50 rounded-lg p-3 text-center">
                <p className="text-purple-300 text-xs">Tempo</p>
                <p className="text-white font-bold text-lg">{formatTime(time)}</p>
              </div>
              <div className="bg-purple-900/50 rounded-lg p-3 text-center">
                <p className="text-purple-300 text-xs">Jogador</p>
                <p className="text-white font-bold text-lg truncate">{player}</p>
              </div>
            </div>
          </div>
        </Card>

        {/* Action Buttons */}
        <div className="space-y-3">
          <Button
            onClick={() => setLocation("/lobby?code=" + code + "&player=" + player + "&host=false")}
            className="w-full bg-gradient-to-r from-yellow-400 to-yellow-500 hover:from-yellow-500 hover:to-yellow-600 text-gray-900 font-bold"
          >
            <RotateCcw className="w-4 h-4 mr-2" />
            Jogar Novamente
          </Button>

          <Button
            onClick={handleShare}
            className="w-full bg-gradient-to-r from-blue-500 to-cyan-500 hover:from-blue-600 hover:to-cyan-600 text-white font-bold"
          >
            <Share2 className="w-4 h-4 mr-2" />
            Compartilhar Resultado
          </Button>

          <Button
            onClick={() => setLocation("/")}
            variant="outline"
            className="w-full border-purple-600 text-white hover:bg-purple-900/50"
          >
            <Home className="w-4 h-4 mr-2" />
            Voltar ao Início
          </Button>
        </div>

        {/* Footer */}
        <p className="text-center text-purple-300 text-xs mt-6">
          Desafie seus amigos a baterem seu recorde! 🎮
        </p>
      </div>
    </div>
  );
}
