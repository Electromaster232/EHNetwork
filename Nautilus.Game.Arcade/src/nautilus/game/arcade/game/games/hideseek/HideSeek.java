package nautilus.game.arcade.game.games.hideseek;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftCreature;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.FallingSand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.util.Vector;

import mineplex.core.common.util.C;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilBlock;
import mineplex.core.common.util.UtilEvent;
import mineplex.core.common.util.UtilEvent.ActionType;
import mineplex.core.common.util.UtilInv;
import mineplex.core.common.util.UtilParticle.ParticleType;
import mineplex.core.common.util.UtilParticle.ViewDist;
import mineplex.core.common.util.UtilAlg;
import mineplex.core.common.util.UtilEnt;
import mineplex.core.common.util.UtilFirework;
import mineplex.core.common.util.UtilGear;
import mineplex.core.common.util.UtilMath;
import mineplex.core.common.util.UtilParticle;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilServer;
import mineplex.core.common.util.UtilTime;
import mineplex.core.itemstack.ItemStackFactory;
import mineplex.core.packethandler.IPacketHandler;
import mineplex.core.packethandler.PacketInfo;
import mineplex.core.recharge.Recharge;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.core.visibility.VisibilityManager;
import mineplex.minecraft.game.core.combat.DeathMessageType;
import mineplex.minecraft.game.core.damage.CustomDamageEvent;
import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.GameType;
import nautilus.game.arcade.events.GameStateChangeEvent;
import nautilus.game.arcade.events.PlayerPrepareTeleportEvent;
import nautilus.game.arcade.game.GameTeam;
import nautilus.game.arcade.game.TeamGame;
import nautilus.game.arcade.game.GameTeam.PlayerState;
import nautilus.game.arcade.game.games.hideseek.forms.BlockForm;
import nautilus.game.arcade.game.games.hideseek.forms.CreatureForm;
import nautilus.game.arcade.game.games.hideseek.forms.Form;
import nautilus.game.arcade.game.games.hideseek.forms.InfestedData;
import nautilus.game.arcade.game.games.hideseek.kits.*;
import nautilus.game.arcade.kit.Kit;
import nautilus.game.arcade.kit.NullKit;
import nautilus.game.arcade.stats.BadHiderStatTracker;
import nautilus.game.arcade.stats.HunterKillerStatTracker;
import nautilus.game.arcade.stats.HunterOfTheYearStatTracker;
import nautilus.game.arcade.stats.MeowStatTracker;
import net.minecraft.server.v1_7_R4.EntityCreature;
import net.minecraft.server.v1_7_R4.Navigation;
import net.minecraft.server.v1_7_R4.PacketPlayOutSpawnEntity;

@SuppressWarnings("deprecation")
public class HideSeek extends TeamGame
{
    public static class MeowEvent extends PlayerEvent
    {
        private static final HandlerList handlers = new HandlerList();

        public static HandlerList getHandlerList()
        {
            return handlers;
        }

        @Override
        public HandlerList getHandlers()
        {
            return getHandlerList();
        }

        public MeowEvent(Player who)
        {
            super(who);
        }
    }

    public static class PlayerChangeFormEvent extends PlayerEvent
    {
        private static final HandlerList handlers = new HandlerList();

        public static HandlerList getHandlerList()
        {
            return handlers;
        }

        @Override
        public HandlerList getHandlers()
        {
            return getHandlerList();
        }

        private final Form _form;

        public PlayerChangeFormEvent(Player who, Form form)
        {
            super(who);

            _form = form;
        }

        public Form getForm()
        {
            return _form;
        }
    }

    public static class PlayerSolidifyEvent extends PlayerEvent
    {
        private static final HandlerList handlers = new HandlerList();

        public static HandlerList getHandlerList()
        {
            return handlers;
        }

        @Override
        public HandlerList getHandlers()
        {
            return getHandlerList();
        }

        public PlayerSolidifyEvent(Player who)
        {
            super(who);
        }
    }

    private GameTeam _hiders;
    private GameTeam _seekers;

    private long _hideTime = 20000;
    private long _gameTime = 360000;
    
    private boolean _bowGiven = false;
    private long _bowGiveTime = 0;
    
    private boolean _started = false;

    private HashMap<Player, Integer> _arrowHits = new HashMap<Player, Integer>();

    private HashMap<Player, Form> _forms = new HashMap<Player, Form>();
    private HashMap<Player, InfestedData> _infested = new HashMap<Player, InfestedData>();
    private HashSet<LivingEntity> _infestDeny = new HashSet<LivingEntity>();

    private HashMap<Creature, Location> _mobs = new HashMap<Creature, Location>();

    private ArrayList<Material> _allowedBlocks;
    private ArrayList<EntityType> _allowedEnts;
    private IPacketHandler _preventSpawnSent = new IPacketHandler()
    {

        @Override
        public void handle(PacketInfo packetInfo)
        {
            if (packetInfo.getPacket() instanceof PacketPlayOutSpawnEntity)
            {
                Form form = _forms.get(packetInfo.getPlayer());
                if (form != null && form instanceof BlockForm
                        && ((BlockForm) form).getEntityId() == ((PacketPlayOutSpawnEntity) packetInfo.getPacket()).a)
                {
                    packetInfo.setCancelled(true);
                }
            }
        }

    };

