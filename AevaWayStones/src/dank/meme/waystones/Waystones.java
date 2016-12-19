package dank.meme.waystones;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

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

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import dank.meme.waystones.commands.CommandManager;
import dank.meme.waystones.commands.waystone.WaystoneCommand;
import net.md_5.bungee.api.ChatColor;

public class Waystones extends JavaPlugin implements Listener, CommandExecutor {
	
	private static Waystones plugin;
	private CommandManager cm;
	public BiMap<String, Waystone> waystones = HashBiMap.create();
	public SQLManager sql;
	
	@Override
	public void onEnable() {
		plugin = this;
		saveDefaultConfig();
		FileConfiguration c = getConfig();
		c.options().copyDefaults(true);
		cm = new CommandManager();
		ConfigurationSection s = c.getConfigurationSection("sql");
		sql = new SQLManager(s.getString("host"), s.getString("port"), s.getString("database"), s.getString("username"), s.getString("password"));
		try {
			ResultSet results = sql.get("waystones");
			while (results.next()) {
				waystones.put(results.getString("name"), new Waystone(new Location(Bukkit.getWorld(results.getString("world")), results.getInt("x"), results.getInt("y"), results.getInt("z"), results.getInt("yaw"), results.getInt("pich")), WaystoneType.valueOf(results.getString("type"))));
			}
			results = null;
			results = sql.get("players");
			while (results.next()) {
				String waystone = results.getString("waystone");
				if (waystones.containsKey(waystone))
					waystones.get(waystone).found.add(UUID.fromString(results.getString("uuid")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		new WaystoneCommand(cm);
	}
	
	public static Waystones getInstance() {
		return plugin;
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
		sql.insert("waystones", Arrays.asList("name", "type", "world", "x", "y", "z"), Arrays.asList(waystones.inverse().get(ws), ws.type.name(), ws.loc.getWorld().getName(), ws.loc.getBlockX() + "", ws.loc.getBlockY() + "", ws.loc.getBlockZ() + "", ws.loc.getYaw() + "", ws.loc.getPitch() + ""));
	}
	
	public static boolean containsIgnoreCase(List<String> haystack, String needle) {
		if (haystack == null)
			return false;
		for (String st : haystack)
			if (st.equalsIgnoreCase(needle))
				return true;
		return false;
	}

	public static void sendMessage(Player pl, String... message) {
		for (String msg : message)
			pl.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
	}
}