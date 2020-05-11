package com.hardlight.controllers;

import com.hardlight.auxiliary.AllScreens;
import com.hardlight.auxiliary.ScreenNavigation;
import com.hardlight.components.ActionButton;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class EntryController implements Initializable {
    @FXML
    private ActionButton btnPvPGame;
    @FXML
    private AnchorPane anchorRoot;
    @FXML
    private StackPane parentContainer;

    public void onPvPGameClicked() {
        Parent pane = ScreenNavigation.getPage(AllScreens.SingleSettings);

        if (pane == null)
            return;

        Scene scene = btnPvPGame.getScene();

        pane.translateXProperty().set(scene.getWidth());
        parentContainer.getChildren().add(pane);

        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(pane.translateXProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);
        timeline.getKeyFrames().add(kf);
        timeline.setOnFinished(event ->
                parentContainer.getChildren().remove(anchorRoot));

        timeline.play();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
