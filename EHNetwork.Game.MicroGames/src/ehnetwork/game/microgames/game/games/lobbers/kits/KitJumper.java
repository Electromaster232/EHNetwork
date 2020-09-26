package ehnetwork.game.microgames.game.games.lobbers.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import ehnetwork.core.common.util.C;
import ehnetwork.core.itemstack.ItemBuilder;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.game.games.lobbers.kits.perks.PerkCraftman;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.game.microgames.kit.perks.PerkDoubleJump;
import ehnetwork.game.microgames.kit.perks.PerkDummy;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class KitJumper extends Kit
{

	public KitJumper(MicroGamesManager manager)
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
