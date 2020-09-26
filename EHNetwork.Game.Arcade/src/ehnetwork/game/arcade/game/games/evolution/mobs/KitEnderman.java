package ehnetwork.game.arcade.game.games.evolution.mobs;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.kit.Kit;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.game.arcade.kit.perks.PerkBlink;

public class KitEnderman extends Kit
{
	public KitEnderman(ArcadeManager manager)
	{
		super(manager, "Enderman", KitAvailability.Hide, 

				new String[] 
						{
				""
						}, 

						new Perk[] 
								{
				new PerkBlink("Blink", 12, 4000)
								}, 
								EntityType.SLIME,
								null);

	}

	@Override
	public void GiveItems(Player player) 
	{
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_AXE));

		UtilPlayer.message(player, C.Line);
		UtilPlayer.message(player, C.Bold + "You evolved into " + F.elem(C.cGreen + C.Bold + GetName()) + "!");	
		UtilPlayer.message(player, F.elem("Right-Click with Axe") + " to use " + F.elem("Blink"));
		UtilPlayer.message(player, C.Line);
		
		player.getWorld().playSound(player.getLocation(), Sound.ENDERMAN_IDLE, 4f, 1f);
		
		
	}
}
