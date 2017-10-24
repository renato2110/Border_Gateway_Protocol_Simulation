package com.company;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by Renato on 22/10/2017.
 */
public class Controller {

    private Server server;
    private RoutingTable routingTable;
    private HashMap<String, String> servers;
    private List<Client> clients;

    public Controller() throws IOException {
        this.servers = new HashMap<>();
        this.clients = new ArrayList<>();
        this.readInputFile();
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
                        input.readLine();
                        break;
                    case 1:
                        while (!((reader = input.readLine()).contains("#"))) {
                            this.routingTable.addSubnet(reader);

                        }
                        break;
                    case 2:
                        String ip;
                        String port;
                        StringTokenizer stringTokenizer;
                        Client newClient;
                        while (!((reader = input.readLine()).contains("#"))) {
                            stringTokenizer = new StringTokenizer(reader, ":");
                            ip = stringTokenizer.nextToken();
                            port = stringTokenizer.nextToken();
                            servers.put(ip, port);
                            newClient = new Client(this.routingTable, Integer.parseInt(port),ip, "AS2");
                            this.clients.add(newClient);
                            (new Thread(newClient)).start();
                        }
                        break;
                    case 3:
                            reader = input.readLine();
                            this.server = new Server(this.routingTable, Integer.parseInt(reader));
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
        } catch (NoSuchElementException e){
            e.printStackTrace();
        }
    }

    public void showRoutes(){
        this.routingTable.showRoutes();
    }

    public void startServer(){
        (new Thread(this.server)).start();
    }

    public void stopServer(){
        this.routingTable.resetRoutingTable();
        servers.clear();
    }


}