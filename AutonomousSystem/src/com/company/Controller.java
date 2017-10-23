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
        this.initRouter();
        this.neighbors = new HashMap<>();
        readInputFile();
        //this.routingTable.showRoutes();
        this.server.startServer();
    }

    private void initRouter(){
        Scanner scanner = new Scanner(System.in);
        System.out.printf("Ingrese el nombre del Enrutador: ");
        String name = scanner.nextLine();
        this.routingTable = new RoutingTable("AS2");
    }

    private void readInputFile() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese la ruta del archivo");
        String file = scanner.nextLine();
        scanner.close();

        try {
            BufferedReader input = new BufferedReader(new FileReader(file));
            input.readLine();
            String reader;
            for (int line = 0; line < 3; line++) {
                switch (line) {
                    case 0:
                        while (!((reader = input.readLine()).contains("#"))) {
                            this.routingTable.addSubnet(reader);
                        }
                        break;
                    case 1:
                        while (!((reader = input.readLine()).contains("#"))) {
                            StringTokenizer stringTokenizer = new StringTokenizer(reader, ":");
                            String ip = stringTokenizer.nextToken();
                            String port = stringTokenizer.nextToken();
                            neighbors.put(ip, port);
                        }
                        break;
                    case 2:
                        while ((reader = input.readLine()) != null) {
                            this.server = new Server(this.routingTable, Integer.parseInt(reader), "localhost");
                        }
                        break;
                    default:
                        break;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Archivo no valido");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