    public HideSeek(ArcadeManager manager)
    {
        super(manager, GameType.HideSeek,

        new Kit[]
            {
                    new KitHiderSwapper(manager), new KitHiderQuick(manager), new KitHiderShocker(manager),
                    new KitHiderTeleporter(manager), new NullKit(manager), new KitSeekerLeaper(manager),
                    new KitSeekerTNT(manager), new KitSeekerRadar(manager),
            },

        new String[]
            {
                    C.cAqua + "Hiders" + C.cWhite + " Run and Hide from Seekers",
                    C.cAqua + "Hiders" + C.cWhite + " Disguise as Blocks or Animals",
                    C.cAqua + "Hiders" + C.cWhite + " Shoot Seekers for Axe Upgrades",
                    C.cAqua + "Hiders" + C.cWhite + " Right-Click with Axe for Speed Boost",
                    C.cRed + "Seekers" + C.cWhite + " Find and kill the Hiders!",
            });

        this.DamageSelf = false;
        this.DeathOut = false;
        this.HungerSet = 20;
        this.PrepareFreeze = false;

        _allowedBlocks = new ArrayList<Material>();
        _allowedBlocks.add(Material.TNT);
        _allowedBlocks.add(Material.BOOKSHELF);
        _allowedBlocks.add(Material.WORKBENCH);
        _allowedBlocks.add(Material.FURNACE);
        _allowedBlocks.add(Material.MELON_BLOCK);
        _allowedBlocks.add(Material.CAULDRON);
        _allowedBlocks.add(Material.FLOWER_POT);
        _allowedBlocks.add(Material.ANVIL);
        _allowedBlocks.add(Material.HAY_BLOCK);
        _allowedBlocks.add(Material.CAKE_BLOCK);

        _allowedEnts = new ArrayList<EntityType>();
        _allowedEnts.add(EntityType.PIG);
        _allowedEnts.add(EntityType.COW);
        _allowedEnts.add(EntityType.CHICKEN);
        _allowedEnts.add(EntityType.SHEEP);

        Manager.GetExplosion().SetRegenerate(true);
        Manager.GetExplosion().SetTNTSpread(false);
        Manager.getCosmeticManager().setHideParticles(true);

        registerStatTrackers(new HunterKillerStatTracker(this), new MeowStatTracker(this), new HunterKillerStatTracker(this),
                new HunterOfTheYearStatTracker(this), new BadHiderStatTracker(this));
    }

    @EventHandler
    public void onGameEndStart(GameStateChangeEvent event)
    {
        if (event.GetState() == GameState.Prepare)
        {
            this.getArcadeManager().getPacketHandler().addPacketHandler(_preventSpawnSent);
        }
        else if (event.GetState() == GameState.End)
        {
            this.getArcadeManager().getPacketHandler().removePacketHandler(_preventSpawnSent);
        }
    }

    public Material GetItemEquivilent(Material mat)
    {
        if (mat == Material.CAULDRON)
            return Material.CAULDRON_ITEM;
        if (mat == Material.FLOWER_POT)
            return Material.FLOWER_POT_ITEM;
        if (mat == Material.CAKE_BLOCK)
            return Material.CAKE;

        return mat;
    }

    @Override
    public void ParseData()
    {
        int i = 0;

        for (ArrayList<Location> locs : WorldData.GetAllCustomLocs().values())
        {
            for (Location loc : locs)
            {
                if (Math.random() > 0.25)
                    continue;

                if (loc.getBlock().getRelative(BlockFace.UP).getType() != Material.AIR)
                    continue;

                loc.getBlock().setType(Material.AIR);
                i++;
            }
        }

        System.out.println("Removed " + i + " Random Blocks.");

        for (Location loc : WorldData.GetDataLocs("BLACK"))
            loc.getBlock().setType(Material.FENCE);

    }

    @EventHandler
    public void CustomTeamGeneration(GameStateChangeEvent event)
    {
        if (event.GetState() != GameState.Recruit)
            return;

        _hiders = GetTeamList().get(1);
        _hiders.SetColor(ChatColor.AQUA);
        _hiders.SetName("Hiders");

        _seekers = GetTeamList().get(0);
        _seekers.SetColor(ChatColor.RED);
        _seekers.SetName("Hunters");

        RestrictKits();
    }

    @Override
    public void RestrictKits()
    {
        for (Kit kit : GetKits())
        {
            for (GameTeam team : GetTeamList())
            {
                if (team.GetColor() == ChatColor.RED)
                {
                    if (kit.GetName().contains("Hider"))
                        team.GetRestrictedKits().add(kit);
                }
                else
                {
                    if (kit.GetName().contains("Hunter"))
                        team.GetRestrictedKits().add(kit);
                }
            }
        }
    }

    @EventHandler
    public void MoveKits(GameStateChangeEvent event)
    {
        if (event.GetState() != GameState.Prepare)
            return;

        for (int i = 0; i < WorldData.GetDataLocs("RED").size() && i < 3; i++)
        {
            if (GetKits().length <= 5 + i)
                continue;

            this.CreatureAllowOverride = true;
            Entity ent = GetKits()[5 + i].SpawnEntity(WorldData.GetDataLocs("RED").get(i));
            this.CreatureAllowOverride = false;

            Manager.GetLobby().AddKitLocation(ent, GetKits()[5 + i], WorldData.GetDataLocs("RED").get(i));
        }
    }

