package game.state;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Class for storing XY coordinates
 */
@Data
@AllArgsConstructor
public class Point {
    /**
     * X coordinate
     */
    public int x;

    /**
     * Y coordinate
     */
    public int y;
}
