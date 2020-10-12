package ehnetwork.game.arcade.game.games.smash.kits;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilInv;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.common.util.UtilParticle;
import ehnetwork.core.common.util.UtilParticle.ParticleType;
import ehnetwork.core.common.util.UtilParticle.ViewDist;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.disguise.disguises.DisguiseBase;
import ehnetwork.core.disguise.disguises.DisguiseSlime;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.game.Game.GameState;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.game.arcade.kit.SmashKit;
import ehnetwork.game.arcade.kit.perks.PerkDoubleJump;
import ehnetwork.game.arcade.kit.perks.PerkSlimeRocket;
import ehnetwork.game.arcade.kit.perks.PerkSlimeSlam;
import ehnetwork.game.arcade.kit.perks.PerkSmashStats;
import ehnetwork.minecraft.game.core.condition.Condition.ConditionType;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class KitSlime extends SmashKit
{
	public KitSlime(ArcadeManager manager)
	{
		super(manager, "Slime", KitAvailability.Free, 

				new String[] 
						{
						}, 

						new Perk[] 
								{
				new PerkSmashStats(6, 1.75, 0.35, 3),
				new PerkDoubleJump("Double Jump", 1.2, 1, false),
				new PerkSlimeSlam(),
				new PerkSlimeRocket(),
								}, 
								EntityType.SLIME,
								new ItemStack(Material.SLIME_BALL),
								"Giga Slime", 24000, Sound.SLIME_ATTACK);
	}

	@Override
	public void giveCoreItems(Player player)
	{
		UtilInv.Clear(player);
		
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_SWORD, (byte)0, 1, 
				C.cYellow + C.Bold + "Hold/Release Block" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Slime Rocket",
				new String[]
						{
			ChatColor.RESET + "Slowly transfer your slimey goodness into",
			ChatColor.RESET + "a new slime. When you release block, the",
			ChatColor.RESET + "new slime is propelled forward.",
			ChatColor.RESET + "",
			ChatColor.RESET + "The more you charge the ability, the stronger",
			ChatColor.RESET + "the new slime is projected forwards.",
			ChatColor.RESET + "",
			ChatColor.RESET + C.cAqua + "Slime Rocket uses Energy (Experience Bar)",
						}));
		
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_AXE, (byte)0, 1, 
				C.cYellow + C.Bold + "Right-Click" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Slime Slam",
				new String[]
						{
			ChatColor.RESET + "Throw your slimey body forwards. If you hit",
			ChatColor.RESET + "another player before you land, you deal",
			ChatColor.RESET + "large damage and knockback to them.",
			ChatColor.RESET + "",
			ChatColor.RESET + "However, you take 50% of the damage and",
			ChatColor.RESET + "knockback in the opposite direction.",
						}));
		
		if (Manager.GetGame().GetState() == GameState.Recruit)
			player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.NETHER_STAR, (byte)0, 1, 
					C.cYellow + C.Bold + "Smash Crystal" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Giga Slime",
					new String[]
							{
				ChatColor.RESET + "Grow into a gigantic slime that deals damage",
				ChatColor.RESET + "and knockback to anyone that comes nearby.",
							}));

		player.getInventory().setHelmet(ItemStackFactory.Instance.CreateStack(Material.CHAINMAIL_HELMET));
		player.getInventory().setChestplate(ItemStackFactory.Instance.CreateStack(Material.CHAINMAIL_CHESTPLATE));
	}
	
	@Override
	public void giveSuperItems(Player player)
	{
		player.getInventory().remove(Material.IRON_SWORD);
		player.getInventory().remove(Material.IRON_AXE);
		
		UtilInv.Update(player);
	}
	
	@Override
	public void GiveItems(Player player) 
	{
		giveCoreItems(player);
		
		//Disguise
		DisguiseSlime disguise = new DisguiseSlime(player);

		if (Manager.GetGame().GetTeam(player) != null)		
			disguise.setName(Manager.GetGame().GetTeam(player).GetColor() + player.getName());
		else			
			disguise.setName(player.getName());
		
		disguise.setCustomNameVisible(true);
		Manager.GetDisguise().disguise(disguise);
		
		disguise.SetSize(3);
	}
	
	@Override
	public void activateSuperCustom(Player player)
	{
		DisguiseBase disguise = Manager.GetDisguise().getDisguise(player);
		if (disguise != null && disguise instanceof DisguiseSlime)
		{
			DisguiseSlime slime = (DisguiseSlime)disguise;

			slime.SetSize(14);
			Manager.GetDisguise().updateDisguise(slime);
		}
		
		player.setExp(0.99f);
		
		Manager.GetCondition().Factory().Speed("Giga Slime", player, player, 20, 1, false, false, false);
	}
	
	@Override
	public void deactivateSuperCustom(Player player)
	{
		Manager.GetCondition().EndCondition(player, ConditionType.SPEED, "Giga Slime");
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void immunityDamagee(CustomDamageEvent event)
	{
		if (event.GetDamageePlayer() == null || event.GetDamagerEntity(true) == null)
			return;
		
		if (this.isSuperActive(event.GetDamageePlayer()))
			event.SetCancelled(getSuperName());
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void immunityDamager(CustomDamageEvent event)
	{
		if (event.GetCause() != DamageCause.ENTITY_ATTACK)
			return;
		
		if (event.GetDamagerPlayer(true) == null)
			return;
		
		if (isSuperActive(event.GetDamagerPlayer(true)))
			event.SetCancelled(getSuperName());
	}
	
	@EventHandler
	public void collide(UpdateEvent event)
	{
		for (Player player : getSuperActive())
			for (Player other : Manager.GetGame().GetPlayers(true))
			{
				if (player.equals(other))
					continue;
				
				if (Manager.isSpectator(other))
					continue;
				
				if (UtilMath.offset(player.getLocation().add(0, 3, 0), other.getLocation()) < 5)
				{
					Manager.GetDamage().NewDamageEvent(other, player, null, 
							DamageCause.CUSTOM, 8, true, false, false,
							player.getName(), GetName());	
					
					UtilParticle.PlayParticle(ParticleType.SLIME, other.getLocation().add(0, 0.6, 0), 1f, 1f, 1f, 0, 20,
							ViewDist.LONG, UtilServer.getPlayers());
					
					player.getWorld().playSound(other.getLocation(), Sound.SLIME_ATTACK, 3f, 1f);
				}
			}
	}
}
