package org.gruppe2.network;

import java.util.UUID;

public class TableEntry {
	private final UUID uuid;
	private final String name;
	private final int currentPlayers;
	private final int maxPlayers;
	
	public TableEntry(UUID uuid, String name, int currentPlayers, int maxPlayers){
		this.uuid = uuid;
		this.name = name;
		this.currentPlayers = currentPlayers;
		this.maxPlayers = maxPlayers;
		
	}
	
	public UUID getUUID(){
		return uuid;
	}

	public String getName() {
		return name;
	}

	public int getMaxPlayers(){
		return maxPlayers;
	}

	public int getCurrentPlayers(){
		return currentPlayers;
	}
}
