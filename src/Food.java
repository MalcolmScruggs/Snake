import java.util.ArrayList;
import java.util.Random;

public class Food {
  int x;
  int y;
  
  /**
   * Convience contrstructor to create a food at a given X and Y integers
   * 
   * @param x - the integer used for the X placement 
   * @param y - the integer used for the Y placement
   */
  Food(int x, int y) {
    this.x = x;
    this.y = y;
  }
  
  // suppress default constructor to to prevent collision issues
  @SuppressWarnings("unused")
  private Food() {
  }
  
  /**
   * Creates a Food object on a cell not currently occupied by the given snake
   * 
   * @param s - the snake which the food will not be placed on top of
   */
  Food(Snake s) {
    Random rand = new Random();
    ArrayList<BodyCell> invalidPlacements = s.tail;
    invalidPlacements.add(new BodyCell(s.getX(), s.getY()));
    
    
    int foodX = rand.nextInt(SnakeWorld.WIDTH);
    int foodY = rand.nextInt(SnakeWorld.HEIGHT);
    
    //TODO add some sort of check that ensures a placement is possible
    // to prevent an infinite loop
    for (int i = 0; i < invalidPlacements.size(); i++) {
      BodyCell bc = invalidPlacements.get(i);
      if (bc.x == foodX && bc.y == foodY) {
        foodX = rand.nextInt(SnakeWorld.WIDTH);
        foodY = rand.nextInt(SnakeWorld.HEIGHT);
        i = 0;
      }
    }
    this.x = foodX;
    this.y = foodY;
  }
}
