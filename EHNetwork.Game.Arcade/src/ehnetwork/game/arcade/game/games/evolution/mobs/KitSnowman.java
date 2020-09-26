package ehnetwork.game.arcade.game.games.evolution.mobs;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.disguise.disguises.DisguiseSnowman;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.kit.Kit;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.game.arcade.kit.perks.PerkConstructor;
import ehnetwork.game.arcade.kit.perks.PerkFallDamage;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class KitSnowman extends Kit
{
	public KitSnowman(ArcadeManager manager)
	{
		super(manager, "Snowman", KitAvailability.Hide, 

				new String[] 
						{
				""
						}, 

						new Perk[] 
								{
				new PerkConstructor("Snowballer", 0.5, 16, Material.SNOW_BALL, "Snowball", false),
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
		UtilPlayer.message(player, F.elem("Right-Click with Snowball") + " to use " + F.elem("Throw Snowball"));
		UtilPlayer.message(player, C.Line);
		
		player.getWorld().playSound(player.getLocation(), Sound.STEP_SNOW, 4f, 1f);
		
		//Disguise
		DisguiseSnowman disguise = new DisguiseSnowman(player);
		disguise.setName(Manager.GetGame().GetTeam(player).GetColor() + player.getName());
		disguise.setCustomNameVisible(true);
		Manager.GetDisguise().disguise(disguise);
	}
	
	@EventHandler
	public void SnowballHit(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;
		
		if (event.GetProjectile() == null)
			return;
		
		if (!(event.GetProjectile() instanceof Snowball))
			return;
		
		event.AddMod("Snowman Kit", "Snowball", 3, true);
	}
}
