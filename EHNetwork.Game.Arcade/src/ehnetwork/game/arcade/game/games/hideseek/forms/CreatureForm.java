package ehnetwork.game.arcade.game.games.hideseek.forms;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import net.minecraft.server.v1_8_R3.Entity;

import de.robingrether.idisguise.disguise.Disguise;
import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilInv;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.game.arcade.game.games.hideseek.HideSeek;

public class CreatureForm extends Form
{
	private EntityType _type;

	private Disguise _disguise;

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
		
		if (_type == EntityType.CHICKEN)			{_disguise = Host.getArcadeManager().GetDisguise().createDisguise(EntityType.CHICKEN);	icon = Material.FEATHER;}
		else if (_type == EntityType.COW)			{_disguise = Host.getArcadeManager().GetDisguise().createDisguise(EntityType.COW);		icon = Material.LEATHER;}
		else if (_type == EntityType.SHEEP)			{_disguise = Host.getArcadeManager().GetDisguise().createDisguise(EntityType.SHEEP);		icon = Material.WOOL;}
		else if (_type == EntityType.PIG)			{_disguise = Host.getArcadeManager().GetDisguise().createDisguise(EntityType.PIG);		icon = Material.PORK;}

		//_disguise.setSoundDisguise(new DisguiseCat(Player));

		Host.Manager.GetDisguise().applyDisguise(_disguise, Player);

		((CraftEntity)Player).getHandle().getDataWatcher().watch(0, (byte) 0, Entity.META_ENTITYDATA, (byte) 0);

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

		((CraftEntity)Player).getHandle().getDataWatcher().watch(0, (byte) 0, Entity.META_ENTITYDATA, (byte) 0);
	}
}
