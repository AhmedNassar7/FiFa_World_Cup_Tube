package app;

import client.MainFrame;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Main {
    public static void deserializeAll() {
        File folder = new File("temp");
        File[] listOfFiles = folder.listFiles();
        assert listOfFiles != null;
        for (File file : listOfFiles) {
            if (file.getName().endsWith(".ser")) {
                try {
                    FileInputStream fileIn = new FileInputStream(file);
                    ObjectInputStream in = new ObjectInputStream(fileIn);
                    Video video = (Video) in.readObject();
                    Video.add(video.getFile(), video.getCover(), video.title, video.description, video.getTags());
                    in.close();
                    fileIn.close();
                } catch (IOException i) {
                    i.printStackTrace();
                    return;
                } catch (ClassNotFoundException c) {
                    System.out.println("Video class not found");
                    c.printStackTrace();
                    return;
                }
            }
        }
    }

    public static void main(String[] args) {
        new File("temp/").mkdirs();
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }
        deserializeAll();
        new MainFrame();

    }
}