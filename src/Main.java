import java.util.*;
public class Main {
    public static void main(String[] args) {


    /*
        to do:

        get ankh
        kill mummy king
        get treasure
        exit

        intro text
        auto-map...keep track of where user has been and only show that portion
        redraw screen after every command
        make variations in the wall type descriptions
        add monsters and combat
        add weapons and armor
        add potions - health and buffs
        add spells

    */


        Game game = new Game();
        Scanner sc = new Scanner(System.in);
        //print intro ascii art intro
        //System.out.println(game.look());
        String command = "";

        while (!game.getGameOver()) {

            System.out.print("command > ");
            command = sc.nextLine();
            command = command.trim();
            if (command.equals("quit")) {
                break;
            }

            System.out.println(game.processCommand(command));

        }

        //print ascii art reward
        System.out.println("\nScore: " + game.getScore());
        System.out.println("See ya!");

    }

}

