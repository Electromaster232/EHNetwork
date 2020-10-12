package ehnetwork.core.npc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftCreature;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import net.minecraft.server.v1_8_R3.EntityInsentient;

import ehnetwork.core.MiniPlugin;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.creature.Creature;
import ehnetwork.core.creature.event.CreatureKillEntitiesEvent;
import ehnetwork.core.database.DBPool;
import ehnetwork.core.npc.command.NpcCommand;
import ehnetwork.core.npc.event.NpcDamageByEntityEvent;
import ehnetwork.core.npc.event.NpcInteractEntityEvent;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.database.Tables;
import ehnetwork.database.tables.records.NpcsRecord;
import org.jooq.Result;
import org.jooq.impl.DSL;

public class NpcManager extends MiniPlugin
{
	private static String itemStackToYaml(ItemStack stack)
	{
		if (stack == null || stack.getType() == Material.AIR)
			return null;
		else
		{
			YamlConfiguration configuration = new YamlConfiguration();
			configuration.set("stack", stack);
			return configuration.saveToString();
		}
	}

	private static ItemStack yamlToItemStack(String yaml)
	{
		if (yaml == null)
			return null;
		else
		{
			try
			{
				YamlConfiguration configuration = new YamlConfiguration();
				configuration.loadFromString(yaml);
				return configuration.getItemStack("stack");
			}
			catch (InvalidConfigurationException e)
			{
				e.printStackTrace();

				return null;
			}
		}
	}

	private final Creature _creature;
	private final List<Npc> _npcs = new ArrayList<>();
	final Map<UUID, Npc> _npcMap = new HashMap<>();
	private final Set<UUID> _npcDeletingPlayers = new HashSet<>();

