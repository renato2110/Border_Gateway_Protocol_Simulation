package com.company;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.*;
/**
 * Created by Renato & Vladimir on 21/10/2017.
 */

public class Server extends Connection implements Runnable {
    private RoutingTable routingTable;
    private int port;

    public Server(RoutingTable routingTable, int PORT) throws IOException {
        this.port = PORT;
        this.routingTable = routingTable;
    }

    public void startServer() {
        try {

            this.initConnection("server", this.port, "localhost");

            System.out.println("\nServer " + this.routingTable.getId() + " waiting in the port " + this.port);

            cs = ss.accept();

            System.out.println("Client connected to the server " + this.routingTable.getId());
            outClient = new DataOutputStream(cs.getOutputStream());
            outClient.flush();
            DataInputStream input = new DataInputStream(cs.getInputStream());

            //System.out.println(serverMessage);
            while (this.active) {
                Thread.sleep(10000);
                if ((serverMessage = input.readUTF()) != null) {
                    System.out.println("Received Message: " + serverMessage);
                    this.connectedAS = serverMessage.split("\\*")[0];
                    System.out.println("Connected to AS: " + this.connectedAS);
                    routingTable.receiveUpdate(serverMessage);
                    outClient.writeUTF(this.routingTable.getUpdatePackage(""/*this.connectedAS*/));
                    routingTable.showRoutes();
                    outClient.flush();
                } else {
                    System.out.println("\nServer " + this.routingTable.getId() + " finished a connection, waiting in the port " + this.port);
                    ss.close();
                    cs = ss.accept();
                    System.out.println("Client connected to the server " + this.routingTable.getId());
                }
            }
        } catch (Exception e) {
            //e.printStackTrace();
            this.stop();
            this.cs = null;
            this.ss = null;
            this.outClient =null;
            this.startServer();
        }
    }



    public void run() {
        this.startServer();
    }
}