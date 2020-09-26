package ehnetwork.core.account.repository.token;

import java.util.ArrayList;
import java.util.List;

import ehnetwork.core.donation.repository.token.CoinTransactionToken;
import ehnetwork.core.donation.repository.token.TransactionToken;
import ehnetwork.core.pet.repository.token.PetToken;
import ehnetwork.core.punish.Tokens.PunishmentToken;
import ehnetwork.minecraft.game.classcombat.Class.repository.token.SlotToken;

public class ClientToken
{
    public int AccountId;
    public String Name; // Shared by ClientToken & PunishClientToken
    public String Rank; // ClientToken (This)
    public boolean RankPerm; // ClientToken (This)
    public String RankExpire; // ClientToken (This)
    public int EconomyBalance; // ClientToken (This)
    
    public AccountToken AccountToken; // ClientToken (This): Specifies an instance AccountToken
	public long LastLogin; // ClientToken

    /* Donor Stuff */
    public ehnetwork.core.donation.repository.token.DonorToken DonorToken;
    public int Gems;
    public boolean Donated;
    public List<Integer> SalesPackages;
    public List<String> UnknownSalesPackages;
    public List<TransactionToken> Transactions;
    public List<CoinTransactionToken> CoinRewards;
    public int Coins;

    /* Punish Stuff */

	public long Time;
	public List<PunishmentToken> Punishments;

	/* Pets n' shit */
    public List<PetToken> Pets;
    public int PetNameTagCount;

    /* Clans/Dominate */

    public static int MAX_SKILL_TOKENS = 12;
    public static int MAX_ITEM_TOKENS = 12;

    public int CustomBuildId;

    public String PlayerName;
    public boolean Active;

    public Integer CustomBuildNumber = 0;

    public String PvpClass = "";

    public String SwordSkill = "";
    public Integer SwordSkillLevel = 0;

    public String AxeSkill = "";
    public Integer AxeSkillLevel = 0;

    public String BowSkill = "";
    public Integer BowSkillLevel = 0;

    public String ClassPassiveASkill = "";
    public Integer ClassPassiveASkillLevel = 0;

    public String ClassPassiveBSkill = "";
    public Integer ClassPassiveBSkillLevel = 0;

    public String GlobalPassiveSkill = "";
    public Integer GlobalPassiveSkillLevel = 0;

    public List<SlotToken> Slots = new ArrayList<SlotToken>(9);

    public int SkillTokens = MAX_SKILL_TOKENS;
    public int ItemTokens = 1;
}
