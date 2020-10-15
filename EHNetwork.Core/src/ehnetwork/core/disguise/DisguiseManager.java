package ehnetwork.core.disguise;


import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import de.robingrether.idisguise.api.DisguiseAPI;
import de.robingrether.idisguise.disguise.Disguise;
import de.robingrether.idisguise.disguise.DisguiseType;
import de.robingrether.idisguise.disguise.MobDisguise;
import ehnetwork.core.MiniPlugin;
import ehnetwork.core.common.util.NautHashMap;


public class DisguiseManager extends MiniPlugin
{
	private NautHashMap<Entity, Object> disguiseList = new NautHashMap<>();
	private DisguiseAPI api;

	public DisguiseManager(JavaPlugin plugin)
	{
		super("Disguise Manager", plugin);
		api = plugin.getServer().getServicesManager().getRegistration(DisguiseAPI.class).getProvider();

	}

	public Disguise createDisguise(EntityType disguiseType)
	{
		return DisguiseType.fromString(disguiseType.toString()).newInstance();
		//r1.setEntity(disguised);
	}

/*
	public void disguise(String playerName, Player... players)
	{

		PlayerDisguise disguise = new PlayerDisguise(playerName);
		for (Player p1 : players){
			disguise.addPlayer(p1);
		}
		disguiseList.put(players[0], disguise);
		disguise.startDisguise();
	}

 */

	public void applyDisguise(Disguise disguise, Entity entity){

		api.disguise((LivingEntity) entity, disguise);
		disguiseList.put(entity, disguise);
	}

	public Disguise getDisguise(Entity entity){
		if(disguiseList.containsKey(entity))
			return (MobDisguise) disguiseList.get(entity);
		else
			return null;
	}


	public void undisguise(Entity player){
		MobDisguise d1 = (MobDisguise) disguiseList.get(player);
		if(d1 != null)
		{
			api.undisguise((LivingEntity) player);
			disguiseList.remove(player);
		}
	}

	public DisguiseAPI getApi(){return api;}

