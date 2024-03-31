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
/**
 * Hello world!
 *
 */
public class App extends WebSocketServer
{
    //Vector<Game> ActiveGames = new Vector<Game>();
    int GameID;

    public App(int port){

    }

    public App(InetSocketAddress address){

    }

    public App(int port, Draft_6455 draft){

    }



    public static void main(String[] args) {
        
    }
}
