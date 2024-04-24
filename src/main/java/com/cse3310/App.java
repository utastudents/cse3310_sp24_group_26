package com.cse3310;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Collections;

import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class App extends WebSocketServer
{
    Vector<Game> ActiveGames = new Vector<Game>();
    Vector<User> ActiveUsers = new Vector<User>();
    Vector<Lobby> LobbyUsers = new Vector<Lobby>();
    int GameID;

    public App(int port){
        super(new InetSocketAddress(port));
    }

    public App(InetSocketAddress address){
        super(address);
    }

    public App(int port, Draft_6455 draft){
        super(new InetSocketAddress(port), Collections.<Draft>singletonList(draft));
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {

        String filename = "words.txt";
        //Read in file of words
        ArrayList<String> wordList = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(filename)))
        {
            String line;
            while((line = br.readLine()) != null)
            {
                wordList.add(line.trim());
            }

        }
        catch (IOException e)
        {
            System.err.println("Error reading file:"+ e.getMessage());
        }
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Game g = new Game(wordList);
        String jsonString = gson.toJson(g);
        System.out.println(jsonString);
        broadcast(jsonString);
       
        System.out.println(conn.getRemoteSocketAddress().getAddress().getHostAddress() + " connected");
    }


    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        // TODO Auto-generated method stub
        System.out.println(conn + " has closed");
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        // TODO Auto-generated method stub
        System.out.println(conn + ": " + message); // Log message in console
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        UserEvent U = gson.fromJson(message, UserEvent.class);
        System.out.println(U.UserId + " sent request " + U.request);
        
        if(U.request == 1){
            User userRequest = new User(U.UserId);
            ActiveUsers.add(userRequest);
            for(User x : ActiveUsers){
                System.out.println(x.username);
            }

            LobbyUsers.add(new Lobby(userRequest));

            String jsonString = gson.toJson(LobbyUsers);
            broadcast(jsonString);

        } else if(U.request == 2){
            for(Lobby i : LobbyUsers){
                if(i.user.equals(U.UserId)){
                    i.ready = !i.ready;
                }
            }
            String jsonString = gson.toJson(LobbyUsers);
            broadcast(jsonString);
        }
        

        /* 
        // Get our Game Object
        Game G = conn.getAttachment();
        G.Update(U);

        // send out the game state every time
        // to everyone
        String jsonString;
        jsonString = gson.toJson(G);

        System.out.println(jsonString);
        broadcast(jsonString);
        
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
        try{
            String envPort = System.getenv("HTTP_PORT");
            int httpPort;
            if(envPort != null){
                httpPort = Integer.parseInt(envPort);
            }
            else{
                httpPort = 9026;
            }

            HttpServer H = new HttpServer(httpPort, "./html");
            H.start();
            System.out.println("http Server started on port:" + httpPort);

            // create and start the websocket server
            envPort = System.getenv("WEBSOCKET_PORT");
            int socketPort;
            if(envPort != null){
                socketPort = Integer.parseInt("envPort");
            }
            else{
                socketPort = 9126;
            }
            
            App A = new App(socketPort);
            A.start();
            System.out.println("websocket Server started on port: " + socketPort);
        }
        catch (NullPointerException e){ // Checks for environment variable
            e.printStackTrace();
        }

        

    }
}
