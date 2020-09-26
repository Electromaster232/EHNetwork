package ehnetwork.game.arcade.game.games.evolution.mobs;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.disguise.disguises.DisguiseChicken;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.kit.Kit;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.game.arcade.kit.perks.PerkConstructor;
import ehnetwork.game.arcade.kit.perks.PerkFallDamage;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class KitChicken extends Kit
{
	public KitChicken(ArcadeManager manager)
	{
		super(manager, "Chicken", KitAvailability.Hide, 

				new String[] 
						{
				""
						}, 

						new Perk[] 
								{
				new PerkConstructor("Egg Pouch", 0.8, 8, Material.EGG, "Egg", false),
				new PerkFallDamage(-2),
								}, 
								EntityType.SLIME,
								null);

	}

	@Override
	public void GiveItems(Player player) 
	{
		UtilPlayer.message(player, C.Line);
		UtilPlayer.message(player, C.Bold + "You evolved into " + F.elem(C.cGreen + C.Bold + GetName()) + "!");	
		UtilPlayer.message(player, F.elem("Right-Click with Eggs") + " to use " + F.elem("Throw Egg"));
		UtilPlayer.message(player, C.Line);

		player.getWorld().playSound(player.getLocation(), Sound.CHICKEN_IDLE, 4f, 1f);

		//Disguise
		DisguiseChicken disguise = new DisguiseChicken(player);
		disguise.setName(Manager.GetGame().GetTeam(player).GetColor() + player.getName());
		disguise.setCustomNameVisible(true);
		Manager.GetDisguise().disguise(disguise);
	}

	@EventHandler
	public void EggHit(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;

		if (event.GetProjectile() == null)
			return;

		if (!(event.GetProjectile() instanceof Egg))
			return;

		event.AddMod("Chicken Kit", "Egg", 1, true);
	}
}
