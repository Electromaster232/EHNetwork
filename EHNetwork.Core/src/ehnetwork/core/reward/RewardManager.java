package ehnetwork.core.reward;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import ehnetwork.core.account.CoreClientManager;
import ehnetwork.core.common.Rank;
import ehnetwork.core.donation.DonationManager;
import ehnetwork.core.inventory.InventoryManager;
import ehnetwork.core.pet.PetManager;
import ehnetwork.core.reward.rewards.CoinReward;
import ehnetwork.core.reward.rewards.InventoryReward;
import ehnetwork.core.reward.rewards.PetReward;
import ehnetwork.core.reward.rewards.RankReward;
import ehnetwork.core.reward.rewards.UnknownPackageReward;

public class RewardManager
{
	private JavaPlugin _plugin;
	private HashMap<RewardRarity, List<Reward>> _treasureMap;
	private Random _random;
	
	private CoreClientManager _clientManager;
	
	private boolean _doubleGadgetValue;
	
	public RewardManager(CoreClientManager clientManager, DonationManager donationManager, InventoryManager inventoryManager, PetManager petManager,
			int commonValueMin, int commonValueMax,
			int uncommonValueMin, int uncommonValueMax,
			int rareValueMin, int rareValueMax,
			int legendValueMin, int legendValueMax,
			boolean doubleGadgetValue)
	{
		_plugin = donationManager.getPlugin();
		_treasureMap = new HashMap<RewardRarity, List<Reward>>();
		_random = new Random();

		for (RewardRarity rarity : RewardRarity.values())
		{
			_treasureMap.put(rarity, new ArrayList<Reward>());
		}
		
		_clientManager = clientManager;
		
		_doubleGadgetValue = doubleGadgetValue;

		addCommon(donationManager, inventoryManager, petManager, commonValueMin, commonValueMax);
		addUncommon(donationManager, inventoryManager, petManager, uncommonValueMin, uncommonValueMax);
		addRare(donationManager, inventoryManager, petManager, rareValueMin, rareValueMax);
		addLegendary(donationManager, inventoryManager, petManager, legendValueMin, legendValueMax);
	}

	public void addCommon(DonationManager donationManager, InventoryManager inventoryManager, PetManager petManager, double minValue, double maxValue)
	{
		RewardRarity rarity = RewardRarity.COMMON;

		// Coins
		addReward(new CoinReward(donationManager, (int)minValue, (int)maxValue, 1, rarity));

		//Increase Value
		if (_doubleGadgetValue)
		{
			minValue *= 2;
			maxValue *= 2;
		}
		
		// Gadgets
		addReward(new InventoryReward(inventoryManager, "Paintballs", "Paintball Gun",
				(int)(100*(minValue/500)), (int)(100*(maxValue/500)),
				new ItemStack(Material.GOLD_BARDING), rarity, 10));
		
		addReward(new InventoryReward(inventoryManager, "Fireworks", "Fireworks",
				(int)(50*(minValue/500)), (int)(50*(maxValue/500)),
				new ItemStack(Material.FIREWORK), rarity, 10));
		
		addReward(new InventoryReward(inventoryManager, "Melons", "Melon Launcher",
				(int)(50*(minValue/500)), (int)(50*(maxValue/500)),
				new ItemStack(Material.MELON_BLOCK), rarity, 10));
		
		addReward(new InventoryReward(inventoryManager, "Flesh Hooks", "Flesh Hook",
				(int)(40*(minValue/500)), (int)(40*(maxValue/500)),
				new ItemStack(Material.getMaterial(131)), rarity, 10));
		
		addReward(new InventoryReward(inventoryManager, "Pearls", "Ethereal Pearl",
				(int)(30*(minValue/500)), (int)(30*(maxValue/500)),
				new ItemStack(Material.ENDER_PEARL), rarity, 10));
		
		addReward(new InventoryReward(inventoryManager, "Bat Swarms", "Bat Blaster",
				(int)(20*(minValue/500)), (int)(20*(maxValue/500)),
				new ItemStack(Material.IRON_BARDING), rarity, 10));
		
		addReward(new InventoryReward(inventoryManager, "TNT", "TNT",
				(int)(20*(minValue/500)), (int)(20*(maxValue/500)),
				new ItemStack(Material.TNT), rarity, 10));
	}

