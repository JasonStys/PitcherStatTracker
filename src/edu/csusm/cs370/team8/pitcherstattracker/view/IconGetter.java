package edu.csusm.cs370.team8.pitcherstattracker.view;

import javax.swing.ImageIcon;
import java.net.URL;
import java.util.Objects;

public class IconGetter {
    private static final String path = "/toolbarButtonGraphics/";

    public static final ImageIcon BACK = new ImageIcon(
            IconGetter.class.getResource(path + "navigation/Back24.gif"));
    public static final ImageIcon FORWARD = new ImageIcon(
            IconGetter.class.getResource(path + "navigation/Forward24.gif"));
    public static final ImageIcon DOWN = new ImageIcon(
            IconGetter.class.getResource(path + "navigation/Down24.gif"));
    public static final ImageIcon UP = new ImageIcon(
            IconGetter.class.getResource(path + "navigation/Up24.gif"));
    public static final ImageIcon HOME = new ImageIcon(
            IconGetter.class.getResource(path + "navigation/Home24.gif"));

    public static final ImageIcon SAVE = new ImageIcon(
            IconGetter.class.getResource(path + "general/Save24.gif"));
    public static final ImageIcon DELETE = new ImageIcon(
            IconGetter.class.getResource(path + "general/Delete24.gif"));
    public static final ImageIcon ADD = new ImageIcon(
            IconGetter.class.getResource(path + "general/Add24.gif"));
    public static final ImageIcon EDIT = new ImageIcon(
            IconGetter.class.getResource(path + "general/Edit24.gif"));
    public static final ImageIcon CUT = new ImageIcon(
            IconGetter.class.getResource(path + "general/Cut24.gif"));

    public static final ImageIcon VIEW = new ImageIcon(
            IconGetter.class.getResource(path + "general/Find24.gif"));
    public static final ImageIcon WORLD = new ImageIcon(
            IconGetter.class.getResource(path + "general/Search24.gif"));
    public static final ImageIcon LIST = new ImageIcon(
            IconGetter.class.getResource(path + "general/History24.gif"));
    public static final ImageIcon STOP = new ImageIcon(
            IconGetter.class.getResource(path + "general/Stop24.gif"));

    private static final ClassLoader loader = IconGetter.class.getClassLoader();
    private static final URL url = loader.getResource("edu/csusm/cs370/team8/pitcherstattracker/img/sticky.png");
    public static final ImageIcon STICKY = new ImageIcon(url);


}
