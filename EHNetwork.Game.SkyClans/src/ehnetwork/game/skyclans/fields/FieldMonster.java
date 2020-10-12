package ehnetwork.game.skyclans.fields;

import java.util.HashSet;
import java.util.WeakHashMap;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import ehnetwork.core.MiniPlugin;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.common.util.UtilWorld;
import ehnetwork.core.creature.Creature;
import ehnetwork.game.skyclans.fields.commands.FieldMonsterCommand;
import ehnetwork.game.skyclans.fields.monsters.FieldMonsterBase;
import ehnetwork.game.skyclans.fields.repository.FieldMonsterToken;
import ehnetwork.game.skyclans.fields.repository.FieldRepository;

public class FieldMonster extends MiniPlugin
{
	private Creature _creature;
	private FieldRepository _repository;
	private HashSet<FieldMonsterBase> _pits;
	private String _serverName;

	private WeakHashMap<Player, FieldMonsterInput> _input = new WeakHashMap<Player, FieldMonsterInput>();

	public FieldMonster(JavaPlugin plugin, FieldRepository repository, Creature creature, String serverName) 
	{
		super("Field Monster", plugin);
		
		_repository = repository;
		_creature = creature;
		_pits = new HashSet<FieldMonsterBase>();
		_serverName = serverName;
		
		Load();
	}
	
	@Override
	public void addCommands()
	{
		addCommand(new FieldMonsterCommand(this));
	}

	public void Help(Player caller) 
	{
		UtilPlayer.message(caller, F.main(getName(), "Commands List;"));
		UtilPlayer.message(caller, F.help("/fm type <Monster>", "Set Monster Type", Rank.ADMIN));
		UtilPlayer.message(caller, F.help("/fm max <#>", "Set Monster Limit", Rank.ADMIN));
		UtilPlayer.message(caller, F.help("/fm rate <Minutes>", "Set Monster Rate", Rank.ADMIN));
		UtilPlayer.message(caller, F.help("/fm radius <#>", "Set Area Radius", Rank.ADMIN));
		UtilPlayer.message(caller, F.help("/fm height <#>", "Set Area Height", Rank.ADMIN));
		UtilPlayer.message(caller, F.help("/fm create <Name>", "Create at your Location", Rank.ADMIN));
		UtilPlayer.message(caller, F.help("/fm delete <Name>", "Delete Field", Rank.ADMIN));
		UtilPlayer.message(caller, F.help("/fm list", "List Monster Fields", Rank.ADMIN));
		UtilPlayer.message(caller, F.help("/fm info <Name>", "Display Monster Field", Rank.ADMIN));
		UtilPlayer.message(caller, F.help("/fm kill", "Kills all Field Monsters", Rank.ADMIN));
		UtilPlayer.message(caller, F.help("/fm wipe", "Delete All Monster Field (Database)", Rank.ADMIN));
	}

	public void Create(Player caller, String name) 
	{
		FieldMonsterInput input = _input.get(caller);

		if (input.type == null)
		{
			UtilPlayer.message(caller, F.main(getName(), "You have not set Monster Type."));
			return;
		}
		
		for (FieldMonsterBase pit : _pits)
		{
			if (name.equalsIgnoreCase(pit.GetName()))
			{
				UtilPlayer.message(caller, F.main(getName(), "Monster Field with this name already exists."));
				return;
			}
		}

		FieldMonsterBase pit = new FieldMonsterBase(this, name, _serverName, input.type, input.mobMax, input.mobRate, caller.getLocation(), input.radius, input.height);
		Add(pit, true);

		UtilPlayer.message(caller, F.main(getName(), "You created Monster Field."));
		pit.Display(caller);
	}

	private void Add(FieldMonsterBase pit, boolean repo)
	{
		UtilServer.getServer().getPluginManager().registerEvents(pit, getPlugin());
		_pits.add(pit);
		
		if (repo)
			_repository.addFieldMonster(pit.GetToken());
	}

	public void Delete(Player caller, String name)
	{
		HashSet<FieldMonsterBase> remove = new HashSet<FieldMonsterBase>();

		for (FieldMonsterBase pit : _pits)
			if (pit.GetName().equalsIgnoreCase(name))
				remove.add(pit);

		int i = remove.size();

		for (FieldMonsterBase pit : remove)
			Delete(pit, true);

		UtilPlayer.message(caller, F.main(getName(), "Deleted " + i + " Monster Field(s)."));
	}

	private void Delete(FieldMonsterBase pit, boolean repo)
	{
		_pits.remove(pit);
		pit.RemoveMonsters();
		HandlerList.unregisterAll(pit);
		
		if (repo)
			_repository.deleteFieldMonster(_serverName, pit.GetToken().Name); 
	}

	public void Wipe(Player player, boolean repo)
	{
		HashSet<FieldMonsterBase> remove = new HashSet<FieldMonsterBase>();

		for (FieldMonsterBase pit : _pits)
			remove.add(pit);

		_pits.clear();

		for (FieldMonsterBase pit : remove)
			Delete(pit, repo);

		UtilPlayer.message(player, F.main(_moduleName, "Field Monsters Wiped."));
	}

	private void Load()
	{
		Wipe(null, false);

		for (FieldMonsterToken token : _repository.getFieldMonsters(_serverName))
		{
			System.out.println("Found FM token : " + token.Type + " " + token.Centre);
			EntityType type = UtilEnt.searchEntity(null, token.Type, false);
			if (type == null)	
				continue;

			Location loc = UtilWorld.strToLoc(token.Centre);
			if (loc == null)	
				continue;

			FieldMonsterBase pit = new FieldMonsterBase(this, token.Name, _serverName, type, token.MobMax, token.MobRate, loc, token.Radius, token.Height);
			Add(pit, false);
		}
	}

	private void Clean()
	{
		for (FieldMonsterBase pit : _pits)
			pit.RemoveMonsters();
	}

	public WeakHashMap<Player, FieldMonsterInput> getInput()
	{
		return _input;
	}

	public HashSet<FieldMonsterBase> getPits()
	{
		return _pits;
	}

	public Creature getCreature()
	{
		return _creature;
	}
}
