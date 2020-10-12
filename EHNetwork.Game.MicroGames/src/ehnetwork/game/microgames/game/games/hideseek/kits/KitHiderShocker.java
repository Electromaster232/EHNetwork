package ehnetwork.game.microgames.game.games.hideseek.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.common.util.C;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.game.microgames.kit.perks.PerkShockingStrike;

public class KitHiderShocker extends KitHider
{
	public KitHiderShocker(MicroGamesManager manager)
	{
		super(manager, "Shocking Hider", KitAvailability.Gem, 5000,

				new String[] 
						{ 
				"Shock and stun seekers!"
						}, 

						new Perk[] 
								{
				new PerkShockingStrike()
								}, 
								EntityType.SLIME,
								new ItemStack(Material.REDSTONE_LAMP_OFF));
	}

	@Override
	public void GiveItems(Player player) 
	{
		player.getInventory().setItem(3, ItemStackFactory.Instance.CreateStack(Material.SLIME_BALL, (byte)0, 1, C.cYellow + C.Bold + "Click Block" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Change Form"));
	}
	
	@Override
	public void SpawnCustom(LivingEntity ent) 
	{
		((Slime)ent).setSize(2);
		
		//Manager.GetDisguise().disguise(new DisguiseBlock(ent, 61, 0));
	}
}
