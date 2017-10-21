package com.company;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by Renato & Vladimir on 21/10/2017.
 */
public class RoutingTable {

    private String id;
    private HashMap<String, String> routes;

    public RoutingTable(String id) {
        this.id = id;
        this.routes = new HashMap<>();
    }

    public void addSubnet(String route) {
        StringTokenizer tokensRoute = new StringTokenizer(route, ":");
        String subnet = tokensRoute.nextToken();
        String path = this.id + "-" + tokensRoute.nextToken();

        routes.put(subnet, path);
    }

    public void receiveUpdate(String packet) {
        StringTokenizer tokensPacket = new StringTokenizer(packet, "*");
        tokensPacket.nextToken();
        String transmitterRoutes = tokensPacket.nextToken();
        StringTokenizer tokensTransmitterRoutes = new StringTokenizer(transmitterRoutes, ",");
        while (tokensTransmitterRoutes.hasMoreTokens()) {
            String updatedRoute = tokensTransmitterRoutes.nextToken();
            this.addSubnet(updatedRoute);
        }
    }

    public void sendUpdates() {
        String packet = this.id+"*";
        int i = 1;
        for (Map.Entry<String, String> entry : routes.entrySet()) {
            packet += entry.getKey()+":"+entry.getValue();
            if(i<routes.size()){
                packet += ",";
                i++;
            }
        }
        // Uso del socket
    }

    public void updateRoute(String route) {
        // pregunta si una ruta es mejor, y actualiza
    }

    public void showRoutes() {
        // Imprimir cada ruta del hashMap
    }
}
