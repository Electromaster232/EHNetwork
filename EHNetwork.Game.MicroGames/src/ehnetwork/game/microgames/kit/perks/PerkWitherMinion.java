package ehnetwork.game.microgames.kit.perks;

import java.util.ArrayList;
import java.util.Iterator;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.util.Vector;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilAction;
import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.common.util.UtilEvent;
import ehnetwork.core.common.util.UtilEvent.ActionType;
import ehnetwork.core.common.util.UtilGear;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.microgames.game.GameTeam;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class PerkWitherMinion extends Perk 
{
	private ArrayList<Skeleton> _ents = new ArrayList<Skeleton>();
	
	public PerkWitherMinion() 
	{
		super("Wither Minions", new String[] 
				{
				C.cYellow + "Left-Click" + C.cGray + " with Diamond Sword to use " + C.cGreen + "Wither Minions"
				});
	}

	@EventHandler
	public void ShootWeb(PlayerInteractEvent event)
	{
		if (event.isCancelled())
			return;

		if (!UtilEvent.isAction(event, ActionType.L))
			return;

		if (UtilBlock.usable(event.getClickedBlock()))
			return;

		if (!UtilGear.isMat(event.getPlayer().getItemInHand(), Material.DIAMOND_SWORD))
			return;

		Player player = event.getPlayer();

		if (!Kit.HasKit(player))
			return;

		if (!Recharge.Instance.use(player, GetName(), 10000, true, true))
			return;

		event.setCancelled(true);

		Manager.GetGame().CreatureAllowOverride = true;
		
		for (int i=0 ; i<2 ; i++)
		{
			Skeleton skel = player.getWorld().spawn(player.getEyeLocation(), Skeleton.class);
			_ents.add(skel);
			
			skel.getEquipment().setHelmet(ItemStackFactory.Instance.CreateStack(Material.SKULL_ITEM, (byte)1, 1));
			
			ItemStack armor = new ItemStack(Material.LEATHER_CHESTPLATE);
			LeatherArmorMeta meta = (LeatherArmorMeta)armor.getItemMeta();
			meta.setColor(Color.BLACK);
			armor.setItemMeta(meta);
			skel.getEquipment().setChestplate(armor);
			
			Manager.GetCondition().Factory().Invisible("Skeleton", skel, skel, 9999, 0, false, false, false);
			Manager.GetCondition().Factory().Speed("Skeleton", skel, skel, 9999, 0, false, false, false);
			
			Vector random = new Vector(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5);
			random.normalize();
			random.multiply(0.1);
			
			UtilAction.velocity(skel, player.getLocation().getDirection().add(random), 1 + Math.random() * 0.4, false, 0, 0.2, 10, false);	
		}

		Manager.GetGame().CreatureAllowOverride = false;
		
		//Inform
		UtilPlayer.message(player, F.main("Game", "You used " + F.skill(GetName()) + "."));

		//Effect
		player.getWorld().playSound(player.getLocation(), Sound.WITHER_HURT, 2f, 0.6f);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void witherFallCancel(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;
		
		if (event.GetCause() != DamageCause.FALL)
			return;
		
		if (!_ents.contains(event.GetDamageeEntity()))
			return;
		
		event.SetCancelled("Minion Fall Damage");
	}
	
	@EventHandler
	public void entityTarget(EntityTargetEvent event)
	{
		if (getWitherTeam() == null)
			return;
		
		if (getWitherTeam().GetPlayers(true).contains(event.getTarget()))
			event.setCancelled(true);
	}
	
	@EventHandler
	public void update(UpdateEvent event) 
	{
		if (event.getType() != UpdateType.TICK)
			return;
		
		Iterator<Skeleton> skelIter = _ents.iterator();
		
		while (skelIter.hasNext())
		{
			Skeleton skel = skelIter.next();
			
			if (!skel.isValid() || skel.getTicksLived() > 300 || skel.getLocation().getY() < 0)
			{
				skel.remove();
				skelIter.remove();
			}
			else 
			{
				if (skel.getTarget() == null)
				{
					skel.setTarget(UtilPlayer.getClosest(skel.getLocation(), getWitherTeam().GetPlayers(true)));
				}
			}
		}
	}
	
	public GameTeam getWitherTeam()
	{
		if (Manager.GetGame() == null)
			return null;
		
		return Manager.GetGame().GetTeam(ChatColor.RED);
	}
}
