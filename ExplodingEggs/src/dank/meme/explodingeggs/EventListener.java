package dank.meme.explodingeggs;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.projectiles.ProjectileSource;

public class EventListener implements Listener {
	
	@EventHandler
	public void projectileThrow(ProjectileLaunchEvent e) {
		Projectile proj = e.getEntity();
		ProjectileSource thrower = proj.getShooter();
		if (proj instanceof Egg && thrower instanceof Player) {
			e.setCancelled(true);
			Player pl = (Player) thrower;
			for (int yaw = 0; yaw < 60; yaw++) {
				Location l = pl.getEyeLocation();
				l.setYaw(yaw * 6);
				l.add(l.getDirection().multiply(1));
				Egg egg = (Egg) l.getWorld().spawnEntity(l, EntityType.EGG);
				egg.setVelocity(l.getDirection());
			}
		}
	}
	
	@EventHandler
	public void projectileHit(ProjectileHitEvent e) {
		Projectile proj = e.getEntity();
		Location l = proj.getLocation();
		if (proj instanceof Egg) {
			World w = l.getWorld();
			w.createExplosion(l, (float) 5);
		}
	}
}