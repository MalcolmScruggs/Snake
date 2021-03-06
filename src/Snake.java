import java.util.ArrayList;

public class Snake {
  private int x;
  private int y;
  private boolean isP2;
  private String direction;
  ArrayList<BodyCell> tail;
  ArrayList<BodyCell> toAdd;

  Snake(int x, int y, boolean isP2) {
    this.setX(x);
    this.setY(y);
    this.setP2(isP2);
    this.direction = "right";
    this.tail = new ArrayList<BodyCell>();
    this.toAdd = new ArrayList<BodyCell>();
  }

  /**
   * Updates the direction of this snake according to a string input
   * Only responds to the following strings:
   * - "up"
   * - "down"
   * - "left"
   * - "right"
   * 
   * @param ke - the string that determine the change of direction
   */
  void moveSnake(String ke) { 
    if (!isP2) {
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
    else if (isP2) {
      if (ke.equals("w")) {
        this.direction = "up";
      }
      else if (ke.equals("d")) {
        this.direction = "right";
      }
      else if (ke.equals("s")) {
        this.direction = "down";
      }
      else if (ke.equals("a")) {
        this.direction = "left";
      }
    }
  }


  /**
   * Moves the snake according to one tick of time having passed.
   * Movement is determine by the local value this.direction.
   * 
   * @throws throws RuntimeException if direction is not one the expected values
   */
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

    if (tail.size() > 0) {
      tail.remove(tail.size() - 1);
      tail.add(0, new BodyCell(oldx, oldy));
    }

  }

  /**
   * Updates the snake based of one tick of time having passed.
   * This consists of:
   *  - adding any eaten foods onto the tail of the snake if at tail
   *  - moving the snake
   */
  public void handleOnTick() {

    // handles adding on cells that have been eaten
    for (int i = 0; i < toAdd.size(); i++) {
      BodyCell bcAdd = toAdd.get(i);
      boolean validAddition = true;
      if (bcAdd.getX() == this.x && bcAdd.getY() == this.y) {
        validAddition = false;
      }
      for (BodyCell bcTail : tail) {
        if (bcAdd.getX() == bcTail.getX() && bcAdd.getX() == bcTail.getY()) {
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

  /**
   * Takes the body cell and stores it until it is appropriate to add it to
   * the snake
   * @param bc - the BodyCell to be added onto the snake
   */
  public void addBodyCell(BodyCell bc) {
    this.toAdd.add(bc);
  }

  public void setX(int x) {
    this.x = x;
  }

  public void setY(int y) {
    this.y = y;
  }

  public int getX() {
    return this.x;
  }

  public int getY() {
    return this.y;
  }

  public boolean isP2() {
    return isP2;
  }

  public void setP2(boolean isP2) {
    this.isP2 = isP2;
  }
}
