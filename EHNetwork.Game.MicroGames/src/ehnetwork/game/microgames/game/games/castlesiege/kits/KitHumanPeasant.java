package ehnetwork.game.microgames.game.games.castlesiege.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.disguise.disguises.DisguiseWolf;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.game.microgames.kit.perks.PerkKnockbackGive;
import ehnetwork.game.microgames.kit.perks.PerkStrength;

public class KitHumanPeasant extends KitHuman
{
	public KitHumanPeasant(MicroGamesManager manager)
	{
		super(manager, "Castle Wolf", KitAvailability.Hide, 

				new String[] 
						{
				"OINK! OINK!"
						}, 

						new Perk[] 
								{
				new PerkStrength(1),
				new PerkKnockbackGive(2)
								}, 

								EntityType.ZOMBIE, new ItemStack(Material.IRON_HOE));

	}
	
	@EventHandler
	public void FireItemResist(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FAST)
			return;
		
		if (Manager.GetGame() == null)
			return;
			
		for (Player player : Manager.GetGame().GetPlayers(true))
		{
			if (!HasKit(player))
				continue;
			
			Manager.GetCondition().Factory().FireItemImmunity(GetName(), player, player, 1.9, false);
		}
	}
	
	@Override 
	public void GiveItems(Player player)
	{
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.BONE, (byte)0, 0, "Wolf Bite"));
		
		player.setHealth(4);
		
		DisguiseWolf disguise = new DisguiseWolf(player);
		disguise.setName(Manager.GetGame().GetTeam(player).GetColor() + player.getName());
		disguise.setCustomNameVisible(true);
		Manager.GetDisguise().disguise(disguise);
	}
}
