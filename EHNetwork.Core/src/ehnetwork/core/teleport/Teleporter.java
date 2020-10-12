package ehnetwork.core.teleport;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import ehnetwork.core.common.util.UtilPlayer;

public class Teleporter 
{
	private Player _pA;
	private Location _loc;
	private String _mA;

	private Player _pB;
	private String _mB;

	private Teleport _tp;
	
	public Teleporter(Teleport teleport, Player pA, Player pB, String mA, String mB, Location loc, boolean record, String log)
	{
		this._tp = teleport;
		this._pA = pA;
		this._pB = pB;
		this._mA = mA;
		this._mB = mB;
		this._loc = loc; 
	}

	public void doTeleport()
	{
		if (_loc == null)
			return;

		//Different Worlds
		/*
		if (!_pA.getWorld().getName().equals(_loc.getWorld().getName()))
		{
			if (_pB == null)
				return;
			
			_tp.UtilPlayer.message(_pB, F.main("Teleport", F.elem(_pA.getName()) + " is not in teleport destinations world."));
			return;
		}
		*/
		
		//Player A
		if (_pA != null)
		{
			
			//Teleport
			_tp.TP(_pA, _loc);
			
			//Inform
			if (_mA != null)
				UtilPlayer.message(_pA, _mA);
		}
		
		//Player B
		if (_pB != null && _mB != null)
			UtilPlayer.message(_pB, _mB);
	}
}
