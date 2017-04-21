import java.util.ArrayList;

public class Snake {
  int x;
  int y;
  String direction;
  ArrayList<BodyCell> tail;
  ArrayList<BodyCell> toAdd;
  
  Snake(int x, int y) {
    this.x = x;
    this.y = y;
    this.direction = "right";
    this.tail = new ArrayList<BodyCell>();
    this.toAdd = new ArrayList<BodyCell>();
  }

  // responds the key input from the player
  void moveSnake(String ke) {
    // checks that player doesn't double back    
    if (ke.equals("up")) {
      this.direction = "up";
    }
    else if (ke.equals("right")) {
      this.direction = "right";
    }
    else if (ke.equals("down")) {
      this.direction = "down";
    }
    else if (ke.equals("left")) {
      this.direction = "left";
    }
  }
  
  // moves snake based on ticks passing
  void moveSnake() {
    int oldx = x;
    int oldy = y;
    if (this.direction.equals("up")) {
      y--;
    }
    else if (this.direction.equals("right")) {
      x++;
    }
    else if (this.direction.equals("down")) {
      y++;
    }
    else if (this.direction.equals("left")) {
      x--;
    }
    else {
      throw new RuntimeException("direction is illegal");
    }
    
    //TODO update tail
    if (tail.size() > 0) {
      tail.remove(tail.size() - 1);
      tail.add(0, new BodyCell(oldx, oldy));
    }
    
  }

  // adjusts all snake behavior based on a tick passing
  public void handleOnTick() {
    
    // handles adding on cells that have been eaten
    for (int i = 0; i < toAdd.size(); i++) {
      BodyCell bcAdd = toAdd.get(i);
      boolean validAddition = true;
      if (bcAdd.x == this.x && bcAdd.y == this.y) {
        validAddition = false;
      }
      for (BodyCell bcTail : tail) {
        if (bcAdd.x == bcTail.x && bcAdd.x == bcTail.y) {
          validAddition = false;
        }
      }
      if (validAddition) {
        this.tail.add(bcAdd);
        toAdd.remove(i);
      }
    }
    
    // moves the snake
    this.moveSnake();

  }
  
  public void addBodyCell(BodyCell bc) {
    this.toAdd.add(bc);
  }
  
  public void setX(int x) {
    this.x = x;
  }
  
  public void setY(int y) {
    this.y = y;
  }
}
