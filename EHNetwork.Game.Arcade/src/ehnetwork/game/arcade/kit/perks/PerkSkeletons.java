package ehnetwork.game.arcade.kit.perks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftCreature;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftSkeleton;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerEvent;
import net.minecraft.server.v1_8_R3.EntityCreature;
import net.minecraft.server.v1_8_R3.Navigation;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilAlg;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.minecraft.game.core.combat.event.CombatDeathEvent;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class PerkSkeletons extends Perk
{
	public static class MinionSpawnEvent extends PlayerEvent
	{
		private static final HandlerList handlers = new HandlerList();

		public static HandlerList getHandlerList()
		{
			return handlers;
		}

		private final PerkSkeletons _perkSkeletons;

		public MinionSpawnEvent(Player who, PerkSkeletons perkSkeletons)
		{
			super(who);

			_perkSkeletons = perkSkeletons;
		}

		@Override
		public HandlerList getHandlers()
		{
			return getHandlerList();
		}

		public PerkSkeletons getPerkSkeletons()
		{
			return _perkSkeletons;
		}
	}

	private HashMap<Player, ArrayList<Skeleton>> _minions = new HashMap<Player, ArrayList<Skeleton>>();

	private boolean _name;
	private int _maxDist = 8;

	public PerkSkeletons(boolean name)
	{
		super("Skeleton Minons", new String[]
				{
						C.cGray + "Killing an opponent summons a skeletal minion."
				});

		_name = name;
	}

	@EventHandler
	public void MinionSpawn(CombatDeathEvent event)
	{
		if (event.GetLog().GetKiller() == null)
			return;

		if (!(event.GetEvent().getEntity() instanceof Player))
			return;

		Player killer = UtilPlayer.searchExact(event.GetLog().GetKiller().GetName());
		if (killer == null)
			return;

		if (!Kit.HasKit(killer))
			return;

		Player killed = (Player) event.GetEvent().getEntity();

		Manager.GetGame().CreatureAllowOverride = true;
		Skeleton skel = killer.getWorld().spawn(killed.getLocation(), Skeleton.class);
		Manager.GetGame().CreatureAllowOverride = false;

		UtilEnt.removeGoalSelectors(skel);

		skel.setMaxHealth(30);
		skel.setHealth(skel.getMaxHealth());

		skel.getEquipment().setItemInHand(killed.getItemInHand());
		skel.getEquipment().setHelmet(killed.getInventory().getHelmet());
		skel.getEquipment().setChestplate(killed.getInventory().getChestplate());
		skel.getEquipment().setLeggings(killed.getInventory().getLeggings());
		skel.getEquipment().setBoots(killed.getInventory().getBoots());

		event.GetEvent().getDrops().remove(killed.getItemInHand());
		event.GetEvent().getDrops().remove(killed.getInventory().getHelmet());
		event.GetEvent().getDrops().remove(killed.getInventory().getChestplate());
		event.GetEvent().getDrops().remove(killed.getInventory().getLeggings());
		event.GetEvent().getDrops().remove(killed.getInventory().getBoots());

		skel.getEquipment().setItemInHandDropChance(1f);
		skel.getEquipment().setHelmetDropChance(1f);
		skel.getEquipment().setChestplateDropChance(1f);
		skel.getEquipment().setLeggingsDropChance(1f);
		skel.getEquipment().setBootsDropChance(1f);

		if (_name)
		{
			skel.setCustomName("Skeletal " + UtilEnt.getName(event.GetEvent().getEntity()));
			skel.setCustomNameVisible(true);
		}

		if (!_minions.containsKey(killer))
			_minions.put(killer, new ArrayList<Skeleton>());

		_minions.get(killer).add(skel);

		killer.playSound(killer.getLocation(), Sound.SKELETON_HURT, 1f, 1f);

		Bukkit.getPluginManager().callEvent(new MinionSpawnEvent(killer, this));
	}

	@EventHandler
	public void TargetCancel(EntityTargetEvent event)
	{
		if (!_minions.containsKey(event.getTarget()))
			return;

		if (_minions.get(event.getTarget()).contains(event.getEntity()))
			event.setCancelled(true);

		for (Player player : _minions.keySet())
		{
			for (Skeleton skel : _minions.get(player))
			{
				if (event.getEntity().equals(skel))
				{
					if (UtilMath.offset(skel, player) > _maxDist)
					{

					}
				}
			}
		}
	}

	@EventHandler
	public void MinionUpdate(UpdateEvent event)
	{
		if (event.getType() != UpdateType.SEC)
			return;

		for (Player player : _minions.keySet())
		{
			Iterator<Skeleton> skelIterator = _minions.get(player).iterator();

			while (skelIterator.hasNext())
			{
				Skeleton skel = skelIterator.next();

				//Dead
				if (!skel.isValid())
				{
					skelIterator.remove();
					continue;
				}

				//Return to Owner
				double range = 4;
				if (skel.getTarget() != null || ((CraftSkeleton) skel).getHandle().getGoalTarget() != null)
				{
					range = _maxDist;
				}

				if (UtilMath.offset(skel, player) > range)
				{
					float speed = 1.25f;
					if (player.isSprinting())
						speed = 1.75f;

					//Move
					Location target = skel.getLocation().add(UtilAlg.getTrajectory(skel, player).multiply(3));

					EntityCreature ec = ((CraftCreature) skel).getHandle();
					Navigation nav = (Navigation) ec.getNavigation();
					nav.a(target.getX(), target.getY(), target.getZ(), speed);

					skel.setTarget(null);
					((CraftSkeleton) skel).getHandle().setGoalTarget(null);
				}
			}
		}
	}

	@EventHandler
	public void Heal(UpdateEvent event)
	{
		if (event.getType() != UpdateType.SEC)
			return;

		for (ArrayList<Skeleton> skels : _minions.values())
		{
			for (Skeleton skel : skels)
			{
				if (skel.getHealth() > 0)
					skel.setHealth(Math.min(skel.getMaxHealth(), skel.getHealth() + 1));
			}
		}
	}

	public boolean IsMinion(Entity ent)
	{
		for (ArrayList<Skeleton> skels : _minions.values())
		{
			for (Skeleton skel : skels)
			{
				if (ent.equals(skel))
				{
					return true;
				}
			}
		}

		return false;
	}

	@EventHandler
	public void Combust(EntityCombustEvent event)
	{
		if (IsMinion(event.getEntity()))
			event.setCancelled(true);
	}

	@EventHandler
	public void Damage(CustomDamageEvent event)
	{
		if (event.GetDamagerEntity(true) == null)
			return;

		if (!IsMinion(event.GetDamagerEntity(true)))
			return;

		double damage = 4;

		if (event.GetDamagerEntity(true) instanceof Skeleton)
		{
			Skeleton skel = (Skeleton) event.GetDamagerEntity(true);

			if (skel.getEquipment().getItemInHand() != null)
			{
				if (skel.getEquipment().getItemInHand().getType() == Material.STONE_SWORD) damage = 5;
				else if (skel.getEquipment().getItemInHand().getType() == Material.IRON_SWORD) damage = 6;
				else if (skel.getEquipment().getItemInHand().getType() == Material.GOLD_SWORD) damage = 6;
				else if (skel.getEquipment().getItemInHand().getType() == Material.DIAMOND_SWORD) damage = 7;

				else if (skel.getEquipment().getItemInHand().getType() == Material.IRON_AXE) damage = 5;
				else if (skel.getEquipment().getItemInHand().getType() == Material.GOLD_AXE) damage = 5;
				else if (skel.getEquipment().getItemInHand().getType() == Material.DIAMOND_AXE) damage = 6;
			}
		}

		if (event.GetProjectile() != null)
			damage = 6;

		event.AddMod("Skeleton Minion", "Negate", -event.GetDamageInitial(), false);
		event.AddMod("Skeleton Minion", "Damage", damage, false);
	}

	@EventHandler
	public void PlayerDeath(PlayerDeathEvent event)
	{
		ArrayList<Skeleton> skels = _minions.remove(event.getEntity());

		if (skels == null)
			return;

		for (Skeleton skel : skels)
			skel.remove();

		skels.clear();
	}

	public List<Skeleton> getSkeletons(Player player)
	{
		return _minions.get(player);
	}
}
