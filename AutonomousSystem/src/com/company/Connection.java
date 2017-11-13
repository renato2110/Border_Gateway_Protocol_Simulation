package com.company;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;

/**
 * Created by Renato & Vladimir on 21/10/2017.
 */
public abstract class Connection {
    protected int PORT;
    protected String HOST;
    protected String serverMessage;
    protected ServerSocket ss;
    protected Socket cs;
    protected DataOutputStream outServer, outClient;
    protected Logbook logbook;
    protected String connectedAS;
    protected boolean active;
    protected boolean turnOff;

    public Connection(){
        this.active = true;
    }

    public void initConnection(String tipo, int PORT, String HOST) throws IOException{
        this.PORT = PORT;
        this.HOST = HOST;
        this.active = true;
        this.turnOff = false;
        if (tipo.equalsIgnoreCase("server")) {

            ss = new ServerSocket(this.PORT);
            cs = new Socket();
        }
        else {
            boolean connected = false;

            while (!connected && active){
                try {
                    InetAddress inetAddress =InetAddress.getByName(HOST);
                    cs = new Socket(inetAddress,PORT);

                }catch (ConnectException e){
                    this.logbook.writeInLogbook("There is no connection to server: " + HOST + ":" + PORT);
                    //System.out.println("There is no connection to server: " + HOST + ":" + PORT);
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException s) {
                        s.printStackTrace();
                    }
                }
                if(cs != null){
                    connected = true;
                }
            }

        }
    }

    public void stop(){
        try {
            this.active = false;
            this.turnOff = true;
            if(cs!=null && !cs.isClosed()){
                cs.close();
            }
            if(ss != null && !ss.isClosed()){
                ss.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getPORT() {
        return PORT;
    }

    public void setPORT(int PORT) {
        this.PORT = PORT;
    }

    public String getHOST() {
        return HOST;
    }

    public void setHOST(String HOST) {
        this.HOST = HOST;
    }
}
