package org.gruppe2.ui.javafx;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;

/**
 * Created by Petter on 26/04/2016.
 * Modal window that only partialy covers the window, and has the possibility to be closed on certain events
 */

public class Modal {
    private Pane parent;
    private BorderPane borderPane;

    private Label title;
    private Button closeButton;
    
    public Modal(boolean canClose) {
    	
        parent = new Pane();
        borderPane = new BorderPane();

        HBox titleBar = new HBox(5);
        titleBar.setAlignment(Pos.CENTER_RIGHT);

        StackPane titlePane = new StackPane();
        title = new Label("Modal Window");
        title.fontProperty().bind(PokerApplication.getApplication().smallFontProperty());
        titlePane.getChildren().add(title);
        titlePane.setAlignment(Pos.CENTER);
        HBox.setHgrow(titlePane, Priority.ALWAYS);

        ImageView closeImage = new ImageView(getClass().getResource("/images/ui/folded.png").toExternalForm());
        closeImage.setPreserveRatio(true);
        closeImage.fitHeightProperty().bind(PokerApplication.getApplication().widthScaleProperty().multiply(18));
        titleBar.getChildren().add(titlePane);
        
        if(canClose){
	        closeButton = new Button();
	        closeButton.setGraphic(closeImage);
	        closeButton.setOnAction(this::onCloseButtonAction);
	        HBox.setHgrow(closeButton, Priority.NEVER);
	        titleBar.getChildren().add(closeButton);
        }

       
        

        borderPane.setTop(titleBar);
        borderPane.setStyle("-fx-background-color: black; -fx-background-radius: 15px");
        borderPane.layoutXProperty().bind(parent.widthProperty().divide(2).subtract(borderPane.widthProperty().divide(2)));
        borderPane.layoutYProperty().bind(parent.heightProperty().divide(2).subtract(borderPane.heightProperty().divide(2)));

        parent.setOnKeyPressed(this::onKeyPressed);
        parent.setStyle("-fx-background-color: radial-gradient(center 50% 50%, radius 150%, rgba(0,0,0,0) 15%,rgba(0,0,0,1) 50%)");
        parent.getChildren().add(borderPane);
    }
   

    public void setPercentSize(double x, double y) {
        borderPane.prefWidthProperty().bind(parent.widthProperty().multiply(x));
        borderPane.prefHeightProperty().bind(parent.heightProperty().multiply(y));
    }

    public void setTitle(String text) {
        title.setText(text);
    }

    public void setContent(Node node) {
        borderPane.setCenter(node);
    }

    public void show() {
        PokerApplication.getRoot().getChildren().add(parent);
    }

    public void close() {
        PokerApplication.getRoot().getChildren().remove(parent);
    }

    private void onCloseButtonAction(ActionEvent actionEvent) {
        close();
    }

    public void onKeyPressed(KeyEvent event) {
        System.out.println(event);
        if (event.getCode() == KeyCode.ESCAPE)
            close();
    }

    public static void messageBox(String title, String message, boolean canClose) {
        Modal modal = new Modal(canClose);
        modal.setPercentSize(0.3, 0.1);
        modal.setTitle(title);
        modal.setContent(new Label(message));
        modal.show();
    }
}
