package byow.Core;

public class Room {
    private int width;
    private int height;
    private Position posLL;
    private Position posLR;
    private Position posUL;

    public Room(int w, int h, Position LL) {
        width = w;
        height = h;
        posLL = LL;
        posLR = new Position(LL.getX() + w, LL.getY());
        posUL = new Position(LL.getX(), LL.getY() + h);
    }

    public int getWidth() { return width;}

    public int getHeight() { return height;}

    public Position getPosLL() { return posLL;}

    public Position getPosLR() { return posLR;}

    public Position getPosUL() { return posUL;}

}
