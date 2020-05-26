package util;

/**
 * This class function helps to provide color for a {@link game.state.Block}.
 */
public class ColorHelper {
    /**
     * This function returns the block color by point.
     *
     * @param num Number of color index.
     * @return The block color.
     */
    public static String getColorByBlock(int num) {
        int c = 0;
        while((num = num / 2) >= 1)
            c++;

        return Config.COLORS[c];
    }

    /**
     * This function returns the block text color by point.
     *
     * @param num Number of color index.
     * @return The label text color.
     */
    public static String getTextColorByBlock(int num) {
        String base = getColorByBlock(num);

        int color = (int)Long.parseLong(base.substring(1), 16);
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = (color >> 0) & 0xFF;

        return String.format("#%02x%02x%02x", 255 - r, 255 - g, 255 - b);
    }
}
