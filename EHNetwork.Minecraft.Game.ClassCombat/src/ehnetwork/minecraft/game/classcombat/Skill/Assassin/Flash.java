package ehnetwork.minecraft.game.classcombat.Skill.Assassin;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.common.util.UtilParticle;
import ehnetwork.core.common.util.UtilParticle.ParticleType;
import ehnetwork.core.common.util.UtilParticle.ViewDist;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.minecraft.game.classcombat.Class.IPvpClass;
import ehnetwork.minecraft.game.classcombat.Skill.SkillActive;
import ehnetwork.minecraft.game.classcombat.Skill.SkillFactory;

public class Flash extends SkillActive
{
	private HashMap<Player, Integer> _flash = new HashMap<Player, Integer>();

	public Flash(SkillFactory skills, String name, IPvpClass.ClassType classType, SkillType skillType,
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
				"Teleport forwards 6 Blocks.",
				"Store up to #1#1 Flash Charges.",
				"Cannot be used while Slowed."
				});
	}
	
	@Override
	public String GetRechargeString()
	{
		return "Recharge: #10#-1 Seconds per Charge";
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

		//No Flash
		if (!_flash.containsKey(player) || _flash.get(player) == 0)
		{
			UtilPlayer.message(player, F.main("Skill", "You have no " + F.skill(GetName() + " Charges") + "."));
			return false;
		}

		return true;
	}

	@EventHandler
	public void Recharge(UpdateEvent event)
	{
		for (Player cur : GetUsers())
		{
			if (!_flash.containsKey(cur))
			{
				_flash.put(cur, 0);
			}
			else
			{
				int charges = _flash.get(cur);
				int level = getLevel(cur);
			
				if (charges >= 1 + level)
					continue;

				if (!Recharge.Instance.use(cur, "Flash Recharge", 10000 - (1000 * level), false, false))
					continue;

				_flash.put(cur, charges + 1);

				//Inform
				UtilPlayer.message(cur, F.main(GetClassType().name(), "Flash Charges: " + F.elem((charges + 1)+"")));
			}
		}
			
	}

	@Override
	public void Skill(Player player, int level) 
	{
		//Use Recharge
		Recharge.Instance.use(player, "Flash Recharge", 8000, false, false);

		_flash.put(player, _flash.get(player) - 1);

		double maxRange = 6;
		double curRange = 0;
		while (curRange <= maxRange)
		{
			Location newTarget = player.getLocation().add(new Vector(0,0.2,0)).add(player.getLocation().getDirection().multiply(curRange));

			if (!UtilBlock.airFoliage(newTarget.getBlock()) || 
				!UtilBlock.airFoliage(newTarget.getBlock().getRelative(BlockFace.UP)))
				break;

			//Progress Forwards
			curRange += 0.2;

			//Smoke Trail
			UtilParticle.PlayParticle(ParticleType.FIREWORKS_SPARK, newTarget.clone().add(0, 0.5, 0), 0, 0, 0, 0, 1,
					ViewDist.LONGER, UtilServer.getPlayers());
		}

		//Modify Range
		curRange -= 0.4;
		if (curRange < 0)
			curRange = 0;

		//Destination
		Location loc = player.getLocation().add(player.getLocation().getDirection().multiply(curRange).add(new Vector(0, 0.4, 0)));

		if (curRange > 0)
			player.teleport(loc);

		player.setFallDistance(0);

		//Inform
		UtilPlayer.message(player, F.main(GetClassType().name(), "Flash Charges: " + F.elem(_flash.get(player)+"")));

		//Effect
		player.getWorld().playSound(player.getLocation(), Sound.WITHER_SHOOT, 0.4f, 1.2f);
		player.getWorld().playSound(player.getLocation(), Sound.SILVERFISH_KILL, 1f, 1.6f);
		
	}

	@Override
	public void Reset(Player player) 
	{
		_flash.remove(player);
	}
}
