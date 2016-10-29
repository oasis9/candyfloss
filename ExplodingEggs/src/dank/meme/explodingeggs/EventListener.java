package dank.meme.explodingeggs;

import static org.bukkit.ChatColor.RED;
import static org.bukkit.ChatColor.YELLOW;

import org.bukkit.Location;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;

public class EventListener implements Listener {
	
	@EventHandler
	public void playerEggBreak(PlayerEggThrowEvent e) {
		Player pl = e.getPlayer();
		pl.sendMessage(YELLOW + "You threw an egg!");
	}
	
	@EventHandler
	public void projectileHit(ProjectileHitEvent e) {
		Projectile proj = e.getEntity();
		if (proj instanceof Egg) {
			Location l = proj.getLocation();
			l.getWorld().createExplosion(l, (float) 5);
			Entity entity = (Entity) proj.getShooter();
			if (entity instanceof Player)
				((Player) entity).sendMessage(RED + "BOOM!");
		}
	}
	
}