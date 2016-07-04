package co.gm4.GM4_UndeadPlayers;

import org.bukkit.plugin.java.JavaPlugin;

import co.gm4.GM4_UndeadPlayers.events.DeathEvent;

/**
 * Project: GM4_UndeadPlayers
 * Author: OSX
 * Date: Jul 04 2016
 * Website (of GM4): http://gm4.co
 */

public class GM4_UndeadPlayers extends JavaPlugin {
	
	@Override
	public void onEnable()
	{
		new DeathEvent(this);
	}
	
	@Override
	public void onDisable()
	{
		
	}
	
}
