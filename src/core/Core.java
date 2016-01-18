package core;

import java.util.Random;
import com.googlecode.lanterna.input.Key.Kind;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.ScreenCharacterStyle;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.Terminal.Color;
import menus.PauseMenu;
import symbols.*;

/**
 * @author Felix Wohnhaas This class implements the core functionality of the
 * game
 */
public class Core extends Window {

    final char idWall = '0';
    final char idIn = '1';
    final char idOut = '2';
    final char idStaticTrap = '3';
    final char idDynamicTrap = '4';
    final char idKey = '5';
    final char empty = '6';
    final char idPlayer = '7';
    final char idCollectible = '8';
    final int realWidth = getResolutionX();
    final int realHeight = getResolutionY() - 3;
    public static DynamicEnemy[] dynamicEnemies;
    int enemies = 0;

    KeyListener listener = new KeyListener(getScreen());
    public static Coordinates region;

    /**
     * Constructor of the core
     *
     * @param screen the screen to use
     * @param resolutionX the resolution in x direction to use
     * @param resolutionY the resolution in y direction to use
     * @param filename the filename to determine the level
     */
    public Core(Screen screen, int resolutionX, int resolutionY, String filename) {
        super(resolutionX, resolutionY, screen);
        Game.io.loadProperties(filename);
        Game.io.createLevelData();
    }

    /**
     * Sets region and draws level. Starts the core of the game by calling the
     * update() Method.
     */
    public void start() {
        getScreen().clear();
        if (region == null) {
            region = region(Game.io.getLvl());
        }
        drawLevel();
        update();
    }

    /**
     * This method checks the level for entries to the Labyrith to find the
     * right region to draw when a new game starts.
     *
     * @param lvl the level to check for the right region
     * @return the found region as a Coordinates Object
     */
    public Coordinates region(char[][] lvl) {
        int width = Game.io.getWidth();
        int height = Game.io.getHeight();
        if (Game.io.getWidth() > realWidth || Game.io.getHeight() > realHeight) {
            for (int i = 0; i < lvl[0].length; i++) {
                if (lvl[0][i] == idIn) {
                    return new Coordinates(0, i / realHeight);
                }
                if (lvl[width - 1][i] == idIn) {
                    return new Coordinates(width / realWidth, i / realHeight);
                }
            }
            for (int i = 0; i < height; i++) {
                if (lvl[i][0] == idIn) {
                    return new Coordinates(i / realWidth, 0);

                }
                if (lvl[i][height - 1] == idIn) {
                    return new Coordinates(i / (realWidth), height / (realHeight));
                }
            }
        }
        return new Coordinates(0, 0);
    }

    /**
     * Iterates through all dynamic enemies
     */
    public void dynamicEnemy() {
        for (int i = 0; i < dynamicEnemies.length; i++) {
            if (dynamicEnemies[i] != null) {
                dynamicEnemies[i] = moveEnemy(dynamicEnemies[i]);
                drawSymbol(dynamicEnemies[i]);
            }
        }
        getScreen().refresh();
        getScreen().getTerminal().setCursorVisible(false);
    }

    /**
     * Moves the dynamic enemies
     *
     * @param enemy the enemy to move
     * @return the new, moved enemy
     */
    public DynamicEnemy moveEnemy(DynamicEnemy enemy) {
        Random rand = new Random();
        int direction = rand.nextInt(4);
        Coordinates pos = enemy.getPosition();
        int offsetX = 0;
        int offsetY = 0;
        switch (direction) {
            case 0:
                offsetX = -1;
                break;
            case 1:
                offsetX = 1;
                break;
            case 2:
                offsetY = -1;
                break;
            case 3:
                offsetY = 1;
                break;
        }
        int x = pos.getX() + region.getX() * realWidth;
        int y = pos.getY() + region.getY() * realHeight;
        if (x < Game.io.getWidth() && y < Game.io.getHeight()) {
            char lvlPos = Game.io.getLvl()[x + offsetX][y + offsetY];
            if (lvlPos != idIn && lvlPos != idOut && lvlPos != idWall) {
                Game.io.getLvl()[x][y] = empty;
                Game.io.getLvl()[x + offsetX][y + offsetY] = idDynamicTrap;
                drawColoredString(" ", Color.BLACK, Color.BLACK, null, pos.getX(), pos.getY());
                return new DynamicEnemy(pos.getX() + offsetX, pos.getY() + offsetY);
            }
        }
        return enemy;
    }

