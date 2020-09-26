package ehnetwork.core.reward;


import ehnetwork.core.common.util.C;

/**
 * Created by Shaun on 9/2/2014.
 */
public enum RewardRarity
{
	/**
	 * This will probably be used to figure out what effects are shown when the chest is opened
	 *
	 * (Fireworks, sounds, etc)
	 */

	OTHER("Other", C.cWhite),
	COMMON("Common", C.cWhite),
	UNCOMMON("Uncommon", C.cAqua),
	RARE("Rare", C.cPurple),
	LEGENDARY("Legendary", C.cGreen),
	MYTHICAL("Mythical", C.cRed);

	private String _name;
	private String _color;

	RewardRarity(String name, String color)
	{
		_name = name;
		_color = color;
	}

	public String getColor()
	{
		return _color;
	}

	public String getName()
	{
		return _name;
	}

}
