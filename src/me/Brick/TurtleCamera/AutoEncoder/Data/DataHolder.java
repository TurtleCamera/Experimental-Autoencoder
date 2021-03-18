package me.Brick.TurtleCamera.AutoEncoder.Data;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.entity.Player;

import me.Brick.TurtleCamera.AutoEncoder.Main;

public class DataHolder {
	
	Main plugin;
	
	public ConfigHandler config;
	
	public ArrayList<UUID> listeners;
	
	public boolean pluginEnabled = true;

	public DataHolder(Main plugin) {
		this.plugin = plugin;
		
		config = new ConfigHandler(plugin);
		
		listeners = new ArrayList<UUID>();
	}
	
	public void sendMessageToListeners(String message) {
		for(UUID uuid : listeners) {
			Player player = plugin.getServer().getPlayer(uuid);
			
			if(player != null) {
				player.sendMessage(message);
			}
		}
	}

}