	public void clearDisguises(){
		for(Entity p : disguiseList.keySet()){
			api.undisguise((LivingEntity) p);
		}
	}

}

   // This is the old Mplex disguise system. You have been R E P L A C E D!
		/* try
		{
			_attributesA = PacketPlayOutUpdateAttributes.class.getDeclaredField("a");
			_attributesA.setAccessible(true);
			_attributesB = PacketPlayOutUpdateAttributes.class.getDeclaredField("b");
			_attributesB.setAccessible(true);
			_soundB = PacketPlayOutNamedSoundEffect.class.getDeclaredField("b");
			_soundB.setAccessible(true);
			_soundC = PacketPlayOutNamedSoundEffect.class.getDeclaredField("c");
			_soundC.setAccessible(true);
			_soundD = PacketPlayOutNamedSoundEffect.class.getDeclaredField("d");
			_soundD.setAccessible(true);
			_bedA = PacketPlayOutBed.class.getDeclaredField("a");
			_bedA.setAccessible(true);
			_bedB = PacketPlayOutBed.class.getDeclaredField("b");
			_bedB.setAccessible(true);
			_bedD = PacketPlayOutBed.class.getDeclaredField("d");
			_bedD.setAccessible(true);
			_eStatusId = PacketPlayOutEntityStatus.class.getDeclaredField("a");
			_eStatusId.setAccessible(true);
			_eStatusState = PacketPlayOutEntityStatus.class.getDeclaredField("b");
			_eStatusState.setAccessible(true);

			_bedChunk = new Chunk(((CraftWorld) Bukkit.getWorlds().get(0)).getHandle(), 0, 0);
			Field cSection = Chunk.class.getDeclaredField("sections");
			cSection.setAccessible(true);

			ChunkSection chunkSection = new ChunkSection(0, false);
			Block block = Block.getById(Material.BED_BLOCK.getId());

			// block = ((Object[]) ReflectionManager.getNmsField(ReflectionManager.getNmsClass("Block"),"byId")
			// .get(null))[Material.BED_BLOCK.getId()];

			for (BlockFace face : new BlockFace[]
				{
						BlockFace.EAST, BlockFace.WEST, BlockFace.NORTH, BlockFace.SOUTH
				})
			{
				chunkSection.setType(1 + face.getModX(), 0, 1 + face.getModZ(), block.getBlockData());
				chunkSection.setData(1 + face.getModX(), 0, 1 + face.getModZ(), block.getBlockData());
				//chunkSection.setSkyLight(1 + face.getModX(), 0, 1 + face.getModZ(), 0);
				//chunkSection.setEmittedLight(1 + face.getModX(), 0, 1 + face.getModZ(), 0);
			}

			ChunkSection[] chunkSections = new ChunkSection[16];
			chunkSections[0] = chunkSection;
			cSection.set(_bedChunk, chunkSections);

			//_bedChunk.world = ;

			_xChunk = Chunk.class.getField("locX");
			_xChunk.setAccessible(true);

			_zChunk = Chunk.class.getField("locZ");
			_zChunk.setAccessible(true);
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		catch (NoSuchFieldException e)
		{
			e.printStackTrace();
		}
		catch (SecurityException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
	}

	public void addFutureDisguise(DisguiseBase disguise, Player... players)
	{
		final int entityId = UtilEnt.getNewEntityId(false);

		_futureDisguises.put(entityId, new HashMap.SimpleEntry(disguise, players));

		Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), new Runnable()
		{
			public void run()
			{
				if (_futureDisguises.containsKey(entityId))
				{
					Entry<DisguiseBase, Player[]> entry = _futureDisguises.remove(entityId);

					Entity entity = UtilEnt.getEntityById(entityId);

					if (entity != null)
					{
						entry.getKey().setEntity(entity);

						disguise(entry.getKey(), entry.getValue());
					}
				}
			}
		}, 4);
	}

	public void addViewerToDisguise(DisguiseBase disguise, Player player, boolean reapply)
	{
		_disguisePlayerMap.get(disguise).add(player);

		if (reapply)
			refreshTrackers(disguise.GetEntity(), new Player[]
				{
					player
				});
	}

	@EventHandler
	public void ChunkAddEntity(ChunkAddEntityEvent event)
	{
		DisguiseBase disguise = _entityDisguiseMap.get(event.GetEntity().getUniqueId().toString());

		if (disguise != null)
		{
			disguise.setEntity(event.GetEntity());
			_spawnPacketMap.put(event.GetEntity().getEntityId(), disguise);
			_entityDisguiseMap.remove(event.GetEntity().getUniqueId().toString());

			if (disguise instanceof DisguiseRabbit)
			{
				_lastRabbitHop.put(disguise.GetEntityId(), new NautHashMap<Integer, Long>());
			}
		}
	}

	@EventHandler
	public void chunkJoin(PlayerJoinEvent event)
	{
		if (!_bedPackets)
			return;

		chunkMove(event.getPlayer(), event.getPlayer().getLocation(), null);
	}

	private void chunkMove(Player player, Location newLoc, Location oldLoc)
	{
		for (Packet packet : getChunkMovePackets(player, newLoc, oldLoc))
		{
			UtilPlayer.sendPacket(player, packet);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void chunkMove(PlayerMoveEvent event)
	{
		if (!_bedPackets)
			return;

		Location to = event.getTo();
		Location from = event.getFrom();

		int x1 = getChunk(to.getX());
		int z1 = getChunk(to.getZ());
		int x2 = getChunk(from.getX());
		int z2 = getChunk(from.getZ());

		if (x1 != x2 || z1 != z2)
		{
			chunkMove(event.getPlayer(), to, from);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void chunkTeleport(PlayerTeleportEvent event)
	{
		if (!_bedPackets)
			return;

		Location to = event.getTo();
		Location from = event.getFrom();

		if (to.getWorld() == from.getWorld())
		{
			int x1 = getChunk(to.getX());
			int z1 = getChunk(to.getZ());

			int x2 = getChunk(from.getX());
			int z2 = getChunk(from.getZ());

			if (x1 != x2 || z1 != z2)
			{
				final Player player = event.getPlayer();
				final Location prev = new Location(to.getWorld(), x1, 0, z1);

				chunkMove(player, null, from);

				Bukkit.getScheduler().scheduleSyncDelayedTask(_plugin, new Runnable()
				{
					public void run()
					{
						if (!player.isOnline())
						{
							return;
						}

						Location loc = player.getLocation();

						if (player.getWorld() != loc.getWorld())
						{
							return;
						}

						int x2 = getChunk(loc.getX());
						int z2 = getChunk(loc.getZ());

						if (prev.getBlockX() == x2 && prev.getBlockZ() == z2 && loc.getWorld() == prev.getWorld())
						{
							chunkMove(player, loc, null);
						}

						refreshBedTrackers(player);
					}
				});
			}
		}
	}

	@EventHandler
	public void ChunkUnload(ChunkUnloadEvent event)
	{
		for (Entity entity : event.getChunk().getEntities())
		{
			if (_spawnPacketMap.containsKey(entity.getEntityId()))
			{
				_entityDisguiseMap.put(entity.getUniqueId().toString(), _spawnPacketMap.get(entity.getEntityId()));
				_spawnPacketMap.remove(entity.getEntityId());
				_lastRabbitHop.remove(entity.getEntityId());
			}
		}
	}

	public void clearDisguises()
	{
		_spawnPacketMap.clear();
		_movePacketMap.clear();
		_moveTempMap.clear();
		_goingUp.clear();
		_entityDisguiseMap.clear();
		_disguisePlayerMap.clear();

		if (_bedPackets)
		{
			unregisterBedChunk();
		}
	}

	private boolean containsSpawnDisguise(Player owner, int entityId)
	{
		return _spawnPacketMap.containsKey(entityId)
				&& (_spawnPacketMap.get(entityId).Global || (_disguisePlayerMap.containsKey(_spawnPacketMap.get(entityId)) && _disguisePlayerMap
						.get(_spawnPacketMap.get(entityId)).contains(owner)));
	}

	public void disguise(DisguiseBase disguise, boolean refreshTrackers, Player... players)
	{
		if (!disguise.GetEntity().isAlive())
			return;

		if (!_bedPackets && disguise instanceof DisguisePlayer && ((DisguisePlayer) disguise).getSleepingDirection() != null)
		{
			_bedPackets = true;

			for (Player player : Bukkit.getOnlinePlayers())
			{
				UtilPlayer.sendPacket(player, getBedChunkLoadPackets(player, player.getLocation()));
			}
		}

		if (players.length != 0)
		{
			disguise.Global = false;
		}

		_spawnPacketMap.put(disguise.GetEntityId(), disguise);
		_disguisePlayerMap.put(disguise, new HashSet<Player>());

		if (disguise instanceof DisguiseRabbit)
		{
			_lastRabbitHop.put(disguise.GetEntityId(), new NautHashMap<Integer, Long>());
		}

		for (Player player : players)
			addViewerToDisguise(disguise, player, false);

		if (disguise.GetEntity() instanceof Player && disguise instanceof DisguisePlayer)
		{
			if (!((Player) disguise.GetEntity()).getName().equalsIgnoreCase(((DisguisePlayer) disguise).getName()))
			{
				_blockedNames.add(((Player) disguise.GetEntity()).getName());
			}
		}

		if (refreshTrackers)
		{
			refreshTrackers(disguise.GetEntity().getBukkitEntity(),
					disguise.Global ? Bukkit.getOnlinePlayers().toArray(new Player[0]) : players);
		}
	}

		 */


	/*

	public Packet[] getBedChunkLoadPackets(Player player, Location newLoc)
	{
		prepareChunk(newLoc);

		Packet[] packets = new Packet[2];

		// Make unload
		packets[0] = new PacketPlayOutMapChunk(_bedChunk, true, 0, UtilPlayer.is1_8(player) ? 48 : 0);

		// Make load
		packets[1] = new PacketPlayOutMapChunkBulk(Arrays.asList(_bedChunk), UtilPlayer.is1_8(player) ? 48 : 0);

		return packets;
	}

	public Packet getBedChunkUnloadPacket(Player player, Location oldLoc)
	{
		prepareChunk(oldLoc);

		return new PacketPlayOutMapChunk(_bedChunk, true, 0, UtilPlayer.is1_8(player) ? 48 : 0);
	}

	private Packet[] getBedPackets(Location recieving, DisguisePlayer playerDisguise)
	{
		try
		{
			PacketPlayOutBed bedPacket = new PacketPlayOutBed();

			_bedA.set(bedPacket, playerDisguise.GetEntityId());

			int chunkX = getChunk(recieving.getX());
			int chunkZ = getChunk(recieving.getZ());

			_bedB.set(bedPacket, (chunkX * 16) + 1 + playerDisguise.getSleepingDirection().getModX());
			_bedD.set(bedPacket, (chunkZ * 16) + 1 + playerDisguise.getSleepingDirection().getModZ());

			PacketPlayOutEntityTeleport teleportPacket = new PacketPlayOutEntityTeleport(playerDisguise.GetEntity());

			teleportPacket.c += (int) (0.35D * 32);

			return new Packet[]
				{
						bedPacket, teleportPacket
				};
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		return null;
	}

	private int getChunk(double block)
	{
		int chunk = (int) Math.floor(block / 16D) - 17;
		chunk -= chunk % 8;
		return chunk;
	}

	private ArrayList<Packet> getChunkMovePackets(Player player, Location newLoc, Location oldLoc)
	{
		ArrayList<Packet> packets = new ArrayList<Packet>();

		if (newLoc != null)
		{
			packets.addAll(Arrays.asList(getBedChunkLoadPackets(player, newLoc)));

			EntityPlayer nmsPlayer = ((CraftPlayer) player).getHandle();

			for (Entry<DisguiseBase, HashSet<Player>> entry : _disguisePlayerMap.entrySet())
			{
				if (entry.getKey().Global || entry.getValue().contains(player))
				{
					EntityTrackerEntry tracker = getEntityTracker(entry.getKey().GetEntity());

					if (tracker != null && tracker.trackedPlayers.contains(nmsPlayer))
					{
						if (entry.getKey() instanceof DisguisePlayer
								&& ((DisguisePlayer) entry.getKey()).getSleepingDirection() != null)
						{

							packets.addAll(Arrays.asList(getBedPackets(newLoc, (DisguisePlayer) entry.getKey())));
						}
					}
				}
			}
		}

		if (oldLoc != null)
		{
			packets.add(getBedChunkUnloadPacket(player, oldLoc));
		}

		return packets;
	}

	public DisguiseBase getDisguise(LivingEntity entity)
	{
		return _spawnPacketMap.get(entity.getEntityId());
	}

	private EntityTrackerEntry getEntityTracker(net.minecraft.server.v1_8_R3.Entity entity)
	{
		return (EntityTrackerEntry) ((WorldServer) entity.world).tracker.trackedEntities.get(entity.getId());
	}

	public void handle(PacketInfo packetInfo)
	{
		if (_handlingPacket)
			return;

		final Packet packet = packetInfo.getPacket();
		final Player owner = packetInfo.getPlayer();
		final PacketVerifier packetVerifier = packetInfo.getVerifier();

		if (UtilPlayer.is1_8(owner)
				&& (packet instanceof PacketPlayOutRelEntityMoveLook || packet instanceof PacketPlayOutRelEntityMove))
		{
			int entityId = -1;

			if (packet instanceof PacketPlayOutRelEntityMoveLook)
			{
				entityId = ((PacketPlayOutRelEntityMoveLook) packet).a;
			}/*
				else if (packet instanceof PacketPlayOutEntityLook)
				{
				entityId = ((PacketPlayOutEntityLook) packet).a;
				}*/ /*
			else if (packet instanceof PacketPlayOutRelEntityMove)
			{
				PacketPlayOutRelEntityMove rPacket = (PacketPlayOutRelEntityMove) packet;

				if (rPacket.b != 0 || rPacket.c != 0 || rPacket.d != 0)
				{
					entityId = rPacket.a;
				}
			}
			/*else if (packet instanceof PacketPlayOutEntityTeleport)
			{
				entityId = ((PacketPlayOutEntityTeleport) packet).a;
			}*/ /*

			if (_lastRabbitHop.containsKey(entityId))
			{
				NautHashMap<Integer, Long> rabbitHops = _lastRabbitHop.get(entityId);

				if (rabbitHops != null)
				{
					long last = rabbitHops.containsKey(owner.getEntityId()) ? System.currentTimeMillis()
							- rabbitHops.get(owner.getEntityId()) : 1000;

					if (last > 500 || last < 100)
					{
						rabbitHops.put(owner.getEntityId(), System.currentTimeMillis());

						PacketPlayOutEntityStatus entityStatus = new PacketPlayOutEntityStatus();
						try
						{
							_eStatusId.set(entityStatus, entityId);
							_eStatusState.set(entityStatus, (byte) 1);

							handlePacket(entityStatus, packetVerifier);
						}
						catch (Exception ex)
						{
							ex.printStackTrace();
						}
					}
				}
			}
		}

		if (packet instanceof PacketPlayOutPlayerInfo)
		{
			if (_blockedNames.contains(((PacketPlayOutPlayerInfo) packet).username))
			{
				packetInfo.setCancelled(true);
			}
		}
		else if (packet instanceof PacketPlayOutSpawnEntity || packet instanceof PacketPlayOutSpawnEntityLiving
				|| packet instanceof PacketPlayOutNamedEntitySpawn)
		{
			int entityId = -1;

			if (packet instanceof PacketPlayOutSpawnEntity)
			{
				entityId = ((PacketPlayOutSpawnEntity) packet).a;
			}
			else if (packet instanceof PacketPlayOutSpawnEntityLiving)
			{
				entityId = ((PacketPlayOutSpawnEntityLiving) packet).a;
			}
			else if (packet instanceof PacketPlayOutNamedEntitySpawn)
			{
				entityId = ((PacketPlayOutNamedEntitySpawn) packet).a;
			}

			if (_futureDisguises.containsKey(entityId))
			{
				Entry<DisguiseBase, Player[]> entry = _futureDisguises.remove(entityId);

				Entity entity = UtilEnt.getEntityById(entityId);

				if (entity != null)
				{
					entry.getKey().setEntity(entity);

					disguise(entry.getKey(), false, entry.getValue());
				}
			}

			if (_spawnPacketMap.containsKey(entityId)
					&& (_spawnPacketMap.get(entityId).Global || _disguisePlayerMap.get(_spawnPacketMap.get(entityId)).contains(
							owner)))
			{
				packetInfo.setCancelled(true);

				handleSpawnPackets(packetInfo, _spawnPacketMap.get(entityId));
			}
		}
		else if (packet instanceof PacketPlayOutUpdateAttributes)
		{
			int entityId = -1;

			try
			{
				entityId = (int) _attributesA.get((PacketPlayOutUpdateAttributes) packet);
			}
			catch (IllegalArgumentException e)
			{
				e.printStackTrace();
			}
			catch (IllegalAccessException e)
			{
				e.printStackTrace();
			}

			if (_spawnPacketMap.containsKey(entityId)
					&& owner.getEntityId() != entityId
					&& (_spawnPacketMap.get(entityId).Global || _disguisePlayerMap.get(_spawnPacketMap.get(entityId)).contains(
							owner)))
			{
				// Crash clients with meta to a block id.
				if (_spawnPacketMap.get(entityId) instanceof DisguiseBlock)
					packetInfo.setCancelled(true);
			}
		}
		else if (packet instanceof PacketPlayOutAnimation)
		{
			int entityId = ((PacketPlayOutAnimation) packet).a;

			if (containsSpawnDisguise(owner, entityId) && owner.getEntityId() != entityId)
			{
				packetInfo.setCancelled(true);
			}
		}
		else if (packet instanceof PacketPlayOutEntityMetadata)
		{
			int entityId = ((PacketPlayOutEntityMetadata) packet).a;

			if (containsSpawnDisguise(owner, entityId) && owner.getEntityId() != entityId)
			{
				handlePacket(_spawnPacketMap.get(entityId).GetMetaDataPacket(), packetVerifier);
				packetInfo.setCancelled(true);
			}
		}
		else if (packet instanceof PacketPlayOutEntityEquipment)
		{
			int entityId = ((PacketPlayOutEntityEquipment) packet).a;

			if (containsSpawnDisguise(owner, entityId) && _spawnPacketMap.get(entityId) instanceof DisguiseInsentient)
			{
				if (!((DisguiseInsentient) _spawnPacketMap.get(entityId)).armorVisible()
						&& ((PacketPlayOutEntityEquipment) packet).b != 0)
				{
					packetInfo.setCancelled(true);
				}
			}
		}
		else if (packet instanceof PacketPlayOutEntityVelocity)
		{
			PacketPlayOutEntityVelocity velocityPacket = (PacketPlayOutEntityVelocity) packet;

			// Only for viewers
			if (velocityPacket.a == owner.getEntityId())
			{
				if (velocityPacket.c > 0)
					_goingUp.add(velocityPacket.a);
			}
			else if (velocityPacket.b == 0 && velocityPacket.c == 0 && velocityPacket.d == 0)
			{
				return;
			}
			else if (_spawnPacketMap.containsKey(velocityPacket.a))
			{
				packetInfo.setCancelled(true);
			}
		}
		else if (packet instanceof PacketPlayOutRelEntityMove)
		{
			final PacketPlayOutRelEntityMove movePacket = (PacketPlayOutRelEntityMove) packet;

			// Only for viewers
			if (movePacket.a == owner.getEntityId())
				return;

			if (_goingUp.contains(movePacket.a) && movePacket.c != 0 && movePacket.c < 20)
			{
				_goingUp.remove(movePacket.a);
				_movePacketMap.remove(movePacket.a);
			}

			if (!containsSpawnDisguise(owner, movePacket.a))
				return;

			final PacketPlayOutEntityVelocity velocityPacket = new PacketPlayOutEntityVelocity();
			velocityPacket.a = movePacket.a;
			velocityPacket.b = movePacket.b * 100;
			velocityPacket.c = movePacket.c * 100;
			velocityPacket.d = movePacket.d * 100;

			if (_movePacketMap.containsKey(movePacket.a))
			{
				PacketPlayOutEntityVelocity lastVelocityPacket = _movePacketMap.get(movePacket.a);

				velocityPacket.b = (int) (.8 * lastVelocityPacket.b);
				velocityPacket.c = (int) (.8 * lastVelocityPacket.c);
				velocityPacket.d = (int) (.8 * lastVelocityPacket.d);
			}

			_movePacketMap.put(movePacket.a, velocityPacket);

			packetVerifier.bypassProcess(velocityPacket);

			if (_goingUp.contains(movePacket.a) && movePacket.c != 0 && movePacket.c > 20)
			{
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(getPlugin(), new Runnable()
				{
					public void run()
					{
						packetVerifier.bypassProcess(velocityPacket);
					}
				});
			}

			if (_spawnPacketMap.get(movePacket.a) instanceof DisguiseBlock)
			{

			}
		}
		else if (packet instanceof PacketPlayOutRelEntityMoveLook)
		{
			final PacketPlayOutRelEntityMoveLook movePacket = (PacketPlayOutRelEntityMoveLook) packet;

			// Only for viewers
			if (movePacket.a == owner.getEntityId())
				return;

			if (_goingUp.contains(movePacket.a) && movePacket.c != 0 && movePacket.c <= 20)
			{
				_goingUp.remove(movePacket.a);
				_movePacketMap.remove(movePacket.a);
			}

			if (!containsSpawnDisguise(owner, movePacket.a))
				return;

			final PacketPlayOutEntityVelocity velocityPacket = new PacketPlayOutEntityVelocity();
			velocityPacket.a = movePacket.a;
			velocityPacket.b = movePacket.b * 100;
			velocityPacket.c = movePacket.c * 100;
			velocityPacket.d = movePacket.d * 100;

			if (_movePacketMap.containsKey(movePacket.a))
			{
				PacketPlayOutEntityVelocity lastVelocityPacket = _movePacketMap.get(movePacket.a);

				velocityPacket.b = (int) (.8 * lastVelocityPacket.b);
				velocityPacket.c = (int) (.8 * lastVelocityPacket.c);
				velocityPacket.d = (int) (.8 * lastVelocityPacket.d);
			}

			_movePacketMap.put(movePacket.a, velocityPacket);

			packetVerifier.bypassProcess(velocityPacket);

			if (_goingUp.contains(movePacket.a) && movePacket.c != 0 && movePacket.c > 20)
			{
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(getPlugin(), new Runnable()
				{
					public void run()
					{
						packetVerifier.bypassProcess(velocityPacket);
					}
				});
			}
		}
	}

	private void handlePacket(Packet packet, PacketVerifier verifier)
	{
		_handlingPacket = true;
		verifier.process(packet);
		_handlingPacket = false;
	}

	private void handleSpawnPackets(PacketInfo packetInfo, DisguiseBase disguise)
	{
		final Player player = packetInfo.getPlayer();

		if (!UtilPlayer.is1_8(player) && disguise instanceof DisguiseRabbit)
		{
			return;
		}

		final PacketVerifier packetVerifier = packetInfo.getVerifier();

		if (disguise instanceof DisguisePlayer)
		{

			final DisguisePlayer pDisguise = (DisguisePlayer) disguise;

			handlePacket(pDisguise.getNewInfoPacket(true), packetVerifier);

			PacketPlayOutNamedEntitySpawn namePacket = pDisguise.spawnBeforePlayer(player.getLocation());

			namePacket.i.watch(0, (byte) 32);

			handlePacket(namePacket, packetVerifier);

			if (pDisguise.getSleepingDirection() != null)
			{
				for (Packet packet : getBedPackets(player.getLocation(), pDisguise))
				{
					handlePacket(packet, packetVerifier);
				}

			}
			else
			{
				handlePacket(new PacketPlayOutEntityTeleport(pDisguise.GetEntity()), packetVerifier);
			}

			for (Packet packet : pDisguise.getEquipmentPackets())
			{
				handlePacket(packet, packetVerifier);
			}

			handlePacket(pDisguise.GetMetaDataPacket(), packetVerifier);

			Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), new Runnable()
			{
				public void run()
				{
					handlePacket(pDisguise.getNewInfoPacket(false), packetVerifier);
				}
			}, 6);
		}
		else
		{
			handlePacket(disguise.GetSpawnPacket(), packetVerifier);

			if (disguise instanceof DisguiseLiving)
			{
				ArrayList<Packet> packets = ((DisguiseLiving) disguise).getEquipmentPackets();

				for (Packet packet : packets)
				{
					handlePacket(packet, packetVerifier);
				}
			}
		}
	}

	public boolean isDisguised(LivingEntity entity)
	{
		return _spawnPacketMap.containsKey(entity.getEntityId());
	}

	@EventHandler
	public void PlayerQuit(PlayerQuitEvent event)
	{
		undisguise(event.getPlayer());

		for (DisguiseBase disguise : _disguisePlayerMap.keySet())
		{
			_disguisePlayerMap.get(disguise).remove(event.getPlayer());
		}

		for (Integer disguise : _lastRabbitHop.keySet())
		{
			_lastRabbitHop.get(disguise).remove(event.getPlayer().getEntityId());
		}
	}

	private void prepareChunk(Location loc)
	{
		int chunkX = getChunk(loc.getX());
		int chunkZ = getChunk(loc.getZ());

		try
		{
			_xChunk.set(_bedChunk, chunkX);
			_zChunk.set(_bedChunk, chunkZ);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	private void refreshBedTrackers(final Player player)
	{
		for (final DisguiseBase disguise : this._disguisePlayerMap.keySet())
		{
			if (!(disguise instanceof DisguisePlayer) || ((DisguisePlayer) disguise).getSleepingDirection() == null)
			{
				continue;
			}

			final EntityTrackerEntry entityTracker = getEntityTracker(disguise.GetEntity());

			if (entityTracker != null)
			{
				if (!entityTracker.trackedPlayers.contains(((CraftPlayer) player).getHandle()))
				{
					continue;
				}

				Packet destroyPacket = new PacketPlayOutEntityDestroy(new int[]
					{
						disguise.GetEntityId()
					});

				entityTracker.clear(((CraftPlayer) player).getHandle());

				UtilPlayer.sendPacket(player, destroyPacket);

				final World world = disguise.GetEntity().getBukkitEntity().getWorld();

				Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), new Runnable()
				{
					public void run()
					{
						try
						{
							Entity entity = disguise.GetEntity().getBukkitEntity();

							if (entity.getWorld() == world && entity.isValid())
							{
								if (player.isOnline() && player.getWorld() == entity.getWorld())
								{
									entityTracker.updatePlayer(((CraftPlayer) player).getHandle());
								}
							}
						}
						catch (Exception ex)
						{
							ex.printStackTrace();
						}
					}
				}, 5);
			}
		}
	}

	private void refreshTrackers(final Entity entity, final Player[] players)
	{
		final EntityTrackerEntry entityTracker = getEntityTracker(((CraftEntity) entity).getHandle());

		if (entityTracker != null)
		{
			Packet destroyPacket = new PacketPlayOutEntityDestroy(new int[]
				{
					entity.getEntityId()
				});

			for (Player player : players)
			{
				entityTracker.clear(((CraftPlayer) player).getHandle());

				UtilPlayer.sendPacket(player, destroyPacket);
			}

			final World world = entity.getWorld();

			Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), new Runnable()
			{
				public void run()
				{
					try
					{
						if (entity.getWorld() == world && entity.isValid())
						{
							for (Player player : players)
							{
								if (player.isOnline() && player.getWorld() == entity.getWorld())
								{
									entityTracker.updatePlayer(((CraftPlayer) player).getHandle());
								}
							}
						}
					}
					catch (Exception ex)
					{
						ex.printStackTrace();
					}
				}
			}, 5);
		}
	}

	public void removeViewerToDisguise(DisguiseBase disguise, Player player)
	{
		_disguisePlayerMap.get(disguise).remove(player);

		refreshTrackers(disguise.GetEntity().getBukkitEntity(), new Player[]
			{
				player
			});
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void switchedWorld(PlayerChangedWorldEvent event)
	{
		if (!_bedPackets)
			return;

		chunkMove(event.getPlayer(), event.getPlayer().getLocation(), null);
	}

	@EventHandler
	public void TeleportDisguises(UpdateEvent event)
	{
		if (event.getType() != UpdateType.SEC)
			return;

		TimingManager.startTotal("Teleport Disguises");
		for (Player player : Bukkit.getOnlinePlayers())
		{
			for (Player otherPlayer : Bukkit.getOnlinePlayers())
			{
				if (player == otherPlayer)
					continue;

				if (otherPlayer.getLocation().subtract(0, .5, 0).getBlock().getTypeId() != 0)
					((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityTeleport(
							((CraftPlayer) otherPlayer).getHandle()));
			}
		}
		TimingManager.stopTotal("Teleport Disguises");
	}

	public void undisguise(LivingEntity entity)
	{
		if (!_spawnPacketMap.containsKey(entity.getEntityId()))
			return;

		_lastRabbitHop.remove(entity.getEntityId());
		DisguiseBase disguise = _spawnPacketMap.remove(entity.getEntityId());
		Collection<? extends Player> players = (disguise.Global ? Bukkit.getOnlinePlayers() : _disguisePlayerMap.remove(disguise));

		_movePacketMap.remove(entity.getEntityId());
		_moveTempMap.remove(entity.getEntityId());
		_blockedNames.remove(((Player) entity).getName());

		refreshTrackers(entity, players.toArray(new Player[0]));

		if (_bedPackets && disguise instanceof DisguisePlayer && ((DisguisePlayer) disguise).getSleepingDirection() != null)
		{
			for (DisguiseBase dis : _disguisePlayerMap.keySet())
			{
				if (dis instanceof DisguisePlayer && ((DisguisePlayer) dis).getSleepingDirection() != null)
				{
					return;
				}
			}

			unregisterBedChunk();
		}
	}

	private void unregisterBedChunk()
	{
		_bedPackets = false;

		for (Player player : Bukkit.getOnlinePlayers())
		{
			chunkMove(player, null, player.getLocation());
		}
	}

	public void updateDisguise(DisguiseBase disguise)
	{
		Collection<? extends Player> players = (disguise.Global ? Bukkit.getOnlinePlayers() : _disguisePlayerMap.get(disguise));

		for (Player player : players)
		{
			if (disguise.GetEntity() == ((CraftPlayer) player).getHandle())
				continue;

			EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();

			entityPlayer.playerConnection.sendPacket(disguise.GetMetaDataPacket());
		}
	}

	@EventHandler
	public void cleanDisguises(UpdateEvent event)
	{
		if (event.getType() != UpdateType.SLOWER || _disguisePlayerMap.isEmpty())
			return;

		for (Iterator<DisguiseBase> disguiseIterator = _disguisePlayerMap.keySet().iterator(); disguiseIterator.hasNext();)
		{
			DisguiseBase disguise = disguiseIterator.next();

			if (!(disguise.GetEntity() instanceof EntityPlayer))
				continue;

			EntityPlayer disguisedPlayer = (EntityPlayer) disguise.GetEntity();

			if (Bukkit.getPlayerExact(disguisedPlayer.getName()) == null || !disguisedPlayer.isAlive() || !disguisedPlayer.valid)
				disguiseIterator.remove();
			else
			{
				for (Iterator<Player> playerIterator = _disguisePlayerMap.get(disguise).iterator(); playerIterator.hasNext();)
				{
					Player player = playerIterator.next();

					if (!player.isOnline() || !player.isValid())
						playerIterator.remove();
				}
			}
		}
	}
}

*/
