package me.Brick.TurtleCamera.AutoEncoder.Events;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.Brick.TurtleCamera.AutoEncoder.Auto_Encoder;
import me.Brick.TurtleCamera.AutoEncoder.Main;

public class PlayerChatHandler implements Listener {
	
	Main plugin;
	
	public PlayerChatHandler(Main plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
    	if(plugin.data.pluginEnabled) {
    		for(String message : filterString(event.getMessage())) {
        		plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
        			@Override
        			public void run() {
                		Auto_Encoder.readSentence(message);
                		plugin.data.sendMessageToListeners(ChatColor.AQUA + "Input: " + Auto_Encoder.info.inputString);
                		plugin.data.sendMessageToListeners(ChatColor.AQUA + "Output: " + Auto_Encoder.info.outputString);
                		plugin.data.sendMessageToListeners(ChatColor.AQUA + "Cost: " + Auto_Encoder.info.cost);
                		plugin.data.sendMessageToListeners(ChatColor.AQUA + "Accuracy: " + (Auto_Encoder.info.numCorrect/Auto_Encoder.MAX_SENTENCE_LENGTH));
                		plugin.data.config.writeInfo(Auto_Encoder.info);
        			}
        		});
    		}
    	}
    }
	
	public ArrayList<String> filterString(String string) {		
		ArrayList<String> filtered = new ArrayList<String>();
		
		String filter = string.toLowerCase().replaceAll("[^(a-z)(0-9) ,\\.\\?\\!\\'\\(\\)]", "");

		while(filter.length() > Auto_Encoder.MAX_SENTENCE_LENGTH) {
			filtered.add(filter.substring(0, Auto_Encoder.MAX_SENTENCE_LENGTH));
			filter = filter.substring(Auto_Encoder.MAX_SENTENCE_LENGTH);
		}
		
		for(int x=0; x<filtered.size(); x++) {
			if(filtered.get(x).length() < Auto_Encoder.MAX_SENTENCE_LENGTH) {
				String temp = filtered.get(x);
				
				while(temp.length() < Auto_Encoder.MAX_SENTENCE_LENGTH) {
					temp = temp + " ";
				}
	
				filtered.set(x, temp);
			}
		}
		
		return filtered;
	}
}
