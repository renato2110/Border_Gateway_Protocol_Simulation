package com.company;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;

/**
 * Created by Renato & Vladimir on 21/10/2017.
 */
public class Connection {
    private int PORT;
    private String HOST;
    protected String serverMessage;
    protected ServerSocket ss;
    protected Socket cs;
    protected DataOutputStream outServer, outClient;

    public void initConnection(String tipo, int PORT, String HOST) throws IOException{
        this.PORT = PORT;
        this.HOST = HOST;
        if (tipo.equalsIgnoreCase("server")) {
            ss = new ServerSocket(this.PORT);
            cs = new Socket();
        }
        else {
            //InetAddress inetAddress = new Inet4Address(HOST);
            boolean connected = false;

            while (!connected){
                try {
                    InetAddress inetAddress =InetAddress.getByName(HOST);
                    cs = new Socket(inetAddress,PORT);

                }catch (ConnectException e){
                    System.out.println("No hay conexión al servidor: " + HOST + ":" + PORT);
                }
                if(cs != null){
                    connected = true;
                }

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //System.out.println("HOST: " + HOST + " PORT: " + PORT);
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
