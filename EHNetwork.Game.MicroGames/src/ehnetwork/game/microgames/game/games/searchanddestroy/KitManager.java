package ehnetwork.game.microgames.game.games.searchanddestroy;

import java.util.ArrayList;

import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.game.games.searchanddestroy.kits.bow.KitBow;
import ehnetwork.game.microgames.game.games.searchanddestroy.kits.bow.KitPinner;
import ehnetwork.game.microgames.game.games.searchanddestroy.kits.bow.KitPunch;
import ehnetwork.game.microgames.game.games.searchanddestroy.kits.bow.KitQuickshooter;
import ehnetwork.game.microgames.game.games.searchanddestroy.kits.bow.KitSharpshooter;
import ehnetwork.game.microgames.game.games.searchanddestroy.kits.bow.KitSniper;
import ehnetwork.game.microgames.game.games.searchanddestroy.kits.bow.KitThunderstorm;
import ehnetwork.game.microgames.game.games.searchanddestroy.kits.rogue.KitEvade;
import ehnetwork.game.microgames.game.games.searchanddestroy.kits.rogue.KitFlash;
import ehnetwork.game.microgames.game.games.searchanddestroy.kits.rogue.KitMultiFlash;
import ehnetwork.game.microgames.game.games.searchanddestroy.kits.rogue.KitPhaseBlast;
import ehnetwork.game.microgames.game.games.searchanddestroy.kits.rogue.KitRewind;
import ehnetwork.game.microgames.game.games.searchanddestroy.kits.rogue.KitRogue;
import ehnetwork.game.microgames.game.games.searchanddestroy.kits.rogue.KitWraith;
import ehnetwork.game.microgames.game.games.searchanddestroy.kits.trooper.KitAssault;
import ehnetwork.game.microgames.game.games.searchanddestroy.kits.trooper.KitBeserker;
import ehnetwork.game.microgames.game.games.searchanddestroy.kits.trooper.KitChampion;
import ehnetwork.game.microgames.game.games.searchanddestroy.kits.trooper.KitJuggernaut;
import ehnetwork.game.microgames.game.games.searchanddestroy.kits.trooper.KitTank;
import ehnetwork.game.microgames.game.games.searchanddestroy.kits.trooper.KitTitan;
import ehnetwork.game.microgames.game.games.searchanddestroy.kits.trooper.KitTrooper;
import ehnetwork.game.microgames.kit.Kit;

public class KitManager
{

    public class UpgradeKit
    {
        ArrayList<UpgradeKit> children;
        UpgradeKit daddy;
        Kit kit;
        int kitSlot;
        int[] path;

        public UpgradeKit(UpgradeKit daddy, Kit me, ArrayList<UpgradeKit> children, int kitSlot, int... pathSlots)
        {
            this.kitSlot = kitSlot;
            this.path = pathSlots;
            this.daddy = daddy;
            this.kit = me;
            this.children = children;
        }
    }

    private ArrayList<UpgradeKit> _kits = new ArrayList<UpgradeKit>();

