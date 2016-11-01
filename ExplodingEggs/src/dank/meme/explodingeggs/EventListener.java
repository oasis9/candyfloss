package dank.meme.explodingeggs;

import org.bukkit.Location;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class EventListener implements Listener {
	
	@EventHandler
	public void projectileHit(ProjectileHitEvent e) {
		Projectile proj = e.getEntity();
		if (proj instanceof Egg) {
			Location l = proj.getLocation();
			
		}
	}
	
}