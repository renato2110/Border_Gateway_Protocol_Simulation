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
        this.path = ".\\AutonomousSystem\\Logbook.txt";
        this.file = new File(path);
        this.startFile();
    }

    private void startFile() throws IOException {
        FileWriter fileWriter = new FileWriter(file);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault());
        Date actual = new Date();
        String moment = simpleDateFormat.format(actual);

        bufferedWriter.write("Date and time of execution: " + moment + "\r\n"+ "\r\n");
        bufferedWriter.close();
    }

    public synchronized void writeInLogbook(String writing) throws IOException {
        FileWriter fileWriter = new FileWriter(file, true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(writing);
        bufferedWriter.newLine();
        bufferedWriter.close();
    }

    public synchronized void openLogbook() throws IOException {
        Runtime.getRuntime().exec("cmd /c start .\\AutonomousSystem\\Logbook.txt");
    }
}
