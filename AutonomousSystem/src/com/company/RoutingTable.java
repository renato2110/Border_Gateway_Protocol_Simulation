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

    public void addSubnet(String subnet) {
        routes.put(subnet, this.id); // Agrega una ruta directamente conectada
    }

    public void deleteSubnet(String subnet){
        // Recalcular la vara, porque ya no puede existir aquella ruta que contenga la subnet eliminada
    }

    public void resetRoutingTable(){
        routes = new HashMap<>(); // Cuando se apaga el enrutador y se deben borrar las rutas conocidas
    }

    public void receiveUpdate(String packet) {
        StringTokenizer tokensPacket = new StringTokenizer(packet, "*");  // Tokeniza el paquete
        tokensPacket.nextToken(); // Quién es el que envía el paquete
        String transmitterRoutes = tokensPacket.nextToken(); // Guarda todas las rutas separadas por ","
        StringTokenizer tokensTransmitterRoutes = new StringTokenizer(transmitterRoutes, ",");  // Tokeniza las rutas separadas por ","
        while (tokensTransmitterRoutes.hasMoreTokens()) {  // Manda a agregar cada ruta a la "tabla de enrutamiento"
            String updatedRoute = tokensTransmitterRoutes.nextToken();
            this.updateRoute(updatedRoute);
        }
    }

    public void sendUpdates() {
        String packet = this.id+"*";  // Inicio del mensaje
        int i = 1; // contador usado para saber cuándo se llega al final del HashMap y no agregar la ","
        for (Map.Entry<String, String> entry : routes.entrySet()) {
            packet += entry.getKey()+":"+entry.getValue(); // Arma UNA ruta, ejemplo: 192.168.0.0:id-AS1-AS"
            if(i<routes.size()){  // Si no llega al final del HashMap, le agrega una ",", para agregar otra ruta
                packet += ",";
                i++;
            }
        }
        // Uso del socket
    }

    public void updateRoute(String route) {
        StringTokenizer tokensRoute = new StringTokenizer(route, ":"); // Tokeniza la ruta
        String subnet = tokensRoute.nextToken(); // Guarda la subred de la ruta, ejemplo: 192.168.0.0
        String path = this.id + "-" + tokensRoute.nextToken(); // Guarda el camino para llegar a la subred con el propio "id", ejemplo: id-AS1-AS2-AS3
        routes.put(subnet, path);
    }

    public void showRoutes() {
        // Imprimir cada ruta del hashMap
    }

}
