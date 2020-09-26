package ehnetwork.game.arcade.game.games.wither.kit;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.disguise.disguises.DisguiseWither;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.kit.Kit;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.game.arcade.kit.perks.PerkWitherArrows;
import ehnetwork.game.arcade.kit.perks.PerkWitherAttack;
import ehnetwork.game.arcade.kit.perks.PerkWitherWeb;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class KitWitherWeb extends Kit
{
	public KitWitherWeb(ArcadeManager manager)
	{
		super(manager, "Wither Trapper", KitAvailability.Free, 

				new String[] 
						{
				""
						}, 

						new Perk[] 
								{
				new PerkWitherArrows(),
				new PerkWitherAttack(),
				new PerkWitherWeb(),
								}, 
								EntityType.WITHER,
								null);

	}

	@Override
	public void GiveItems(Player player) 
	{
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.GOLD_SWORD, (byte)0, 1, 
				C.cYellow + C.Bold + "Left-Click" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Wither Skull"));
		
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_SWORD, (byte)0, 1, 
				C.cYellow + C.Bold + "Left-Click" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Web Blast"));

		//Disguise
		DisguiseWither disguise = new DisguiseWither(player);

		if (Manager.GetGame().GetTeam(player) != null)		
			disguise.setName(Manager.GetGame().GetTeam(player).GetColor() + player.getName());
		else			
			disguise.setName(player.getName());

		disguise.setCustomNameVisible(true);
		Manager.GetDisguise().disguise(disguise);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void witherDamageCancel(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;
		
		Player player = event.GetDamageePlayer();
		if (player == null)
			return;
		
		if (HasKit(player))
			event.SetCancelled("Wither Immunity");
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void witherMeleeCancel(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;
		
		Player player = event.GetDamagerPlayer(true);
		if (player == null)
			return;
		
		if (!HasKit(player))
			return;

		if (event.GetCause() != DamageCause.ENTITY_ATTACK)
			return;

		event.SetCancelled("Wither Melee Cancel");
	}

	@EventHandler
	public void witherFlight(UpdateEvent event)
	{
		for (Player player : UtilServer.getPlayers())
		{
			if (!HasKit(player))
				continue;

			if (player.isFlying())
				continue;

			player.setAllowFlight(true);
			player.setFlying(true);
		}
	}
}
