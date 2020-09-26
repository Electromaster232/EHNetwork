package ehnetwork.core;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import net.minecraft.server.v1_7_R4.DataWatcher;
import net.minecraft.server.v1_7_R4.EnumEntitySize;
import net.minecraft.server.v1_7_R4.MathHelper;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayOutAttachEntity;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_7_R4.PacketPlayOutSpawnEntity;
import net.minecraft.server.v1_7_R4.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_7_R4.WatchableObject;

import ehnetwork.core.common.DummyEntity;
import ehnetwork.core.common.util.NautHashMap;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.event.CustomTagEvent;
import ehnetwork.core.packethandler.IPacketHandler;
import ehnetwork.core.packethandler.PacketHandler;
import ehnetwork.core.packethandler.PacketInfo;
import ehnetwork.core.packethandler.PacketVerifier;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import fr.neatmonster.nocheatplus.checks.CheckType;
import fr.neatmonster.nocheatplus.checks.access.IViolationInfo;
import fr.neatmonster.nocheatplus.checks.moving.MovingData;
import fr.neatmonster.nocheatplus.hooks.NCPExemptionManager;
import fr.neatmonster.nocheatplus.hooks.NCPHook;
import fr.neatmonster.nocheatplus.hooks.NCPHookManager;

public class CustomTagFix extends MiniPlugin implements IPacketHandler, NCPHook
{
	private NautHashMap<String, NautHashMap<Integer, Integer>> _entityMap = new NautHashMap<String, NautHashMap<Integer, Integer>>();
	private NautHashMap<String, NautHashMap<Integer, String>> _entityNameMap = new NautHashMap<String, NautHashMap<Integer, String>>();
	private NautHashMap<String, NautHashMap<Integer, Integer>> _entityVehicleMap = new NautHashMap<String, NautHashMap<Integer, Integer>>();
	private HashSet<String> _loggedIn = new HashSet<String>();
	private HashSet<Integer> _ignoreSkulls = new HashSet<Integer>();
	
	private NautHashMap<UUID, Long> _exemptTimeMap = new NautHashMap<UUID, Long>();
	private NautHashMap<UUID, NautHashMap<CheckType, Long>> _doubleStrike = new NautHashMap<UUID, NautHashMap<CheckType, Long>>();
	
	private Field _destroyId;
	
	public CustomTagFix(JavaPlugin plugin, PacketHandler packetHandler)
	{
		super("Custom Tag Fix", plugin);

		packetHandler.addPacketHandler(this);
		
		try
		{
			_destroyId = PacketPlayOutEntityDestroy.class.getDeclaredField("a");
			_destroyId.setAccessible(true);
		}
		catch (Exception exception)
		{
			System.out.println("Field exception in CustomTagFix : ");
			exception.printStackTrace();
		}
		
		NCPHookManager.addHook(CheckType.MOVING_SURVIVALFLY, this);
		NCPHookManager.addHook(CheckType.MOVING_PASSABLE, this);
		NCPHookManager.addHook(CheckType.ALL, this);
	}
	
	@EventHandler
	public void playerQuit(PlayerQuitEvent event)
	{
		_entityMap.remove(event.getPlayer().getName());
		_entityNameMap.remove(event.getPlayer().getName());
		_entityVehicleMap.remove(event.getPlayer().getName());
		_loggedIn.remove(event.getPlayer());
	}
	
	@EventHandler
	public void ncpExempt(final PlayerToggleFlightEvent event)
	{
		long ignoreTime = System.currentTimeMillis() + 1500;
		
		if (_exemptTimeMap.containsKey(event.getPlayer().getUniqueId()))
		{
			_exemptTimeMap.put(event.getPlayer().getUniqueId(), Math.max(ignoreTime, _exemptTimeMap.get(event.getPlayer().getUniqueId())));
			return;
		}
		
		try
		{
			NCPExemptionManager.exemptPermanently(event.getPlayer());
		}
		catch (Exception exception)
		{
			
		}
		
		_exemptTimeMap.put(event.getPlayer().getUniqueId(), ignoreTime);
	}
	
