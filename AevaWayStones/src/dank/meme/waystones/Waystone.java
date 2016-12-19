package dank.meme.waystones;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;

public class Waystone {
	
	Location loc;
	WaystoneType type;
	List<UUID> found = new ArrayList<UUID>();
	
	public Waystone(Location loc, WaystoneType type) {
		this.loc = loc;
		this.type = type;
	}
}