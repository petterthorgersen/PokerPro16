package org.gruppe2.game.controller;

import java.util.UUID;

import org.gruppe2.game.event.ChatEvent;
import org.gruppe2.game.session.Message;

public class ChatController extends AbstractController {

	@Message
	public void chat(String message, UUID playerUUID) {
		addEvent(new ChatEvent(message, playerUUID));
	}

}
