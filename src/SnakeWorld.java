import java.awt.Color;
import java.util.ArrayList;

import javalib.impworld.World;
import javalib.impworld.WorldScene;
import javalib.worldimages.OutlineMode;
import javalib.worldimages.RectangleImage;
import javalib.worldimages.TextImage;
import javalib.worldimages.WorldImage;

public class SnakeWorld extends World{
  public static final int WIDTH = 25;
  public static final int HEIGHT = 25;
  public static final int CELL_SIZE = 15;
  public static final double SPEED = 0.1;
  private static final int FONT_SIZE = 20;
  private static final boolean WALLS_KILL = true;

  Snake snek;
  ArrayList<Food> loFood;
  boolean gameActive;
  int score;

  SnakeWorld() {
    this.resetGame();
  }

  @Override
  public WorldScene makeScene() {
    WorldScene scene = new WorldScene(WIDTH * CELL_SIZE, HEIGHT * CELL_SIZE);
    if (gameActive) { // render the snake
      for (Food f : loFood) {
        WorldImage img = new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.SOLID, Color.BLUE);
        scene.placeImageXY(img, f.x * CELL_SIZE, f.y * CELL_SIZE);
      }

      WorldImage snekHead = new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.SOLID, Color.BLACK);
      scene.placeImageXY(snekHead, snek.x * CELL_SIZE, snek.y * CELL_SIZE);

      for (BodyCell bc : snek.tail) {
        WorldImage img = new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.SOLID, Color.BLACK);
        scene.placeImageXY(img, bc.x * CELL_SIZE, bc.y * CELL_SIZE);
      }
    }
    else { // render the end game screen
      WorldImage img = new TextImage("press R to reset", FONT_SIZE, Color.RED);
      scene.placeImageXY(img, WIDTH / 2 * CELL_SIZE, HEIGHT / 2 * CELL_SIZE);
    }
    
    // Display the score
    WorldImage img = new TextImage("Score: " + score, FONT_SIZE, Color.RED);
    scene.placeImageXY(img, WIDTH / 2 * CELL_SIZE, HEIGHT + HEIGHT / 5 * CELL_SIZE);

    return scene;
  }

  @Override
  public void onTick() {
    snek.handleOnTick();
    this.snakeCollision();
    this.foodCollision();
    this.handleBoundries();
    this.score = snek.tail.size();
  }

  @Override
  public void onKeyEvent(String ke) {
    if (ke.equals("r")) {
      resetGame();
    }
    else {
      snek.moveSnake(ke);
    }
  }

  public void resetGame() {
    this.snek = new Snake(WIDTH / 2, HEIGHT / 2);
    this.gameActive = true;
    this.score = 0;
    this.loFood = new ArrayList<Food>();
    this.loFood.add(new Food(snek));
  }

  public void snakeCollision() {
    for (BodyCell bc : snek.tail) {
      if (bc.x == snek.x && bc.y == snek.y) {
        this.gameActive = false;
        break;
      }
    }
  }

  public void foodCollision() {
    for (int i = 0; i < loFood.size(); i++) {
      Food f = loFood.get(i);
      if (f.x == snek.x && f.y == snek.y) {
        loFood.remove(i);
        loFood.add(new Food(this.snek));
        snek.addBodyCell(new BodyCell(f.x, f.y));
      }
    }
  }

  public void handleBoundries() {
    if (WALLS_KILL) {
      if (snek.x > WIDTH - 1 || snek.x < 0
          || snek.y > HEIGHT - 1 || snek.y < 0) {
        this.gameActive = false;
      }
    }
    else {
      if (snek.x > WIDTH - 1) {
        snek.setX(0);
      }
      else if (snek.x < 0) {
        snek.setX(WIDTH - 1);
      }
      else if (snek.y > HEIGHT -1) {
        snek.setY(0);
      }
      else if (snek.y < 0) {
        snek.setY(HEIGHT - 1);
      }
    }
  }
  
}