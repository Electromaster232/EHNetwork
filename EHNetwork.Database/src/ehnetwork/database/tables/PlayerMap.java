/**
 * This class is generated by jOOQ
 */
package ehnetwork.database.tables;

import ehnetwork.database.Account;
import ehnetwork.database.Keys;
import ehnetwork.database.tables.records.PlayerMapRecord;

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.5.2"
	},
	comments = "This class is generated by jOOQ"
)
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class PlayerMap extends org.jooq.impl.TableImpl<PlayerMapRecord> implements java.io.Serializable, java.lang.Cloneable {

	private static final long serialVersionUID = -1399340256;

	/**
	 * The reference instance of <code>Account.playerMap</code>
	 */
	public static final PlayerMap playerMap = new PlayerMap();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<PlayerMapRecord> getRecordType() {
		return PlayerMapRecord.class;
	}

	/**
	 * The column <code>Account.playerMap.id</code>.
	 */
	public final org.jooq.TableField<PlayerMapRecord, java.lang.Integer> id = createField("id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>Account.playerMap.playerName</code>.
	 */
	public final org.jooq.TableField<PlayerMapRecord, java.lang.String> playerName = createField("playerName", org.jooq.impl.SQLDataType.VARCHAR.length(256), this, "");

	/**
	 * The column <code>Account.playerMap.serverName</code>.
	 */
	public final org.jooq.TableField<PlayerMapRecord, java.lang.String> serverName = createField("serverName", org.jooq.impl.SQLDataType.VARCHAR.length(256), this, "");

	/**
	 * The column <code>Account.playerMap.us</code>.
	 */
	public final org.jooq.TableField<PlayerMapRecord, java.lang.Byte> us = createField("us", org.jooq.impl.SQLDataType.TINYINT.nullable(false).defaulted(true), this, "");

	/**
	 * Create a <code>Account.playerMap</code> table reference
	 */
	public PlayerMap() {
		this("playerMap", null);
	}

	/**
	 * Create an aliased <code>Account.playerMap</code> table reference
	 */
	public PlayerMap(java.lang.String alias) {
		this(alias, PlayerMap.playerMap);
	}

	private PlayerMap(java.lang.String alias, org.jooq.Table<PlayerMapRecord> aliased) {
		this(alias, aliased, null);
	}

	private PlayerMap(java.lang.String alias, org.jooq.Table<PlayerMapRecord> aliased, org.jooq.Field<?>[] parameters) {
		super(alias, Account.Account, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Identity<PlayerMapRecord, java.lang.Integer> getIdentity() {
		return Keys.IDENTITY_playerMap;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.UniqueKey<PlayerMapRecord> getPrimaryKey() {
		return Keys.KEY_playerMap_PRIMARY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.UniqueKey<PlayerMapRecord>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<PlayerMapRecord>>asList(Keys.KEY_playerMap_PRIMARY, Keys.KEY_playerMap_playerIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PlayerMap as(java.lang.String alias) {
		return new PlayerMap(alias, this);
	}

	/**
	 * Rename this table
	 */
	public PlayerMap rename(java.lang.String name) {
		return new PlayerMap(name, null);
	}
}
