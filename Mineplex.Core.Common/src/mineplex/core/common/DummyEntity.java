package mineplex.core.common;

import net.minecraft.server.v1_7_R4.Entity;
import net.minecraft.server.v1_7_R4.NBTTagCompound;
import net.minecraft.server.v1_7_R4.World;

public class DummyEntity extends Entity
{
	public DummyEntity(World world)
	{
		super(world);
	}

	@Override
	protected void c()
	{
	}

	@Override
	protected void a(NBTTagCompound nbttagcompound)
	{
	}

	@Override
	protected void b(NBTTagCompound nbttagcompound)
	{
	}
}
