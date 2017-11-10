package com.company;

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
            System.out.println("Llega");
            //ss.accept().getOutputStream().flush();
            cs = ss.accept();
            System.out.println("Client connected to the server " + this.routingTable.getId());
            //cs.getOutputStream().flush();
            outClient = new DataOutputStream(cs.getOutputStream());
            outClient.flush();
            DataInputStream input = new DataInputStream(cs.getInputStream());

            //System.out.println(serverMessage);
            while (this.active) {
                Thread.sleep(3000);
                if ((serverMessage = input.readUTF()) != null) {
                    System.out.println("PILI: " + serverMessage);
                    routingTable.receiveUpdate(serverMessage);
                    outClient.flush();
                    outClient.writeUTF("Hola, amigo");
                    outClient.flush();
                    //outClient.writeUTF(routingTable.getUpdatePackage(" ")); // WARNING
                    //routingTable.showRoutes();
                } else {
                    System.out.println("\nServer " + this.routingTable.getId() + " finished a connection, waiting in the port " + this.port);
                    ss.close(); // Cuidado
                    cs = ss.accept();
                    System.out.println("Client connected to the server " + this.routingTable.getId());
                }
            }
            System.out.println("Salió!!");
            //System.out.println("\nServidor " + this.routingTable.getId() + " cerrado");
            //ss.close();
            //routingTable.showRoutes();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }



    public void run() {
        this.startServer();
    }
}