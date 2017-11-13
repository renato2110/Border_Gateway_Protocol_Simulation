package com.company;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Renato & Vladimir on 11/11/2017.
 */
public class Logbook {

    private static String path;
    private static File file;

    public Logbook() throws IOException {
        if(System.getProperty("os.name").toLowerCase().contains("windows")) {
            this.path = ".\\AutonomousSystem\\Logbook.txt";
        }else
        {
            this.path = "./AutonomousSystem/Logbook.txt";
        }
        this.file = new File(path);
        this.startFile();
    }

    private String getMoment(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault());
        Date actual = new Date();
        return simpleDateFormat.format(actual);
    }

    private void startFile() throws IOException {
        FileWriter fileWriter;
        if(this.file.exists()){
            fileWriter = new FileWriter(file,true);
        }else {
            fileWriter = new FileWriter(file);
        }
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        String moment = this.getMoment();

        bufferedWriter.write("\nDate and time of execution: " + moment + ".\r\n"+ "\r\n");
        bufferedWriter.close();
    }

    public synchronized void writeInLogbook(String writing) throws IOException {
        FileWriter fileWriter = new FileWriter(file, true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(this.getMoment() + " : " + writing);
        bufferedWriter.newLine();
        bufferedWriter.close();
    }

    public synchronized void openLogbook() throws IOException {
        if(System.getProperty("os.name").toLowerCase().contains("windows")){
            Runtime.getRuntime().exec("cmd /c start .\\AutonomousSystem\\Logbook.txt");
        }else {

            String[] args = new String[] {"xterm", "-e", "/usr/bin/tail -f ./AutonomousSystem/Logbook.txt"};
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(args);
        }

    }
}