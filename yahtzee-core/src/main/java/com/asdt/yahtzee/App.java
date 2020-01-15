package com.asdt.yahtzee;

import com.asdt.yahtzee.game.Game;
import com.asdt.yahtzee.network.Client;
import com.asdt.yahtzee.network.Server;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        System.out.println("Yahtzee!");
        Game game = new Game();

        int tries = 0;

        if (args.length == 0) {
            (new UI(game)).gameStart();
        } else if (args.length == 1) {
            try {
                tries = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Argument" + args[0] + " must be an integer.");
                System.exit(1);
            }
            new SimStat(game, tries);
        } else if (args.length == 2) {
            String host = "";
            int port = 3000;
            try {
                host = args[0];
                port = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.err.println("Argument" + args[1] + " is a port and must be an integer.");
                System.exit(1);
            }
            if (host.equals("server")) {
                Server server = new Server(port);
                server.start();
                System.out.println("Server is running");

            } else {
                Client client = new Client(host, port);
                client.start();
            }
        }

    }
}
