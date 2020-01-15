package com.asdt.yahtzee.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import com.asdt.yahtzee.network.messages.KeepRequest;
import com.asdt.yahtzee.network.messages.Request;
import com.asdt.yahtzee.network.messages.Response;
import com.asdt.yahtzee.network.messages.ScoreRequest;
import com.asdt.yahtzee.network.messages.UserRequest;
import com.asdt.yahtzee.players.ConsolePlayer;
import com.asdt.yahtzee.players.GamePlayer;

public class Client {
    private static final String OPPONENT = "Opponent";
    Socket socket;
    ObjectInputStream in;
    ObjectOutputStream out;

    String name;
    Scanner scanner = new Scanner(System.in, "UTF-8");
    GamePlayer consolePlayer = new ConsolePlayer();
    private String firstPlayer;
    boolean connected = false;
    private int numOfPlayers;

    public Client(String host, int port) {
        try {
            this.socket = new Socket(host, port);
            System.out.println("Connected to server...");
            connected = true;
        } catch (IOException e1) {
            System.out.println("Cannot connect to the server " + host + ":" + port);
            // e1.printStackTrace();
        }
    }

    public void start() {
        if (!connected) {
            System.out.println("Cannot start the client. Not connected!");
            throw new RuntimeException();
        } else {
            try {
                // It is important that you create first the output stream and then the input
                // stream. Otherwise it might deadlock.
                // Creation of the input stream is a blocking operation
                out = new ObjectOutputStream(socket.getOutputStream());
                out.flush();
                in = new ObjectInputStream(socket.getInputStream());

                int clientId = (Integer) in.readObject();
                System.out.println("Your id is " + clientId);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            startUI();
        }
    }

    public void sendObject(Object object) {
        try {
            out.writeObject(object);
            out.flush();
        } catch (IOException e) {
            System.out.println("Cannot send to the server!");
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startUI() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.print("Enter your name: ");
        name = scanner.next();

        System.out.print("Solo game (1), 2-player game (2): ");
        String num = scanner.next();
        while (!(num.equals("1") || num.equals("2"))) {
            System.out.println("Please enter 1 or 2: ");
            num = scanner.next();
        }

        numOfPlayers = Integer.parseInt(num);
        sendObject(new UserRequest(name, numOfPlayers));

        // WAIT for game to start
        String str = "";
        while (!str.equals(Response.GAME_STARTED)) {
            try {
                System.out.println("Please wait for (other players to join and) the game to start....");
                str = (String) in.readObject();
                System.out.println(Response.GAME_STARTED);
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        }

        sendObject(Request.CURRENTPLAYER);
        firstPlayer = "";
        try {
            firstPlayer = (String) in.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("1st player: " + firstPlayer);

        for (int i = 0; i < 13; i++) {
            print("\n-----ROUND " + (i + 1) + "------");
            round();
        }
    }

    public void round() {
        if (numOfPlayers == 1) {
            play(name);
        } else if (numOfPlayers == 2) {
            if (firstPlayer.equals(name)) {
                play(name);
                play(OPPONENT);
            } else {
                play(OPPONENT);
                play(name);
            }

        }
    }

    public void play(String name) {
        print("\n" + name + "'s turn\n");
        print("**** 1st ROLL ****");

        if (!name.equals(OPPONENT)) {
            sendObject(new KeepRequest(name, new int[] {}));
        }

        String gameString = "";
        try {
            gameString = (String) in.readObject();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }

        print(gameString);

        for (int r = 0; r < 2; r++) {
            if (!name.equals(OPPONENT)) {
                print(name + " Write indices (1-5) of dice to keep with a 0 at the end, (-1) to keep all");

                // interact with the player
                int[] array = consolePlayer.rollKeeping();

                if (array.length == 5 || array.length == 1 && array[0] == -1) {
                    print("Keeping all");
                    sendObject(new KeepRequest(name, new int[] { 1, 2, 3, 4, 5 }));
                    gameString = "";
                    try {
                        gameString = (String) in.readObject();
                    } catch (ClassNotFoundException | IOException e) {
                        e.printStackTrace();
                    }
                    print(gameString);
                    break;
                }

                if (r == 0)
                    print("**** 2nd ROLL ****");
                else
                    print("**** 3rd ROLL ****");

                sendObject(new KeepRequest(name, array));

            } else {
                System.out.println("Wait for the opponent to choose dice to keep ...");
            }

            gameString = "";
            try {
                gameString = (String) in.readObject();
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
            print(gameString);

            // opponent has exited the loop
            if (name.equals(OPPONENT) && gameString.contains("K  K  K  K  K"))
                break;

        }

        if (!name.equals(OPPONENT)) {
            print("Enter an available scoring category xx: ");

            // interact with the player
            String categoryName = consolePlayer.selectCategory();

            sendObject(new ScoreRequest(name, categoryName));
            int score = 0;
            try {
                score = (Integer) in.readObject();
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }

            while (score < 0) {
                print("Invalid choice! " + "(score=" + score + ")");

                // interact with the player
                categoryName = consolePlayer.selectCategory();

                sendObject(new ScoreRequest(name, categoryName));
                try {
                    score = (Integer) in.readObject();
                } catch (ClassNotFoundException | IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("Wait for the opponent to choose a category for scoring...");
        }

        gameString = "";
        try {
            gameString = (String) in.readObject();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        print("###### ROUND SCORING ######");
        print(gameString);

    }

    protected void print(Object string) {
        System.out.println(string.toString());
    }

}
