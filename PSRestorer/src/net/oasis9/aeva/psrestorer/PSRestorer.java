package net.oasis9.aeva.psrestorer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.intellectualcrafters.plot.api.PlotAPI;
import com.intellectualcrafters.plot.object.Plot;

/**
 * Created by Bob Sharp/bobdesu/oasis9.net on 31/10/2016.
 * Do not remove this notice.
 */
public class PSRestorer extends JavaPlugin {
	
	public PlotAPI api;
	public Boolean running = false;
	public Boolean notFound = true;
	public Boolean notFoundX = true;
	public int x = 0;
	public int z = 0;
	public int xNone = 0;
	public int zNone = 0;
	File dataFolder = null;
	File saveTo = null;
	FileWriter fw = null;
	PrintWriter pw = null;
	
	@Override
	public void onEnable() {
		PluginManager manager = Bukkit.getServer().getPluginManager();
        Plugin plotsquared = manager.getPlugin("PlotSquared");
        if (plotsquared != null && !plotsquared.isEnabled()) {
            log("[" + getName() + "] Could not find PlotSquared! Disabling self.");
            manager.disablePlugin(this);
            return;
        }
        api = new PlotAPI();
        
	}
	@Override
	public void onDisable() {
		if (pw != null)
			pw.close();
	}
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (running) {
			sender.sendMessage("Iterations still in progress!");
			return true;
		} else
			running = true;
		Boolean findIncLow = true;
		Boolean findIncHigh = true;
		World w = sender instanceof Player ? ((Player) sender).getWorld() : Bukkit.getWorlds().iterator().next();
		int low = 0;
		int ainc = 0;
		int ay = 65;
		if (args.length > 0)
			try {
				ay = Integer.valueOf(args[0]);
			} catch (Exception ex) {
				log("[+-] NaN: Continuing with y 65");
			}
		int bx = 0;
		int bz = 0;
		final int y = ay;
		log("[+-] Determining plot sizes..");
		for (int x = 0; x < 2000; x++) {
			if (!findIncHigh)
				break;
			for (int z = 0; z < 2000; z++) {
				if (!findIncHigh)
					break;
				Block b = w.getBlockAt(x, y, z);
				if (b != null && b.getType().equals(Material.WALL_SIGN)) {
					BlockState bs = b.getState();
					if (bs != null && bs instanceof Sign) {
						Sign s = (Sign) bs;
						if (ChatColor.stripColor(s.getLine(3)).contains("Claimed")) {
							if (findIncLow) {
								low = z;
								bx = x;
								bz = z;
								findIncLow = false;
							} else if (findIncHigh) {
								ainc = z - low;
								findIncHigh = false;
							}
						}
					}
				}
			}
		}
		final int inc = ainc;
		final int bgx = bx;
		final int bgz = bz;
		if (inc == 0) {
			log("[+-] No plots to restore!");
			running = false;
			return true;
		}
		log("[+-] Calculated plot (including road) sizes: " + inc + " blocks");
		try {
		log("[+] Beginning positive iterations..");
		for (int lx = bgx; true; lx += inc) {
			x = lx;
			zNone = 0;
			notFoundX = true;
			notFound = true;
			for (int lz = bgz; true; lz += inc) {
				z = lz;
				logOnly("[+] Processing x: " + x + " z: " + z);
				final Block b = w.getBlockAt(x, y, z);
				if (b != null && b.getType().equals(Material.WALL_SIGN)) {
					final BlockState bs = b.getState();
					if (bs != null && bs instanceof Sign) {
						Sign s = (Sign) bs;
						if (ChatColor.stripColor(s.getLine(3)).contains("Claimed")) {
							notFound = false;
							String name = ChatColor.stripColor(s.getLine(2));
							String ids = ChatColor.stripColor(s.getLine(0).replace("ID: ", ""));
							String[] ida = ids.split(";");
							int idx = Integer.valueOf(ida[0]);
							int idz = Integer.valueOf(ida[1]);
							logOnly("[+] Found plot " + idx + ";" + idz);
							UUID uuid = get(name);
							if (uuid == null)
								log("[+] Couldn't find " + name + "'s UUID - Cannot set as plot " + idx + ";" + idz + " owner.");
							else {
								Plot p = api.getPlot(w, idx, idz);
								if (!p.hasOwner())
									p.setOwner(uuid);
								log("[+] Set plot " + idx + ";" + idz + " owner " + name + ": " + uuid);
							}
						} else
							notFound = true;
					} else
						notFound = true;
				} else
					notFound = true;
				if (notFound) {
					if (zNone > 10)
						break;
					else
						zNone++;
				} else
					notFoundX = false;
			}
			if (notFoundX)
				if (xNone > 10)
					break;
				else
					xNone++;
		}
		log("[-] Beginning negative iterations..");
		for (int lx = bgx; true; lx -= inc) {
			x = lx;
			zNone = 0;
			notFoundX = true;
			notFound = true;
			for (int lz = bgz; true; lz -= inc) {
				z = lz;
				logOnly("[-] Processing x: " + x + " z: " + z);
				final Block b = w.getBlockAt(x, y, z);
				if (b != null && b.getType().equals(Material.WALL_SIGN)) {
					final BlockState bs = b.getState();
					if (bs != null && bs instanceof Sign) {
						Sign s = (Sign) bs;
						if (ChatColor.stripColor(s.getLine(3)).contains("Claimed")) {
							notFound = false;
							String name = ChatColor.stripColor(s.getLine(2));
							String ids = ChatColor.stripColor(s.getLine(0).replace("ID: ", ""));
							String[] ida = ids.split(";");
							int idx = Integer.valueOf(ida[0]);
							int idz = Integer.valueOf(ida[1]);
							logOnly("[-] Found plot " + idx + ";" + idz);
							UUID uuid = get(name);
							if (uuid == null)
								log("[-] Couldn't find " + name + "'s UUID - Cannot set as plot " + idx + ";" + idz + " owner.");
							else {
								Plot p = api.getPlot(w, idx, idz);
								if (!p.hasOwner())
									p.setOwner(uuid);
								log("[-] Set plot " + idx + ";" + idz + " owner " + name + ": " + uuid);
							}
						} else
							notFound = true;
					} else
						notFound = true;
				} else
					notFound = true;
				if (notFound) {
					if (zNone > 10)
						break;
					else
						zNone++;
				} else
					notFoundX = false;
			}
			if (notFoundX)
				if (xNone > 10)
					break;
				else
					xNone++;
		}
		log("[+-] Processing complete.");
		} catch (Exception ex) {
			log("[+-] Woah! " + ex.getMessage());
		}
		running = false;
		return true;
	}
	void logOnly(String msg) {
		Bukkit.getLogger().info(msg);
		if (dataFolder == null) {
			dataFolder = getDataFolder();
			if (!dataFolder.exists())
				dataFolder.mkdir();
		}
		if (saveTo == null) {
			saveTo = new File(getDataFolder(), "log-" + new Date().toString() + ".txt");
			if (!saveTo.exists()) {
				try {
					saveTo.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if (fw == null || pw == null)
			try {
				fw = new FileWriter(saveTo, true);
				pw = new PrintWriter(fw);
			} catch (Exception e) {
				e.printStackTrace();
			}
		pw.println(msg);
		pw.flush();
	}
	void log(String msg) {
		logOnly(msg);
		for (Player pl : Bukkit.getOnlinePlayers())
			pl.sendMessage(msg);
	}
	public UUID get(String user) {
		UUID uuid = null;
    	try {
        	URL url = new URL("https://mcapi.ca/uuid/player/" + user);
			String response = get(url);
			JsonArray entries = (JsonArray) new JsonParser().parse(response);
			JsonObject jobj = (JsonObject) entries.get(0);
			Boolean isNull = jobj.get("name").isJsonNull();
			String raw = jobj.get("uuid_formatted").getAsString();
			if (!isNull && !raw.equalsIgnoreCase("----"))
				uuid = UUID.fromString(raw);
			return uuid;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return uuid;
    }
	public String get(URL url) throws IOException {
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setUseCaches(false);
		connection.setDoInput(true);
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String line;
		StringBuffer response = new StringBuffer();
		while ((line = reader.readLine()) != null)
			response.append(line);
		reader.close();
		return response.toString();
	}
}