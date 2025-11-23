# ♠️ Networked Three Card Poker

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![JavaFX](https://img.shields.io/badge/JavaFX-CSS-blue?style=for-the-badge)
![Maven](https://img.shields.io/badge/Maven-Build-C71A36?style=for-the-badge)
![JUnit](https://img.shields.io/badge/JUnit-Testing-25A162?style=for-the-badge)

A robust client-server desktop game that allows multiple clients to connect to a central server to play Three Card Poker. The project demonstrates strict MVC architecture, thread safety, and polished UI design.

## 📸 Screenshots
<table>
  <tr>
    <td align="center"><img src="/screenshots/server_entry.png" width="100%" /><br />Server Entrypoint</td>
    <td align="center"><img src="/screenshots/server_log.png" width="100%" /><br />Real-Time Updating Server Logs</td>
  </tr>
</table>

<table>
  <tr>
    <td align="center"><img src="/screenshots/client_entry.png" width="100%" /><br />Client Entrypoint</td>
    <td align="center"><img src="/screenshots/nightmode_gameplay.png" width="100%" /><br />"Night Mode" Themed Gameplay</td>
    <td align="center"><img src="/screenshots/pixel_gameplay.png" width="100%" /><br />"Pixel" Themed Gameplay</td>
  </tr>
</table>

## 🛠 Technical Highlights

### 1. Client-Server Architecture
* **Centralized Logic:** The client is a "dumb terminal" (View); all game logic, deck management, and win/loss calculations occur strictly on the Server (Model) to prevent cheating.
* **Concurrency:** The Server utilizes a multi-threaded `ClientHandler` architecture to manage multiple simultaneous connections without blocking the UI or other players.
* **Object Serialization:** Communication occurs via `ObjectInputStream` and `ObjectOutputStream`, passing a shared `PokerInfo` DTO (Data Transfer Object) rather than parsing raw strings.

### 2. Advanced JavaFX UI
* **Dynamic Theming:** Implemented a live theme switcher that hot-swaps CSS files and reloads assets at runtime to switch between "Retro Pixel," "Classic Casino," and "Night Sky" aesthetics.
* **FXML & MVC:** Strict separation of the layout (FXML), logic (Controllers), and data (POJOs).
* **Animation:** Uses JavaFX `Transitions` (Fade, Translate, Scale) for dealing cards, winning celebrations, and background effects (twinkling stars).

### 3. Reliability & Testing
* **Graceful Handling:** Robust `try-catch` resource management ensures sockets and streams close properly upon disconnection.
* **Unit Testing:** Comprehensive JUnit 5 test suite covering all game logic edge cases (Straight vs Flush, A-2-3 straights, Kickers, etc.).

## 🚀 How to Run

**Prerequisites:** Java 17+, Maven.

1.  **Start the Server:**
    ```bash
    cd server
    mvn clean
    mvn compile exec:java
    ```
2.  **Start a Client (Open new terminal):**
    ```bash
    cd client
    mvn clean 
    mvn compile exec:java
    ```

## 📂 Project Structure

### 🖥️ Client Module (`projectThreeClient`)
*Handles the JavaFX GUI, animations, and network communication.*

**Source Code (`src/main/java/`)**
* `JavaFXTemplate.java`: Main application entry point; handles scene switching and CSS theming.
* `ClientConnect.java`: Manages the background socket thread for server communication.
* `PokerInfo.java`: Shared DTO (Data Transfer Object) for sending game state over the network.
* `GameController.java`: Controls the main gameplay UI (bets, card display, animations).
* `EntryController.java`: Handles the initial connection screen (IP/Port input).
* `ResultsController.java`: Manages the win/loss screen and animations.
* `RulesController.java`: Displays the game rules.
* `CardImageUtil.java`: Utility for loading card assets based on the active theme.

**Resources (`src/main/resources/`)**
* **FXML:** `entry.fxml`, `game.fxml`, `results.fxml`, `rules.fxml`
* **CSS:** `pixelstyle.css` (Retro), `classicstyle.css` (Casino), `nightstyle.css` (Dark Mode)
* **Assets:** Card images (Pixel, Classic, Night variants) and Fonts.

---

### 🗄️ Server Module (`projectThreeServer`)
*Handles all game logic, deck management, and win/loss calculations.*

**Source Code (`src/main/java/`)**
* `Server.java`: Main backend logic; spawns `ClientHandler` threads for each player.
* `JavaFXTemplate.java`: Server GUI entry point.
* `ServerIntroController.java`: Configuration screen for setting the listening port.
* `ServerLogController.java`: Dashboard displaying active clients and game logs.
* `GamePlay.java`: Manages the state of a single game round (dealing, folding, playing).
* `ThreeCardLogic.java`: Static logic for evaluating hand rankings and payouts.
* `Deck.java` & `Hand.java`: Classes for managing the 52-card deck and player hands.
* `PokerInfo.java`: Shared DTO (must match Client version exactly).
* `Card.java`: Model class representing a single playing card.

**Testing (`src/test/java/`)**
* `ThreeCardLogicTest.java`: JUnit 5 test suite verifying hand evaluations and payout logic.

## 🔮 Future Roadmap

While the current application successfully implements a multi-threaded Client-Server architecture for concurrent single-player games, future development goals include:

* **True Multiplayer Interactivity:** Refactoring the server to group `ClientHandler` threads into shared "Lobbies." This would allow clients to view each other's moves and compete for a shared pot.
* **Global Chat System:** Implementing a broadcast protocol to allow text communication between connected clients.
* **Database Integration:** Persisting player bankrolls and game history using SQL/SQLite instead of ephemeral session storage.
