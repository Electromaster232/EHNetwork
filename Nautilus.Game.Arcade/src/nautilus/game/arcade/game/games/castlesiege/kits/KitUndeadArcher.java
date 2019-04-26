package nautilus.game.arcade.game.games.castlesiege.kits;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import mineplex.core.common.util.UtilInv;
import mineplex.core.disguise.disguises.DisguiseSkeleton;
import mineplex.core.itemstack.ItemStackFactory;
import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.kit.Kit;
import nautilus.game.arcade.kit.KitAvailability;
import nautilus.game.arcade.kit.Perk;
import nautilus.game.arcade.kit.perks.PerkIronSkin;

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
		
		DisguiseSkeleton disguise = new DisguiseSkeleton(player);
		disguise.setName(Manager.GetGame().GetTeam(player).GetColor() + player.getName());
		disguise.setCustomNameVisible(true);
		Manager.GetDisguise().disguise(disguise);
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
