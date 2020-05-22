package game.javafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import javafx.scene.image.Image;
import util.Config;

import javax.inject.Inject;
import java.io.IOException;

@Slf4j
public class LaunchController {

    @Inject
    private FXMLLoader fxmlLoader;

    @FXML
    private Pane mainPane;

    @FXML
    private TextField playerNameTextField;

    @FXML
    private ComboBox gridSize;

    @FXML
    private ComboBox maxPoints;

    @FXML
    private Label errorLabel;

    @FXML
    public void initialize() {

        maxPoints.getItems().addAll(
                "512",
                "1024",
                "2048",
                "4096",
                "8192"
        );
        maxPoints.setValue(String.format("%d", Config.MAX_POINT.getValue()));

        gridSize.getItems().addAll(
                "3x3",
                "4x4",
                "5x5",
                "6x6",
                "8x8"
        );
        gridSize.setValue(Config.SIZE.getValue() + "x" + Config.SIZE.getValue());

        Image img = new Image(getClass().getResource("/images/background.jpg").toExternalForm(), 1920, 1080, false, true);
        BackgroundImage myBI = new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);

        mainPane.setBackground(new Background(myBI));
    }

    public void startAction(ActionEvent actionEvent) throws IOException {
        if (playerNameTextField.getText().isEmpty()) {
            errorLabel.setText("Enter your name!");
        } else {
            Config.SIZE.setValue(Integer.parseInt(((String) gridSize.getValue()).split("x")[0]));
            Config.MAX_POINT.setValue(Integer.parseInt((String) maxPoints.getValue()));

            fxmlLoader.setLocation(getClass().getResource("/fxml/game.fxml"));
            Parent root = fxmlLoader.load();
            fxmlLoader.<GameController>getController().setPlayerName(playerNameTextField.getText());
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
            log.info("The players name is set to {}, loading game scene", playerNameTextField.getText());
        }
    }

}
