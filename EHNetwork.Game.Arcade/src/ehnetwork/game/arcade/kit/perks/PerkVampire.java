package ehnetwork.game.arcade.kit.perks;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.game.arcade.game.Game;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.minecraft.game.core.combat.event.CombatDeathEvent;

public class PerkVampire extends Perk
{
	private int _recover;
	
	public PerkVampire(int recover) 
	{
		super("Vampire", new String[] 
				{ 
				C.cGray + "You heal " + recover + "HP when you kill someone",
				});
		
		_recover = recover;
	}
		
	@EventHandler
	public void PlayerKillAward(CombatDeathEvent event)
	{
		Game game = Manager.GetGame();
		if (game == null)	return;

		if (!(event.GetEvent().getEntity() instanceof Player))
			return;

		if (event.GetLog().GetKiller() == null)
			return;

		Player killer = UtilPlayer.searchExact(event.GetLog().GetKiller().GetName());
		if (killer == null)
			return;

		UtilPlayer.health(killer, _recover);
	}
}
