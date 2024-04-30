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
    String appVersion = "";

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

        ServerEvent sendBack = new ServerEvent(1, LobbyUsers);
        String jsonString = gson.toJson(sendBack);
        broadcast(jsonString);
        System.out.println(conn.getRemoteSocketAddress().getAddress().getHostAddress() + " connected");
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        int gameid = 0;
        PlayerList list = new PlayerList();
        User thisUser = new User();

        int found = 0;
        for (User u : ActiveUsers) {
            if (u.conn == conn) {
                thisUser = u;
                found = 1;
            }
        }

        gameid = thisUser.GameId;

        // Fill list with player data from the game (EXCEPT THE USER THAT IS ABOUT TO
        // LEAVE)
        for (User user : ActiveUsers) {
            if (gameid == user.GameId && thisUser.username != user.username) {
                list.players.add(user.username);
                list.playerScores.add(user.wordCount);
            }
        }

        // Send completed game list to all users in the specific game
        int usersIngame = 0;
        for (User user : ActiveUsers) {
            if (gameid == user.GameId && thisUser.username != user.username) {
                usersIngame++;
                String jsonString = gson.toJson(list);
                user.conn.send(jsonString);
            }
        }

        System.out.println("removing " + thisUser.username + " from player and lobby list");
        String tempName = thisUser.username;

        for (int j = 0; j < LobbyUsers.size(); j++) {
            if (LobbyUsers.get(j).user.equals(tempName)) {
                if (LobbyUsers.get(j).ready == true) {
                    numReady--;
                }
                LobbyUsers.remove(j);
            }
        }

        if (ActiveUsers != null && found == 1) {
            ActiveUsers.remove(ActiveUsers.indexOf(thisUser));
        }

        if (usersIngame < 2) {
            forceDisconnect(gameid);
            for (User u : ActiveUsers) {
                if (u.GameId == gameid) {
                    for (Lobby lobby : LobbyUsers) {
                        if (u.username.equals(lobby.user)) {
                            LobbyUsers.remove(lobby);
                        }
                    }
                }
            }
        }
        if (LobbyUsers != null) {
            String jsonString = gson.toJson(LobbyUsers);
            broadcast(jsonString);
        }

        System.out.println(conn + " has closed");
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println(conn + ": " + message); // Log message in console

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        UserEvent U = gson.fromJson(message, UserEvent.class);
        System.out.println(U.UserId + " sent request " + U.request);

        if (U.request == 0) {
            System.out.println("Version " + appVersion + " applied to title");
            appVersion = System.getenv("VERSION");
            Version version = new Version(appVersion);

            String jsonString = gson.toJson(version);

            broadcast(jsonString);
        }

        if (U.request == 1) { // New user logged in
            if (U.UserId == "") {
                Error err = new Error(U.UserId, "Error: Enter a username.");
                conn.send(gson.toJson(err));
                return;
            }

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

            broadcast(jsonString);

        } else if (U.request == 2) // User readying or unreadying. Update on everyone's screen.
        {
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

            ServerEvent sendBack = new ServerEvent(1, LobbyUsers);
            String jsonString = gson.toJson(sendBack);
            broadcast(jsonString);
        } else if (U.request == 3) // User has sent message. Update on everyone's screen;
        {
            // U has:
            // - request #3
            // - GameId
            // - UserId
            // - chatMessage

            UserEvent e = new UserEvent();
            e.UserId = U.UserId;
            e.request = U.request;
            e.chatMessage = U.chatMessage;
            e.color = U.color;

            int senderGameId = -1;

            for (User user : ActiveUsers) {
                if (U.UserId.equals(user.username)) {
                    senderGameId = user.GameId;
                }
            }

            for (User user : ActiveUsers) {
                if ((U.UserId.equals(user.username) != true) && (senderGameId == user.GameId)) {
                    user.conn.send(gson.toJson(e));
                }
            }

        } else if (U.request == 4) { // User has pressed a letter. Update button data.
            int id = U.buttonId;
            int userIndex = 0;
            int gameid = 0;

            for (User user : ActiveUsers) {
                if (U.UserId.equals(user.username)) {
                    gameid = user.GameId;
                    userIndex = ActiveUsers.indexOf(user);
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
                    g.AllCompletedButtons.addAll(g.CompletedButtons);

                    ActiveUsers.get(userIndex).wordCount++;
                    updateScores(U.UserId);
                }
            }

            UserEvent e = new UserEvent();
            e.buttonId = U.buttonId;
            e.UserId = U.UserId;
            e.GameId = gameid;
            e.color = U.color;
            e.completedButtons = g.CompletedButtons;
            e.request = U.request;

            String jsonString = gson.toJson(e);
            System.out.println(jsonString);

            for (User u : ActiveUsers) {
                if (u.GameId == gameid) {
                    u.conn.send(jsonString);
                }
            }

            g.CompletedButtons.clear();

        } else if (U.request == 5) { // User has started a game
            ArrayList<User> waitingList = new ArrayList<>();

            if ((numReady > 1) && (ActiveGames.size() < 6)) {
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
                System.out.println();

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
                            System.out.println("User " + user.username + " assigned to game: " + GameId);
                            user.GameId = GameId;
                        }
                    }

                    jsonString = gson.toJson(ActiveGames.get(GameId));
                    u.conn.send(jsonString);
                }

                GameId++;
            }
        } else if (U.request == 6) { // Update player scores for the current game
            updateScores(U.UserId);
        }
    }

    public void forceDisconnect(int GameId) {
        ArrayList<User> disconnectUsers = new ArrayList<>();
        for (User u : ActiveUsers) {
            if (u.GameId == GameId) {
                u.conn.send("disconnect");
                disconnectUsers.add(u);
            }
        }

        for (User u : disconnectUsers) {
            ActiveUsers.remove(ActiveUsers.indexOf(u));
        }
    }

    public void updateScores(String UserId) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        PlayerList list = new PlayerList();
        int gameid = 0;

        // Find game the user is in
        for (User user : ActiveUsers) {
            if (UserId.equals(user.username)) {
                gameid = user.GameId;
            }
        }

        // Fill list with player data from the game
        for (User user : ActiveUsers) {
            if (gameid == user.GameId) {
                list.players.add(user.username);
                list.playerScores.add(user.wordCount);
            }
        }

        // Send completed game list to all users in the specific game
        for (User user : ActiveUsers) {
            if (gameid == user.GameId) {
                String jsonString = gson.toJson(list);
                user.conn.send(jsonString);
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

    class LetterTimer extends TimerTask {
        public void run() {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            int randChoice;
            int chosenButton;

            for (int i = 0; i < ActiveGames.size(); i++) {
                Game g = ActiveGames.get(i);

                do {
                    randChoice = (int) Math.floor(Math.random() * g.startIds.size());
                    chosenButton = g.startIds.get(randChoice);
                } while (g.AllCompletedButtons.contains(chosenButton) == true);

                for (User u : ActiveUsers) {
                    if (u.GameId == i) { // Target users in game i
                        System.out.println("Timer task has occurred on button " + chosenButton);

                        timerEvent e = new timerEvent();
                        e.timerButton = chosenButton;

                        u.conn.send(gson.toJson(e));
                    }
                }
            }

        }
    }

    public static void main(String[] args) {

        // Set up the http server
        try {
            String envPort = System.getenv("HTTP_PORT");
            System.out.println(envPort);
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
                socketPort = Integer.valueOf(envPort);
            }

            App A = new App(socketPort);
            A.start();

            LetterTimer letterTimer = A.new LetterTimer();

            Timer timer = new Timer();
            timer.scheduleAtFixedRate(letterTimer, 0, 30000);

            String[] c = { "Red", "Green", "Blue", "Yellow", "Purple" };

            A.colors.addAll(Arrays.asList(c));

            System.out.println("websocket Server started on port: " + socketPort);
        } catch (NullPointerException e) { // Checks for environment variable
            e.printStackTrace();
        }

    }
}
