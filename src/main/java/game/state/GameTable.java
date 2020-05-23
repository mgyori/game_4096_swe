package game.state;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import util.Config;

import java.util.HashMap;
import java.util.Random;

/**
 * Class for 4096 game table logic.
 */
@Slf4j
public class GameTable {
    private HashMap<Point, Block> blocks;
    @Getter
    private int rows;
    @Getter
    private int cols;
    private Random rand;
    @Getter
    private int score;
    private boolean canAddNew;
    @Getter
    private boolean finished;

    public GameTable(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.rand = new Random();
        this.blocks = new HashMap<Point, Block>();
    }

    /**
     * Add game block to the specified location.
     *
     * @param point The block in x-y coordinate.
     * @param block The block.
     */
    public void addBlock(Point point, Block block) {
        this.blocks.put(point, block);
    }

    /**
     * Get the game block from specified location.
     *
     * @param x The block X coordinate.
     * @param y The block Y coordinate.
     * @return The block in x-y coordinate.
     */
    public Block getBlock(int x, int y) {
        return this.blocks.get(new Point(x, y));
    }

    /**
     * Function for rendering and updateing the block and the table design.
     */
    public void render() {
        for(Block b : this.blocks.values())
            b.render();
    }

    /**
     * Function for checking for available free space in table.
     *
     * @return Returns true if exists free space.
     */
    private boolean checkFreeSpace() {
        for(Block b : this.blocks.values())
            if (b.getPoint() == 0)
                return true;
        return false;
    }

    /**
     * Method to add random block to the table.
     */
    private void addRandomBlock() {
        if (!checkFreeSpace())
            return;

        int x = 0, y = 0;

        do {
            x = this.rand.nextInt(this.cols);
            y = this.rand.nextInt(this.rows);
        } while (this.getBlock(x, y).getPoint() != 0);

        log.debug("Add new block to x: {} y: {}!", x, y);

        this.getBlock(x, y).setPoint(this.rand.nextInt(10) < 4 ? 4 : 2);
        this.getBlock(x, y).setNew(true);
    }

    /**
     * Method to start the game and reset score (also viewed blocks).
     */
    public void startGame() {
        log.info("Start game!");

        this.canAddNew = true;
        this.score = 0;
        this.finished = false;

        for(Block b : this.blocks.values()) {
            b.setPoint(0);
            b.setNew(false);
            b.setLocked(false);
        }

        this.nextRound();
    }

    /**
     * Method to start the next round. This method unlocks all blocks and adds X random blocks to a random positions.
     */
    private void nextRound() {
        log.debug("Start next round!");

        this.unlockAllBlock();

        if (!this.checkFreeSpace()) {
            log.warn("No free space!");
        } else {
            if (canAddNew) {
                for (int i = 0; i < Config.ADDED_BLOCKS.getValue(); i++)
                    this.addRandomBlock();
            }
        }

        this.render();

        if ((!checkAnyMovable() && !canAddNew) || (this.rows + this.cols) == 0)
            this.finished = true;
    }

    /**
     * Check for any movable blocks in a table.
     *
     * @return Returns true if any block is movable in table.
     */
    public boolean checkAnyMovable() {
        for (int i = 0; i < this.rows - 1; i++) {
            for (int j = 0; j < this.cols - 1; j++) {
                Block thisBlock = this.getBlock(i, j);
                Block downerBlock = this.getBlock(i + 1, j);
                Block rightBlock = this.getBlock(i, j + 1);

                if (thisBlock.getPoint() == 0 || downerBlock.getPoint() == 0 || rightBlock.getPoint() == 0)
                    return true;
                else if (thisBlock.getPoint() == downerBlock.getPoint() || thisBlock.getPoint() == rightBlock.getPoint())
                    return true;
            }
        }

        return false;
    }

    /**
     * This function unlocks all blocks in the table. If the block is locked it won't be movable anymore.
     */
    private void unlockAllBlock() {
        log.debug("Unlocked all block");
        for(Block b : this.blocks.values()) {
            b.setLocked(false);
            b.setNew(false);
        }
    }

