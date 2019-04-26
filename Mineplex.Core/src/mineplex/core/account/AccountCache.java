package mineplex.core.account;

import java.util.UUID;

import mineplex.serverdata.data.Data;

public class AccountCache implements Data
{
	private UUID _uuid;
	private Integer _id;
	
	public AccountCache(UUID uuid, int id)
	{
		_uuid = uuid;
		_id = id;
	}
	
	public UUID getUUID()
	{
		return _uuid;
	}
	
	public int getId()
	{
		return _id;
	}

	@Override
	public String getDataId()
	{
		return _uuid.toString();
	}
}
