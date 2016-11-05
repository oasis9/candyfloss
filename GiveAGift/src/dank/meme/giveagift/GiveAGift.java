package dank.meme.giveagift;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import net.minecraft.server.v1_9_R1.EnumParticle;
import net.minecraft.server.v1_9_R1.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_9_R1.PlayerConnection;

public class GiveAGift extends JavaPlugin {
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("nah fam");
			return true;
		}
		Player pl = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("gift")) {
			Location l = pl.getLocation();
			l.add(l.getDirection().multiply(2));
			l.setYaw(l.getYaw() + 180 % 360);
			ArmorStand as = (ArmorStand) l.getWorld().spawnEntity(l, EntityType.ARMOR_STAND);
			as.setVisible(false);
			as.setHelmet(new ItemStack(Material.ENDER_CHEST));
			new BukkitRunnable() {
				Location pll = pl.getEyeLocation();
				Location l = new Location(pll.getWorld(), pll.getX(), pll.getY(), pll.getZ());
				@Override
				public void run() {
					double y = l.getY() + (1 / 360 / 2);
					if (y >= pll.getBlockY() + 1)
						cancel();
					l.setY(y);
					l.setYaw(l.getYaw() + (360 / 2));
					Vector v = l.getDirection();
					l.add(v.multiply(0.5));
					Location opp = l.clone();
					opp.subtract(v);
					PacketPlayOutWorldParticles helix1 = new PacketPlayOutWorldParticles(EnumParticle.FIREWORKS_SPARK, true, (float) l.getX(), (float) l.getY(), (float) l.getZ(), 0, 0, 0, 0, 1);
					PacketPlayOutWorldParticles helix2 = new PacketPlayOutWorldParticles(EnumParticle.FIREWORKS_SPARK, true, (float) opp.getX(), (float) opp.getY(), (float) opp.getZ(), 0, 0, 0, 0, 1);
					PlayerConnection pc = ((CraftPlayer) pl).getHandle().playerConnection;
					pc.sendPacket(helix1);
					pc.sendPacket(helix2);
				}
			}.runTaskTimer(this, 20 / 2, 20 / 10);
		}
		return true;
	}
	
}