/**
 * This class is generated by jOOQ
 */
package ehnetwork.database.tables;

import ehnetwork.database.Account;
import ehnetwork.database.Keys;
import ehnetwork.database.tables.records.ClanTerritoryRecord;

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
public class ClanTerritory extends org.jooq.impl.TableImpl<ClanTerritoryRecord> implements java.io.Serializable, java.lang.Cloneable {

	private static final long serialVersionUID = -1393369389;

	/**
	 * The reference instance of <code>Account.clanTerritory</code>
	 */
	public static final ClanTerritory clanTerritory = new ClanTerritory();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<ClanTerritoryRecord> getRecordType() {
		return ClanTerritoryRecord.class;
	}

	/**
	 * The column <code>Account.clanTerritory.id</code>.
	 */
	public final org.jooq.TableField<ClanTerritoryRecord, java.lang.Integer> id = createField("id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>Account.clanTerritory.clanId</code>.
	 */
	public final org.jooq.TableField<ClanTerritoryRecord, java.lang.Integer> clanId = createField("clanId", org.jooq.impl.SQLDataType.INTEGER, this, "");

	/**
	 * The column <code>Account.clanTerritory.serverId</code>.
	 */
	public final org.jooq.TableField<ClanTerritoryRecord, java.lang.Integer> serverId = createField("serverId", org.jooq.impl.SQLDataType.INTEGER, this, "");

	/**
	 * The column <code>Account.clanTerritory.chunk</code>.
	 */
	public final org.jooq.TableField<ClanTerritoryRecord, java.lang.String> chunk = createField("chunk", org.jooq.impl.SQLDataType.VARCHAR.length(100), this, "");

	/**
	 * The column <code>Account.clanTerritory.safe</code>.
	 */
	public final org.jooq.TableField<ClanTerritoryRecord, java.lang.Boolean> safe = createField("safe", org.jooq.impl.SQLDataType.BIT, this, "");

	/**
	 * Create a <code>Account.clanTerritory</code> table reference
	 */
	public ClanTerritory() {
		this("clanTerritory", null);
	}

	/**
	 * Create an aliased <code>Account.clanTerritory</code> table reference
	 */
	public ClanTerritory(java.lang.String alias) {
		this(alias, ClanTerritory.clanTerritory);
	}

	private ClanTerritory(java.lang.String alias, org.jooq.Table<ClanTerritoryRecord> aliased) {
		this(alias, aliased, null);
	}

	private ClanTerritory(java.lang.String alias, org.jooq.Table<ClanTerritoryRecord> aliased, org.jooq.Field<?>[] parameters) {
		super(alias, Account.Account, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Identity<ClanTerritoryRecord, java.lang.Integer> getIdentity() {
		return Keys.IDENTITY_clanTerritory;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.UniqueKey<ClanTerritoryRecord> getPrimaryKey() {
		return Keys.KEY_clanTerritory_PRIMARY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.UniqueKey<ClanTerritoryRecord>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<ClanTerritoryRecord>>asList(Keys.KEY_clanTerritory_PRIMARY, Keys.KEY_clanTerritory_territory_server_chunk_UNIQUE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.ForeignKey<ClanTerritoryRecord, ?>> getReferences() {
		return java.util.Arrays.<org.jooq.ForeignKey<ClanTerritoryRecord, ?>>asList(Keys.clanTerritory_ibfk_1, Keys.clanTerritory_ibfk_2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ClanTerritory as(java.lang.String alias) {
		return new ClanTerritory(alias, this);
	}

	/**
	 * Rename this table
	 */
	public ClanTerritory rename(java.lang.String name) {
		return new ClanTerritory(name, null);
	}
}