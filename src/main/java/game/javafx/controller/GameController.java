package game.javafx.controller;

import game.state.Block;
import game.state.GameTable;
import game.state.Point;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DurationFormatUtils;
import game.results.GameResult;
import game.results.GameResultDao;
import util.Config;

import javax.inject.Inject;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Slf4j
public class GameController {

    @Inject
    private FXMLLoader fxmlLoader;

    @Inject
    private GameResultDao gameResultDao;

    private String playerName;
    private GameTable gameTable;
    private Instant startTime;

    @FXML
    private Label messageLabel;

    @FXML
    private GridPane gameGrid;

    @FXML
    private Label stepsLabel;

    @FXML
    private Label stopWatchLabel;

    private Timeline stopWatchTimeline;

    @FXML
    private Button resetButton;

    @FXML
    private Button giveUpButton;

    private BooleanProperty gameOver = new SimpleBooleanProperty();

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    @FXML
    public void initialize() {
        gameOver.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                log.info("Game is over");
                log.debug("Saving result to database...");
                gameResultDao.persist(createGameResult());
                stopWatchTimeline.stop();
            }
        });
        resetGame();
    }

    private void resetGame() {
        gameTable = new GameTable(Config.SIZE.getValue(), Config.SIZE.getValue());

        startTime = Instant.now();
        gameOver.setValue(false);
        createStopWatch();
        Platform.runLater(() -> messageLabel.setText("Good luck, " + playerName + "!"));

        gameGrid.getChildren().removeIf(Block.class::isInstance);
        for (int i = 0; i < Config.SIZE.getValue(); i++) {
            for (int j = 0; j < Config.SIZE.getValue(); j++) {
                Block block = new Block();
                gameTable.addBlock(new Point(i, j), block);
                gameGrid.add(block, i, j);
            }
        }

        gameTable.startGame();
    }

    @FXML
    public void handleKeyPressedEvent(KeyEvent event) {
        log.debug("Pressed button -> {}", event.getCode().toString());

        if (gameTable.isFinished())
            return;

        switch(event.getCode()) {
            case DOWN:
            case S:
                gameTable.moveDown();
                break;

            case UP:
            case W:
                gameTable.moveUp();
                break;

            case LEFT:
            case A:
                gameTable.moveLeft();
                break;

            case RIGHT:
            case D:
                gameTable.moveRight();
                break;

            default:
                break;
        }

        stepsLabel.setText(String.format("%d", gameTable.getScore()));

        if (gameTable.isFinished()) {
            giveUpButton.setText("Finish");
            gameOver.setValue(true);
            resetButton.setDisable(true);
            if (gameTable.isFinished() && gameTable.checkAnyMovable()) {
                messageLabel.setText("Congratulations, " + playerName + "!");
            } else {
                messageLabel.setText("Game Over, " + playerName + "!");
            }
        }
    }

    public void handleResetButton(ActionEvent actionEvent)  {
        log.debug("{} is pressed", ((Button) actionEvent.getSource()).getText());
        log.info("Resetting game...");
        stopWatchTimeline.stop();
        resetGame();
    }

    public void handleGiveUpButton(ActionEvent actionEvent) throws IOException {
        String buttonText = ((Button) actionEvent.getSource()).getText();
        log.debug("{} is pressed", buttonText);
        if (buttonText.equals("Give Up")) {
            log.info("The game has been given up");
        }
        gameOver.setValue(true);
        log.info("Loading high scores scene...");
        fxmlLoader.setLocation(getClass().getResource("/fxml/highscores.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private GameResult createGameResult() {
        GameResult result = GameResult.builder()
                .player(playerName)
                .solved(gameTable.isFinished())
                .grid(Config.SIZE.getValue())
                .duration(Duration.between(startTime, Instant.now()))
                .score(gameTable.getScore())
                .build();
        return result;
    }

    private void createStopWatch() {
        stopWatchTimeline = new Timeline(new KeyFrame(javafx.util.Duration.ZERO, e -> {
            long millisElapsed = startTime.until(Instant.now(), ChronoUnit.MILLIS);
            stopWatchLabel.setText(DurationFormatUtils.formatDuration(millisElapsed, "HH:mm:ss"));
        }), new KeyFrame(javafx.util.Duration.seconds(1)));
        stopWatchTimeline.setCycleCount(Animation.INDEFINITE);
        stopWatchTimeline.play();
    }

}
