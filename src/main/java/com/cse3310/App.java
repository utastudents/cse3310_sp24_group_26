package com.cse3310;

import java.io.BufferedReader;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class App extends WebSocketServer
{
    Vector<Game> ActiveGames = new Vector<Game>();
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onOpen'");
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onClose'");
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        // TODO Auto-generated method stub
        System.out.println(conn + ": " + message); // Log message in console

        // Bring in the data from the webpage
        // A UserEvent is all that is allowed at this point
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        UserEvent U = gson.fromJson(message, UserEvent.class);
        System.out.println(U.Button);

        // Get our Game Object
        Game G = conn.getAttachment();
        G.Update(U);

        // send out the game state every time
        // to everyone
        String jsonString;
        jsonString = gson.toJson(G);

        System.out.println(jsonString);
        broadcast(jsonString);
        throw new UnsupportedOperationException("Unimplemented method 'onMessage'");
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
        String envPort = System.getenv("HTTP_PORT");
        int httpPort = Integer.parseInt(envPort);
        HttpServer H = new HttpServer(httpPort, "./html");
        H.start();
        System.out.println("http Server started on port:" + httpPort);

        // create and start the websocket server
        envPort = System.getenv("WEBSOCKET_PORT");
        int socketPort = Integer.parseInt("envPort");
        App A = new App(socketPort);
        A.start();
        System.out.println("websocket Server started on port: " + socketPort);
    }
}
