package ehnetwork.game.microgames.game.games.evolution.mobs;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.disguise.disguises.DisguiseCreeper;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.game.microgames.kit.perks.PerkExplode;
import ehnetwork.game.microgames.kit.perks.PerkFood;

public class KitCreeper extends Kit
{
	public KitCreeper(MicroGamesManager manager)
	{
		super(manager, "Creeper", KitAvailability.Hide, 

				new String[] 
						{
				""
						}, 

						new Perk[] 
								{
				new PerkFood(1),
				new PerkExplode("Detonate", 1, 6000)
								}, 
								EntityType.SLIME,
								null);

	}

	@Override
	public void GiveItems(Player player) 
	{
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.TNT));

		UtilPlayer.message(player, C.Line);
		UtilPlayer.message(player, C.Bold + "You evolved into " + F.elem(C.cGreen + C.Bold + GetName()) + "!");	
		UtilPlayer.message(player, F.elem("Attack") + " to use " + F.elem("Detonate"));
		UtilPlayer.message(player, C.Line);

		player.getWorld().playSound(player.getLocation(), Sound.CREEPER_HISS, 4f, 1f);

		//Disguise
		DisguiseCreeper disguise = new DisguiseCreeper(player);
		disguise.setName(Manager.GetGame().GetTeam(player).GetColor() + player.getName());
		disguise.setCustomNameVisible(true);
		Manager.GetDisguise().disguise(disguise);
	}
}
