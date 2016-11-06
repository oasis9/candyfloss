package net.oasis9.aeva.gadgets;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import static org.bukkit.ChatColor.YELLOW;
import static org.bukkit.ChatColor.AQUA;
import static org.bukkit.ChatColor.RED;

public class Gadgets extends JavaPlugin {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("trap")) {
			if (sender.hasPermission("gadget.trap")) {
				if (args.length > 0) {
					Player trap = Bukkit.getPlayer(args[0]);
					if (trap == null)
						sender.sendMessage(RED + "Unknown player!");
					else {
						Boolean silent = args.length > 1 && args[1].equalsIgnoreCase("-s");
						trap(trap, silent);
						if (!silent)
							Bukkit.broadcastMessage(AQUA + sender.getName() + YELLOW + " trapped " + AQUA + trap.getName() + YELLOW + " in an ice box!");
					}
				} else
					sender.sendMessage(YELLOW + "Trap a player in an ice box: " + AQUA + "/trap [name]");
			} else
				sender.sendMessage(RED + "You aren't allowed to do this!");
		}
		return true;
	}
	
	@SuppressWarnings("deprecation")
	public void trap(Player pl, Boolean silent) {
		if (pl == null)
			return;
		Location player = pl.getLocation().getBlock().getLocation();
		int radius = 5;
		for (int pitch = 0; pitch < 90; pitch++)
			for (int lr = 0; lr < 180; lr++) {
				player.setPitch(pitch * 2 - 90);
				player.setYaw(lr * 2);
				for (Player on : Bukkit.getOnlinePlayers())
					on.sendBlockChange(player.clone().add(player.getDirection().multiply(radius)), Material.ICE, (byte) 0);
			}
		new BukkitRunnable() {
			public void run() {
				for (int pitch = 0; pitch < 90; pitch++)
					for (int lr = 0; lr < 180; lr++) {
						player.setPitch(pitch * 2 - 90);
						player.setYaw(lr * 2);
						for (Player on : Bukkit.getOnlinePlayers()) {
							Location l = player.clone().add(player.getDirection().multiply(radius));
							Block b = l.getBlock();
							on.sendBlockChange(l, b.getType(), b.getData());
						}
					}
				if (!silent)
					Bukkit.broadcastMessage(AQUA + pl.getName() + YELLOW + " has been released!");
			}
		}.runTaskLater(this, 20 * 5);
	}
}