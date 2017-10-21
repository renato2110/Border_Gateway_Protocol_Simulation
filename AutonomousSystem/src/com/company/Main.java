package com.company;

import java.io.IOException;
import java.util.StringTokenizer;

public class Main {

    public static void main(String[] args) throws IOException {

        RoutingTable routingTable = new RoutingTable("AS9");
        routingTable.receiveUpdate("AS1*192.168.0.0:AS1-AS2-AS3,10.0.0.0:AS1-AS3");
        routingTable.showRoutes();

        /*Server server = new Server();
        System.out.println("Iniciando servidor\n");
        server.startServer();

        Client client = new Client();
        System.out.println("Iniciando cliente\n");
        client.startClient();  */
    }
}
