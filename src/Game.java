import java.util.*;

public class Game {

    private final int ROWS = 8;
    private final int COLS = 10;

    private Inventory inventory;
    private int score;
    private String[][] map;
    private int playerRow;
    private int playerCol;
    private boolean gameOver = false;

    public Game() {
        inventory = new Inventory();
        score = 0;

        map = new String[ROWS][COLS];
        ArrayList<String> lines = MapManager.loadFile("./resources/map.txt");
        MapManager.loadMap(lines, map);
        setInitialLocation();

    }

    private void setInitialLocation() {

        for(int row=0; row<ROWS; row++) {
            for(int col=0; col<COLS; col++) {
                if(map[row][col].equals("EN")) {
                    this.playerRow = row;
                    this.playerCol = col;
                    return;
                }
            }
        }

        System.out.println("Could not find entrance location!");

    }

    public boolean getGameOver() {
        return this.gameOver;
    }

    private String goDirection(String direction) {
        String result = "Evaluation of direction failed.";
        int peekRow = this.playerRow;
        int peekCol = this.playerCol;

        switch(direction) {
            case "north":
                if(peekRow-1 >= 0) {
                    peekRow--;
                }
                break;
            case "south":
                if(peekRow+1 < this.ROWS) {
                    peekRow++;
                }
                break;
            case "east":
                if(peekCol+1 < this.COLS) {
                    peekCol++;
                }
                break;
            case "west":
                if(peekCol-1 >= 0) {
                    peekCol--;
                }
                break;
        }

        if(peekRow == this.playerRow && peekCol == this.playerCol) {
            return "A cold impenetrable stone wall bars your way to the " + direction + ".";
        }

        String rawValue = map[peekRow][peekCol];
        if(rawValue.startsWith("K")) {
            this.playerCol = peekCol;
            this.playerRow = peekRow;
            return "Well, you are stepping on a key, maybe you should take it with you.";
        }

        if(rawValue.startsWith("D")) {
            return "A locked door bars your way.";
        }

        if(rawValue.startsWith("T")) {
            handleTeleporter(rawValue);
            return "A blinding flash precedes a wrenching feeling of dislocation.  Where are you?";
        }

        switch(rawValue) {
            case "0":
                this.playerCol = peekCol;
                this.playerRow = peekRow;
                result = "You move cautiously forward to the " + direction + ".";
                break;
            case "1":
                result = "Although interesting, the wall covered with hieroglyphs does not allow you to pass.";
                break;
            case "EN":
                this.playerCol = peekCol;
                this.playerRow = peekRow;
                result = "You've come back to the beginning - you are at the entrace to this cursed Pyramid.";
                break;
            case "A":
                this.playerCol = peekCol;
                this.playerRow = peekRow;
                result = "You're standing on a curious flagstone that gives off a faint vibration.";
                break;
            case "AN":
                this.playerCol = peekCol;
                this.playerRow = peekRow;
                result = "You're standing on a beautiful Ankh.";
                break;
            case "M":
                result = "The terrifying Mummy King bars your way forward!";
                //TODO: print ascii art of mummy!
                break;
            case "EX":
                result = "You crawl out of the narrow tunnel that leads to freedom!  You win!";
                this.score += 100;
                this.gameOver = true;
                break;
            case "$":
                this.playerCol = peekCol;
                this.playerRow = peekRow;
                result = "You are standing on a pile of gold.  I don't think anyone would miss it if you...borrow some.";
                break;
        }

        return result;
    }

    private void handleTeleporter(String rawValue) {
        //parse the raw value
        //the game supports teleporters A-L
        String teleporterDestination = rawValue.substring(1);

        for(int row=0; row<this.ROWS; row++) {
            for(int col=0; col<this.COLS; col++) {
                if(map[row][col].equals(teleporterDestination)) {
                    this.playerRow = row;
                    this.playerCol = col;
                    return;
                }
            }
        }

    }

