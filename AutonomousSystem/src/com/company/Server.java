package com.company;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Renato & Vladimir on 21/10/2017.
 */

public class Server extends Connection {
    private RoutingTable routingTable;

    public Server(RoutingTable routingTable, int PORT, String HOST) throws IOException {
        super("server", PORT, HOST);
        this.routingTable = routingTable;
    }

    public void startServer() {
        try {
            System.out.println("\nServidor " + this.routingTable.getId() + " esperando...");
            cs = ss.accept();
            System.out.println("Cliente conectado en el servidor "+this.routingTable.getId());
            outClient = new DataOutputStream(cs.getOutputStream());
            outClient.writeUTF(routingTable.getUpdatePackage(" ")); // CAMBIAAAAAAAAAAAAR
            BufferedReader input = new BufferedReader(new InputStreamReader(cs.getInputStream()));

            while (!((serverMessage = input.readLine()).equals("stop"))) {
                System.out.println(serverMessage);
                routingTable.receiveUpdate(serverMessage);
            }
            System.out.println("\nServidor " + this.routingTable.getId() + " cerrado");
            ss.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}