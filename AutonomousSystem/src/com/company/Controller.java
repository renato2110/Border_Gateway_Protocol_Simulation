package com.company;

import java.io.*;
import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Created by Renato on 22/10/2017.
 */
public class Controller implements Runnable {

    private List<Server> listeningServers;
    private RoutingTable routingTable;
    private HashMap<String, String> servers;
    private List<Client> clients;
    private String file;
    static Semaphore readFileLock = new Semaphore(1);

    public Controller() throws IOException {
        try {
            readFileLock.acquire();
            Scanner scanner = new Scanner(System.in);
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            System.out.flush();
            System.out.printf("Ingrese la ruta del archivo: ");
            //this.file = scanner.nextLine();
            this.file = in.readLine();
            /*while (scanner.hasNextLine()){
                System.out.println("hola");
            }*/

            //file = "/home/vladimir/Escritorio/input.txt";
            in.mark(0);
            in.reset();

            readFileLock.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void readInputFile() {

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
                            if(stringTokenizer.hasMoreTokens()){
                                ip = stringTokenizer.nextToken();

                                if(stringTokenizer.hasMoreTokens()){
                                    port = stringTokenizer.nextToken();
                                    servers.put(ip, port);
                                    newClient = new Client(this.routingTable, Integer.parseInt(port),ip, "AS2");
                                    System.out.println(newClient.getHOST() +":"+ newClient.getPORT());

                                    this.clients.add(newClient);
                                }
                            }
                        }
                        break;
                    case 3:
                            this.listeningServers = new ArrayList<Server>();
                            while ((reader = input.readLine()) != null) {
                                this.listeningServers.add(new Server(this.routingTable, Integer.parseInt(reader)));
                            }
                            break;
                    default:
                        break;
                }
            }

            this.startServers();
            for(Client c : this.clients){
                (new Thread(c)).start();
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

    public void startServers(){
        for(Server s : listeningServers){
            (new Thread(s)).start();
        }
    }

    public void stopServer(){
        this.routingTable.resetRoutingTable();
        servers.clear();
    }


    @Override
    public void run() {
        this.servers = new HashMap<>();
        this.clients = new ArrayList<>();
        this.readInputFile();
    }
}