    /**
     * Draws the border to separate the GUI from the game
     */
    public void drawBorder() {
        for (int x = 0; x < realWidth; x++) {
            drawColoredString("\u2550", Color.GREEN, Color.BLACK, null, x, realHeight);
            drawColoredString(" ", Color.BLACK, Color.BLACK, null, x, realHeight + 1);
        }
        drawColoredString("Lives: ", Color.WHITE, Color.BLACK, ScreenCharacterStyle.Bold, 3, realHeight + 1);
        for (int i = 0; i < Game.player.getLives(); i++) {
            drawColoredString("\u2665", Color.RED, Color.BLACK, null, 2 * i + 10, realHeight + 1);
        }
        drawColoredString("Key: ", Color.WHITE, Color.BLACK, ScreenCharacterStyle.Bold, 20, realHeight + 1);
        if (Game.player.hasKey()) {
            drawColoredString("\u06DE", Color.CYAN, Color.BLACK, null, 26, realHeight + 1);
        }
        drawColoredString("Score: " + Game.player.getScore(), Color.WHITE, Color.BLACK, ScreenCharacterStyle.Bold, 30, realHeight + 1);
        getScreen().refresh();
    }

    /**
     * Draws a specific region of the level
     */
    public void drawLevel() {
        enemies = 0;
        dynamicEnemies = new DynamicEnemy[50];
        int levelX = 0;
        int levelY = 0;
        Entry entry = null;
        int posX = 0;
        int posY = 0;
        for (int x = 0; x < realWidth; x++) {
            for (int y = 0; y < realHeight; y++) {
                levelX = x + realWidth * region.getX();
                levelY = y + realHeight * region.getY();
                if (levelY < Game.io.getHeight() && levelX < Game.io.getWidth()) {
                    switch (Game.io.getLvl()[levelX][levelY]) {
                        case idWall:
                            drawSymbol(new Wall(x, y));
                            break;
                        case idIn:
                            entry = new Entry(x, y);
                            posX = x;
                            posY = y;
                            drawSymbol(entry);
                            break;
                        case idOut:
                            drawSymbol(new Exit(x, y));
                            break;
                        case idStaticTrap:
                            drawSymbol(new StaticEnemy(x, y));
                            break;
                        case idDynamicTrap:
                            DynamicEnemy enemy = new DynamicEnemy(x, y);
                            drawSymbol(enemy);
                            dynamicEnemies[enemies] = enemy;
                            enemies++;
                            break;
                        case idKey:
                            drawSymbol(new Keys(x, y));
                            break;
                        case empty:
                            drawSymbol(new Path(x, y));
                            break;
                        case idCollectible:
                            drawSymbol(new Collectible(x, y));
                            break;
                        default:
                            drawSymbol(new Path(x, y));
                            break;
                    }
                }
            }
        }
        if (Game.player == null) {
            Game.player = new Player(posX, posY);
        }
        getScreen().setCursorPosition(Game.player.getPosition().getX(), Game.player.getPosition().getY());
        drawSymbol(Game.player);
        getScreen().refresh();
        getScreen().getTerminal().setCursorVisible(false);
    }

    /**
     * Draws a character at a specific position
     *
     * @param symbol the object that specifies the position and the character to
     * draw
     */
    public void drawSymbol(Symbol symbol) {
        char character = symbol.getSymbol();
        Terminal.Color background = symbol.getBackgroundColor();
        Terminal.Color foreground = symbol.getForegroundColor();
        drawColoredString(character + "", foreground, background, null, symbol.getPosition().getX(),
                symbol.getPosition().getY());
    }