    /**
     * This method adds up the points of two blocks if the result is not null, the target block will have that value.
     * If the point reaches the maximum possible points (according to Config class) the finished variable will be set to True.
     *
     * @param selected The selected block.
     * @param target The target block.
     */
    private void processBlocks(Block selected, Block target) {
        if (selected.getPoint() != 0 && target.getPoint() == 0) {
            target.setPoint(selected.getPoint());
            selected.setPoint(0);
            selected.setLocked(false);
            target.setLocked(false);
            this.canAddNew = true;
        } else if (selected.getPoint() != 0 && selected.getPoint() == target.getPoint()) {
            this.score+=selected.getPoint();
            this.canAddNew = true;
            target.setPoint(selected.getPoint() * 2);
            selected.setPoint(0);
            selected.setLocked(false);
            target.setLocked(true);

            if (target.getPoint() == Config.MAX_POINT.getValue())
                this.finished = true;
        }
    }

    /**
     * Method to move all blocks (if movable) down.
     */
    public void moveDown() {
        log.debug("Move down");
        this.canAddNew = false;
        for (int i = 0; i < this.rows; i++) {
            for (int j = this.cols - 2; j >= 0; j--) {
                int thisX = i,
                        thisY = j - 1;
                while (true) {
                    thisY++;

                    if (thisY + 1 >= this.cols)
                        break;

                    Block thisBlock = this.getBlock(thisX, thisY);
                    if (thisBlock.isLocked())
                        continue;

                    Block downerBlock = this.getBlock(thisX, thisY + 1);
                    if (downerBlock.isLocked())
                        continue;

                    this.processBlocks(thisBlock, downerBlock);
                }
            }
        }

        this.nextRound();
    }

    /**
     * Method to move all blocks (if movable) up.
     */
    public void moveUp() {
        log.debug("Move up");
        this.canAddNew = false;
        for (int i = 0; i < this.rows; i++) {
            for (int j = 1; j < this.cols; j++) {
                int thisX = i,
                        thisY = j + 1;
                while (true) {
                    thisY--;

                    if (thisY - 1 < 0)
                        break;

                    Block thisBlock = this.getBlock(thisX, thisY);
                    if (thisBlock.isLocked())
                        continue;

                    Block upperBlock = this.getBlock(thisX, thisY - 1);
                    if (upperBlock.isLocked())
                        continue;

                    this.processBlocks(thisBlock, upperBlock);
                }
            }
        }

        this.nextRound();
    }

    /**
     * Method to move all blocks (if movable) left.
     */
    public void moveLeft() {
        log.debug("Move Left");
        this.canAddNew = false;
        for (int i = 1; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                int thisX = i + 1,
                        thisY = j;
                while (true) {
                    thisX--;

                    if (thisX - 1 < 0)
                        break;

                    Block thisBlock = this.getBlock(thisX, thisY);
                    if (thisBlock.isLocked())
                        continue;

                    Block leftBlock = this.getBlock(thisX - 1, thisY);
                    if (leftBlock.isLocked())
                        continue;

                    this.processBlocks(thisBlock, leftBlock);
                }
            }
        }

        this.nextRound();
    }

    /**
     * Method to move all blocks (if movable) right.
     */
    public void moveRight() {
        log.debug("Move Right");
        this.canAddNew = false;
        for (int i = this.rows - 2; i >= 0; i--) {
            for (int j = 0; j < this.cols; j++) {
                int thisX = i - 1,
                        thisY = j;
                while (true) {
                    thisX++;

                    if (thisX + 1 >= this.rows)
                        break;

                    Block thisBlock = this.getBlock(thisX, thisY);
                    if (thisBlock.isLocked())
                        continue;

                    Block rightBlock = this.getBlock(thisX + 1, thisY);
                    if (rightBlock.isLocked())
                        continue;

                    this.processBlocks(thisBlock, rightBlock);
                }
            }
        }

        this.nextRound();
    }
}