    public KitManager(SearchAndDestroy search)
    {
        MicroGamesManager man = search.getArcadeManager();

        { // Register sword kits
            ArrayList<UpgradeKit> children1 = new ArrayList<UpgradeKit>();
            ArrayList<UpgradeKit> children2 = new ArrayList<UpgradeKit>();
            ArrayList<UpgradeKit> children3 = new ArrayList<UpgradeKit>();

            UpgradeKit sword1 = new UpgradeKit(null, new KitTrooper(man), children1, 4);
            _kits.add(sword1);
            UpgradeKit sword2 = new UpgradeKit(sword1, new KitTank(man), children2, 20, 12);
            _kits.add(sword2);
            UpgradeKit sword3 = new UpgradeKit(sword1, new KitAssault(man), children3, 24, 14);
            _kits.add(sword3);
            UpgradeKit sword4 = new UpgradeKit(sword2, new KitJuggernaut(man), new ArrayList<UpgradeKit>(), 46, 29, 37);
            _kits.add(sword4);
            UpgradeKit sword5 = new UpgradeKit(sword2, new KitTitan(man), new ArrayList<UpgradeKit>(), 48, 29, 39);
            _kits.add(sword5);
            UpgradeKit sword6 = new UpgradeKit(sword3, new KitChampion(man), new ArrayList<UpgradeKit>(), 50, 33, 41);
            _kits.add(sword6);
            UpgradeKit sword7 = new UpgradeKit(sword3, new KitBeserker(man), new ArrayList<UpgradeKit>(), 52, 33, 43);
            _kits.add(sword7);
            children1.add(sword2);
            children1.add(sword3);
            children2.add(sword4);
            children2.add(sword5);
            children3.add(sword6);
            children3.add(sword7);

        }

        { // Register bow kits
            ArrayList<UpgradeKit> children1 = new ArrayList<UpgradeKit>();
            ArrayList<UpgradeKit> children2 = new ArrayList<UpgradeKit>();
            ArrayList<UpgradeKit> children3 = new ArrayList<UpgradeKit>();

            UpgradeKit bowKit = new UpgradeKit(null, new KitBow(man), children1, 4);
            _kits.add(bowKit);
            UpgradeKit quickShooter = new UpgradeKit(bowKit, new KitQuickshooter(man), children2, 20, 12);
            _kits.add(quickShooter);
            UpgradeKit bow3 = new UpgradeKit(bowKit, new KitPunch(man), children3, 24, 14);
            _kits.add(bow3);
            UpgradeKit sniper = new UpgradeKit(quickShooter, new KitSniper(man), new ArrayList<UpgradeKit>(), 46, 29, 37);
            _kits.add(sniper);
            UpgradeKit sharpShooter = new UpgradeKit(quickShooter, new KitSharpshooter(man), new ArrayList<UpgradeKit>(), 48, 29,
                    39);
            _kits.add(sharpShooter);
            UpgradeKit pinner = new UpgradeKit(bow3, new KitPinner(man), new ArrayList<UpgradeKit>(), 50, 33, 41);
            _kits.add(pinner);
            UpgradeKit thunderstorm = new UpgradeKit(bow3, new KitThunderstorm(man), new ArrayList<UpgradeKit>(), 52, 33, 43);
            _kits.add(thunderstorm);
            children1.add(quickShooter);
            children1.add(bow3);
            children2.add(sniper);
            children2.add(sharpShooter);
            children3.add(pinner);
            children3.add(thunderstorm);

        }

             { // Register rogue kits
                 ArrayList<UpgradeKit> children1 = new ArrayList<UpgradeKit>();
                 ArrayList<UpgradeKit> children2 = new ArrayList<UpgradeKit>();
                 ArrayList<UpgradeKit> children3 = new ArrayList<UpgradeKit>();

                 UpgradeKit rogue1 = new UpgradeKit(null, new KitRogue(man), children1, 4);
                 _kits.add(rogue1);
                 UpgradeKit rogue2 = new UpgradeKit(rogue1, new KitFlash(man), children2, 20, 12);
                 _kits.add(rogue2);
                 UpgradeKit rogue3 = new UpgradeKit(rogue1, new KitEvade(man), children3, 24, 14);
                 _kits.add(rogue3);
                 UpgradeKit rogue4 = new UpgradeKit(rogue2, new KitMultiFlash(man), new ArrayList<UpgradeKit>(), 46, 29, 37);
                 _kits.add(rogue4);
                 UpgradeKit rogue5 = new UpgradeKit(rogue2, new KitPhaseBlast(man), new ArrayList<UpgradeKit>(), 48, 29, 39);
                 _kits.add(rogue5);
                 UpgradeKit rogue6 = new UpgradeKit(rogue3, new KitRewind(man), new ArrayList<UpgradeKit>(), 50, 33, 41);
                 _kits.add(rogue6);
                 UpgradeKit rogue7 = new UpgradeKit(rogue3, new KitWraith(man), new ArrayList<UpgradeKit>(), 52, 33, 43);
                 _kits.add(rogue7);
                 children1.add(rogue2);
                 children1.add(rogue3);
                 children2.add(rogue4);
                 children2.add(rogue5);
                 children3.add(rogue6);
                 children3.add(rogue7);

             }

    }

    public Kit[] get_kits()
    {
        Kit[] kitArray = new Kit[_kits.size()];
        for (int i = 0; i < kitArray.length; i++)
        {
            kitArray[i] = _kits.get(i).kit;
        }
        return kitArray;
    }

    public ArrayList<UpgradeKit> getUpgradeKits()
    {
        return _kits;
    }

}
