package ehnetwork.game.arcade.game.games.milkcow.kits;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import ehnetwork.core.common.util.C;
import ehnetwork.core.disguise.disguises.DisguiseCow;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.kit.AbbreviatedKit;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.game.arcade.kit.perks.PerkCharge;
import ehnetwork.game.arcade.kit.perks.PerkCowBomb;
import ehnetwork.game.arcade.kit.perks.PerkDamageSet;
import ehnetwork.game.arcade.kit.perks.PerkKnockbackMultiplier;
import ehnetwork.game.arcade.kit.perks.PerkSeismicCow;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class KitCow extends AbbreviatedKit
{
	public KitCow(ArcadeManager manager)
	{
		super(manager, "The Angry Cow", KitAvailability.Free, 

				new String[] 
						{
						}, 

						new Perk[] 
								{
				new PerkDamageSet(4),
				new PerkKnockbackMultiplier(4),
				new PerkCharge(),
				new PerkCowBomb(),
				new PerkSeismicCow(),
								}, 
								EntityType.COW,
								null);

	}

	@Override
	public void GiveItems(Player player) 
	{
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_AXE, (byte)0, 1, 
				C.cYellow + C.Bold + "Right-Click" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Cow Bomb",
				new String[]
						{
			ChatColor.RESET + "Say goodbye to one of your children",
			ChatColor.RESET + "and hurl them towards your opponents.",
			ChatColor.RESET + "Explodes on impact, dealing knockback",
						}));


		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_SPADE, (byte)0, 1, 
				C.cYellow + C.Bold + "Right-Click" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Body Slam",
				new String[]
						{
			ChatColor.RESET + "Hurl your giant fat cow-body forwards.",
			ChatColor.RESET + "Deals damage and knockback to anyone it",
			ChatColor.RESET + "collides with.",
						}));
		
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.LEATHER, (byte)0, 1, 
				C.cYellow + C.Bold + "Sprint" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Cow Charge",
				new String[]
						{
			ChatColor.RESET + "Charge with great power, flinging",
			ChatColor.RESET + "filthy farmers out of your way!",
						}));

		//Disguise
		DisguiseCow disguise = new DisguiseCow(player);
		disguise.setName(C.cRed + player.getName());//+ " the Furious Cow");
		disguise.setCustomNameVisible(true);
		Manager.GetDisguise().disguise(disguise);
	}
	
	@EventHandler
	public void NoDamage(CustomDamageEvent event)
	{
		Player player = event.GetDamageePlayer();
		if (player == null)		return;
		
		if (HasKit(player))
			event.SetCancelled("Cow Immunity");
	}
}
