package ehnetwork.game.arcade.kit.perks;

import java.util.HashSet;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import de.robingrether.idisguise.disguise.MobDisguise;
import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.arcade.kit.SmashPerk;

public class PerkPigZombie extends SmashPerk
{
	public HashSet<Player> _active = new HashSet<Player>();
	
	public PerkPigZombie() 
	{
		super("Nether Pig", new String[] 
				{ 
				C.cGray + "Become Nether Pig when HP is below 6.",
				C.cGray + "Return to Pig when HP is 10 or higher."
				});
	}
	
	@EventHandler
	public void Check(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FASTER)
			return;
		
		for (Player player : Manager.GetGame().GetPlayers(true))
		{
			if (!Kit.HasKit(player))
				continue;
			
			//Active
			if (_active.contains(player))
			{
				Manager.GetCondition().Factory().Speed("Pig Zombie", player, player, 0.9, 0, false, false, false);
				
				if (player.getHealth() < 10 || isSuperActive(player))
					continue;
				
				//Deactivate
				_active.remove(player);
				
				//Armor
				player.getInventory().setHelmet(null);
				player.getInventory().setChestplate(ItemStackFactory.Instance.CreateStack(Material.CHAINMAIL_CHESTPLATE));
				player.getInventory().setLeggings(ItemStackFactory.Instance.CreateStack(Material.CHAINMAIL_LEGGINGS));
				player.getInventory().setBoots(ItemStackFactory.Instance.CreateStack(Material.CHAINMAIL_BOOTS));
				
				player.getInventory().remove(Material.IRON_HELMET);
				player.getInventory().remove(Material.IRON_CHESTPLATE);
				player.getInventory().remove(Material.IRON_LEGGINGS);
				player.getInventory().remove(Material.IRON_BOOTS);
				
				//Disguise
				MobDisguise disguise = (MobDisguise) Manager.GetDisguise().createDisguise(EntityType.PIG_ZOMBIE);
				
				if (Manager.GetGame().GetTeam(player) != null)		
					disguise.setCustomName(Manager.GetGame().GetTeam(player).GetColor() + player.getName());
				else			
					disguise.setCustomName(player.getName());
				
				disguise.setCustomNameVisible(true);
				Manager.GetDisguise().applyDisguise(disguise, player);
				
				//Sound
				player.getWorld().playSound(player.getLocation(), Sound.PIG_IDLE, 2f, 1f);
				player.getWorld().playSound(player.getLocation(), Sound.PIG_IDLE, 2f, 1f);
				
				//Inform
				UtilPlayer.message(player, F.main("Skill", "You returned to " + F.skill("Pig Form") + "."));
			}
			//Not Active
			else
			{
				if (player.getHealth() <= 0 || (!isSuperActive(player) && player.getHealth() > 6))
					continue;
				
				//Activate
				_active.add(player);
				
				//Armor
				player.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
				player.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
				player.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
				player.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));
				
				//Disguise
				MobDisguise disguise = (MobDisguise) Manager.GetDisguise().createDisguise(EntityType.PIG_ZOMBIE);

				if (Manager.GetGame().GetTeam(player) != null)
					disguise.setCustomName(Manager.GetGame().GetTeam(player).GetColor() + player.getName());
				else
					disguise.setCustomName(player.getName());

				disguise.setCustomNameVisible(true);
				Manager.GetDisguise().applyDisguise(disguise, player);

				//Sound
				player.getWorld().playSound(player.getLocation(), Sound.ZOMBIE_PIG_ANGRY, 2f, 1f);
				player.getWorld().playSound(player.getLocation(), Sound.ZOMBIE_PIG_ANGRY, 2f, 1f);
				
				//Inform
				UtilPlayer.message(player, F.main("Skill", "You transformed into " + F.skill("Nether Pig Form") + "."));
				
				player.setExp(0.99f);
			}
		}
	}
	
	@EventHandler
	public void Clean(PlayerDeathEvent event)
	{
		_active.remove(event.getEntity());
	}
}
