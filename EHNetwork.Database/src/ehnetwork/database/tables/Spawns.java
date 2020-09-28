/**
 * This class is generated by jOOQ
 */
package ehnetwork.database.tables;

import ehnetwork.database.Account;
import ehnetwork.database.Keys;
import ehnetwork.database.tables.records.SpawnsRecord;

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
public class Spawns extends org.jooq.impl.TableImpl<SpawnsRecord> implements java.io.Serializable, java.lang.Cloneable {

	private static final long serialVersionUID = -220321018;

	/**
	 * The reference instance of <code>Account.spawns</code>
	 */
	public static final Spawns spawns = new Spawns();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<SpawnsRecord> getRecordType() {
		return SpawnsRecord.class;
	}

	/**
	 * The column <code>Account.spawns.id</code>.
	 */
	public final org.jooq.TableField<SpawnsRecord, java.lang.Integer> id = createField("id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>Account.spawns.serverName</code>.
	 */
	public final org.jooq.TableField<SpawnsRecord, java.lang.String> serverName = createField("serverName", org.jooq.impl.SQLDataType.VARCHAR.length(100), this, "");

	/**
	 * The column <code>Account.spawns.location</code>.
	 */
	public final org.jooq.TableField<SpawnsRecord, java.lang.String> location = createField("location", org.jooq.impl.SQLDataType.VARCHAR.length(100), this, "");

	/**
	 * Create a <code>Account.spawns</code> table reference
	 */
	public Spawns() {
		this("spawns", null);
	}

	/**
	 * Create an aliased <code>Account.spawns</code> table reference
	 */
	public Spawns(java.lang.String alias) {
		this(alias, Spawns.spawns);
	}

	private Spawns(java.lang.String alias, org.jooq.Table<SpawnsRecord> aliased) {
		this(alias, aliased, null);
	}

	private Spawns(java.lang.String alias, org.jooq.Table<SpawnsRecord> aliased, org.jooq.Field<?>[] parameters) {
		super(alias, Account.Account, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Identity<SpawnsRecord, java.lang.Integer> getIdentity() {
		return Keys.IDENTITY_spawns;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.UniqueKey<SpawnsRecord> getPrimaryKey() {
		return Keys.KEY_spawns_PRIMARY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.UniqueKey<SpawnsRecord>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<SpawnsRecord>>asList(Keys.KEY_spawns_PRIMARY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Spawns as(java.lang.String alias) {
		return new Spawns(alias, this);
	}

	/**
	 * Rename this table
	 */
	public Spawns rename(java.lang.String name) {
		return new Spawns(name, null);
	}
}