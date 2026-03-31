
package src.balde.bah.tp6;

import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

public class CrosswordController {

    @FXML private GridPane gridPane;
    @FXML private ListView<Clue> horizontalListView;
    @FXML private ListView<Clue> verticalListView;

    // Nouveaux éléments FXML pour multijoueur et améliorations UI
    @FXML private CheckMenuItem multiplayerMode;
    @FXML private CheckMenuItem showTimer;
    @FXML private CheckMenuItem showHints;
    @FXML private Label timerLabel;
    @FXML private Label gameStatusLabel;
    @FXML private Label currentPlayerLabel;
    @FXML private HBox multiplayerInfo;
    @FXML private Label player1Score;
    @FXML private Label player2Score;
    @FXML private Label statsLabel;

    private Crossword crossword;
    private boolean isHorizontal = true;
    private DatabaseHelper dbHelper = new DatabaseHelper();

    // Variables multijoueur
    private List<Player> players = new ArrayList<>();
    private int currentPlayerIndex = 0;
    private boolean isMultiplayerMode = false;
    private Timer gameTimer;
    private long gameStartTime;
    private int totalCells = 0;
    private int filledCells = 0;

    // Case courante
    private int currentRow = -1;
    private int currentCol = -1;

    @FXML
    public void initialize() {
        // Initialiser avec un joueur solo
        players.add(new Player("Joueur 1"));
        updatePlayerDisplay();
        startTimer();
    }

    @FXML
    public void handleNewGrid() throws Exception {
        Crossword nouvellePartie = dbHelper.extractRandomGrid();
        if (nouvellePartie != null) {
            this.setCrossword(nouvellePartie);
            resetGame();
        }
    }

