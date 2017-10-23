package com.company;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MultiHashtable;

import java.util.*;

/**
 * Created by Renato & Vladimir on 21/10/2017.
 */
public class RoutingTable {
    private String id;
    private HashMap<String, ArrayList<String>> routes;

    public RoutingTable(String id) {
        this.id = id;
        this.routes = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public void addSubnet(String subnet) {
        ArrayList<String> subnetRoutes = new ArrayList<>();
        subnetRoutes.add(id);
        routes.put(subnet, subnetRoutes); // Agrega una ruta directamente conectada
    }

    public void deleteSubnet(String subnet, String AS) {
        this.routes.remove(subnet);
        for (Map.Entry<String, ArrayList<String>> entry : this.routes.entrySet()) {
            for (int i = 0; i < entry.getValue().size(); i++) {
                if ((entry.getValue().get(i)).contains(AS)) {
                    entry.getValue().remove(entry.getValue().get(i));
                }
            }
        }
    }

    public void resetRoutingTable() {
        this.routes.clear(); // Cuando se apaga el enrutador y se deben borrar las rutas conocidas
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

    public String getUpdatePackage() { // Uso del socket, forman el String para enviar
        String packet = this.id + "*";
        int i = 1; // Utilizado para saber cuando se dejan de agrega ","
        for (Map.Entry<String, ArrayList<String>> entry : this.routes.entrySet()) { // Recorre todas las subredes conocidas
            packet += entry.getKey() + ":"; // Agrega cada vez la una subred y ":", ejemplo packet+"192.168.0.0:"
            int minimum = this.getMinimumRouteSize(entry.getValue()); // Define el tamaño para la ruta menor
            for (int j = 0; j < entry.getValue().size(); j++) { //Recorre las rutas conocidas hasta encontrar la menor
                if (minimum == this.getRouteSize(entry.getValue().get(j))) {
                    packet += entry.getValue().get(j);
                    break;
                }
            }
            if (i < this.routes.size()) { // Si no ha llegado al final, sigue agregando ","
                packet += ",";
            }
            i++;
        }
        return packet;
    }

    public void updateRoute(String route) {
        StringTokenizer tokensRoute = new StringTokenizer(route, ":"); // Tokeniza la ruta
        String subnet = tokensRoute.nextToken(); // Guarda la subred de la ruta, ejemplo: 192.168.0.0
        String path = this.id + "-" + tokensRoute.nextToken(); // Guarda el camino para llegar a la subred con el propio "id", ejemplo: id-AS1-AS2-AS3
        if (this.routes.containsKey(subnet)) {  // Si conoce al menos una ruta para la subred
            if (!this.routes.get(subnet).contains(path)) { // Si ya conoce la ruta para la subred
                this.routes.get(subnet).add(path);
            }
        } else { // Si no conoce la subred, crea un nuevo ArrayList con su ruta
            ArrayList<String> subnetRoutes = new ArrayList<>();
            subnetRoutes.add(path);
            this.routes.put(subnet, subnetRoutes);
        }
    }

    public void showRoutes() {
        System.out.println("\nRutas conocidas por " + this.id + ":\n");
        for (Map.Entry<String, ArrayList<String>> entry : this.routes.entrySet()) {
            int minimum = this.getMinimumRouteSize(entry.getValue());
            for (int i = 0; i < entry.getValue().size(); i++) {
                if (minimum == this.getRouteSize(entry.getValue().get(i))) {
                    System.out.printf("*");
                }
                System.out.println("RED " + entry.getKey() + ": " + entry.getValue().get(i));
            }
        }
    }

    public int getMinimumRouteSize(ArrayList<String> paths) {
        int minimum = 20;
        for (int i = 0; i < paths.size(); i++) {
            String path = paths.get(i);
            StringTokenizer pathTokenizer = new StringTokenizer(path, "-");
            if (minimum > pathTokenizer.countTokens()) {
                minimum = pathTokenizer.countTokens();
            }
        }
        return minimum;
    }

    public int getRouteSize(String path) {
        StringTokenizer pathTokenizer = new StringTokenizer(path, "-");
        return pathTokenizer.countTokens();
    }

}
