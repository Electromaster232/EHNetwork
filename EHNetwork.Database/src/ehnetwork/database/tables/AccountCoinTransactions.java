/**
 * This class is generated by jOOQ
 */
package ehnetwork.database.tables;

import ehnetwork.database.Account;
import ehnetwork.database.Keys;
import ehnetwork.database.tables.records.AccountCoinTransactionsRecord;

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
public class AccountCoinTransactions extends org.jooq.impl.TableImpl<AccountCoinTransactionsRecord> implements java.io.Serializable, java.lang.Cloneable {

	private static final long serialVersionUID = -205501041;

	/**
	 * The reference instance of <code>Account.accountCoinTransactions</code>
	 */
	public static final AccountCoinTransactions accountCoinTransactions = new AccountCoinTransactions();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<AccountCoinTransactionsRecord> getRecordType() {
		return AccountCoinTransactionsRecord.class;
	}

	/**
	 * The column <code>Account.accountCoinTransactions.id</code>.
	 */
	public final org.jooq.TableField<AccountCoinTransactionsRecord, java.lang.Integer> id = createField("id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>Account.accountCoinTransactions.accountId</code>.
	 */
	public final org.jooq.TableField<AccountCoinTransactionsRecord, java.lang.Integer> accountId = createField("accountId", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>Account.accountCoinTransactions.reason</code>.
	 */
	public final org.jooq.TableField<AccountCoinTransactionsRecord, java.lang.String> reason = createField("reason", org.jooq.impl.SQLDataType.VARCHAR.length(100), this, "");

	/**
	 * The column <code>Account.accountCoinTransactions.coins</code>.
	 */
	public final org.jooq.TableField<AccountCoinTransactionsRecord, java.lang.Integer> coins = createField("coins", org.jooq.impl.SQLDataType.INTEGER, this, "");

	/**
	 * Create a <code>Account.accountCoinTransactions</code> table reference
	 */
	public AccountCoinTransactions() {
		this("accountCoinTransactions", null);
	}

	/**
	 * Create an aliased <code>Account.accountCoinTransactions</code> table reference
	 */
	public AccountCoinTransactions(java.lang.String alias) {
		this(alias, AccountCoinTransactions.accountCoinTransactions);
	}

	private AccountCoinTransactions(java.lang.String alias, org.jooq.Table<AccountCoinTransactionsRecord> aliased) {
		this(alias, aliased, null);
	}

	private AccountCoinTransactions(java.lang.String alias, org.jooq.Table<AccountCoinTransactionsRecord> aliased, org.jooq.Field<?>[] parameters) {
		super(alias, Account.Account, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Identity<AccountCoinTransactionsRecord, java.lang.Integer> getIdentity() {
		return Keys.IDENTITY_accountCoinTransactions;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.UniqueKey<AccountCoinTransactionsRecord> getPrimaryKey() {
		return Keys.KEY_accountCoinTransactions_PRIMARY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.UniqueKey<AccountCoinTransactionsRecord>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<AccountCoinTransactionsRecord>>asList(Keys.KEY_accountCoinTransactions_PRIMARY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.ForeignKey<AccountCoinTransactionsRecord, ?>> getReferences() {
		return java.util.Arrays.<org.jooq.ForeignKey<AccountCoinTransactionsRecord, ?>>asList(Keys.FK_ACT_ACCOUNTS_ID);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AccountCoinTransactions as(java.lang.String alias) {
		return new AccountCoinTransactions(alias, this);
	}

	/**
	 * Rename this table
	 */
	public AccountCoinTransactions rename(java.lang.String name) {
		return new AccountCoinTransactions(name, null);
	}
}