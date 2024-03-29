package ehnetwork.game.microgames.game.games.smash.kits;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilInv;
import ehnetwork.core.disguise.disguises.DisguiseEnderman;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.game.Game.GameState;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.game.microgames.kit.SmashKit;
import ehnetwork.game.microgames.kit.perks.PerkBlink;
import ehnetwork.game.microgames.kit.perks.PerkBlockToss;
import ehnetwork.game.microgames.kit.perks.PerkDoubleJump;
import ehnetwork.game.microgames.kit.perks.PerkEndermanDragon;
import ehnetwork.game.microgames.kit.perks.PerkSmashStats;
import ehnetwork.game.microgames.kit.perks.event.PerkBlockGrabEvent;
import ehnetwork.game.microgames.kit.perks.event.PerkBlockThrowEvent;

public class KitEnderman extends SmashKit
{
	public HashMap<Player, DisguiseEnderman> _disguises = new HashMap<Player, DisguiseEnderman>();

	public KitEnderman(MicroGamesManager manager)
	{
		super(manager, "Enderman", KitAvailability.Gem, 3000,

				new String[] 
						{
						}, 

						new Perk[] 
								{
				new PerkSmashStats(7, 1.3, 0.25, 6),
				new PerkDoubleJump("Double Jump", 0.9, 0.9, false),
				new PerkBlink("Blink", 16, 6000),
				new PerkBlockToss(),
				new PerkEndermanDragon()
								}, 
								EntityType.ENDERMAN,
								new ItemStack(Material.ENDER_PEARL),
								"Dragon Rider", 30000, Sound.ENDERDRAGON_GROWL);

	}

	@Override
	public void giveCoreItems(Player player)
	{
		UtilInv.Clear(player);
		
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_SWORD, (byte)0, 1, 
				C.cYellow + C.Bold + "Hold/Release Block" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Block Toss",
				new String[]
						{
			ChatColor.RESET + "Picks up a block from the ground, and",
			ChatColor.RESET + "then hurls it at opponents, causing huge",
			ChatColor.RESET + "damage and knockback if it hits.",
			ChatColor.RESET + "",
			ChatColor.RESET + "The longer you hold the block, the harder",
			ChatColor.RESET + "you throw it. You will hear a 'tick' sound",
			ChatColor.RESET + "when it is fully charged.",
						}));

		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_AXE, (byte)0, 1, 
				C.cYellow + C.Bold + "Right-Click" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Blink",
				new String[]
						{
			ChatColor.RESET + "Instantly teleport in the direction",
			ChatColor.RESET + "you are looking.",
			ChatColor.RESET + "",
			ChatColor.RESET + "You cannot pass through blocks.",
						}));
		
		if (Manager.GetGame().GetState() == GameState.Recruit)
			player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.NETHER_STAR, (byte)0, 1, 
					C.cYellow + C.Bold + "Smash Crystal" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Dragon Rider",
					new String[]
							{
				ChatColor.RESET + "Summon a dragon from The End to fly into",
				ChatColor.RESET + "your opponents, dealing devestating damage.",
							}));

		player.getInventory().setHelmet(ItemStackFactory.Instance.CreateStack(Material.CHAINMAIL_HELMET));
		player.getInventory().setChestplate(ItemStackFactory.Instance.CreateStack(Material.CHAINMAIL_CHESTPLATE));
		player.getInventory().setLeggings(ItemStackFactory.Instance.CreateStack(Material.CHAINMAIL_LEGGINGS));
		player.getInventory().setBoots(ItemStackFactory.Instance.CreateStack(Material.CHAINMAIL_BOOTS));
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
		DisguiseEnderman disguise = new DisguiseEnderman(player);

		if (Manager.GetGame().GetTeam(player) != null)		
			disguise.setName(Manager.GetGame().GetTeam(player).GetColor() + player.getName());
		else			
			disguise.setName(player.getName());
		
		disguise.setCustomNameVisible(true);
		disguise.a(false);
		Manager.GetDisguise().disguise(disguise);

		_disguises.put(player, disguise);
	}

	@EventHandler
	public void BlockGrab(PerkBlockGrabEvent event)
	{
		SetBlock(_disguises.get(event.GetPlayer()), event.GetId(), event.GetData());
	}

	@EventHandler
	public void BlockThrow(PerkBlockThrowEvent event)
	{
		SetBlock(_disguises.get(event.GetPlayer()), 0, (byte)0);
	}

	@EventHandler
	public void Death(PlayerDeathEvent event)
	{
		SetBlock(_disguises.get(event.getEntity()), 0, (byte)0);
	}

	public void SetBlock(DisguiseEnderman disguise, int id, byte data)
	{
		if (disguise == null)
			return;

		disguise.SetCarriedId(id);
		disguise.SetCarriedData(data);

		Manager.GetDisguise().updateDisguise(disguise);
	}
	
	@EventHandler
	public void cleanDisguises(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FAST)
			return;

		for (Iterator<Entry<Player, DisguiseEnderman>> iterator = _disguises.entrySet().iterator(); iterator.hasNext();)
		{
			Entry<Player, DisguiseEnderman> current = iterator.next();
			
			if (!Manager.GetDisguise().isDisguised(current.getKey()))
			{
				iterator.remove();
			}
			else if (Manager.GetDisguise().getDisguise(current.getKey()) != current.getValue())
			{
				iterator.remove();
			}
		}
	}
}
