package swapstones.controller;

import gameresult.OnePlayerGameResult;
import gameresult.manager.OnePlayerGameResultManager;
import gameresult.manager.json.JsonOnePlayerGameResultManager;
import javafx.beans.property.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import lombok.Setter;
import puzzle.TwoPhaseMoveState;
import swapstones.model.PuzzleState;
import swapstones.model.Stone;
import swapstones.util.Stopwatch;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.tinylog.Logger;


import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.time.ZonedDateTime;

public class GameController {

    @FXML
    private GridPane board;

    @FXML
    private TextField numberOfMovesField;

    @FXML
    private Label stopwatchLabel;

    public Button GiveUp;

    private final Stopwatch stopwatch = new Stopwatch();

    private PuzzleState model = new PuzzleState();
    private UserViewController userView = new UserViewController();

    private final IntegerProperty numberOfMoves = new SimpleIntegerProperty(0);

    private int fromChosen = -1;

    private int toChosen = -1;
    @Setter
    private String playerName;
    @Setter
    private boolean solved;
    @Setter
    private Duration duration;
    @Setter
    private ZonedDateTime created;

    private OnePlayerGameResultManager gameResultManager;

    public GameController() {
        this.gameResultManager = new JsonOnePlayerGameResultManager(Path.of("one-player-results.json"));
    }

    private void bindNumberOfMoves() {
        numberOfMovesField.textProperty().bind(numberOfMoves.asString());
    }

    @FXML
    private void initialize() {
        for (var j = 0; j < board.getColumnCount(); j++) {
            var square = createSquare(j);
            board.add(square, j, 0);
        }
        registerKeyEventHandler();
        bindNumberOfMoves();
        stopwatchLabel.textProperty().bind(stopwatch.hhmmssProperty());
        stopwatch.start();
        created = ZonedDateTime.now();
    }

    private StackPane createSquare(int col) {
        var square = new StackPane();
        square.getStyleClass().add("square");
        var piece = new Circle(12);
        var color = getColor(model.getStone(col));
        piece.setFill(color);
        square.getChildren().add(piece);
        square.setOnMouseClicked(this::handleMouseClick);
        return square;
    }

    @FXML
    private void handleMouseClick(MouseEvent event) {
        var source = (Node) event.getSource();
        var col = GridPane.getColumnIndex(source);
        Logger.debug("Click on square ({})", col);

        if (fromChosen == -1) {
            fromChosen = col;
            Logger.debug("Selected square to move from: {}", fromChosen);
        } else {
            toChosen = col;
            Logger.debug("Selected square to move to: {}", toChosen);

            makeMoveIfLegal(fromChosen, toChosen);

            fromChosen = -1;
            toChosen = -1;
        }
    }

    private void registerKeyEventHandler() {
        Platform.runLater(() -> board.getScene().setOnKeyPressed(this::handleKeyPress));
    }

    @FXML
    private void handleKeyPress(KeyEvent keyEvent) {
        var quitKeyCombination = new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN);
        if (quitKeyCombination.match(keyEvent)) {
            Logger.debug("Exiting");
            Platform.exit();
        }
    }

    private void makeMoveIfLegal(int fromIndex, int toIndex) {
        TwoPhaseMoveState.TwoPhaseMove<Integer> move = new TwoPhaseMoveState.TwoPhaseMove<>(fromIndex, toIndex);
        if (model.isLegalMove(move)) {
            model.makeMove(move);
            updateUI();
            numberOfMoves.set(numberOfMoves.get() + 1);

            if (model.isSolved()) {
                solved = model.isSolved();
                setGameResultManager();
                showSolvedAlertAndExit();
            }
        } else {
            Logger.warn("Attempted illegal move: {}", move);
        }
    }

    private void updateUI() {
        for (var j = 0; j < board.getColumnCount(); j++) {
            updateSquare(0, j);
        }
    }

    private void updateSquare(int row, int col) {
        var piece = (Circle) ((StackPane) getNodeFromGrid(row, col)).getChildren().get(0);
        var color = getColor(model.getStone(col));
        piece.setFill(color);
    }

    private Color getColor(Stone stone) {
        return switch (stone) {
            case EMPTY -> Color.TRANSPARENT;
            case HEAD -> Color.BLACK;
            case TAIL -> Color.RED;
        };
    }

    public Node getNodeFromGrid(int row, int col) {
        for (var square : board.getChildren()) {
            if (GridPane.getRowIndex(square) == row && GridPane.getColumnIndex(square) == col) {
                return square;
            }
        }
        throw new AssertionError();
    }


    public void handleSwitchScene() throws IOException {
        Stage stage = (Stage) board.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/tableview.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }


    private void setGameResultManager(){
        duration = Duration.ofSeconds(stopwatch.secondsProperty().get());
        OnePlayerGameResult gameResult = new OnePlayerGameResult(playerName, solved, numberOfMoves.intValue());
        gameResult.setDuration(duration);
        gameResult.setCreated(created);
        try {
            gameResultManager.add(gameResult);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void handleGiveUp(){
        solved = model.isSolved();
        setGameResultManager();
        try {
            handleSwitchScene();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void showSolvedAlertAndExit() {
        var alert = new Alert(Alert.AlertType.INFORMATION);
        stopwatch.stop();
        alert.setHeaderText("Puzzle is solved");
        alert.setContentText("Congratulations! You solved the puzzle");
        alert.showAndWait();
        try {
            handleSwitchScene();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
