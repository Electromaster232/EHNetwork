package ehnetwork.minecraft.game.classcombat.item;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;

import ehnetwork.core.common.util.UtilAction;
import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.common.util.UtilEvent;
import ehnetwork.core.common.util.UtilEvent.ActionType;
import ehnetwork.core.common.util.UtilGear;
import ehnetwork.core.common.util.UtilInv;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.core.projectile.IThrown;
import ehnetwork.core.projectile.ProjectileUser;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.minecraft.game.classcombat.item.event.ItemTriggerEvent;

public abstract class ItemUsable extends Item implements IThrown
{	
	private ActionType _useAction;
	private boolean _useStock;
	private long _useDelay;
	private int _useEnergy;

	private ActionType _throwAction;
	private boolean _throwStock;
	private long _throwDelay;
	private int _throwEnergy;
	private float _throwPower;

	private long _throwExpire;
	private boolean _throwPlayer;
	private boolean _throwBlock;
	private boolean _throwIdle;
	private boolean _throwPickup;

	public ItemUsable(ItemFactory factory, String name, String[] desc,
			Material type, int amount, boolean canDamage, int gemCost, int tokenCost, 
			ActionType useAction, boolean useStock, long useDelay, int useEnergy,
			ActionType throwAction, boolean throwStock, long throwDelay, int throwEnergy, float throwPower, 
			long throwExpire, boolean throwPlayer, boolean throwBlock, boolean throwIdle, boolean throwPickup) 
	{
		super(factory, name, desc, type, amount, canDamage, gemCost, tokenCost);
		_useAction = useAction;
		_throwAction = throwAction;
		_useStock = useStock;
		_useDelay = useDelay;
		_useEnergy = useEnergy;

		_throwStock = throwStock;
		_throwDelay = throwDelay;
		_throwPower = throwPower;
		_throwEnergy = throwEnergy;
		_throwExpire = throwExpire;
		_throwIdle = throwIdle;
		_throwPlayer = throwPlayer;
		_throwBlock = throwBlock;
		_throwPickup = throwPickup;
	}

	@EventHandler
	public void Use(PlayerInteractEvent event)
	{		
		if (_useAction == null)	
			return;

		Player player = event.getPlayer();

		if (!UtilGear.isMat(player.getItemInHand(), GetType()))
			return;

		if (!UtilEvent.isAction(event, _useAction))
			return;

		if (UtilBlock.usable(event.getClickedBlock()))
			return;

		event.setCancelled(true);

		//Check Allowed
		ItemTriggerEvent trigger = new ItemTriggerEvent(player, this);
		Bukkit.getServer().getPluginManager().callEvent(trigger);

		if (trigger.IsCancelled())
			return;

		if (!EnergyRecharge(player, GetName(), _useEnergy, _useDelay))
			return;

		if (_useStock)
		{
			if (player.getItemInHand().getAmount() > 1)
				player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
			else
				player.setItemInHand(null);

			UtilInv.Update(player);
		}

		UseAction(event);		
	}	

	public abstract void UseAction(PlayerInteractEvent event);

	@EventHandler
	public void Throw(PlayerInteractEvent event)
	{
		if (_throwAction == null)	
			return;

		Player player = event.getPlayer();

		if (((CraftPlayer)player).getHandle().spectating)
			return;

		if (!UtilGear.isMat(player.getItemInHand(), GetType()))
			return;

		if (!UtilEvent.isAction(event, _throwAction))
			return;

		if (UtilBlock.usable(event.getClickedBlock()))
			return;

		event.setCancelled(true);

		//Check Allowed
		ItemTriggerEvent trigger = new ItemTriggerEvent(player, this);
		Bukkit.getServer().getPluginManager().callEvent(trigger);

		if (trigger.IsCancelled())
			return;

		if (!EnergyRecharge(player, GetName(), _throwEnergy, _throwDelay))
			return;

		if (_throwStock)
		{
			if (player.getItemInHand().getAmount() > 1)
				player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
			else
				player.setItemInHand(null);

			UtilInv.Update(player);
		}

		//Throw
		long expire = -1;
		if (_throwExpire >= 0)
			expire = System.currentTimeMillis() + _throwExpire;

		org.bukkit.entity.Item ent = player.getWorld().dropItem(player.getEyeLocation(), ItemStackFactory.Instance.CreateStack(GetType()));
		UtilAction.velocity(ent, player.getLocation().getDirection(), _throwPower, false, 0, 0.2, 10, false);
		Factory.Throw().AddThrow(ent, player, this, expire, _throwPlayer, _throwBlock, _throwIdle, _throwPickup, 0.5f);

		ThrowCustom(event, ent);
	}

	public void ThrowCustom(PlayerInteractEvent event, org.bukkit.entity.Item ent) 
	{
		//Specifics.
	}

	@Override
	public abstract void Collide(LivingEntity target, Block block, ProjectileUser data);

	@Override
	public abstract void Idle(ProjectileUser data);

	@Override
	public abstract void Expire(ProjectileUser data);

	private boolean EnergyRecharge(Player player, String ability, int energy, long recharge) 
	{
		if (!Factory.Energy().Use(player, ability, energy, false, true))
			return false;

		if (!Recharge.Instance.use(player, ability, recharge, (recharge > 2000), false))
			return false;

		if (!Factory.Energy().Use(player, ability, energy, true, true))
			return false;

		return true;
	}
}
