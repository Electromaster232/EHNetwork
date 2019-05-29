package nautilus.game.arcade.game.games.colorswap;


import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import mineplex.core.common.util.C;
import mineplex.core.common.util.F;
import mineplex.core.common.util.NautHashMap;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilServer;
import mineplex.core.common.util.UtilTextBottom;
import mineplex.core.common.util.UtilTextMiddle;
import mineplex.core.common.util.UtilTime;
import mineplex.core.itemstack.ItemStackFactory;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.minecraft.game.core.damage.CustomDamageEvent;
import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.GameType;
import nautilus.game.arcade.events.GameStateChangeEvent;
import nautilus.game.arcade.game.SoloGame;
import nautilus.game.arcade.game.games.colorswap.kits.KitKnockback;
import nautilus.game.arcade.kit.Kit;

public class ColorSwap extends SoloGame
{
	private long _time;
	private int _roundNum = 1;
	private boolean _roundGoing = true;
	private Material _currentColor;
	private NautHashMap<Material, String> _nameMap = new NautHashMap<>();
	public ColorSwap(ArcadeManager manager)
	{
		super(manager, GameType.ColorSwap,
				new Kit[]
						{
								new KitKnockback(manager)

						},

				new String[]
						{
								"Stand on the color shown in chat within 3 seconds.",
								"Players who are not on that color will die!",
								"Last player alive is the winner!"
						});
		this.HealthSet = 20;
		this.HungerSet = 20;
		this.PrepareFreeze = false;
		this.PrepareTime = 6000;
		this.DeathOut = true;
		this.DamageTeamSelf = false;
		this.Damage = false;
		this.DamagePvP = false;
		this.DamageTeamOther = false;

	}


	@EventHandler
	public void timerUpdate(UpdateEvent event){
		if (!IsLive())
			return;




		if (_roundGoing)
		{
			UtilTextBottom.displayProgress(C.cRed + C.Bold + "Time Remaining", getTimePercent(), GetTimeString(), UtilServer.getPlayers());
			return;
		}
	}


	private double getTimePercent(){
		return (3 - (double)(System.currentTimeMillis()-_time)/1000d)/3;
	}

	public String GetTimeString()
	{


		return UtilTime.MakeStr((long)(3*1000 - (double)(System.currentTimeMillis()-_time)));
	}


	@EventHandler
	public void gameBegin(GameStateChangeEvent event){
		if(!IsLive()){
			return;
		}
		_nameMap.put(Material.DIAMOND_BLOCK, "Diamond Block");
		_nameMap.put(Material.REDSTONE_BLOCK, "Redstone Block");
		_nameMap.put(Material.GOLD_BLOCK, "Gold Block");
		_nameMap.put(Material.EMERALD_BLOCK, "Emerald Block");
		_nameMap.put(Material.IRON_BLOCK, "Iron Block");
		_nameMap.put(Material.LAPIS_BLOCK, "Lapis Block");
		for (Player soundPlayer : UtilServer.getPlayers()){
			soundPlayer.playEffect(this.GetSpectatorLocation(), Effect.RECORD_PLAY, 2259);
		}
		_time = System.currentTimeMillis();
		setCurrentColor();

	}

	@EventHandler
	public void damageHandler(CustomDamageEvent event){
		if(!IsLive()){
			return;
		}
		if(_roundNum > 5){
			event.AddKnockback("Player Knockback", 1);
		}
		else
		{
			event.setCancelled(true);
		}
	}


	private void checkPlayers(){
		if(!IsLive()){
			return;
		}
		_roundGoing = false;
		/*
		for (Player soundPlayer : UtilServer.getPlayers()){
			soundPlayer.playSound(soundPlayer.getLocation(), Sound.AMBIENCE_THUNDER, 2f, 2f);
		}

		 */
		for (Player eventPlayer : getArcadeManager().GetGame().GetPlayers(true)){

			Material playerBlock = eventPlayer.getLocation().subtract(0, 1, 0).getBlock().getType();

			if(playerBlock == Material.AIR){
				playerBlock = eventPlayer.getLocation().subtract(0, 2, 0).getBlock().getType();
			}

			if(playerBlock != _currentColor){
				eventPlayer.getWorld().strikeLightning(eventPlayer.getLocation());
				eventPlayer.damage(21);
				UtilPlayer.message(eventPlayer, F.main("Death", "You were not standing on the correct block!"));
			}

		}

		UtilServer.getServer().getScheduler().runTaskLater(Manager.getPlugin(), new Runnable()
		{
			public void run()
			{
				_roundNum = _roundNum + 1;
				setCurrentColor();
				_time = System.currentTimeMillis();
				_roundGoing = true;

			}}, 60);

	}


	private void setCurrentColor(){
		if(!IsLive()){
			return;
		}
		if(_roundNum == 6){
			UtilTextMiddle.display("",C.cGreen + C.Bold + "You can now knockback other players!");
			getArcadeManager().GetGame().Damage = true;
			getArcadeManager().GetGame().DamagePvP = true;
			getArcadeManager().GetGame().DamageTeamSelf = true;
			getArcadeManager().GetGame().DamageTeamOther = true;
		}
		Material[] colors = {Material.DIAMOND_BLOCK, Material.REDSTONE_BLOCK, Material.GOLD_BLOCK, Material.EMERALD_BLOCK, Material.IRON_BLOCK, Material.LAPIS_BLOCK};
		double colorNum = (Math.random() * ((5) + 1)) + 0;
		_currentColor = colors[((int) colorNum)];
		ItemStack _stack = ItemStackFactory.Instance.CreateStack(_currentColor);
		for (Player soundPlayer : UtilServer.getPlayers()){
			soundPlayer.getInventory().clear();
			soundPlayer.getInventory().setItemInHand(_stack);
			soundPlayer.playSound(soundPlayer.getLocation(), Sound.NOTE_PLING, 2f, 2f);
		}

		UtilServer.broadcast(F.main("Game", C.cGreen + C.Bold + "The next block is " + _nameMap.get(_currentColor)));
		UtilServer.getServer().getScheduler().runTaskLater(Manager.getPlugin(), new Runnable()
		{
			public void run()
			{
				checkPlayers();
			}}, 60);
	}

}
