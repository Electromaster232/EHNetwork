package ehnetwork.minecraft.game.classcombat.Skill.Assassin;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilAction;
import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.common.util.UtilParticle;
import ehnetwork.core.common.util.UtilParticle.ParticleType;
import ehnetwork.core.common.util.UtilParticle.ViewDist;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.disguise.disguises.DisguisePlayer;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.minecraft.game.classcombat.Class.IPvpClass;
import ehnetwork.minecraft.game.classcombat.Skill.SkillActive;
import ehnetwork.minecraft.game.classcombat.Skill.SkillFactory;
import ehnetwork.minecraft.game.classcombat.event.ClassCombatCreatureAllowSpawnEvent;
import ehnetwork.minecraft.game.core.condition.Condition.ConditionType;

public class Illusion extends SkillActive
{
	private HashMap<Player, Skeleton> _active = new HashMap<Player, Skeleton>();

	public Illusion(SkillFactory skills, String name, IPvpClass.ClassType classType, SkillType skillType,
					int cost, int levels,
					int energy, int energyMod,
					long recharge, long rechargeMod, boolean rechargeInform,
					Material[] itemArray,
					Action[] actionArray)
	{
		super(skills, name, classType, skillType,
				cost, levels,
				energy, energyMod, 
				recharge, rechargeMod, rechargeInform, 
				itemArray,
				actionArray);

		SetDesc(new String[] 
				{
				"Hold Block to go invisible and create an",
				"illusion of yourself that runs towards",
				"your target location.",
				"",
				"Invisibility ends if you release Block",
				"or your Illusion is killed.",
				"",
				"Illusion lasts up to #2#1 seconds.",
				"",
				"Gives Slow 2 for up to 4 seconds",
				"to nearby players upon ending."
				});
		
		setAchievementSkill(true);
	}
	
	@Override
	public String GetEnergyString()
	{
		return "Energy: #40#-3 and #12.5#-0.5 per Second";
	}

	@Override
	public boolean CustomCheck(Player player, int level) 
	{
		if (player.getLocation().getBlock().getTypeId() == 8 || player.getLocation().getBlock().getTypeId() == 9)
		{
			UtilPlayer.message(player, F.main("Skill", "You cannot use " + F.skill(GetName()) + " in water."));
			return false;
		}
		
		return true;
	}

	@Override
	public void Skill(Player player, int level) 
	{
		//Spawn
		ClassCombatCreatureAllowSpawnEvent enableEvent = new ClassCombatCreatureAllowSpawnEvent(true);
		UtilServer.getServer().getPluginManager().callEvent(enableEvent);
		
		Skeleton skel = player.getWorld().spawn(player.getLocation(), Skeleton.class);
		skel.teleport(player.getLocation());
		UtilEnt.Vegetate(skel);
		UtilEnt.silence(skel, true);
		
		ClassCombatCreatureAllowSpawnEvent disableEvent = new ClassCombatCreatureAllowSpawnEvent(false);
		UtilServer.getServer().getPluginManager().callEvent(disableEvent);
		
		skel.getEquipment().setHelmet(player.getInventory().getHelmet());
		skel.getEquipment().setChestplate(player.getInventory().getChestplate());
		skel.getEquipment().setLeggings(player.getInventory().getLeggings());
		skel.getEquipment().setBoots(player.getInventory().getBoots());
		skel.getEquipment().setItemInHand(Math.random() > 0.5 ? player.getItemInHand() : new ItemStack(Material.IRON_AXE));

		//Get in range
		ArrayList<Player> inRange = new ArrayList<Player>();
		for (Player other : UtilServer.getPlayers())
			if (UtilMath.offset2d(skel, other) < 70)
				inRange.add(other);
				
		//Disguise
		DisguisePlayer disguise = new DisguisePlayer(skel, ((CraftPlayer)player).getHandle().getProfile());
		Factory.Disguise().disguise(disguise, inRange.toArray(new Player[inRange.size()]));

		//Invis
		Factory.Condition().Factory().Cloak(GetName(), player, player, 2 + 1*level, false, true);
		
		_active.put(player, skel);

		//Inform
		UtilPlayer.message(player, F.main(GetClassType().name(), "You used " + F.skill(GetName()) + "."));
	}

	@EventHandler
	public void update(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		for (Player cur : GetUsers())
		{
			if (!_active.containsKey(cur))
				continue;

			Skeleton skel = _active.get(cur);
			
			if (Factory.Condition().GetActiveCondition(cur, ConditionType.CLOAK) == null || 
				!cur.isBlocking() || 
				!Factory.Energy().Use(cur, GetName(), 0.625 - (getLevel(cur) * 0.025), true, true) ||
				skel == null ||
				!skel.isValid())
			{
				end(cur);
				continue;
			}
			else
			{
				if (UtilEnt.isGrounded(skel) &&
					(!UtilBlock.airFoliage(skel.getLocation().add(skel.getLocation().getDirection()).getBlock()) ||
					!UtilBlock.airFoliage(skel.getLocation().add(skel.getLocation().getDirection().multiply(2)).getBlock())))
				{
					UtilAction.velocity(skel, 0.6, 0.4, 1, false);
				}
				
				UtilEnt.CreatureMoveFast(skel, cur.getTargetBlock(null, 0).getLocation().add(0, 2, 0), 1.8f);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void illusionDeath(EntityDeathEvent event)
	{
		if (_active.containsValue(event.getEntity()))
		{
			event.getDrops().clear();
			event.getEntity().remove();
		}
	}
	
	private void end(Player player)
	{
		Factory.Condition().EndCondition(player, null, GetName());
		
		Skeleton skel = _active.remove(player);
		if (skel == null)
			return;
					
		//Level
		int level = getLevel(player);
		
		//Blind
		HashMap<LivingEntity, Double> targets = UtilEnt.getInRadius(skel.getLocation(), 6d + 0.5 * level);
		for (LivingEntity cur : targets.keySet())
		{
			if (cur.equals(player))
				continue;

			//Condition
			Factory.Condition().Factory().Slow(GetName(), cur, player, 4 * targets.get(cur), 1, false, false, false, false);
		}
		
		//Effect
		UtilParticle.PlayParticle(ParticleType.LARGE_SMOKE, skel.getLocation().add(0, 1, 0), 0.3f, 0.3f, 0.3f, 0.06f, 30,
				ViewDist.LONGER, UtilServer.getPlayers());
		
		for (int i=0 ; i<2 ; i++)
			skel.getWorld().playSound(skel.getLocation(), Sound.FIZZ, 2f, 0.4f);
		
		skel.getEquipment().clear();
		skel.remove();
	}

	@Override
	public void Reset(Player player) 
	{
		end(player);
	}
}
