import java.awt.Color;
import java.util.ArrayList;

import javalib.impworld.World;
import javalib.impworld.WorldScene;
import javalib.worldimages.OutlineMode;
import javalib.worldimages.RectangleImage;
import javalib.worldimages.TextImage;
import javalib.worldimages.WorldImage;

public class SnakeWorld extends World{
  public static final int WIDTH = 35;
  public static final int HEIGHT = 35;
  public static final int CELL_SIZE = 20;
  public static final double SPEED = 0.1; // lower is faster
  private static final int FONT_SIZE = 20;
  private static final boolean WALLS_KILL = false;
  private static final WorldImage background = new RectangleImage(WIDTH * CELL_SIZE,
      HEIGHT * CELL_SIZE, OutlineMode.SOLID, Color.BLACK);

  Snake snek;
  Snake snek2;
  ArrayList<Food> loFood;
  boolean gameActive;
  boolean startScreenActive = true;
  boolean isTwoPlayer = true;
  int score;
  int hiScore;

  SnakeWorld() {
    this.resetGame();
  }


  /**
   * Generates the image that will be displayed to the user
   */
  @Override
  public WorldScene makeScene() {
    if (startScreenActive) {
      return makeStartScene();
    }
    else if (!gameActive) {
      return makeDeathScene();
    }
    else {
      return makeGameScene();
    }
  }

  /**
   * Generates the game screen for when the game screen when the game is active
   * @return the screen to be shown to the user
   */
  private WorldScene makeGameScene() {
    WorldScene scene = new WorldScene(WIDTH * CELL_SIZE, WIDTH * CELL_SIZE);
    scene.placeImageXY(background, WIDTH / 2 * CELL_SIZE, HEIGHT / 2 * CELL_SIZE);

    if (gameActive) { // render the snake
      for (Food f : loFood) {
        WorldImage img = new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.SOLID, Color.BLUE);
        scene.placeImageXY(img, f.getX() * CELL_SIZE, f.getY() * CELL_SIZE);
      }

      WorldImage snekHead = new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.SOLID, Color.WHITE);
      scene.placeImageXY(snekHead, snek.getX() * CELL_SIZE, snek.getY() * CELL_SIZE);

