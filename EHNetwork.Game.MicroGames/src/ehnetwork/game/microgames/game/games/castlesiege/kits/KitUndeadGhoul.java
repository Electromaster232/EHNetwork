package ehnetwork.game.microgames.game.games.castlesiege.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.disguise.disguises.DisguisePigZombie;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.game.microgames.kit.perks.PerkLeap;
import ehnetwork.game.microgames.kit.perks.PerkSpeed;

public class KitUndeadGhoul extends KitUndead
{
	public KitUndeadGhoul(MicroGamesManager manager)
	{
		super(manager, "Undead Ghoul", KitAvailability.Free, 

				new String[] 
						{
				"Weak, but able to jump around with ease."
						}, 

						new Perk[] 
								{
				new PerkLeap("Ghoul Leap", 1.2, 0.8, 8000),
				new PerkSpeed(0)
								}, 
								EntityType.PIG_ZOMBIE,
								new ItemStack(Material.STONE_AXE));
	}
	
	@Override 
	public void GiveItems(Player player)
	{
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.STONE_AXE));
		
		DisguisePigZombie disguise = new DisguisePigZombie(player);
		disguise.setName(Manager.GetGame().GetTeam(player).GetColor() + player.getName());
		disguise.setCustomNameVisible(true);
		Manager.GetDisguise().disguise(disguise);
	}

	@EventHandler
	public void PickupArrow(PlayerPickupItemEvent event)
	{
		if (!HasKit(event.getPlayer()))
			return;
		
		if (event.getItem().getItemStack().getType() == Material.ARROW)
			event.setCancelled(true);
	}
}
