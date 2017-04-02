/**
 * Setting up the stage, and default start scene
 * May implement alt start...
 */

package org.gruppe2.ui.javafx;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import org.gruppe2.Main;
import org.gruppe2.ui.javafx.ingame.Game;
import org.gruppe2.ui.javafx.ingame.GameScene;
import org.gruppe2.ui.javafx.menu.Intro;

public class PokerApplication extends Application {
    private final static double width = 1280;
    private final static double height = 768;
    private final static double fontSize = 14.0;
    private static StackPane root = new StackPane(); // Setting global root. Will only change scenes.
    public boolean introFinished = false;
    
    private static PokerApplication application;

    private ObjectProperty<Font> bigFont = new SimpleObjectProperty<>();
    private ObjectProperty<Font> smallFont = new SimpleObjectProperty<>();
    private DoubleProperty widthScale = new SimpleDoubleProperty();
    private DoubleProperty heightScale = new SimpleDoubleProperty();
    private DoubleProperty scale = new SimpleDoubleProperty();

    /**
     * Controllers will need to get current root to change scenes
     *
     * @return root
     */
    public static StackPane getRoot() {
        return root;
    }

    public double getScale() {
        return scale.get();
    }

    public DoubleProperty scaleProperty() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale.set(scale);
    }

    public Font getBigFont() {
        return bigFont.get();
    }

    public ObjectProperty<Font> bigFontProperty() {
        return bigFont;
    }

    public void setBigFont(Font bigFont) {
        this.bigFont.set(bigFont);
    }

    public static double getHeight() {
        return height;
    }

    public static PokerApplication getApplication() {
        return application;
    }

    @Override
    public void start(Stage stage) throws Exception {
        application = this;

        root.widthProperty().addListener((observable, oldVal, newVal) -> {
            onSizeChange(newVal.doubleValue(), root.getHeight());
            widthScale.set(newVal.doubleValue() / width);
        });

        root.heightProperty().addListener((observable, oldVal, newVal) -> {
            onSizeChange(root.getWidth(), newVal.doubleValue());
            heightScale.set(newVal.doubleValue() / height);
        });

        scale.addListener((o, oldVal, newVal) -> {
            bigFont.setValue(Font.font(newVal.doubleValue() * fontSize * 1.2));
            smallFont.setValue(Font.font(newVal.doubleValue()*fontSize));
        });

        startValues(stage);
        setStartScene(stage);
    }

    private void startValues(Stage stage) {
        stage.setTitle("PokerPro16");
        stage.getIcons().add(new Image("/images/ui/icon.png"));
        stage.setOnCloseRequest(e -> System.exit(1));
    }

    /**
     * Set up scene and stage Starts the intro No global stylesheet in javaFX 8
     * stage, only on every scene
     */
    private void setStartScene(Stage stage) {
        if (Main.isAutostart()) {
            Game.autostart();
            Game.getInstance().join();
            root.getChildren().add(new GameScene());
        } else {
            root.getChildren().add(new Intro());
        }

        Scene scene = new Scene(root, width, height, false, SceneAntialiasing.BALANCED);
        scene.getStylesheets().add(
                getClass().getResource("/css/style.css").toExternalForm());
        stage.setMinWidth(640);
        stage.setMinHeight(480);
        stage.setScene(scene);
        stage.show();
        setKeyListener(scene);
    }
    
    public static void launch(String []args) {
        Application.launch(PokerApplication.class, args);
    }
    
    private void setKeyListener(Scene scene) {
//    	scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
//			
//
//			@Override
//            public void handle(KeyEvent event) {
//            	System.out.println("key pressed application");
//            	if(!introFinished){
//            		System.out.println("switchin to main menu");
//            		introFinished = true;
//            		SceneController.setScene(new MainMenu());
//            		
//            	}
//            	else if(gameStarted && game != null)
//            		game.gameKeyOptions(event);
//            }
//        });
	}


    private void onSizeChange(double width, double height) {
        double ratio = (double) PokerApplication.width / PokerApplication.height;

        if (width / ratio < height) {
            scale.set(width / PokerApplication.width);
        } else {
            scale.set(height / PokerApplication.height);
        }
    }

    public double getWidthScale() {
        return widthScale.get();
    }

    public DoubleProperty widthScaleProperty() {
        return widthScale;
    }

    public double getHeightScale() {
        return heightScale.get();
    }

    public DoubleProperty heightScaleProperty() {
        return heightScale;
    }

    public Font getSmallFont() {
        return smallFont.get();
    }

    public ObjectProperty<Font> smallFontProperty() {
        return smallFont;
    }

    public void setSmallFont(Font smallFont) {
        this.smallFont.set(smallFont);
    }
}
