package com.asdt.yahtzee.network;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * A new Connection is created for every client connecting to the server. The
 * connection has a listener in order to handle and optionally respond to client
 * messages. A sendObject method is used to send messages to the client.
 */
public class Connection implements Runnable {
    Socket socket;
    ObjectInputStream in;
    ObjectOutputStream out;
    Listener listener;

    int id;

    public Connection(Socket socket) {
        this.socket = socket;
        try {
            // It is important that you create first the output stream and then the input
            // stream. Otherwise it mmight deadlock.
            // Creation of the input stream is a blocking operation
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());
            listener = new Listener(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (socket.isConnected()) {
            try {
                listener.on(in.readObject());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (EOFException e) {
                System.out.println("EOFException handled. Exiting run Connection thread. id:" + id);
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Connection closed. id:" + id);
        close();
    }

    public void sendObject(Object object) {
        try {
            out.writeObject(object);
            out.flush();
        } catch (IOException e) {
            System.out.println("Cannot write to client. id:" + id);
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            System.out.println("Cannot close connection to client. id:" + id);
            e.printStackTrace();
        }
    }

    public void setId(int id) {
        this.id = id;
    }

}
