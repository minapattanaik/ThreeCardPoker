import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import javafx.application.Platform;

public class Server {

    private int port;
    private ServerSocket serverSocket;
    private boolean isRunning = false;
    private ArrayList<ClientHandler> clients = new ArrayList<>();
    private ServerLogController controller;
    private int totalGamesPlayed = 0;

    public Server(int port) {
        this.port = port;
    }

    public void setController(ServerLogController controller) {
        this.controller = controller;
    }

    public void startServer() {
        isRunning = true;
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(port);
                Platform.runLater(() -> controller.log("Server started on port " + port));

                while (isRunning) {
                    Socket s = serverSocket.accept();
                    ClientHandler client = new ClientHandler(s, clients.size() + 1);
                    clients.add(client);
                    client.start();
                    Platform.runLater(() -> {
                        controller.updateClientCount(clients.size());
                        controller.log(">>> Client #" + client.id + " connected.");
                    });
                }
            } catch (Exception e) {
                if (isRunning) Platform.runLater(() -> controller.log("Server Error: " + e.getMessage()));
            }
        }).start();
    }

    public void stopServer() {
        isRunning = false;
        try {
            if (serverSocket != null) serverSocket.close();
            for (ClientHandler c : clients) c.close();
            clients.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class ClientHandler extends Thread {
        Socket socket;
        int id;
        ObjectOutputStream out;
        ObjectInputStream in;
        GamePlay gamePlay;
        double clientBankroll = 1000.0;
        int clientRounds = 0;

        public ClientHandler(Socket socket, int id) {
            this.socket = socket;
            this.id = id;
            this.gamePlay = new GamePlay();
        }

        public void run() {
            try {
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());
                socket.setTcpNoDelay(true);

                while (true) {
                    PokerInfo incoming = (PokerInfo) in.readObject();
                    PokerInfo outgoing = incoming;

                    if ("DEAL".equals(incoming.getAction())) {
                        clientRounds++;
                        gamePlay.dealHands();

                        outgoing.setPlayerHand(cardsToStrings(gamePlay.getCurrentHand().getCards()));
                        outgoing.setDealerHand(cardsToStrings(gamePlay.getDealerHand().getCards()));

                        String pHandStr = formatHand(gamePlay.getCurrentHand().getCards());
                        Platform.runLater(() -> {
                            controller.log("--------------------------------------------------");
                            controller.log("Client #" + id + " starts Hand " + clientRounds);
                            controller.log("   Bets: Ante $" + incoming.getAnteBet() + " | PairPlus $" + incoming.getPairPlusBet());
                            controller.log("   Dealt: " + pHandStr);
                        });
                    } else if ("PLAY".equals(incoming.getAction())) {
                        Result result = gamePlay.evalHands(incoming.getAnteBet(), incoming.getPairPlusBet(), false);
                        fillResult(outgoing, result);

                        // --- EXPANDED LOGGING (PLAY) ---
                        String dHandStr = formatHand(gamePlay.getDealerHand().getCards());

                        // Check specifically for Pair Plus win for logging purposes
                        int ppWin = ThreeCardLogic.evalPPWinnings(new ArrayList<>(gamePlay.getCurrentHand().getCards()), incoming.getPairPlusBet());
                        String ppStatus = (incoming.getPairPlusBet() > 0) ? (ppWin > 0 ? "WON PairPlus ($" + ppWin + ")" : "LOST PairPlus") : "No PP Bet";

                        Platform.runLater(() -> {
                            totalGamesPlayed++;
                            controller.updateTotalGames(totalGamesPlayed);
                            controller.log("Client #" + id + " chose PLAY (Bet $" + incoming.getAnteBet() + ")");
                            controller.log("   Dealer reveals: " + dHandStr);
                            controller.log("   Result: " + result.getResult());
                            controller.log("   " + ppStatus);
                            controller.log("   Net: " + (result.getWinnings() >= 0 ? "+$" : "-$") + Math.abs(result.getWinnings()) + " | Total Bankroll: $" + clientBankroll);
                        });
                    } else if ("FOLD".equals(incoming.getAction())) {
                        Result result = gamePlay.evalHands(incoming.getAnteBet(), incoming.getPairPlusBet(), true);
                        fillResult(outgoing, result);

                        String dHandStr = formatHand(gamePlay.getDealerHand().getCards());

                        Platform.runLater(() -> {
                            totalGamesPlayed++;
                            controller.updateTotalGames(totalGamesPlayed);
                            controller.log("Client #" + id + " FOLDED.");
                            controller.log("   Dealer had: " + dHandStr);
                            controller.log("   Loss: -$" + Math.abs(result.getWinnings()) + " | Total Bankroll: $" + clientBankroll);
                        });
                    } else if ("FRESH_START".equals(incoming.getAction())) {
                        clientBankroll = 1000.0;
                        clientRounds = 0;
                        outgoing.setTotalWinnings(1000.0);
                        Platform.runLater(() -> controller.log("<<< Client #" + id + " reset game (Fresh Start) >>>"));
                    }

                    out.writeObject(outgoing);
                    out.reset();
                }
            } catch (Exception e) {
                Platform.runLater(() -> {
                    controller.log("<<< Client #" + id + " disconnected. >>>");
                    clients.remove(this);
                    controller.updateClientCount(clients.size());
                });
            } finally {
                close();
            }
        }

        private void fillResult(PokerInfo outgoing, Result result) {
            outgoing.setWinnings(result.getWinnings());
            outgoing.setGameMessage(result.getResult());

            // FIX: Use helper to convert Card objects to Strings
            outgoing.setDealerHand(cardsToStrings(gamePlay.getDealerHand().getCards()));

            this.clientBankroll += result.getWinnings();

            outgoing.setTotalWinnings(this.clientBankroll);
            outgoing.setRoundsPlayed(this.clientRounds);
        }

        // HELPER: Formats cards into a string like "[Q-H, 10-S, 2-D]" for LOGS
        private String formatHand(java.util.List<Card> hand) {
            StringBuilder sb = new StringBuilder("[");
            for (Card c : hand) {
                String rank;
                switch (c.getRank()) {
                    case 11: rank = "J"; break;
                    case 12: rank = "Q"; break;
                    case 13: rank = "K"; break;
                    case 14: rank = "A"; break;
                    default: rank = String.valueOf(c.getRank());
                }
                String suit = c.getSuit().toString().substring(0, 1);
                sb.append(rank).append("-").append(suit).append(" ");
            }
            return sb.toString().trim() + "]";
        }

        // HELPER: Converts cards to Strings for POKERINFO (Network)
        private ArrayList<String> cardsToStrings(java.util.List<Card> hand) {
            ArrayList<String> list = new ArrayList<>();
            for (Card c : hand) {
                String rank;
                switch (c.getRank()) {
                    case 11: rank = "J"; break;
                    case 12: rank = "Q"; break;
                    case 13: rank = "K"; break;
                    case 14: rank = "A"; break;
                    default: rank = String.valueOf(c.getRank());
                }
                String suit = c.getSuit().toString().substring(0, 1);
                list.add(rank + "-" + suit);
            }
            return list;
        }

        public void close() {
            try {
                if (socket != null) socket.close();
            } catch (Exception e) {
            }
        }
    }
}