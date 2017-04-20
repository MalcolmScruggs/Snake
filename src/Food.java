import java.util.ArrayList;
import java.util.Random;

import javalib.worldimages.WorldImage;

public class Food {
  int x;
  int y;
  
  //convenience constructor 
  Food(int x, int y) {
    this.x = x;
    this.y = y;
  }
  
  // suppress default constructor to to prevent collision issues
  @SuppressWarnings("unused")
  private Food() {
  }
  
  Food(Snake s) {
    Random rand = new Random();
    ArrayList<BodyCell> invalidPlacements = s.tail;
    invalidPlacements.add(new BodyCell(s.x, s.y));
    
    
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
  
  WorldImage makeImage() {
    //TODO
    return null; 
  }
}