	@EventHandler
	public void ncpExemptVelocity(final PlayerVelocityEvent event)
	{
		long ignoreTime = System.currentTimeMillis() + (long)(event.getVelocity().length() * 2000);
		if (_exemptTimeMap.containsKey(event.getPlayer().getUniqueId()))
		{
			_exemptTimeMap.put(event.getPlayer().getUniqueId(), Math.max(ignoreTime, _exemptTimeMap.get(event.getPlayer().getUniqueId())));
			return;
		}
		
		_exemptTimeMap.put(event.getPlayer().getUniqueId(), ignoreTime);
	}
	
	@EventHandler
	public void unexempt(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;
		
		for (Iterator<Entry<UUID, Long>> iterator = _exemptTimeMap.entrySet().iterator(); iterator.hasNext();)
		{
			final Entry<UUID, Long> entry = iterator.next();
			
			if (System.currentTimeMillis() > entry.getValue())
			{
				iterator.remove();
			}
		}
		
		for (Iterator<Entry<UUID, NautHashMap<CheckType, Long>>> iterator = _doubleStrike.entrySet().iterator(); iterator.hasNext();)
		{
			Entry<UUID, NautHashMap<CheckType, Long>> entry = iterator.next();

			for (Iterator<Entry<CheckType, Long>> innerIterator = entry.getValue().entrySet().iterator(); innerIterator.hasNext();)
			{
				final Entry<CheckType, Long> entry2 = innerIterator.next();
				
				if (System.currentTimeMillis() > entry2.getValue())
				{
					innerIterator.remove();
				}
			}
			
			if (entry.getValue() == null || entry.getValue().size() == 0)
				iterator.remove();
		}
	}
	
	@EventHandler
	public void cleanMap(UpdateEvent event)
	{
		if (event.getType() != UpdateType.SEC)
			return;
		
		for (Iterator<String> iterator = _loggedIn.iterator(); iterator.hasNext();)
		{
			String player = iterator.next();
			
			if (Bukkit.getPlayerExact(player) == null)
			{
				iterator.remove();
				_entityMap.remove(player);
				_entityNameMap.remove(player);
				_entityVehicleMap.remove(player);
			}
		}
		
		if (Bukkit.getServer().getOnlinePlayers().size() < _loggedIn.size())
		{
			System.out.println("PROBLEM - _loggedIn TOOOOOO BIIIIIGGGGG.");
		}
	}
	
