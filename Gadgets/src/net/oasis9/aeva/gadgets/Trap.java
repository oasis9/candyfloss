package net.oasis9.aeva.gadgets;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

@SuppressWarnings("deprecation")
public abstract class Trap {
	
	Player[] apl;
	Map<UUID, Location> locs = new HashMap<UUID, Location>();
	int radius = 5;
	int detail = 10;
	
	abstract void release(Player... apl);
	public void trap(Player... apl) {
		if (apl == null || apl.length <= 0)
			return;
		this.apl = apl;
		for (Player pl : apl) {
			if (pl == null)
				continue;
			Location loc = pl.getLocation().getBlock().getLocation();
			locs.put(pl.getUniqueId(), loc);
			double dPitch = 180 / detail;
			double dYaw = 360 / detail;
			for (int pitch = 0; pitch < dPitch; pitch++)
				for (int yaw = 0; yaw < dYaw; yaw++) {
					loc.setPitch((int) ((double) pitch * detail - 90));
					loc.setYaw((int) ((double) yaw * detail));
					for (Player on : Bukkit.getOnlinePlayers())
						on.sendBlockChange(loc.clone().add(loc.clone().getDirection().multiply(radius)), Material.STAINED_GLASS, (byte) 15);
				}
		}
		new BukkitRunnable() {
			public void run() {
				prerelease();
			}
		}.runTaskLater(Gadgets.getPlugin(Gadgets.class), 20 * 5);
	}
	
	private void prerelease() {
		for (Player pl : apl) {
			if (pl == null)
				continue;
			UUID uuid = pl.getUniqueId();
			Location loc = locs.get(uuid);
			locs.remove(uuid);
			double dPitch = 180 / detail;
			double dYaw = 360 / detail;
			for (int pitch = 0; pitch < dPitch; pitch++)
				for (int yaw = 0; yaw < dYaw; yaw++) {
					loc.setPitch((int) ((double) pitch * detail - 90));
					loc.setYaw((int) ((double) yaw * detail));
					for (Player on : Bukkit.getOnlinePlayers())
						on.sendBlockChange(loc.clone().add(loc.getDirection().multiply(radius)), Material.AIR, (byte) 0);
			}
		}
		release(apl);
	}
}