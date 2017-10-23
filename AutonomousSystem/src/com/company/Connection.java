package com.company;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Renato & Vladimir on 21/10/2017.
 */
public class Connection {
    private int PORT;
    private String HOST;
    protected String serverMessage;
    protected ServerSocket ss;
    protected Socket cs;
    protected DataOutputStream outServer, outClient;

    public Connection(String tipo, int PORT, String HOST) throws IOException{
        this.PORT = PORT;
        this.HOST = HOST;
        if (tipo.equalsIgnoreCase("server")) {
            ss = new ServerSocket(this.PORT);
            cs = new Socket();
        }
        else {
            cs = new Socket(HOST, PORT);
        }
    }
}
