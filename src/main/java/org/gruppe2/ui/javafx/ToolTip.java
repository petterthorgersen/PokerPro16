package org.gruppe2.ui.javafx;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

import org.gruppe2.ui.UIResources;

/**
 * Created by Petter on 26/04/2016.
 */
public class ToolTip extends Pane {
    public ToolTip(Node node){
        UIResources.loadFXML(this);
        getChildren().add(node);
    }
    @FXML
    public void close() {
        PokerApplication.getRoot().getChildren().remove(this);
    }
}
