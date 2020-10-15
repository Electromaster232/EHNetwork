package ehnetwork.game.arcade.game.games.draw.tools;


import java.util.HashMap;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import ehnetwork.core.common.util.UtilEvent;
import ehnetwork.core.common.util.UtilEvent.ActionType;
import ehnetwork.core.common.util.UtilGear;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.game.arcade.game.games.draw.Draw;

public abstract class Tool
{
	protected Draw Host;
	
	protected Player _drawer;
	protected Block _start;
	
	protected Material _material;
	
	protected HashMap<Block, Byte> _past = new HashMap<Block, Byte>();
	protected HashMap<Block, Byte> _new = new HashMap<Block, Byte>();
	
	public Tool(Draw host, Material mat)
	{
		Host = host;
		_material = mat;
	}
	
	public void start(PlayerInteractEvent event)
	{
		if (!UtilEvent.isAction(event, ActionType.R))
			return;
		Block block = event.getPlayer().getTargetBlock((Set<Material>) null, 60);

		if (block == null)
			return;

		if (!Host.getCanvas().contains(block))
			return;
		
		if (!UtilGear.isMat(event.getPlayer().getItemInHand(), _material))
			return;
		
		_drawer = event.getPlayer();
		_start = block;
		
		Host.setLock(false);
	}
	
	public void update()
	{
		if (_start == null || _drawer == null)
			return;
		
		if (!_drawer.isOnline() || !Host.isDrawer(_drawer) || !_drawer.isBlocking())
		{
			_drawer = null;
			_start = null;
			_past.clear();
			return;
		}
		
		_new = new HashMap<Block, Byte>();
		
		//Calculate New
		Block end = _drawer.getTargetBlock((Set<Material>) null, 64);
		if (end != null && Host.getCanvas().contains(end))
		{
			customDraw(end);
		}
		
		//Remove Old
		for (Block block : _past.keySet())
		{
			if (!_new.containsKey(block))
				block.setData(_past.get(block));
		}
		
		_past = _new;
		_new = null;
		
		for (Player other : UtilServer.getPlayers())
			other.playSound(other.getLocation(), Sound.FIZZ, 0.2f, 2f);
	}
	
	public abstract void customDraw(Block end);
}
