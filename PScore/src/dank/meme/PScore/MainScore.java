package dank.meme.PScore;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class MainScore extends JavaPlugin implements Listener {
	
	public static final Player sender = null;
	
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, this);
	}
	
	@EventHandler
	public void playerJoin(PlayerJoinEvent e) {
		scoreboard(e.getPlayer());
	}
	
	public void scoreboard(Player pl) {
		ScoreboardManager manager =  Bukkit.getScoreboardManager();
		Scoreboard main = manager.getNewScoreboard();
		
		Objective objective = main.registerNewObjective("main1", "dummy");
		objective.setDisplayName(ChatColor.RED + "Cirrent Server");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		/* Possible team usage not needed for simple scoreboards
		 Team team = main.registerNewTeam("team");
		*/
		 
		int a = Bukkit.getViewDistance(); 
		int b = Bukkit.getMaxPlayers(); 
			
		Score score = objective.getScore(ChatColor.RED + "Viewable Chunks:");
		score.setScore(a);
		
		Score score1 = objective.getScore(ChatColor.GREEN + "Max Players");
		score1.setScore(b);
		
		Score score2 = objective.getScore(ChatColor.AQUA + pl.getName());
		score2.setScore(0);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)){
			sender.sendMessage("Could not disable Scoreboard");
			return true;
		}
		Player pl = (Player) sender;
		if (Bukkit.getName().equalsIgnoreCase("psdisable")){
			pl.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
			pl.sendMessage(ChatColor.RED + "Scoreboard Disabled");
		
		}
		return true;
	}		
}


