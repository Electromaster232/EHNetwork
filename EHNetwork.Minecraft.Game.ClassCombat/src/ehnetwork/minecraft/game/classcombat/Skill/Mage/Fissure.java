package ehnetwork.minecraft.game.classcombat.Skill.Mage;

import java.util.HashSet;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;

import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.minecraft.game.classcombat.Class.IPvpClass;
import ehnetwork.minecraft.game.classcombat.Skill.SkillActive;
import ehnetwork.minecraft.game.classcombat.Skill.SkillFactory;

public class Fissure extends SkillActive 
{
	private HashSet<FissureData> _active = new HashSet<FissureData>();
	
	public Fissure(SkillFactory skills, String name, IPvpClass.ClassType classType, SkillType skillType,
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
				"Fissures the earth infront of you,",
				"creating an impassable wall.",
				"",
				"Players struck by the initial slam",
				"receive Slow 2 for #2#0.5 seconds",
				"",
				"Players struck by the fissure",
				"receive #2#0.4 damage plus an ",
				"additional #0.6#0.2 damage for",
				"every block fissure has travelled."
				});
	}

	@Override
	public boolean CustomCheck(Player player, int level) 
	{
		if (player.getLocation().getBlock().getTypeId() == 8 || player.getLocation().getBlock().getTypeId() == 9)
		{
			UtilPlayer.message(player, F.main("Skill", "You cannot use " + F.skill(GetName()) + " in water."));
			return false;
		}
		
		if (!UtilEnt.isGrounded(player))
		{
			UtilPlayer.message(player, F.main("Skill", "You cannot use " + F.skill(GetName()) + " while airborne."));
			return false;
		}

		return true;
	}

	@Override
	public void Skill(Player player, int level) 
	{
		FissureData data = new FissureData(this, player, level, player.getLocation().getDirection(), player.getLocation().add(0, -0.4, 0));
		_active.add(data);
		
		//Inform
		UtilPlayer.message(player, F.main(GetClassType().name(), "You used " + F.skill(GetName(level)) + "."));
	}
	
	@EventHandler
	public void Update(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;
		
		HashSet<FissureData> remove = new HashSet<FissureData>();
		
		for (FissureData data : _active)
			if (data.Update())
				remove.add(data);
		
		for (FissureData data : remove)
		{
			_active.remove(data);
			data.Clear();
		}	
	}

	@Override
	public void Reset(Player player) 
	{

	}
}
