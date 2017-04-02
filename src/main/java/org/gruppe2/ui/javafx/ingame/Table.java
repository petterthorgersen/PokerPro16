package org.gruppe2.ui.javafx.ingame;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

import org.gruppe2.game.Player;
import org.gruppe2.game.event.PlayerJoinEvent;
import org.gruppe2.game.event.PlayerLeaveEvent;
import org.gruppe2.game.event.PlayerPostActionEvent;
import org.gruppe2.game.helper.GameHelper;
import org.gruppe2.game.helper.RoundHelper;
import org.gruppe2.game.session.Handler;
import org.gruppe2.game.session.Helper;

/**
 * The ingame table used to show players, cards, pot etc.
 * @author htj063
 *
 */
public class Table extends Pane {
    @Helper
    private GameHelper game;
    @Helper
    private RoundHelper round;

    private final double ratio = 2464.0 / 1368.0;
    private final double logicalWidth = 308;
    private final double logicalHeight = 171;
    private final double tableScale = 0.7;

    private ObjectProperty<Font> font = new SimpleObjectProperty<>();

    private DoubleProperty scale = new SimpleDoubleProperty();

    private DoubleProperty fitWidth = new SimpleDoubleProperty();
    private DoubleProperty fitHeight = new SimpleDoubleProperty();

    private List<PlayerInfoBox> players = new ArrayList<>();
    private List<PlayerCards> playerCards = new ArrayList<>();
    private Label pot = new Label();

    public Table() {
        Game.setAnnotated(this);

        fitWidth.addListener((o, oldVal, newVal) -> {
            double width = newVal.doubleValue();
            double height = fitHeight.get();

            if (width / ratio < height) {
                setMaxWidth(width);
                setMaxHeight(width / ratio);
            } else {
                setMaxWidth(height * ratio);
                setMaxHeight(height);
            }
        });

        fitHeight.addListener((o, oldVal, newVal) -> {
            double width = fitWidth.get();
            double height = newVal.doubleValue();

            if (height * ratio < width) {
                setMaxWidth(height * ratio);
                setMaxHeight(height);
            } else {
                setMaxWidth(width);
                setMaxHeight(width / ratio);
            }
        });

        scale.addListener((o, oldVal, newVal) -> font.setValue(Font.font(newVal.doubleValue() * 4.0)));

        scale.bind(maxWidthProperty().divide(logicalWidth));

        ImageView tableImage = new ImageView("/images/ui/PokertableWithLogo.png");
        tableImage.fitWidthProperty().bind(maxWidthProperty().multiply(tableScale));
        tableImage.fitHeightProperty().bind(maxHeightProperty().multiply(tableScale));
        tableImage.layoutXProperty().bind(translateX(tableImage.fitWidthProperty(), 0));
        tableImage.layoutYProperty().bind(translateY(tableImage.fitHeightProperty(), 0));
        getChildren().add(tableImage);

        createPlayerInfoBoxes();

        CommunityCards communityCards = new CommunityCards();
        communityCards.maxWidthProperty().bind(scale.multiply(80));
        communityCards.maxHeightProperty().bind(scale.multiply(80));
        communityCards.layoutXProperty().bind(translateX(communityCards.widthProperty(), 0));
        communityCards.layoutYProperty().bind(translateY(communityCards.heightProperty(), -15));
        getChildren().add(communityCards);

        pot.setText("Total pot: $0");
        pot.fontProperty().bind(font);
        pot.layoutXProperty().bind(translateX(pot.widthProperty(), 0));
        pot.layoutYProperty().bind(translateY(pot.heightProperty(), 50));
        getChildren().add(pot);
    }

    @Handler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (event.getPlayer().getUUID().equals(Game.getPlayerUUID())) {
            createPlayerInfoBoxes();
        }

        int i = getPlayerIndex(event.getPlayer().getTablePosition());

        if (i < 0)
            return;

