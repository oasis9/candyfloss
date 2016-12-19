package dank.meme.waystones;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import dank.meme.waystones.commands.CommandManager;
import dank.meme.waystones.commands.waystone.WaystoneCommand;
import dank.meme.waystones.gui.MainGUI;
import dank.meme.waystones.gui.WaystoneTypeGUI;
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
		ConfigurationSection s = c.getConfigurationSection("sql");
		sql = new SQLManager(s.getString("url"), s.getString("port"), s.getString("database"), s.getString("username"), s.getString("password"));
		try {
			sql.open();
			ResultSet results = sql.get("waystones");
			while (results.next()) {
				waystones.put(results.getString("name"), new Waystone(new Location(Bukkit.getWorld(results.getString("world")), results.getInt("x"), results.getInt("y"), results.getInt("z"), results.getInt("yaw"), results.getInt("pich")), WaystoneType.valueOf(results.getString("type"))));
			}
			results = sql.get("players");
			while (results.next()) {
				String waystone = results.getString("waystone");
				if (waystones.containsKey(waystone))
					waystones.get(waystone).found.add(UUID.fromString(results.getString("uuid")));
			}
			sql.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new MainGUI(), this);
		pm.registerEvents(new WaystoneTypeGUI(), this);
		cm = new CommandManager();
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
		try {
			waystones.put(name, ws);
			sql.open();
			PreparedStatement statement = sql.connection
					.prepareStatement("INSERT INTO waystones (name,type,world,x,y,z,yaw,pitch) values(?,?,?,?,?,?,?,?);");
			statement.setString(1, waystones.inverse().get(ws));
			statement.setString(2, ws.type.name());
			statement.setString(3, ws.loc.getWorld().getName());
			statement.setInt(4, ws.loc.getBlockX());
			statement.setInt(5, ws.loc.getBlockY());
			statement.setInt(6, ws.loc.getBlockZ());
			statement.setInt(7, (int) ws.loc.getYaw());
			statement.setInt(8, (int) ws.loc.getPitch());
			statement.executeUpdate();
			sql.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
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