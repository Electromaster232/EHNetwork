/**
 * This class is generated by jOOQ
 */
package ehnetwork.database;

import ehnetwork.database.routines.AccountGoldChange;
import ehnetwork.database.routines.Check_daily;
import ehnetwork.database.routines.Check_rank;
import ehnetwork.database.routines.CreateLeaderboard;
import ehnetwork.database.routines.HeidiSQL_temproutine_1;
import ehnetwork.database.routines.HeidiSQL_temproutine_2;
import ehnetwork.database.routines.InsertStatEvent;
import ehnetwork.database.routines.PlayerLogin;
import ehnetwork.database.routines.SwapTableNames;
import ehnetwork.database.routines.Test;
import ehnetwork.database.routines.TestPro;
import ehnetwork.database.routines.UpdateTournament;
import ehnetwork.database.routines.UpdateTournaments;

/**
 * Convenience access to all stored procedures and functions in Account
 */
@javax.annotation.Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.5.2"
	},
	comments = "This class is generated by jOOQ"
)
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Routines {

	/**
	 * Call <code>Account.accountGoldChange</code>
	 */
	public static java.lang.Byte callAccountgoldchange(org.jooq.Configuration configuration, java.lang.Integer accountId, java.lang.Integer goldChange) {
		AccountGoldChange p = new AccountGoldChange();
		p.setAccountId(accountId);
		p.setGoldChange(goldChange);

		p.execute(configuration);
		return p.getSuccess();
	}

	/**
	 * Call <code>Account.check_daily</code>
	 */
	public static Check_daily callCheckDaily(org.jooq.Configuration configuration, java.lang.Integer accountId_in, java.lang.Integer coinsChange, java.lang.Integer gemsChange) {
		Check_daily p = new Check_daily();
		p.setAccountId_in(accountId_in);
		p.setCoinsChange(coinsChange);
		p.setGemsChange(gemsChange);

		p.execute(configuration);
		return p;
	}

	/**
	 * Call <code>Account.check_rank</code>
	 */
	public static Check_rank callCheckRank(org.jooq.Configuration configuration, java.lang.Integer accountId_in, java.lang.Integer coinsChange, java.lang.Integer gemsChange) {
		Check_rank p = new Check_rank();
		p.setAccountId_in(accountId_in);
		p.setCoinsChange(coinsChange);
		p.setGemsChange(gemsChange);

		p.execute(configuration);
		return p;
	}

	/**
	 * Call <code>Account.createLeaderboard</code>
	 */
	public static void callCreateleaderboard(org.jooq.Configuration configuration, java.lang.String tableName) {
		CreateLeaderboard p = new CreateLeaderboard();
		p.setTableName(tableName);

		p.execute(configuration);
	}

	/**
	 * Call <code>Account.HeidiSQL_temproutine_1</code>
	 */
	public static void callHeidisqlTemproutine_1(org.jooq.Configuration configuration, java.lang.Integer statAccountID, java.sql.Date statDate, java.lang.Byte statGamemode, java.lang.String statServerGroup, java.lang.Byte statType, java.lang.Short statValue) {
		HeidiSQL_temproutine_1 p = new HeidiSQL_temproutine_1();
		p.setStatAccountID(statAccountID);
		p.setStatDate(statDate);
		p.setStatGamemode(statGamemode);
		p.setStatServerGroup(statServerGroup);
		p.setStatType(statType);
		p.setStatValue(statValue);

		p.execute(configuration);
	}

	/**
	 * Call <code>Account.HeidiSQL_temproutine_2</code>
	 */
	public static void callHeidisqlTemproutine_2(org.jooq.Configuration configuration, java.lang.Integer statAccountID, java.sql.Date statDate, java.lang.Byte statGamemode, java.lang.String statServerGroup, java.lang.Byte statType, java.lang.Short statValue) {
		HeidiSQL_temproutine_2 p = new HeidiSQL_temproutine_2();
		p.setStatAccountID(statAccountID);
		p.setStatDate(statDate);
		p.setStatGamemode(statGamemode);
		p.setStatServerGroup(statServerGroup);
		p.setStatType(statType);
		p.setStatValue(statValue);

		p.execute(configuration);
	}

	/**
	 * Call <code>Account.insertStatEvent</code>
	 */
	public static void callInsertstatevent(org.jooq.Configuration configuration, java.lang.Integer statAccountID, java.sql.Date statDate, java.lang.Byte statGamemode, java.lang.String statServerGroup, java.lang.Byte statType, java.lang.Short statValue) {
		InsertStatEvent p = new InsertStatEvent();
		p.setStatAccountID(statAccountID);
		p.setStatDate(statDate);
		p.setStatGamemode(statGamemode);
		p.setStatServerGroup(statServerGroup);
		p.setStatType(statType);
		p.setStatValue(statValue);

		p.execute(configuration);
	}

	/**
	 * Call <code>Account.playerLogin</code>
	 */
	public static void callPlayerlogin(org.jooq.Configuration configuration, java.lang.String paramUuid, java.lang.String paramName) {
		PlayerLogin p = new PlayerLogin();
		p.setParamUuid(paramUuid);
		p.setParamName(paramName);

		p.execute(configuration);
	}

	/**
	 * Call <code>Account.swapTableNames</code>
	 */
	public static void callSwaptablenames(org.jooq.Configuration configuration, java.lang.String tableName1, java.lang.String tableName2) {
		SwapTableNames p = new SwapTableNames();
		p.setTableName1(tableName1);
		p.setTableName2(tableName2);

		p.execute(configuration);
	}

	/**
	 * Call <code>Account.test</code>
	 */
	public static void callTest(org.jooq.Configuration configuration, java.lang.String playerUuid, java.lang.String playerName) {
		Test p = new Test();
		p.setPlayerUuid(playerUuid);
		p.setPlayerName(playerName);

		p.execute(configuration);
	}

	/**
	 * Call <code>Account.testPro</code>
	 */
	public static java.sql.Date callTestpro(org.jooq.Configuration configuration) {
		TestPro p = new TestPro();

		p.execute(configuration);
		return p.getVar();
	}

	/**
	 * Call <code>Account.updateTournament</code>
	 */
	public static void callUpdatetournament(org.jooq.Configuration configuration, java.lang.String tourneyName) {
		UpdateTournament p = new UpdateTournament();
		p.setTourneyName(tourneyName);

		p.execute(configuration);
	}

	/**
	 * Call <code>Account.updateTournaments</code>
	 */
	public static void callUpdatetournaments(org.jooq.Configuration configuration) {
		UpdateTournaments p = new UpdateTournaments();

		p.execute(configuration);
	}
}
