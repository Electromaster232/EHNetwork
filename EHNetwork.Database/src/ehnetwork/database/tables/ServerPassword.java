/**
 * This class is generated by jOOQ
 */
package ehnetwork.database.tables;

import ehnetwork.database.Account;
import ehnetwork.database.Keys;
import ehnetwork.database.tables.records.ServerPasswordRecord;

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
public class ServerPassword extends org.jooq.impl.TableImpl<ServerPasswordRecord> implements java.io.Serializable, java.lang.Cloneable {

	private static final long serialVersionUID = -1801488583;

	/**
	 * The reference instance of <code>Account.serverPassword</code>
	 */
	public static final ServerPassword serverPassword = new ServerPassword();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<ServerPasswordRecord> getRecordType() {
		return ServerPasswordRecord.class;
	}

	/**
	 * The column <code>Account.serverPassword.id</code>.
	 */
	public final org.jooq.TableField<ServerPasswordRecord, java.lang.Integer> id = createField("id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>Account.serverPassword.server</code>.
	 */
	public final org.jooq.TableField<ServerPasswordRecord, java.lang.String> server = createField("server", org.jooq.impl.SQLDataType.VARCHAR.length(100), this, "");

	/**
	 * The column <code>Account.serverPassword.password</code>.
	 */
	public final org.jooq.TableField<ServerPasswordRecord, java.lang.String> password = createField("password", org.jooq.impl.SQLDataType.VARCHAR.length(100), this, "");

	/**
	 * Create a <code>Account.serverPassword</code> table reference
	 */
	public ServerPassword() {
		this("serverPassword", null);
	}

	/**
	 * Create an aliased <code>Account.serverPassword</code> table reference
	 */
	public ServerPassword(java.lang.String alias) {
		this(alias, ServerPassword.serverPassword);
	}

	private ServerPassword(java.lang.String alias, org.jooq.Table<ServerPasswordRecord> aliased) {
		this(alias, aliased, null);
	}

	private ServerPassword(java.lang.String alias, org.jooq.Table<ServerPasswordRecord> aliased, org.jooq.Field<?>[] parameters) {
		super(alias, Account.Account, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Identity<ServerPasswordRecord, java.lang.Integer> getIdentity() {
		return Keys.IDENTITY_serverPassword;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.UniqueKey<ServerPasswordRecord> getPrimaryKey() {
		return Keys.KEY_serverPassword_PRIMARY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.UniqueKey<ServerPasswordRecord>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<ServerPasswordRecord>>asList(Keys.KEY_serverPassword_PRIMARY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ServerPassword as(java.lang.String alias) {
		return new ServerPassword(alias, this);
	}

	/**
	 * Rename this table
	 */
	public ServerPassword rename(java.lang.String name) {
		return new ServerPassword(name, null);
	}
}
