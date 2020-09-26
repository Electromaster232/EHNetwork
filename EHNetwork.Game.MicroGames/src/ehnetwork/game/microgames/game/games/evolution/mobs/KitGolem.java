package ehnetwork.game.microgames.game.games.evolution.mobs;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.disguise.disguises.DisguiseIronGolem;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.game.microgames.kit.perks.PerkSlam;

public class KitGolem extends Kit
{
	public KitGolem(MicroGamesManager manager)
	{
		super(manager, "Iron Golem", KitAvailability.Hide, 

				new String[] 
						{
				""
						}, 

						new Perk[] 
								{
						new PerkSlam("Ground Pound", 1, 1.2, 10000),
								}, 
								EntityType.SLIME,
								null);

	}

	@Override
	public void GiveItems(Player player) 
	{
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.DIAMOND_AXE));

		UtilPlayer.message(player, C.Line);
		UtilPlayer.message(player, C.Bold + "You evolved into " + F.elem(C.cGreen + C.Bold + GetName()) + "!");	
		UtilPlayer.message(player, F.elem("Right-Click with Axe") + " to use " + F.elem("Ground Pound"));
		UtilPlayer.message(player, C.Line);
		
		player.getWorld().playSound(player.getLocation(), Sound.IRONGOLEM_DEATH, 4f, 1f);
		
		//Disguise
		DisguiseIronGolem disguise = new DisguiseIronGolem(player);
		disguise.setName(Manager.GetGame().GetTeam(player).GetColor() + player.getName());
		disguise.setCustomNameVisible(true);
		Manager.GetDisguise().disguise(disguise);
	}
}
