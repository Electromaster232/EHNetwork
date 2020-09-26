package ehnetwork.game.microgames.game.games.squidshooter.kits;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.common.util.C;
import ehnetwork.core.disguise.disguises.DisguiseSquid;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.game.microgames.kit.perks.PerkSquidRifle;
import ehnetwork.game.microgames.kit.perks.PerkSquidSwim;

public class KitRifle extends Kit
{
	public KitRifle(MicroGamesManager manager)
	{
		super(manager, "Squid Gunner", KitAvailability.Free, 

				new String[] 
						{ 
				"All rounder squid! Fast projectile and reload!"
						}, 

						new Perk[] 
								{
				new PerkSquidSwim(),
				new PerkSquidRifle()
								}, 
								EntityType.SQUID,
								new ItemStack(Material.INK_SACK));
	}

	@Override
	public void GiveItems(Player player) 
	{
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_AXE));
		
		ItemStack helm = ItemStackFactory.Instance.CreateStack(Material.DIAMOND_HELMET);
		helm.addEnchantment(Enchantment.OXYGEN, 3);
		player.getInventory().setHelmet(helm);

		//Disguise
		DisguiseSquid disguise = new DisguiseSquid(player);
		disguise.setName(C.cWhite + player.getName());
		disguise.setCustomNameVisible(true);
		Manager.GetDisguise().disguise(disguise);
	}
}
