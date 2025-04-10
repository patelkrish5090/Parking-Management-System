package com.smartparking;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class IOhandler {

    public void logTime(String license, boolean entry){
        if(entry){
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter("Logs.txt",true));
                writer.write("\nEntry "+license+" "+java.time.LocalDateTime.now().toString());
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter("Logs.txt",true));
                writer.write("\nExit "+license+" "+java.time.LocalDateTime.now().toString());
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
