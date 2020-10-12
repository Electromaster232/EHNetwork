package ehnetwork.game.arcade.game.games.hideseek.forms;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilInv;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.disguise.disguises.DisguiseBase;
import ehnetwork.core.disguise.disguises.DisguiseCat;
import ehnetwork.core.disguise.disguises.DisguiseChicken;
import ehnetwork.core.disguise.disguises.DisguiseCow;
import ehnetwork.core.disguise.disguises.DisguisePig;
import ehnetwork.core.disguise.disguises.DisguiseSheep;
import ehnetwork.game.arcade.game.games.hideseek.HideSeek;

public class CreatureForm extends Form
{
	private EntityType _type;

	private DisguiseBase _disguise;

	public CreatureForm(HideSeek host, Player player, EntityType entityType) 
	{
		super(host, player);

		_type = entityType;

		Apply();
	}

	@Override
	public void Apply() 
	{
		Material icon = Material.PORK;
		
		if (_type == EntityType.CHICKEN)			{_disguise = new DisguiseChicken(Player);	icon = Material.FEATHER;}
		else if (_type == EntityType.COW)			{_disguise = new DisguiseCow(Player);		icon = Material.LEATHER;}
		else if (_type == EntityType.SHEEP)			{_disguise = new DisguiseSheep(Player);		icon = Material.WOOL;}
		else if (_type == EntityType.PIG)			{_disguise = new DisguisePig(Player);		icon = Material.PORK;}

		_disguise.setSoundDisguise(new DisguiseCat(Player));
		Host.Manager.GetDisguise().disguise(_disguise);

		((CraftEntity)Player).getHandle().getDataWatcher().watch(0, Byte.valueOf((byte) 0));

		//Inform
		UtilPlayer.message(Player, F.main("Game", C.cWhite + "You are now a " + F.elem(UtilEnt.getName(_type)) + "!"));

		//Give Item
		Player.getInventory().setItem(8, new ItemStack(Host.GetItemEquivilent(icon)));
		UtilInv.Update(Player);

		//Sound
		Player.playSound(Player.getLocation(), Sound.ZOMBIE_UNFECT, 2f, 2f);
	}

	@Override
	public void Remove() 
	{
		Host.Manager.GetDisguise().undisguise(Player);

		((CraftEntity)Player).getHandle().getDataWatcher().watch(0, Byte.valueOf((byte) 0));
	}
}
