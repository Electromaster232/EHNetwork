package ehnetwork.core.reward;

public enum RewardType
{
	//% Chances			Mythic		Legend		Rare		Uncommon
	GameLoot(			0.000001,	0.00001,	0.0001,		3),
	OldChest(			0,			0.05,		0.4,		5),
	AncientChest(		0,			1,			4,			25),
	MythicalChest(		0.5,		3,			12,			75);
	
	private double _mythicalChance;
	private double _legendaryChance;
	private double _rareChance;
	private double _uncommonChance;
	
	RewardType(double mythical, double legend, double rare, double uncommon)
	{
		_mythicalChance = 	(mythical / 100d);
		_legendaryChance = 	_mythicalChance + (legend / 100d);		//Add previous chance to prep for generateRarity random values.
		_rareChance = 		_legendaryChance + (rare / 100d);
		_uncommonChance = 	_rareChance + (uncommon / 100d);
	}

	public RewardRarity generateRarity(boolean requiresUncommon)
	{
		double rand = Math.random();
		
		RewardRarity rarity = RewardRarity.COMMON;
		
		if (rand <= _mythicalChance)							rarity = RewardRarity.MYTHICAL;
		else if (rand <= _legendaryChance)						rarity = RewardRarity.LEGENDARY;
		else if (rand <= _rareChance)							rarity = RewardRarity.RARE;
		else if (rand <= _uncommonChance || requiresUncommon)	rarity = RewardRarity.UNCOMMON;

		return rarity;
	}
}