	@SuppressWarnings("unchecked")
	public void handle(PacketInfo packetInfo)
	{
		if (packetInfo.isCancelled())
			return;
		
		Packet packet = packetInfo.getPacket();
		Player owner = packetInfo.getPlayer();
		PacketVerifier verifier = packetInfo.getVerifier();

		if (owner.isOnline() && UtilPlayer.is1_8(owner))
		{
			if (owner.isOnline() && !_entityMap.containsKey(owner.getName()))
			{
				_entityMap.put(owner.getName(), new NautHashMap<Integer, Integer>());
				_entityNameMap.put(owner.getName(), new NautHashMap<Integer, String>());
				_entityVehicleMap.put(owner.getName(), new NautHashMap<Integer, Integer>());
				_loggedIn.add(owner.getName());
			}
			
			if (packet instanceof PacketPlayOutSpawnEntityLiving)
			{
				PacketPlayOutSpawnEntityLiving spawnPacket = (PacketPlayOutSpawnEntityLiving)packet;

				// Ignore Armor stand packets
				if (spawnPacket.b == 30 || spawnPacket.l == null || spawnPacket.l.c() == null || spawnPacket.a == 777777)
				{
					if (spawnPacket.b == 30)
					{
				        _ignoreSkulls.add(spawnPacket.a);
					}

					return;
				}
				
				for (WatchableObject watchable : (List<WatchableObject>)spawnPacket.l.c())
				{
					if ((watchable.a() == 11 || watchable.a() == 3) && watchable.b() instanceof Byte && ((Byte)watchable.b()) == 1)
					{
						final String entityName = spawnPacket.l.getString(10);
						
						if (entityName.isEmpty())
						{
							_entityNameMap.get(owner.getName()).remove(spawnPacket.a);
							return;
						}
						
						if (_entityMap.get(owner.getName()).containsKey(spawnPacket.a))
						{
							verifier.bypassProcess(new PacketPlayOutEntityDestroy(_entityMap.get(owner.getName()).get(spawnPacket.a)));
						}
						
						int newId = UtilEnt.getNewEntityId();
						sendProtocolPackets(owner, spawnPacket.a, newId, entityName, verifier);
						_entityMap.get(owner.getName()).put(spawnPacket.a, newId);
						_entityNameMap.get(owner.getName()).put(spawnPacket.a, entityName);
						
						break;
					}
				}
			}
			else if (packet instanceof PacketPlayOutEntityMetadata)
			{
				PacketPlayOutEntityMetadata metaPacket = (PacketPlayOutEntityMetadata)packet;
				
				if (!_entityMap.get(owner.getName()).containsKey(metaPacket.a) && metaPacket.a != 777777 && !_ignoreSkulls.contains(metaPacket.a))
				{
					String entityName = "";
					boolean nameShowing = false;
					
					for (WatchableObject watchable : (List<WatchableObject>)metaPacket.b)
					{
						if ((watchable.a() == 11 || watchable.a() == 3) && watchable.b() instanceof Byte && ((Byte)watchable.b()) == 1)
						{
							nameShowing = true;
						}
						if ((watchable.a() == 10 || watchable.a() == 2) && watchable.b() instanceof String)
						{
							entityName = (String)watchable.b();
						}
					}
					
					if (nameShowing && !entityName.isEmpty())
					{
						int newId = UtilEnt.getNewEntityId();
						sendProtocolPackets(owner, metaPacket.a, newId, entityName, verifier);
						_entityMap.get(owner.getName()).put(metaPacket.a, newId);
						_entityNameMap.get(owner.getName()).put(metaPacket.a, entityName);
					}
					else if (!entityName.isEmpty())
					{
						_entityNameMap.get(owner.getName()).remove(metaPacket.a);
					}
				}
			}
			else if (packet instanceof PacketPlayOutEntityDestroy)
			{
				try
				{
					for (int id : (int[])_destroyId.get(packet))
					{
						if (_entityMap.get(owner.getName()).containsKey(id))
						{
							verifier.bypassProcess(new PacketPlayOutEntityDestroy(_entityMap.get(owner.getName()).get(id)));							
							_entityMap.get(owner.getName()).remove(id);
							_entityVehicleMap.get(owner.getName()).remove(id);
							_entityNameMap.get(owner.getName()).remove(id);
						}
					}
				}
				catch (Exception exception)
				{
					exception.printStackTrace();
				}
			} 
			else if (packet instanceof PacketPlayOutSpawnEntity) 
			{
			    PacketPlayOutSpawnEntity spawnPacket = (PacketPlayOutSpawnEntity) packet;
			    if (spawnPacket.j == 66 && spawnPacket.a != 777777)
			    {
			        _ignoreSkulls.add(spawnPacket.a);
			    }
			}
			/*
			else if (packet instanceof PacketPlayOutAttachEntity)
			{
				PacketPlayOutAttachEntity attachPacket = (PacketPlayOutAttachEntity)packet;

				/* TODO dynamic attach handling?
				if (attachPacket.c == -1)
				{
					if (_entityVehicleMap.get(owner).containsKey(attachPacket.b) && _entityNameMap.get(owner).containsKey(_entityVehicleMap.get(owner).get(attachPacket.b)))
					{
						int newId = UtilEnt.getNewEntityId();
						sendProtocolPackets(owner, _entityVehicleMap.get(owner).get(attachPacket.b), newId, _entityNameMap.get(owner).get(_entityVehicleMap.get(owner).get(attachPacket.b)), verifier);
						_entityMap.get(owner).put(attachPacket.b, newId);
						_entityVehicleMap.get(owner).remove(attachPacket.b);
						
						packetInfo.setCancelled(true);
					}
				}
				else
				{
					_entityVehicleMap.get(owner).put(attachPacket.b, attachPacket.c);
				}
				
				if (_entityMap.get(owner).containsKey(attachPacket.c))
				{
					verifier.bypassProcess(new PacketPlayOutEntityDestroy(_entityMap.get(owner).get(attachPacket.c)));
					_entityMap.get(owner).remove(attachPacket.c);
				}
				else
*/
			/*
				//System.out.println(owner.getName() + " id=" + owner.getEntityId() + " recieving AttachPacket b=" + attachPacket.b + " c=" + attachPacket.c);
				if (attachPacket.c == -1 && _entityMap.get(owner).containsKey(attachPacket.b))
				{
					verifier.bypassProcess(new PacketPlayOutEntityDestroy(_entityMap.get(owner).get(attachPacket.b)));
					_entityMap.get(owner).remove(attachPacket.b);
				}				
				else if (attachPacket.c == owner.getEntityId())
				{
					if (_entityMap.get(owner).containsKey(attachPacket.b))
					{
						verifier.bypassProcess(new PacketPlayOutEntityDestroy(_entityMap.get(owner).get(attachPacket.b)));
					}

					PacketPlayOutSpawnEntityLiving armorPacket = new PacketPlayOutSpawnEntityLiving();
					armorPacket.a = UtilEnt.getNewEntityId();
					armorPacket.b = (byte) 30;
					armorPacket.c = (int)EnumEntitySize.SIZE_2.a(100);
					armorPacket.d = (int)MathHelper.floor(64 * 32.0D);
					armorPacket.e = (int)EnumEntitySize.SIZE_2.a(100);
					armorPacket.i = (byte) ((int) (0 * 256.0F / 360.0F));
					armorPacket.j = (byte) ((int) (0 * 256.0F / 360.0F));
					armorPacket.k = (byte) ((int) (0 * 256.0F / 360.0F));

			        double var2 = 3.9D;
			        double var4 = 0;
			        double var6 = 0;
			        double var8 = 0;

			        if (var4 < -var2)
			        {
			            var4 = -var2;
			        }

			        if (var6 < -var2)
			        {
			            var6 = -var2;
			        }

			        if (var8 < -var2)
			        {
			            var8 = -var2;
			        }

			        if (var4 > var2)
			        {
			            var4 = var2;
			        }

			        if (var6 > var2)
			        {
			            var6 = var2;
			        }

			        if (var8 > var2)
			        {
			            var8 = var2;
			        }

			        armorPacket.f = (int)(var4 * 8000.0D);
			        armorPacket.g = (int)(var6 * 8000.0D);
			        armorPacket.h = (int)(var8 * 8000.0D);
					
					final DataWatcher watcher = new DataWatcher(new DummyEntity(((CraftWorld)owner.getWorld()).getHandle()));
					
					watcher.a(0, Byte.valueOf((byte)0));
					watcher.a(1, Short.valueOf((short)300));
					watcher.a(2, "");
					watcher.a(3, Byte.valueOf((byte) 0));
					watcher.a(4, Byte.valueOf((byte)0));
					watcher.a(7, Integer.valueOf(0));
					watcher.a(8, Byte.valueOf((byte)0));
					watcher.a(9, Byte.valueOf((byte)0));
					watcher.a(6, Float.valueOf(1.0F));
					watcher.a(10, Byte.valueOf((byte)0));
					
					// Set invisible
				    int i1 = watcher.getInt(0);
				    watcher.watch(0, Byte.valueOf((byte)(i1 | 1 << 5)));
				    	
				    // Set small
			        byte b1 = watcher.getByte(10);
			        b1 = (byte)(b1 | 0x1);
			        
			        watcher.watch(10, Byte.valueOf(b1));
					
					armorPacket.l = watcher;
					
					// Spawn armor packet
					verifier.bypassProcess(armorPacket);					
					
					PacketPlayOutAttachEntity attachPacket2 = new PacketPlayOutAttachEntity();
					attachPacket2.c = owner.getEntityId();
					attachPacket2.b = armorPacket.a;
					attachPacket2.a = 0;
					
					// Send armor attach to player.
					verifier.bypassProcess(attachPacket2);
					
					// Change original packet to attach to armor stand
					attachPacket.c = armorPacket.a;
					
					_entityMap.get(owner).put(attachPacket.b, armorPacket.a);
				}
			}
		*/
		}
	}
	