    public void GiveItems(boolean bowOnly)
    {
        for (Player player : _hiders.GetPlayers(true))
        {
        	if (bowOnly)
        	{
        		 // Bow
                ItemStack bow = ItemStackFactory.Instance.CreateStack(Material.BOW, (byte) 0, 1, C.cYellow + C.Bold + "Shoot Hunters"
                        + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Upgrades Axe");
                bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
                player.getInventory().setItem(1, bow);
                player.getInventory().setItem(28, ItemStackFactory.Instance.CreateStack(Material.ARROW));
        	}
        	else
        	{
        		 // Axe
                player.getInventory().setItem(0,
                        ItemStackFactory.Instance.CreateStack(Material.WOOD_AXE, (byte) 0, 1, C.cGreen + "Speed Axe"));

                // Meower
                player.getInventory().setItem(
                        4,
                        ItemStackFactory.Instance.CreateStack(Material.SUGAR, (byte) 0, 1, C.cYellow + C.Bold + "Meow" + C.cWhite
                                + C.Bold + " - " + C.cGreen + C.Bold + "+0.25 Gems"));

                // Firework
                ItemStack firework = ItemStackFactory.Instance.CreateStack(Material.FIREWORK, (byte) 0, 5, C.cYellow + C.Bold
                        + "Firework" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "+2 Gems");
                FireworkMeta metaData = (FireworkMeta) firework.getItemMeta();
                metaData.setPower(1);
                metaData.addEffect(FireworkEffect.builder().flicker(true).withColor(Color.AQUA).with(Type.BALL_LARGE).trail(true)
                        .build());
                firework.setItemMeta(metaData);
                player.getInventory().setItem(5, firework);

                // Recharges
                Recharge.Instance.useForce(player, "Meow", 15000);
                Recharge.Instance.useForce(player, "Firework", 15000);
        	}
            
            player.playSound(player.getLocation(), Sound.HORSE_ARMOR, 1f, 1f);
        }
    }

    @EventHandler
    public void InitialDisguise(PlayerPrepareTeleportEvent event)
    {
        if (_hiders.HasPlayer(event.GetPlayer().getName(), true))
        {
            if (GetKit(event.GetPlayer()) instanceof KitHider)
            {
                Form form = new BlockForm(this, event.GetPlayer(), _allowedBlocks.get(UtilMath.r(_allowedBlocks.size())));

                _forms.put(event.GetPlayer(), form);

                form.Apply();

                Bukkit.getPluginManager().callEvent(new PlayerChangeFormEvent(event.GetPlayer(), form));
            }
        }
    }

    @EventHandler
    public void ChangeDisguise(PlayerInteractEvent event)
    {
        if (event.getClickedBlock() == null)
            return;

        Player player = event.getPlayer();

        if (!UtilGear.isMat(player.getItemInHand(), Material.SLIME_BALL))
            return;

        if (!_allowedBlocks.contains(event.getClickedBlock().getType()))
        {
            UtilPlayer.message(
                    player,
                    F.main("Game",
                            "You cannot morph into "
                                    + F.elem(ItemStackFactory.Instance
                                            .GetName(event.getClickedBlock().getType(), (byte) 0, false) + " Block") + "."));
            return;
        }

        if (!Recharge.Instance.use(player, "Change Form", 6000, true, false))
            return;

        if (!(GetKit(player) instanceof KitHiderSwapper))
            UtilInv.remove(player, Material.SLIME_BALL, (byte) 0, 1);

        // Remove Old
        _forms.get(player).Remove();

        Form form = new BlockForm(this, player, event.getClickedBlock().getType());

        // Set New
        _forms.put(player, form);

        form.Apply();

        Bukkit.getPluginManager().callEvent(new PlayerChangeFormEvent(player, form));
    }

    @EventHandler
    public void ChangeDisguise(PlayerInteractEntityEvent event)
    {
        if (event.getRightClicked() == null)
            return;

        Player player = event.getPlayer();

        if (!UtilGear.isMat(player.getItemInHand(), Material.SLIME_BALL))
            return;

        if (!_allowedEnts.contains(event.getRightClicked().getType()))
        {
            UtilPlayer.message(player,
                    F.main("Game", "You cannot morph into " + F.elem(UtilEnt.getName(event.getRightClicked())) + "."));
            return;
        }

        if (!Recharge.Instance.use(player, "Change Form", 6000, true, false))
            return;

        if (!(GetKit(player) instanceof KitHiderSwapper))
            UtilInv.remove(player, Material.SLIME_BALL, (byte) 0, 1);

        // Remove Old
        _forms.get(player).Remove();

        Form form = new CreatureForm(this, player, event.getRightClicked().getType());

        // Set New
        _forms.put(player, form);

        Bukkit.getPluginManager().callEvent(new PlayerChangeFormEvent(player, form));
    }

    @EventHandler
    public void ChangeDisguise(CustomDamageEvent event)
    {
        Player player = event.GetDamagerPlayer(false);
        if (player == null)
            return;

        if (!UtilGear.isMat(player.getItemInHand(), Material.SLIME_BALL))
            return;

        if (!_allowedEnts.contains(event.GetDamageeEntity().getType()))
        {
            UtilPlayer.message(player,
                    F.main("Game", "You cannot morph into " + F.elem(UtilEnt.getName(event.GetDamageeEntity())) + "."));
            return;
        }

        if (!Recharge.Instance.use(player, "Change Form", 6000, true, false))
            return;

        if (!(GetKit(player) instanceof KitHiderSwapper))
            UtilInv.remove(player, Material.SLIME_BALL, (byte) 0, 1);

        // Remove Old
        _forms.get(player).Remove();

        Form form = new CreatureForm(this, player, event.GetDamageeEntity().getType());

        // Set New
        _forms.put(player, form);

        Bukkit.getPluginManager().callEvent(new PlayerChangeFormEvent(player, form));
    }

    @EventHandler
    public void FallingBlockBreak(ItemSpawnEvent event)
    {
        if (event.getEntity().getItemStack().getType() == Material.getMaterial(175))
            return;

        event.setCancelled(true);

        for (Form form : _forms.values())
            if (form instanceof BlockForm)
                ((BlockForm) form).FallingBlockCheck();
    }

    @EventHandler
    public void FallingBlockLand(EntityChangeBlockEvent event)
    {
        if (event.getEntity() instanceof FallingBlock)
        {
            event.setCancelled(true);
            event.getEntity().remove();

            for (Form form : _forms.values())
                if (form instanceof BlockForm)
                    ((BlockForm) form).FallingBlockCheck();
        }
    }

    @EventHandler
    public void FallingBlockUpdate(UpdateEvent event)
    {
        if (event.getType() != UpdateType.TICK)
            return;

        if (!InProgress())
            return;

        for (Form form : _forms.values())
            if (form instanceof BlockForm)
                ((BlockForm) form).FallingBlockCheck();
    }

    @EventHandler
    public void SolidifyUpdate(UpdateEvent event)
    {
        if (!IsLive())
            return;

        if (event.getType() != UpdateType.TICK)
            return;

        for (Form form : _forms.values())
            if (form instanceof BlockForm)
                ((BlockForm) form).SolidifyUpdate();
    }

    @EventHandler
    public void SolidBlockDamage(BlockDamageEvent event)
    {
        if (!_seekers.HasPlayer(event.getPlayer()))
            return;

        for (Form form : _forms.values())
        {
            if (!(form instanceof BlockForm))
                continue;

            if (((BlockForm) form).GetBlock() == null)
                continue;

            if (!((BlockForm) form).GetBlock().equals(event.getBlock()))
                continue;

            // Damage Event
            Manager.GetDamage().NewDamageEvent(form.Player, event.getPlayer(), null, DamageCause.CUSTOM, 4, true, true, false,
                    event.getPlayer().getName(), null);

            ((BlockForm) form).SolidifyRemove();
        }
    }

    @EventHandler
    public void FallingBlockDamage(EntityDamageEvent event)
    {
        if (!(event instanceof EntityDamageByEntityEvent))
            return;

        if (!(event.getEntity() instanceof FallingSand))
            return;

        if (event.getEntity().getVehicle() == null)
            return;

        if (!(event.getEntity().getVehicle() instanceof LivingEntity))
            return;

        LivingEntity damagee = (LivingEntity) event.getEntity().getVehicle();

        EntityDamageByEntityEvent eventEE = (EntityDamageByEntityEvent) event;

        LivingEntity damager = null;
        Projectile proj = null;

        if (eventEE.getDamager() instanceof Projectile)
        {
            proj = (Projectile) eventEE.getDamager();
            damager = (LivingEntity) proj.getShooter();
        }
        else if (eventEE.getDamager() instanceof LivingEntity)
        {
            damager = (LivingEntity) eventEE.getDamager();
        }

        // Damage Event
        Manager.GetDamage().NewDamageEvent(damagee, damager, proj, event.getCause(), event.getDamage(), true, false, false, null,
                null);

        event.setCancelled(true);
    }

    @EventHandler
    public void AnimalSpawn(GameStateChangeEvent event)
    {
        if (event.GetState() != GameState.Prepare)
            return;

        this.CreatureAllowOverride = true;

        for (Location loc : WorldData.GetDataLocs("WHITE"))
            _mobs.put(loc.getWorld().spawn(loc, Sheep.class), loc);

        for (Location loc : WorldData.GetDataLocs("PINK"))
            _mobs.put(loc.getWorld().spawn(loc, Pig.class), loc);

        for (Location loc : WorldData.GetDataLocs("YELLOW"))
            _mobs.put(loc.getWorld().spawn(loc, Chicken.class), loc);

        for (Location loc : WorldData.GetDataLocs("BROWN"))
            _mobs.put(loc.getWorld().spawn(loc, Cow.class), loc);

        this.CreatureAllowOverride = false;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void AnimalDamage(CustomDamageEvent event)
    {
        if (event.GetDamageePlayer() == null && !(event.GetDamageeEntity() instanceof Slime))
            event.SetCancelled("Animal Damage");

        if (event.GetDamagerEntity(false) != null && event.GetDamagerEntity(false) instanceof Slime)
            event.SetCancelled("Slime Attack");
    }

    @EventHandler
    public void AnimalReturn(UpdateEvent event)
    {
        if (event.getType() != UpdateType.SEC)
            return;

        for (Creature ent : _mobs.keySet())
        {
            if (UtilMath.offset(ent.getLocation(), _mobs.get(ent)) < 5)
                continue;

            Location loc = _mobs.get(ent).add(
                    UtilAlg.getTrajectory(_mobs.get(ent), ent.getLocation()).multiply(Math.random() * 3));

            EntityCreature ec = ((CraftCreature) ent).getHandle();
            Navigation nav = ec.getNavigation();
            nav.a(loc.getX(), loc.getY(), loc.getZ(), 1f);
        }
    }

    @EventHandler
    public void AttackSeeker(CustomDamageEvent event)
    {
        if (event.GetDamagerPlayer(true) == null)
            return;

        if (!_hiders.HasPlayer(event.GetDamagerPlayer(true)))
            return;

        if (event.GetDamageInitial() > 1)
            return;

        event.AddMod("H&S", "Negate", -event.GetDamageInitial(), false);
        event.AddMod("H&S", "Attack", event.GetDamageInitial(), true);
    }

    @EventHandler
    public void ArrowShoot(EntityShootBowEvent event)
    {
        if (!(event.getEntity() instanceof Player))
            return;

        Player shooter = (Player) event.getEntity();

        if (!_hiders.HasPlayer(shooter))
            return;

        Arrow arrow = shooter.getWorld().spawnArrow(
                shooter.getEyeLocation().add(shooter.getLocation().getDirection().multiply(1.5)),
                shooter.getLocation().getDirection(), (float) event.getProjectile().getVelocity().length(), 0f);
        arrow.setShooter(shooter);

        event.setCancelled(true);
    }

    @EventHandler
    public void ArrowHit(CustomDamageEvent event)
    {
        if (event.IsCancelled())
            return;

        if (event.GetProjectile() == null)
            return;

        Player damagee = event.GetDamageePlayer();
        if (damagee == null)
            return;

        if (!_seekers.HasPlayer(damagee))
            return;

        Player damager = event.GetDamagerPlayer(true);
        if (damager == null)
            return;

        if (!_hiders.HasPlayer(damager))
            return;

        event.AddMod("Hide & Seek", "Negate", -event.GetDamageInitial(), false);
        event.AddMod("Hide & Seek", "Damage Set", 2, false);
        event.AddKnockback("Hide & Seek", 2);

        Powerup(damager);
    }

    public void Powerup(Player player)
    {
        int count = 1;
        if (_arrowHits.containsKey(player))
            count += _arrowHits.get(player);

        _arrowHits.put(player, count);

        if (count == 4)
        {
            player.getInventory().remove(Material.WOOD_AXE);
            player.getInventory().addItem(
                    ItemStackFactory.Instance.CreateStack(Material.STONE_AXE, (byte) 0, 1, C.cGreen + "Super Axe"));

            // Sound
            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1f, 1f);

            // Inform
            UtilPlayer.message(player, F.main("Game", "You upgraded to " + F.elem("Super Axe") + "!"));
        }
        else if (count == 8)
        {
            player.getInventory().remove(Material.STONE_AXE);
            player.getInventory().addItem(
                    ItemStackFactory.Instance.CreateStack(Material.IRON_AXE, (byte) 0, 1, C.cGreen + "Ultra Axe"));

            // Sound
            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1f, 1f);

            // Inform
            UtilPlayer.message(player, F.main("Game", "You upgraded to " + F.elem("Ultra Axe") + "!"));
        }
        else if (count == 12)
        {
            player.getInventory().remove(Material.IRON_AXE);
            player.getInventory().addItem(
                    ItemStackFactory.Instance.CreateStack(Material.DIAMOND_AXE, (byte) 0, 1, C.cGreen + "Hyper Axe"));

            // Sound
            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1f, 1f);

            // Inform
            UtilPlayer.message(player, F.main("Game", "You upgraded to " + F.elem("Hyper Axe") + "!"));
        }
        else if (count < 12)
        {
            // Sound
            player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1f, 1f);
        }
    }

    @EventHandler
    public void UseBoost(PlayerInteractEvent event)
    {
        Player player = event.getPlayer();

        if (!UtilEvent.isAction(event, ActionType.R))
            return;

        if (player.getItemInHand() == null)
            return;

        if (!player.getItemInHand().getType().toString().contains("_AXE"))
            return;

        if (!_hiders.HasPlayer(player))
            return;

        if (!Recharge.Instance.use(player, "Axe Boost", 16000, true, true))
            return;

        if (UtilGear.isMat(player.getItemInHand(), Material.WOOD_AXE))
        {
            Manager.GetCondition().Factory().Speed("Boost", player, player, 4, 0, false, false, false);

            // Inform
            UtilPlayer.message(player, F.main("Game", "You used " + F.elem("Basic Boost") + "!"));

            // Sound
            player.getWorld().playSound(player.getLocation(), Sound.BLAZE_BREATH, 1f, 1f);
        }
        else if (UtilGear.isMat(player.getItemInHand(), Material.STONE_AXE))
        {
            Manager.GetCondition().Factory().Speed("Boost", player, player, 4, 1, false, false, false);

            // Inform
            UtilPlayer.message(player, F.main("Game", "You used " + F.elem("Ultra Boost") + "!"));

            // Sound
            player.getWorld().playSound(player.getLocation(), Sound.BLAZE_BREATH, 1f, 1f);
        }
        else if (UtilGear.isMat(player.getItemInHand(), Material.IRON_AXE))
        {
            Manager.GetCondition().Factory().Speed("Boost", player, player, 4, 1, false, false, false);
            Manager.GetCondition().Factory().Regen("Boost", player, player, 4, 0, false, false, false);

            // Inform
            UtilPlayer.message(player, F.main("Game", "You used " + F.elem("Mega Boost") + "!"));

            // Sound
            player.getWorld().playSound(player.getLocation(), Sound.BLAZE_BREATH, 1f, 1f);
        }
        else if (UtilGear.isMat(player.getItemInHand(), Material.DIAMOND_AXE))
        {
            Manager.GetCondition().Factory().Speed("Boost", player, player, 4, 2, false, false, false);
            Manager.GetCondition().Factory().Regen("Boost", player, player, 4, 1, false, false, false);

            // Inform
            UtilPlayer.message(player, F.main("Game", "You used " + F.elem("Hyper Boost") + "!"));

            // Sound
            player.getWorld().playSound(player.getLocation(), Sound.BLAZE_BREATH, 1f, 1f);
        }
    }

    @EventHandler
    public void UseMeow(PlayerInteractEvent event)
    {
        Player player = event.getPlayer();

        if (!UtilEvent.isAction(event, ActionType.R))
            return;

        if (!UtilGear.isMat(player.getItemInHand(), Material.SUGAR))
            return;

        event.setCancelled(true);

        if (!Recharge.Instance.use(player, "Meow", 5000, true, true))
            return;

        player.getWorld().playSound(player.getLocation(), Sound.CAT_MEOW, 1f, 1f);

        this.AddGems(player, 0.25, "Meows", true, true);

        UtilParticle.PlayParticle(ParticleType.NOTE, player.getLocation().add(0, 0.75, 0), 0.4f, 0.4f, 0.4f, 0, 6,
				ViewDist.MAX, UtilServer.getPlayers());

        Bukkit.getPluginManager().callEvent(new MeowEvent(event.getPlayer()));
    }

    @EventHandler
    public void UseFirework(PlayerInteractEvent event)
    {
        Player player = event.getPlayer();

        if (!UtilEvent.isAction(event, ActionType.R))
            return;

        if (!UtilGear.isMat(player.getItemInHand(), Material.FIREWORK))
            return;

        event.setCancelled(true);
        
        if (!Recharge.Instance.use(player, "Firework", 15000, true, true))
            return;
        
        this.AddGems(player, 2, "Fireworks", true, true);
        
        UtilInv.remove(player, Material.FIREWORK, (byte)0, 1);
        UtilInv.Update(player);
        
        UtilFirework.launchFirework(player.getEyeLocation(), 
				FireworkEffect.builder().flicker(Math.random() > 0.5).withColor(Color.YELLOW).with(Type.BALL_LARGE).trail(true).flicker(true).build(), 
				new Vector(0,0,0), 2);
    }

    @EventHandler
    public void HiderTimeGems(UpdateEvent event)
    {
        if (GetState() != GameState.Live)
            return;

        if (event.getType() != UpdateType.SEC)
            return;

        for (Player player : _hiders.GetPlayers(true))
        {
            this.AddGems(player, 0.05, "Seconds Alive", true, true);
        }
    }

    @EventHandler
    public void UpdateSeekers(UpdateEvent event)
    {
        if (!IsLive())
            return;

        if (event.getType() != UpdateType.FAST)
            return;

        int req = Math.max(1, GetPlayers(true).size() / 5);

        while (_seekers.GetPlayers(true).size() < req && _hiders.GetPlayers(true).size() > 0)
        {
            Player player = _hiders.GetPlayers(true).get(UtilMath.r(_hiders.GetPlayers(true).size()));
            SetSeeker(player, true);
        }
    }

    @EventHandler
    public void WaterDamage(UpdateEvent event)
    {
        if (!IsLive())
            return;

        if (event.getType() != UpdateType.FAST)
            return;
    }

    @EventHandler
    public void WorldWaterDamage(UpdateEvent event)
    {
        if (!IsLive())
            return;

        if (event.getType() != UpdateType.SEC)
            return;

        for (Player player : _hiders.GetPlayers(true))
            if (player.getLocation().getBlock().getTypeId() == 8 || player.getLocation().getBlock().getTypeId() == 9)
            {
                // Damage Event
                Manager.GetDamage().NewDamageEvent(player, null, null, DamageCause.DROWNING, 2, false, false, false, "Water",
                        "Water Damage");

                player.getWorld().playSound(player.getLocation(), Sound.SPLASH, 0.8f, 1f + (float) Math.random() / 2);
            }
    }

    @EventHandler
    public void PlayerDeath(PlayerQuitEvent event)
    {
        Form form = _forms.remove(event.getPlayer());
        if (form != null)
            form.Remove();
    }

    @EventHandler
    public void PlayerDeath(PlayerDeathEvent event)
    {
        if (_hiders.HasPlayer(event.getEntity()))
            SetSeeker(event.getEntity(), false);
    }

    public void SetSeeker(Player player, boolean forced)
    {
        GameTeam pastTeam = GetTeam(player);
        if (pastTeam != null && pastTeam.equals(_hiders))
            pastTeam.SetPlacement(player, PlayerState.OUT);

        SetPlayerTeam(player, _seekers, true);

        Manager.GetDisguise().undisguise(player);

        // Remove Form
        Form form = _forms.remove(player);
        if (form != null)
            form.Remove();

        // Kit
        SetKit(player, GetKits()[5], false);
        GetKits()[5].ApplyKit(player);

        // Refresh
        VisibilityManager.Instance.refreshPlayerToAll(player);
        
        if (forced)
        {
            AddGems(player, 10, "Forced Seeker", false, false);

            Announce(F.main("Game",
                    F.elem(_hiders.GetColor() + player.getName()) + " was moved to " + F.elem(C.cRed + C.Bold + "Hunters") + "."));

            player.getWorld().strikeLightningEffect(player.getLocation());

            player.damage(1000);
        }

        UtilPlayer.message(player, C.cRed + C.Bold + "You are now a Hunter!");

        player.eject();
        player.leaveVehicle();
        player.teleport(_seekers.GetSpawn());
    }

    @Override
    public void EndCheck()
    {
        if (!IsLive())
            return;

        if (GetPlayers(true).isEmpty())
        {
            SetState(GameState.End);
            return;
        }

        if (_hiders.GetPlayers(true).isEmpty())
        {
            ArrayList<Player> places = _hiders.GetPlacements(true);

            AnnounceEnd(_hiders.GetPlacements(true));

            // Gems
            if (places.size() >= 1)
                AddGems(places.get(0), 20, "1st Place", false, false);

            if (places.size() >= 2)
                AddGems(places.get(1), 15, "2nd Place", false, false);

            if (places.size() >= 3)
                AddGems(places.get(2), 10, "3rd Place", false, false);

            for (Player player : GetPlayers(false))
                if (player.isOnline())
                    AddGems(player, 10, "Participation", false, false);

            SetState(GameState.End);
        }
    }

    @Override
    public double GetKillsGems(Player killer, Player killed, boolean assist)
    {
        if (_hiders.HasPlayer(killed))
        {
            if (!assist)
                return 4;
            else
                return 1;
        }

        if (!assist)
            return 1;

        return 0;
    }

    @EventHandler
    public void AnnounceHideTime(GameStateChangeEvent event)
    {
        if (event.GetState() != GameState.Live)
            return;

        Announce(C.cAqua + C.Bold + "Hiders have 20 Seconds to hide!");
    }

    @EventHandler
    public void Timer(UpdateEvent event)
    {
        if (GetState() != GameState.Live)
            return;

        if (event.getType() != UpdateType.TICK)
            return;

      
        
        // Hide Time
        if (!_started)
        {
            long timeLeft = _hideTime - (System.currentTimeMillis() - GetStateTime());

            if (timeLeft <= 0)
            {
                _started = true;

                // Hider Items
                GiveItems(false);
                _bowGiveTime = System.currentTimeMillis();

                // Remove Barrier
                for (Location loc : WorldData.GetDataLocs("BLACK"))
                    loc.getBlock().setType(Material.AIR);

                Announce(C.cRed + C.Bold + "The Hunters have been released!");
            }
        }
        // Seek Time
        else
        {
        	//Give Bow
            if (!_bowGiven && UtilTime.elapsed(_bowGiveTime, 10000))
            {
            	 GiveItems(true);
            	 _bowGiven = true;
            }
        	
            long timeLeft = _gameTime - (System.currentTimeMillis() - GetStateTime() - _hideTime);

            if (timeLeft <= 0)
            {
                WriteScoreboard();

                AnnounceEnd(_hiders);

                for (Player player : _hiders.GetPlayers(true))
                    AddGems(player, 10, "Winning Team", false, false);

                for (Player player : GetPlayers(false))
                    if (player.isOnline())
                        AddGems(player, 10, "Participation", false, false);

                SetState(GameState.End);
            }
        }
    }

    @Override
    @EventHandler
    public void ScoreboardUpdate(UpdateEvent event)
    {
        if (event.getType() != UpdateType.FAST)
            return;

        WriteScoreboard();
    }

    private void WriteScoreboard()
    {
        // Wipe Last
        Scoreboard.Reset();

        for (GameTeam team : this.GetTeamList())
        {
            Scoreboard.WriteBlank();
            Scoreboard.Write(team.GetPlayers(true).size() + " " + team.GetColor() + team.GetName());
        }

        if (!_started)
        {
            long timeLeft = _hideTime - (System.currentTimeMillis() - GetStateTime());

            Scoreboard.WriteBlank();
            Scoreboard.Write(C.cYellow + C.Bold + "Hide Time");
            Scoreboard.Write(UtilTime.MakeStr(Math.max(0, timeLeft), 0));
        }
        else
        {
            long timeLeft = _gameTime - (System.currentTimeMillis() - GetStateTime() - _hideTime);

            Scoreboard.WriteBlank();
            Scoreboard.Write(C.cYellow + C.Bold + "Seek Time");
            Scoreboard.Write(UtilTime.MakeStr(Math.max(0, timeLeft), 0));
        }

        Scoreboard.Draw();
    }

    @Override
    public GameTeam ChooseTeam(Player player)
    {
        if (CanJoinTeam(_seekers))
            return _seekers;

        return _hiders;
    }

    @Override
    public boolean CanJoinTeam(GameTeam team)
    {
        if (team.GetColor() == ChatColor.RED)
        {
            return team.GetSize() < Math.max(1, GetPlayers(true).size() / 5);
        }

        return true;
    }

    @Override
    public boolean CanThrowTNT(Location location)
    {
        for (Location loc : _seekers.GetSpawns())
            if (UtilMath.offset(loc, location) < 24)
                return false;

        return true;
    }

    @Override
    public DeathMessageType GetDeathMessageType()
    {
        return DeathMessageType.Detailed;
    }

    @EventHandler
    public void UsableCancel(PlayerInteractEvent event)
    {
        if (event.getClickedBlock() == null)
            return;

        if (UtilBlock.usable(event.getClickedBlock()))
            event.setCancelled(true);
    }

    public GameTeam getHiders()
    {
        return _hiders;
    }

    public GameTeam getSeekers()
    {
        return _seekers;
    }

    @EventHandler
    public void InfestDisguise(PlayerInteractEvent event)
    {
        if (event.getClickedBlock() == null)
            return;

        final Player player = event.getPlayer();

        final Block block = event.getClickedBlock();

        if (!UtilGear.isMat(player.getItemInHand(), Material.MAGMA_CREAM))
            return;

        if (!_allowedBlocks.contains(block.getType()))
        {
            UtilPlayer.message(
                    player,
                    F.main("Game",
                            "You cannot infest "
                                    + F.elem(ItemStackFactory.Instance.GetName(block.getType(), (byte) 0, false) + " Block")
                                    + "."));
            return;
        }

        if (block.getRelative(BlockFace.UP).getType() != Material.AIR
                || _infested.values().contains(block.getRelative(BlockFace.UP)))
        {
            UtilPlayer.message(player, F.main("Game", "You can only infest blocks with air above them."));
            return;
        }

        if (IsInfesting(player) || _infestDeny.contains(player))
        {
            UtilPlayer.message(player, F.main("Game", "You are already infesting something."));
            return;
        }

        if (!Recharge.Instance.use(player, "Infest", 8000, true, true))
            return;

        // Cloak
        InfestStart(player);

        // Data
        final InfestedData data = new InfestedData(block);

        // Teleport
        player.teleport(block.getLocation().add(0.5, 0, 0.5));

        // Fake Block
        UtilServer.getServer().getScheduler().runTaskLater(Manager.getPlugin(), new Runnable()
        {
            @Override
            public void run()
            {
                if (block.getType() == Material.AIR)
                {
                    player.sendBlockChange(block.getLocation(), 36, (byte) 0);
                    for (Player other : UtilServer.getPlayers())
                        if (!other.equals(player))
                            other.sendBlockChange(block.getLocation(), data.Material, data.Data);
                }
            }

        }, 5);

        _infested.put(player, data);
    }

    @EventHandler
    public void InfestDisguise(PlayerInteractEntityEvent event)
    {
        if (event.getRightClicked() == null)
            return;

        Player player = event.getPlayer();

        if (!UtilGear.isMat(player.getItemInHand(), Material.MAGMA_CREAM))
            return;

        if (!_allowedEnts.contains(event.getRightClicked().getType()))
        {
            UtilPlayer.message(player,
                    F.main("Game", "You cannot infest " + F.elem(UtilEnt.getName(event.getRightClicked())) + "."));
            return;
        }

        if (IsInfesting(player) || _infestDeny.contains(player))
        {
            UtilPlayer.message(player, F.main("Game", "You are already infesting something."));
            return;
        }

        if (!Recharge.Instance.use(player, "Infest", 8000, true, true))
            return;

        // Cloak
        InfestStart(player);

        // Infest
        event.getRightClicked().setPassenger(player);
    }

    @EventHandler
    public void InfestDisguise(CustomDamageEvent event)
    {
        Player player = event.GetDamagerPlayer(false);
        if (player == null)
            return;

        if (!UtilGear.isMat(player.getItemInHand(), Material.MAGMA_CREAM))
            return;

        if (!_allowedEnts.contains(event.GetDamageeEntity().getType()))
        {
            UtilPlayer.message(player,
                    F.main("Game", "You cannot morph into " + F.elem(UtilEnt.getName(event.GetDamageeEntity())) + "."));
            return;
        }

        if (IsInfesting(player) || _infestDeny.contains(player))
        {
            UtilPlayer.message(player, F.main("Game", "You are already infesting something."));
            return;
        }

        if (!Recharge.Instance.use(player, "Infest", 8000, true, true))
            return;

        // Cloak
        InfestStart(player);

        // Infest
        event.GetDamageeEntity().setPassenger(player);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void InfectDamageShuffleUp(CustomDamageEvent event)
    {
        if (event.GetDamageePlayer() == null)
        {
            if (event.GetDamageeEntity().getPassenger() != null
                    && event.GetDamageeEntity().getPassenger() instanceof LivingEntity)
            {
                LivingEntity passenger = (LivingEntity) event.GetDamageeEntity().getPassenger();

                // Leave
                event.GetDamageeEntity().eject();
                passenger.leaveVehicle();

                // End
                InfestEnd(passenger);

                // Damage Event
                Manager.GetDamage().NewDamageEvent(passenger, event.GetDamagerEntity(true), null, DamageCause.CUSTOM, 4, true,
                        true, false, UtilEnt.getName(event.GetDamagerEntity(true)), null);
            }
        }
    }

    @EventHandler
    public void InfestBlockDamage(BlockDamageEvent event)
    {
        if (!_seekers.HasPlayer(event.getPlayer()))
            return;

        for (Player player : _infested.keySet())
        {
            if (!_infested.get(player).Block.equals(event.getBlock()))
                continue;

            // Damage Event
            Manager.GetDamage().NewDamageEvent(player, event.getPlayer(), null, DamageCause.CUSTOM, 4, true, true, false,
                    event.getPlayer().getName(), null);

            // Remove
            _infested.remove(player).restore();

            // End
            InfestEnd(player);
        }
    }

    @EventHandler
    public void InfestLeave(VehicleExitEvent event)
    {
        InfestEnd(event.getExited());
    }

    @EventHandler
    public void InfestLeave(UpdateEvent event)
    {
        if (event.getType() != UpdateType.TICK)
            return;

        // Block Leave
        Iterator<Player> infestIterator = _infested.keySet().iterator();
        while (infestIterator.hasNext())
        {
            Player player = infestIterator.next();

            InfestedData data = _infested.get(player);

            // Update
            if (data.Block.getType() == Material.AIR)
            {
                player.sendBlockChange(data.Block.getLocation(), 36, (byte) 0);
                for (Player other : UtilServer.getPlayers())
                    if (!other.equals(player))
                        other.sendBlockChange(data.Block.getLocation(), data.Material, data.Data);
            }

            if (!player.getLocation().getBlock().equals(data.Block))
            {
                // End
                InfestEnd(player);

                // Restore Block
                data.restore();

                infestIterator.remove();
            }
        }

        // Invisible
        for (Player player : GetPlayers(true))
        {
            if (!(GetKit(player) instanceof KitHiderTeleporter))
                continue;

            if (_infested.containsKey(player) || player.getVehicle() != null)
                Manager.GetCondition().Factory().Cloak("Infest", player, player, 1.9, false, false);
        }
    }

    public void InfestStart(LivingEntity ent)
    {
        UtilParticle.PlayParticle(ParticleType.SLIME, ent.getLocation().add(0, 0.6, 0), 0.3f, 0.3f, 0.3f, 0, 24,
				ViewDist.MAX, UtilServer.getPlayers());
        ent.getWorld().playSound(ent.getLocation(), Sound.SLIME_ATTACK, 2f, 1f);
        ent.getWorld().playSound(ent.getLocation(), Sound.SLIME_ATTACK, 2f, 1f);

        Manager.GetCondition().Factory().Cloak("Infest", ent, ent, 1.9, false, false);

        // Gets rid of timer, not needed until end
        Recharge.Instance.recharge((Player) ent, "Infest");

        _infestDeny.add(ent);
    }

    public void InfestEnd(LivingEntity ent)
    {
        Manager.GetCondition().EndCondition(ent, null, "Infest");

        ent.getWorld().playSound(ent.getLocation(), Sound.SLIME_ATTACK, 2f, 0.6f);

        if (ent instanceof Player)
        {
            Recharge.Instance.recharge((Player) ent, "Infest");
            Recharge.Instance.use((Player) ent, "Infest", 8000, true, true);
            _infestDeny.remove(ent);
        }
    }

    public boolean IsInfesting(LivingEntity ent)
    {
        if (_infested.containsKey(ent))
            return true;

        return ent.getVehicle() != null;
    }
}