    public String processCommand(String command) {
        String feedback = "Sorry, I don't understand that.";

        command = command.toLowerCase();
        if(command.equals("inventory")) {
            return this.inventory.getInventory();
        }

        if(command.equals("map")) {
            MapManager.printMap(map, this.playerRow, this.playerCol);
            System.out.println();
            return "";
        }

        if(command.equals("look")) {
            return this.look();
        }

        //shortcuts for directions
        if(command.equals("n")) {
            command = "north";
        }
        if(command.equals("s")) {
            command = "south";
        }
        if(command.equals("e")) {
            command = "east";
        }
        if(command.equals("w")) {
            command = "west";
        }

        if(command.equals("north") ||
                command.equals("south") ||
                command.equals("east") ||
                command.equals("west")) {
            feedback = goDirection(command);
        }

        //get key, get ankh, get gold
        if(command.startsWith("get")) {
            feedback = tryGetting(command);
        }

        //use ankh
        if(command.startsWith("use")) {
            feedback = tryUse(command);
        }

        //open door north/south/west/east
        if(command.startsWith("open")) {
            feedback = tryOpen(command);
        }

        return feedback;
    }

    public String tryUse(String command) {
        String result = "";

        String[] parsed = command.split(" ");
        if(parsed.length < 2) {
            return "I don't understand that.\n" + "Usage: use <item>";
        }

        if(parsed[1].equals("ankh")) {
            if(inventory.hasAnkh()) {
                if(isMummyAdjacent()) {
                    return killMummy();
                } else {
                    return "The Ankh flares with a blinding yellow light but nothing else happens.";
                }
            } else {
                return "You don't have an Ankh!";
            }
        }

        return result;
    }

    private String killMummy() {
        int mummyRow = 0;
        int mummyCol = 0;

        //find Mummy King.  Game supports only 1 Mummy King, the mighty Mummra!
        for(int row=0; row<ROWS; row++) {
            for(int col=0; col<COLS; col++) {
                if(map[row][col].equals("M")) {
                    mummyRow = row;
                    mummyCol = col;
                }
            }
        }

        map[mummyRow][mummyCol] = "0";  //poof!

        return "The Ankh flares with a blinding yellow light, turning the Mummy King Mummra to ash!";
    }

    private boolean isMummyAdjacent() {
        boolean isAdjacent = false;
        int mummyRow = 0;
        int mummyCol = 0;

        //find Mummy King.  Game supports only 1 Mummy King!
        for(int row=0; row<ROWS; row++) {
            for(int col=0; col<COLS; col++) {
                if(map[row][col].equals("M")) {
                    mummyRow = row;
                    mummyCol = col;
                }
            }
        }

        if(this.playerRow == mummyRow) {
            if(this.playerCol-1 == mummyCol || this.playerCol+1 == mummyCol) {
                isAdjacent = true;
            }
        }

        if(this.playerCol == mummyCol) {
            if(this.playerRow-1 == mummyRow || this.playerRow+1 == mummyRow) {
                isAdjacent = true;
            }
        }

        return isAdjacent;
    }

    private String tryGetting(String command) {
        String result = "";
        int targetRow = this.playerRow;
        int targetCol = this.playerCol;

        String[] parsed = command.split(" ");
        if(parsed.length < 3) {
            return "I don't understand that.\n" + "Usage:  get <item> <direction>";
        }

        switch(parsed[2]) {
            case "north":
                targetRow--;
                break;
            case "south":
                targetRow++;
                break;
            case "east":
                targetCol++;
                break;
            case "west":
                targetCol--;
                break;
        }

        if(targetRow > ROWS-1 || targetRow < 0 ||
                targetCol > COLS-1 || targetCol < 0) {
            result = "There is nothing to get in that direction.";
        }
        else {
            String rawValue = map[targetRow][targetCol];
            Item item = Item.getItemInfo(rawValue);
            if(item == null) {
                result = "You can't pick that up.";
            } else {
                score += item.getExperienceValue();
                this.inventory.addItem(item);
                map[targetRow][targetCol] = "0";
                result = "You pick up the " + item.getName() + " and put it in your pocket.";
            }
        }

        return result;
    }

