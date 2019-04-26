package nautilus.game.arcade.game.games.lobbers.kits;

import mineplex.core.common.util.C;
import mineplex.core.itemstack.ItemBuilder;
import mineplex.minecraft.game.core.damage.CustomDamageEvent;
import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.game.games.lobbers.kits.perks.PerkCraftman;
import nautilus.game.arcade.kit.Kit;
import nautilus.game.arcade.kit.KitAvailability;
import nautilus.game.arcade.kit.Perk;
import nautilus.game.arcade.kit.perks.PerkDoubleJump;
import nautilus.game.arcade.kit.perks.PerkDummy;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class KitJumper extends Kit
{

	public KitJumper(ArcadeManager manager)
	{
		super(manager, "Jumper", KitAvailability.Free, 0, new String[]
				{
				C.cGray + "Use your jumping abilities to leap away from trouble!"
				}, new Perk[]
						{
				new PerkDoubleJump("Double Jump", 1.2, 1.2, false, 6000, true),
				new PerkDummy("Feathered Boots", 
						new String[]
								{
						C.cGray + "You take no fall damage."
								}),
				new PerkCraftman()
						}, EntityType.ZOMBIE, new ItemBuilder(Material.IRON_AXE).build());
	}

	@Override
	public void GiveItems(Player player)
	{

	}

	@EventHandler
	public void onDamage(CustomDamageEvent event)
	{
		if (!Manager.GetGame().IsLive())
			return;
		
		if (!(event.GetDamageeEntity() instanceof Player))
			return;
		
		if (!HasKit(event.GetDamageePlayer()))
			return;
			
		if (event.GetCause() == DamageCause.FALL)
		{
			event.SetCancelled("Jumper no fall damage");
		}
	}
}
