package com.company;

import java.io.*;
import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Created by Vladimir & Renato on 22/10/2017.
 */
public class Controller{

    private List<Server> listeningServers;
    private RoutingTable routingTable;
    private HashMap<String, String> servers;
    private List<Client> clients;
    private Logbook logbook;

    public Controller(String path) throws IOException {
        this.servers = new HashMap<>();
        this.clients = new ArrayList<>();
        this.logbook = new Logbook();
        this.readInputFile(path);
    }

    private void readInputFile(String path) {

        try {
            FileReader fileReader = new FileReader(path);
            BufferedReader input = new BufferedReader(fileReader);
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
                            if(stringTokenizer.hasMoreTokens()){
                                ip = stringTokenizer.nextToken();
                                if(stringTokenizer.hasMoreTokens()){
                                    port = stringTokenizer.nextToken();
                                    servers.put(ip, port);
                                    newClient = new Client(this.routingTable, Integer.parseInt(port),ip, this.logbook);
                                    this.clients.add(newClient);
                                }
                            }
                        }
                        break;
                    case 3:
                            this.listeningServers = new ArrayList<Server>();
                            while ((reader = input.readLine()) != null) {
                                this.listeningServers.add(new Server(this.routingTable, Integer.parseInt(reader), this.logbook));
                            }
                            break;
                    default:
                        break;
                }
            }

            this.startServers();
            this.startClients();

        } catch (FileNotFoundException e) {
            System.out.println("Archivo no valido");
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchElementException e){
            e.printStackTrace();
        }
    }

    public String showRoutes(){
        return this.routingTable.showRoutes();
    }

    public void addSubnet(String subnet) throws IOException {
        this.logbook.writeInLogbook("Added the subnet: " + subnet + "\r\n");
        this.routingTable.addSubnet(subnet);
    }

    public void startServers(){
        for(Server s : listeningServers){
            (new Thread(s)).start();
        }
    }

    public void stopServers(){
        this.routingTable.resetRoutingTable();
        for(Server s : listeningServers){
            s.stop();
        }
        servers.clear();
    }

    public void startClients(){
        for(Client c : this.clients){
            (new Thread(c)).start();
        }
    }

    public void stopClients(){
        for(Client c : this.clients){
            c.stop();
        }
    }

    public void openLogbook() throws IOException {
        this.logbook.openLogbook();
    }
}