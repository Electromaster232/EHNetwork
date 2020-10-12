package ehnetwork.minecraft.game.classcombat.Skill.Assassin;

import java.util.HashMap;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilAlg;
import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.common.util.UtilParticle;
import ehnetwork.core.common.util.UtilParticle.ParticleType;
import ehnetwork.core.common.util.UtilParticle.ViewDist;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.common.util.UtilTime;
import ehnetwork.core.common.util.UtilWorld;
import ehnetwork.minecraft.game.classcombat.Class.IPvpClass;
import ehnetwork.minecraft.game.classcombat.Skill.SkillActive;
import ehnetwork.minecraft.game.classcombat.Skill.SkillFactory;

public class Blink extends SkillActive
{
	private HashMap<Player, Location> _loc = new HashMap<Player, Location>();
	private HashMap<Player, Long> _blinkTime = new HashMap<Player, Long>();

	public Blink(SkillFactory skills, String name, IPvpClass.ClassType classType, SkillType skillType,
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
				"Instantly teleport forwards #9#3 Blocks.",
				"Cannot be used while Slowed.",
				"",
				"Using again within 5 seconds De-Blinks,",
				"returning you to your original location.",
				"Can be used while Slowed."
				});
	}

	@Override
	public boolean CustomCheck(Player player, int level) 
	{
		if (player.hasPotionEffect(PotionEffectType.SLOW))
		{
			UtilPlayer.message(player, F.main(GetClassType().name(), "You cannot use " + F.skill(GetName()) + " while Slowed."));
			return false;
		}

		if (player.getLocation().getBlock().getTypeId() == 8 || player.getLocation().getBlock().getTypeId() == 9)
		{
			UtilPlayer.message(player, F.main("Skill", "You cannot use " + F.skill(GetName()) + " in water."));
			return false;
		}

		//Deblink
		if (_loc.containsKey(player) && _blinkTime.containsKey(player))
			if (!UtilTime.elapsed(_blinkTime.get(player), 5000))
			{
				//Require 500ms after blink to deblink
				if (UtilTime.elapsed(_blinkTime.get(player), 500))
					Deblink(player, level);
				
				return false;
			}		

		return true;
	}

	@Override
	public void Skill(Player player, int level) 
	{
		//Smoke Trail
		Block lastSmoke = player.getLocation().getBlock();

		double maxRange = 9 + (level*3);
		double curRange = 0;
		while (curRange <= maxRange)
		{
			Location newTarget = player.getLocation().add(new Vector(0,0.2,0)).add(player.getLocation().getDirection().multiply(curRange));

			if (!UtilBlock.airFoliage(newTarget.getBlock()) || !UtilBlock.airFoliage(newTarget.getBlock().getRelative(BlockFace.UP)))
				break;

			//Lock Players
			for (Player cur : player.getWorld().getPlayers())
			{
				if (cur.equals(player))
					continue;

				if (UtilMath.offset(newTarget, cur.getLocation()) > 1)
					continue;

				//Action
				Location target = cur.getLocation().add(player.getLocation().subtract(cur.getLocation()).toVector().normalize());
				player.teleport(UtilWorld.locMerge(player.getLocation(), target));

				//Inform
				UtilPlayer.message(player, F.main(GetClassType().name(), "You used " + F.skill(GetName()) + "."));

				//Effect
				player.getWorld().playEffect(player.getLocation(), Effect.BLAZE_SHOOT, 0);
				return;
			}

			//Progress Forwards
			curRange += 0.2;

			//Smoke Trail
			UtilParticle.PlayParticle(ParticleType.LARGE_SMOKE, newTarget.clone().add(0, 0.5, 0), 0, 0, 0, 0, 1,
					ViewDist.LONGER, UtilServer.getPlayers());

			lastSmoke = newTarget.getBlock();
		}

		//Modify Range
		curRange -= 0.4;
		if (curRange < 0)
			curRange = 0;

		//Destination
		Location loc = player.getLocation().add(player.getLocation().getDirection().multiply(curRange).add(new Vector(0, 0.4, 0)));
		_loc.put(player, player.getLocation());

		//Action
		if (curRange > 0)
		{
			player.leaveVehicle();
			player.teleport(loc);
		}

		player.setFallDistance(0);

		//Inform
		UtilPlayer.message(player, F.main(GetClassType().name(), "You used " + F.skill(GetName()) + "."));

		//Effect
		player.getWorld().playEffect(player.getLocation(), Effect.BLAZE_SHOOT, 0);

		//Record
		_blinkTime.put(player, System.currentTimeMillis());
	}

	public void Deblink(Player player, int level)
	{
		UtilPlayer.message(player, F.main(GetClassType().name(), "You used " + F.skill("De-Blink") + "."));

		//Smoke Trail
		Block lastSmoke = player.getLocation().getBlock();

		double curRange = 0;

		Location target = _loc.remove(player);

		boolean done = false;
		while (!done)
		{
			Vector vec = UtilAlg.getTrajectory(player.getLocation(), 
					new Location(player.getWorld(), target.getX(), target.getY(), target.getZ()));

			Location newTarget = player.getLocation().add(vec.multiply(curRange));

			//Progress Forwards
			curRange += 0.2;

			//Smoke Trail
			UtilParticle.PlayParticle(ParticleType.LARGE_SMOKE, newTarget.clone().add(0, 0.5, 0), 0, 0, 0, 0, 1,
					ViewDist.LONGER, UtilServer.getPlayers());
				
			lastSmoke = newTarget.getBlock();

			if (UtilMath.offset(newTarget, target) < 0.4)
						done = true;
					
					if (curRange > 24)
				done = true;

			if (curRange > 24)
				done = true;
		}

		player.teleport(target);
		
		player.setFallDistance(0);

		//Effect
		player.getWorld().playEffect(player.getLocation(), Effect.BLAZE_SHOOT, 0);
	}

	@Override
	public void Reset(Player player) 
	{
		_loc.remove(player);
		_blinkTime.remove(player);
	}
}