	public void addUncommon(DonationManager donationManager, InventoryManager inventoryManager, PetManager petManager, double minValue, double maxValue)
	{
		RewardRarity rarity = RewardRarity.UNCOMMON;

		// Coins
		addReward(new CoinReward(donationManager, (int)minValue, (int)maxValue, 250, RewardRarity.UNCOMMON));

		//Increase Value
		if (_doubleGadgetValue)
		{
			minValue *= 2;
			maxValue *= 2;
		}
		
		// Gadgets
		addReward(new InventoryReward(inventoryManager, "Paintballs", "Paintball Gun",
				(int)(100*(minValue/500)), (int)(100*(maxValue/500)),
				new ItemStack(Material.GOLD_BARDING), rarity, 250));
		
		addReward(new InventoryReward(inventoryManager, "Fireworks", "Fireworks",
				(int)(50*(minValue/500)), (int)(50*(maxValue/500)),
				new ItemStack(Material.FIREWORK), rarity, 250));
		
		addReward(new InventoryReward(inventoryManager, "Melons", "Melon Launcher",
				(int)(50*(minValue/500)), (int)(50*(maxValue/500)),
				new ItemStack(Material.MELON_BLOCK), rarity, 250));
		
		addReward(new InventoryReward(inventoryManager, "Flesh Hooks", "Flesh Hook",
				(int)(40*(minValue/500)), (int)(40*(maxValue/500)),
				new ItemStack(Material.getMaterial(131)), rarity, 250));
		
		addReward(new InventoryReward(inventoryManager, "Pearls", "Ethereal Pearl",
				(int)(30*(minValue/500)), (int)(30*(maxValue/500)),
				new ItemStack(Material.ENDER_PEARL), rarity, 250));
		
		addReward(new InventoryReward(inventoryManager, "Bat Swarms", "Bat Blaster",
				(int)(20*(minValue/500)), (int)(20*(maxValue/500)),
				new ItemStack(Material.IRON_BARDING), rarity, 250));
		
		addReward(new InventoryReward(inventoryManager, "TNT", "TNT",
				(int)(20*(minValue/500)), (int)(20*(maxValue/500)),
				new ItemStack(Material.TNT), rarity, 250));

		// Pets
		addReward(new PetReward(petManager, inventoryManager, donationManager, "Cow Pet", "Cow",
				EntityType.COW, rarity, 500));
		addReward(new PetReward(petManager, inventoryManager, donationManager, "Sheep Pet", "Sheep",
				EntityType.SHEEP, rarity, 333));
		addReward(new PetReward(petManager, inventoryManager, donationManager, "Mooshroom Pet", "Mooshroom",
				EntityType.MUSHROOM_COW, rarity, 200));
		addReward(new PetReward(petManager, inventoryManager, donationManager, "Pig Pet", "Pig",
				EntityType.PIG, rarity, 200));
		addReward(new PetReward(petManager, inventoryManager, donationManager, "Ocelot Pet", "Cat",
				EntityType.OCELOT, rarity, 167));
		addReward(new PetReward(petManager, inventoryManager, donationManager, "Chicken Pet", "Chicken",
				EntityType.CHICKEN, rarity, 143));
		addReward(new PetReward(petManager, inventoryManager, donationManager, "Wolf Pet", "Dog",
				EntityType.WOLF, rarity, 125));

		// Music Discs
		addReward(new UnknownPackageReward(donationManager, "13 Disc", "13 Disc",
				new ItemStack(2256), rarity, 25));
		addReward(new UnknownPackageReward(donationManager, "Cat Disc", "Cat Disc",
				new ItemStack(2257), rarity, 25));
		addReward(new UnknownPackageReward(donationManager, "Blocks Disc", "Blocks Disc",
				new ItemStack(2258), rarity, 25));
		addReward(new UnknownPackageReward(donationManager, "Chirp Disc", "Chirp Disc",
				new ItemStack(2259), rarity, 25));
		addReward(new UnknownPackageReward(donationManager, "Far Disc", "Far Disc",
				new ItemStack(2260), rarity, 25));
		addReward(new UnknownPackageReward(donationManager, "Mall Disc", "Mall Disc",
				new ItemStack(2261), rarity, 25));
		addReward(new UnknownPackageReward(donationManager, "Mellohi Disc", "Mellohi Disc",
				new ItemStack(2262), rarity, 25));
		addReward(new UnknownPackageReward(donationManager, "Stal Disc", "Stal Disc",
				new ItemStack(2263), rarity, 25));
		addReward(new UnknownPackageReward(donationManager, "Strad Disc", "Strad Disc",
				new ItemStack(2264), rarity, 25));
		addReward(new UnknownPackageReward(donationManager, "Ward Disc", "Ward Disc",
				new ItemStack(2265), rarity, 25));
//		addReward(new UnknownPackageReward(donationManager, "11 Disc", "11 Disc",
//				new ItemStack(2266), rarity, 25));
		addReward(new UnknownPackageReward(donationManager, "Wait Disc", "Wait Disc",
				new ItemStack(2267), rarity, 25));
	}

