package game.state;


import com.sun.javafx.application.PlatformImpl;
import org.junit.jupiter.api.Test;

import util.Config;

import static org.junit.jupiter.api.Assertions.*;

class GameTableTest {
    GameTable createEmptyTable(int size) {
        PlatformImpl.startup(()->{});

        GameTable table = new GameTable(size, size);
        for(int i = 0; i < size; i++)
            for(int j = 0; j < size; j++)
                table.addBlock(new Point(i, j), new Block());
        return table;
    }

    @Test
    void testAddBlock() {
        GameTable table = new GameTable(1, 1);
        Block block = new Block();
        table.addBlock(new Point(0, 0), block);
        assertEquals(block, table.getBlock(0, 0));
    }

    @Test
    void testCheckAnyMovable() {
        GameTable table = createEmptyTable(2);
        assertTrue(table.checkAnyMovable());
        table.getBlock(0, 1).setPoint(1);
        table.getBlock(1, 1).setPoint(2);
        table.getBlock(0, 0).setPoint(3);
        table.getBlock(1, 0).setPoint(4);
        assertFalse(table.checkAnyMovable());
    }

    @Test
    void testMoveDown() {
        GameTable table = createEmptyTable(2);

        // Trying to move block down
        table.getBlock(0, 0).setPoint(2);
        table.moveDown();
        assertEquals(2, table.getBlock(0, 1).getPoint());

        table.getBlock(0, 0).setPoint(2);

        // Trying to lock target block
        table.getBlock(0, 0).setLocked(true);
        table.moveDown();
        assertEquals(2, table.getBlock(0, 1).getPoint());
        table.getBlock(0, 0).setLocked(false);

        // Trying to lock downer block
        table.getBlock(0, 1).setLocked(true);
        table.moveDown();
        assertEquals(2, table.getBlock(0, 1).getPoint());
        table.getBlock(0, 1).setLocked(false);

        // Trying to adds two block
        table.moveDown();
        assertEquals(4, table.getBlock(0, 1).getPoint());
    }

    @Test
    void testMoveUp() {
        GameTable table = createEmptyTable(2);

        // Trying to move block up
        table.getBlock(0, 1).setPoint(2);
        table.moveUp();
        assertEquals(2, table.getBlock(0, 0).getPoint());

        table.getBlock(0, 1).setPoint(2);

        // Trying to lock target block
        table.getBlock(0, 1).setLocked(true);
        table.moveUp();
        assertEquals(2, table.getBlock(0, 0).getPoint());
        table.getBlock(0, 1).setLocked(false);

        // Trying to lock downer block
        table.getBlock(0, 0).setLocked(true);
        table.moveUp();
        assertEquals(2, table.getBlock(0, 0).getPoint());
        table.getBlock(0, 0).setLocked(false);

        // Trying to adds two block
        table.moveUp();
        assertEquals(4, table.getBlock(0, 0).getPoint());
    }

    @Test
    void testMoveLeft() {
        GameTable table = createEmptyTable(2);

        // Trying to move block left
        table.getBlock(1, 0).setPoint(2);
        table.moveLeft();
        assertEquals(2, table.getBlock(0, 0).getPoint());

        table.getBlock(1, 0).setPoint(2);

        // Trying to lock target block
        table.getBlock(1, 0).setLocked(true);
        table.moveLeft();
        assertEquals(2, table.getBlock(0, 0).getPoint());
        table.getBlock(1, 0).setLocked(false);

        // Trying to lock downer block
        table.getBlock(0, 0).setLocked(true);
        table.moveLeft();
        assertEquals(2, table.getBlock(0, 0).getPoint());
        table.getBlock(0, 0).setLocked(false);

        // Trying to adds two block
        table.moveLeft();
        assertEquals(4, table.getBlock(0, 0).getPoint());
    }

    @Test
    void testMoveRight() {
        GameTable table = createEmptyTable(2);

        // Trying to move block left
        table.getBlock(0, 0).setPoint(2);
        table.moveRight();
        assertEquals(2, table.getBlock(1, 0).getPoint());

        table.getBlock(0, 0).setPoint(2);

        // Trying to lock target block
        table.getBlock(0, 0).setLocked(true);
        table.moveRight();
        assertEquals(2, table.getBlock(1, 0).getPoint());
        table.getBlock(0, 0).setLocked(false);

        // Trying to lock downer block
        table.getBlock(1, 0).setLocked(true);
        table.moveRight();
        assertEquals(2, table.getBlock(0, 0).getPoint());
        table.getBlock(1, 0).setLocked(false);

        // Trying to adds two block
        table.moveRight();
        assertEquals(4, table.getBlock(1, 0).getPoint());
    }

    @Test
    void testFinished() {
        GameTable table = createEmptyTable(2);
        assertFalse(table.isFinished());

        table.getBlock(0, 0).setPoint(Config.MAX_POINT.getValue() / 2);
        table.getBlock(1, 0).setPoint(Config.MAX_POINT.getValue() / 2);
        table.moveRight();
        assertTrue(table.isFinished());
    }

    @Test
    void testStartGame() {
        GameTable table = createEmptyTable(1);
        table.startGame();
        table.getBlock(0, 0).setPoint(Config.MAX_POINT.getValue());
        table.moveRight();
        assertTrue(table.isFinished());
    }

    @Test
    void testInitedValues() {
        GameTable table = createEmptyTable(1);
        assertEquals(0, table.getScore());
    }
}