      for (BodyCell bc : snek.tail) {
        WorldImage img = new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.SOLID, Color.WHITE);
        scene.placeImageXY(img, bc.getX() * CELL_SIZE, bc.getY() * CELL_SIZE);
      }

      if (isTwoPlayer) {
        WorldImage snekHead2 = new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.SOLID, Color.MAGENTA);
        scene.placeImageXY(snekHead2, snek2.getX() * CELL_SIZE, snek2.getY() * CELL_SIZE);

        for (BodyCell bc : snek2.tail) {
          WorldImage img = new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.SOLID, Color.MAGENTA);
          scene.placeImageXY(img, bc.getX() * CELL_SIZE, bc.getY() * CELL_SIZE);
        }
      }
    }

    // Display the score
    WorldImage scoreImg = new TextImage("Score: " + score, FONT_SIZE, Color.RED);
    scene.placeImageXY(scoreImg, WIDTH / 2 * CELL_SIZE, HEIGHT / 5 * CELL_SIZE);

    return scene;
  }

  /**
   * generate the scene to be displayed after the snake has collided and died
   * @return - the screen to be show the user
   */
  private WorldScene makeDeathScene() {
    WorldScene scene = new WorldScene(WIDTH * CELL_SIZE, HEIGHT * CELL_SIZE);
    scene.placeImageXY(background, WIDTH / 2 * CELL_SIZE, HEIGHT / 2 * CELL_SIZE);

    WorldImage img = new TextImage("press any key to play again", FONT_SIZE, Color.WHITE);
    scene.placeImageXY(img, WIDTH / 2 * CELL_SIZE, HEIGHT / 2 * CELL_SIZE);

    WorldImage hiScoreImg = new TextImage("High Score: " + hiScore, FONT_SIZE, Color.WHITE);
    scene.placeImageXY(hiScoreImg, WIDTH / 2 * CELL_SIZE, HEIGHT / 5 * CELL_SIZE + CELL_SIZE * 2);

    // Display the score
    WorldImage scoreImg = new TextImage("Score: " + score, FONT_SIZE, Color.RED);
    scene.placeImageXY(scoreImg, WIDTH / 2 * CELL_SIZE, HEIGHT / 5 * CELL_SIZE);

    return scene;
  }

  /**
   * Generates the starts screen
   * @return the screen to be shown to the user
   */
  private WorldScene makeStartScene() {
    WorldScene scene = new WorldScene(WIDTH * CELL_SIZE, HEIGHT * CELL_SIZE);
    scene.placeImageXY(background, WIDTH / 2 * CELL_SIZE, HEIGHT / 2 * CELL_SIZE);

    //place Snake title
    WorldImage snakeImg = new TextImage("SNAKE", FONT_SIZE * 2, Color.WHITE);
    scene.placeImageXY(snakeImg, WIDTH / 2 * CELL_SIZE, HEIGHT / 5 * CELL_SIZE);

    WorldImage controlsText = new TextImage("Press any key to start", FONT_SIZE, Color.WHITE);
    scene.placeImageXY(controlsText, WIDTH / 2 * CELL_SIZE, HEIGHT / 2 * CELL_SIZE);

    return scene;
  }

  /**
   * Handles the actions that should occur to the game based on a tick passing
   */
  @Override
  public void onTick() {
    snek.handleOnTick();
    if (isTwoPlayer) {
      snek2.handleOnTick();
    }
    this.snakeCollision();
    this.foodCollision();
    this.handleBoundries();
    this.score = snek.tail.size();
    if (isTwoPlayer) {
      this.score += snek2.tail.size();
    }
  }

  /**
   * Responds to user input from the keyboard
   */
  @Override
  public void onKeyEvent(String ke) {
    if (startScreenActive) {
      this.startScreenActive = false;
    }
    else if (!gameActive) {
      resetGame();
    }
    else if (ke.equals("r")) {
      resetGame();
    }
    else {
      snek.moveSnake(ke);
      if (isTwoPlayer) {
        snek2.moveSnake(ke);
      }
    }
  }

  /**
   * Resets the base values of the game to start a new round of snake
   */
  public void resetGame() {
    this.snek = new Snake(WIDTH / 2, HEIGHT / 2, false);
    this.gameActive = true;
    this.score = 0;
    this.loFood = new ArrayList<Food>();
    this.loFood.add(new Food(snek));

    if (isTwoPlayer) {
      this.snek2 = new Snake(WIDTH / 2 + 1, HEIGHT / 2 + 1, true);
    }
  }

  /**
   * Checks if the snake has hit itself.
   * Sets gameAvtice to false if it has. Does nothing otherwise
   */
  public void snakeCollision() {
    if (isTwoPlayer) {
      for (BodyCell bc : snek2.tail) {
        for (BodyCell bc2 : snek.tail) {
          if ((bc.getX() == snek2.getX() && bc.getY() == snek2.getY())
              || (bc.getX() == snek.getX() && bc.getY() == snek.getY())
              || (bc2.getX() == snek2.getX() && bc2.getY() == snek2.getY())
              || (bc2.getX() == snek.getX() && bc2.getY() == snek.getY())
              || (snek.getX() == snek2.getX() && snek.getY() == snek2.getY())) {
            this.gameActive = false;
            this.hiScore = Math.max(score, hiScore);
            break;
          }
        }
      }
    }
    else {
      for (BodyCell bc : snek.tail) {
        if (bc.getX() == snek.getX() && bc.getY() == snek.getY()) {
          this.gameActive = false;
          this.hiScore = Math.max(score, hiScore);
          break;
        }
      }
    }
  }

  /**
   * Checks if the snake has hit food.
   * If it has creates another food and adds a BodyCell to the snake
   */
  public void foodCollision() {
    for (int i = 0; i < loFood.size(); i++) {
      Food f = loFood.get(i);
      if (f.getX() == snek.getX() && f.getY() == snek.getY()) {
        loFood.remove(i);
        loFood.add(new Food(this.snek));
        snek.addBodyCell(new BodyCell(f.getX(), f.getY()));
      }
    }

    if (isTwoPlayer) {
      for (int i = 0; i < loFood.size(); i++) {
        Food f = loFood.get(i);
        if (f.getX() == snek2.getX() && f.getY() == snek2.getY()) {
          loFood.remove(i);
          loFood.add(new Food(this.snek2));
          snek2.addBodyCell(new BodyCell(f.getX(), f.getY()));
        }
      }
    }
  }

  /**
   * Handle the behavior of the snake depending on the boundary settings.
   * If WALLS_KILL then hitting a wall ends the game
   * Otherwise the snake moves from one edge of the screen to the opposite.
   */
  public void handleBoundries() {
    if (WALLS_KILL) {
      if (snek.getX() > WIDTH - 1 || snek.getX() < 0
          || snek.getY() > HEIGHT - 1 || snek.getY() < 0) {
        this.gameActive = false;
        this.hiScore = Math.max(score, hiScore);
      }
    }
    else {
      if (snek.getX() > WIDTH - 1) {
        snek.setX(0);
      }
      else if (snek.getX() < 0) {
        snek.setX(WIDTH - 1);
      }
      else if (snek.getY() > HEIGHT -1) {
        snek.setY(0);
      }
      else if (snek.getY() < 0) {
        snek.setY(HEIGHT - 1);
      }
    }

    if (isTwoPlayer) {
      if (WALLS_KILL) {
        if (snek2.getX() > WIDTH - 1 || snek2.getX() < 0
            || snek2.getY() > HEIGHT - 1 || snek2.getY() < 0) {
          this.gameActive = false;
          this.hiScore = Math.max(score, hiScore);
        }
      }
      else {
        if (snek2.getX() > WIDTH - 1) {
          snek2.setX(0);
        }
        else if (snek2.getX() < 0) {
          snek2.setX(WIDTH - 1);
        }
        else if (snek2.getY() > HEIGHT -1) {
          snek2.setY(0);
        }
        else if (snek2.getY() < 0) {
          snek2.setY(HEIGHT - 1);
        }
      }
    }
  }

}