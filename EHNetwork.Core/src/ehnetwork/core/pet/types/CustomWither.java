package ehnetwork.core.pet.types;

import java.lang.reflect.Field;
import java.util.HashMap;

import org.bukkit.craftbukkit.v1_7_R4.SpigotTimings;
import org.bukkit.entity.EntityType;
import net.minecraft.server.v1_7_R4.EntityTypes;
import net.minecraft.server.v1_7_R4.EntityWither;
import net.minecraft.server.v1_7_R4.MathHelper;
import net.minecraft.server.v1_7_R4.World;

public class CustomWither extends EntityWither
{
    static
    {
        try
        {
            Field f = EntityTypes.class.getDeclaredField("f");
            f.setAccessible(true);
            HashMap map = (HashMap) f.get(null);
            map.put(CustomWither.class, (int) EntityType.WITHER.getTypeId());
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    private int bq;

    public CustomWither(World world)
    {
        super(world);
        s(530);
    }

    @Override
    protected void bn()
    {
        this.aU += 1;
        this.world.methodProfiler.a("checkDespawn");
        w();
        this.world.methodProfiler.b();

        if (this.fromMobSpawner)
        {
            return;
        }

        this.world.methodProfiler.a("sensing");
        this.getEntitySenses().a();
        this.world.methodProfiler.b();
        this.world.methodProfiler.a("targetSelector");
        this.targetSelector.a();
        this.world.methodProfiler.b();
        this.world.methodProfiler.a("goalSelector");
        this.goalSelector.a();
        this.world.methodProfiler.b();
        this.world.methodProfiler.a("navigation");
        this.getNavigation().f();
        this.world.methodProfiler.b();
        this.world.methodProfiler.a("mob tick");
        bp();
        this.world.methodProfiler.b();
        this.world.methodProfiler.a("controls");
        this.world.methodProfiler.a("move");
        this.getControllerMove().c();
        this.world.methodProfiler.c("look");
        this.getControllerLook().a();
        this.world.methodProfiler.c("jump");
        this.getControllerJump().b();
        this.world.methodProfiler.b();
        this.world.methodProfiler.b();
    }

    @Override
    public void e()
    {
        if (this.bq > 0)
        {
            this.bq -= 1;
        }

        if (this.bg > 0)
        {
            double d0 = this.locX + (this.bh - this.locX) / this.bg;
            double d1 = this.locY + (this.bi - this.locY) / this.bg;
            double d2 = this.locZ + (this.bj - this.locZ) / this.bg;
            double d3 = MathHelper.g(this.bk - this.yaw);

            this.yaw = ((float) (this.yaw + d3 / this.bg));
            this.pitch = ((float) (this.pitch + (this.bl - this.pitch) / this.bg));
            this.bg -= 1;

            if (!this.Vegetated)
            {
                setPosition(d0, d1, d2);
            }
            b(this.yaw, this.pitch);
        }
        else if (!br())
        {
            this.motX *= 0.98D;
            this.motY *= 0.98D;
            this.motZ *= 0.98D;
        }

        if (Math.abs(this.motX) < 0.005D)
        {
            this.motX = 0.0D;
        }

        if (Math.abs(this.motY) < 0.005D)
        {
            this.motY = 0.0D;
        }

        if (Math.abs(this.motZ) < 0.005D)
        {
            this.motZ = 0.0D;
        }

        this.world.methodProfiler.a("ai");
        SpigotTimings.timerEntityAI.startTiming();
        if (bh())
        {
            this.bc = false;
            this.bd = 0.0F;
            this.be = 0.0F;
            this.bf = 0.0F;
        }
        else if (br())
        {
            if (bk())
            {
                this.world.methodProfiler.a("newAi");
                bn();
                this.world.methodProfiler.b();
            }
            else
            {
                this.world.methodProfiler.a("oldAi");
                bq();
                this.world.methodProfiler.b();
                this.aO = this.yaw;
            }
        }
        SpigotTimings.timerEntityAI.stopTiming();

        this.world.methodProfiler.b();
        this.world.methodProfiler.a("jump");
        if (this.bc)
        {
            if ((!M()) && (!P()))
            {
                if ((this.onGround) && (this.bq == 0))
                {
                    bj();
                    this.bq = 10;
                }
            }
            else
                this.motY += 0.03999999910593033D;
        }
        else
        {
            this.bq = 0;
        }

        this.world.methodProfiler.b();
        this.world.methodProfiler.a("travel");
        this.bd *= 0.98F;
        this.be *= 0.98F;
        this.bf *= 0.9F;
        SpigotTimings.timerEntityAIMove.startTiming();
        e(this.bd, this.be);
        SpigotTimings.timerEntityAIMove.stopTiming();
        this.world.methodProfiler.b();
        this.world.methodProfiler.a("push");
        if (!this.world.isStatic)
        {
            SpigotTimings.timerEntityAICollision.startTiming();
            bo();
            SpigotTimings.timerEntityAICollision.stopTiming();
        }

        this.world.methodProfiler.b();
    }

    @Override
    protected float bf()
    {
        return 0.4F;
    }
}
