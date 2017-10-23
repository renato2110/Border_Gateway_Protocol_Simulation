package com.company;

import java.io.IOException;


public class Main {

    public static void main(String[] args) throws IOException {

        // Controller controller = new Controller();


        RoutingTable routingTable = new RoutingTable("AS9");
        routingTable.receiveUpdate("AS1*192.168.0.0:AS1-AS2-AS3,10.0.0.0:AS1-AS3,192.168.0.0:AS2-AS8,192.168.0.0:AS2-AS8,10.0.0.0:A4-AS6");
        routingTable.showRoutes();
        System.out.println("\n" + routingTable.getUpdatePackage("AS2"));

        /*
        Server server = new Server("AS1");
        System.out.println("Iniciando servidor\n");
        server.startServer();*/

        /*Client client = new Client();
        System.out.println("Iniciando cliente\n");
        client.startClient();*/
    }
}
