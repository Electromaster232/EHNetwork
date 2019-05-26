package nautilus.game.arcade.game.games.paintball;

import mineplex.core.common.util.C;
import mineplex.core.common.util.UtilEnt;
import mineplex.core.disguise.disguises.DisguisePlayer;
import nautilus.game.arcade.game.Game;

import org.bukkit.ChatColor;
import org.bukkit.EntityEffect;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;

public class PlayerCopy 
{
	private Game Host;
	
	private Skeleton _ent;
	private Player _owner;
	
	public PlayerCopy(Game host, Player owner, ChatColor nameColor)
	{
		Host = host;
		
		_owner = owner;
		
		Host.CreatureAllowOverride = true;
		_ent = owner.getWorld().spawn(owner.getLocation(), Skeleton.class);
		Host.CreatureAllowOverride = false;
		
		UtilEnt.ghost(_ent, true, false);
		
		UtilEnt.Vegetate(_ent);
		
		//Armor
		_ent.getEquipment().setArmorContents(owner.getInventory().getArmorContents());
		
		_ent.setCustomName(C.cWhite + C.Bold + C.Scramble + "XX" + ChatColor.RESET + " " + nameColor + owner.getName() + " " + C.cWhite + C.Bold + C.Scramble + "XX");
		_ent.setCustomNameVisible(true);
		
		//Disguise
//		DisguisePlayer disguise = new DisguisePlayer(_ent, ((CraftPlayer)owner).getProfile());
//		Host.Manager.GetDisguise().disguise(disguise);
	}

	public LivingEntity GetEntity() 
	{
		return _ent;
	}

	public Player GetPlayer() 
	{
		return _owner;
	}

}
