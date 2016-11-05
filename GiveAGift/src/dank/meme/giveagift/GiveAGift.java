package dank.meme.giveagift;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

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
			l.setPitch(0);
			l.add(l.getDirection().multiply(2));
			l.setYaw(l.getYaw() + 180 % 360);
			ArmorStand as = (ArmorStand) l.getWorld().spawnEntity(l, EntityType.ARMOR_STAND);
			as.setVisible(false);
			as.setGravity(false);
			as.setHelmet(new ItemStack(new Random().nextBoolean() && new Random().nextBoolean() ? Material.ENDER_CHEST : Material.TNT));
			new BukkitRunnable() {
				int i = 0;
				@Override
				public void run() {
					Location hlx = l.clone();
					double x = 4;
					double xpow = Math.pow(x, 2);
					hlx.add(0, 1.5 + (1d / (360d / xpow) * i), 0);
					hlx.setYaw((float) (i * xpow % 360));
					hlx.add(hlx.getDirection().multiply(0.5));
					if (i > 360 / xpow) {
						if (as.getHelmet().getType().equals(Material.ENDER_CHEST)) {
							Firework fw = (Firework) l.getWorld().spawnEntity(l.clone().add(0, 2, 0), EntityType.FIREWORK);
			            	FireworkMeta fwm = fw.getFireworkMeta();
			            	FireworkEffect effect = FireworkEffect.builder().withColor(Color.WHITE).withColor(Color.RED).withColor(Color.GREEN).withFade(Color.GRAY).with(Type.STAR).trail(true).flicker(true).build();
			            	fwm.addEffect(effect);
			            	fw.setFireworkMeta(fwm);
				            new BukkitRunnable() {
				            	@Override
				            	public void run() {
				            		fw.detonate();
				            		as.remove();
				            	}
				            }.runTaskLater(getPlugin(GiveAGift.class), 1);
						} else
				            new BukkitRunnable() {
			            	@Override
			            	public void run() {
			            		as.getWorld().createExplosion(as.getLocation(), (float) 5);
			            		as.remove();
			            	}
			            }.runTaskLater(getPlugin(GiveAGift.class), 1);
						cancel();
						return;
					}
					Location opp = hlx.clone();
					opp.subtract(hlx.getDirection());
					PacketPlayOutWorldParticles helix1 = new PacketPlayOutWorldParticles(EnumParticle.FIREWORKS_SPARK, true, (float) hlx.getX(), (float) hlx.getY(), (float) hlx.getZ(), 0, 0, 0, 0, 1);
					PacketPlayOutWorldParticles helix2 = new PacketPlayOutWorldParticles(EnumParticle.FIREWORKS_SPARK, true, (float) opp.getX(), (float) opp.getY(), (float) opp.getZ(), 0, 0, 0, 0, 1);
					for (Player p : Bukkit.getOnlinePlayers()) {
						PlayerConnection pc = ((CraftPlayer) p).getHandle().playerConnection;
						pc.sendPacket(helix1);
						pc.sendPacket(helix2);
					}
					i++;
				}
			}.runTaskTimer(this, 10, 1);
		}
		return true;
	}
	
}