package com.cse3310;

import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

public class WebSocket
{
    int port;
    Connection con;
    
    @Override
    public void onOpen(Connection conn, ClientHandshake handshake) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onOpen'");
    }

    @Override
    public void onClose(Connection conn, int code, String reason, boolean remote) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onClose'");
    }

    @Override
    public void onMessage(Connection conn, String message) {
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
    public void onError(Connection conn, Exception ex) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onError'");
    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onStart'");
    }


}