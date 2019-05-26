package nautilus.game.arcade.game.games.milkcow.kits;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import mineplex.core.common.util.C;
import mineplex.core.disguise.disguises.DisguiseCow;
import mineplex.core.itemstack.ItemStackFactory;
import mineplex.minecraft.game.core.damage.CustomDamageEvent;
import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.kit.AbbreviatedKit;
import nautilus.game.arcade.kit.KitAvailability;
import nautilus.game.arcade.kit.Perk;
import nautilus.game.arcade.kit.SmashKit;
import nautilus.game.arcade.kit.perks.*;

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
