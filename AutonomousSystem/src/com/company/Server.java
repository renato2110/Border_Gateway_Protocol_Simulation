package com.company;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Renato & Vladimir on 21/10/2017.
 */

public class Server extends Connection {

    public Server() throws IOException {
        super("server");
    }

    public void startServer() {
        try {
            System.out.println("Esperando...");
            cs = ss.accept();
            System.out.println("Cliente en línea");
            outClient = new DataOutputStream(cs.getOutputStream());
            outClient.writeUTF("Petición recibida y aceptada");
            BufferedReader input = new BufferedReader(new InputStreamReader(cs.getInputStream()));

            while ((serverMessage = input.readLine()) != null) {
                System.out.println(serverMessage);
            }
            System.out.println("Fin de la conexión");
            ss.close();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}