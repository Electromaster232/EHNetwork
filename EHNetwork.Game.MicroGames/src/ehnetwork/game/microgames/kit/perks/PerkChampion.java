package ehnetwork.game.microgames.kit.perks;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.game.microgames.game.Game;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.minecraft.game.core.combat.event.CombatDeathEvent;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class PerkChampion extends Perk
{
	private HashMap<Player, Integer> _bonus = new HashMap<Player, Integer>();
	
	public PerkChampion() 
	{
		super("Champion", new String[] 
				{
				C.cGray + "You get stronger with each kill",
				});
	}

	@EventHandler
	public void kill(CombatDeathEvent event)
	{
		Game game = Manager.GetGame();
		if (game == null)	
			return;

		if (!(event.GetEvent().getEntity() instanceof Player))
			return;

		Player killed = (Player)event.GetEvent().getEntity();

		if (event.GetLog().GetKiller() != null)
		{
			Player killer = UtilPlayer.searchExact(event.GetLog().GetKiller().GetName());

			if (killer != null && !killer.equals(killed))
			{
				int past = 0;
				if (_bonus.containsKey(killer))
					past = _bonus.get(killer);
				
				_bonus.put(killer, past + 1);
				
				UtilPlayer.message(killer, F.main("Game", "Bonus Damage: " + F.elem((past + 1)+"")));
			}
		}
	}
	
	@EventHandler
	public void damageBonus(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;

		if (event.GetCause() != DamageCause.ENTITY_ATTACK && event.GetCause() != DamageCause.PROJECTILE)
			return;

		Player damager = event.GetDamagerPlayer(true);
		if (damager == null)	
			return;

		if (!_bonus.containsKey(damager))
			return;
		
		int bonus = _bonus.get(damager);
		
		event.AddMod(GetName(), GetName(), bonus, false);
	}
}
