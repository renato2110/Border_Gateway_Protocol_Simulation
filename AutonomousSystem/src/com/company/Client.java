package com.company;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Renato & Vladimir on 21/10/2017.
 */
public class Client extends Connection implements Runnable{
    public  Client(int port, String host) throws IOException {
        super("client", port, host);
    }

    public void startClient(){
        try {
            outServer = new DataOutputStream(cs.getOutputStream());

            for (int i = 0; i < 2; i++) {
                outServer.flush();
                outServer.writeUTF("AS1*192.167.0.0:AS1-AS2-AS3,10.5.0.0:AS1-AS3,192.168.3.0:AS2-AS8,192.168.85.0:AS2-AS8,10.0.33.0:AS4-AS6\n");
            }
            cs.close();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void run() {
        this.startClient();
    }

}
