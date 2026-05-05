import { useState } from "react";
import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { useLocation } from "wouter";

export default function Home() {
  const [, setLocation] = useLocation();
  const [playerName, setPlayerName] = useState("");
  const [roomCode, setRoomCode] = useState("");
  const [loading, setLoading] = useState(false);

  const handleCreateRoom = async () => {
    if (!playerName.trim()) {
      alert("Digite seu nome para continuar");
      return;
    }
    setLoading(true);
    // Simular criação de sala
    setTimeout(() => {
      const code = Math.random().toString(36).substring(2, 8).toUpperCase();
      setLocation(`/lobby?code=${code}&player=${playerName}&host=true`);
    }, 1000);
  };

  const handleJoinRoom = async () => {
    if (!playerName.trim() || !roomCode.trim()) {
      alert("Preencha todos os campos");
      return;
    }
    setLoading(true);
    // Simular entrada na sala
    setTimeout(() => {
      setLocation(`/lobby?code=${roomCode}&player=${playerName}&host=false`);
    }, 1000);
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-purple-900 via-purple-800 to-indigo-900 flex items-center justify-center p-4">
      <div className="w-full max-w-md">
        {/* Logo & Header */}
        <div className="text-center mb-8">
          <div className="text-6xl mb-4">🔐</div>
          <h1 className="text-4xl font-bold text-white mb-2">EscapeCall</h1>
          <p className="text-purple-200">Videoconferência + Escape Room</p>
        </div>

        {/* Main Card */}
        <Card className="bg-purple-800/50 border-purple-600/50 backdrop-blur">
          <div className="p-8">
            <Tabs defaultValue="create" className="w-full">
              <TabsList className="grid w-full grid-cols-2 bg-purple-900/50">
                <TabsTrigger value="create" className="data-[state=active]:bg-purple-600">
                  🚀 Criar Sala
                </TabsTrigger>
                <TabsTrigger value="join" className="data-[state=active]:bg-purple-600">
                  🔑 Entrar
                </TabsTrigger>
              </TabsList>

              {/* Create Room Tab */}
              <TabsContent value="create" className="space-y-4 mt-6">
                <div>
                  <label className="block text-sm font-medium text-purple-100 mb-2">
                    Seu Nome
                  </label>
                  <Input
                    placeholder="Digite seu nome..."
                    value={playerName}
                    onChange={(e) => setPlayerName(e.target.value)}
                    className="bg-purple-900/50 border-purple-600 text-white placeholder:text-purple-400"
                    maxLength={20}
                  />
                </div>
                <Button
                  onClick={handleCreateRoom}
                  disabled={loading}
                  className="w-full bg-gradient-to-r from-yellow-400 to-yellow-500 hover:from-yellow-500 hover:to-yellow-600 text-gray-900 font-bold"
                >
                  {loading ? "Criando..." : "🚀 Criar Sala"}
                </Button>
                <p className="text-xs text-purple-300 text-center">
                  Você será o host e poderá convidar amigos
                </p>
              </TabsContent>

              {/* Join Room Tab */}
              <TabsContent value="join" className="space-y-4 mt-6">
                <div>
                  <label className="block text-sm font-medium text-purple-100 mb-2">
                    Seu Nome
                  </label>
                  <Input
                    placeholder="Digite seu nome..."
                    value={playerName}
                    onChange={(e) => setPlayerName(e.target.value)}
                    className="bg-purple-900/50 border-purple-600 text-white placeholder:text-purple-400"
                    maxLength={20}
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-purple-100 mb-2">
                    Código da Sala
                  </label>
                  <Input
                    placeholder="ABCD12"
                    value={roomCode}
                    onChange={(e) => setRoomCode(e.target.value.toUpperCase())}
                    className="bg-purple-900/50 border-purple-600 text-yellow-300 placeholder:text-purple-400 font-mono text-center text-lg tracking-widest"
                    maxLength={6}
                  />
                </div>
                <Button
                  onClick={handleJoinRoom}
                  disabled={loading}
                  className="w-full bg-gradient-to-r from-blue-500 to-cyan-500 hover:from-blue-600 hover:to-cyan-600 text-white font-bold"
                >
                  {loading ? "Entrando..." : "🔑 Entrar na Sala"}
                </Button>
              </TabsContent>
            </Tabs>
          </div>
        </Card>

        {/* Footer */}
        <p className="text-center text-purple-300 text-xs mt-6">
          Requer câmera e microfone para videoconferência
        </p>
      </div>
    </div>
  );
}
