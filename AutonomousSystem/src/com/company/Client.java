package com.company;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Renato & Vladimir on 21/10/2017.
 */
public class Client extends Connection implements Runnable{
    private RoutingTable routingTable;
    private int port;
    private String hostname;
    private String AS;

    public  Client(RoutingTable routingTable, int port, String host, String AS) throws IOException {
        this.routingTable = routingTable;
        this.port = port;
        this.hostname = host;
        this.AS = AS;
    }

    public void startClient(){
        try {
            this.initConnection("client",this.port,this.hostname);
            outServer = new DataOutputStream(cs.getOutputStream());
            for (int i = 0; i < 2; i++) {
                outServer.flush();
                outServer.writeUTF(this.routingTable.getUpdatePackage(AS)+"\n");
            }
            cs.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        this.startClient();
    }

}
