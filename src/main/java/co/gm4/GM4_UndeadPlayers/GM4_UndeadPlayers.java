package co.gm4.GM4_UndeadPlayers;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import co.gm4.GM4_UndeadPlayers.events.DeathEvent;

/**
 * Project: GM4_UndeadPlayers
 * Author: OSX
 * Date: Jul 04 2016
 * Website (of GM4): http://gm4.co
 */

public class GM4_UndeadPlayers extends JavaPlugin {
	
	public Class<?> iChatThing;
	public Class<?> chatSerializer;
	public Class<?> packetChatThing;
	public Class<?> packetClass;
	
	private Method chatSerializer_a;
	private Constructor<?> packetChatThing_;
	
	public String zombieName = "Undead Player";
	public boolean canPickUpItems = true;
	public boolean nameTagVisible = true;
	public boolean canDespawn = false;
	public List<String> accomplishedPlayers;
	
	@Override
	public void onEnable()
	{
		String version = Bukkit.getServer().getClass().getName().replace(".", ",").split(",")[3] + ".";
		String NMS_V = "net.minecraft.server." + version;
		
		String iChatString = NMS_V + "IChatBaseComponent";
		String chatSerializerString = NMS_V + "IChatBaseComponent$ChatSerializer";
		String packetChatString = NMS_V + "PacketPlayOutChat";
		String packetClassString = NMS_V + "Packet";
		
		try {
			this.iChatThing = Class.forName(iChatString);
			this.chatSerializer = Class.forName(chatSerializerString);
			this.packetChatThing = Class.forName(packetChatString);
			this.packetClass = Class.forName(packetClassString);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			
			Bukkit.getPluginManager().disablePlugin(this);
			
			return;
		}
		
		this.saveDefaultConfig();
		
		this.zombieName = this.getConfig().getString("zombieName");
		this.canPickUpItems = this.getConfig().getBoolean("canPickUpItems");
		this.nameTagVisible = this.getConfig().getBoolean("nameTagVisible");
		this.canDespawn = this.getConfig().getBoolean("canDespawn");
		this.accomplishedPlayers = this.getConfig().getStringList("accomplishedPlayers");
		
		this.getConfig().set("zombieName", this.zombieName);
		this.getConfig().set("canPickUpItems", this.canPickUpItems);
		this.getConfig().set("nameTagVisible", this.nameTagVisible);
		this.getConfig().set("canDespawn", this.canDespawn);
		this.getConfig().set("accomplishedPlayers", this.accomplishedPlayers);
		
		this.saveConfig();
		
		try {
			this.chatSerializer_a = this.chatSerializer.getMethod("a", String.class);
			this.packetChatThing_ = this.packetChatThing.getConstructor(iChatThing, byte.class);
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			
			Bukkit.getPluginManager().disablePlugin(this);
			
			return;
		}
		
		new DeathEvent(this);
	}
	
	@Override
	public void onDisable()
	{
		
	}
	
	public Object getPlayerConnection(Player player)
	{
		Method getHandle;
		try {
			getHandle = player.getClass().getMethod("getHandle");
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			
			Bukkit.getPluginManager().disablePlugin(this);
			
			return null;
		}
		
	    Object nmsPlayer;
		try {
			nmsPlayer = getHandle.invoke(player);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			
			Bukkit.getPluginManager().disablePlugin(this);
			
			return null;
		}
		
	    Field conField;
		try {
			conField = nmsPlayer.getClass().getField("playerConnection");
		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
			
			Bukkit.getPluginManager().disablePlugin(this);
			
			return null;
		}
		
		Object con;
		
	    try {
	    	con = conField.get(nmsPlayer);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			
			Bukkit.getPluginManager().disablePlugin(this);
			
			return null;
		}
	    
		return con;
	}
	
	public Object makeTellraw(String json)
	{
		Object tellraw;
		
		try {
			tellraw = chatSerializer_a.invoke(null, json);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			
			Bukkit.getPluginManager().disablePlugin(this);
			
			return null;
		}
		
		if(this.iChatThing.isInstance(tellraw))
			return tellraw;
		else
			return null;
	}
	
	public Object makePacketPlayOutChat(Object tellraw, byte arg1)
	{
		Object packetPlayOutChat;
		
		try {
			packetPlayOutChat = packetChatThing_.newInstance(tellraw, arg1);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
			
			Bukkit.getPluginManager().disablePlugin(this);
			
			return null;
		}
		
		if(packetChatThing.isInstance(packetPlayOutChat))
			return packetPlayOutChat;
		else
			return null;
	}
	
	public void sendPacket(Object connection, Object packet)
	{
		Method sendPacketMethod;
		
		try {
			sendPacketMethod = connection.getClass().getMethod("sendPacket", this.packetClass);
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			
			Bukkit.getPluginManager().disablePlugin(this);
			
			return;
		}
		
		try {
			sendPacketMethod.invoke(connection, packet);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			
			Bukkit.getPluginManager().disablePlugin(this);
			
			return;
		}
	}
}
