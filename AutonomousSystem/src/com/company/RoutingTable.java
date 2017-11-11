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

    public synchronized void addSubnet(String subnet) {
        if (!routes.containsKey(subnet)) {
            ArrayList<String> subnetRoutes = new ArrayList<>();
            subnetRoutes.add("");
            routes.put(subnet, subnetRoutes); // Agrega una ruta directamente conectada
        }
    }

    public synchronized void cleanASRoutes(String AS) {
        for (Map.Entry<String, ArrayList<String>> entry : this.routes.entrySet()) {
            for (int i = 0; i < entry.getValue().size(); i++) {
                if ((entry.getValue().get(i)).startsWith(AS)) {
                    entry.getValue().remove(entry.getValue().get(i));
                }
                if (entry.getValue().isEmpty()){
                    routes.remove(entry.getKey());
                }
            }
        }
    }

    public synchronized void deleteAS(String AS){
        for (Map.Entry<String, ArrayList<String>> entry : this.routes.entrySet()) {
            for (int i = 0; i < entry.getValue().size(); i++) {
                if ((entry.getValue().get(i)).contains(AS)) {
                    entry.getValue().remove(entry.getValue().get(i));
                }
                if (entry.getValue().isEmpty()){
                    routes.remove(entry.getKey());
                }
            }
        }
    }

    public synchronized void resetRoutingTable() {
        this.routes.clear(); // Cuando se apaga el enrutador y se deben borrar las rutas conocidas
    }

    public synchronized void receiveUpdate(String packet) {
            try {

                StringTokenizer tokensPacket = new StringTokenizer(packet, "*");  // Tokeniza el paquete
                String AS = tokensPacket.nextToken(); // Quién es el que envía el paquete
                this.cleanASRoutes(AS);
                if (tokensPacket.hasMoreTokens()) {
                    String transmitterRoutes = tokensPacket.nextToken(); // Guarda todas las rutas separadas por ","
                    StringTokenizer tokensTransmitterRoutes = new StringTokenizer(transmitterRoutes, ",");  // Tokeniza las rutas separadas por ","
                    String updatedRoute;
                    while (tokensTransmitterRoutes.hasMoreTokens()) {  // Manda a agregar cada ruta a la "tabla de enrutamiento"
                        updatedRoute = tokensTransmitterRoutes.nextToken();
                        if(!updatedRoute.contains(this.id)){
                            this.updateRoute(updatedRoute);
                        }

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error in receiveUpdate");
            }
    }

    public synchronized String getUpdatePackage(String neighbor) { // Uso del socket, forman el String para enviar
        String packet = this.id + "*";
        int i = 1; // Utilizado para saber cuando se dejan de agrega ","
        for (Map.Entry<String, ArrayList<String>> entry : this.routes.entrySet()) { // Recorre todas las subredes conocidas

                String temporalPacket = entry.getKey() + ":" + id; // Agrega cada vez la una subred y ":", ejemplo packet+"192.168.0.0:"
                int minimum = this.getMinimumRouteSize(entry.getValue()); // Define el tamaño para la ruta menor
                for (int j = 0; j < entry.getValue().size(); j++) { //Recorre las rutas conocidas hasta encontrar la menor
                    if (minimum == j) {
                        if (entry.getValue().get(j).equals("")) {
                            packet += temporalPacket + ",";
                        }
                        else {
                            String route = entry.getValue().get(j);
                            if (!route.contains(neighbor)) {
                                System.out.println("RENATOOO :3");
                                temporalPacket += "-" + route;
                                packet += temporalPacket + ",";
                            }
                            break;
                        }
                    }
                }

            }
        if (packet.endsWith(",")) {
            packet = packet.substring(0, packet.length() - 1);
        }

        return packet;
    }

    public synchronized void updateRoute(String route) {
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

    public synchronized void showRoutes() {
        System.out.println("\nKnowledge routes by " + this.id + ":\n");
        for (Map.Entry<String, ArrayList<String>> entry : this.routes.entrySet()) {
            int minimum = this.getMinimumRouteSize(entry.getValue());
            for (int i = 0; i < entry.getValue().size(); i++) {
                if (minimum == i) {
                    System.out.printf("*");
                }
                System.out.printf("SUBNET " + entry.getKey() + ": ");
                if (entry.getValue().get(i).equals("")) {
                    System.out.println("DIRECTLY CONNECTED");
                }else{
                    System.out.println(entry.getValue().get(i));
                }

            }
        }
    }

    private synchronized int getMinimumRouteSize(ArrayList<String> paths) {
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
