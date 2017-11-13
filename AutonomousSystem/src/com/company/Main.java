package com.company;

import com.gui.MenuInterface;

import java.io.*;


public class Main {

    public static void main(String[] args) throws IOException {

        //new Menu().showMenu();

        MenuInterface menuInterface = new MenuInterface();
        menuInterface.startInterface();
        /*
        RoutingTable routingTable = new RoutingTable("AS9");
        routingTable.receiveUpdate("AS1*192.168.0.0:AS1-AS2-AS3-AS8,10.0.0.0:AS1-AS3,10.0.0.0:AS4-AS6");
        routingTable.showRoutes();
        routingTable.receiveUpdate("AS1*192.168.0.0:AS1-AS2-AS3,11.0.0.0:AS1-AS3,172.168.0.0:AS2-AS8,172.168.0.0:AS2-AS8,11.0.0.0:AS4-AS6");
        routingTable.showRoutes();
        System.out.println("\n" + routingTable.getUpdatePackage("AS8"));
        */


    }
}
