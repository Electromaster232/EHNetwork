package ehnetwork.game.skyclans.fields;

import org.bukkit.plugin.java.JavaPlugin;

import ehnetwork.core.MiniPlugin;
import ehnetwork.core.creature.Creature;
import ehnetwork.core.energy.Energy;
import ehnetwork.game.skyclans.fields.repository.FieldRepository;
import ehnetwork.minecraft.game.core.condition.ConditionManager;

public class Field extends MiniPlugin
{
	private FieldBlock _block;
	private FieldOre _ore;
	private FieldMonster _mob;
	
	public Field(JavaPlugin plugin, Creature creature, ConditionManager condition, Energy energy, String serverName) 
	{
		super("Field Factory", plugin);
		
		FieldRepository repository = new FieldRepository(plugin);
		_block = new FieldBlock(plugin, condition, energy, repository, serverName);
		_ore = new FieldOre(plugin, repository, serverName);
		_mob = new FieldMonster(plugin, repository, creature, serverName);
	}
	
	public FieldBlock GetBlock()
	{
		return _block;
	}
	
	public FieldOre GetOre()
	{
		return _ore;
	}
	
	public FieldMonster GetMonster()
	{
		return _mob;
	}	
}