    /**
     * Updates the game cycle and calls all relevant methods
     */
    public void update() {
        drawBorder();
        boolean game = true;
        long time = System.currentTimeMillis();
        setPos(0, 0);
        while (game) {
            getScreen().getTerminal().setCursorVisible(false);
            long newTime = System.currentTimeMillis();
            if (newTime - time > 1000) {
                dynamicEnemy();
                time = System.currentTimeMillis();
            }
            game = move();
            game = check();
            drawBorder();
        }
        System.exit(0);
    }

    /**
     * Sets the position of the player after checking for possible obstacles
     *
     * @param x the amount of units to move in x direction
     * @param y the amount of units to move in y direction
     */
    private void setPos(int x, int y) {
        int playerX = Game.player.getPosition().getX();
        int playerY = Game.player.getPosition().getY();
        Terminal terminal = getScreen().getTerminal();
        boolean redraw = false;
        if (playerX == 0 && x == -1) {
            char obstacle = Game.io.getLvl()[region.getX() * realWidth - 1][playerY + region.getY() * realHeight];
            if (obstacle != idWall) {
                terminal.moveCursor(playerX, playerY);
                terminal.putCharacter(' ');
                region.setX(region.getX() - 1);
                Game.player.getPosition().setX(realWidth - 1);
                x = 0;
                playerX = realWidth - 1;
                redraw = true;
            } else {
                return;
            }
        } else if (playerX == realWidth - 1 && x == 1) {
            char obstacle = Game.io.getLvl()[realWidth + region.getX() * realWidth][playerY
                    + region.getY() * realHeight];
            if (obstacle != idWall) {
                terminal.moveCursor(playerX, playerY);
                terminal.putCharacter(' ');
                region.setX(region.getX() + 1);
                Game.player.getPosition().setX(0);
                x = 0;
                playerX = 0;
                redraw = true;
            } else {
                return;
            }
        } else if (playerY == 0 && y == -1) {
            char obstacle = Game.io.getLvl()[playerX + region.getX() * realWidth][region.getY() * realHeight - 1];
            if (obstacle != idWall) {
                terminal.moveCursor(playerX, playerY);
                terminal.putCharacter(' ');
                region.setY(region.getY() - 1);
                Game.player.getPosition().setY(realHeight - 1);
                y = 0;
                playerY = realHeight - 1;
                redraw = true;
            } else {
                return;
            }
        } else if (playerY == realHeight - 1 && y == 1) {
            char obstacle = Game.io.getLvl()[playerX + region.getX() * realWidth][realHeight
                    + region.getY() * realHeight];
            if (obstacle != idWall) {
                terminal.moveCursor(playerX, playerY);
                terminal.putCharacter(' ');
                region.setY(region.getY() + 1);
                Game.player.getPosition().setY(0);
                y = 0;
                playerY = 0;
                redraw = true;
            } else {
                return;
            }
        }

        int levelX = playerX + realWidth * region.getX();
        int levelY = playerY + realHeight * region.getY();
        if (levelX == 0 && x == -1 || levelY == 0 && y == -1 || levelX == Game.io.getWidth() && x == 1
                || levelY == Game.io.getHeight() && y == 1) {
            return;
        }

        if ((Game.io.getLvl()[levelX + x][levelY + y] != idWall && Game.io.getLvl()[levelX + x][levelY + y] != idIn)) {
            if (Game.io.getLvl()[levelX + x][levelY + y] == idOut && !Game.player.hasKey()) {
                return;
            }
            terminal.setCursorVisible(false);
            terminal.moveCursor(playerX, playerY);
            if (Game.io.getLvl()[levelX][levelY] == idIn) {
                terminal.applyForegroundColor(Color.GREEN);
                terminal.putCharacter(Entry.symbol);
                terminal.applyForegroundColor(Color.WHITE);
            } else {
                terminal.putCharacter(' ');
            }

            Game.player.getPosition().setX(playerX + x);
            Game.player.getPosition().setY(playerY + y);
            terminal.applyForegroundColor(Terminal.Color.WHITE);
            terminal.moveCursor(playerX + x, playerY + y);
            if (x == -1 && y == 0) {
                terminal.putCharacter('\u25C4');
            } else if (x == 1 && y == 0) {
                terminal.putCharacter('\u25BA');
            } else if (x == 0 && y == -1) {
                terminal.putCharacter('\u25B2');
            } else if (x == 0 && y == 1) {
                terminal.putCharacter('\u25BC');
            } else {
                terminal.putCharacter(Game.player.getSymbol());
            }
        }
        if (redraw) {
            getScreen().clear();
            drawLevel();
        }
    }

