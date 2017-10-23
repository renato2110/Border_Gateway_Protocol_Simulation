package com.company;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * Created by Renato on 22/10/2017.
 */
public class Controller {

    private Server server;
    private RoutingTable routingTable;
    private HashMap<String, String> neighbors;

    public Controller() throws IOException {
        this.neighbors = new HashMap<>();
        readInputFile();
        this.startServer();
    }

    private void readInputFile() {
        Scanner scanner = new Scanner(System.in);
        System.out.printf("Ingrese la ruta del archivo: ");
        String file = scanner.nextLine();
        scanner.close();

        try {
            BufferedReader input = new BufferedReader(new FileReader(file));
            input.readLine();
            String reader;
            for (int line = 0; line < 4; line++) {
                switch (line) {
                    case 0:
                        reader = input.readLine();
                        this.routingTable = new RoutingTable(reader);
                        break;
                    case 1:
                        while (!((reader = input.readLine()).contains("#"))) {
                            this.routingTable.addSubnet(reader);
                        }
                        break;
                    case 2:
                        while (!((reader = input.readLine()).contains("#"))) {
                            StringTokenizer stringTokenizer = new StringTokenizer(reader, ":");
                            String ip = stringTokenizer.nextToken();
                            String port = stringTokenizer.nextToken();
                            neighbors.put(ip, port);
                        }
                        break;
                    case 3:
                            reader = input.readLine();
                            this.server = new Server(this.routingTable, Integer.parseInt(reader), "localhost");
                        break;
                    default:
                        break;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Archivo no valido");
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showRoutes(){
        this.routingTable.showRoutes();
    }

    public void startServer(){
        this.server.startServer();
    }

    public void stopServer(){
        this.routingTable.resetRoutingTable();
        neighbors.clear();
    }


}