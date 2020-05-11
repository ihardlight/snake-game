package com.hardlight;

import com.hardlight.auxiliary.AllScreens;
import com.hardlight.auxiliary.ScreenNavigation;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.InputStream;

public class SnakeGameApplication extends Application {
    final public String SNAKE_ICON = "../../resources/snake-icon.jpg";
    final public String GAME_TITLE = "Snake Game";
    final public int WINDOW_WIDTH = 600;
    final public int WINDOW_HEIGHT = 600;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Pane root = ScreenNavigation.getPage(AllScreens.Entry);
        InputStream iconStream = getClass().getResourceAsStream(SNAKE_ICON);
        Image image = new Image(iconStream);
        primaryStage.getIcons().add(image);

        primaryStage.setTitle(GAME_TITLE);
        primaryStage.setScene(new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT));
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
