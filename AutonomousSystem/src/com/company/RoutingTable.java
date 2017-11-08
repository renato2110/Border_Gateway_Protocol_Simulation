package com.company;


import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Created by Renato & Vladimir on 21/10/2017.
 */
public class RoutingTable {
    static Semaphore semaphore = new Semaphore(1);
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
        subnetRoutes.add("");
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
        try {
            semaphore.acquire();
            try {

                StringTokenizer tokensPacket = new StringTokenizer(packet, "*");  // Tokeniza el paquete
                tokensPacket.nextToken(); // Quién es el que envía el paquete
                String transmitterRoutes = tokensPacket.nextToken(); // Guarda todas las rutas separadas por ","
                StringTokenizer tokensTransmitterRoutes = new StringTokenizer(transmitterRoutes, ",");  // Tokeniza las rutas separadas por ","
                while (tokensTransmitterRoutes.hasMoreTokens()) {  // Manda a agregar cada ruta a la "tabla de enrutamiento"
                    String updatedRoute = tokensTransmitterRoutes.nextToken();
                    this.updateRoute(updatedRoute);
                }

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error in receiveUpdate");
            }finally {
                semaphore.release();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public String getUpdatePackage(String neighbor) { // Uso del socket, forman el String para enviar
        String packet = this.id + "*";
        int i = 1; // Utilizado para saber cuando se dejan de agrega ","
        for (Map.Entry<String, ArrayList<String>> entry : this.routes.entrySet()) { // Recorre todas las subredes conocidas
            String temporalPacket = entry.getKey() + ":" + id; // Agrega cada vez la una subred y ":", ejemplo packet+"192.168.0.0:"
            int minimum = this.getMinimumRouteSize(entry.getValue()); // Define el tamaño para la ruta menor
            for (int j = 0; j < entry.getValue().size(); j++) { //Recorre las rutas conocidas hasta encontrar la menor
                if (minimum == j) {
                    if (entry.getValue().get(j).equals("")) {
                        packet += temporalPacket;
                    }
                    else {
                        String route = entry.getValue().get(j);
                        StringTokenizer stringTokenizer = new StringTokenizer(route, "-");
                        String routeTest = stringTokenizer.nextToken();
                        if (routeTest.equals(neighbor)) {
                            if (packet.endsWith(",")) {
                                packet = packet.substring(0, packet.length() - 1);
                            }
                            i++;
                        } else {
                            temporalPacket += "-" + route;
                            packet += temporalPacket;
                        }
                        break;
                    }
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
        if (tokensRoute.hasMoreTokens()) {
            String subnet = tokensRoute.nextToken(); // Guarda la subred de la ruta, ejemplo: 192.168.0.0
            if (tokensRoute.hasMoreTokens()) {
                String path = tokensRoute.nextToken(); // Guarda el camino para llegar a la subred con el propio "id", ejemplo: id-AS1-AS2-AS3
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

        }
    }

    public void showRoutes() {
        System.out.println("\nRutas conocidas por " + this.id + ":\n");
        for (Map.Entry<String, ArrayList<String>> entry : this.routes.entrySet()) {
            int minimum = this.getMinimumRouteSize(entry.getValue());
            for (int i = 0; i < entry.getValue().size(); i++) {
                if (minimum == i) {
                    System.out.printf("*");
                }
                System.out.printf("RED " + entry.getKey() + ": ");
                if (entry.getValue().get(i).equals("")) {
                    System.out.println("DIRECTAMENTE CONECTADO");
                }
                System.out.println(entry.getValue().get(i));
            }
        }
    }

    private int getMinimumRouteSize(ArrayList<String> paths) {
        String path = paths.get(0);
        StringTokenizer pathTokenizer = new StringTokenizer(path, "-");
        int value = pathTokenizer.countTokens();
        int minimum = 0;
        for (int i = 1; i < paths.size(); i++) {
            path = paths.get(i);
            pathTokenizer = new StringTokenizer(path, "-");
            if (value > pathTokenizer.countTokens()) {
                value = pathTokenizer.countTokens();
                minimum = i;
            }
        }
        return minimum;
    }
}
