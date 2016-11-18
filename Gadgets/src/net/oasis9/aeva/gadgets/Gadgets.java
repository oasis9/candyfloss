package net.oasis9.aeva.gadgets;

import static org.bukkit.ChatColor.AQUA;
import static org.bukkit.ChatColor.YELLOW;
import static org.bukkit.ChatColor.RED;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Gadgets extends JavaPlugin {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("trap")) {
			if (sender.hasPermission("gadget.trap")) {
				if (args.length > 0) {
					Player trap = Bukkit.getPlayer(args[0]);
					if (trap == null)
						if (args[0].equalsIgnoreCase("*")) {
							Bukkit.broadcastMessage(AQUA + sender.getName() + YELLOW + " trapped everyone in a box!");
							Collection<? extends Player> playersCollection = Bukkit.getOnlinePlayers();
							Player[] players = new Player[playersCollection.size()];
							for (int i = 0; i < players.length; i++)
								players[i] = (Player) playersCollection.toArray()[i];
							new Trap() {
								@Override
								public void release(Player... pl) {
									Bukkit.broadcastMessage(YELLOW + "Everyone has been released!");
								}
							}.trap(players);
						} else
							sender.sendMessage(RED + "Unknown player!");
					else {
						Boolean silent = args.length > 1 && args[1].equalsIgnoreCase("-s");
						new Trap() {
							@Override
							public void release(Player... apl) {
								for (Player pl : apl)
									Bukkit.broadcastMessage(AQUA + pl.getName() + YELLOW + " has been released!");
							}
						}.trap(trap);
						if (!silent)
							Bukkit.broadcastMessage(AQUA + sender.getName() + YELLOW + " trapped " + AQUA + trap.getName() + YELLOW + " in a box!");
					}
				} else
					sender.sendMessage(YELLOW + "Trap a player in an ice box: " + AQUA + "/trap [name]");
			} else
				sender.sendMessage(RED + "You aren't allowed to do this!");
		}
		return true;
	}
}