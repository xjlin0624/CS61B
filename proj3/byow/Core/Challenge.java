package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.util.Random;

public class Challenge {

    TERenderer ter = new TERenderer();
    private Random rand;
    private int width;
    private int height;
    private TETile[][] map;
    private int tree;
    private boolean hasW;
    private int avatarX;
    private int avatarY;

    public Challenge(Random rand) {
        width = 80;
        height = 30;
        this.rand = rand;
        map = new TETile[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                map[i][j] = Tileset.NOTHING;
            }
        }
        hasW = false;
        tree = 5;
        setup();
        putObjs();
    }

//    public static void main (String[] a){
//        Random rand = new Random(1);
//        Challenge chl = new Challenge(rand);
//        TERenderer ter = new TERenderer();
//        ter.initialize(80,35);
//        ter.renderFrame(chl.getMap());
//        chl.game();
//    }

    public TETile[][] getMap(){return map;}

    public void setup() {
        for (int j = 9; j < 21; j++) {
            for (int i = 27; i < 53; i++) {
                if (j == 9 || j == 20 || i == 27 || i == 52) {
                    map[i][j] = Tileset.WALL;
                } else {
                    map[i][j] = Tileset.FLOOR;
                }
            }
        }
        avatarX = 40;
        avatarY = 15;
        map[avatarX][avatarY] = Tileset.AVATAR;
    }

    public void putObjs() {
        for (int i = 0; i < tree; i++) {
            Position p1 = new Position(rand, 7, 5);
            while (map[28 + p1.getX()][9 + p1.getY()] != Tileset.FLOOR) {
                p1 = new Position(rand, 23, 10);
            }
            map[28 + p1.getX()][9 + p1.getY()] = Tileset.TREE;

            Position p2 = new Position(rand, 7, 5);
            while (map[28 + p2.getX()][10 + p2.getY()] != Tileset.FLOOR) {
                p2 = new Position(rand,23, 10);
            }
            map[28 + p2.getX()][10 + p2.getY()] = Tileset.WATER;
        }
    }

    public void game() {
        ter.initialize(80, 35);
        while (tree != 0) {
            walk(getNextKey());
            ter.renderFrame(this.map);
        }
    }

    public char getNextKey() {
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                return Character.toUpperCase(StdDraw.nextKeyTyped());
            }
        }
    }

    public void walk(Character c) {
        if (c == 'W') {
            if (map[avatarX][avatarY + 1] == Tileset.FLOOR) {
                map[avatarX][avatarY + 1] = Tileset.AVATAR;
                map[avatarX][avatarY] = Tileset.FLOOR;
                avatarY++;
            } else if (map[avatarX][avatarY + 1] == Tileset.WATER && !hasW) {
                map[avatarX][avatarY +1] = Tileset.FLOOR;
                hasW = true;
            } else if (map[avatarX][avatarY + 1] == Tileset.TREE && hasW) {
                map[avatarX][avatarY + 1] = Tileset.FLOWER;
                hasW = false;
                tree--;
            }
        } else if (c == 'A') {
            if (map[avatarX - 1][avatarY] == Tileset.FLOOR) {
                map[avatarX - 1][avatarY] = Tileset.AVATAR;
                map[avatarX][avatarY] = Tileset.FLOOR;
                avatarX--;
            } else if (map[avatarX - 1][avatarY] == Tileset.WATER && !hasW) {
                map[avatarX - 1][avatarY] = Tileset.FLOOR;
                hasW = true;
            } else if (map[avatarX - 1][avatarY] == Tileset.TREE && hasW) {
                map[avatarX - 1][avatarY] = Tileset.FLOWER;
                hasW = false;
                tree--;
            }
        } else if (c == 'S') {
            if (map[avatarX][avatarY - 1] == Tileset.FLOOR) {
                map[avatarX][avatarY - 1] = Tileset.AVATAR;
                map[avatarX][avatarY] = Tileset.FLOOR;
                avatarY--;
            } else if (map[avatarX][avatarY - 1] == Tileset.WATER && !hasW) {
                map[avatarX][avatarY - 1] = Tileset.FLOOR;
                hasW = true;
            } else if (map[avatarX][avatarY - 1] == Tileset.TREE && hasW) {
                map[avatarX][avatarY - 1] = Tileset.FLOWER;
                hasW = false;
                tree--;
            }
        } else if (c == 'D') {
            if (map[avatarX + 1][avatarY] == Tileset.FLOOR) {
                map[avatarX + 1][avatarY] = Tileset.AVATAR;
                map[avatarX][avatarY] = Tileset.FLOOR;
                avatarX++;
            } else if (map[avatarX + 1][avatarY] == Tileset.WATER && !hasW) {
                map[avatarX + 1][avatarY] = Tileset.FLOOR;
                hasW = true;
            } else if (map[avatarX + 1][avatarY] == Tileset.TREE && hasW) {
                map[avatarX + 1][avatarY] = Tileset.FLOWER;
                hasW = false;
                tree--;
            }
        }
    }

}