	private void sendProtocolPackets(final Player owner, final int entityId, final int newEntityId, String entityName, final PacketVerifier packetList)
	{
		CustomTagEvent event = new CustomTagEvent(owner, entityId, entityName);
		_plugin.getServer().getPluginManager().callEvent(event);
		final String finalEntityName = event.getCustomName();

		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(getPlugin(), new Runnable()
		{
			public void run()
			{
				final PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving();
				packet.a = newEntityId;
				packet.b = (byte) 30;
				packet.c = (int)EnumEntitySize.SIZE_2.a(100);
				packet.d = (int)MathHelper.floor(64 * 32.0D);
				packet.e = (int)EnumEntitySize.SIZE_2.a(100);
				packet.i = (byte) ((int) (0 * 256.0F / 360.0F));
				packet.j = (byte) ((int) (0 * 256.0F / 360.0F));
				packet.k = (byte) ((int) (0 * 256.0F / 360.0F));

		        double var2 = 3.9D;
		        double var4 = 0;
		        double var6 = 0;
		        double var8 = 0;

		        if (var4 < -var2)
		        {
		            var4 = -var2;
		        }

		        if (var6 < -var2)
		        {
		            var6 = -var2;
		        }

		        if (var8 < -var2)
		        {
		            var8 = -var2;
		        }

		        if (var4 > var2)
		        {
		            var4 = var2;
		        }

		        if (var6 > var2)
		        {
		            var6 = var2;
		        }

		        if (var8 > var2)
		        {
		            var8 = var2;
		        }

		        packet.f = (int)(var4 * 8000.0D);
		        packet.g = (int)(var6 * 8000.0D);
		        packet.h = (int)(var8 * 8000.0D);
				
				final DataWatcher watcher = new DataWatcher(new DummyEntity(((CraftWorld)owner.getWorld()).getHandle()));
				
				watcher.a(0, Byte.valueOf((byte)0));
				watcher.a(1, Short.valueOf((short)300));
				watcher.a(2, "");
				watcher.a(3, Byte.valueOf((byte) 0));
				watcher.a(4, Byte.valueOf((byte)0));
				watcher.a(7, Integer.valueOf(0));
				watcher.a(8, Byte.valueOf((byte)0));
				watcher.a(9, Byte.valueOf((byte)0));
				watcher.a(6, Float.valueOf(1.0F));
				watcher.a(10, Byte.valueOf((byte)0));
				
				// Set invisible
			    int i1 = watcher.getInt(0);
			    watcher.watch(0, Byte.valueOf((byte)(i1 | 1 << 5)));
			    	
			    // Set small
		        byte b1 = watcher.getByte(10);
		        b1 = (byte)(b1 | 0x1);
		        
		        watcher.watch(10, Byte.valueOf(b1));
			    
				watcher.watch(2, finalEntityName);
				watcher.watch(3, Byte.valueOf((byte)1));
				
				packet.l = watcher;
				
				packetList.bypassProcess(packet);
				
		        PacketPlayOutAttachEntity vehiclePacket = new PacketPlayOutAttachEntity();
		        vehiclePacket.a = 0;
		        vehiclePacket.b = packet.a;
		        vehiclePacket.c = entityId;
		        
				packetList.bypassProcess(vehiclePacket);
				
				/* TODO dynamic attach handling?
				if (_entityVehicleMap.get(owner).containsValue(entityId))
				{
			        vehiclePacket = new PacketPlayOutAttachEntity();
			        vehiclePacket.a = 0;
			        
			        for (Entry<Integer, Integer> entry : _entityVehicleMap.get(owner).entrySet())
			        {
			        	if (entry.getValue() == entityId)
			        		vehiclePacket.b = entry.getKey();
			        }

			        vehiclePacket.c = packet.a;
				}
				*/
			}
		});
	}

	@Override
	public String getHookName()
	{
		return "Mineplex Hook";
	}

	@Override
	public String getHookVersion()
	{
		return "Latest";
	}

	@Override
	public boolean onCheckFailure(CheckType checkType, Player player, IViolationInfo violationInfo)
	{
		boolean failure = false;
		
		if (checkType == CheckType.MOVING_SURVIVALFLY || checkType == CheckType.MOVING_PASSABLE)
		{			
			failure = _exemptTimeMap.containsKey(player.getUniqueId());
			
			if (failure)
				MovingData.getData(player).clearFlyData();
		}
		
		// This is the second strike system.
		if (!failure)
		{
			if (!_doubleStrike.containsKey(player.getUniqueId()) || !_doubleStrike.get(player.getUniqueId()).containsKey(checkType.getParent()))
				failure = true;

			if (!_doubleStrike.containsKey(player.getUniqueId()))
				_doubleStrike.put(player.getUniqueId(), new NautHashMap<CheckType, Long>());
			
			_doubleStrike.get(player.getUniqueId()).put(checkType.getParent(), System.currentTimeMillis() + 5000);
		}
		
		return failure;
	}
}

