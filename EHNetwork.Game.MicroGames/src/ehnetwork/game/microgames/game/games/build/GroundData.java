package ehnetwork.game.microgames.game.games.build;

import org.bukkit.Material;

public class GroundData
{
	public final Material _material;
	public final byte _data;

	public GroundData(Material material)
	{
		this(material, (byte) 0);
	}

	public GroundData(Material material, byte data)
	{
		_material = material;
		_data = data;
	}

	public byte getData()
	{
		return _data;
	}

	public Material getMaterial()
	{
		return _material;
	}
}