    private String tryOpen(String command) {
        String result = "";
        int targetRow = this.playerRow;
        int targetCol = this.playerCol;

        String[] parsed = command.split(" ");
        if(parsed.length < 3) {
            return "I don't understand that.\n" + "Usage:  open door <direction>";
        }

        switch(parsed[2]) {
            case "north":
                targetRow--;
                break;
            case "south":
                targetRow++;
                break;
            case "east":
                targetCol++;
                break;
            case "west":
                targetCol--;
                break;
        }

        if(targetRow > ROWS-1 || targetRow < 0 ||
                targetCol > COLS-1 || targetCol < 0) {
            result = "There is no door in that direction.";
        } else {
            String rawValue = map[targetRow][targetCol];
            if(rawValue.startsWith("D")) {
                if(inventory.hasKeyForDoor(rawValue)) {
                    map[targetRow][targetCol] = "0";
                    Item key = inventory.getKeyForDoor(rawValue);
                    inventory.removeItem(key.getRawValue());
                    score += 10;
                    result = "With an audible click you unlock the door and swing it open.";
                } else {
                    result = "You don't have a key to this door.";
                }
            } else {
                result = "There is no door in that direction.";
            }
        }

        return result;
    }

    public String look() {

        String north = "", south = "", east = "", west = "";
        String peek = null;

        //north
        if(playerRow-1 >= 0) {
            peek = map[playerRow-1][playerCol];
            if(isItem(peek)) {
                north = "You see a " + Item.getItemInfo(peek).getName() +
                        " to the north.";
            } else {
                north = "You see a " + getRawDescription(peek) +
                        " to the north.";
            }
        } else {
            north = "A cold impenetrable stone wall towers above you to the north.";
        }

        //south
        if(playerRow+1 < ROWS) {
            peek = map[playerRow+1][playerCol];
            if(isItem(peek)) {
                south = "You see a " + Item.getItemInfo(peek).getName() +
                        " to the south.";
            } else {
                south = "You see a " + getRawDescription(peek) +
                        " to the south.";
            }
        } else {
            south = "A cold impenetrable stone wall towers above you to the south.";
        }

        //east
        if(playerCol+1 < COLS) {
            peek = map[playerRow][playerCol+1];
            if(isItem(peek)) {
                east = "You see a " + Item.getItemInfo(peek).getName() +
                        " to the east.";
            } else {
                east = "You see a " + getRawDescription(peek) +
                        " to the east.";
            }
        } else {
            east = "A cold impenetrable stone wall towers above you to the east.";
        }

        //west
        if(playerCol-1 >= 0) {
            peek = map[playerRow][playerCol-1];
            if(isItem(peek)) {
                west = "You see a " + Item.getItemInfo(peek).getName() +
                        " to the west.";
            } else {
                west = "You see a " + getRawDescription(peek) +
                        " to the west.";
            }
        } else {
            west = "A cold impenetrable stone wall towers above you to the west.";
        }

        //System.out.println("\nLocation: (" + playerRow + ", " + 
        //    playerCol + ")");

        return ("Score: " + this.score + "\n" +
                north + "\n" + south + "\n" + east + "\n" + west);
    }

    private String getRawDescription(String rawValue) {
        String desc = "";

        if(rawValue.equals("0")) {
            desc = "dank stretch of hallway";
        } else if(rawValue.startsWith("D")) {
            desc = "locked door";
        } else if(rawValue.startsWith("T")) {
            desc = "glowing disc on the stone floor";
        } else if(rawValue.equals("A")) {
            desc = "peculiar flagstone that gives off a faint hum";
        } else if(rawValue.equals("M")) {
            desc = "terrifying mummy who bars the way! You cannot continue";
        } else if(rawValue.equals("EN")) {
            desc = "caved in section of the Pyramid that you fell through";
        } else if(rawValue.equals("EX")) {
            desc = "sliver of sunlight coming through a crack. You've found the exit";
        } else if(rawValue.equals("1")) {
            desc = "wall covered in ancient hieroglyphs";
        }

        return desc;
    }

    private boolean isItem(String rawValue) {
        if(Item.getItemInfo(rawValue) != null) {
            return true;
        }

        return false;
    }

    public int getScore() {
        return this.score;
    }

}