package org.gruppe2.ui.javafx.menu;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import org.gruppe2.Main;
import org.gruppe2.Resources;
import org.gruppe2.ui.UIResources;
import org.gruppe2.ui.javafx.Modal;
import org.gruppe2.ui.javafx.PokerApplication;

public class Settings extends VBox {
	private static Modal modal = null;

	@FXML
	TextField nameField;

	@FXML
	TilePane avatarTiles;

	private Pane highlitedPane;

	public Settings(){
		UIResources.loadFXML(this);
		if(Main.getProperty("name") != null)
		nameField.setText(Main.getProperty("name"));
		getAvatars();
	}

	public static void show(boolean canClose) {
		modal = new Modal(canClose);
		modal.setPercentSize(0.5, 0.5);
		modal.setContent(new Settings());
		modal.setTitle("Settings: Set name and avatar");
		modal.show();
	}

	public void apply(){
		if(nameField.getText() != null && !nameField.getText().isEmpty())
		Main.setProperty("name" ,nameField.getText());
		modal.close();
	}

	private void getAvatars(){
		String[] avatars = Resources.listAvatars();

		for(String avatar: avatars){
			Pane avatarPane = new Pane();
			ImageView imageView = new ImageView(UIResources.getAvatar(avatar));
			imageView.preserveRatioProperty().setValue(true);
			imageView.fitWidthProperty().bind(PokerApplication.getRoot().widthProperty().multiply(0.05));

			if(avatar.equals(Main.getProperty("avatar"))) {
				avatarPane.setStyle("-fx-effect: dropshadow(gaussian, #0099ff, 5, 2, 0, 0);");
				highlitedPane = avatarPane;
			}

			avatarPane.setOnMouseClicked(frisk -> {Main.setProperty("avatar",avatar);
				flagTile(avatarPane);
			});

			avatarPane.getChildren().add(imageView);
			avatarTiles.getChildren().add(avatarPane);

		}
	}

	private void flagTile(Pane pane){
		if(highlitedPane!= null) {
			highlitedPane.setStyle("");
		}
		pane.setStyle("-fx-effect: dropshadow(gaussian, #0099ff, 5, 2, 0, 0);");
		highlitedPane = pane;
	}
}
