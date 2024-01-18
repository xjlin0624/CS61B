package byow.Core;

import java.util.Random;

public class Position {
    private int x;
    private int y;

    public Position(Random rand) {
        x = RandomUtils.uniform(rand, 79);
        y = RandomUtils.uniform(rand, 29);
    }

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position(Random rand, int x, int y) {
        this.x = RandomUtils.uniform(rand, x);
        this.y = RandomUtils.uniform(rand, y);
    }
    public Position(int x, int y, boolean t){
        x = (int) (Math.random()*x);
        y = (int) (Math.random()*y);
    }

    public int getX() { return x;}

    public int getY() {
        return y;
    }
}
