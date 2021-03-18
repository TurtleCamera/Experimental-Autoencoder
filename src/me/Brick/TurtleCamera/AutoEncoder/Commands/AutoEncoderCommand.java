package me.Brick.TurtleCamera.AutoEncoder.Commands;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.Brick.TurtleCamera.AutoEncoder.Auto_Encoder;
import me.Brick.TurtleCamera.AutoEncoder.Main;
import me.Brick.TurtleCamera.AutoEncoder.Data.Constants;

public class AutoEncoderCommand implements CommandExecutor, TabCompleter {
	
	Main plugin;
	
	public AutoEncoderCommand(Main plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

		if(sender.isOp() && cmd.getName().equalsIgnoreCase("autoencoder")) {
			
			if(args.length == 0) {
				sender.sendMessage(ChatColor.GOLD + plugin.getDescription().getName() + " v" + plugin.getDescription().getVersion() + " by TurtleCamera & Brick:");
				sender.sendMessage(ChatColor.GOLD + "/autoencoder info " + ChatColor.RED + "Display information page.");
				sender.sendMessage(ChatColor.GOLD + "/autoencoder listen [Player] " + ChatColor.RED + "Listen to output results.");
				sender.sendMessage(ChatColor.GOLD + "/autoencoder toggle_enabled " + ChatColor.RED + "Toggle plugin on/off.");
				sender.sendMessage(ChatColor.GOLD + "/autoencoder toggle_weight_restriction " + ChatColor.RED + "Toggle weight restriction.");
				sender.sendMessage(ChatColor.GOLD + "/autoencoder print_to_console " + ChatColor.RED + "Print all data to console.");
				sender.sendMessage(ChatColor.GOLD + "/autoencoder save " + ChatColor.RED + "Save all data.");
			}
			else if(args.length == 1 && args[0].equalsIgnoreCase("info")) {
				
				ArrayList<String> listening = new ArrayList<String>();
				
				for(UUID uuid : plugin.data.listeners) {
					listening.add(plugin.getServer().getOfflinePlayer(uuid).getName());
				}
				
				sender.sendMessage(ChatColor.AQUA + plugin.getDescription().getName() + " Information:");
				sender.sendMessage(ChatColor.AQUA + "Enabled: " + plugin.data.pluginEnabled);
				sender.sendMessage(ChatColor.AQUA + "Player(s) Listening: " + listening.toString());
				sender.sendMessage(ChatColor.AQUA + "Message Count: " + Auto_Encoder.count);
				sender.sendMessage(ChatColor.AQUA + "Weight Restriction: " + Auto_Encoder.toggleWeightRestriction);
			}
			else if(args.length == 1 && sender instanceof Player && args[0].equalsIgnoreCase("listen")) {
				
				Player player = (Player)sender;
				
				if(!plugin.data.listeners.contains(player.getUniqueId())) {
					plugin.data.listeners.add(player.getUniqueId());
					player.sendMessage(ChatColor.GREEN + "[" + plugin.getDescription().getName() + "] You are now listening to the output.");
				}
				else {
					plugin.data.listeners.remove(player.getUniqueId());
					player.sendMessage(ChatColor.RED + "[" + plugin.getDescription().getName() + "] You are no longer listening to the output.");
				}
			}
			else if(args.length == 2 && args[0].equalsIgnoreCase("listen")) {
				
				OfflinePlayer player = null;
				
				for(OfflinePlayer offlineplayer : plugin.getServer().getOfflinePlayers()) {
					if(args[1].equalsIgnoreCase(offlineplayer.getName())) {
						player = offlineplayer;
						break;
					}
				}
				
				if(player == null) {
					sender.sendMessage(ChatColor.RED + "[" + plugin.getDescription().getName() + "] Player does not exist, or has never joined the server.");
					return true;
				}
				
				if(!plugin.data.listeners.contains(player.getUniqueId())) {
					plugin.data.listeners.add(player.getUniqueId());
					sender.sendMessage(ChatColor.GREEN + "[" + plugin.getDescription().getName() + "] " + player.getName() + " is now listening to the output.");
				}
				else {
					plugin.data.listeners.remove(player.getUniqueId());
					sender.sendMessage(ChatColor.RED + "[" + plugin.getDescription().getName() + "] " + player.getName() + " is no longer listening to the output.");
				}
			}
			else if(args.length == 1 && args[0].equalsIgnoreCase("toggle_enabled")) {
				if(plugin.data.pluginEnabled) {
					sender.sendMessage(ChatColor.RED + "[" + plugin.getDescription().getName() + "] Disabled network training.");
				}
				else {
					sender.sendMessage(ChatColor.GREEN + "[" + plugin.getDescription().getName() + "] Enabled network training.");
				}
				plugin.data.pluginEnabled = !plugin.data.pluginEnabled;
			}
			else if(args.length == 1 && args[0].equalsIgnoreCase("toggle_weight_restriction")) {
				if(Auto_Encoder.toggleWeightRestriction) {
					sender.sendMessage(ChatColor.RED + "[" + plugin.getDescription().getName() + "] Set weight restriction to false.");
				}
				else {
					sender.sendMessage(ChatColor.GREEN + "[" + plugin.getDescription().getName() + "] Set weight restriction to true.");
				}
				Auto_Encoder.toggleWeightRestriction = !Auto_Encoder.toggleWeightRestriction;
			}
			else if(args.length == 1 && args[0].equalsIgnoreCase("print_to_console")) {
				sender.sendMessage(ChatColor.GOLD + "[" + plugin.getDescription().getName() + "] Printing all matrices & data to console.");
				plugin.data.config.printData();
			}
			else if(args.length == 1 && args[0].equalsIgnoreCase("save")) {
				plugin.data.config.writeAllData();
				sender.sendMessage(ChatColor.RED + "[" + plugin.getDescription().getName() + "] Saved all data.");
			}
			else {
				sender.sendMessage(ChatColor.RED + "Invalid syntax, type /autoencoder for a list of commands.");
			}

		}
		else {
			sender.sendMessage(ChatColor.RED + Constants.NO_PERMISSION_MESSAGE);
		}
			
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		
		List<String> result = new ArrayList<String>();
		
		if(sender.isOp() && command.getName().equalsIgnoreCase("autoencoder")) {
			
			if(args.length == 1) {
	            result.add("info");
	            result.add("listen");
	            result.add("toggle_enabled");
	            result.add("toggle_weight_restriction");
	            result.add("print_to_console");
	            result.add("save");
	            
		        for(int x=0; x<result.size(); x++) {
					if(!result.get(x).toLowerCase().startsWith(args[0].toLowerCase())) {
						result.remove(x);
						x--;
					}
		        }
	            
	            return result;
			}
			else if(args.length == 2 && args[0].equalsIgnoreCase("listen")) {
				for(OfflinePlayer player : plugin.getServer().getOfflinePlayers()) {
					result.add(player.getName());
				}
		        for(int x=0; x<result.size(); x++) {
					if(!result.get(x).toLowerCase().startsWith(args[1].toLowerCase())) {
						result.remove(x);
						x--;
					}
		        }
		        return result;
			}

		}
		
		return result;
	}
}
