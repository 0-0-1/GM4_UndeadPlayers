package co.gm4.GM4_UndeadPlayers.events;

import org.bukkit.Bukkit;
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
		
		String uuid = player.getUniqueId().toString();
		if(!(plugin.accomplishedPlayers.contains(uuid)))
		{
			plugin.accomplishedPlayers.add(uuid);
			plugin.saveConfig();
			
			Player[] players_ = new Player[1];
			Player[] players = Bukkit.getOnlinePlayers().toArray(players_);
			
			for(int i = 0; i < players.length; i++)
			{
				Object connection = plugin.getPlayerConnection(players[i]);
				if(connection.equals(null))
					return;
				
				Object tellraw = plugin.makeTellraw("{\"translate\":\"chat.type.achievement\",\"with\":[{\"text\":\"" + player.getDisplayName() + "\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"" + player.getDisplayName() + "\n" + uuid + "\"}},{\"text\":\"[Risen]\",\"color\":\"dark_aqua\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"Risen\n\",\"color\":\"dark_aqua\"},{\"italic\":\"true\",\"text\":\"Achievement\n\",\"color\":\"white\"},{\"text\":\"Die and raise up an undead\nzombie in your body's place.\",\"color\":\"white\"}]}}}]}");
				if(tellraw.equals(null))
					return;
				
				Object packet = plugin.makePacketPlayOutChat(tellraw, (byte) 1);
				if(packet.equals(null))
					return;
				
				plugin.sendPacket(connection, packet);
			}
			
		}
		
		Entity entity = deathWorld.spawnEntity(deathLocation, EntityType.ZOMBIE);
		
		boolean entityIsZombie = entity instanceof Zombie;
		assert entityIsZombie;
		
		Zombie zombie = (Zombie) entity;
		
		zombie.setCustomName(plugin.zombieName);
		zombie.setCustomNameVisible(plugin.nameTagVisible);
		zombie.setRemoveWhenFarAway(plugin.canDespawn);
		zombie.setCanPickupItems(plugin.canPickUpItems);
		
		zombie.setBaby(false);
		zombie.setVillagerProfession(Villager.Profession.NORMAL);
	}
}
