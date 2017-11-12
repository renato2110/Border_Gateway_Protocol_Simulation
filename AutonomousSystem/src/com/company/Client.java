package com.company;

import sun.rmi.runtime.Log;

import java.io.*;

/**
 * Created by Renato & Vladimir on 21/10/2017.
 */
public class Client extends Connection implements Runnable {
    private RoutingTable routingTable;
    private int port;
    private String hostAddress;


    public Client(RoutingTable routingTable, int port, String host, Logbook logbook) throws IOException {
        this.routingTable = routingTable;
        this.port = port;
        this.hostAddress = host;
        this.PORT = port;
        this.HOST = host;
        this.connectedAS = "";
        this.logbook = logbook;
    }

    public void startClient() {
        try {
            this.initConnection("client", this.port, this.hostAddress);
            if (this.cs != null && !this.cs.isClosed()) {
                this.outServer = new DataOutputStream(cs.getOutputStream());
                DataInputStream input = new DataInputStream(cs.getInputStream());
                String writing = "";
                while (this.active) {
                    writing = "Sending to the server " + this.connectedAS + ": " + this.routingTable.getUpdatePackage(this.connectedAS)+"\r\n";
                    //System.out.println(writing);
                    this.logbook.writeInLogbook(writing);
                    this.outServer.writeUTF(this.routingTable.getUpdatePackage(this.connectedAS));
                    this.serverMessage = input.readUTF();
                    if (this.serverMessage == null) {
                        this.logbook.writeInLogbook("Lost connection\r\n");
                        System.out.println("Lost connection");
                    } else {
                        writing = "Server message: " + this.serverMessage + "\r\n";
                        this.logbook.writeInLogbook(writing);
                        //System.out.println(writing);
                        this.connectedAS = serverMessage.split("\\*")[0];
                        this.routingTable.receiveUpdate(this.serverMessage);
                        writing = this.routingTable.showRoutes();
                        this.logbook.writeInLogbook(writing);
                        //System.out.println(writing);
                    }

                    Thread.sleep(10000);
                    // System.out.println(serverMessage);
                    // serverMessage = input.readLine();
                    //System.out.println(serverMessage);
                    // Guarda el mapeo
                }
            }
            //cs.close();
        } catch (Exception e) {
            this.stop(); //Mandar a la bitácora
            this.cs = null;
            this.outServer = null;
            this.startClient();

            //e.printStackTrace();
        }
    }


    @Override
    public void run() {
        this.startClient();
    }

}