    public void setCrossword(Crossword crossword) {
        this.crossword = crossword;
        if (gridPane != null && gridPane.getChildren() != null) {
            gridPane.getChildren().clear();
        }
        initializeGrid();

        if (horizontalListView != null) {
            horizontalListView.setItems(crossword.getHorizontalClues());
            // Clic sur un indice horizontal => case courante sur la première lettre
            horizontalListView.setOnMouseClicked(e -> {
                Clue selected = horizontalListView.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    isHorizontal = true;
                    moveFocusTo(selected.getRow(), selected.getColumn());
                    updateClueSelection(selected.getRow(), selected.getColumn());
                }
            });
        }
        if (verticalListView != null) {
            verticalListView.setItems(crossword.getVerticalClues());
            // Clic sur un indice vertical => case courante sur la première lettre
            verticalListView.setOnMouseClicked(e -> {
                Clue selected = verticalListView.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    isHorizontal = false;
                    moveFocusTo(selected.getRow(), selected.getColumn());
                    updateClueSelection(selected.getRow(), selected.getColumn());
                }
            });
        }
    }

    public void initializeGrid() {
        if (crossword == null || gridPane == null) return;

        try {
            for (int row = 0; row < crossword.getHeight(); row++) {
                for (int col = 0; col < crossword.getWidth(); col++) {
                    StackPane cellWrapper = new StackPane();
                    cellWrapper.getStyleClass().add("crossword-cell");
                    cellWrapper.setFocusTraversable(true);
                    final int r = row;
                    final int c = col;

                    if (crossword.isBlackSquare(row, col)) {
                        cellWrapper.getStyleClass().add("black-square");
                    } else {
                        Label letterLabel = new Label();
                        letterLabel.getStyleClass().add("letter-label");
                        letterLabel.textProperty().bind(crossword.propositionProperty(r, c));
                        letterLabel.textProperty().addListener((obs, oldVal, newVal) -> {
                            cellWrapper.getStyleClass().remove("correct-cell");
                            cellWrapper.getStyleClass().remove("wrong-cell");
                            if (newVal != null && !newVal.isEmpty() && !newVal.equals(" ")) {
                                animateLetter(letterLabel);
                            }
                        });
                        cellWrapper.getChildren().add(letterLabel);

                        // Clic souris sur une case blanche
                        cellWrapper.setOnMouseClicked(e -> {
                            cellWrapper.requestFocus();
                            currentRow = r;
                            currentCol = c;
                            updateClueSelection(r, c);
                        });

                        cellWrapper.setOnKeyPressed(e -> handleKeyPress(e, r, c));
                    }
                    gridPane.add(cellWrapper, col, row);
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur d'initialisation : " + e.getMessage());
        }
    }

    @FXML
    private void handleClear() {
        if (crossword == null) return;
        try {
            for (int r = 0; r < crossword.getHeight(); r++) {
                for (int c = 0; c < crossword.getWidth(); c++) {
                    if (!crossword.isBlackSquare(r, c)) {
                        crossword.setProposition(r, c, ' ');
                    }
                }
            }
            for (Node node : gridPane.getChildren()) {
                node.getStyleClass().remove("correct-cell");
                node.getStyleClass().remove("wrong-cell");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleKeyPress(KeyEvent event, int row, int col) {
        KeyCode code = event.getCode();
        currentRow = row;
        currentCol = col;
        try {
            if (code.isLetterKey()) {
                char letter = event.getText().toUpperCase().charAt(0);
                crossword.setProposition(row, col, letter);
                moveFocus(row, col, isHorizontal, 1);
                updateStats();
                checkForCompletion();

            } else if (code == KeyCode.BACK_SPACE) {
                crossword.setProposition(row, col, ' ');
                moveFocus(row, col, isHorizontal, -1);

            } else if (code == KeyCode.ENTER) {
                validateGrid();

            } else if (code == KeyCode.RIGHT) {
                if (!isHorizontal) {
                    isHorizontal = true; // Changer de direction sans déplacer
                } else {
                    moveFocus(row, col, true, 1);
                }
            } else if (code == KeyCode.LEFT) {
                if (!isHorizontal) {
                    isHorizontal = true;
                } else {
                    moveFocus(row, col, true, -1);
                }
            } else if (code == KeyCode.DOWN) {
                if (isHorizontal) {
                    isHorizontal = false; // Changer de direction sans déplacer
                } else {
                    moveFocus(row, col, false, 1);
                }
            } else if (code == KeyCode.UP) {
                if (isHorizontal) {
                    isHorizontal = false;
                } else {
                    moveFocus(row, col, false, -1);
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la saisie : " + e.getMessage());
        }
    }

    private void moveFocus(int row, int col, boolean horizontal, int step) {
        try {
            int nextR = row + (horizontal ? 0 : step);
            int nextC = col + (horizontal ? step : 0);

            if (crossword.correctCoords(nextR, nextC) && !crossword.isBlackSquare(nextR, nextC)) {
                currentRow = nextR;
                currentCol = nextC;
                for (Node node : gridPane.getChildren()) {
                    if (GridPane.getRowIndex(node) == nextR && GridPane.getColumnIndex(node) == nextC) {
                        node.requestFocus();
                        updateClueSelection(nextR, nextC);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            // case invalide, on ne bouge pas
        }
    }

    private void moveFocusTo(int row, int col) {
        currentRow = row;
        currentCol = col;
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col) {
                node.requestFocus();
                break;
            }
        }
    }

    /**
     * Met à jour la sélection dans les deux ListView selon la case (row, col).
     * L'indice de la direction courante est affiché en rouge, l'autre en défaut.
     */
    private void updateClueSelection(int row, int col) {
        if (horizontalListView == null || verticalListView == null) return;

        // Chercher l'indice horizontal qui couvre cette case
        Clue hClue = null;
        for (Clue clue : crossword.getHorizontalClues()) {
            if (clue.getRow() == row && clue.getColumn() <= col) {
                // Vérifier que le mot couvre bien la colonne
                hClue = clue;
                // On garde le dernier trouvé dont la colonne de départ <= col
                // (le plus proche à gauche)
            }
        }
        // Affiner : garder seulement celui dont la colonne est la plus grande <= col
        Clue bestHClue = null;
        for (Clue clue : crossword.getHorizontalClues()) {
            if (clue.getRow() == row && clue.getColumn() <= col) {
                if (bestHClue == null || clue.getColumn() > bestHClue.getColumn()) {
                    bestHClue = clue;
                }
            }
        }

        // Chercher l'indice vertical qui couvre cette case
        Clue bestVClue = null;
        for (Clue clue : crossword.getVerticalClues()) {
            if (clue.getColumn() == col && clue.getRow() <= row) {
                if (bestVClue == null || clue.getRow() > bestVClue.getRow()) {
                    bestVClue = clue;
                }
            }
        }

        if (bestHClue != null) {
            horizontalListView.getSelectionModel().select(bestHClue);
            horizontalListView.scrollTo(bestHClue);
        } else {
            horizontalListView.getSelectionModel().clearSelection();
        }

        if (bestVClue != null) {
            verticalListView.getSelectionModel().select(bestVClue);
            verticalListView.scrollTo(bestVClue);
        } else {
            verticalListView.getSelectionModel().clearSelection();
        }
    }

    // ===========================================
    // MÉTHODES MULTIJOUEUR ET AMÉLIORATIONS UI
    // ===========================================

    @FXML
    private void toggleMultiplayer() {
        isMultiplayerMode = multiplayerMode.isSelected();
        multiplayerInfo.setVisible(isMultiplayerMode);
        multiplayerInfo.setManaged(isMultiplayerMode);

        if (isMultiplayerMode && players.size() < 2) {
            addPlayer();
        }

        updatePlayerDisplay();
        updateGameStatus();
    }

    @FXML
    private void addPlayer() {
        TextInputDialog dialog = new TextInputDialog("Joueur " + (players.size() + 1));
        dialog.setTitle("Ajouter un joueur");
        dialog.setHeaderText("Entrez le nom du nouveau joueur:");
        dialog.setContentText("Nom:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            if (!name.trim().isEmpty()) {
                players.add(new Player(name.trim()));
                updatePlayerDisplay();
            }
        });
    }

    @FXML
    private void resetScores() {
        for (Player player : players) {
            player = new Player(player.getName()); // Reset all stats
        }
        updatePlayerDisplay();
        updateStats();
    }

    @FXML
    private void handleValidate() {
        validateGrid();
        checkForCompletion();
    }

    @FXML
    private void handleExit() {
        System.exit(0);
    }

    @FXML
    private void showAbout() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("À propos - Mots Croisés JavaFX");
        alert.setHeaderText("TP6 PRA - L3 ISTIC");
        alert.setContentText("Application de mots croisés interactive\n\n" +
                           "Développé par: Ibrahim Balde & Ibrahima Kalil Bah\n" +
                           "Technologies: JavaFX, MySQL, JDBC\n" +
                           "Version: 2.0 - Mode Multijoueur");
        alert.showAndWait();
    }

    private void startTimer() {
        gameStartTime = System.currentTimeMillis();
        if (gameTimer != null) {
            gameTimer.cancel();
        }
        gameTimer = new Timer();
        gameTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                javafx.application.Platform.runLater(() -> updateTimer());
            }
        }, 0, 1000);
    }

    private void updateTimer() {
        if (timerLabel != null && showTimer.isSelected()) {
            long elapsed = (System.currentTimeMillis() - gameStartTime) / 1000;
            timerLabel.setText(String.format("Timer: %02d:%02d", elapsed / 60, elapsed % 60));
        }
    }

    private void resetGame() {
        gameStartTime = System.currentTimeMillis();
        filledCells = 0;
        totalCells = 0;

        // Compter les cases blanches
        if (crossword != null) {
            for (int r = 0; r < crossword.getHeight(); r++) {
                for (int c = 0; c < crossword.getWidth(); c++) {
                    if (!crossword.isBlackSquare(r, c)) {
                        totalCells++;
                    }
                }
            }
        }

        updateStats();
        updateGameStatus();
    }

    private void updatePlayerDisplay() {
        if (currentPlayerLabel != null) {
            currentPlayerLabel.setText("Joueur: " + getCurrentPlayer().getName());
        }

        if (players.size() >= 1 && player1Score != null) {
            player1Score.setText(String.valueOf(players.get(0).getScore()));
        }
        if (players.size() >= 2 && player2Score != null) {
            player2Score.setText(String.valueOf(players.get(1).getScore()));
        }
    }

    private void updateStats() {
        if (statsLabel != null) {
            String stats = String.format("Cases remplies: %d/%d\nTemps: %s\nPrécision: %.1f%%",
                                       filledCells, totalCells,
                                       timerLabel.getText().replace("Timer: ", ""),
                                       getCurrentPlayer().getAccuracy());
            statsLabel.setText(stats);
        }
    }

    private void updateGameStatus() {
        if (gameStatusLabel != null) {
            if (isMultiplayerMode) {
                gameStatusLabel.setText("Mode multijoueur - Tour de " + getCurrentPlayer().getName());
            } else {
                gameStatusLabel.setText("Mode solo - Bonne chance !");
            }
        }
    }

    private Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    private void nextPlayer() {
        if (isMultiplayerMode && players.size() > 1) {
            getCurrentPlayer().endTurn();
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
            getCurrentPlayer().startTurn();
            updatePlayerDisplay();
            updateGameStatus();
        }
    }

    private void validateGrid() {
        try {
            int correctCount = 0;
            int totalValidated = 0;
            filledCells = 0;

            for (Node node : gridPane.getChildren()) {
                Integer r = GridPane.getRowIndex(node);
                Integer c = GridPane.getColumnIndex(node);

                if (r != null && c != null && !crossword.isBlackSquare(r, c)) {
                    char prop = crossword.getProposition(r, c);
                    char sol = crossword.getSolution(r, c);

                    // Compter les cases remplies
                    if (prop != ' ' && prop != '\0') {
                        filledCells++;
                    }

                    node.getStyleClass().remove("correct-cell");
                    node.getStyleClass().remove("wrong-cell");

                    if (prop != ' ' && prop != '\0') {
                        totalValidated++;
                        if (Character.toUpperCase(prop) == Character.toUpperCase(sol)) {
                            node.getStyleClass().add("correct-cell");
                            correctCount++;
                            if (isMultiplayerMode) {
                                getCurrentPlayer().addCorrectAnswer();
                            }
                        } else {
                            node.getStyleClass().add("wrong-cell");
                            if (isMultiplayerMode) {
                                getCurrentPlayer().addWrongAnswer();
                            }
                        }
                    }
                }
            }

            // Mettre à jour les statistiques
            updateStats();
            updatePlayerDisplay();

            // Passer au joueur suivant en mode multijoueur
            if (isMultiplayerMode && totalValidated > 0) {
                nextPlayer();
            }

        } catch (Exception e) {
            System.err.println("Erreur de validation : " + e.getMessage());
        }
    }

    private void animateLetter(Label label) {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), label);
        st.setFromX(0.7); st.setFromY(0.7);
        st.setToX(1.0);   st.setToY(1.0);
        st.play();
    }

    /**
     * Vérifie si la grille est complètement remplie et correcte, et affiche félicitations si oui
     */
    private void checkForCompletion() {
        try {
            if (crossword.isGridComplete()) {
                showCongratulations();
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la vérification : " + e.getMessage());
        }
    }

    /**
     * Affiche une boîte de dialogue de félicitations
     */
    private void showCongratulations() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("🎉 Félicitations ! 🎉");

        String winnerMessage = "";
        if (isMultiplayerMode && players.size() > 1) {
            // Trouver le gagnant
            Player winner = players.stream()
                    .max((p1, p2) -> Integer.compare(p1.getScore(), p2.getScore()))
                    .orElse(players.get(0));

            alert.setHeaderText("Partie terminée !");
            winnerMessage = String.format("\nGagnant: %s (Score: %d)\n\n",
                                        winner.getName(), winner.getScore());

            // Afficher les scores de tous les joueurs
            StringBuilder scores = new StringBuilder("Scores finaux:\n");
            for (Player player : players) {
                scores.append(String.format("• %s: %d points (%.1f%% précision)\n",
                                          player.getName(), player.getScore(), player.getAccuracy()));
            }
            alert.setContentText("La grille a été complétée !" + winnerMessage + scores.toString() +
                               "\nVoulez-vous commencer une nouvelle partie ?");
        } else {
            alert.setHeaderText("Grille complétée !");
            alert.setContentText("Bravo ! Vous avez réussi à compléter la grille des mots croisés correctement.\n\n" +
                               "Temps: " + timerLabel.getText() + "\n" +
                               "Précision: " + String.format("%.1f%%", getCurrentPlayer().getAccuracy()) + "\n\n" +
                               "Voulez-vous jouer une nouvelle partie cher " + getCurrentPlayer().getName() + " ?");
        }

        alert.showAndWait().ifPresent(response -> {
            try {
                handleNewGrid();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
