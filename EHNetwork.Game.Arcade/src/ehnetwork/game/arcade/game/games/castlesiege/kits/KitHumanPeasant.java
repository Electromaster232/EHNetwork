package ehnetwork.game.arcade.game.games.castlesiege.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import de.robingrether.idisguise.disguise.Disguise;
import de.robingrether.idisguise.disguise.WolfDisguise;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.game.arcade.kit.perks.PerkKnockbackGive;
import ehnetwork.game.arcade.kit.perks.PerkStrength;

public class KitHumanPeasant extends KitHuman
{
	public KitHumanPeasant(ArcadeManager manager)
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
		
		Disguise d1 = Manager.GetDisguise().createDisguise(EntityType.WOLF);
		WolfDisguise disguise = (WolfDisguise) d1;
		disguise.setCustomName(Manager.GetGame().GetTeam(player).GetColor() + player.getName());
		disguise.setCustomNameVisible(true);
		Manager.GetDisguise().applyDisguise(disguise, player);
	}
}
