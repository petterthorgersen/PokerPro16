package org.gruppe2.game.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.gruppe2.Resources;
import org.gruppe2.ai.NewDumbAI;
import org.gruppe2.game.Player;
import org.gruppe2.game.event.PlayerJoinEvent;
import org.gruppe2.game.event.PlayerLeaveEvent;
import org.gruppe2.game.helper.GameHelper;
import org.gruppe2.game.model.GameModel;
import org.gruppe2.game.session.Helper;
import org.gruppe2.game.session.Message;

public class GameController extends AbstractController {
    @Helper
    private GameHelper game;

    private List<Integer> availableTablePositions = new ArrayList<>();

    @Override
    public void init() {
        for (int i = 0; i < game.getModel().getMaxPlayers(); i++) {
            availableTablePositions.add(i);
        }
    }

    @Override
    public void update() {
        if (game.getModel().getBotPolicy() == GameModel.BotPolicy.FILL) {
            if (game.getPlayers().stream().allMatch(Player::isBot))
                return;

            if (game.getPlayers().size() < game.getModel().getMaxPlayers()) {
                addBot();
            }
        }
    }

    @Message
    public boolean addBot() {
        Random random = new Random();
        String[] avatars = Resources.listAvatars();
        String avatar = avatars[random.nextInt(avatars.length)];

        return addPlayerModel(UUID.randomUUID(), NewDumbAI.randomName(), avatar, true);
    }

    @Message
    public boolean addPlayer(UUID uuid, String name, String avatar) {
        return addPlayerModel(uuid, name, avatar, false);
    }

    @Message
    public void kickPlayer(UUID uuid) {
        @SuppressWarnings("unused")
		List<Player> players = null;

        synchronized (players = game.getPlayers()) {
            Optional<Player> player = game.findPlayerByUUID(uuid);

            if (!player.isPresent())
                return;

            game.getPlayers().remove(player.get());
            availableTablePositions.add(player.get().getTablePosition());

            addEvent(new PlayerLeaveEvent(player.get()));
        }
    }

    private boolean addPlayerModel(UUID uuid, String name, String avatar, boolean isBot) {
        List<Player> players;

        synchronized (players = game.getPlayers()) {
            if (players.size() >= game.getModel().getMaxPlayers()) {
                Optional<Player> findBot = players.stream()
                        .filter(Player::isBot)
                        .findFirst();

                if (findBot.isPresent()) {
                    kickPlayer(findBot.get().getUUID());
                } else {
                    return false;
                }
            }

            int pos = availableTablePositions.remove(0);
            Player player = new Player(uuid, renameDuplicate(name), avatar, isBot, pos);

            players.add(player);

            if (game.canStart()) {
                getContext().message("roundStart");
            }

            player.setBank(game.getBuyIn());

            addEvent(new PlayerJoinEvent(player));

            return true;
        }
    }

    private String renameDuplicate(String name) {
        int maxDuplicate = 0;
        Pattern pattern = Pattern.compile(Pattern.quote(name) + "(?: \\((\\d+)\\))?");

        for (Player p : game.getPlayers()) {
            Matcher matcher = pattern.matcher(p.getName());

            if (matcher.find()) {
                String group = matcher.group(1);
                int asInteger = 1;

                if (group != null)
                    asInteger = Integer.parseInt(group);

                maxDuplicate = Math.max(maxDuplicate, asInteger);
            }
        }

        if (maxDuplicate > 0) {
            name = String.format("%s (%d)", name, maxDuplicate + 1);
        }

        return name;
    }
}
