package dank.meme.explodingeggs;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class ExplodingEggs extends JavaPlugin {
	
	@Override
	public void onEnable() {
		Bukkit.getLogger().info(getName() + " has been enabled!");
		Bukkit.getPluginManager().registerEvents(new EventListener(), this);
	}
}