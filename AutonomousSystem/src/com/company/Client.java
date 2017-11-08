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

    public  Client(RoutingTable routingTable, int port, String host, String AS) throws IOException {
        this.routingTable = routingTable;
        this.port = port;
        this.hostAddress= host;
        this.AS = AS;
        this.PORT = port;
        this.HOST = host;
    }

    public void startClient(){
        try {
            this.initConnection("client",this.port,this.hostAddress);
            outServer = new DataOutputStream(cs.getOutputStream());
            outServer.flush();
            BufferedReader input = new BufferedReader(new InputStreamReader(cs.getInputStream()));
            while (true) {
                Thread.sleep(30000);
                outServer.writeUTF(this.routingTable.getUpdatePackage(this.AS) + "\n");
                serverMessage = input.readLine();
                System.out.println(serverMessage);
                // Guarda el mapeo
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
