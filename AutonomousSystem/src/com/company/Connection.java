package com.company;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Renato & Vladimir on 21/10/2017.
 */
public class Connection {
    private final int PORT = 1234;
    private final String HOST = "localhost";
    protected String serverMessage;
    protected ServerSocket ss;
    protected Socket cs;
    protected DataOutputStream outServer, outClient;

    public Connection(String tipo) throws IOException{
        if (tipo.equalsIgnoreCase("server")) {
            ss = new ServerSocket(PORT);
            cs = new Socket();
        }
        else {
            cs = new Socket(HOST, PORT);
        }
    }
}
