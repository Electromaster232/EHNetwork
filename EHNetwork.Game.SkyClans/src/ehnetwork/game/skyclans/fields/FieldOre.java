package ehnetwork.game.skyclans.fields;

import java.util.ArrayList;
import java.util.HashSet;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import ehnetwork.core.MiniPlugin;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilEvent;
import ehnetwork.core.common.util.UtilGear;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilTime;
import ehnetwork.core.common.util.UtilWorld;
import ehnetwork.core.common.util.UtilEvent.ActionType;
import ehnetwork.game.skyclans.fields.commands.FieldOreCommand;
import ehnetwork.game.skyclans.fields.repository.FieldOreToken;
import ehnetwork.game.skyclans.fields.repository.FieldRepository;

public class FieldOre extends MiniPlugin
{
	private FieldRepository _repository;
	private HashSet<String> _active = new HashSet<String>();
	
	private ArrayList<FieldOreData> _oreInactive = new ArrayList<FieldOreData>();
	private ArrayList<FieldOreData> _oreActive = new ArrayList<FieldOreData>();

	private HashSet<Location> _oreLocations = new HashSet<Location>();

	private long _oreRegen = 0;
	private long _oreRegenTime = 20000;
	
	private String _serverName;
	
	public FieldOre(JavaPlugin plugin, FieldRepository repository, String serverName) 
	{
		super("Field Ore", plugin);
		
		_repository = repository;
		_serverName = serverName;
		
		load();
	}
	
	@Override
	public void addCommands()
	{
		addCommand(new FieldOreCommand(this));
	}
	
	public void help(Player caller) 
	{
		UtilPlayer.message(caller, F.main(_moduleName, "Commands List;"));
		UtilPlayer.message(caller, F.help("/fo toggle", "Toggle Tools", Rank.ADMIN));
		UtilPlayer.message(caller, F.help("/fo list", "List Ores", Rank.ADMIN));
		UtilPlayer.message(caller, F.help("/fo fill", "Set Ores to Ore", Rank.ADMIN));
		UtilPlayer.message(caller, F.help("/fo reset", "Reset Ores to Stone", Rank.ADMIN));
		UtilPlayer.message(caller, F.help("/fo wipe", "Delete All Ore Fields (Database)", Rank.ADMIN));
	}
	
	@EventHandler
	public void handleInteract(PlayerInteractEvent event)
	{
		if (!_active.contains(event.getPlayer().getName()))
			return;
		
		if (UtilGear.isMat(event.getPlayer().getItemInHand(), Material.DIAMOND))
		{
			if (UtilEvent.isAction(event, ActionType.L))
				addBlock(event.getPlayer(), event);

			else if (UtilEvent.isAction(event, ActionType.R_BLOCK))
				delBlock(event.getPlayer(), event);
		}
	}
	
	public void reset(Player player)
	{
		for (FieldOreData ore : _oreActive)
		{
			ore.SetActive(false);
			_oreInactive.add(ore);
		}

		_oreActive.clear();
		
		UtilPlayer.message(player, F.main(_moduleName, "Field Ore Reset."));
	}
	
	public void fill(Player player)
	{
		while (!_oreInactive.isEmpty())
			_oreInactive.get(UtilMath.r(_oreInactive.size())).StartVein(2 + UtilMath.r(5));

		UtilPlayer.message(player, F.main(_moduleName, "Field Ore Generated."));
	}
	
	public void list(Player player)
	{
		UtilPlayer.message(player, F.main(_moduleName, F.value("Total", ""+_oreLocations.size())));
		UtilPlayer.message(player, F.main(_moduleName, F.value("Active", ""+_oreActive.size())));
		UtilPlayer.message(player, F.main(_moduleName, F.value("Inactive", ""+_oreInactive.size())));
	}
	
	public void wipe(Player player)
	{
		reset(player);
		
		for (Location loc : _oreLocations)
		{
			_repository.deleteFieldOre(_serverName, UtilWorld.locToStr(loc));
		}

		_oreInactive.clear();
		_oreLocations.clear();

		UtilPlayer.message(player, F.main(_moduleName, "Field Ore Wiped."));
	}
	
	private void addBlock(Player player, PlayerInteractEvent event) 
	{
		Block block = player.getTargetBlock(null, 0);

		if (Get(block.getLocation()) != null)
		{
			UtilPlayer.message(player, F.main(_moduleName, "This is already Field Ore."));
			return;
		}

		//Repo
		FieldOreToken token = new FieldOreToken();
		token.Server = _serverName;
		token.Location = UtilWorld.locToStr(block.getLocation());
		_repository.addFieldOre(token);

		//Memory
		_oreInactive.add(new FieldOreData(this, block.getLocation()));

		//Inform
		block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, 57);
		UtilPlayer.message(player, F.main(_moduleName, "Field Ore Added."));

		event.setCancelled(true);
	}

	private void delBlock(Player player, PlayerInteractEvent event) 
	{
		event.setCancelled(true);

		FieldOreData ore = Get(event.getPlayer().getTargetBlock(null, 0).getLocation());

		if (ore == null)
		{
			UtilPlayer.message(player, F.main(_moduleName, "This is not Field Ore."));
			return;
		}
		
		_repository.deleteFieldOre(_serverName, UtilWorld.locToStr(event.getClickedBlock().getLocation()));

		ore.GetLocation().getBlock().setType(Material.STONE);

		ore.Delete();

		_oreActive.remove(ore);
		_oreInactive.remove(ore);
		_oreLocations.remove(ore);

		//Inform
		event.getClickedBlock().getWorld().playEffect(event.getClickedBlock().getLocation(), Effect.STEP_SOUND, 57);
		UtilPlayer.message(player, F.main(_moduleName, "Field Ore Removed."));	
	}

	public FieldOreData Get(Location loc)
	{
		for (FieldOreData ore : _oreInactive)
			if (ore.GetLocation().equals(loc))
				return ore;

		for (FieldOreData ore : _oreActive)
			if (ore.GetLocation().equals(loc))
				return ore;

		return null;
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void Break(BlockBreakEvent event)
	{
		if (event.isCancelled())
			return;

		if (!_oreLocations.contains(event.getBlock().getLocation()))
			return;
		
		FieldOreData ore = Get(event.getBlock().getLocation());

		if (ore == null)
			return;

		event.setCancelled(true);

		ore.OreMined(event.getPlayer().getEyeLocation());
	}

	@EventHandler
	private void Regenerate(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FAST)
			return;

		if (_oreInactive.isEmpty())
			return;

		if (!UtilTime.elapsed(_oreRegen, UtilField.scale(_oreRegenTime)))
			return;

		_oreRegen = System.currentTimeMillis();

		//Start!
		_oreInactive.get(UtilMath.r(_oreInactive.size())).StartVein(2 + UtilMath.r(5));
	}

	public ArrayList<FieldOreData> GetActive() 
	{
		return _oreActive;
	}

	public ArrayList<FieldOreData> GetInactive() 
	{
		return _oreInactive;
	}
	
	public HashSet<Location> GetLocations()
	{
		return _oreLocations;
	}
	
	public void load()
	{
		clean();

		for (FieldOreToken token : _repository.getFieldOres(_serverName))
		{
			Location loc = UtilWorld.strToLoc(token.Location);
			
			loc.getBlock().setType(Material.STONE);
			_oreInactive.add(new FieldOreData(this, loc));
			_oreLocations.add(loc);
		}
	}

	public void clean()
	{
		reset(null);
		_oreInactive.clear();
		_oreActive.clear();
		_oreLocations.clear();
	}

	public HashSet<String> getActivePlayers()
	{
		return _active;
	}
}
