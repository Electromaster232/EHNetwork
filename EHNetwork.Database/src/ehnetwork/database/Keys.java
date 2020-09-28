/**
 * This class is generated by jOOQ
 */
package ehnetwork.database;

import ehnetwork.database.tables.*;
import ehnetwork.database.tables.records.*;

/**
 * A class modelling foreign key relationships between tables of the <code>Account</code> 
 * schema
 */
@javax.annotation.Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.5.2"
	},
	comments = "This class is generated by jOOQ"
)
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

	// -------------------------------------------------------------------------
	// IDENTITY definitions
	// -------------------------------------------------------------------------

	public static final org.jooq.Identity<AccountClanRecord, java.lang.Integer> IDENTITY_accountClan = Identities0.IDENTITY_accountClan;
	public static final org.jooq.Identity<AccountCoinTransactionsRecord, java.lang.Integer> IDENTITY_accountCoinTransactions = Identities0.IDENTITY_accountCoinTransactions;
	public static final org.jooq.Identity<AccountFriendRecord, java.lang.Integer> IDENTITY_accountFriend = Identities0.IDENTITY_accountFriend;
	public static final org.jooq.Identity<AccountGemTransactionsRecord, java.lang.Integer> IDENTITY_accountGemTransactions = Identities0.IDENTITY_accountGemTransactions;
	public static final org.jooq.Identity<AccountIgnoreRecord, java.lang.Integer> IDENTITY_accountIgnore = Identities0.IDENTITY_accountIgnore;
	public static final org.jooq.Identity<AccountInventoryRecord, java.lang.Integer> IDENTITY_accountInventory = Identities0.IDENTITY_accountInventory;
	public static final org.jooq.Identity<AccountPollsRecord, java.lang.Integer> IDENTITY_accountPolls = Identities0.IDENTITY_accountPolls;
	public static final org.jooq.Identity<AccountPreferencesRecord, java.lang.Integer> IDENTITY_accountPreferences = Identities0.IDENTITY_accountPreferences;
	public static final org.jooq.Identity<AccountsRecord, java.lang.Integer> IDENTITY_accounts = Identities0.IDENTITY_accounts;
	public static final org.jooq.Identity<AccountStatsRecord, java.lang.Integer> IDENTITY_accountStats = Identities0.IDENTITY_accountStats;
	public static final org.jooq.Identity<AccountTasksRecord, java.lang.Integer> IDENTITY_accountTasks = Identities0.IDENTITY_accountTasks;
	public static final org.jooq.Identity<AccountTransactionsRecord, java.lang.Integer> IDENTITY_accountTransactions = Identities0.IDENTITY_accountTransactions;
	public static final org.jooq.Identity<BonusRecord, java.lang.Integer> IDENTITY_bonus = Identities0.IDENTITY_bonus;
	public static final org.jooq.Identity<ClanAlliancesRecord, java.lang.Integer> IDENTITY_clanAlliances = Identities0.IDENTITY_clanAlliances;
	public static final org.jooq.Identity<ClanEnemiesRecord, java.lang.Integer> IDENTITY_clanEnemies = Identities0.IDENTITY_clanEnemies;
	public static final org.jooq.Identity<ClansRecord, java.lang.Integer> IDENTITY_clans = Identities0.IDENTITY_clans;
	public static final org.jooq.Identity<ClanServerRecord, java.lang.Integer> IDENTITY_clanServer = Identities0.IDENTITY_clanServer;
	public static final org.jooq.Identity<ClanShopItemRecord, java.lang.Integer> IDENTITY_clanShopItem = Identities0.IDENTITY_clanShopItem;
	public static final org.jooq.Identity<ClanTerritoryRecord, java.lang.Integer> IDENTITY_clanTerritory = Identities0.IDENTITY_clanTerritory;
	public static final org.jooq.Identity<EloRatingRecord, org.jooq.types.UInteger> IDENTITY_eloRating = Identities0.IDENTITY_eloRating;
	public static final org.jooq.Identity<FieldBlockRecord, java.lang.Integer> IDENTITY_fieldBlock = Identities0.IDENTITY_fieldBlock;
	public static final org.jooq.Identity<FieldMonsterRecord, java.lang.Integer> IDENTITY_fieldMonster = Identities0.IDENTITY_fieldMonster;
	public static final org.jooq.Identity<FieldOreRecord, java.lang.Integer> IDENTITY_fieldOre = Identities0.IDENTITY_fieldOre;
	public static final org.jooq.Identity<ItemCategoriesRecord, java.lang.Integer> IDENTITY_itemCategories = Identities0.IDENTITY_itemCategories;
	public static final org.jooq.Identity<ItemsRecord, java.lang.Integer> IDENTITY_items = Identities0.IDENTITY_items;
	public static final org.jooq.Identity<MailRecord, java.lang.Integer> IDENTITY_mail = Identities0.IDENTITY_mail;
	public static final org.jooq.Identity<MailboxRecord, java.lang.Integer> IDENTITY_mailbox = Identities0.IDENTITY_mailbox;
	public static final org.jooq.Identity<NpcsRecord, java.lang.Integer> IDENTITY_npcs = Identities0.IDENTITY_npcs;
	public static final org.jooq.Identity<PlayerMapRecord, java.lang.Integer> IDENTITY_playerMap = Identities0.IDENTITY_playerMap;
	public static final org.jooq.Identity<PollsRecord, java.lang.Integer> IDENTITY_polls = Identities0.IDENTITY_polls;
	public static final org.jooq.Identity<RankBenefitsRecord, java.lang.Integer> IDENTITY_rankBenefits = Identities0.IDENTITY_rankBenefits;
	public static final org.jooq.Identity<ServerPasswordRecord, java.lang.Integer> IDENTITY_serverPassword = Identities0.IDENTITY_serverPassword;
	public static final org.jooq.Identity<SpawnsRecord, java.lang.Integer> IDENTITY_spawns = Identities0.IDENTITY_spawns;
	public static final org.jooq.Identity<StatEventsRecord, org.jooq.types.UInteger> IDENTITY_statEvents = Identities0.IDENTITY_statEvents;
	public static final org.jooq.Identity<StatsRecord, java.lang.Integer> IDENTITY_stats = Identities0.IDENTITY_stats;
	public static final org.jooq.Identity<TasksRecord, java.lang.Integer> IDENTITY_tasks = Identities0.IDENTITY_tasks;
	public static final org.jooq.Identity<TransactionsRecord, java.lang.Integer> IDENTITY_transactions = Identities0.IDENTITY_transactions;

	// -------------------------------------------------------------------------
	// UNIQUE and PRIMARY KEY definitions
	// -------------------------------------------------------------------------

	public static final org.jooq.UniqueKey<AccountClanRecord> KEY_accountClan_PRIMARY = UniqueKeys0.KEY_accountClan_PRIMARY;
	public static final org.jooq.UniqueKey<AccountCoinTransactionsRecord> KEY_accountCoinTransactions_PRIMARY = UniqueKeys0.KEY_accountCoinTransactions_PRIMARY;
	public static final org.jooq.UniqueKey<AccountFriendRecord> KEY_accountFriend_PRIMARY = UniqueKeys0.KEY_accountFriend_PRIMARY;
	public static final org.jooq.UniqueKey<AccountFriendRecord> KEY_accountFriend_uuidIndex = UniqueKeys0.KEY_accountFriend_uuidIndex;
	public static final org.jooq.UniqueKey<AccountGemTransactionsRecord> KEY_accountGemTransactions_PRIMARY = UniqueKeys0.KEY_accountGemTransactions_PRIMARY;
	public static final org.jooq.UniqueKey<AccountIgnoreRecord> KEY_accountIgnore_PRIMARY = UniqueKeys0.KEY_accountIgnore_PRIMARY;
	public static final org.jooq.UniqueKey<AccountInventoryRecord> KEY_accountInventory_PRIMARY = UniqueKeys0.KEY_accountInventory_PRIMARY;
	public static final org.jooq.UniqueKey<AccountInventoryRecord> KEY_accountInventory_accountItemIndex = UniqueKeys0.KEY_accountInventory_accountItemIndex;
	public static final org.jooq.UniqueKey<AccountPollsRecord> KEY_accountPolls_PRIMARY = UniqueKeys0.KEY_accountPolls_PRIMARY;
	public static final org.jooq.UniqueKey<AccountPollsRecord> KEY_accountPolls_accountPollIndex = UniqueKeys0.KEY_accountPolls_accountPollIndex;
	public static final org.jooq.UniqueKey<AccountPreferencesRecord> KEY_accountPreferences_PRIMARY = UniqueKeys0.KEY_accountPreferences_PRIMARY;
	public static final org.jooq.UniqueKey<AccountPreferencesRecord> KEY_accountPreferences_uuid_index = UniqueKeys0.KEY_accountPreferences_uuid_index;
	public static final org.jooq.UniqueKey<AccountsRecord> KEY_accounts_PRIMARY = UniqueKeys0.KEY_accounts_PRIMARY;
	public static final org.jooq.UniqueKey<AccountsRecord> KEY_accounts_uuidIndex = UniqueKeys0.KEY_accounts_uuidIndex;
	public static final org.jooq.UniqueKey<AccountStatRecord> KEY_accountStat_PRIMARY = UniqueKeys0.KEY_accountStat_PRIMARY;
	public static final org.jooq.UniqueKey<AccountStatsRecord> KEY_accountStats_PRIMARY = UniqueKeys0.KEY_accountStats_PRIMARY;
	public static final org.jooq.UniqueKey<AccountTasksRecord> KEY_accountTasks_PRIMARY = UniqueKeys0.KEY_accountTasks_PRIMARY;
	public static final org.jooq.UniqueKey<AccountTransactionsRecord> KEY_accountTransactions_PRIMARY = UniqueKeys0.KEY_accountTransactions_PRIMARY;
	public static final org.jooq.UniqueKey<ActiveTournamentsRecord> KEY_activeTournaments_PRIMARY = UniqueKeys0.KEY_activeTournaments_PRIMARY;
	public static final org.jooq.UniqueKey<BonusRecord> KEY_bonus_PRIMARY = UniqueKeys0.KEY_bonus_PRIMARY;
	public static final org.jooq.UniqueKey<ClanAlliancesRecord> KEY_clanAlliances_PRIMARY = UniqueKeys0.KEY_clanAlliances_PRIMARY;
	public static final org.jooq.UniqueKey<ClanEnemiesRecord> KEY_clanEnemies_PRIMARY = UniqueKeys0.KEY_clanEnemies_PRIMARY;
	public static final org.jooq.UniqueKey<ClanEnemiesRecord> KEY_clanEnemies_unique_clanId = UniqueKeys0.KEY_clanEnemies_unique_clanId;
	public static final org.jooq.UniqueKey<ClanEnemiesRecord> KEY_clanEnemies_unique_otherClanId = UniqueKeys0.KEY_clanEnemies_unique_otherClanId;
	public static final org.jooq.UniqueKey<ClansRecord> KEY_clans_PRIMARY = UniqueKeys0.KEY_clans_PRIMARY;
	public static final org.jooq.UniqueKey<ClansRecord> KEY_clans_clanName = UniqueKeys0.KEY_clans_clanName;
	public static final org.jooq.UniqueKey<ClanServerRecord> KEY_clanServer_PRIMARY = UniqueKeys0.KEY_clanServer_PRIMARY;
	public static final org.jooq.UniqueKey<ClanShopItemRecord> KEY_clanShopItem_PRIMARY = UniqueKeys0.KEY_clanShopItem_PRIMARY;
	public static final org.jooq.UniqueKey<ClanShopItemRecord> KEY_clanShopItem_shop_page_slot_UNIQUE = UniqueKeys0.KEY_clanShopItem_shop_page_slot_UNIQUE;
	public static final org.jooq.UniqueKey<ClanTerritoryRecord> KEY_clanTerritory_PRIMARY = UniqueKeys0.KEY_clanTerritory_PRIMARY;
	public static final org.jooq.UniqueKey<ClanTerritoryRecord> KEY_clanTerritory_territory_server_chunk_UNIQUE = UniqueKeys0.KEY_clanTerritory_territory_server_chunk_UNIQUE;
	public static final org.jooq.UniqueKey<EloRatingRecord> KEY_eloRating_PRIMARY = UniqueKeys0.KEY_eloRating_PRIMARY;
	public static final org.jooq.UniqueKey<EloRatingRecord> KEY_eloRating_uuid_gameType_index = UniqueKeys0.KEY_eloRating_uuid_gameType_index;
	public static final org.jooq.UniqueKey<FieldBlockRecord> KEY_fieldBlock_PRIMARY = UniqueKeys0.KEY_fieldBlock_PRIMARY;
	public static final org.jooq.UniqueKey<FieldMonsterRecord> KEY_fieldMonster_PRIMARY = UniqueKeys0.KEY_fieldMonster_PRIMARY;
	public static final org.jooq.UniqueKey<FieldOreRecord> KEY_fieldOre_PRIMARY = UniqueKeys0.KEY_fieldOre_PRIMARY;
	public static final org.jooq.UniqueKey<ItemCategoriesRecord> KEY_itemCategories_PRIMARY = UniqueKeys0.KEY_itemCategories_PRIMARY;
	public static final org.jooq.UniqueKey<ItemCategoriesRecord> KEY_itemCategories_nameIndex = UniqueKeys0.KEY_itemCategories_nameIndex;
	public static final org.jooq.UniqueKey<ItemsRecord> KEY_items_PRIMARY = UniqueKeys0.KEY_items_PRIMARY;
	public static final org.jooq.UniqueKey<ItemsRecord> KEY_items_uniqueNameCategoryIndex = UniqueKeys0.KEY_items_uniqueNameCategoryIndex;
	public static final org.jooq.UniqueKey<MailRecord> KEY_mail_PRIMARY = UniqueKeys0.KEY_mail_PRIMARY;
	public static final org.jooq.UniqueKey<MailboxRecord> KEY_mailbox_PRIMARY = UniqueKeys0.KEY_mailbox_PRIMARY;
	public static final org.jooq.UniqueKey<NpcsRecord> KEY_npcs_PRIMARY = UniqueKeys0.KEY_npcs_PRIMARY;
	public static final org.jooq.UniqueKey<PlayerMapRecord> KEY_playerMap_PRIMARY = UniqueKeys0.KEY_playerMap_PRIMARY;
	public static final org.jooq.UniqueKey<PlayerMapRecord> KEY_playerMap_playerIndex = UniqueKeys0.KEY_playerMap_playerIndex;
	public static final org.jooq.UniqueKey<PollsRecord> KEY_polls_PRIMARY = UniqueKeys0.KEY_polls_PRIMARY;
	public static final org.jooq.UniqueKey<RankBenefitsRecord> KEY_rankBenefits_PRIMARY = UniqueKeys0.KEY_rankBenefits_PRIMARY;
	public static final org.jooq.UniqueKey<ServerPasswordRecord> KEY_serverPassword_PRIMARY = UniqueKeys0.KEY_serverPassword_PRIMARY;
	public static final org.jooq.UniqueKey<SpawnsRecord> KEY_spawns_PRIMARY = UniqueKeys0.KEY_spawns_PRIMARY;
	public static final org.jooq.UniqueKey<StatEventsRecord> KEY_statEvents_PRIMARY = UniqueKeys0.KEY_statEvents_PRIMARY;
	public static final org.jooq.UniqueKey<StatEventsRecord> KEY_statEvents_UK_DailyEntry = UniqueKeys0.KEY_statEvents_UK_DailyEntry;
	public static final org.jooq.UniqueKey<StatsRecord> KEY_stats_PRIMARY = UniqueKeys0.KEY_stats_PRIMARY;
	public static final org.jooq.UniqueKey<StatsRecord> KEY_stats_nameIndex = UniqueKeys0.KEY_stats_nameIndex;
	public static final org.jooq.UniqueKey<StatTypesRecord> KEY_statTypes_PRIMARY = UniqueKeys0.KEY_statTypes_PRIMARY;
	public static final org.jooq.UniqueKey<StatTypesRecord> KEY_statTypes_name = UniqueKeys0.KEY_statTypes_name;
	public static final org.jooq.UniqueKey<TasksRecord> KEY_tasks_PRIMARY = UniqueKeys0.KEY_tasks_PRIMARY;
	public static final org.jooq.UniqueKey<TasksRecord> KEY_tasks_name_UNIQUE = UniqueKeys0.KEY_tasks_name_UNIQUE;
	public static final org.jooq.UniqueKey<TournamentLBRecord> KEY_TournamentLB_PRIMARY = UniqueKeys0.KEY_TournamentLB_PRIMARY;
	public static final org.jooq.UniqueKey<TournamentLBRecord> KEY_TournamentLB_UK_PlayerEntry = UniqueKeys0.KEY_TournamentLB_UK_PlayerEntry;
	public static final org.jooq.UniqueKey<TransactionsRecord> KEY_transactions_PRIMARY = UniqueKeys0.KEY_transactions_PRIMARY;

	// -------------------------------------------------------------------------
	// FOREIGN KEY definitions
	// -------------------------------------------------------------------------

	public static final org.jooq.ForeignKey<AccountClanRecord, AccountsRecord> FK_accountClan_accounts = ForeignKeys0.FK_accountClan_accounts;
	public static final org.jooq.ForeignKey<AccountClanRecord, ClansRecord> FK_accountClan_clans2 = ForeignKeys0.FK_accountClan_clans2;
	public static final org.jooq.ForeignKey<AccountCoinTransactionsRecord, AccountsRecord> FK_ACT_ACCOUNTS_ID = ForeignKeys0.FK_ACT_ACCOUNTS_ID;
	public static final org.jooq.ForeignKey<AccountGemTransactionsRecord, AccountsRecord> accountGemTransactions_ibfk_1 = ForeignKeys0.accountGemTransactions_ibfk_1;
	public static final org.jooq.ForeignKey<AccountInventoryRecord, AccountsRecord> accountInventory_ibfk_1 = ForeignKeys0.accountInventory_ibfk_1;
	public static final org.jooq.ForeignKey<AccountInventoryRecord, ItemsRecord> accountInventory_ibfk_2 = ForeignKeys0.accountInventory_ibfk_2;
	public static final org.jooq.ForeignKey<AccountPollsRecord, AccountsRecord> accountPolls_ibfk_1 = ForeignKeys0.accountPolls_ibfk_1;
	public static final org.jooq.ForeignKey<AccountPollsRecord, PollsRecord> accountPolls_ibfk_2 = ForeignKeys0.accountPolls_ibfk_2;
	public static final org.jooq.ForeignKey<AccountStatRecord, AccountsRecord> accountStat_account = ForeignKeys0.accountStat_account;
	public static final org.jooq.ForeignKey<AccountStatRecord, StatsRecord> accountStat_stat = ForeignKeys0.accountStat_stat;
	public static final org.jooq.ForeignKey<AccountStatsRecord, AccountsRecord> accountStats_ibfk_1 = ForeignKeys0.accountStats_ibfk_1;
	public static final org.jooq.ForeignKey<AccountStatsRecord, StatsRecord> accountStats_ibfk_2 = ForeignKeys0.accountStats_ibfk_2;
	public static final org.jooq.ForeignKey<AccountTasksRecord, AccountsRecord> ACCOUNTTASKS_ACCOUNTID = ForeignKeys0.ACCOUNTTASKS_ACCOUNTID;
	public static final org.jooq.ForeignKey<AccountTasksRecord, TasksRecord> ACCOUNTTASKS_TASKID = ForeignKeys0.ACCOUNTTASKS_TASKID;
	public static final org.jooq.ForeignKey<AccountTransactionsRecord, AccountsRecord> ACCOUNTTRANSACTIONS_ACCOUNTID = ForeignKeys0.ACCOUNTTRANSACTIONS_ACCOUNTID;
	public static final org.jooq.ForeignKey<AccountTransactionsRecord, TransactionsRecord> ACCOUNTTRANSACTIONS_TRANSACTIONID = ForeignKeys0.ACCOUNTTRANSACTIONS_TRANSACTIONID;
	public static final org.jooq.ForeignKey<BonusRecord, AccountsRecord> bonus_ibfk_1 = ForeignKeys0.bonus_ibfk_1;
	public static final org.jooq.ForeignKey<ClanAlliancesRecord, ClansRecord> clanAlliances_ibfk_2 = ForeignKeys0.clanAlliances_ibfk_2;
	public static final org.jooq.ForeignKey<ClanAlliancesRecord, ClansRecord> clanAlliances_ibfk_1 = ForeignKeys0.clanAlliances_ibfk_1;
	public static final org.jooq.ForeignKey<ClanEnemiesRecord, ClansRecord> clanEnemies_ibfk_1 = ForeignKeys0.clanEnemies_ibfk_1;
	public static final org.jooq.ForeignKey<ClanEnemiesRecord, ClansRecord> clanEnemies_ibfk_2 = ForeignKeys0.clanEnemies_ibfk_2;
	public static final org.jooq.ForeignKey<ClansRecord, ClanServerRecord> clans_ibfk_1 = ForeignKeys0.clans_ibfk_1;
	public static final org.jooq.ForeignKey<ClanTerritoryRecord, ClansRecord> clanTerritory_ibfk_1 = ForeignKeys0.clanTerritory_ibfk_1;
	public static final org.jooq.ForeignKey<ClanTerritoryRecord, ClanServerRecord> clanTerritory_ibfk_2 = ForeignKeys0.clanTerritory_ibfk_2;
	public static final org.jooq.ForeignKey<ItemsRecord, ItemCategoriesRecord> items_ibfk_1 = ForeignKeys0.items_ibfk_1;
	public static final org.jooq.ForeignKey<MailRecord, AccountsRecord> mail_ibfk_1 = ForeignKeys0.mail_ibfk_1;
	public static final org.jooq.ForeignKey<MailboxRecord, AccountsRecord> mailbox_ibfk_1 = ForeignKeys0.mailbox_ibfk_1;
	public static final org.jooq.ForeignKey<RankBenefitsRecord, AccountsRecord> rankBenefits_ibfk_1 = ForeignKeys0.rankBenefits_ibfk_1;

	// -------------------------------------------------------------------------
	// [#1459] distribute members to avoid static initialisers > 64kb
	// -------------------------------------------------------------------------

	private static class Identities0 extends org.jooq.impl.AbstractKeys {
		public static org.jooq.Identity<AccountClanRecord, java.lang.Integer> IDENTITY_accountClan = createIdentity(AccountClan.accountClan, AccountClan.accountClan.id);
		public static org.jooq.Identity<AccountCoinTransactionsRecord, java.lang.Integer> IDENTITY_accountCoinTransactions = createIdentity(AccountCoinTransactions.accountCoinTransactions, AccountCoinTransactions.accountCoinTransactions.id);
		public static org.jooq.Identity<AccountFriendRecord, java.lang.Integer> IDENTITY_accountFriend = createIdentity(AccountFriend.accountFriend, AccountFriend.accountFriend.id);
		public static org.jooq.Identity<AccountGemTransactionsRecord, java.lang.Integer> IDENTITY_accountGemTransactions = createIdentity(AccountGemTransactions.accountGemTransactions, AccountGemTransactions.accountGemTransactions.id);
		public static org.jooq.Identity<AccountIgnoreRecord, java.lang.Integer> IDENTITY_accountIgnore = createIdentity(AccountIgnore.accountIgnore, AccountIgnore.accountIgnore.id);
		public static org.jooq.Identity<AccountInventoryRecord, java.lang.Integer> IDENTITY_accountInventory = createIdentity(AccountInventory.accountInventory, AccountInventory.accountInventory.id);
		public static org.jooq.Identity<AccountPollsRecord, java.lang.Integer> IDENTITY_accountPolls = createIdentity(AccountPolls.accountPolls, AccountPolls.accountPolls.id);
		public static org.jooq.Identity<AccountPreferencesRecord, java.lang.Integer> IDENTITY_accountPreferences = createIdentity(AccountPreferences.accountPreferences, AccountPreferences.accountPreferences.id);
		public static org.jooq.Identity<AccountsRecord, java.lang.Integer> IDENTITY_accounts = createIdentity(Accounts.accounts, Accounts.accounts.id);
		public static org.jooq.Identity<AccountStatsRecord, java.lang.Integer> IDENTITY_accountStats = createIdentity(AccountStats.accountStats, AccountStats.accountStats.id);
		public static org.jooq.Identity<AccountTasksRecord, java.lang.Integer> IDENTITY_accountTasks = createIdentity(AccountTasks.accountTasks, AccountTasks.accountTasks.id);
		public static org.jooq.Identity<AccountTransactionsRecord, java.lang.Integer> IDENTITY_accountTransactions = createIdentity(AccountTransactions.accountTransactions, AccountTransactions.accountTransactions.id);
		public static org.jooq.Identity<BonusRecord, java.lang.Integer> IDENTITY_bonus = createIdentity(Bonus.bonus, Bonus.bonus.accountId);
		public static org.jooq.Identity<ClanAlliancesRecord, java.lang.Integer> IDENTITY_clanAlliances = createIdentity(ClanAlliances.clanAlliances, ClanAlliances.clanAlliances.id);
		public static org.jooq.Identity<ClanEnemiesRecord, java.lang.Integer> IDENTITY_clanEnemies = createIdentity(ClanEnemies.clanEnemies, ClanEnemies.clanEnemies.id);
		public static org.jooq.Identity<ClansRecord, java.lang.Integer> IDENTITY_clans = createIdentity(Clans.clans, Clans.clans.id);
		public static org.jooq.Identity<ClanServerRecord, java.lang.Integer> IDENTITY_clanServer = createIdentity(ClanServer.clanServer, ClanServer.clanServer.id);
		public static org.jooq.Identity<ClanShopItemRecord, java.lang.Integer> IDENTITY_clanShopItem = createIdentity(ClanShopItem.clanShopItem, ClanShopItem.clanShopItem.id);
		public static org.jooq.Identity<ClanTerritoryRecord, java.lang.Integer> IDENTITY_clanTerritory = createIdentity(ClanTerritory.clanTerritory, ClanTerritory.clanTerritory.id);
		public static org.jooq.Identity<EloRatingRecord, org.jooq.types.UInteger> IDENTITY_eloRating = createIdentity(EloRating.eloRating, EloRating.eloRating.id);
		public static org.jooq.Identity<FieldBlockRecord, java.lang.Integer> IDENTITY_fieldBlock = createIdentity(FieldBlock.fieldBlock, FieldBlock.fieldBlock.id);
		public static org.jooq.Identity<FieldMonsterRecord, java.lang.Integer> IDENTITY_fieldMonster = createIdentity(FieldMonster.fieldMonster, FieldMonster.fieldMonster.id);
		public static org.jooq.Identity<FieldOreRecord, java.lang.Integer> IDENTITY_fieldOre = createIdentity(FieldOre.fieldOre, FieldOre.fieldOre.id);
		public static org.jooq.Identity<ItemCategoriesRecord, java.lang.Integer> IDENTITY_itemCategories = createIdentity(ItemCategories.itemCategories, ItemCategories.itemCategories.id);
		public static org.jooq.Identity<ItemsRecord, java.lang.Integer> IDENTITY_items = createIdentity(Items.items, Items.items.id);
		public static org.jooq.Identity<MailRecord, java.lang.Integer> IDENTITY_mail = createIdentity(Mail.mail, Mail.mail.id);
		public static org.jooq.Identity<MailboxRecord, java.lang.Integer> IDENTITY_mailbox = createIdentity(Mailbox.mailbox, Mailbox.mailbox.id);
		public static org.jooq.Identity<NpcsRecord, java.lang.Integer> IDENTITY_npcs = createIdentity(Npcs.npcs, Npcs.npcs.id);
		public static org.jooq.Identity<PlayerMapRecord, java.lang.Integer> IDENTITY_playerMap = createIdentity(PlayerMap.playerMap, PlayerMap.playerMap.id);
		public static org.jooq.Identity<PollsRecord, java.lang.Integer> IDENTITY_polls = createIdentity(Polls.polls, Polls.polls.id);
		public static org.jooq.Identity<RankBenefitsRecord, java.lang.Integer> IDENTITY_rankBenefits = createIdentity(RankBenefits.rankBenefits, RankBenefits.rankBenefits.id);
		public static org.jooq.Identity<ServerPasswordRecord, java.lang.Integer> IDENTITY_serverPassword = createIdentity(ServerPassword.serverPassword, ServerPassword.serverPassword.id);
		public static org.jooq.Identity<SpawnsRecord, java.lang.Integer> IDENTITY_spawns = createIdentity(Spawns.spawns, Spawns.spawns.id);
		public static org.jooq.Identity<StatEventsRecord, org.jooq.types.UInteger> IDENTITY_statEvents = createIdentity(StatEvents.statEvents, StatEvents.statEvents.eventId);
		public static org.jooq.Identity<StatsRecord, java.lang.Integer> IDENTITY_stats = createIdentity(Stats.stats, Stats.stats.id);
		public static org.jooq.Identity<TasksRecord, java.lang.Integer> IDENTITY_tasks = createIdentity(Tasks.tasks, Tasks.tasks.id);
		public static org.jooq.Identity<TransactionsRecord, java.lang.Integer> IDENTITY_transactions = createIdentity(Transactions.transactions, Transactions.transactions.id);
	}

	private static class UniqueKeys0 extends org.jooq.impl.AbstractKeys {
		public static final org.jooq.UniqueKey<AccountClanRecord> KEY_accountClan_PRIMARY = createUniqueKey(AccountClan.accountClan, AccountClan.accountClan.id);
		public static final org.jooq.UniqueKey<AccountCoinTransactionsRecord> KEY_accountCoinTransactions_PRIMARY = createUniqueKey(AccountCoinTransactions.accountCoinTransactions, AccountCoinTransactions.accountCoinTransactions.id);
		public static final org.jooq.UniqueKey<AccountFriendRecord> KEY_accountFriend_PRIMARY = createUniqueKey(AccountFriend.accountFriend, AccountFriend.accountFriend.id);
		public static final org.jooq.UniqueKey<AccountFriendRecord> KEY_accountFriend_uuidIndex = createUniqueKey(AccountFriend.accountFriend, AccountFriend.accountFriend.uuidSource, AccountFriend.accountFriend.uuidTarget);
		public static final org.jooq.UniqueKey<AccountGemTransactionsRecord> KEY_accountGemTransactions_PRIMARY = createUniqueKey(AccountGemTransactions.accountGemTransactions, AccountGemTransactions.accountGemTransactions.id);
		public static final org.jooq.UniqueKey<AccountIgnoreRecord> KEY_accountIgnore_PRIMARY = createUniqueKey(AccountIgnore.accountIgnore, AccountIgnore.accountIgnore.id);
		public static final org.jooq.UniqueKey<AccountInventoryRecord> KEY_accountInventory_PRIMARY = createUniqueKey(AccountInventory.accountInventory, AccountInventory.accountInventory.id);
		public static final org.jooq.UniqueKey<AccountInventoryRecord> KEY_accountInventory_accountItemIndex = createUniqueKey(AccountInventory.accountInventory, AccountInventory.accountInventory.accountId, AccountInventory.accountInventory.itemId);
		public static final org.jooq.UniqueKey<AccountPollsRecord> KEY_accountPolls_PRIMARY = createUniqueKey(AccountPolls.accountPolls, AccountPolls.accountPolls.id);
		public static final org.jooq.UniqueKey<AccountPollsRecord> KEY_accountPolls_accountPollIndex = createUniqueKey(AccountPolls.accountPolls, AccountPolls.accountPolls.accountId, AccountPolls.accountPolls.pollId);
		public static final org.jooq.UniqueKey<AccountPreferencesRecord> KEY_accountPreferences_PRIMARY = createUniqueKey(AccountPreferences.accountPreferences, AccountPreferences.accountPreferences.id);
		public static final org.jooq.UniqueKey<AccountPreferencesRecord> KEY_accountPreferences_uuid_index = createUniqueKey(AccountPreferences.accountPreferences, AccountPreferences.accountPreferences.uuid);
		public static final org.jooq.UniqueKey<AccountsRecord> KEY_accounts_PRIMARY = createUniqueKey(Accounts.accounts, Accounts.accounts.id);
		public static final org.jooq.UniqueKey<AccountsRecord> KEY_accounts_uuidIndex = createUniqueKey(Accounts.accounts, Accounts.accounts.uuid);
		public static final org.jooq.UniqueKey<AccountStatRecord> KEY_accountStat_PRIMARY = createUniqueKey(AccountStat.accountStat, AccountStat.accountStat.accountId, AccountStat.accountStat.statId);
		public static final org.jooq.UniqueKey<AccountStatsRecord> KEY_accountStats_PRIMARY = createUniqueKey(AccountStats.accountStats, AccountStats.accountStats.id);
		public static final org.jooq.UniqueKey<AccountTasksRecord> KEY_accountTasks_PRIMARY = createUniqueKey(AccountTasks.accountTasks, AccountTasks.accountTasks.id);
		public static final org.jooq.UniqueKey<AccountTransactionsRecord> KEY_accountTransactions_PRIMARY = createUniqueKey(AccountTransactions.accountTransactions, AccountTransactions.accountTransactions.id);
		public static final org.jooq.UniqueKey<ActiveTournamentsRecord> KEY_activeTournaments_PRIMARY = createUniqueKey(ActiveTournaments.activeTournaments, ActiveTournaments.activeTournaments.name);
		public static final org.jooq.UniqueKey<BonusRecord> KEY_bonus_PRIMARY = createUniqueKey(Bonus.bonus, Bonus.bonus.accountId);
		public static final org.jooq.UniqueKey<ClanAlliancesRecord> KEY_clanAlliances_PRIMARY = createUniqueKey(ClanAlliances.clanAlliances, ClanAlliances.clanAlliances.id);
		public static final org.jooq.UniqueKey<ClanEnemiesRecord> KEY_clanEnemies_PRIMARY = createUniqueKey(ClanEnemies.clanEnemies, ClanEnemies.clanEnemies.id);
		public static final org.jooq.UniqueKey<ClanEnemiesRecord> KEY_clanEnemies_unique_clanId = createUniqueKey(ClanEnemies.clanEnemies, ClanEnemies.clanEnemies.clanId);
		public static final org.jooq.UniqueKey<ClanEnemiesRecord> KEY_clanEnemies_unique_otherClanId = createUniqueKey(ClanEnemies.clanEnemies, ClanEnemies.clanEnemies.otherClanId);
		public static final org.jooq.UniqueKey<ClansRecord> KEY_clans_PRIMARY = createUniqueKey(Clans.clans, Clans.clans.id);
		public static final org.jooq.UniqueKey<ClansRecord> KEY_clans_clanName = createUniqueKey(Clans.clans, Clans.clans.serverId, Clans.clans.name);
		public static final org.jooq.UniqueKey<ClanServerRecord> KEY_clanServer_PRIMARY = createUniqueKey(ClanServer.clanServer, ClanServer.clanServer.id, ClanServer.clanServer.serverName);
		public static final org.jooq.UniqueKey<ClanShopItemRecord> KEY_clanShopItem_PRIMARY = createUniqueKey(ClanShopItem.clanShopItem, ClanShopItem.clanShopItem.id);
		public static final org.jooq.UniqueKey<ClanShopItemRecord> KEY_clanShopItem_shop_page_slot_UNIQUE = createUniqueKey(ClanShopItem.clanShopItem, ClanShopItem.clanShopItem.shopName, ClanShopItem.clanShopItem.shopPage, ClanShopItem.clanShopItem.slot);
		public static final org.jooq.UniqueKey<ClanTerritoryRecord> KEY_clanTerritory_PRIMARY = createUniqueKey(ClanTerritory.clanTerritory, ClanTerritory.clanTerritory.id);
		public static final org.jooq.UniqueKey<ClanTerritoryRecord> KEY_clanTerritory_territory_server_chunk_UNIQUE = createUniqueKey(ClanTerritory.clanTerritory, ClanTerritory.clanTerritory.serverId, ClanTerritory.clanTerritory.chunk);
		public static final org.jooq.UniqueKey<EloRatingRecord> KEY_eloRating_PRIMARY = createUniqueKey(EloRating.eloRating, EloRating.eloRating.id);
		public static final org.jooq.UniqueKey<EloRatingRecord> KEY_eloRating_uuid_gameType_index = createUniqueKey(EloRating.eloRating, EloRating.eloRating.uuid, EloRating.eloRating.gamemode);
		public static final org.jooq.UniqueKey<FieldBlockRecord> KEY_fieldBlock_PRIMARY = createUniqueKey(FieldBlock.fieldBlock, FieldBlock.fieldBlock.id);
		public static final org.jooq.UniqueKey<FieldMonsterRecord> KEY_fieldMonster_PRIMARY = createUniqueKey(FieldMonster.fieldMonster, FieldMonster.fieldMonster.id);
		public static final org.jooq.UniqueKey<FieldOreRecord> KEY_fieldOre_PRIMARY = createUniqueKey(FieldOre.fieldOre, FieldOre.fieldOre.id);
		public static final org.jooq.UniqueKey<ItemCategoriesRecord> KEY_itemCategories_PRIMARY = createUniqueKey(ItemCategories.itemCategories, ItemCategories.itemCategories.id);
		public static final org.jooq.UniqueKey<ItemCategoriesRecord> KEY_itemCategories_nameIndex = createUniqueKey(ItemCategories.itemCategories, ItemCategories.itemCategories.name);
		public static final org.jooq.UniqueKey<ItemsRecord> KEY_items_PRIMARY = createUniqueKey(Items.items, Items.items.id);
		public static final org.jooq.UniqueKey<ItemsRecord> KEY_items_uniqueNameCategoryIndex = createUniqueKey(Items.items, Items.items.name, Items.items.categoryId);
		public static final org.jooq.UniqueKey<MailRecord> KEY_mail_PRIMARY = createUniqueKey(Mail.mail, Mail.mail.id);
		public static final org.jooq.UniqueKey<MailboxRecord> KEY_mailbox_PRIMARY = createUniqueKey(Mailbox.mailbox, Mailbox.mailbox.id);
		public static final org.jooq.UniqueKey<NpcsRecord> KEY_npcs_PRIMARY = createUniqueKey(Npcs.npcs, Npcs.npcs.id);
		public static final org.jooq.UniqueKey<PlayerMapRecord> KEY_playerMap_PRIMARY = createUniqueKey(PlayerMap.playerMap, PlayerMap.playerMap.id);
		public static final org.jooq.UniqueKey<PlayerMapRecord> KEY_playerMap_playerIndex = createUniqueKey(PlayerMap.playerMap, PlayerMap.playerMap.playerName);
		public static final org.jooq.UniqueKey<PollsRecord> KEY_polls_PRIMARY = createUniqueKey(Polls.polls, Polls.polls.id);
		public static final org.jooq.UniqueKey<RankBenefitsRecord> KEY_rankBenefits_PRIMARY = createUniqueKey(RankBenefits.rankBenefits, RankBenefits.rankBenefits.id);
		public static final org.jooq.UniqueKey<ServerPasswordRecord> KEY_serverPassword_PRIMARY = createUniqueKey(ServerPassword.serverPassword, ServerPassword.serverPassword.id);
		public static final org.jooq.UniqueKey<SpawnsRecord> KEY_spawns_PRIMARY = createUniqueKey(Spawns.spawns, Spawns.spawns.id);
		public static final org.jooq.UniqueKey<StatEventsRecord> KEY_statEvents_PRIMARY = createUniqueKey(StatEvents.statEvents, StatEvents.statEvents.eventId);
		public static final org.jooq.UniqueKey<StatEventsRecord> KEY_statEvents_UK_DailyEntry = createUniqueKey(StatEvents.statEvents, StatEvents.statEvents.accountId, StatEvents.statEvents.date, StatEvents.statEvents.gamemode, StatEvents.statEvents.serverGroup, StatEvents.statEvents.type);
		public static final org.jooq.UniqueKey<StatsRecord> KEY_stats_PRIMARY = createUniqueKey(Stats.stats, Stats.stats.id);
		public static final org.jooq.UniqueKey<StatsRecord> KEY_stats_nameIndex = createUniqueKey(Stats.stats, Stats.stats.name);
		public static final org.jooq.UniqueKey<StatTypesRecord> KEY_statTypes_PRIMARY = createUniqueKey(StatTypes.statTypes, StatTypes.statTypes.id);
		public static final org.jooq.UniqueKey<StatTypesRecord> KEY_statTypes_name = createUniqueKey(StatTypes.statTypes, StatTypes.statTypes.name);
		public static final org.jooq.UniqueKey<TasksRecord> KEY_tasks_PRIMARY = createUniqueKey(Tasks.tasks, Tasks.tasks.id);
		public static final org.jooq.UniqueKey<TasksRecord> KEY_tasks_name_UNIQUE = createUniqueKey(Tasks.tasks, Tasks.tasks.name);
		public static final org.jooq.UniqueKey<TournamentLBRecord> KEY_TournamentLB_PRIMARY = createUniqueKey(TournamentLB.TournamentLB, TournamentLB.TournamentLB.rank);
		public static final org.jooq.UniqueKey<TournamentLBRecord> KEY_TournamentLB_UK_PlayerEntry = createUniqueKey(TournamentLB.TournamentLB, TournamentLB.TournamentLB.accountId);
		public static final org.jooq.UniqueKey<TransactionsRecord> KEY_transactions_PRIMARY = createUniqueKey(Transactions.transactions, Transactions.transactions.id);
	}

	private static class ForeignKeys0 extends org.jooq.impl.AbstractKeys {
		public static final org.jooq.ForeignKey<AccountClanRecord, AccountsRecord> FK_accountClan_accounts = createForeignKey(Keys.KEY_accounts_PRIMARY, AccountClan.accountClan, AccountClan.accountClan.accountId);
		public static final org.jooq.ForeignKey<AccountClanRecord, ClansRecord> FK_accountClan_clans2 = createForeignKey(Keys.KEY_clans_PRIMARY, AccountClan.accountClan, AccountClan.accountClan.clanId);
		public static final org.jooq.ForeignKey<AccountCoinTransactionsRecord, AccountsRecord> FK_ACT_ACCOUNTS_ID = createForeignKey(Keys.KEY_accounts_PRIMARY, AccountCoinTransactions.accountCoinTransactions, AccountCoinTransactions.accountCoinTransactions.accountId);
		public static final org.jooq.ForeignKey<AccountGemTransactionsRecord, AccountsRecord> accountGemTransactions_ibfk_1 = createForeignKey(Keys.KEY_accounts_uuidIndex, AccountGemTransactions.accountGemTransactions, AccountGemTransactions.accountGemTransactions.accounts_uuid);
		public static final org.jooq.ForeignKey<AccountInventoryRecord, AccountsRecord> accountInventory_ibfk_1 = createForeignKey(Keys.KEY_accounts_PRIMARY, AccountInventory.accountInventory, AccountInventory.accountInventory.accountId);
		public static final org.jooq.ForeignKey<AccountInventoryRecord, ItemsRecord> accountInventory_ibfk_2 = createForeignKey(Keys.KEY_items_PRIMARY, AccountInventory.accountInventory, AccountInventory.accountInventory.itemId);
		public static final org.jooq.ForeignKey<AccountPollsRecord, AccountsRecord> accountPolls_ibfk_1 = createForeignKey(Keys.KEY_accounts_PRIMARY, AccountPolls.accountPolls, AccountPolls.accountPolls.accountId);
		public static final org.jooq.ForeignKey<AccountPollsRecord, PollsRecord> accountPolls_ibfk_2 = createForeignKey(Keys.KEY_polls_PRIMARY, AccountPolls.accountPolls, AccountPolls.accountPolls.pollId);
		public static final org.jooq.ForeignKey<AccountStatRecord, AccountsRecord> accountStat_account = createForeignKey(Keys.KEY_accounts_PRIMARY, AccountStat.accountStat, AccountStat.accountStat.accountId);
		public static final org.jooq.ForeignKey<AccountStatRecord, StatsRecord> accountStat_stat = createForeignKey(Keys.KEY_stats_PRIMARY, AccountStat.accountStat, AccountStat.accountStat.statId);
		public static final org.jooq.ForeignKey<AccountStatsRecord, AccountsRecord> accountStats_ibfk_1 = createForeignKey(Keys.KEY_accounts_PRIMARY, AccountStats.accountStats, AccountStats.accountStats.accountId);
		public static final org.jooq.ForeignKey<AccountStatsRecord, StatsRecord> accountStats_ibfk_2 = createForeignKey(Keys.KEY_stats_PRIMARY, AccountStats.accountStats, AccountStats.accountStats.statId);
		public static final org.jooq.ForeignKey<AccountTasksRecord, AccountsRecord> ACCOUNTTASKS_ACCOUNTID = createForeignKey(Keys.KEY_accounts_PRIMARY, AccountTasks.accountTasks, AccountTasks.accountTasks.accountId);
		public static final org.jooq.ForeignKey<AccountTasksRecord, TasksRecord> ACCOUNTTASKS_TASKID = createForeignKey(Keys.KEY_tasks_PRIMARY, AccountTasks.accountTasks, AccountTasks.accountTasks.taskId);
		public static final org.jooq.ForeignKey<AccountTransactionsRecord, AccountsRecord> ACCOUNTTRANSACTIONS_ACCOUNTID = createForeignKey(Keys.KEY_accounts_PRIMARY, AccountTransactions.accountTransactions, AccountTransactions.accountTransactions.accountId);
		public static final org.jooq.ForeignKey<AccountTransactionsRecord, TransactionsRecord> ACCOUNTTRANSACTIONS_TRANSACTIONID = createForeignKey(Keys.KEY_transactions_PRIMARY, AccountTransactions.accountTransactions, AccountTransactions.accountTransactions.transactionId);
		public static final org.jooq.ForeignKey<BonusRecord, AccountsRecord> bonus_ibfk_1 = createForeignKey(Keys.KEY_accounts_PRIMARY, Bonus.bonus, Bonus.bonus.accountId);
		public static final org.jooq.ForeignKey<ClanAlliancesRecord, ClansRecord> clanAlliances_ibfk_2 = createForeignKey(Keys.KEY_clans_PRIMARY, ClanAlliances.clanAlliances, ClanAlliances.clanAlliances.clanId);
		public static final org.jooq.ForeignKey<ClanAlliancesRecord, ClansRecord> clanAlliances_ibfk_1 = createForeignKey(Keys.KEY_clans_PRIMARY, ClanAlliances.clanAlliances, ClanAlliances.clanAlliances.otherClanId);
		public static final org.jooq.ForeignKey<ClanEnemiesRecord, ClansRecord> clanEnemies_ibfk_1 = createForeignKey(Keys.KEY_clans_PRIMARY, ClanEnemies.clanEnemies, ClanEnemies.clanEnemies.clanId);
		public static final org.jooq.ForeignKey<ClanEnemiesRecord, ClansRecord> clanEnemies_ibfk_2 = createForeignKey(Keys.KEY_clans_PRIMARY, ClanEnemies.clanEnemies, ClanEnemies.clanEnemies.otherClanId);
		public static final org.jooq.ForeignKey<ClansRecord, ClanServerRecord> clans_ibfk_1 = createForeignKey(Keys.KEY_clanServer_PRIMARY, Clans.clans, Clans.clans.serverId);
		public static final org.jooq.ForeignKey<ClanTerritoryRecord, ClansRecord> clanTerritory_ibfk_1 = createForeignKey(Keys.KEY_clans_PRIMARY, ClanTerritory.clanTerritory, ClanTerritory.clanTerritory.clanId);
		public static final org.jooq.ForeignKey<ClanTerritoryRecord, ClanServerRecord> clanTerritory_ibfk_2 = createForeignKey(Keys.KEY_clanServer_PRIMARY, ClanTerritory.clanTerritory, ClanTerritory.clanTerritory.serverId);
		public static final org.jooq.ForeignKey<ItemsRecord, ItemCategoriesRecord> items_ibfk_1 = createForeignKey(Keys.KEY_itemCategories_PRIMARY, Items.items, Items.items.categoryId);
		public static final org.jooq.ForeignKey<MailRecord, AccountsRecord> mail_ibfk_1 = createForeignKey(Keys.KEY_accounts_PRIMARY, Mail.mail, Mail.mail.accountId);
		public static final org.jooq.ForeignKey<MailboxRecord, AccountsRecord> mailbox_ibfk_1 = createForeignKey(Keys.KEY_accounts_PRIMARY, Mailbox.mailbox, Mailbox.mailbox.accountId);
		public static final org.jooq.ForeignKey<RankBenefitsRecord, AccountsRecord> rankBenefits_ibfk_1 = createForeignKey(Keys.KEY_accounts_PRIMARY, RankBenefits.rankBenefits, RankBenefits.rankBenefits.accountId);
	}
}