package ehnetwork.game.arcade.game.games.castlesiege.kits;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import de.robingrether.idisguise.disguise.Disguise;
import de.robingrether.idisguise.disguise.MobDisguise;
import ehnetwork.core.common.util.UtilInv;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.game.arcade.kit.perks.PerkIronSkin;

public class KitUndeadArcher extends KitUndead
{
	public KitUndeadArcher(ArcadeManager manager)
	{
		super(manager, "Undead Archer", KitAvailability.Gem, 

				new String[] 
						{
				"Makes use of arrows scavenged from human archers."
						}, 

						new Perk[] 
								{
				new PerkIronSkin(1)
								}, 
								EntityType.SKELETON,
								new ItemStack(Material.BOW));

	}
	
	@Override
	public void GiveItems(Player player)
	{
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.STONE_AXE));
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.BOW));

		Disguise d1 = Manager.GetDisguise().createDisguise(EntityType.SKELETON);
		MobDisguise disguise = (MobDisguise) d1;
		disguise.setCustomName(Manager.GetGame().GetTeam(player).GetColor() + player.getName());
		disguise.setCustomNameVisible(true);
		Manager.GetDisguise().applyDisguise(disguise, player);
	}
	
	@EventHandler
	public void ArrowPickup(PlayerPickupItemEvent event)
	{
		if (event.getItem().getItemStack().getType() != Material.ARROW)
			return;
		
		if (!HasKit(event.getPlayer()))
			return;
		
		if (UtilInv.contains(event.getPlayer(), Material.ARROW, (byte)0, 4))
			return;
		
		event.getItem().remove();
		
		event.getPlayer().getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.ARROW));
		
		event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ITEM_PICKUP, 1f, 1f);
	}
}
