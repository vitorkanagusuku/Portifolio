import { useEffect, useState, useRef } from "react";
import { useLocation, useSearch } from "wouter";
import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { X, Lightbulb } from "lucide-react";

interface Puzzle {
  id: number;
  title: string;
  description: string;
  answer: string;
  hint: string;
  points: number;
  category: string;
}

const PUZZLES: Puzzle[] = [
  {
    id: 1,
    title: "O Guardião das Portas",
    description: "Eu falo sem boca e ouço sem ouvidos. Não tenho corpo, mas ganho vida com o vento. O que sou?",
    answer: "eco",
    hint: "Ocorre em cavernas ou montanhas.",
    points: 150,
    category: "Lógica",
  },
  {
    id: 2,
    title: "Cifra Numérica",
    description: "Se 1=5, 2=25, 3=125, 4=625, quanto é 5?",
    answer: "1",
    hint: "Leia a primeira igualdade novamente.",
    points: 150,
    category: "Matemática",
  },
  {
    id: 3,
    title: "O Próximo Passo",
    description: "Complete a sequência: J, F, M, A, M, J, ...",
    answer: "j",
    hint: "Pense nos meses do ano.",
    points: 150,
    category: "Lógica",
  },
];

export default function Game() {
  const [, setLocation] = useLocation();
  const search = useSearch();
  const params = new URLSearchParams(search);
  const roomCode = params.get("code") || "ABCD12";
  const playerName = params.get("player") || "Jogador";

  const [currentPuzzleIndex, setCurrentPuzzleIndex] = useState(0);
  const [answer, setAnswer] = useState("");
  const [score, setScore] = useState(0);
  const [timeLeft, setTimeLeft] = useState(300);
  const [showHint, setShowHint] = useState(false);
  const [hintsUsed, setHintsUsed] = useState(0);
  const [feedback, setFeedback] = useState<"correct" | "wrong" | null>(null);
  const [videoMinimized, setVideoMinimized] = useState(false);
  const timerRef = useRef<any>(null);

  const currentPuzzle = PUZZLES[currentPuzzleIndex];
  const progress = ((currentPuzzleIndex + 1) / PUZZLES.length) * 100;

  // Timer
  useEffect(() => {
    timerRef.current = setInterval(() => {
      setTimeLeft((prev) => {
        if (prev <= 1) {
          setLocation(`/result?score=${score}&time=${300}&player=${playerName}&code=${roomCode}`);
          return 0;
        }
        return prev - 1;
      });
    }, 1000);

    return () => {
      if (timerRef.current) clearInterval(timerRef.current);
    };
  }, [score, timeLeft, playerName, roomCode, currentPuzzleIndex, setLocation]);

  const handleSubmitAnswer = () => {
    if (answer.toLowerCase().trim() === currentPuzzle.answer.toLowerCase()) {
      const points = currentPuzzle.points - hintsUsed * 50;
      setScore((prev) => prev + Math.max(0, points));
      setFeedback("correct");

      setTimeout(() => {
        if (currentPuzzleIndex < PUZZLES.length - 1) {
          setCurrentPuzzleIndex((prev) => prev + 1);
          setAnswer("");
          setShowHint(false);
          setHintsUsed(0);
          setFeedback(null);
        } else {
          // Fim do jogo
          setLocation(
            `/result?score=${score + Math.max(0, currentPuzzle.points - hintsUsed * 50)}&time=${300 - timeLeft}&player=${playerName}&code=${roomCode}`
          );
        }
      }, 1500);
    } else {
      setFeedback("wrong");
      setTimeout(() => setFeedback(null), 1000);
    }
  };

  const handleHint = () => {
    if (hintsUsed < 3) {
      setShowHint(true);
      setHintsUsed((prev) => prev + 1);
    }
  };

  const formatTime = (seconds: number) => {
    const mins = Math.floor(seconds / 60);
    const secs = seconds % 60;
    return `${mins.toString().padStart(2, "0")}:${secs.toString().padStart(2, "0")}`;
  };

  const timerColor =
    timeLeft > 120 ? "text-green-400" : timeLeft > 60 ? "text-yellow-400" : "text-red-400";

  return (
    <div className="min-h-screen bg-gradient-to-br from-purple-900 via-purple-800 to-indigo-900 p-4">
      {/* Top Bar */}
      <div className="max-w-6xl mx-auto mb-4">
        <div className="flex items-center justify-between bg-purple-900/50 backdrop-blur rounded-lg p-4 border border-purple-600/30">
          <Button
            onClick={() => setLocation("/")}
            size="icon"
            variant="ghost"
            className="text-purple-300 hover:text-white"
          >
            <X className="w-5 h-5" />
          </Button>

          <div className="flex-1 mx-4">
            <p className="text-sm text-purple-200 mb-1">Enigma {currentPuzzleIndex + 1} de {PUZZLES.length}</p>
            <div className="w-full bg-purple-800 rounded-full h-2">
              <div
                className="bg-gradient-to-r from-purple-500 to-pink-500 h-2 rounded-full transition-all"
                style={{ width: `${progress}%` }}
              />
            </div>
          </div>

          <div className="text-right ml-4">
            <span className="text-yellow-400 font-bold">⭐ {score} pts</span>
          </div>
        </div>
      </div>

      <div className="max-w-6xl mx-auto grid grid-cols-1 lg:grid-cols-3 gap-4">
        {/* Video Area */}
        <div className="lg:col-span-2">
          {!videoMinimized ? (
            <Card className="bg-black border-purple-600/30 aspect-video flex items-center justify-center relative overflow-hidden">
              <div className="absolute inset-0 bg-gradient-to-br from-purple-900/50 to-indigo-900/50" />
              <div className="relative text-center">
                <div className="text-6xl mb-4">📹</div>
                <p className="text-white font-semibold">Videoconferência Jitsi Meet</p>
                <p className="text-purple-300 text-sm mt-2">(Integração com câmera e microfone)</p>
              </div>
              <Button
                onClick={() => setVideoMinimized(true)}
                className="absolute bottom-4 right-4 bg-purple-600 hover:bg-purple-700"
                size="sm"
              >
                Minimizar
              </Button>
            </Card>
          ) : (
            <Card className="bg-purple-800/50 border-purple-600/30 p-4 cursor-pointer hover:bg-purple-800/70 transition-colors"
              onClick={() => setVideoMinimized(false)}>
              <div className="flex items-center justify-between">
                <span className="text-white font-semibold">📹 Videoconferência (minimizada)</span>
                <span className="text-purple-300">Clique para expandir</span>
              </div>
            </Card>
          )}
        </div>

        {/* Right Sidebar - Timer & Puzzle */}
        <div className="space-y-4">
          {/* Timer Card */}
          <Card className="bg-purple-900/50 border-purple-600/30 backdrop-blur p-6 text-center">
            <p className="text-purple-200 text-sm mb-2">Tempo Restante</p>
            <div className={`text-5xl font-bold font-mono ${timerColor}`}>
              {formatTime(timeLeft)}
            </div>
            <div className="w-full bg-purple-800 rounded-full h-2 mt-4">
              <div
                className={`h-2 rounded-full transition-all ${
                  timeLeft > 120
                    ? "bg-green-500"
                    : timeLeft > 60
                    ? "bg-yellow-500"
                    : "bg-red-500"
                }`}
                style={{ width: `${(timeLeft / 300) * 100}%` }}
              />
            </div>
          </Card>

          {/* Puzzle Card */}
          <Card className="bg-purple-800/50 border-purple-600/50 backdrop-blur p-6">
            <div className="mb-4">
              <span className="inline-block bg-purple-600 text-white px-3 py-1 rounded-full text-xs font-semibold mb-2">
                🧠 {currentPuzzle.category}
              </span>
              <h2 className="text-xl font-bold text-white">{currentPuzzle.title}</h2>
            </div>

            <p className="text-purple-100 text-sm mb-4 leading-relaxed">
              {currentPuzzle.description}
            </p>

            {feedback === "correct" && (
              <div className="bg-green-500/20 border border-green-500 rounded-lg p-3 mb-4 text-green-400 font-semibold text-center">
                ✅ CORRETO! +{Math.max(0, currentPuzzle.points - hintsUsed * 50)} pontos
              </div>
            )}

            {feedback === "wrong" && (
              <div className="bg-red-500/20 border border-red-500 rounded-lg p-3 mb-4 text-red-400 font-semibold text-center">
                ❌ Resposta errada. Tente novamente!
              </div>
            )}

            {showHint && (
              <div className="bg-yellow-500/20 border border-yellow-500 rounded-lg p-3 mb-4 text-yellow-300 text-sm">
                💡 <span className="font-semibold">Dica:</span> {currentPuzzle.hint}
              </div>
            )}

            <Input
              placeholder="Digite sua resposta..."
              value={answer}
              onChange={(e) => setAnswer(e.target.value)}
              onKeyPress={(e) => e.key === "Enter" && handleSubmitAnswer()}
              className="bg-purple-900/50 border-purple-600 text-white placeholder:text-purple-400 mb-3"
            />

            <div className="flex gap-2">
              <Button
                onClick={handleHint}
                disabled={hintsUsed >= 3}
                className="flex-1 bg-yellow-600 hover:bg-yellow-700 disabled:bg-gray-600 text-white"
                size="sm"
              >
                <Lightbulb className="w-4 h-4 mr-1" />
                Dica ({3 - hintsUsed})
              </Button>
              <Button
                onClick={handleSubmitAnswer}
                className="flex-1 bg-gradient-to-r from-green-500 to-emerald-500 hover:from-green-600 hover:to-emerald-600 text-white"
                size="sm"
              >
                ✓ Responder
              </Button>
            </div>
          </Card>
        </div>
      </div>
    </div>
  );
}
