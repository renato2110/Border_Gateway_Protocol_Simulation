package com.company;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Renato & Vladimir on 21/10/2017.
 */

public class Server extends Connection implements Runnable{
    private RoutingTable routingTable;
    private int port;

    public Server(RoutingTable routingTable, int PORT) throws IOException {
        this.port = PORT;
        this.routingTable = routingTable;
    }

    public void startServer() {
        try {

            this.initConnection("server", this.port, "localhost");

            System.out.println("\nServidor " + this.routingTable.getId() + " esperando...");
            cs = ss.accept();
            System.out.println("Cliente conectado en el servidor "+this.routingTable.getId());
            outClient = new DataOutputStream(cs.getOutputStream());
            outClient.flush();
            outClient.writeUTF(routingTable.getUpdatePackage(" ")); // CAMBIAAAAAAAAAAAAR

            BufferedReader input = new BufferedReader(new InputStreamReader(cs.getInputStream()));

            while ((serverMessage = input.readLine()) != null) {
                System.out.println(serverMessage);
                //routingTable.receiveUpdate(serverMessage);
            }
            System.out.println("\nServidor " + this.routingTable.getId() + " cerrado");
            ss.close();
            routingTable.showRoutes();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void run() {
        this.startServer();
    }
}