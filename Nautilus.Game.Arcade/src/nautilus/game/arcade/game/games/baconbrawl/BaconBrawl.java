package nautilus.game.arcade.game.games.baconbrawl;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import mineplex.core.common.util.UtilPlayer;
import mineplex.core.itemstack.ItemStackFactory;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.minecraft.game.core.damage.CustomDamageEvent;
import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.GameType;
import nautilus.game.arcade.game.SoloGame;
import nautilus.game.arcade.game.games.baconbrawl.kits.*;
import nautilus.game.arcade.kit.Kit;

public class BaconBrawl extends SoloGame
{
	public BaconBrawl(ArcadeManager manager) 
	{
		super(manager, GameType.BaconBrawl,

				new Kit[]
						{
				new KitPig(manager),
				new KitMamaPig(manager),
				new KitSheepPig(manager)
						},

						new String[]
								{
				"Knock other pigs out of the arena!",
				"Last pig in the arena wins!" 
								});
	
		this.DamageTeamSelf = true;
		this.HungerSet = 20;
		this.PrepareFreeze = false;
	}
	
	@EventHandler
	public void Hunger(UpdateEvent event)
	{
		if (event.getType() != UpdateType.SLOW)
			return;
		
		if (!IsLive())
			return;
		
		for (Player player : GetPlayers(true))
		{
			if (player.getFoodLevel() <= 0)
			{
				Manager.GetDamage().NewDamageEvent(player, null, null, 
						DamageCause.STARVATION, 4, false, true, false,
						"Starvation", GetName());
			}
			
			UtilPlayer.hunger(player, -1);
		}
	}
	
	@EventHandler
	public void HungerRestore(CustomDamageEvent event)
	{
		Player damager = event.GetDamagerPlayer(true);
		if (damager != null)
			UtilPlayer.hunger(damager, 2);
	}
	
	@EventHandler
	public void DamageEvent(CustomDamageEvent event)
	{
		if (event.GetCause() == DamageCause.ENTITY_ATTACK || event.GetCause() == DamageCause.CUSTOM  || event.GetCause() == DamageCause.PROJECTILE)
		{
			event.GetDamageeEntity().setHealth(event.GetDamageeEntity().getMaxHealth());
			event.AddKnockback("Pig Wrestle", 1.5 + ((System.currentTimeMillis() - GetStateTime()) / 120000d));
		}
	}
}
