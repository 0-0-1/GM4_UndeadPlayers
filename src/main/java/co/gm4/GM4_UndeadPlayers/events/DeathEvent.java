package co.gm4.GM4_UndeadPlayers.events;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import co.gm4.GM4_UndeadPlayers.GM4_UndeadPlayers;

public class DeathEvent implements Listener {

	private GM4_UndeadPlayers plugin;
	
	public DeathEvent(GM4_UndeadPlayers plugin)
	{
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e)
	{
		Player player = e.getEntity();
		Location deathLocation = player.getLocation();
		World deathWorld = player.getWorld();
		
		Entity entity = deathWorld.spawnEntity(deathLocation, EntityType.ZOMBIE);
		
		boolean entityIsZombie = entity instanceof Zombie;
		assert entityIsZombie;
		
		Zombie zombie = (Zombie) entity;
		
		zombie.setCustomName("Undead Player");
		zombie.setCustomNameVisible(true);
		
		zombie.setBaby(false);
		zombie.setVillagerProfession(Villager.Profession.NORMAL);
		
		zombie.setRemoveWhenFarAway(false);
	}
}
