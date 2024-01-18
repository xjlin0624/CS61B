package byow.Core;

import java.util.Random;

public class Hallway {
    int width;
    int length;
    boolean isHZ;
    Position posStart;

    public Hallway(Random rand){
        width = RandomUtils.uniform(rand, 3);
        length = RandomUtils.uniform(rand, 50);
    }

    public void setOrientation(boolean isHZ){
        this.isHZ = isHZ;
    }

    public void setStart(Position pos){
        posStart = pos;
    }

}
