package dank.meme.explodingeggs;

import java.util.ArrayList;
import java.util.List;

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
	
	List<Egg> original = new ArrayList<Egg>();
	List<Egg> secondary = new ArrayList<Egg>();
	
	@EventHandler
	public void projectileThrow(ProjectileLaunchEvent e) {
		Projectile proj = e.getEntity();
		ProjectileSource thrower = proj.getShooter();
		if (proj instanceof Egg && thrower instanceof Player) {
			/*e.setCancelled(true);
			Player pl = (Player) thrower;*/
			Egg egg = (Egg) proj;
			original.add(egg);
			/*int d = 36;
			for (int yaw = 0; yaw < 360 / d; yaw++) {
				Location l = pl.getEyeLocation();
				l.setYaw(yaw * d);
				l.add(l.getDirection().multiply(1));
				Egg egg = (Egg) l.getWorld().spawnEntity(l, EntityType.EGG);
				egg.setVelocity(l.getDirection());
			}*/
		}
	}
	
	@EventHandler
	public void projectileHit(ProjectileHitEvent e) {
		Projectile proj = e.getEntity();
		Location l = proj.getLocation();
		if (proj instanceof Egg) {
			Egg egg = (Egg) proj;
			World w = l.getWorld();
			if (original.contains(egg)) {
				original.remove(egg);
				int d = 6;
				for (int yaw = 0; yaw < 360; yaw += 360 / d) {
					l.setYaw(yaw);
					l.add(l.getDirection().multiply(1));
					Egg newegg = (Egg) l.getWorld().spawnEntity(l, EntityType.EGG);
					secondary.add(newegg);
					newegg.setVelocity(l.getDirection());
				}
			} else if (secondary.contains(egg)) {
				secondary.remove(egg);
				int d = 4;
				for (int yaw = 0; yaw < 360; yaw += 360 / d) {
					l.setYaw(yaw);
					l.add(l.getDirection().multiply(1));
					Egg newegg = (Egg) l.getWorld().spawnEntity(l, EntityType.EGG);
					newegg.setVelocity(l.getDirection());
				}
			} else
				w.createExplosion(l, (float) 5);
		}
	}
}