	public void addRare(DonationManager donationManager, InventoryManager inventoryManager, PetManager petManager, double minValue, double maxValue)
	{
		RewardRarity rarity = RewardRarity.RARE;

		// Coins
		addReward(new CoinReward(donationManager, (int)minValue, (int)maxValue, 100, RewardRarity.RARE));

		// Mounts
		addReward(new UnknownPackageReward(donationManager, "Mount Mule", "Mount Mule",
				new ItemStack(Material.HAY_BLOCK), rarity, 200));
		addReward(new UnknownPackageReward(donationManager, "Minecart Mount", "Minecart",
				new ItemStack(Material.MINECART), rarity, 100));
		addReward(new UnknownPackageReward(donationManager, "Slime Mount", "Slime Mount",
				new ItemStack(Material.SLIME_BALL), rarity, 67));
		addReward(new UnknownPackageReward(donationManager, "Glacial Steed", "Glacial Steed",
				new ItemStack(Material.SNOW_BALL), rarity, 50));

		// Morphs
		addReward(new UnknownPackageReward(donationManager, "Cow Morph", "Cow Morph",
				new ItemStack(Material.LEATHER), rarity, 167));
		addReward(new UnknownPackageReward(donationManager, "Villager Morph", "Villager Morph",
				new ItemStack(Material.EMERALD), rarity, 83));
		addReward(new UnknownPackageReward(donationManager, "Chicken Morph", "Chicken Morph",
				new ItemStack(Material.FEATHER), rarity, 50));
		addReward(new UnknownPackageReward(donationManager, "Enderman Morph", "Enderman Morph",
				new ItemStack(Material.ENDER_PEARL), rarity, 33));
		

		// Gadgets
		addReward(new InventoryReward(inventoryManager, "Coin Party Bomb", "Coin Party Bomb", 1, 1,
				new ItemStack(Material.getMaterial(175)), rarity, 100));

		// Costumes
		addReward(new UnknownPackageReward(donationManager, "Rave Hat", "Rave Hat",
				new ItemStack(Material.LEATHER_HELMET), rarity, 30));
		addReward(new UnknownPackageReward(donationManager, "Rave Shirt", "Rave Shirt",
				new ItemStack(Material.LEATHER_CHESTPLATE), rarity, 30));
		addReward(new UnknownPackageReward(donationManager, "Rave Pants", "Rave Pants",
				new ItemStack(Material.LEATHER_LEGGINGS), rarity, 30));
		addReward(new UnknownPackageReward(donationManager, "Rave Boots", "Rave Boots",
				new ItemStack(Material.LEATHER_BOOTS), rarity, 30));
		addReward(new UnknownPackageReward(donationManager, "Space Helmet", "Space Helmet",
				new ItemStack(Material.GLASS), rarity, 50));
		addReward(new UnknownPackageReward(donationManager, "Space Jacket", "Space Jacket",
				new ItemStack(Material.GOLD_CHESTPLATE), rarity, 50));
		addReward(new UnknownPackageReward(donationManager, "Space Pants", "Space Pants",
				new ItemStack(Material.GOLD_LEGGINGS), rarity, 50));
		addReward(new UnknownPackageReward(donationManager, "Space Boots", "Space Boots",
				new ItemStack(Material.GOLD_BOOTS), rarity, 50));

	}

