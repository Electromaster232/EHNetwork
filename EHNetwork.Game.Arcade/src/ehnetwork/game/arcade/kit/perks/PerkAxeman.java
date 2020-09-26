package ehnetwork.game.arcade.kit.perks;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import ehnetwork.core.common.util.C;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class PerkAxeman extends Perk
{
	public PerkAxeman() 
	{
		super("Axe Master", new String[] 
				{
				C.cGray + "Deals +1 Damage with Axes",
				});
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void AxeDamage(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;
		
		Player damager = event.GetDamagerPlayer(false);
		if (damager == null)	return;

		if (damager.getItemInHand() == null)
			return;
		
		if (!damager.getItemInHand().getType().toString().contains("_AXE"))
			return;
		
		if (!Kit.HasKit(damager))
			return;
		
		event.AddMod(damager.getName(), GetName(), 1, false);
	}
}
