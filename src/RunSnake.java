
public class RunSnake {
  
  public static void main(String[] args) {
    SnakeWorld s = new SnakeWorld();
    s.bigBang(SnakeWorld.WIDTH * SnakeWorld.CELL_SIZE, SnakeWorld.HEIGHT * SnakeWorld.CELL_SIZE,
        SnakeWorld.SPEED);
  }
  
}
