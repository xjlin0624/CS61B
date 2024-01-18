package byow.Core;
import byow.TileEngine.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class MakeWorldMap {
    private Random rand;
    private int width;
    private int height;
    private TETile[][] map;
    private ArrayList<Room> rooms;
    private boolean lightsOn;
    private ArrayList<Position> lights;
    private ArrayList<Position> water;
    private boolean avatar;
    private int avatarX;
    private int avatarY;

    public MakeWorldMap(long seed, boolean a) {
        width = 80;
        height = 30;
        this.rand = new Random(seed);
        map = new TETile[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                map[i][j] = Tileset.NOTHING;
            }
        }
        rooms = new ArrayList<>();
        avatar = a;
        lights = new ArrayList<>();
        water = new ArrayList<>();
        createRoom();
        connect1();
        if (avatar) {
            createAvatar();
        }
        createDoor();
        createChallenge();
    }

    public void createAvatar() {
        int w = RandomUtils.uniform(rand, 77) + 1;
        int h = RandomUtils.uniform(rand, 27) + 1;
        while (map[w][h] != Tileset.FLOOR) {
            w = RandomUtils.uniform(rand, 77) + 1;
            h = RandomUtils.uniform(rand, 27) + 1;
        }

        map[w][h] = Tileset.AVATAR;
        avatarX = w;
        avatarY = h;
    }

    public void walk(Character c) {
        if (c == 'W') {
            if (map[avatarX][avatarY + 1] == Tileset.FLOOR) {
                map[avatarX][avatarY + 1] = Tileset.AVATAR;
                map[avatarX][avatarY] = Tileset.FLOOR;
                avatarY++;
            } else if (map[avatarX][avatarY + 1] == Tileset.WATER) {
                Challenge chl = new Challenge(rand);
                TERenderer ter = new TERenderer();
                ter.renderFrame(chl.getMap());
                chl.game();
                map[avatarX][avatarY + 1] = Tileset.FLOOR; // turn water tile back to floor
            }
        } else if (c == 'A') {
            if(map[avatarX - 1][avatarY] == Tileset.FLOOR) {
                map[avatarX - 1][avatarY] = Tileset.AVATAR;
                map[avatarX][avatarY] = Tileset.FLOOR;
                avatarX--;
            } else if (map[avatarX - 1][avatarY] == Tileset.WATER) {
                Challenge chl = new Challenge(rand);
                TERenderer ter = new TERenderer();
                ter.renderFrame(chl.getMap());
                chl.game();
                map[avatarX - 1][avatarY] = Tileset.FLOOR; // turn water tile back to floor
            }
        } else if (c == 'S') {
            if(map[avatarX][avatarY - 1] == Tileset.FLOOR) {
                map[avatarX][avatarY - 1] = Tileset.AVATAR;
                map[avatarX][avatarY] = Tileset.FLOOR;
                avatarY--;
            } else if (map[avatarX][avatarY - 1] == Tileset.WATER) {
                // enter the alt world
                Challenge chl = new Challenge(rand);
                TERenderer ter = new TERenderer();
                ter.renderFrame(chl.getMap());
                chl.game();
                map[avatarX][avatarY - 1] = Tileset.FLOOR; // turn water tile back to floor
            }
        } else if (c == 'D') {
            if(map[avatarX + 1][avatarY] == Tileset.FLOOR) {
                map[avatarX + 1][avatarY] = Tileset.AVATAR;
                map[avatarX][avatarY] = Tileset.FLOOR;
                avatarX++;
            } else if (map[avatarX + 1][avatarY] == Tileset.WATER) {
                // enter the alt world
                Challenge chl = new Challenge(rand);
                TERenderer ter = new TERenderer();
                ter.renderFrame(chl.getMap());
                chl.game();
                map[avatarX + 1][avatarY] = Tileset.FLOOR; // turn water tile back to floor
            }
        } else if (c == 'O') {
            lightsOn = true;
            switchOF();
        } else if (c == 'F') {
            lightsOn = false;
            switchOF();
        }
    }

    public void createDoor() {
        int w = RandomUtils.uniform(rand, 77) + 1;
        int h = RandomUtils.uniform(rand, 27) + 1;
        while (map[w][h] != Tileset.WALL) {
            w = RandomUtils.uniform(rand, 77) + 1;
            h = RandomUtils.uniform(rand, 27) + 1;
        }

        map[w][h] = Tileset.LOCKED_DOOR;
    }


    public Position createLight(Room rm) {
        Position LL = rm.getPosLL();
        int w = rm.getWidth();
        int h = rm.getHeight();
        int x = LL.getX() + RandomUtils.uniform(rand, w - 2) + 2;
        int y = LL.getY() + RandomUtils.uniform(rand, h - 2) + 2;
        return new Position(x, y);
    }

    public Position createWater(Room rm) {
        Position LL = rm.getPosLL();
        int w = rm.getWidth();
        int h = rm.getHeight();
        int x = LL.getX() + RandomUtils.uniform(rand, w - 2) + 2;
        int y = LL.getY() + RandomUtils.uniform(rand, h - 2) + 2;
        return new Position(x, y);
    }

    public void switchOF() {
        for (Position pos : lights) {
            int x = pos.getX();
            int y = pos.getY();
            if (lightsOn) {
                for (int i = x - 1; i < x + 2; i++) {
                    for (int j = y - 1; j < y + 2; j++) {
                        if (map[i][j] == Tileset.WALL || map[i][j] == Tileset.TREE || map[i][j] == Tileset.LOCKED_DOOR) {
                            break;
                        } else if (i == x && j == y) {
                            map[x][y] = new TETile('Ω', new Color(128, 192, 128), Color.blue, "light source");
                        } else {
                            TETile t = new TETile('•', new Color(128, 192, 128), Color.blue, "light");
                            map[i][j] = t;
                            TETile.colorVariant(t, 60, 60, 60, rand);
                        }
                    }
                }

            } else {
                for (int i = x - 1; i < x + 2; i++) {
                    for (int j = y - 1; j < y + 2; j++) {
                        if (map[i][j] == Tileset.WALL || map[i][j] == Tileset.TREE || map[i][j] == Tileset.LOCKED_DOOR) {
                            break;
                        } else {
                            map[i][j] = Tileset.FLOOR;
                        }
                    }
                }
            }
        }
    }

    public void createChallenge() {
        int i = RandomUtils.uniform(rand, rooms.size());
        Room room = rooms.get(i);
        Position position = room.getPosLL();
        int h = room.getHeight();
        int w = room.getWidth();
        int x = position.getX() + w / 2;
        int y = position.getY() + h / 2;

        map[x][y] = Tileset.WATER;
    }

    public void createRoom() {
        for (int i = 0; i < 30; i++) {
            Position LL = new Position(rand);
            int w = RandomUtils.uniform(rand, 7) + 3;
            int h = RandomUtils.uniform(rand, 7) + 3;
            Room r = new Room(w, h, LL);
            if (checkEmpty(r)) {
                rooms.add(r);
                drawRoom(r);
                lights.add(createLight(r));
            } else {
                i++;
            }
        }
    }

    public boolean checkEmpty(Room r) {
        Position ll = r.getPosLL();
        Position lr = r.getPosLR();
        Position ul = r.getPosUL();
        for (int i = ll.getX(); i <= lr.getX(); i++) {
            for (int j = ll.getY(); j <= ul.getY(); j++) {
                if (i > 79 || j > 29 || map[i][j] != Tileset.NOTHING) {
                    return false;
                }
            }
        }
        return true;
    }

    public void drawRoom(Room r) {
        Position ll = r.getPosLL();  //  . . .
        Position lr = r.getPosLR();  //  . d .
        Position ul = r.getPosUL();
        for (int i = ll.getX(); i <= lr.getX(); i++) {
            for (int j = ll.getY(); j <= ul.getY(); j++) {
                if (i == ll.getX() || i == lr.getX() || j == ll.getY() || j == ul.getY()) {
                    map[i][j] = Tileset.WALL;
                } else {
                    map[i][j] = Tileset.FLOOR;
                }
            }
        }
    }

    public void connect1() {
        for (int i = 0; i < rooms.size(); i++) {
            int left = RandomUtils.uniform(rand, rooms.get(i).getHeight() - 2) + 1;
            int right = RandomUtils.uniform(rand, rooms.get(i).getHeight() - 2) + 1;
            int up = RandomUtils.uniform(rand, rooms.get(i).getWidth() - 2) + 1;
            int down = RandomUtils.uniform(rand, rooms.get(i).getWidth() - 2) + 1;
            drawHZHall(rooms.get(i), left, right);
            drawVTHall(rooms.get(i), down, up);
        }
    }

    public void drawHZHall(Room room, int left, int right) {
        int iL = room.getPosLL().getX();
        int iR = room.getPosLR().getX();
        int LL = room.getPosLL().getY();
        int jL = room.getPosLL().getY() + left;
        int jR = room.getPosLR().getY() + right;
        for (int j = LL; j < LL + room.getHeight(); j++) {
            if (map[iL][j] == Tileset.FLOOR || map[iR][j] == Tileset.FLOOR) {
                return;
            }
        }
        if (iL != 0 && map[iL - 1][jL] != Tileset.FLOOR) {
            for (int i = iL; i >= 0; i--) {
                map[i][jL + 1] = Tileset.WALL;
                map[i][jL - 1] = Tileset.WALL;
                if (map[i][jL] == Tileset.WALL || map[i][jL] == Tileset.FLOOR) {
                    map[i][jL] = Tileset.FLOOR;
                    if (i != iL) {
                        break;
                    }
                } else {
                    if (i == 0) {
                        map[i][jL] = Tileset.WALL;
                    } else {
                        map[i][jL] = Tileset.FLOOR;
                    }
                }
            }
        }
        if (iR != 79 && map[iR + 1][jR] != Tileset.FLOOR) {
            for (int i = iR; i < 80; i++) {
                map[i][jR + 1] = Tileset.WALL;
                map[i][jR - 1] = Tileset.WALL;
                if (map[i][jR] == Tileset.WALL || map[i][jR] == Tileset.FLOOR) {
                    map[i][jR] = Tileset.FLOOR;
                    if (i != iR) {
                        break;
                    }
                } else {
                    if (i == 79) {
                        map[i][jR] = Tileset.WALL;
                    } else {
                        map[i][jR] = Tileset.FLOOR;
                    }
                }
            }
        }
    }

    public void drawVTHall(Room room, int down, int up) {
        int jD = room.getPosLL().getY();
        int jU = room.getPosUL().getY();
        int LL = room.getPosLL().getX();
        int iD = room.getPosLL().getX() + down;
        int iU = room.getPosUL().getX() + up;
        for (int i = LL; i < LL + room.getWidth(); i++) {
            if (map[i][jD] == Tileset.FLOOR || map[i][jU] == Tileset.FLOOR) {
                return;
            }
        }
        if (jD != 0 && map[iD][jD - 1] != Tileset.FLOOR) {
            for (int j = jD; j >= 0; j--) {
                map[iD + 1][j] = Tileset.WALL;
                map[iD - 1][j] = Tileset.WALL;
                if (map[iD][j] == Tileset.WALL || map[iD][j] == Tileset.FLOOR) {
                    map[iD][j] = Tileset.FLOOR;
                    if (j != jD) {
                        break;
                    }
                } else {
                    if (j == 0) {
                        map[iD][j] = Tileset.WALL;
                    } else {
                        map[iD][j] = Tileset.FLOOR;
                    }
                }
            }
        }
        if (jU != 29 && map[iU][jU + 1] != Tileset.FLOOR) {
            for (int j = jU; j < 30; j++) {
                map[iU + 1][j] = Tileset.WALL;
                map[iU - 1][j] = Tileset.WALL;
                if (map[iU][j] == Tileset.WALL || map[iU][j] == Tileset.FLOOR) {
                    map[iU][j] = Tileset.FLOOR;
                    if (j != jU) {
                        break;
                    }
                } else {
                    if (j == 29) {
                        map[iU][j] = Tileset.WALL;
                    } else {
                        map[iU][j] = Tileset.FLOOR;
                    }
                }
            }
        }
    }

    public TETile[][] getMap(){
        return map;
    }

}