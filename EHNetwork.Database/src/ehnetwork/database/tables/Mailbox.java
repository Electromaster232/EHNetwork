/**
 * This class is generated by jOOQ
 */
package ehnetwork.database.tables;

import ehnetwork.database.Account;
import ehnetwork.database.Keys;
import ehnetwork.database.tables.records.MailboxRecord;

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
public class Mailbox extends org.jooq.impl.TableImpl<MailboxRecord> implements java.io.Serializable, java.lang.Cloneable {

	private static final long serialVersionUID = 581316209;

	/**
	 * The reference instance of <code>Account.mailbox</code>
	 */
	public static final Mailbox mailbox = new Mailbox();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<MailboxRecord> getRecordType() {
		return MailboxRecord.class;
	}

	/**
	 * The column <code>Account.mailbox.id</code>.
	 */
	public final org.jooq.TableField<MailboxRecord, java.lang.Integer> id = createField("id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>Account.mailbox.accountId</code>.
	 */
	public final org.jooq.TableField<MailboxRecord, java.lang.Integer> accountId = createField("accountId", org.jooq.impl.SQLDataType.INTEGER, this, "");

	/**
	 * The column <code>Account.mailbox.sender</code>.
	 */
	public final org.jooq.TableField<MailboxRecord, java.lang.String> sender = createField("sender", org.jooq.impl.SQLDataType.VARCHAR.length(64), this, "");

	/**
	 * The column <code>Account.mailbox.message</code>.
	 */
	public final org.jooq.TableField<MailboxRecord, java.lang.String> message = createField("message", org.jooq.impl.SQLDataType.VARCHAR.length(1024), this, "");

	/**
	 * The column <code>Account.mailbox.archived</code>.
	 */
	public final org.jooq.TableField<MailboxRecord, java.lang.Byte> archived = createField("archived", org.jooq.impl.SQLDataType.TINYINT, this, "");

	/**
	 * The column <code>Account.mailbox.deleted</code>.
	 */
	public final org.jooq.TableField<MailboxRecord, java.lang.Byte> deleted = createField("deleted", org.jooq.impl.SQLDataType.TINYINT, this, "");

	/**
	 * The column <code>Account.mailbox.timeSent</code>.
	 */
	public final org.jooq.TableField<MailboxRecord, java.sql.Timestamp> timeSent = createField("timeSent", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false).defaulted(true), this, "");

	/**
	 * Create a <code>Account.mailbox</code> table reference
	 */
	public Mailbox() {
		this("mailbox", null);
	}

	/**
	 * Create an aliased <code>Account.mailbox</code> table reference
	 */
	public Mailbox(java.lang.String alias) {
		this(alias, Mailbox.mailbox);
	}

	private Mailbox(java.lang.String alias, org.jooq.Table<MailboxRecord> aliased) {
		this(alias, aliased, null);
	}

	private Mailbox(java.lang.String alias, org.jooq.Table<MailboxRecord> aliased, org.jooq.Field<?>[] parameters) {
		super(alias, Account.Account, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Identity<MailboxRecord, java.lang.Integer> getIdentity() {
		return Keys.IDENTITY_mailbox;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.UniqueKey<MailboxRecord> getPrimaryKey() {
		return Keys.KEY_mailbox_PRIMARY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.UniqueKey<MailboxRecord>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<MailboxRecord>>asList(Keys.KEY_mailbox_PRIMARY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.ForeignKey<MailboxRecord, ?>> getReferences() {
		return java.util.Arrays.<org.jooq.ForeignKey<MailboxRecord, ?>>asList(Keys.mailbox_ibfk_1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Mailbox as(java.lang.String alias) {
		return new Mailbox(alias, this);
	}

	/**
	 * Rename this table
	 */
	public Mailbox rename(java.lang.String name) {
		return new Mailbox(name, null);
	}
}