package mineplex.core.gadget.types;

import java.util.ArrayList;
import java.util.Iterator;

import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilBlock;
import mineplex.core.common.util.UtilMath;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.gadget.GadgetManager;
import mineplex.core.gadget.event.GadgetActivateEvent;
import mineplex.core.gadget.event.GadgetBlockEvent;
import mineplex.core.gadget.gadgets.SongData;
import mineplex.core.recharge.Recharge;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class MusicGadget extends Gadget
{
	private int _id;
	private long _duration;
	
	private ArrayList<SongData> _songs = new ArrayList<SongData>();
	
	public MusicGadget(GadgetManager manager, String name, String[] desc, int cost, int id, long duration)
	{
		super(manager, GadgetType.MusicDisc, name, desc, cost, Material.getMaterial(id), (byte)0);
		
		_id = id;
		_duration = duration;
	}

	@Override
	public void Enable(Player player)
	{
		GadgetActivateEvent gadgetEvent = new GadgetActivateEvent(player, this);		
		Bukkit.getServer().getPluginManager().callEvent(gadgetEvent);
		
		if (gadgetEvent.isCancelled())
		{
			UtilPlayer.message(player, F.main("Inventory", GetName() + " is not enabled."));
			return;
		}
		
		if (!Manager.canPlaySongAt(player.getLocation()))
		{
			UtilPlayer.message(player, F.main("Music", "There is already a song playing."));
			return;
		}
		
		//Near Portal
		for (Block block : UtilBlock.getInRadius(player.getLocation(), 3).keySet())
		{
			if (block.getType() == Material.PORTAL)
			{
				UtilPlayer.message(player, F.main("Music", "Cannot play songs near Portals."));
				return;
			}
		}
		
		//Invalid Location
		Block block = player.getLocation().getBlock();
		for (int x=-1 ; x<=1 ; x++)
			for (int z=-1 ; z<=1 ; z++)
				if (!UtilBlock.airFoliage(block.getRelative(x, 0, z)))
				{
					UtilPlayer.message(player, F.main("Music", "You cannot place a Jukebox here."));
					return;
				}
		
		//Near Parkour
		ArrayList<Block> blocks = new ArrayList<Block>();
		blocks.add(block);
		GadgetBlockEvent gadgetBlockEvent = new GadgetBlockEvent(this, blocks);
		Bukkit.getServer().getPluginManager().callEvent(gadgetBlockEvent);
		
		if (gadgetEvent.isCancelled())
		{
			UtilPlayer.message(player, F.main("Music", "You cannot place a Jukebox here."));
			return;
		}
		
		player.getWorld().playEffect(player.getLocation(), Effect.RECORD_PLAY, _id);
		
		_songs.add(new SongData(player.getLocation().getBlock(), _duration));
	}
	
	@Override
	public void EnableCustom(Player player)
	{
		
	}

	@Override
	public void DisableCustom(Player player)
	{
		
	}
	
	@EventHandler
	public void Update(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FASTER)
			return;
		
		Iterator<SongData> songIterator = _songs.iterator();
		
		while (songIterator.hasNext())
		{
			SongData song = songIterator.next();
			
			if (song.update())
				songIterator.remove();
		}
	}
	
	@EventHandler
	public void gadgetBlockChange(GadgetBlockEvent event)
	{
		for (Iterator<Block> iterator = event.getBlocks().iterator(); iterator.hasNext();)
		{
			Block block = iterator.next();
			
			for (SongData data : _songs)
			{
				if (data.Block.equals(block))
				{
					iterator.remove();
					break;
				}
			}	
		}
	}

	public boolean canPlayAt(Location location)
	{
		if (!_songs.isEmpty())
			return false;
		
//		for (SongData data : _songs)
//		{
//			if (UtilMath.offset(data.Block.getLocation(), location) < 48)
//			{
//				return false;
//			}
//		}
		
		return true;
	}
}
