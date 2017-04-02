package org.gruppe2.game.event;

import java.util.UUID;

public class ChatEvent implements Event {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4927330537383925492L;
	private final String message;
	private final UUID playerUUID;
	
	public ChatEvent(String message, UUID playerUUID){
		this.message = message;
		this.playerUUID = playerUUID;
	}
	
	public String getMessage(){
		return message;
	}
	public UUID getPlayerUUID(){
		return playerUUID;
	}
}
