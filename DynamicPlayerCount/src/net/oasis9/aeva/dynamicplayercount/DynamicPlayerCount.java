package net.oasis9.aeva.dynamicplayercount;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_9_R1.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.minecraft.server.v1_9_R1.DedicatedPlayerList;

public class DynamicPlayerCount extends JavaPlugin {
	
	@Override
	public void onEnable() {
		saveDefaultConfig();
		FileConfiguration config = getConfig();
		config.options().copyDefaults(true);
		if (config.isInt("maxplayers"))
			setMaxPlayers(config.getInt("maxplayers"));
		else
			config.set("maxplayers", getMaxPlayers());
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length > 0)
			try {
				if (sender instanceof Player) {
					if (((Player) sender).hasPermission(permissionModify()))
						setMaxPlayers(Integer.valueOf(args[0]));
					showMaxPlayers(sender);
				} else {
					setMaxPlayers(Integer.valueOf(args[0]));
					showMaxPlayers(sender);
				}
			} catch (Exception ex) {
				if (sender instanceof Player && ((Player) sender).hasPermission(permissionView()))
					showMaxPlayers(sender);
			}
		else if ((sender instanceof Player && ((Player) sender).hasPermission(permissionView())) || !(sender instanceof Player))
			showMaxPlayers(sender);
		return true;
	}
	
	public String permissionModify() {
		return getConfig().getString("permissions.modify");
	}

	public String permissionView() {
		return getConfig().getString("permissions.view");
	}
	
	public String messageKick() {
		return ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.kick"));
	}
	
	public String messageView() {
		return ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.view")).replace("%maxplayers%", "" + getMaxPlayers());
	}
	
	public String messageModify() {
		return ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.modify"));
	}
	
	public void showMaxPlayers(CommandSender sender) {
		String msg = messageView();
		if (sender instanceof Player && ((Player) sender).hasPermission(permissionView())) {
			sender.sendMessage(msg);
			if (((Player) sender).hasPermission(permissionModify()))
				sender.sendMessage(messageModify());
		} else {
			sender.sendMessage(ChatColor.stripColor(msg));
			sender.sendMessage(messageModify());
		}
	}
	
	public int getMaxPlayers() {
		return Bukkit.getMaxPlayers();
	}
	
	public void setMaxPlayers(int maxPlayers) {
		try {
			CraftServer ms = (CraftServer) Bukkit.getServer();
			Field pl = ms.getClass().getDeclaredField("playerList");
			if (pl != null)
				pl.setAccessible(true);
				Class<?> fieldType = pl.getType();
				if (DedicatedPlayerList.class == fieldType) {
					DedicatedPlayerList dpl = (DedicatedPlayerList) pl.get(ms);
					if (dpl != null) {
						Field mp = dpl.getClass().getSuperclass().getDeclaredField("maxPlayers");
						if (mp != null) {
							mp.setAccessible(true);
							if (Bukkit.getOnlinePlayers().size() > getMaxPlayers()) {
								while (Bukkit.getOnlinePlayers().size() > getMaxPlayers())
									Bukkit.getOnlinePlayers().iterator().next().kickPlayer(messageKick());
							}
							mp.set(dpl, maxPlayers);
						} else
							Bukkit.broadcastMessage("mp null");
					} else
						Bukkit.broadcastMessage("dpl null");
		        } else
					Bukkit.broadcastMessage("pl null");
		} catch (Exception e) {
			e.printStackTrace();
			Bukkit.broadcastMessage("Error: " + e.getMessage() + " - " + e.getCause() + " [DPC has gone to shit]");
			for (StackTraceElement st : e.getStackTrace())
				Bukkit.broadcastMessage(st.toString());
		}
	}
}