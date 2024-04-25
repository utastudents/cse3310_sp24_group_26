package com.cse3310;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Arrays;

import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class App extends WebSocketServer {
    Vector<Game> ActiveGames = new Vector<Game>();
    Vector<User> ActiveUsers = new Vector<User>();
    Vector<Lobby> LobbyUsers = new Vector<Lobby>();
    ArrayList<String> colors = new ArrayList<String>();

    int GameID;

    public App(int port) {
        super(new InetSocketAddress(port));
    }

    public App(InetSocketAddress address) {
        super(address);
    }

    public App(int port, Draft_6455 draft) {
        super(new InetSocketAddress(port), Collections.<Draft>singletonList(draft));
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String jsonString = gson.toJson(LobbyUsers);
        broadcast(jsonString);
        System.out.println(conn.getRemoteSocketAddress().getAddress().getHostAddress() + " connected");
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {

        for (int i = 0; i < ActiveUsers.size(); i++) {
            if (ActiveUsers.get(i).conn == conn) {
                System.out.println("removing " + ActiveUsers.get(i).username + " from player and lobby list");
                String tempName = ActiveUsers.get(i).username;

                for (int j = 0; j < LobbyUsers.size(); j++) {
                    if (LobbyUsers.get(j).user.equals(tempName)) {
                        LobbyUsers.remove(j);
                    }

                }
                ActiveUsers.remove(i);
                break;
            }
        }
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String jsonString = gson.toJson(LobbyUsers);
        broadcast(jsonString);

        System.out.println(conn + " has closed");
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println(conn + ": " + message); // Log message in console

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        UserEvent U = gson.fromJson(message, UserEvent.class);
        System.out.println(U.UserId + " sent request " + U.request);

        if (U.request == 1) { // New user logged in
            User userRequest = new User(U.UserId, conn);

            // Choosing random user color
            userRequest.color = U.color;

            System.out.println("User color is " + U.color);
            ActiveUsers.add(userRequest);
            for (User x : ActiveUsers) {
                System.out.println(x.username);
            }

            LobbyUsers.add(new Lobby(userRequest));

            String jsonString = gson.toJson(LobbyUsers);
            broadcast(jsonString);

        } else if (U.request == 2) // User readying or unreadying. Update on everyone's screen.
        {
            for (Lobby i : LobbyUsers) {
                if (i.user.equals(U.UserId)) {
                    i.ready = !i.ready;
                }
            }
            String jsonString = gson.toJson(LobbyUsers);
            broadcast(jsonString);
        } else if (U.request == 3) // User has sent message. Update on everyone's screen;
        {
            // Message data already packaged. Just broadcast.
            // U has:
            // - request #3
            // - GameId
            // - UserId
            // - chatMessage
            String jsonString = gson.toJson(U);
            System.out.println("User has sent message: " + jsonString);
            broadcast(jsonString);
        } else if (U.request == 4) { // User has pressed a letter. Update button data.
            String jsonString = gson.toJson(U);
            System.out.println("User has pressed a letter: " + jsonString);
            broadcast(jsonString);
        } else if (U.request == 5) { // Game started. Make the grid.
            int count = 0;

            for (Lobby user : LobbyUsers) {
                if (user.ready == true) {
                    count++;
                }
            }

            // Check if ALL lobby users are ready
            if (count == LobbyUsers.size()) {
                String filename = "words.txt";
                // Read in file of words
                ArrayList<String> wordList = new ArrayList<>();
                try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        wordList.add(line.trim());
                    }

                } catch (IOException e) {
                    System.err.println("Error reading file:" + e.getMessage());
                }

                Game g = new Game(wordList);
                String jsonString = gson.toJson(g);
                System.out.println(jsonString);
                broadcast(jsonString);

                // Since the game has started, clear the lobbyuser array
                LobbyUsers.clear();
            } else {
                Error err = new Error(U.UserId, "Not enough players ready in the lobby");
                String jsonString = gson.toJson(err);
                System.out.println(jsonString);

                broadcast(jsonString);
            }

        }

        /*
         * // Get our Game Object
         * Game G = conn.getAttachment();
         * G.Update(U);
         * 
         * // send out the game state every time
         * // to everyone
         * String jsonString;
         * jsonString = gson.toJson(G);
         * 
         * System.out.println(jsonString);
         * broadcast(jsonString);
         * 
         */
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
        if (conn != null) {
            // some errors like port binding failed may not be assignable to a specific
            // websocket
        }
    }

    @Override
    public void onStart() {
        System.out.println("Server started!");
        setConnectionLostTimeout(0);
    }

    public static void main(String[] args) {

        // Set up the http server
        try {
            String envPort = System.getenv("HTTP_PORT");
            int httpPort = 9026;
            if (envPort != null) {
                httpPort = Integer.valueOf(envPort);
            }

            HttpServer H = new HttpServer(httpPort, "./html");
            H.start();
            System.out.println("http Server started on port:" + httpPort);

            // create and start the websocket server
            envPort = System.getenv("WEBSOCKET_PORT");
            int socketPort = 9126;
            if (envPort != null) {
                socketPort = Integer.valueOf("envPort");
            }

            App A = new App(socketPort);
            A.start();

            String[] c = { "Red", "Green", "Blue", "Yellow", "Purple" };

            A.colors.addAll(Arrays.asList(c));

            System.out.println("websocket Server started on port: " + socketPort);
        } catch (NullPointerException e) { // Checks for environment variable
            e.printStackTrace();
        }

    }
}
