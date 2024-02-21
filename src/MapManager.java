import java.io.*;
import java.util.*;
public class MapManager {


    //MapManager static class method:  void loadMap(ArrayList<String>, String[][])
    public static void loadMap(ArrayList<String> lines, String[][] map) {
        int r = 0;
        for(String line : lines) {

            String[] squares = line.split("\\s+");
            map[r++] = squares;
        }

    }

    //MapManager static class method: void printMap(String[][])
    public static void printMap(String[][] map) {

        for(int row=0; row<map.length; row++) {
            for(int col=0; col<map[row].length; col++) {
                System.out.print(map[row][col]);
                if(map[row][col].length() == 1) {
                    System.out.print("  ");
                }
                else {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }

    }

    public static void printMap(String[][] map, int playerRow, int playerCol) {

        for(int row=0; row<map.length; row++) {
            for(int col=0; col<map[row].length; col++) {
                if(row == playerRow && col == playerCol) {
                    System.out.print("P  ");
                } else {
                    System.out.print(map[row][col]);
                    if(map[row][col].length() == 1) {
                        System.out.print("  ");
                    }
                    else {
                        System.out.print(" ");
                    }
                }
            }
            System.out.println();
        }

    }

    //MapManager static class method:   ArrayList<String> Utils.loadFile(String)
    public static ArrayList<String> loadFile(String fileName) {

        ArrayList<String> lines = new ArrayList<>();

        try {
            Scanner fileIn = new Scanner(new File(fileName));
            while (fileIn.hasNext())
            {
                String lineIn = fileIn.nextLine();
                lines.add(lineIn);
            }
            fileIn.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return lines;
    }


}
