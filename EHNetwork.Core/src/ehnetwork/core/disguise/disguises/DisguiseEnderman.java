package ehnetwork.core.disguise.disguises;

import java.util.Arrays;

import org.bukkit.entity.EntityType;
import net.minecraft.server.v1_7_R4.MobEffect;
import net.minecraft.server.v1_7_R4.MobEffectList;
import net.minecraft.server.v1_7_R4.PotionBrewer;

import org.spigotmc.ProtocolData;

public class DisguiseEnderman extends DisguiseMonster
{
	public DisguiseEnderman(org.bukkit.entity.Entity entity)
	{
		super(EntityType.ENDERMAN, entity);
		
		DataWatcher.a(16, new ProtocolData.ByteShort( (short) 0 ) );
		DataWatcher.a(17, new Byte((byte)0));
		DataWatcher.a(18, new Byte((byte)0));
		
        int i = PotionBrewer.a(Arrays.asList(new MobEffect(MobEffectList.FIRE_RESISTANCE.id, 777)));
        DataWatcher.watch(8, Byte.valueOf((byte)(PotionBrewer.b(Arrays.asList(new MobEffect(MobEffectList.FIRE_RESISTANCE.id, 777))) ? 1 : 0)));
        DataWatcher.watch(7, Integer.valueOf(i));
	}

	public void UpdateDataWatcher()
	{
		super.UpdateDataWatcher();

		DataWatcher.watch(0, Byte.valueOf((byte)(DataWatcher.getByte(0) & ~(1 << 0))));
		DataWatcher.watch(16, new ProtocolData.ByteShort( DataWatcher.getShort(16) ));
	}
	
	public void SetCarriedId(int i)
	{
		DataWatcher.watch(16, new ProtocolData.ByteShort( (short)(i & 0xFF)) );
	}
	
	public int GetCarriedId()
	{
		return DataWatcher.getByte(16);
	}
	
	public void SetCarriedData(int i)
	{
		DataWatcher.watch(17, Byte.valueOf((byte)(i & 0xFF)));
	}
	
	public int GetCarriedData()
	{
		return DataWatcher.getByte(17);
	}

	public boolean bX()
	{
		return DataWatcher.getByte(18) > 0;
	}
	
	public void a(boolean flag)
	{
		DataWatcher.watch(18, Byte.valueOf((byte)(flag ? 1 : 0)));
	}
	
    protected String getHurtSound()
    {
        return "mob.endermen.hit";
    }
}
