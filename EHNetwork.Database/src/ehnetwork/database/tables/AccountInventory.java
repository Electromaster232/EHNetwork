/**
 * This class is generated by jOOQ
 */
package ehnetwork.database.tables;

import ehnetwork.database.Account;
import ehnetwork.database.Keys;
import ehnetwork.database.tables.records.AccountInventoryRecord;

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
public class AccountInventory extends org.jooq.impl.TableImpl<AccountInventoryRecord> implements java.io.Serializable, java.lang.Cloneable {

	private static final long serialVersionUID = 1899953592;

	/**
	 * The reference instance of <code>Account.accountInventory</code>
	 */
	public static final AccountInventory accountInventory = new AccountInventory();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<AccountInventoryRecord> getRecordType() {
		return AccountInventoryRecord.class;
	}

	/**
	 * The column <code>Account.accountInventory.id</code>.
	 */
	public final org.jooq.TableField<AccountInventoryRecord, java.lang.Integer> id = createField("id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>Account.accountInventory.accountId</code>.
	 */
	public final org.jooq.TableField<AccountInventoryRecord, java.lang.Integer> accountId = createField("accountId", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>Account.accountInventory.itemId</code>.
	 */
	public final org.jooq.TableField<AccountInventoryRecord, java.lang.Integer> itemId = createField("itemId", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>Account.accountInventory.count</code>.
	 */
	public final org.jooq.TableField<AccountInventoryRecord, java.lang.Integer> count = createField("count", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * Create a <code>Account.accountInventory</code> table reference
	 */
	public AccountInventory() {
		this("accountInventory", null);
	}

	/**
	 * Create an aliased <code>Account.accountInventory</code> table reference
	 */
	public AccountInventory(java.lang.String alias) {
		this(alias, AccountInventory.accountInventory);
	}

	private AccountInventory(java.lang.String alias, org.jooq.Table<AccountInventoryRecord> aliased) {
		this(alias, aliased, null);
	}

	private AccountInventory(java.lang.String alias, org.jooq.Table<AccountInventoryRecord> aliased, org.jooq.Field<?>[] parameters) {
		super(alias, Account.Account, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Identity<AccountInventoryRecord, java.lang.Integer> getIdentity() {
		return Keys.IDENTITY_accountInventory;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.UniqueKey<AccountInventoryRecord> getPrimaryKey() {
		return Keys.KEY_accountInventory_PRIMARY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.UniqueKey<AccountInventoryRecord>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<AccountInventoryRecord>>asList(Keys.KEY_accountInventory_PRIMARY, Keys.KEY_accountInventory_accountItemIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.ForeignKey<AccountInventoryRecord, ?>> getReferences() {
		return java.util.Arrays.<org.jooq.ForeignKey<AccountInventoryRecord, ?>>asList(Keys.accountInventory_ibfk_1, Keys.accountInventory_ibfk_2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AccountInventory as(java.lang.String alias) {
		return new AccountInventory(alias, this);
	}

	/**
	 * Rename this table
	 */
	public AccountInventory rename(java.lang.String name) {
		return new AccountInventory(name, null);
	}
}
