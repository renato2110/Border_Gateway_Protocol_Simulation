package com.company;

import java.io.IOException;


public class Main {

    public static void main(String[] args) throws IOException {

        //Menu menu = new Menu();
        new Menu().showMenu();
        //Controller controller = new Controller("/home/vladimir/Escritorio/input2.txt");

        /*System.out.println(System.getProperty("user.dir"));
        System.out.println(System.getProperty("os.name"));*/

        /*RoutingTable routingTable = new RoutingTable("AS9");
        routingTable.receiveUpdate("AS1*192.168.0.0:AS1-AS2-AS3,10.0.0.0:AS1-AS3,192.168.0.0:AS2-AS8,192.168.0.0:AS2-AS8,10.0.0.0:AS4-AS6");
        //routingTable.showRoutes();
        //System.out.println("\n" + routingTable.getUpdatePackage("AS9"));




        Server server = new Server(routingTable,5555);
        System.out.println("Iniciando servidor\n");
        (new Thread(server)).start();
        //server.startServer();



        Client client = new Client(5555,"localhost");
        System.out.println("Iniciando cliente\n");
        (new Thread(client)).start();
        //client.startClient();
        */
    }
}