	public void addLegendary(DonationManager donationManager, InventoryManager inventoryManager, PetManager petManager, double minValue, double maxValue)
	{
		RewardRarity rarity = RewardRarity.LEGENDARY;

		// Coins
		addReward(new CoinReward(donationManager, (int)minValue, (int)maxValue, 25, RewardRarity.LEGENDARY));

		// Mounts
		addReward(new UnknownPackageReward(donationManager, "Infernal Horror", "Infernal Horror",
				new ItemStack(Material.BONE), rarity, 33));

		// Morphs
		addReward(new UnknownPackageReward(donationManager, "Bat Morph", "Bat Morph",
				new ItemStack(Material.SKULL_ITEM, 1, (short) 0, (byte) 1), rarity, 25));
		addReward(new UnknownPackageReward(donationManager, "Block Morph", "Block Morph",
				new ItemStack(Material.EMERALD_BLOCK), rarity, 20));

		// Particles
		addReward(new UnknownPackageReward(donationManager, "Shadow Walk Particles", "Shadow Walk",
				new ItemStack(Material.LEATHER_BOOTS), rarity, 33));
		addReward(new UnknownPackageReward(donationManager, "Enchanted Particles", "Enchanted",
				new ItemStack(Material.BOOK), rarity, 25));
		addReward(new UnknownPackageReward(donationManager, "Flame Rings Particles", "Flame Rings",
				new ItemStack(Material.BLAZE_POWDER), rarity, 17));
		addReward(new UnknownPackageReward(donationManager, "Rain Cloud Particles", "Rain Cloud",
				new ItemStack(Material.INK_SACK, 1, (short) 0, (byte) 4), rarity, 13));
		addReward(new UnknownPackageReward(donationManager, "Blood Helix Particles", "Blood Helix",
				new ItemStack(Material.REDSTONE), rarity, 10));
		addReward(new UnknownPackageReward(donationManager, "Emerald Twirl Particles", "Green Ring",
				new ItemStack(Material.EMERALD), rarity, 8));
		addReward(new UnknownPackageReward(donationManager, "Flame Fairy Particles", "Flame Fairy",
				new ItemStack(Material.APPLE), rarity, 4));
		addReward(new UnknownPackageReward(donationManager, "Heart Particles", "I Heart You",
				new ItemStack(Material.BLAZE_POWDER), rarity, 2));
	}

	public void addReward(Reward reward)
	{
		RewardRarity rarity = reward.getRarity();

		List<Reward> treasureList = _treasureMap.get(rarity);

		treasureList.add(reward);
	}

	public Reward[] getRewards(Player player, RewardType type)
	{
		int currentReward = 0;
		Reward[] rewards = new Reward[4];
		boolean hasUncommon = false;
		boolean canGiveMythical = true;

		while (currentReward < 4)
		{
			Reward reward = nextReward(player, rewards, currentReward == 3 && !hasUncommon, type, canGiveMythical);

			if (reward == null)
			{
				continue;
			}

			if (reward.getRarity().ordinal() >= RewardRarity.UNCOMMON.ordinal())
			{
				hasUncommon = true;
			}
			
			//Only allow 1 Mythical
			if (reward.getRarity().ordinal() >= RewardRarity.MYTHICAL.ordinal())
			{
				canGiveMythical = false;
			}
			
			rewards[currentReward] = reward;
			currentReward++;
		}

		// Swap the last reward with another one, this makes the uncommon added at the end of some chests seem more random
		int slotToSwitch = _random.nextInt(4);
		if (slotToSwitch != 3)
		{
			Reward thirdReward = rewards[3];
			Reward otherReward = rewards[slotToSwitch];

			rewards[3] = otherReward;
			rewards[slotToSwitch] = thirdReward;
		}

		return rewards;
	}

//	private Reward nextReward(Player player, Reward[] excludedRewards)
//	{
//		return nextReward(player, excludedRewards, false, isChestOpening);
//	}

	public Reward nextReward(Player player, Reward[] excludedRewards, boolean requiresUncommon, RewardType type, boolean canGiveMythical)
	{
		RewardRarity rarity = type.generateRarity(requiresUncommon);
		
		//Dont give Rank Upgrade if already has Legend
		if (rarity == RewardRarity.MYTHICAL)
		{
			if (!canGiveMythical || _clientManager.Get(player).GetRank().Has(Rank.LEGEND))
			{
				rarity = RewardRarity.LEGENDARY;
			}
			else
			{
				return new RankReward(_clientManager, 0, rarity);
			}
		}
		
		List<Reward> treasureList = _treasureMap.get(rarity);

		int totalWeight = 0;
		ArrayList<Reward> possibleRewards = new ArrayList<Reward>();
		for (Reward treasure : treasureList)
		{
			boolean isExcluded = false;

			if (excludedRewards != null)
			{
				for (int i = 0; i < excludedRewards.length && !isExcluded; i++)
				{
					if (excludedRewards[i] != null && excludedRewards[i].equals(treasure))
					{
						isExcluded = true;
					}
				}
			}

			if ((player == null || treasure.canGiveReward(player)) && !isExcluded)
			{
				possibleRewards.add(treasure);
				totalWeight += treasure.getWeight();
			}
		}

		if (totalWeight > 0)
		{
			int weight = _random.nextInt(totalWeight);
			int currentWeight = 0;

			for (Reward reward : possibleRewards)
			{
				currentWeight += reward.getWeight();

				if (weight <= currentWeight)
					return reward;
			}
		}

		return null;
	}
}