    /**
     * Checks for possible events in the game - Player walks into trap - Player
     * collects key - Player finds exit - Player finds collectible
     *
     * @return true if game goes on, false if current game state prevents game
     * from going on
     */
    public boolean check() {
        int playerX = Game.player.getPosition().getX();
        int playerY = Game.player.getPosition().getY();
        char pos = Game.io.getLvl()[playerX + region.getX() * realWidth][playerY + region.getY() * realHeight];
        switch (pos) {
            case idStaticTrap:
                Game.io.getLvl()[playerX + region.getX() * realWidth][playerY + region.getY() * realHeight] = empty;
                Game.player.died();
                if (Game.player.getLives() <= 0) {
                    return false;
                }
                break;
            case idDynamicTrap:
                for (int i = 0; i < dynamicEnemies.length; i++) {
                    DynamicEnemy e = dynamicEnemies[i];
                    if (e != null) {
                        if (e.getPosition().getX() == playerX && e.getPosition().getY() == playerY) {
                            dynamicEnemies[i] = null;
                        }
                    }
                }
                Game.io.getLvl()[playerX + region.getX() * realWidth][playerY + region.getY() * realHeight] = empty;
                Game.player.died();
                if (Game.player.getLives() <= 0) {
                    getScreen().clear();
                    drawColoredString("You lost!", Color.GREEN, Color.BLACK, ScreenCharacterStyle.Bold, realWidth / 2 - 4,
                            realHeight / 2);
                    getScreen().refresh();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {

                    }
                    return false;
                }
                break;
            case idKey:
                Game.io.getLvl()[playerX + region.getX() * realWidth][playerY + region.getY() * realHeight] = empty;
                Game.player.setHasKey(true);
                break;
            case idOut:
                Game.io.getLvl()[playerX + region.getX() * realWidth][playerY + region.getY() * realHeight] = empty;
                getScreen().clear();
                drawColoredString("You won with a score of " + Game.player.getScore() + "!", Color.GREEN, Color.BLACK, ScreenCharacterStyle.Bold, realWidth / 2 - 4,
                        realHeight / 2);
                getScreen().refresh();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {

                }
                return false;
            case idCollectible:
                Game.io.getLvl()[playerX + region.getX() * realWidth][playerY + region.getY() * realHeight] = empty;
                Game.player.addScore();
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * Determines the direction to move and starts the Pause Menu if necessary
     *
     * @return true if a movement is encountered, false if the Pause Menu is
     * started.
     */
    public boolean move() {
        Kind kind = listener.getKey(false);
        if (kind != null) {
            switch (kind) {
                case ArrowDown:
                    setPos(0, 1);
                    break;
                case ArrowUp:
                    setPos(0, -1);
                    break;
                case ArrowLeft:
                    setPos(-1, 0);
                    break;
                case ArrowRight:
                    setPos(1, 0);
                    break;
                case Escape:
                    getScreen().putString(Game.player.getPosition().getX(), Game.player.getPosition().getY(), " ", Color.BLACK, Color.BLACK, ScreenCharacterStyle.Bold);
                    PauseMenu pause = new PauseMenu(getResolutionX(), getResolutionY(), getScreen(), this);
                    pause.interact(null);
                    return false;
                default:
                    setPos(0, 0);
                    break;
            }
        }
        return true;
    }
}
