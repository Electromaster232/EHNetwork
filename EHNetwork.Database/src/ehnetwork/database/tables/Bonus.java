/**
 * This class is generated by jOOQ
 */
package ehnetwork.database.tables;

import ehnetwork.database.Account;
import ehnetwork.database.Keys;
import ehnetwork.database.tables.records.BonusRecord;

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
public class Bonus extends org.jooq.impl.TableImpl<BonusRecord> implements java.io.Serializable, java.lang.Cloneable {

	private static final long serialVersionUID = -70389593;

	/**
	 * The reference instance of <code>Account.bonus</code>
	 */
	public static final Bonus bonus = new Bonus();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<BonusRecord> getRecordType() {
		return BonusRecord.class;
	}

	/**
	 * The column <code>Account.bonus.accountId</code>.
	 */
	public final org.jooq.TableField<BonusRecord, java.lang.Integer> accountId = createField("accountId", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>Account.bonus.dailytime</code>.
	 */
	public final org.jooq.TableField<BonusRecord, java.sql.Timestamp> dailytime = createField("dailytime", org.jooq.impl.SQLDataType.TIMESTAMP, this, "");

	/**
	 * The column <code>Account.bonus.ranktime</code>.
	 */
	public final org.jooq.TableField<BonusRecord, java.sql.Date> ranktime = createField("ranktime", org.jooq.impl.SQLDataType.DATE, this, "");

	/**
	 * Create a <code>Account.bonus</code> table reference
	 */
	public Bonus() {
		this("bonus", null);
	}

	/**
	 * Create an aliased <code>Account.bonus</code> table reference
	 */
	public Bonus(java.lang.String alias) {
		this(alias, Bonus.bonus);
	}

	private Bonus(java.lang.String alias, org.jooq.Table<BonusRecord> aliased) {
		this(alias, aliased, null);
	}

	private Bonus(java.lang.String alias, org.jooq.Table<BonusRecord> aliased, org.jooq.Field<?>[] parameters) {
		super(alias, Account.Account, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Identity<BonusRecord, java.lang.Integer> getIdentity() {
		return Keys.IDENTITY_bonus;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.UniqueKey<BonusRecord> getPrimaryKey() {
		return Keys.KEY_bonus_PRIMARY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.UniqueKey<BonusRecord>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<BonusRecord>>asList(Keys.KEY_bonus_PRIMARY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.ForeignKey<BonusRecord, ?>> getReferences() {
		return java.util.Arrays.<org.jooq.ForeignKey<BonusRecord, ?>>asList(Keys.bonus_ibfk_1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Bonus as(java.lang.String alias) {
		return new Bonus(alias, this);
	}

	/**
	 * Rename this table
	 */
	public Bonus rename(java.lang.String name) {
		return new Bonus(name, null);
	}
}