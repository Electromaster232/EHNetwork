package ehnetwork.game.microgames.kit.perks;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilAction;
import ehnetwork.core.common.util.UtilFirework;
import ehnetwork.core.common.util.UtilInv;
import ehnetwork.core.disguise.disguises.DisguiseBase;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.core.projectile.IThrown;
import ehnetwork.core.projectile.ProjectileUser;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.microgames.kit.Perk;

public class PerkRevealer extends Perk implements IThrown
{
	public static class PlayerRevealEvent extends PlayerEvent
	{
		private static final HandlerList handlers = new HandlerList();

		public static HandlerList getHandlerList()
		{
			return handlers;
		}

		@Override
		public HandlerList getHandlers()
		{
			return getHandlerList();
		}

		private final LivingEntity _revealer;

		public PlayerRevealEvent(Player who, LivingEntity revealer)
		{
			super(who);

			_revealer = revealer;
		}

		public LivingEntity getRevealer()
		{
			return _revealer;
		}
	}

	private static final FireworkEffect REVEALER_FIREWORK_EFFECT = FireworkEffect
			.builder()
			.flicker(false)
			.withColor(Color.AQUA)
			.with(FireworkEffect.Type.BALL_LARGE)
			.trail(false)
			.build();

	private static class RevealedPlayerInfo
	{
		public int _expirationSeconds = 5;
		public DisguiseBase _disguise;

		public RevealedPlayerInfo(DisguiseBase disguise)
		{
			_disguise = disguise;
		}
	}

	private final Map<Player, RevealedPlayerInfo> _revealedPlayers = new HashMap<>();

	public PerkRevealer()
	{
		super("Revealer", new String[]{C.cYellow + "Right-Click" + C.cGray + " with Diamond to " + C.cGreen + "Throw Revealer"});
	}

	@EventHandler
	public void onPlayerThrowRevealer(PlayerInteractEvent event)
	{
		if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;

		if (event.getPlayer().getItemInHand() == null)
			return;

		if (event.getPlayer().getItemInHand().getType() != Material.DIAMOND)
			return;

		Player player = event.getPlayer();

		if (!Kit.HasKit(player))
			return;

		event.setCancelled(true);

		UtilInv.remove(player, Material.DIAMOND, (byte) 0, 1);
		UtilInv.Update(player);

		Item ent = player.getWorld().dropItem(player.getEyeLocation(), ItemStackFactory.Instance.CreateStack(Material.DIAMOND));
		UtilAction.velocity(ent, player.getLocation().getDirection(), 1.2, false, 0, 0.2, 10, false);
		Manager.GetProjectile().AddThrow(ent, player, this, -1, true, true, true, false, 0.5f);
	}

	@EventHandler
	public void onUpdate(UpdateEvent event)
	{
		if (event.getType() == UpdateType.SEC)
		{
			for (Iterator<Map.Entry<Player, RevealedPlayerInfo>> it = getRevealedPlayers().entrySet().iterator(); it.hasNext(); )
			{
				Map.Entry<Player, RevealedPlayerInfo> entry = it.next();

				if (!entry.getKey().isOnline())
					it.remove();
				else if (entry.getValue()._expirationSeconds <= 0)
				{
					it.remove();

					Manager.GetDisguise().disguise(entry.getValue()._disguise);
				}
				else
					entry.getValue()._expirationSeconds--;
			}
		}
	}

	@Override
	public void Collide(LivingEntity target, Block block, ProjectileUser data)
	{
		explode(data);
	}

	@Override
	public void Idle(ProjectileUser data)
	{
		explode(data);
	}

	@Override
	public void Expire(ProjectileUser data)
	{
		explode(data);
	}

	private void explode(ProjectileUser data)
	{
		// Workaround to make firework effect always visible
		for (int i = 0; i < 3; i++)
			UtilFirework.playFirework(data.GetThrown().getLocation(), REVEALER_FIREWORK_EFFECT);

		data.GetThrown().getLocation().getWorld().playSound(data.GetThrown().getLocation(), Sound.ZOMBIE_UNFECT, 2f, 0.5f);

		for (Player player : Manager.GetGame().GetPlayers(true))
		{
			if (player == data.GetThrower())
				continue;

			if (player.getLocation().getWorld() == data.GetThrown().getWorld() && player.getLocation().distanceSquared(data.GetThrown().getLocation()) <= 12 * 12)
			{
				RevealedPlayerInfo info = getRevealedPlayers().get(player);

				if (info == null)
				{
					Bukkit.getPluginManager().callEvent(new PlayerRevealEvent(player, data.GetThrower()));

					info = new RevealedPlayerInfo(Manager.GetDisguise().getDisguise(player));
					getRevealedPlayers().put(player, info);

					Manager.GetDisguise().undisguise(player);
					player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 100, 0));
				}

				info._expirationSeconds = 5;
			}
		}

		data.GetThrown().remove();
	}

	public Map<Player, RevealedPlayerInfo> getRevealedPlayers()
	{
		return _revealedPlayers;
	}
}
