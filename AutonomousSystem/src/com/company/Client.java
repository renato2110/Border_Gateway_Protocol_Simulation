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
            outServer.writeUTF(this.routingTable.getUpdatePackage(this.AS)+"\n");
            //cs.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void run() {
        this.startClient();
    }

}
