package com.hardlight.auxiliary;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public class ScreenNavigation {
    public static final String PATH_TO_SCREENS = "../screens/";
    public static final String SCREEN_END = "Screen";
    public static final String EXTENSION = ".fxml";
    private static final HashMap<AllScreens, Pane> pages = initPages();

    private static HashMap<AllScreens, Pane> initPages() {
        HashMap<AllScreens, Pane> pages = new HashMap<>();
        System.out.println(Arrays.toString(AllScreens.values()));
        Arrays.stream(AllScreens.values())
                .forEach(screen -> {
                    try {
                        String paneName = getPaneName(screen);
                        Pane pane = FXMLLoader.load(ScreenNavigation.class.getResource(paneName));
                        pages.put(screen, pane);
                    } catch (IOException ignore) {
                    }
                });
        return pages;
    }

    private static String getPaneName(AllScreens screen) {
        String screenName = screen.toString() + SCREEN_END;
        return PATH_TO_SCREENS + screenName + EXTENSION;
    }

    public static Pane getPage(AllScreens screen) {
        return pages.get(screen);
    }
}
