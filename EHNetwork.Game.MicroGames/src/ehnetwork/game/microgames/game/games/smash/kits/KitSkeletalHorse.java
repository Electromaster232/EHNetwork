package ehnetwork.game.microgames.game.games.smash.kits;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilInv;
import ehnetwork.core.disguise.disguises.DisguiseHorse;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.game.Game.GameState;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.game.microgames.kit.SmashKit;
import ehnetwork.game.microgames.kit.perks.PerkBoneRush;
import ehnetwork.game.microgames.kit.perks.PerkDeadlyBones;
import ehnetwork.game.microgames.kit.perks.PerkDoubleJump;
import ehnetwork.game.microgames.kit.perks.PerkHorseKick;
import ehnetwork.game.microgames.kit.perks.PerkSmashStats;

public class KitSkeletalHorse extends SmashKit
{
	public KitSkeletalHorse(MicroGamesManager manager)
	{
		super(manager, "Skeletal Horse", KitAvailability.Gem, 7000,

				new String[] 
						{
						}, 

						new Perk[] 
								{
				new PerkSmashStats(6, 1.4, 0.35, 6),
				new PerkDoubleJump("Double Jump", 1, 1, false),
				new PerkHorseKick(),
				new PerkBoneRush(),
				new PerkDeadlyBones()
				
								}, 
								EntityType.HORSE,
								new ItemStack(Material.BONE),
								"Bone Storm", 24000, Sound.HORSE_SKELETON_DEATH);
	}

	@Override
	public void giveCoreItems(Player player)
	{
		UtilInv.Clear(player);
		
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_AXE, (byte)0, 1, 
				C.cYellow + C.Bold + "Right-Click" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Bone Kick",
				new String[]
						{
			ChatColor.RESET + "Stand on your hind legs and maul enemies",
			ChatColor.RESET + "in front of you with your front legs, ",
			ChatColor.RESET + "dealing damage and large knockback.",
			
						}));
		
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_SPADE, (byte)0, 1, 
				C.cYellow + C.Bold + "Right-Click" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Bone Rush",
				new String[]
						{
			ChatColor.RESET + "Charge forth in a deadly wave of bones.",
			ChatColor.RESET + "Bones deal small damage and knockback.",
			ChatColor.RESET + "",
			ChatColor.RESET + "Holding Crouch will prevent you from",
			ChatColor.RESET + "moving forward with the bones.",
						}));
		
		if (Manager.GetGame().GetState() == GameState.Recruit)
			player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.BONE, (byte)0, 1, 
					C.cYellow + C.Bold + "Passive" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Deadly Bones",
					new String[]
							{
				ChatColor.RESET + "Whenever you take damage, you drop a bone",
				ChatColor.RESET + "which will explode after a few seconds",
				ChatColor.RESET + "dealing damage and knockback to enemies."
							}));
		
		if (Manager.GetGame().GetState() == GameState.Recruit)
			player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.NETHER_STAR, (byte)0, 1, 
					C.cYellow + C.Bold + "Smash Crystal" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Bone Storm",
					new String[]
							{
				ChatColor.RESET + "Charge forth in a mighty bone storm.",
				ChatColor.RESET + "Bones deal damage and knockback.",
				ChatColor.RESET + "",
				ChatColor.RESET + "Holding Crouch will prevent you from",
				ChatColor.RESET + "moving forward with the bones.",
							}));

		player.getInventory().setHelmet(ItemStackFactory.Instance.CreateStack(Material.CHAINMAIL_HELMET));
		player.getInventory().setChestplate(ItemStackFactory.Instance.CreateStack(Material.CHAINMAIL_CHESTPLATE));
		player.getInventory().setLeggings(ItemStackFactory.Instance.CreateStack(Material.CHAINMAIL_LEGGINGS));
		player.getInventory().setBoots(ItemStackFactory.Instance.CreateStack(Material.CHAINMAIL_BOOTS));
	}
	
	@Override
	public void giveSuperItems(Player player)
	{
		player.getInventory().remove(Material.IRON_SPADE);
		player.getInventory().remove(Material.IRON_AXE);
		
		UtilInv.Update(player);
	}
	
	@Override
	public void GiveItems(Player player) 
	{
		giveCoreItems(player);
		
		//Disguise
		DisguiseHorse disguise = new DisguiseHorse(player);
		disguise.setType(Variant.SKELETON_HORSE);

		if (Manager.GetGame().GetTeam(player) != null)		
			disguise.setName(Manager.GetGame().GetTeam(player).GetColor() + player.getName());
		else			
			disguise.setName(player.getName());
		
		disguise.setCustomNameVisible(true);
		Manager.GetDisguise().disguise(disguise);
	}
	
	@Override
	public Entity SpawnEntity(Location loc)
	{
		EntityType type = _entityType;
		if (type == EntityType.PLAYER)
			type = EntityType.ZOMBIE;

		LivingEntity entity = (LivingEntity) Manager.GetCreature().SpawnEntity(loc, type);

		entity.setRemoveWhenFarAway(false);
		entity.setCustomName(GetAvailability().GetColor() + GetName() + " Kit");
		entity.setCustomNameVisible(true);
		entity.getEquipment().setItemInHand(_itemInHand);

		if (type == EntityType.HORSE)
		{
			Horse horse = (Horse)entity;
			horse.setAdult();
			horse.setVariant(Variant.SKELETON_HORSE);
		}

		UtilEnt.Vegetate(entity);

		SpawnCustom(entity); 

		return entity;
	}
}
