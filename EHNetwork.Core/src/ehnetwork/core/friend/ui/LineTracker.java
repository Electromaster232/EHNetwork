package ehnetwork.core.friend.ui;

import java.util.UUID;

import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.PacketPlayOutPlayerInfo;
import net.minecraft.util.com.mojang.authlib.GameProfile;

public class LineTracker
{
	private String _line = null;
	private String _oldLine = null;
	private PacketPlayOutPlayerInfo _clearOldPacket;
	private PacketPlayOutPlayerInfo _addNewPacket;
	private PacketPlayOutPlayerInfo _clearNewPacket;

	public LineTracker(String line)
	{
		setLine(line);
	}

	public boolean setLine(String s)
	{
		if (s != null && s.length() > 16)
			s = s.substring(0, 16);

		if (_line != null && _line.compareTo(s) == 0)
			return false;

		_oldLine = _line;
		_line = s;

		if (_oldLine != null)
		{
			 _clearOldPacket = new PacketPlayOutPlayerInfo();
			 _clearOldPacket.username = _oldLine;
			 _clearOldPacket.action = PacketPlayOutPlayerInfo.REMOVE_PLAYER;
			 _clearOldPacket.ping = 0;
			 _clearOldPacket.player = new GameProfile(UUID.randomUUID(), _oldLine);
		}

		if (_line != null)
		{
			_addNewPacket = new PacketPlayOutPlayerInfo();
			_addNewPacket.username = _line;
			_addNewPacket.action = PacketPlayOutPlayerInfo.ADD_PLAYER;
			_addNewPacket.ping = 0;
			_addNewPacket.player = new GameProfile(UUID.randomUUID(), _line);
			
			_clearNewPacket = new PacketPlayOutPlayerInfo();
			_clearNewPacket.username = _line;
			_clearNewPacket.action = PacketPlayOutPlayerInfo.REMOVE_PLAYER;
			_clearNewPacket.ping = 0;
			_clearNewPacket.player = new GameProfile(UUID.randomUUID(), _line);
		}

		return true;
	}

	public void displayLineToPlayer(EntityPlayer entityPlayer)
	{
		if (_oldLine != null)
		{
			entityPlayer.playerConnection.sendPacket(_clearOldPacket);
		}

		if (_line != null)
		{
			entityPlayer.playerConnection.sendPacket(_addNewPacket);
		}
	}

	public void removeLineForPlayer(EntityPlayer entityPlayer)
	{
		if (_line != null)
		{
			entityPlayer.playerConnection.sendPacket(_clearNewPacket);
		}
	}

	public void clearOldLine()
	{
		_oldLine = null;
	}
}
