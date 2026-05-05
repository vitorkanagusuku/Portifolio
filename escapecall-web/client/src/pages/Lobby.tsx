import { useEffect, useState } from "react";
import { useLocation, useSearch } from "wouter";
import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import { Copy, Play } from "lucide-react";

interface Player {
  id: string;
  name: string;
  ready: boolean;
  isHost: boolean;
}

export default function Lobby() {
  const [, setLocation] = useLocation();
  const search = useSearch();
  const params = new URLSearchParams(search);
  const roomCode = params.get("code") || "ABCD12";
  const playerName = params.get("player") || "Jogador";
  const isHost = params.get("host") === "true";

  const [players, setPlayers] = useState<Player[]>([
    {
      id: "1",
      name: playerName,
      ready: false,
      isHost: isHost,
    },
  ]);
  const [copied, setCopied] = useState(false);

  // Simular entrada de outros jogadores
  useEffect(() => {
    const timer = setTimeout(() => {
      setPlayers((prev) => [
        ...prev,
        {
          id: "2",
          name: "Ana",
          ready: false,
          isHost: false,
        },
      ]);
    }, 3000);

    return () => clearTimeout(timer);
  }, []);

  const handleCopyCode = () => {
    navigator.clipboard.writeText(roomCode);
    setCopied(true);
    setTimeout(() => setCopied(false), 2000);
  };

  const handleReady = () => {
    setPlayers((prev) =>
      prev.map((p) =>
        p.id === "1" ? { ...p, ready: !p.ready } : p
      )
    );
  };

  const handleStartGame = () => {
    setLocation(`/game?code=${roomCode}&player=${playerName}`);
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-purple-900 via-purple-800 to-indigo-900 p-4">
      <div className="max-w-2xl mx-auto">
        {/* Header */}
        <div className="text-center mb-8">
          <h1 className="text-3xl font-bold text-white mb-2">🚪 Sala de Espera</h1>
          <p className="text-purple-200">Aguardando jogadores entrarem na sala</p>
        </div>

        {/* Room Code Card */}
        <Card className="bg-purple-800/50 border-yellow-500/50 backdrop-blur mb-6">
          <div className="p-6 text-center">
            <p className="text-purple-200 text-sm mb-2">Compartilhe este código:</p>
            <div className="flex items-center justify-center gap-4">
              <div className="text-5xl font-bold text-yellow-400 tracking-widest font-mono">
                {roomCode}
              </div>
              <Button
                onClick={handleCopyCode}
                size="icon"
                className="bg-yellow-500 hover:bg-yellow-600 text-gray-900"
              >
                <Copy className="w-5 h-5" />
              </Button>
            </div>
            {copied && <p className="text-green-400 text-sm mt-2">✓ Copiado!</p>}
            <p className="text-purple-300 text-xs mt-2">Envie para seus amigos entrarem!</p>
          </div>
        </Card>

        {/* Players Card */}
        <Card className="bg-purple-800/50 border-purple-600/50 backdrop-blur mb-6">
          <div className="p-6">
            <div className="flex items-center justify-between mb-4">
              <h2 className="text-xl font-bold text-white">👥 Jogadores</h2>
              <span className="bg-purple-600 text-white px-3 py-1 rounded-full text-sm">
                {players.length}/6
              </span>
            </div>

            <div className="space-y-3">
              {players.map((player) => (
                <div
                  key={player.id}
                  className="flex items-center justify-between p-3 bg-purple-900/50 rounded-lg border border-purple-600/30"
                >
                  <div className="flex items-center gap-3">
                    <div className="w-10 h-10 rounded-full bg-gradient-to-br from-purple-400 to-purple-600 flex items-center justify-center text-white font-bold">
                      {player.name[0]}
                    </div>
                    <div>
                      <p className="font-semibold text-white">
                        {player.name}
                        {player.isHost && (
                          <span className="ml-2 text-yellow-400 text-xs">👑 Host</span>
                        )}
                      </p>
                      <p className="text-xs text-purple-300">
                        {player.ready ? "✓ Pronto" : "Aguardando..."}
                      </p>
                    </div>
                  </div>
                  {player.ready && (
                    <span className="text-green-400 text-lg">✓</span>
                  )}
                </div>
              ))}
            </div>
          </div>
        </Card>

        {/* Info Card */}
        <Card className="bg-purple-900/50 border-purple-600/30 backdrop-blur mb-6">
          <div className="p-4">
            <p className="text-purple-200 text-sm">
              <span className="font-bold">📖 Como Jogar:</span> Todos entram na videochamada automaticamente. Um enigma aparece na tela. Discutam em grupo e resolvam juntos!
            </p>
          </div>
        </Card>

        {/* Action Buttons */}
        <div className="space-y-3">
          <Button
            onClick={handleReady}
            className="w-full bg-purple-600 hover:bg-purple-700 text-white font-bold"
          >
            {players[0]?.ready ? "✓ Você está pronto" : "Marcar como Pronto"}
          </Button>

          {isHost && (
            <Button
              onClick={handleStartGame}
              className="w-full bg-gradient-to-r from-yellow-400 to-yellow-500 hover:from-yellow-500 hover:to-yellow-600 text-gray-900 font-bold text-lg"
            >
              <Play className="w-5 h-5 mr-2" />
              Iniciar Jogo!
            </Button>
          )}

          {!isHost && (
            <div className="text-center text-purple-300 text-sm py-4">
              ⏳ Aguardando o host iniciar o jogo…
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
