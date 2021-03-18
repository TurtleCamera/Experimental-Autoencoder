//Auto-Encoder Project

package me.Brick.TurtleCamera.AutoEncoder;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import me.Brick.TurtleCamera.AutoEncoder.Commands.AutoEncoderCommand;
import me.Brick.TurtleCamera.AutoEncoder.Data.DataHolder;
import me.Brick.TurtleCamera.AutoEncoder.Events.PlayerChatHandler;


public class Main extends JavaPlugin {
	
	public DataHolder data;
	
	public void onEnable() {
		
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();
		
		data = new DataHolder(this);
		
		data.config.loadData();
		
		new PlayerChatHandler(this);
		
		AutoEncoderCommand encodercommand = new AutoEncoderCommand(this);
		getCommand("autoencoder").setExecutor(encodercommand);
		getCommand("autoencoder").setTabCompleter(encodercommand);
		
		Bukkit.getLogger().info("[" + this.getDescription().getName() + "] v" + this.getDescription().getVersion() + " has been enabled.");
	}
	
	public void onDisable() {
		data.config.writeAllData();
		Bukkit.getLogger().info("[" + this.getDescription().getName() + "] " + this.getDescription().getVersion() + " has been disabled.");
	} 
	
}



