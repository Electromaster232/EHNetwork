package ehnetwork.core.gadget.gadgets;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import net.minecraft.util.com.mojang.authlib.GameProfile;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.ProfileLoader;
import ehnetwork.core.common.util.UUIDFetcher;
import ehnetwork.core.common.util.UtilEvent;
import ehnetwork.core.disguise.disguises.DisguisePlayer;
import ehnetwork.core.gadget.GadgetManager;
import ehnetwork.core.gadget.types.MorphGadget;
import ehnetwork.core.recharge.Recharge;

public class MorphGeno extends MorphGadget
{
	private GameProfile _profile = null;

	public MorphGeno(GadgetManager manager)
	{
		super(manager, "Genocide604", new String[] 
				{ 
				"Say goodbye to Genocide604 by burping",
				"and eating a lot.",
				" ",
				C.cYellow + "Left Click" + C.cGray + " to use " + C.cGreen + "Burp",
				"  ",
				C.cRed + C.Bold + "WARNING: " + ChatColor.RESET + "This is a temporary item!",
				},
				10,
				Material.SKULL_ITEM, (byte)3);

		_profile = new ProfileLoader(UUIDFetcher.getUUIDOf("Genocide604").toString(), "Genocide604").loadProfile();
	}

	@Override
	public void EnableCustom(final Player player) 
	{
		this.ApplyArmor(player);

		DisguisePlayer disguise = new DisguisePlayer(player, _profile);
		Manager.getDisguiseManager().disguise(disguise);
	}

	@Override
	public void DisableCustom(Player player) 
	{
		this.RemoveArmor(player);
		Manager.getDisguiseManager().undisguise(player);
	}

	@EventHandler
	public void Action(PlayerInteractEvent event)
	{
		Player player = event.getPlayer();

		if (!IsActive(player))
			return;

		if (!UtilEvent.isAction(event, UtilEvent.ActionType.L))
			return;

		if (!Recharge.Instance.use(player, GetName(), 1500, false, false))
			return;
		
		player.getWorld().playSound(player.getEyeLocation(), Sound.BURP, 1f, (float) (0.8f + Math.random() * 0.4f));
	}
}
