package nautilus.game.arcade.game.games.wither.kit;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

import mineplex.core.common.util.C;
import mineplex.core.common.util.UtilServer;
import mineplex.core.disguise.disguises.DisguiseWither;
import mineplex.core.itemstack.ItemStackFactory;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.minecraft.game.core.damage.CustomDamageEvent;
import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.kit.Kit;
import nautilus.game.arcade.kit.KitAvailability;
import nautilus.game.arcade.kit.Perk;
import nautilus.game.arcade.kit.perks.*;

public class KitWitherMinion extends Kit
{
	public KitWitherMinion(ArcadeManager manager)
	{
		super(manager, "Wither", KitAvailability.Free,

		new String[]
		{
			""
		},

		new Perk[]
		{
				new PerkWitherArrows(), new PerkWitherAttack(),
				new PerkWitherMinion(), new PerkWitherCompassScent()
		}, EntityType.WITHER, null);

	}

	// @Override
	// public void SpawnCustom(LivingEntity ent)
	// {
	// ent.setMaxHealth(300);
	// ent.setHealth(300);
	//
	// DisguiseWither disguise = new DisguiseWither(ent);
	// disguise.SetName(C.cYellow + "Wither");
	// disguise.SetCustomNameVisible(true);
	// Manager.GetDisguise().disguise(disguise);
	// }

	@Override
	public void GiveItems(Player player)
	{
		player.getInventory().addItem(
				ItemStackFactory.Instance.CreateStack(Material.GOLD_SWORD,
						(byte) 0, 1, C.cYellow + C.Bold + "Left-Click"
								+ C.cWhite + C.Bold + " - " + C.cGreen + C.Bold
								+ "Wither Skull"));

		player.getInventory().addItem(
				ItemStackFactory.Instance.CreateStack(Material.DIAMOND_SWORD,
						(byte) 0, 1, C.cYellow + C.Bold + "Right-Click"
								+ C.cWhite + C.Bold + " - " + C.cGreen + C.Bold
								+ "Skeletal Minions"));

		player.getInventory().addItem(
				ItemStackFactory.Instance
						.CreateStack(Material.COMPASS, (byte) 0, 1, C.cYellow
								+ C.Bold + "Human Finder X-9000"));

		// Disguise
		DisguiseWither disguise = new DisguiseWither(player);

		if (Manager.GetGame().GetTeam(player) != null)
			disguise.setName(Manager.GetGame().GetTeam(player).GetColor()
					+ player.getName());
		else disguise.setName(player.getName());

		disguise.setCustomNameVisible(true);
		Manager.GetDisguise().disguise(disguise);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void witherDamageCancel(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;

		Player player = event.GetDamageePlayer();
		if (player == null)
			return;

		if (HasKit(player))
			event.SetCancelled("Wither Immunity");
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void witherMeleeCancel(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;

		Player player = event.GetDamagerPlayer(true);
		if (player == null)
			return;

		if (!HasKit(player))
			return;

		if (event.GetCause() != DamageCause.ENTITY_ATTACK)
			return;

		event.SetCancelled("Wither Melee Cancel");
	}

	@EventHandler
	public void witherFlight(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		for (Player player : UtilServer.getPlayers())
		{
			if (!HasKit(player))
				continue;

			if (player.isFlying())
				continue;

			player.setAllowFlight(true);
			player.setFlying(true);
		}
	}
}
