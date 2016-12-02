package dank.meme.waystones;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import dank.meme.waystones.commands.CommandManager;
import dank.meme.waystones.commands.waystone.WaystoneCommand;

public class Waystones extends JavaPlugin implements Listener, CommandExecutor {
	
	private CommandManager cm;
	public static Waystones plugin;
	private Map<String, Waystone> waystones = new HashMap<String, Waystone>();
	
	@Override
	public void onEnable() {
		plugin = this;
		saveDefaultConfig();
		FileConfiguration config = getConfig();
		config.options().copyDefaults(true);
		if (config.isConfigurationSection("waystones"))
			for (String key : config.getConfigurationSection("waystones").getKeys(false))
				try {
					ConfigurationSection ws = config.getConfigurationSection("waystones." + key);
					addWaystone(key, new Waystone(new Location(Bukkit.getWorld(ws.getString("world")), ws.getInt("x"), ws.getInt("y"), ws.getInt("z")), WaystoneType.valueOf(ws.getString("type"))));
				} catch (Exception ex) {
					Bukkit.getLogger().warning("Couldn't load Waystone " + key + "!");
					ex.printStackTrace();
				}
		cm = new CommandManager();
		new WaystoneCommand(cm);
		saveConfig();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player pl = (Player) sender;
			cm.run(pl, cmd.getName(), args);
		}
		return true;
	}
	
	public void addWaystone(String name, Waystone ws) {
		waystones.put(name, ws);
		ConfigurationSection config = getConfig().getConfigurationSection("waystones." + name);
		config.set("world", ws.loc.getWorld().getName());
		config.set("x", ws.loc.getX());
		config.set("y", ws.loc.getY());
		config.set("z", ws.loc.getZ());
		config.set("type", ws.type);
		saveConfig();
	}
	
	public static boolean containsIgnoreCase(List<String> haystack, String needle) {
		if (haystack == null)
			return false;
		for (String st : haystack)
			if (st.equalsIgnoreCase(needle))
				return true;
		return false;
	}
}