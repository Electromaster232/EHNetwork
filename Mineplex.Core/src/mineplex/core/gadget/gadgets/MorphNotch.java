package mineplex.core.gadget.gadgets;

import net.minecraft.util.com.mojang.authlib.GameProfile;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.*;

import mineplex.core.common.util.C;
import mineplex.core.common.util.ProfileLoader;
import mineplex.core.common.util.UUIDFetcher;
import mineplex.core.common.util.UtilEvent;
import mineplex.core.common.util.UtilEvent.ActionType;
import mineplex.core.disguise.disguises.DisguisePlayer;
import mineplex.core.recharge.Recharge;
import mineplex.core.gadget.GadgetManager;
import mineplex.core.gadget.types.MorphGadget;

public class MorphNotch extends MorphGadget
{
	private GameProfile _notchProfile = null;
	
	public MorphNotch(GadgetManager manager)
	{
		super(manager, "Notch", new String[] 
				{ 
				"Who wouldn't want to be Notch?!",
				//C.cYellow + "Left Click" + C.cGray + " to use " + C.cGreen + "Enforce EULA",
				},
				50000,
				Material.SKULL_ITEM, (byte)3);
		
		_notchProfile = new ProfileLoader(UUIDFetcher.getUUIDOf("Notch").toString(), "Notch").loadProfile();
	}

	@Override
	public void EnableCustom(final Player player) 
	{
		this.ApplyArmor(player);

		DisguisePlayer disguise = new DisguisePlayer(player, _notchProfile);
		Manager.getDisguiseManager().disguise(disguise);
	}

	@Override
	public void DisableCustom(Player player) 
	{
		this.RemoveArmor(player);
		Manager.getDisguiseManager().undisguise(player);
	}

	//@EventHandler
	public void Action(PlayerInteractEvent event)
	{
		Player player = event.getPlayer();

		if (!IsActive(player))
			return;

		if (!UtilEvent.isAction(event, ActionType.L))
			return;
		
		if (!Recharge.Instance.use(player, GetName(), 1500, false, false))
			return;
		
		player.sendMessage("You have enforced the EULA.");
		
	}
}
