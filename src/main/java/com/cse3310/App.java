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
    int numReady = 0;
    int GameId = 0;

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
        /*
         * String filename = "words.txt";
         * //Read in file of words
         * ArrayList<String> wordList = new ArrayList<>();
         * try(BufferedReader br = new BufferedReader(new FileReader(filename)))
         * {
         * String line;
         * while((line = br.readLine()) != null)
         * {
         * wordList.add(line.trim());
         * }
         * 
         * }
         * catch (IOException e)
         * {
         * System.err.println("Error reading file:"+ e.getMessage());
         * }
         * GsonBuilder builder = new GsonBuilder();
         * Gson gson = builder.create();
         * String jsonString = gson.toJson(LobbyUsers);
         * broadcast(jsonString);
         */
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
                        if (LobbyUsers.get(j).ready == true) {
                            numReady--;
                        }
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

            for (User a : ActiveUsers) {
                if (a.username.equals(U.UserId)) {
                    Error err = new Error(U.UserId, "Error: Username already exists. Enter another name.");
                    conn.send(gson.toJson(err));
                    return;
                }
            }

            User userRequest = new User(U.UserId, conn);

            // Choosing random user color
            userRequest.color = U.color;

            System.out.println("PRINTING LOBBY FROM REQUEST 1 ");
            ActiveUsers.add(userRequest);
            for (User x : ActiveUsers) {
                System.out.println(x.username);
            }
            System.out.println();

            LobbyUsers.add(new Lobby(userRequest));
            ServerEvent sendBack = new ServerEvent(1, LobbyUsers);
            String jsonString = gson.toJson(sendBack);
            // broadcast(jsonString);

            for (User a : ActiveUsers) {
                a.conn.send(jsonString);
            }

        } else if (U.request == 2) // User readying or unreadying. Update on everyone's screen.
        {
            System.out.println("ENTERED HERE");
            for (Lobby i : LobbyUsers) {
                if (i.user.equals(U.UserId)) {
                    i.ready = !i.ready;
                    if (i.ready == true) {
                        numReady++;
                    } else {
                        numReady--;
                    }
                }
            }

            System.out.println("NUMREADY: " + numReady);
            ServerEvent sendBack = new ServerEvent(1, LobbyUsers);
            String jsonString = gson.toJson(sendBack);
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
            int id = U.buttonId;
            int userIndex = 0;
            int gameid = 0;

            for (User user : ActiveUsers) {
                if (U.UserId.equals(user.username)) {
                    gameid = user.GameId;
                    userIndex = ActiveUsers.indexOf(user);

                    System.out.println("gameid = " + gameid);
                    System.out.println("user.GameId = " + user.GameId);
                }
            }

            Game g = ActiveGames.get(gameid);

            if (g.ActiveButtons.contains(id)) {
                g.ActiveButtons.remove(g.ActiveButtons.indexOf(id));
            } else {
                g.ActiveButtons.add(id);
            }

            // Check if button id is the end of a word
            if (g.isEnd(id) == 0) {
                int startId = g.startIds.get(g.endIds.indexOf(id));

                // If the start button is also active
                if (g.ActiveButtons.contains(startId)) {
                    g.CompletedButtons = g.getCompletedButtons(startId, id);
                    ActiveUsers.get(userIndex).wordCount++;
                }
            }

            UserEvent e = new UserEvent();
            e.buttonId = U.buttonId;
            e.UserId = U.UserId;
            e.GameId = GameId;
            e.color = U.color;
            e.completedButtons = g.CompletedButtons;
            e.request = U.request;

            String jsonString = gson.toJson(e);
            System.out.println(jsonString);

            for (User u : ActiveUsers) {
                if (u.GameId == gameid) {
                    System.out.println("User " + u.username + " assigned game " + gameid);
                    u.conn.send(jsonString);
                }
            }

            g.CompletedButtons.clear();

        } else if (U.request == 5) { // User has started a game
            ArrayList<User> waitingList = new ArrayList<>();

            System.out.println("NUM READY: " + numReady);
            if ((numReady > 1) && (ActiveGames.size() < 6)) {
                System.out.println("ENTERED HERE");
                // create player list and remove them from lobby
                for (int k = 0; k < LobbyUsers.size(); k++) {
                    if (LobbyUsers.get(k).ready == true) {
                        for (User a : ActiveUsers) {
                            if (a.username.equals(LobbyUsers.get(k).user)) {
                                waitingList.add(a);
                            }
                        }

                        LobbyUsers.remove(k);
                        k--;
                        numReady--;
                    }
                    if (waitingList.size() > 4) {
                        break;
                    }
                }

                // update lobby for those still there
                ServerEvent sendBack = new ServerEvent(1, LobbyUsers);
                String jsonString = gson.toJson(sendBack);
                broadcast(jsonString);

                System.out.println("PRINTING USERNAMES");
                for (User x : waitingList) {
                    System.out.println(x.username);
                }

                String filename = "words.txt";
                ArrayList<String> wordList = new ArrayList<>();
                try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        wordList.add(line.trim());
                    }

                } catch (IOException e) {
                    System.err.println("Error reading file:" + e.getMessage());
                }

                Game g = new Game(wordList, GameId);
                ActiveGames.add(g);

                // send game to those who are ready
                for (User u : waitingList) {
                    for (User user : ActiveUsers) {
                        if (user == u) {
                            System.out.println("User being assigned to game: " + GameId);
                            user.GameId = GameId;
                        }
                    }

                    jsonString = gson.toJson(ActiveGames.get(GameId));
                    u.conn.send(jsonString);
                    System.out.println("Game started. User set to game " + u.GameId);
                }

                GameId++;
            }
        }
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
