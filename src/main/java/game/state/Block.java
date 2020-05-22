package game.state;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import lombok.Getter;
import lombok.Setter;
import util.ColorHelper;
import util.Config;

/**
 * Block class for 4096 game blocks.
 */
public class Block extends StackPane {
    /**
     * Returns the block points. This is can be set by SetPoint function.
     * @return Returns the block points.
     */
    @Getter @Setter
    private int point;

    private Label label;

    /**
     * Returns the block lock state.
     * @return Returns that block is locked.
     */
    @Getter @Setter
    private boolean locked;

    /**
     * Return the block new state. If true the block is was not opened yet.
     * @return Return the block state.
     */
    @Getter @Setter
    private boolean isNew;

    public Block() {
        this.locked = false;
        this.isNew = false;

        this.setPrefSize(Config.WIDTH.getValue() / Config.SIZE.getValue(), Config.HEIGHT.getValue() / Config.SIZE.getValue());

        this.label = new Label();
        this.getChildren().add(this.label);

        this.setPoint(0);
        this.render();
    }

    /**
     * Function for render the block. This function update the block design.
     */
    public void render() {
        this.setStyle("-fx-background-color: " + ColorHelper.getColorByBlock(this.point) + "; -fx-border-color: black");
        this.label.setStyle("-fx-font-size:32px; -fx-text-fill: " + ColorHelper.getTextColorByBlock(this.point) + "; -fx-effect: dropshadow(one-pass-box, black, 4, 0.0, 0, 0)");
        if (this.getPoint() == 0)
            this.label.setText("");
        else
            this.label.setText(String.format("%d", this.getPoint()));
    }
}
