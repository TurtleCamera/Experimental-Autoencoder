package me.Brick.TurtleCamera.AutoEncoder.Data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;

import me.Brick.TurtleCamera.AutoEncoder.Auto_Encoder;
import me.Brick.TurtleCamera.AutoEncoder.Information;
import me.Brick.TurtleCamera.AutoEncoder.Main;

public class ConfigHandler {
	
	Main plugin;
	
	public ConfigHandler(Main plugin) {
		this.plugin = plugin;
		
		File file = new File(plugin.getDataFolder().getAbsolutePath() + "./info.out");
		
		if(!file.exists()) {
			try {
				file.createNewFile();
				plugin.getLogger().info("info file not found, creating...");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public void writeInfo(Information info) {
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(plugin.getDataFolder().getAbsolutePath() + "./info.out", true)));
			out.append("Input: " + info.inputString + "\nOutput: " + info.outputString + "\nCost: " + info.cost + "\nNumCorrect: " + info.numCorrect + "\nAccuracy: " + (info.numCorrect / Auto_Encoder.MAX_SENTENCE_LENGTH) + "\n");
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeAllData() {
		
		ArrayList<String> listeners = new ArrayList<String>();
		plugin.getConfig().set("Listeners", null);
		for(UUID uuid : plugin.data.listeners) {
			listeners.add(uuid.toString());
		}
		plugin.getConfig().set("Listeners", listeners);
		
		plugin.getConfig().set("Structures", null);
		
		plugin.getConfig().set("Structures.count", Auto_Encoder.count);
		
		//Write all weightMatrixes:
		for(int x=0; x<Auto_Encoder.weightMatrix1.length; x++) {
			plugin.getConfig().set("Structures.weightMatrix1." + x, Auto_Encoder.weightMatrix1[x]);
		}
		
		for(int x=0; x<Auto_Encoder.weightMatrix2.length; x++) {
			plugin.getConfig().set("Structures.weightMatrix2." + x, Auto_Encoder.weightMatrix2[x]);
		}
		
		for(int x=0; x<Auto_Encoder.weightMatrix3.length; x++) {
			plugin.getConfig().set("Structures.weightMatrix3." + x, Auto_Encoder.weightMatrix3[x]);
		}
		
		for(int x=0; x<Auto_Encoder.weightMatrix4.length; x++) {
			plugin.getConfig().set("Structures.weightMatrix4." + x, Auto_Encoder.weightMatrix4[x]);
		}
		
		for(int x=0; x<Auto_Encoder.weightMatrix5.length; x++) {
			plugin.getConfig().set("Structures.weightMatrix5." + x, Auto_Encoder.weightMatrix5[x]);
		}
		
		for(int x=0; x<Auto_Encoder.weightMatrix6.length; x++) {
			plugin.getConfig().set("Structures.weightMatrix6." + x, Auto_Encoder.weightMatrix6[x]);
		}
		
		//Write all biasVectors:
		plugin.getConfig().set("Structures.biasVector1", Auto_Encoder.biasVector1);
		plugin.getConfig().set("Structures.biasVector2", Auto_Encoder.biasVector2);
		plugin.getConfig().set("Structures.biasVector3", Auto_Encoder.biasVector3);
		plugin.getConfig().set("Structures.biasVector4", Auto_Encoder.biasVector4);
		plugin.getConfig().set("Structures.biasVector5", Auto_Encoder.biasVector5);
		plugin.getConfig().set("Structures.biasVector6", Auto_Encoder.biasVector6);

		//Write all stochasticPartialDerivativeWeightMatrixes:
		for(int x=0; x<Auto_Encoder.stochasticPartialDerivativeWeightMatrix1.length; x++) {
			plugin.getConfig().set("Structures.stochasticPartialDerivativeWeightMatrix1." + x, Auto_Encoder.stochasticPartialDerivativeWeightMatrix1[x]);
		}
		
		for(int x=0; x<Auto_Encoder.stochasticPartialDerivativeWeightMatrix2.length; x++) {
			plugin.getConfig().set("Structures.stochasticPartialDerivativeWeightMatrix2." + x, Auto_Encoder.stochasticPartialDerivativeWeightMatrix2[x]);
		}
		
		for(int x=0; x<Auto_Encoder.stochasticPartialDerivativeWeightMatrix3.length; x++) {
			plugin.getConfig().set("Structures.stochasticPartialDerivativeWeightMatrix3." + x, Auto_Encoder.stochasticPartialDerivativeWeightMatrix3[x]);
		}
		
		for(int x=0; x<Auto_Encoder.stochasticPartialDerivativeWeightMatrix4.length; x++) {
			plugin.getConfig().set("Structures.stochasticPartialDerivativeWeightMatrix4." + x, Auto_Encoder.stochasticPartialDerivativeWeightMatrix4[x]);
		}
		
		for(int x=0; x<Auto_Encoder.stochasticPartialDerivativeWeightMatrix5.length; x++) {
			plugin.getConfig().set("Structures.stochasticPartialDerivativeWeightMatrix5." + x, Auto_Encoder.stochasticPartialDerivativeWeightMatrix5[x]);
		}
		
		for(int x=0; x<Auto_Encoder.stochasticPartialDerivativeWeightMatrix6.length; x++) {
			plugin.getConfig().set("Structures.stochasticPartialDerivativeWeightMatrix6." + x, Auto_Encoder.stochasticPartialDerivativeWeightMatrix6[x]);
		}
		
		//Write stochasticPartialDerivativeBiasVectors:
		plugin.getConfig().set("Structures.stochasticPartialDerivativeBiasVector1", Auto_Encoder.stochasticPartialDerivativeBiasVector1);
		plugin.getConfig().set("Structures.stochasticPartialDerivativeBiasVector2", Auto_Encoder.stochasticPartialDerivativeBiasVector2);
		plugin.getConfig().set("Structures.stochasticPartialDerivativeBiasVector3", Auto_Encoder.stochasticPartialDerivativeBiasVector3);
		plugin.getConfig().set("Structures.stochasticPartialDerivativeBiasVector4", Auto_Encoder.stochasticPartialDerivativeBiasVector4);
		plugin.getConfig().set("Structures.stochasticPartialDerivativeBiasVector5", Auto_Encoder.stochasticPartialDerivativeBiasVector5);
		plugin.getConfig().set("Structures.stochasticPartialDerivativeBiasVector6", Auto_Encoder.stochasticPartialDerivativeBiasVector6);
		
		plugin.getConfig().set("Options", null);
		plugin.getConfig().set("Options.toggleWeightRestriction", Auto_Encoder.toggleWeightRestriction);
		plugin.getConfig().set("Options.enabled", plugin.data.pluginEnabled);
		
		plugin.saveConfig();
	}
	
	public void loadData() {
		plugin.getConfig().addDefault("Structures", "");
		plugin.getConfig().addDefault("Options", "");
		plugin.getConfig().addDefault("Listeners", "");
		
		//Load listeners:
	    for(String listener : plugin.getConfig().getStringList("Listeners")) {
		    plugin.data.listeners.add(UUID.fromString(listener));
		    plugin.getServer().getLogger().info("Loaded listener: " + listener);
	    }
		
	    //Check if the network is new:
		if(plugin.getConfig().getConfigurationSection("Structures.weightMatrix1") == null) {
			plugin.getLogger().info("New network detected: Initialzing random values...");
			//loadRandom();
			Auto_Encoder.initialize(false);
			return;
		}
		
		Auto_Encoder.initialize(true);
		
		//Load options:
		Auto_Encoder.toggleWeightRestriction = plugin.getConfig().getBoolean("Options.toggleWeightRestriction");
		plugin.data.pluginEnabled = plugin.getConfig().getBoolean("Options.enabled");
		
		//Load count:
		Auto_Encoder.count = plugin.getConfig().getInt("Structures.count");
		
		boolean first = true;
		
		//Load weightMatrix1:
		ConfigurationSection section = plugin.getConfig().getConfigurationSection("Structures.weightMatrix1");
		for(String key : section.getKeys(false)) {

			List<Float> row = section.getFloatList(key);
			
			if(first) {
				Auto_Encoder.weightMatrix1 = new float[section.getKeys(false).size()][row.size()];
				first = false;
			}
			
			for(int x=0; x<row.size(); x++) {
				Auto_Encoder.weightMatrix1[Integer.parseInt(key)][x] = row.get(x).floatValue();
			}
		}
		first = true;
		
		//Load weightMatrix2:
		section = plugin.getConfig().getConfigurationSection("Structures.weightMatrix2");
		for(String key : section.getKeys(false)) {

			List<Float> row = section.getFloatList(key);
			
			if(first) {
				Auto_Encoder.weightMatrix2 = new float[section.getKeys(false).size()][row.size()];
				first = false;
			}
			
			for(int x=0; x<row.size(); x++) {
				Auto_Encoder.weightMatrix2[Integer.parseInt(key)][x] = row.get(x).floatValue();
			}
		}
		first = true;
		
		//Load weightMatrix3:
		section = plugin.getConfig().getConfigurationSection("Structures.weightMatrix3");
		for(String key : section.getKeys(false)) {

			List<Float> row = section.getFloatList(key);
			
			if(first) {
				Auto_Encoder.weightMatrix3 = new float[section.getKeys(false).size()][row.size()];
				first = false;
			}
			
			for(int x=0; x<row.size(); x++) {
				Auto_Encoder.weightMatrix3[Integer.parseInt(key)][x] = row.get(x).floatValue();
			}
		}
		first = true;
		
		//Load weightMatrix4:
		section = plugin.getConfig().getConfigurationSection("Structures.weightMatrix4");
		for(String key : section.getKeys(false)) {

			List<Float> row = section.getFloatList(key);
			
			if(first) {
				Auto_Encoder.weightMatrix4 = new float[section.getKeys(false).size()][row.size()];
				first = false;
			}
			
			for(int x=0; x<row.size(); x++) {
				Auto_Encoder.weightMatrix4[Integer.parseInt(key)][x] = row.get(x).floatValue();
			}
		}
		first = true;
		
		//Load weightMatrix5:
		section = plugin.getConfig().getConfigurationSection("Structures.weightMatrix5");
		for(String key : section.getKeys(false)) {

			List<Float> row = section.getFloatList(key);
			
			if(first) {
				Auto_Encoder.weightMatrix5 = new float[section.getKeys(false).size()][row.size()];
				first = false;
			}
			
			for(int x=0; x<row.size(); x++) {
				Auto_Encoder.weightMatrix5[Integer.parseInt(key)][x] = row.get(x).floatValue();
			}
		}
		first = true;
		
		//Load weightMatrix6:
		section = plugin.getConfig().getConfigurationSection("Structures.weightMatrix6");
		for(String key : section.getKeys(false)) {

			List<Float> row = section.getFloatList(key);
			
			if(first) {
				Auto_Encoder.weightMatrix6 = new float[section.getKeys(false).size()][row.size()];
				first = false;
			}
			
			for(int x=0; x<row.size(); x++) {
				Auto_Encoder.weightMatrix6[Integer.parseInt(key)][x] = row.get(x).floatValue();
			}
		}
		first = true;
		
		//Load biasVector1:
		List<Float> tempBiasList = plugin.getConfig().getFloatList("Structures.biasVector1");
		Auto_Encoder.biasVector1 = new float[tempBiasList.size()];
		for(int x=0; x<tempBiasList.size(); x++) {
			Auto_Encoder.biasVector1[x] = tempBiasList.get(x);
		}
		
		//Load biasVector2:
		tempBiasList = plugin.getConfig().getFloatList("Structures.biasVector2");
		Auto_Encoder.biasVector2 = new float[tempBiasList.size()];
		for(int x=0; x<tempBiasList.size(); x++) {
			Auto_Encoder.biasVector2[x] = tempBiasList.get(x);
		}
		
		//Load biasVector3:
		tempBiasList = plugin.getConfig().getFloatList("Structures.biasVector3");
		Auto_Encoder.biasVector3 = new float[tempBiasList.size()];
		for(int x=0; x<tempBiasList.size(); x++) {
			Auto_Encoder.biasVector3[x] = tempBiasList.get(x);
		}
		
		//Load biasVector4:
		tempBiasList = plugin.getConfig().getFloatList("Structures.biasVector1");
		Auto_Encoder.biasVector4 = new float[tempBiasList.size()];
		for(int x=0; x<tempBiasList.size(); x++) {
			Auto_Encoder.biasVector4[x] = tempBiasList.get(x);
		}
		
		//Load biasVector5:
		tempBiasList = plugin.getConfig().getFloatList("Structures.biasVector5");
		Auto_Encoder.biasVector5 = new float[tempBiasList.size()];
		for(int x=0; x<tempBiasList.size(); x++) {
			Auto_Encoder.biasVector5[x] = tempBiasList.get(x);
		}
		
		//Load biasVector6:
		tempBiasList = plugin.getConfig().getFloatList("Structures.biasVector6");
		Auto_Encoder.biasVector6 = new float[tempBiasList.size()];
		for(int x=0; x<tempBiasList.size(); x++) {
			Auto_Encoder.biasVector6[x] = tempBiasList.get(x);
		}
		
		//Load stochasticPartialDerivativeWeightMatrix1:
		section = plugin.getConfig().getConfigurationSection("Structures.stochasticPartialDerivativeWeightMatrix1");
		for(String key : section.getKeys(false)) {

			List<Float> row = section.getFloatList(key);
			
			if(first) {
				Auto_Encoder.stochasticPartialDerivativeWeightMatrix1 = new float[section.getKeys(false).size()][row.size()];
				first = false;
			}
			
			for(int x=0; x<row.size(); x++) {
				Auto_Encoder.stochasticPartialDerivativeWeightMatrix1[Integer.parseInt(key)][x] = row.get(x).floatValue();
			}
		}
		first = true;
		
		//Load stochasticPartialDerivativeWeightMatrix2:
		section = plugin.getConfig().getConfigurationSection("Structures.stochasticPartialDerivativeWeightMatrix2");
		for(String key : section.getKeys(false)) {

			List<Float> row = section.getFloatList(key);
			
			if(first) {
				Auto_Encoder.stochasticPartialDerivativeWeightMatrix2 = new float[section.getKeys(false).size()][row.size()];
				first = false;
			}
			
			for(int x=0; x<row.size(); x++) {
				Auto_Encoder.stochasticPartialDerivativeWeightMatrix2[Integer.parseInt(key)][x] = row.get(x).floatValue();
			}
		}
		first = true;
		
		//Load stochasticPartialDerivativeWeightMatrix3:
		section = plugin.getConfig().getConfigurationSection("Structures.stochasticPartialDerivativeWeightMatrix3");
		for(String key : section.getKeys(false)) {

			List<Float> row = section.getFloatList(key);
			
			if(first) {
				Auto_Encoder.stochasticPartialDerivativeWeightMatrix3 = new float[section.getKeys(false).size()][row.size()];
				first = false;
			}
			
			for(int x=0; x<row.size(); x++) {
				Auto_Encoder.stochasticPartialDerivativeWeightMatrix3[Integer.parseInt(key)][x] = row.get(x).floatValue();
			}
		}
		first = true;
		
		//Load stochasticPartialDerivativeWeightMatrix4:
		section = plugin.getConfig().getConfigurationSection("Structures.stochasticPartialDerivativeWeightMatrix4");
		for(String key : section.getKeys(false)) {

			List<Float> row = section.getFloatList(key);
			
			if(first) {
				Auto_Encoder.stochasticPartialDerivativeWeightMatrix4 = new float[section.getKeys(false).size()][row.size()];
				first = false;
			}
			
			for(int x=0; x<row.size(); x++) {
				Auto_Encoder.stochasticPartialDerivativeWeightMatrix4[Integer.parseInt(key)][x] = row.get(x).floatValue();
			}
		}
		first = true;
		
		//Load stochasticPartialDerivativeWeightMatrix5:
		section = plugin.getConfig().getConfigurationSection("Structures.stochasticPartialDerivativeWeightMatrix5");
		for(String key : section.getKeys(false)) {

			List<Float> row = section.getFloatList(key);
			
			if(first) {
				Auto_Encoder.stochasticPartialDerivativeWeightMatrix5 = new float[section.getKeys(false).size()][row.size()];
				first = false;
			}
			
			for(int x=0; x<row.size(); x++) {
				Auto_Encoder.stochasticPartialDerivativeWeightMatrix5[Integer.parseInt(key)][x] = row.get(x).floatValue();
			}
		}
		first = true;
		
		//Load stochasticPartialDerivativeWeightMatrix6:
		section = plugin.getConfig().getConfigurationSection("Structures.stochasticPartialDerivativeWeightMatrix6");
		for(String key : section.getKeys(false)) {

			List<Float> row = section.getFloatList(key);
			
			if(first) {
				Auto_Encoder.stochasticPartialDerivativeWeightMatrix6 = new float[section.getKeys(false).size()][row.size()];
				first = false;
			}
			
			for(int x=0; x<row.size(); x++) {
				Auto_Encoder.stochasticPartialDerivativeWeightMatrix6[Integer.parseInt(key)][x] = row.get(x).floatValue();
			}
		}
		first = true;
		
		//Load stochasticPartialDerivativeBiasVector1:
		tempBiasList = plugin.getConfig().getFloatList("Structures.stochasticPartialDerivativeBiasVector1");
		Auto_Encoder.stochasticPartialDerivativeBiasVector1 = new float[tempBiasList.size()];
		for(int x=0; x<tempBiasList.size(); x++) {
			Auto_Encoder.stochasticPartialDerivativeBiasVector1[x] = tempBiasList.get(x);
		}
		
		//Load stochasticPartialDerivativeBiasVector2:
		tempBiasList = plugin.getConfig().getFloatList("Structures.stochasticPartialDerivativeBiasVector2");
		Auto_Encoder.stochasticPartialDerivativeBiasVector2 = new float[tempBiasList.size()];
		for(int x=0; x<tempBiasList.size(); x++) {
			Auto_Encoder.stochasticPartialDerivativeBiasVector2[x] = tempBiasList.get(x);
		}
		
		//Load stochasticPartialDerivativeBiasVector3:
		tempBiasList = plugin.getConfig().getFloatList("Structures.stochasticPartialDerivativeBiasVector3");
		Auto_Encoder.stochasticPartialDerivativeBiasVector3 = new float[tempBiasList.size()];
		for(int x=0; x<tempBiasList.size(); x++) {
			Auto_Encoder.stochasticPartialDerivativeBiasVector3[x] = tempBiasList.get(x);
		}
		
		//Load stochasticPartialDerivativeBiasVector4:
		tempBiasList = plugin.getConfig().getFloatList("Structures.stochasticPartialDerivativeBiasVector4");
		Auto_Encoder.stochasticPartialDerivativeBiasVector4 = new float[tempBiasList.size()];
		for(int x=0; x<tempBiasList.size(); x++) {
			Auto_Encoder.stochasticPartialDerivativeBiasVector4[x] = tempBiasList.get(x);
		}
		
		//Load stochasticPartialDerivativeBiasVector5:
		tempBiasList = plugin.getConfig().getFloatList("Structures.stochasticPartialDerivativeBiasVector5");
		Auto_Encoder.stochasticPartialDerivativeBiasVector5 = new float[tempBiasList.size()];
		for(int x=0; x<tempBiasList.size(); x++) {
			Auto_Encoder.stochasticPartialDerivativeBiasVector5[x] = tempBiasList.get(x);
		}
		
		//Load stochasticPartialDerivativeBiasVector6:
		tempBiasList = plugin.getConfig().getFloatList("Structures.stochasticPartialDerivativeBiasVector6");
		Auto_Encoder.stochasticPartialDerivativeBiasVector6 = new float[tempBiasList.size()];
		for(int x=0; x<tempBiasList.size(); x++) {
			Auto_Encoder.stochasticPartialDerivativeBiasVector6[x] = tempBiasList.get(x);
		}
	}
	
	public void printData() {
		
		//Display all for test:
		for(float[] row : Auto_Encoder.weightMatrix1) {
			for(float num : row) {
				System.out.print(num + " ");
			}
			System.out.println();
		}
		System.out.println();
		for(float[] row : Auto_Encoder.weightMatrix2) {
			for(float num : row) {
				System.out.print(num + " ");
			}
			System.out.println();
		}
		System.out.println();
		for(float[] row : Auto_Encoder.weightMatrix3) {
			for(float num : row) {
				System.out.print(num + " ");
			}
			System.out.println();
		}
		System.out.println();
		for(float[] row : Auto_Encoder.weightMatrix4) {
			for(float num : row) {
				System.out.print(num + " ");
			}
			System.out.println();
		}
		System.out.println();
		for(float[] row : Auto_Encoder.weightMatrix5) {
			for(float num : row) {
				System.out.print(num + " ");
			}
			System.out.println();
		}
		System.out.println();
		for(float[] row : Auto_Encoder.weightMatrix6) {
			for(float num : row) {
				System.out.print(num + " ");
			}
			System.out.println();
		}
		System.out.println();
		
		for(float num : Auto_Encoder.biasVector1) {
			System.out.print(num + " ");
		}
		System.out.println("\n");
		for(float num : Auto_Encoder.biasVector2) {
			System.out.print(num + " ");
		}
		System.out.println("\n");
		for(float num : Auto_Encoder.biasVector3) {
			System.out.print(num + " ");
		}
		System.out.println("\n");
		for(float num : Auto_Encoder.biasVector4) {
			System.out.print(num + " ");
		}
		System.out.println("\n");
		for(float num : Auto_Encoder.biasVector5) {
			System.out.print(num + " ");
		}
		System.out.println("\n");
		for(float num : Auto_Encoder.biasVector6) {
			System.out.print(num + " ");
		}
		System.out.println("\n");
		
		for(float[] row : Auto_Encoder.stochasticPartialDerivativeWeightMatrix1) {
			for(float num : row) {
				System.out.print(num + " ");
			}
			System.out.println();
		}
		System.out.println();
		for(float[] row : Auto_Encoder.stochasticPartialDerivativeWeightMatrix2) {
			for(float num : row) {
				System.out.print(num + " ");
			}
			System.out.println();
		}
		System.out.println();
		for(float[] row : Auto_Encoder.stochasticPartialDerivativeWeightMatrix3) {
			for(float num : row) {
				System.out.print(num + " ");
			}
			System.out.println();
		}
		System.out.println();
		for(float[] row : Auto_Encoder.stochasticPartialDerivativeWeightMatrix4) {
			for(float num : row) {
				System.out.print(num + " ");
			}
			System.out.println();
		}
		System.out.println();
		for(float[] row : Auto_Encoder.stochasticPartialDerivativeWeightMatrix5) {
			for(float num : row) {
				System.out.print(num + " ");
			}
			System.out.println();
		}
		System.out.println();
		for(float[] row : Auto_Encoder.stochasticPartialDerivativeWeightMatrix6) {
			for(float num : row) {
				System.out.print(num + " ");
			}
			System.out.println();
		}
		System.out.println();
		
		for(float num : Auto_Encoder.stochasticPartialDerivativeBiasVector1) {
			System.out.print(num + " ");
		}
		System.out.println("\n");
		for(float num : Auto_Encoder.stochasticPartialDerivativeBiasVector2) {
			System.out.print(num + " ");
		}
		System.out.println("\n");
		for(float num : Auto_Encoder.stochasticPartialDerivativeBiasVector3) {
			System.out.print(num + " ");
		}
		System.out.println("\n");
		for(float num : Auto_Encoder.stochasticPartialDerivativeBiasVector4) {
			System.out.print(num + " ");
		}
		System.out.println("\n");
		for(float num : Auto_Encoder.stochasticPartialDerivativeBiasVector5) {
			System.out.print(num + " ");
		}
		System.out.println("\n");
		for(float num : Auto_Encoder.stochasticPartialDerivativeBiasVector6) {
			System.out.print(num + " ");
		}
		System.out.println("\n");
	}
}
