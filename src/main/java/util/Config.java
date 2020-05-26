package util;

/**
 * Class for game configuration.
 */
public enum Config {
    /**
     * The grid width.
     */
    WIDTH(512),

    /**
     * The grid height.
     */
    HEIGHT(512),

    /**
     * The grid size (N x N).
     */
    SIZE(5),

    /**
     * Maximum point of game.
     */
    MAX_POINT(4096),

    /**
     * Number of added blocks per round.
     */
    ADDED_BLOCKS(2);

    private int value;

    /**
     * Constructor of {@link Config} class.
     * @param value Value of the enum.
     */
    Config(int value) {
        this.value = value;
    };

    /**
     * Get config enum value.
     * @return Returns an integer value.
     */
    public int getValue() {
        return this.value;
    }

    /**
     * Set config enum value.
     * @param value The new enum value.
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**
     * Colors of block levels.
     */
    public static String[] COLORS = new String[] { "#e9faa7", "#49d186", "#25b867", "#187843", "#1d968c", "#1d7096",
            "#1f2aa3", "#9514db", "#d41eb8", "#e01063", "#ff0a3b", "#940925", "#b52414" };
}
