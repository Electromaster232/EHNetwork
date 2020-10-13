package ehnetwork.game.arcade.game.games.castlesiege.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import de.robingrether.idisguise.disguise.Disguise;
import de.robingrether.idisguise.disguise.MobDisguise;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.game.arcade.kit.perks.PerkLeap;
import ehnetwork.game.arcade.kit.perks.PerkSpeed;

public class KitUndeadGhoul extends KitUndead
{
	public KitUndeadGhoul(ArcadeManager manager)
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

		Disguise d1 = Manager.GetDisguise().createDisguise(EntityType.PIG_ZOMBIE);
		MobDisguise disguise = (MobDisguise) d1;
		disguise.setCustomName(Manager.GetGame().GetTeam(player).GetColor() + player.getName());
		disguise.setCustomNameVisible(true);
		Manager.GetDisguise().applyDisguise(disguise, player);
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