	public NpcManager(JavaPlugin plugin, Creature creature)
	{
		super("NpcManager", plugin);

		_creature = creature;

		_plugin.getServer().getScheduler().scheduleSyncRepeatingTask(_plugin, new Runnable()
		{
			public void run()
			{
				updateNpcLocations();
			}
		}, 0L, 5L);

		_plugin.getServer().getPluginManager().registerEvents(this, _plugin);

		try
		{ 
			loadNpcs();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void addCommands()
	{
		addCommand(new NpcCommand(this));
	}

	public void help(Player caller, String message)
	{
		UtilPlayer.message(caller, F.main(_moduleName, "Commands List:"));
		UtilPlayer.message(caller, F.help("/npc add <type> [radius] [adult] [name]", "Create a new NPC.", Rank.DEVELOPER));
		UtilPlayer.message(caller, F.help("/npc del ", "Right click NPC to delete.", Rank.DEVELOPER));
		UtilPlayer.message(caller, F.help("/npc home", "Teleport NPCs to home locations.", Rank.DEVELOPER));
		UtilPlayer.message(caller, F.help("/npc clear", "Deletes all NPCs.", Rank.DEVELOPER));
		UtilPlayer.message(caller, F.help("/npc refresh", "Refresh NPCs from database.", Rank.DEVELOPER));

		if (message != null)
			UtilPlayer.message(caller, F.main(_moduleName, ChatColor.RED + message));
	}

	public void help(Player caller)
	{
		help(caller, null);
	}

	private Npc getNpcByEntityUUID(UUID uuid)
	{
		if (uuid == null)
			return null;

		return _npcMap.get(uuid);
	}

	public Npc getNpcByEntity(Entity entity)
	{
		if (entity == null)
			return null;

		return getNpcByEntityUUID(entity.getUniqueId());
	}

	public boolean isNpc(Entity entity)
	{
		return getNpcByEntity(entity) != null;
	}

	public boolean isDetachedNpc(LivingEntity entity)
	{
		return !isNpc(entity) && entity.getCustomName() != null && entity.getCustomName().startsWith(ChatColor.RESET.toString());
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onEntityDamage(EntityDamageEvent event)
	{
		if (event.getEntity() instanceof LivingEntity && isNpc(event.getEntity()))
			event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityDamage(EntityDamageByEntityEvent event)
	{
		if (isNpc(event.getEntity()))
		{
			if (event.getEntity() instanceof LivingEntity && event.getDamager() instanceof LivingEntity)
			{
				NpcDamageByEntityEvent npcEvent = new NpcDamageByEntityEvent((LivingEntity) event.getEntity(), (LivingEntity) event.getDamager());

				Bukkit.getServer().getPluginManager().callEvent(npcEvent);
			}

			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onCreatureKillEntities(CreatureKillEntitiesEvent event)
	{
		for (Iterator<Entity> entityIterator = event.GetEntities().iterator(); entityIterator.hasNext(); )
		{
			if (isNpc(entityIterator.next()))
				entityIterator.remove();
		}
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onEntityTarget(EntityTargetEvent event)
	{
		if (isNpc(event.getEntity()))
			event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onEntityCombust(EntityCombustEvent event)
	{
		if (isNpc(event.getEntity()))
			event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event)
	{
		if (event.getRightClicked() instanceof LivingEntity)
		{
			if (_npcDeletingPlayers.remove(event.getPlayer().getUniqueId()))
			{
				try
				{
					if (deleteNpc(event.getRightClicked()))
						event.getPlayer().sendMessage(F.main(getName(), "Deleted npc."));
					else
						event.getPlayer().sendMessage(F.main(getName(), "Failed to delete npc.  That one isn't in the list."));
				}
				catch (SQLException e)
				{
					e.printStackTrace();
				}
			}
			else if (isNpc(event.getRightClicked()))
			{
				NpcInteractEntityEvent npcEvent = new NpcInteractEntityEvent((LivingEntity) event.getRightClicked(), event.getPlayer());

				Bukkit.getServer().getPluginManager().callEvent(npcEvent);

				event.setCancelled(true);
			}
		}
	}

	public Entity addNpc(Player player, EntityType entityType, double radius, boolean adult, String name, String entityMeta) throws SQLException
	{
		try (Connection connection = DBPool.ACCOUNT.getConnection())
		{
			String helmet = itemStackToYaml(player.getInventory().getHelmet());
			String chestplate = itemStackToYaml(player.getInventory().getChestplate());
			String leggings = itemStackToYaml(player.getInventory().getLeggings());
			String boots = itemStackToYaml(player.getInventory().getBoots());
			String inHand = itemStackToYaml(player.getInventory().getItemInHand());

			NpcsRecord npcsRecord = DSL.using(connection).newRecord(Tables.npcs);
			npcsRecord.setServer(getServerName());
			npcsRecord.setName(name);
			npcsRecord.setWorld(player.getWorld().getName());
			npcsRecord.setX(player.getLocation().getX());
			npcsRecord.setY(player.getLocation().getY());
			npcsRecord.setZ(player.getLocation().getZ());
			npcsRecord.setRadius(radius);
			npcsRecord.setEntityType(entityType.name());
			npcsRecord.setAdult(adult);
			npcsRecord.setHelmet(helmet);
			npcsRecord.setChestplate(chestplate);
			npcsRecord.setLeggings(leggings);
			npcsRecord.setBoots(boots);
			npcsRecord.setInHand(inHand);
			npcsRecord.setEntityMeta(entityMeta);

			try
			{
				npcsRecord.insert();
			}
			finally
			{
				npcsRecord.detach();
			}

			Npc npc = new Npc(this, npcsRecord);
			_npcs.add(npc);

			return spawnNpc(npc);
		}
	}

	public Entity spawnNpc(Npc npc)
	{
		LivingEntity entity = (LivingEntity) _creature.SpawnEntity(npc.getLocation(), EntityType.valueOf(npc.getDatabaseRecord().getEntityType()));

		if (npc.getDatabaseRecord().getName() == null)
		{
			entity.setCustomNameVisible(false);
			entity.setCustomName(ChatColor.RESET.toString());
		}
		else
		{
			String name = npc.getDatabaseRecord().getName();
			for (ChatColor color : ChatColor.values())
				name = name.replace("(" + color.name().toLowerCase() + ")", color.toString());
			name = ChatColor.translateAlternateColorCodes('&', name);

			entity.setCustomNameVisible(true);
			entity.setCustomName(ChatColor.RESET + name);
		}

		entity.setCanPickupItems(false);
		entity.setRemoveWhenFarAway(false);
		((EntityInsentient) ((CraftLivingEntity) entity).getHandle()).persistent = true;

		if (entity instanceof Ageable)
		{
			if (npc.getDatabaseRecord().getAdult())
				((Ageable) entity).setAdult();
			else
				((Ageable) entity).setBaby();

			((Ageable) entity).setAgeLock(true);
		}
		if (entity instanceof Zombie)
			((Zombie) entity).setBaby(!npc.getDatabaseRecord().getAdult());
		if (entity instanceof Slime && npc.getDatabaseRecord().getEntityMeta() != null)
			((Slime) entity).setSize(Integer.parseInt(npc.getDatabaseRecord().getEntityMeta()));
		if (entity instanceof Skeleton && npc.getDatabaseRecord().getEntityMeta() != null)
			((Skeleton) entity).setSkeletonType(Skeleton.SkeletonType.valueOf(npc.getDatabaseRecord().getEntityMeta().toUpperCase()));
		if (entity instanceof Villager && npc.getDatabaseRecord().getEntityMeta() != null)
			((Villager) entity).setProfession(Villager.Profession.valueOf(npc.getDatabaseRecord().getEntityMeta().toUpperCase()));

		if (entity instanceof org.bukkit.entity.Creature)
			((org.bukkit.entity.Creature) entity).setTarget(null);

		if (npc.getDatabaseRecord().getRadius() == 0)
		{
			UtilEnt.Vegetate(entity);
			UtilEnt.silence(entity, true);
			UtilEnt.ghost(entity, true, false);

			// Add Look AI
			UtilEnt.addLookAtPlayerAI(entity, 10.0F);
		}

		if (npc.getDatabaseRecord().getHelmet() != null)
			entity.getEquipment().setHelmet(yamlToItemStack(npc.getDatabaseRecord().getHelmet()));
		if (npc.getDatabaseRecord().getChestplate() != null)
			entity.getEquipment().setChestplate(yamlToItemStack(npc.getDatabaseRecord().getChestplate()));
		if (npc.getDatabaseRecord().getLeggings() != null)
			entity.getEquipment().setLeggings(yamlToItemStack(npc.getDatabaseRecord().getLeggings()));
		if (npc.getDatabaseRecord().getBoots() != null)
			entity.getEquipment().setBoots(yamlToItemStack(npc.getDatabaseRecord().getBoots()));
		if (npc.getDatabaseRecord().getInHand() != null)
			entity.getEquipment().setItemInHand(yamlToItemStack(npc.getDatabaseRecord().getInHand()));

		npc.setEntity(entity);

		return entity;
	}

	public boolean deleteNpc(Entity entity) throws SQLException
	{
		Npc npc = getNpcByEntity(entity);

		if (npc != null)
		{
			try (Connection connection = DBPool.ACCOUNT.getConnection())
			{
				npc.getDatabaseRecord().attach(DSL.using(connection).configuration());
				npc.getDatabaseRecord().delete();

				entity.remove();
				_npcMap.remove(entity.getUniqueId());
				_npcs.remove(npc);

				return true;
			}
			finally
			{
				npc.getDatabaseRecord().detach();
			}
		}

		return false;
	}

	public void prepDeleteNpc(Player admin)
	{
		_npcDeletingPlayers.add(admin.getUniqueId());
	}

	private void updateNpcLocations()
	{
		for (World world : Bukkit.getWorlds())
		{
			for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class))
			{
				Npc npc = getNpcByEntity(entity);
				if (npc == null)
					continue;

				entity.setTicksLived(1);
				((EntityInsentient) ((CraftLivingEntity) entity).getHandle()).persistent = true;
				UtilEnt.silence(entity, true);

				if (!entity.getLocation().getChunk().isLoaded())
					continue;

				if (!entity.isDead() && entity.isValid())
				{
					for (ItemStack armor : entity.getEquipment().getArmorContents())
					{
						if (armor != null && armor.getType() != Material.AIR)
							armor.setDurability((short) 0);
					}

					if (npc.getFailedAttempts() >= 10 || npc.getDatabaseRecord().getRadius() == 0)
					{
						Location location = npc.getLocation();
						location.setPitch(entity.getLocation().getPitch());
						location.setYaw(entity.getLocation().getYaw());
						entity.teleport(location);
						entity.setVelocity(new Vector(0, 0, 0));
						npc.setFailedAttempts(0);
					}
					else if (!npc.isInRadius(entity.getLocation()) && npc.getEntity() instanceof CraftCreature)
					{
						npc.returnToPost();
						npc.incrementFailedAttempts();
					}
					else if (npc.getEntity() instanceof CraftCreature)
					{
						if (npc.isReturning())
							npc.clearGoals();

						npc.setFailedAttempts(0);
					}
				}
			}
		}
	}

	public void teleportNpcsHome()
	{
		for (World world : Bukkit.getWorlds())
		{
			for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class))
			{
				Npc npc = getNpcByEntity(entity);
				if (npc == null)
					continue;

				if (!entity.getLocation().getChunk().isLoaded())
					continue;

				if (!entity.isDead() && entity.isValid())
				{
					Location location = npc.getLocation();
					location.setPitch(entity.getLocation().getPitch());
					location.setYaw(entity.getLocation().getYaw());
					entity.teleport(location);
					entity.setVelocity(new Vector(0, 0, 0));

					npc.setFailedAttempts(0);
				}
			}
		}
	}

	public void loadNpcs() throws SQLException
	{
		String serverType = getServerName();

		try (Connection connection = DBPool.ACCOUNT.getConnection())
		{
			Result<NpcsRecord> result = DSL.using(connection)
					.selectFrom(Tables.npcs)
					.where(Tables.npcs.server.eq(serverType))
					.fetch();

			for (NpcsRecord record : result)
			{
				record.detach();

				Npc npc = new Npc(this, record);
				_npcs.add(npc);

				if (npc.getChunk().isLoaded())
					spawnNpc(npc);
			}
		}
	}

	public void clearNpcs(boolean deleteFromDatabase) throws SQLException
	{
		if (deleteFromDatabase)
		{
			String serverType = getServerName();

			try (Connection connection = DBPool.ACCOUNT.getConnection())
			{
				DSL.using(connection)
						.delete(Tables.npcs)
						.where(Tables.npcs.server.eq(serverType))
						.execute();
			}
		}

		for (World world : Bukkit.getWorlds())
		{
			for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class))
			{
				if (isNpc(entity))
					entity.remove();
			}
		}

		_npcs.clear();
		_npcMap.clear();
	}

	@EventHandler
	public void onUpdate(UpdateEvent event)
	{
		if (event.getType() != UpdateType.SEC)
			return;

		if (Bukkit.getOnlinePlayers().isEmpty())
			return;

		for (World world : Bukkit.getWorlds())
		{
			for (LivingEntity livingEntity : world.getEntitiesByClass(LivingEntity.class))
			{
				if (isDetachedNpc(livingEntity))
					livingEntity.remove();
			}
		}

		for (Npc npc : _npcs)
		{
			if (npc.getEntity() != null && !npc.getEntity().isValid() && npc.getChunk().isLoaded())
				spawnNpc(npc);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onChunkLoad(ChunkLoadEvent event)
	{
		for (Entity entity : event.getChunk().getEntities())
		{
			if (entity instanceof LivingEntity)
			{
				Npc npc = getNpcByEntity(entity);
				if (npc != null)
				{
					UtilEnt.silence(entity, true);
					UtilEnt.ghost(entity, true, false);

					if (npc.getDatabaseRecord().getRadius() == 0)
					{
						UtilEnt.Vegetate(entity);
						UtilEnt.ghost(entity, true, false);
					}
				}

				if (isDetachedNpc((LivingEntity) entity))
					entity.remove();
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onEntityDeath(EntityDeathEvent event)
	{
		Npc npc = getNpcByEntity(event.getEntity());

		if (npc != null)
			npc.setEntity(null);
	}

	@EventHandler
	public void onUpdateNpcMessage(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		for (Player player : Bukkit.getOnlinePlayers())
		{
			for (Npc npc : _npcs)
			{
				if (npc.getInfo() == null)
					continue;

				if (npc.getInfoRadiusSquared() == null)
					continue;

				if (npc.getDatabaseRecord().getInfoDelay() == null)
					continue;

				if (npc.getEntity() == null)
					continue;

				if (npc.getEntity().getWorld() != player.getWorld())
					continue;

				if (npc.getEntity().getLocation().distanceSquared(player.getLocation()) > npc.getInfoRadiusSquared())
					continue;

				if (!Recharge.Instance.use(player, npc.getEntity().getCustomName() + " Info", npc.getDatabaseRecord().getInfoDelay(), false, false))
					continue;

				player.sendMessage(npc.getInfo());
				
				player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1f, 2f);
			}
		}
	}

	@EventHandler
	public void onNpcInteract(PlayerInteractEntityEvent event)
	{
		Npc npc = getNpcByEntity(event.getRightClicked());
		if (npc == null)
			return;

		if (npc.getInfo() == null)
			return;

		if (!Recharge.Instance.use(event.getPlayer(), npc.getEntity().getCustomName() + " Info Click", 2000, false, false))
			return;

		event.getPlayer().sendMessage(npc.getInfo());
		
		event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ORB_PICKUP, 1f, 2f);
	}

	@EventHandler
	public void onNpcDamage(EntityDamageByEntityEvent event)
	{
		if (!(event.getDamager() instanceof Player))
			return;

		Player player = (Player) event.getDamager();

		Npc npc = getNpcByEntity(event.getEntity());
		if (npc == null)
			return;

		if (npc.getInfo() == null)
			return;

		if (!Recharge.Instance.use(player, npc.getEntity().getCustomName() + " Info Click", 2000, false, false))
			return;

		player.sendMessage(npc.getInfo());
		
		player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1f, 2f);
	}

	public String getServerName()
	{
		String serverName = getPlugin().getClass().getSimpleName();

		if (Bukkit.getMotd() != null && Bukkit.getMotd().equalsIgnoreCase("test"))
			serverName += "-Test";

		return serverName;
	}
}
