package dank.meme.waystones;

import org.bukkit.Location;

public class Waystone {
	
	Location loc;
	WaystoneType type;
	
	public Waystone(Location loc, WaystoneType type) {
		this.loc = loc;
		this.type = type;
	}
}