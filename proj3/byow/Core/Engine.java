package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    public boolean DEBUG = false;
    public boolean quit = false;
    public String p = ""; //p for persistence

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard(String name) {

        menu("");
        Character c = getNextKey();

        File f = new File("LastMap.txt");
        //PrintWriter pw = new PrintWriter(f, p);

        if (c == 'N') {
            p += c;
            String s = solicitNCharsInput();
            p += s;
            p += 'S';
            long seed = Long.parseLong(s);
            MakeWorldMap n = new MakeWorldMap(seed, true);

            //StdDraw.clear(Color.BLACK);
            ter.initialize(WIDTH, 35);
            ter.renderFrame(n.getMap());

            while(quit == false) {
                c = getNextKey();
                p += c;
                n.walk(c);
                if (c == ':') {
                    Character d = getNextKey();
                    p += d;
                    if (d== 'Q') {
                        quit = true;
                    } else {
                        n.walk(d);
                    }
                }
                ter.renderFrame(n.getMap());
                // TODO: mousePress or hover over
                // mouseX/Y records the cur mouse location
                TETile[][] finalWorldFrame = n.getMap();
                int mX = (int) StdDraw.mouseX();
                int mY = (int) StdDraw.mouseY();
                if(mX>=0 && mX<=79 &&mY >=0 && mY<=29) {
                    TETile tile = finalWorldFrame[mX][mY];
                    // TODO: put text on canvas
                    outputTile(tile.description(), name);
                }

            }
            writeContents(f, p);
            //TODO write p into file

            message("Successfully quit.");
            StdDraw.pause(1500);
            System.exit(0);

        } else if(c =='L'){
            //TODO read p from file and do this
            //PrintWriter.out();

            try {
                BufferedReader in = new BufferedReader(new FileReader("LastMap.txt"));
                p = in.readLine();

            } catch (FileNotFoundException ex){
                System.out.println(ex);
            } catch (IOException ex){
                System.out.println(ex);
            }
            ter.initialize(WIDTH, 35);
            MakeWorldMap n = inputString(p);
            ter.renderFrame(n.getMap());
            while(quit == false) {
                c = getNextKey();
                p += c;
                n.walk(c);
                if (c == ':') {
                    Character d = getNextKey();
                    p += d;
                    if (d== 'Q') {
                        quit = true;
                    } else {
                        n.walk(d);
                    }
                }
                ter.renderFrame(n.getMap());
                // TODO: mousePress or hover over
                // mouseX/Y records the cur mouse location
                TETile[][] finalWorldFrame = n.getMap();
                int mX = (int) StdDraw.mouseX();
                int mY = (int) StdDraw.mouseY();
                if(mX>=0 && mX<=79 &&mY >=0 && mY<=29) {
                    TETile tile = finalWorldFrame[mX][mY];
                    // TODO: put text on canvas
                    outputTile(tile.description(), name);
                }

            }
            // TODO write p into file
            writeContents(f, p);

            message("Successfully quit.");
            StdDraw.pause(1000);
            System.exit(0);
            //continue to walk but map object is trouble


        } else if (c == 'Q') {
            message("Successfully quit.");
            StdDraw.pause(1500);
            System.exit(0);
        } else if (c == 'G') {
            String n = getName();
            interactWithKeyboard(n);
            message("Successfully quit.");
            StdDraw.pause(1500);
            System.exit(0);
        }else {
            message("Please input N, L, Q, or G.");
            StdDraw.pause(1500);
            System.exit(0);
        }

    }

    public String getName(){
        String str = "";
        Font font = new Font("Monaco", Font.BOLD, 50);
        StdDraw.setFont(font);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 80);
        StdDraw.setYscale(0, 30);

        StdDraw.setPenColor(Color.PINK);
        StdDraw.text(40, 20, "Please enter name, end with period");
        Font smFont = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(smFont);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(40, 9, str);
        StdDraw.show();
        Character c = getNextKey();

        while (c !='.') { // while the str length < n, if StdDraw.hasNext
            StdDraw.setFont(font);
            StdDraw.clear(Color.BLACK);
            StdDraw.enableDoubleBuffering();
            StdDraw.setXscale(0, 80);
            StdDraw.setYscale(0, 30);

            StdDraw.setPenColor(Color.PINK);
            StdDraw.text(40, 20, "Please enter name, end with period");
            StdDraw.setFont(smFont);
            StdDraw.setPenColor(Color.WHITE);
            str += c;
            StdDraw.text(40, 9, str);
            StdDraw.show();
            c = getNextKey();
        }
        return str;
    }

    public void outputTile (String tile, String name) {
        StdDraw.enableDoubleBuffering();

        Font smFont = new Font("Monaco", Font.BOLD, 15);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(smFont);
        StdDraw.text(7, 32, tile);
        StdDraw.text(40, 32, name);
        StdDraw.show();
        //StdDraw.pause(1500);
    }

    public void menu(String s){
        StdDraw.setCanvasSize(80*16, 30*16);
        Font font = new Font("Monaco", Font.BOLD, 50);
        StdDraw.setFont(font);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 80);
        StdDraw.setYscale(0, 30);

        StdDraw.setPenColor(Color.PINK);
        StdDraw.text(40, 22, "The Maze");
        Font smFont = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(smFont);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(40, 15, "New Game (N)");
        StdDraw.text(40, 12, "Load Game (L)");
        StdDraw.text(40, 9, "Quit (Q)");
        StdDraw.text(40, 6, "Give Name (G)");
        Font smsmFont = new Font("Monaco", Font.BOLD, 15);
        StdDraw.setFont(smsmFont);
        StdDraw.text(40, 2.5, s);
        StdDraw.show();

    }

    public String solicitNCharsInput() {
        String str = "";

        Font font = new Font("Monaco", Font.BOLD, 50);
        StdDraw.setFont(font);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 80);
        StdDraw.setYscale(0, 30);

        StdDraw.setPenColor(Color.PINK);
        StdDraw.text(40, 20, "Please enter seed, ending with S");
        Font smFont = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(smFont);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(40, 9, str);
        StdDraw.show();
        Character c = getNextKey();

        while (c !='S') { // while the str length < n, if StdDraw.hasNext
            StdDraw.setFont(font);
            StdDraw.clear(Color.BLACK);
            StdDraw.enableDoubleBuffering();
            StdDraw.setXscale(0, 80);
            StdDraw.setYscale(0, 30);

            StdDraw.setPenColor(Color.PINK);
            StdDraw.text(40, 20, "Please enter seed, ending with S");
            StdDraw.setFont(smFont);
            StdDraw.setPenColor(Color.WHITE);
            str += c;
            StdDraw.text(40, 9, str);
            StdDraw.show();
            c = getNextKey();

        }

        return str;
    }


    public void message(String s){
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(15, 25, s);
        StdDraw.show();
    }

    public char getNextKey() {
        while (true) {
        if (StdDraw.hasNextKeyTyped()) {
            char c = Character.toUpperCase(StdDraw.nextKeyTyped());
            return c;
            }
        }
    }

    public MakeWorldMap inputString(String input){
        String s = "";
        int ct = 1;
        while (Character.toUpperCase(input.charAt(ct)) != 'S'){ //TODO not sure if anything is in between and also if it ends in :q
            s += input.charAt(ct);
            ct ++;
        } //ct stays at index for S.
        long seed = Long.parseLong(s);
        MakeWorldMap n = new MakeWorldMap(seed, true);
        //TETile[][] finalWorldFrame = n.getMap(); //n234sadef:q
        while(ct!=input.length()-1){ //not q but stops at :
            ct++;
            n.walk(Character.toUpperCase(input.charAt(ct)));

        }
        if(DEBUG) {

            ter.initialize(WIDTH, HEIGHT);
            ter.renderFrame(n.getMap());
        }

        return n;
    }





    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // TODO: Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.

        //TETile[][] finalWorldFrame = null;
        if(Character.toUpperCase(input.charAt(0))!='N' && Character.toUpperCase(input.charAt(0))!='L'){
            return null;
        } //N7193300625454684331SAAAWASDAAWDWSD
        File f = new File("LastMap.txt");
        int ct = 1;
        MakeWorldMap n;
        String p ="";

        if(Character.toUpperCase(input.charAt(0))=='N') {
            String s = "";
            p += "N";

            while (Character.toUpperCase(input.charAt(ct)) != 'S') { //TODO not sure if anything is in between and also if it ends in :q
                s += input.charAt(ct);
                ct++;
            } //ct stays at index for S.
            long seed = Long.parseLong(s);
            p += s;
            n = new MakeWorldMap(seed, true);
            p +="S";
        } else {

            try{
                BufferedReader in = new BufferedReader(new FileReader("LastMap.txt"));
                p = in.readLine();

            } catch (FileNotFoundException ex){
                System.out.println(ex);
            } catch (IOException ex){
                System.out.println(ex);
            }
            n = inputString(p);
            ct--;

        }

        while(ct!=input.length()-1){ //not q but stops at :
            ct++;
            n.walk(Character.toUpperCase(input.charAt(ct)));
            p += Character.toUpperCase(input.charAt(ct));
        }
        if (DEBUG) {
            ter.initialize(WIDTH, HEIGHT);
            ter.renderFrame(n.getMap());
        }
        writeContents(f, p);
        return n.getMap();
    }

    static void writeContents(File file, Object... contents) {
        try {
            if (file.isDirectory()) {
                throw
                        new IllegalArgumentException("cannot overwrite directory");
            }
            BufferedOutputStream str =
                    new BufferedOutputStream(Files.newOutputStream(file.toPath()));
            for (Object obj : contents) {
                if (obj instanceof byte[]) {
                    str.write((byte[]) obj);
                } else {
                    str.write(((String) obj).getBytes(StandardCharsets.UTF_8));
                }
            }
            str.close();
        } catch (IOException | ClassCastException excp) {
            throw new IllegalArgumentException(excp.getMessage());
        }
    }

}
