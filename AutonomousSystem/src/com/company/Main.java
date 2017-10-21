package com.company;

import java.util.StringTokenizer;

public class Main {

    public static void main(String[] args) {

        RoutingTable routingTable = new RoutingTable("AS9");
        routingTable.receiveUpdate("AS1*192.168.0.0:AS1-AS2-AS3,10.0.0.0:AS1-AS3");
        routingTable.sendUpdates();

    }
}
