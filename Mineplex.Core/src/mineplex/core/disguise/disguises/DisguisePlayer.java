package mineplex.core.disguise.disguises;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_7_R4.MathHelper;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_7_R4.PacketPlayOutPlayerInfo;
import net.minecraft.util.com.mojang.authlib.GameProfile;

public class DisguisePlayer extends DisguiseHuman
{
	private GameProfile _profile;
	private boolean _sneaking;
	private BlockFace _sleeping;

	public DisguisePlayer(org.bukkit.entity.Entity entity)
	{
		super(entity);
	}

	public DisguisePlayer(org.bukkit.entity.Entity entity, GameProfile profile)
	{
		this(entity);

		setProfile(profile);
	}

	public void setProfile(GameProfile profile)
	{
		GameProfile newProfile = new GameProfile(UUID.randomUUID(), profile.getName());

		newProfile.getProperties().putAll(profile.getProperties());

		_profile = newProfile;
	}

	public BlockFace getSleepingDirection()
	{
		return _sleeping;
	}

	/**
	 * Don't use this if the disguise is already on as it will not work the way you want it to. Contact libraryaddict if you need
	 * that added.
	 */
	public void setSleeping(BlockFace sleeping)
	{
		_sleeping = sleeping;
	}

	public void setSneaking(boolean sneaking)
	{
		_sneaking = sneaking;
	}

	public Packet getOldInfoPacket(boolean add)
	{
		PacketPlayOutPlayerInfo playerInfo = new PacketPlayOutPlayerInfo();

		if (Entity instanceof Player)
		{
			playerInfo.username = Entity.getName();
			playerInfo.action = add ? 0 : 4;
			playerInfo.ping = 90;
			playerInfo.player = ((CraftPlayer) (Player) Entity).getProfile();
			playerInfo.gamemode = 0;
		}

		return playerInfo;
	}

	public Packet getNewInfoPacket(boolean add)
	{
		PacketPlayOutPlayerInfo newDisguiseInfo = new PacketPlayOutPlayerInfo();
		newDisguiseInfo.username = _profile.getName();
		newDisguiseInfo.action = add ? 0 : 4;
		newDisguiseInfo.ping = 90;
		newDisguiseInfo.player = _profile;
		newDisguiseInfo.gamemode = 0;

		return newDisguiseInfo;
	}

	@Override
	public void UpdateDataWatcher()
	{
		super.UpdateDataWatcher();

		byte b0 = DataWatcher.getByte(0);

		if (_sneaking)
			DataWatcher.watch(0, Byte.valueOf((byte) (b0 | 1 << 1)));
		else
			DataWatcher.watch(0, Byte.valueOf((byte) (b0 & ~(1 << 1))));
	}

	public PacketPlayOutNamedEntitySpawn spawnBeforePlayer(Location spawnLocation)
	{
		Location loc = spawnLocation.add(spawnLocation.getDirection().normalize().multiply(30));
		loc.setY(Math.max(loc.getY(), 0));

		PacketPlayOutNamedEntitySpawn packet = new PacketPlayOutNamedEntitySpawn();
		packet.a = Entity.getId();
		packet.b = _profile;
		packet.c = MathHelper.floor(loc.getX() * 32.0D);
		packet.d = MathHelper.floor(loc.getY() * 32.0D);
		packet.e = MathHelper.floor(loc.getZ() * 32.0D);
		packet.f = (byte) ((int) (loc.getYaw() * 256.0F / 360.0F));
		packet.g = (byte) ((int) (loc.getPitch() * 256.0F / 360.0F));
		packet.i = DataWatcher;

		return packet;
	}

	@Override
	public PacketPlayOutNamedEntitySpawn GetSpawnPacket()
	{
		PacketPlayOutNamedEntitySpawn packet = new PacketPlayOutNamedEntitySpawn();
		packet.a = Entity.getId();
		packet.b = _profile;
		packet.c = MathHelper.floor(Entity.locX * 32.0D);
		packet.d = MathHelper.floor(Entity.locY * 32.0D);
		packet.e = MathHelper.floor(Entity.locZ * 32.0D);
		packet.f = (byte) ((int) (Entity.yaw * 256.0F / 360.0F));
		packet.g = (byte) ((int) (Entity.pitch * 256.0F / 360.0F));
		packet.i = DataWatcher;

		return packet;
	}

	public String getName()
	{
		return _profile.getName();
	}
}
