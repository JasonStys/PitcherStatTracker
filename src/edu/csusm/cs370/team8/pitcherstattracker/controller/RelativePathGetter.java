package edu.csusm.cs370.team8.pitcherstattracker.controller;

import edu.csusm.cs370.team8.pitcherstattracker.MainWindow;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;

// Needed for .jar file version. from https://stackoverflow.com/a/6849255
public class RelativePathGetter {
    public static String getPath() throws IOException {
        String path = MainWindow.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        path = (new File(path)).getParentFile().getPath();
        path = path + "\\data";
        Files.createDirectories(Paths.get(path));
        String decodedPath = URLDecoder.decode(path, "UTF-8");
        //System.out.println("decodedPath: " + decodedPath);
        return decodedPath;
    }
}
