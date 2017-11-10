package com.company;

import java.io.*;

/**
 * Created by Renato & Vladimir on 21/10/2017.
 */
public class Client extends Connection implements Runnable{
    private RoutingTable routingTable;
    private int port;
    private String hostAddress;
    private String AS;


    public  Client(RoutingTable routingTable, int port, String host) throws IOException {
        this.routingTable = routingTable;
        this.port = port;
        this.hostAddress= host;
        this.PORT = port;
        this.HOST = host;
    }

    public void startClient(){
        try {
            this.initConnection("client",this.port,this.hostAddress);
            if(this.cs!=null && !this.cs.isClosed()){
                cs.getOutputStream().flush();
                this.outServer = new DataOutputStream(cs.getOutputStream());
                this.outServer.flush();
                DataInputStream input = new DataInputStream(cs.getInputStream());
                while (this.active) {
                    Thread.sleep(3000);
                    System.out.println("Enviando al servidor: " + this.routingTable.getUpdatePackage(this.AS));
                    this.outServer.writeUTF(this.routingTable.getUpdatePackage(this.AS) + "\n");
                    this.outServer.flush();
                    this.serverMessage = input.readUTF();
                    if (this.serverMessage == null) {
                        System.out.println("Lost connection");
                    }else {
                        System.out.println("Server message: " + this.serverMessage);
                    }
                    // System.out.println(serverMessage);
                    // serverMessage = input.readLine();
                    //System.out.println(serverMessage);
                    // Guarda el mapeo
                }
            }
            //cs.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void run() {
        this.startClient();
    }

}