        players.get(i).setPlayerUUID(event.getPlayer().getUUID());
        playerCards.get(i).setPlayerUUID(event.getPlayer().getUUID());
    }

    @Handler
    public void onPlayerLeave(PlayerLeaveEvent event) {
        if (event.getPlayer().getUUID().equals(Game.getPlayerUUID())) {
            createPlayerInfoBoxes();
        }

        int i = getPlayerIndex(event.getPlayer().getTablePosition());

        if (i < 0)
            return;

        players.get(i).setPlayerUUID(null);
        playerCards.get(i).setPlayerUUID(null);
    }

    @Handler
    public void onPostAction(PlayerPostActionEvent event) {
        pot.setText("Total pot: $" + round.getPot());
    }

    public double getFitWidth() {
        return fitWidth.get();
    }

    public DoubleProperty fitWidthProperty() {
        return fitWidth;
    }

    public void setFitWidth(double fitWidth) {
        this.fitWidth.set(fitWidth);
    }

    public double getFitHeight() {
        return fitHeight.get();
    }

    public DoubleProperty fitHeightProperty() {
        return fitHeight;
    }

    public void setFitHeight(double fitHeight) {
        this.fitHeight.set(fitHeight);
    }

    private int getPlayerIndex(int tablePosition) {
        Optional<Player> player = game.findPlayerByUUID(Game.getPlayerUUID());

        if (!player.isPresent())
            return tablePosition;

        if (tablePosition == player.get().getTablePosition())
            return -1;

        int offset = game.getModel().getMaxPlayers() - player.get().getTablePosition() - 1;
        if (tablePosition >= player.get().getTablePosition())
            tablePosition--;

        return (tablePosition + offset) % (game.getModel().getMaxPlayers() - 1);
    }

    private void createPlayerInfoBoxes() {
        Optional<Player> player = game.findPlayerByUUID(Game.getPlayerUUID());

        players.forEach(p -> getChildren().remove(p));
        players.clear();

        playerCards.forEach(p -> getChildren().remove(p));
        playerCards.clear();

        int n = game.getModel().getMaxPlayers();

        if (player.isPresent())
            n--;

        for (int i = 0; i < n; i++) {
            PlayerInfoBox p = new PlayerInfoBox();
            p.fontProperty().bind(font);
            p.maxWidthProperty().bind(scale.multiply(45));
            p.maxHeightProperty().bind(scale.multiply(15));
            setPositionAroundTable(p, p.widthProperty(), p.heightProperty(), (i + 1.0) / (n + 1.0), 1.2);
            getChildren().add(p);
            players.add(p);

            PlayerCards c = new PlayerCards();
            c.maxHeightProperty().bind(scale.multiply(20));
            c.maxWidthProperty().bind(scale.multiply(20));
            double angle = setPositionAroundTable(c, c.widthProperty(), c.heightProperty(), (i + 1.0) / (n + 1.0), 0.6);
            c.setRotate(angle * 180.0 / Math.PI + 180);
            getChildren().add(c);
            playerCards.add(c);
        }

        game.getPlayers().forEach(p -> {
            int pos = getPlayerIndex(p.getTablePosition());

            if (pos < 0)
                return;

            players.get(pos).setPlayerUUID(p.getUUID());
            playerCards.get(pos).setPlayerUUID(p.getUUID());
        });
    }

    private DoubleBinding translateX(ReadOnlyDoubleProperty width, double x) {
        return scale.multiply(tableScale * x).add(widthProperty().divide(2)).subtract(width.divide(2));
    }

    private DoubleBinding translateY(ReadOnlyDoubleProperty height, double y) {
        return scale.multiply(tableScale * y).add(heightProperty().divide(2)).subtract(height.divide(2));
    }

    private double setPositionAroundTable(Node node, ReadOnlyDoubleProperty width, ReadOnlyDoubleProperty height, double x, double dist) {
        final double rectWidth = (logicalWidth - logicalHeight);
        final double rectHeight = logicalHeight;
        final double theta = Math.PI;

        dist *= rectHeight / 2.0;

        double arcLength = theta * dist;
        double circumference = rectWidth + 2.0 * arcLength;

        double position = x * circumference;

        if (position < arcLength) {
            double angle = (position / arcLength) * theta + Math.PI / 2.0;

            node.layoutXProperty().bind(translateX(width, Math.cos(angle) * dist - rectWidth / 2.0));
            node.layoutYProperty().bind(translateY(height, Math.sin(angle) * dist));

            return angle + Math.PI / 2.0;
        } else if (position > rectWidth + arcLength) {
            double angle = (position - rectWidth) / arcLength * theta + Math.PI / 2.0;

            node.layoutXProperty().bind(translateX(width, Math.cos(angle) * dist + rectWidth / 2.0));
            node.layoutYProperty().bind(translateY(height, Math.sin(angle) * dist));

            return angle + Math.PI / 2.0;
        } else {
            double offsetX = position - arcLength;

            node.layoutXProperty().bind(translateX(width, offsetX - rectWidth / 2.0));
            node.layoutYProperty().bind(translateY(height, -dist));

            return 0.0;
        }
    }
}
