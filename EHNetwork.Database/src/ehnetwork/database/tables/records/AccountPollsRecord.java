/**
 * This class is generated by jOOQ
 */
package ehnetwork.database.tables.records;

import ehnetwork.database.tables.AccountPolls;

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
public class AccountPollsRecord extends org.jooq.impl.UpdatableRecordImpl<AccountPollsRecord> implements java.io.Serializable, java.lang.Cloneable, org.jooq.Record4<java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Byte> {

	private static final long serialVersionUID = 1341641701;

	/**
	 * Setter for <code>Account.accountPolls.id</code>.
	 */
	public void setId(java.lang.Integer value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>Account.accountPolls.id</code>.
	 */
	public java.lang.Integer getId() {
		return (java.lang.Integer) getValue(0);
	}

	/**
	 * Setter for <code>Account.accountPolls.accountId</code>.
	 */
	public void setAccountId(java.lang.Integer value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>Account.accountPolls.accountId</code>.
	 */
	public java.lang.Integer getAccountId() {
		return (java.lang.Integer) getValue(1);
	}

	/**
	 * Setter for <code>Account.accountPolls.pollId</code>.
	 */
	public void setPollId(java.lang.Integer value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>Account.accountPolls.pollId</code>.
	 */
	public java.lang.Integer getPollId() {
		return (java.lang.Integer) getValue(2);
	}

	/**
	 * Setter for <code>Account.accountPolls.value</code>.
	 */
	public void setValue(java.lang.Byte value) {
		setValue(3, value);
	}

	/**
	 * Getter for <code>Account.accountPolls.value</code>.
	 */
	public java.lang.Byte getValue() {
		return (java.lang.Byte) getValue(3);
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Record1<java.lang.Integer> key() {
		return (org.jooq.Record1) super.key();
	}

	// -------------------------------------------------------------------------
	// Record4 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row4<java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Byte> fieldsRow() {
		return (org.jooq.Row4) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row4<java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Byte> valuesRow() {
		return (org.jooq.Row4) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field1() {
		return AccountPolls.accountPolls.id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field2() {
		return AccountPolls.accountPolls.accountId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field3() {
		return AccountPolls.accountPolls.pollId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Byte> field4() {
		return AccountPolls.accountPolls.value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value1() {
		return getId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value2() {
		return getAccountId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value3() {
		return getPollId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Byte value4() {
		return getValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AccountPollsRecord value1(java.lang.Integer value) {
		setId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AccountPollsRecord value2(java.lang.Integer value) {
		setAccountId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AccountPollsRecord value3(java.lang.Integer value) {
		setPollId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AccountPollsRecord value4(java.lang.Byte value) {
		setValue(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AccountPollsRecord values(java.lang.Integer value1, java.lang.Integer value2, java.lang.Integer value3, java.lang.Byte value4) {
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached AccountPollsRecord
	 */
	public AccountPollsRecord() {
		super(AccountPolls.accountPolls);
	}

	/**
	 * Create a detached, initialised AccountPollsRecord
	 */
	public AccountPollsRecord(java.lang.Integer id, java.lang.Integer accountId, java.lang.Integer pollId, java.lang.Byte value) {
		super(AccountPolls.accountPolls);

		setValue(0, id);
		setValue(1, accountId);
		setValue(2, pollId);
		setValue(3, value);
	}
}
