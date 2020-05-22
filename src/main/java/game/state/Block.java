package game.state;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import lombok.Getter;
import lombok.Setter;
import util.ColorHelper;
import util.Config;

public class Block extends StackPane {
    @Getter @Setter
    private int point;
    private GameTable table;
    private Label label;
    @Getter @Setter
    private boolean locked;
    @Getter @Setter
    private boolean isNew;

    public Block(GameTable table, Point p) {
        this.table = table;
        this.locked = false;
        this.isNew = false;

        this.setPrefSize(Config.WIDTH.getValue() / Config.SIZE.getValue(), Config.HEIGHT.getValue() / Config.SIZE.getValue());

        this.label = new Label();
        this.getChildren().add(this.label);

        this.setPoint(0);
        this.render();
    }

    public void render() {
        this.setStyle("-fx-background-color: " + ColorHelper.getColorByBlock(this.point) + "; -fx-border-color: black");
        this.label.setStyle("-fx-font-size:32px; -fx-text-fill: " + ColorHelper.getTextColorByBlock(this.point) + "; -fx-effect: dropshadow(one-pass-box, black, 4, 0.0, 0, 0)");
        if (this.getPoint() == 0)
            this.label.setText("");
        else
            this.label.setText(String.format("%d", this.getPoint()));
    }
}
