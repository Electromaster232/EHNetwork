/**
 * This class is generated by jOOQ
 */
package ehnetwork.database.tables.records;

import ehnetwork.database.tables.TournamentLB;

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
public class TournamentLBRecord extends org.jooq.impl.UpdatableRecordImpl<TournamentLBRecord> implements java.io.Serializable, java.lang.Cloneable, org.jooq.Record3<org.jooq.types.UInteger, org.jooq.types.UInteger, org.jooq.types.UInteger> {

	private static final long serialVersionUID = 91822650;

	/**
	 * Setter for <code>Account.TournamentLB.rank</code>.
	 */
	public void setRank(org.jooq.types.UInteger value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>Account.TournamentLB.rank</code>.
	 */
	public org.jooq.types.UInteger getRank() {
		return (org.jooq.types.UInteger) getValue(0);
	}

	/**
	 * Setter for <code>Account.TournamentLB.accountId</code>.
	 */
	public void setAccountId(org.jooq.types.UInteger value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>Account.TournamentLB.accountId</code>.
	 */
	public org.jooq.types.UInteger getAccountId() {
		return (org.jooq.types.UInteger) getValue(1);
	}

	/**
	 * Setter for <code>Account.TournamentLB.value</code>.
	 */
	public void setValue(org.jooq.types.UInteger value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>Account.TournamentLB.value</code>.
	 */
	public org.jooq.types.UInteger getValue() {
		return (org.jooq.types.UInteger) getValue(2);
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Record1<org.jooq.types.UInteger> key() {
		return (org.jooq.Record1) super.key();
	}

	// -------------------------------------------------------------------------
	// Record3 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row3<org.jooq.types.UInteger, org.jooq.types.UInteger, org.jooq.types.UInteger> fieldsRow() {
		return (org.jooq.Row3) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row3<org.jooq.types.UInteger, org.jooq.types.UInteger, org.jooq.types.UInteger> valuesRow() {
		return (org.jooq.Row3) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<org.jooq.types.UInteger> field1() {
		return TournamentLB.TournamentLB.rank;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<org.jooq.types.UInteger> field2() {
		return TournamentLB.TournamentLB.accountId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<org.jooq.types.UInteger> field3() {
		return TournamentLB.TournamentLB.value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.types.UInteger value1() {
		return getRank();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.types.UInteger value2() {
		return getAccountId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.types.UInteger value3() {
		return getValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TournamentLBRecord value1(org.jooq.types.UInteger value) {
		setRank(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TournamentLBRecord value2(org.jooq.types.UInteger value) {
		setAccountId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TournamentLBRecord value3(org.jooq.types.UInteger value) {
		setValue(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TournamentLBRecord values(org.jooq.types.UInteger value1, org.jooq.types.UInteger value2, org.jooq.types.UInteger value3) {
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached TournamentLBRecord
	 */
	public TournamentLBRecord() {
		super(TournamentLB.TournamentLB);
	}

	/**
	 * Create a detached, initialised TournamentLBRecord
	 */
	public TournamentLBRecord(org.jooq.types.UInteger rank, org.jooq.types.UInteger accountId, org.jooq.types.UInteger value) {
		super(TournamentLB.TournamentLB);

		setValue(0, rank);
		setValue(1, accountId);
		setValue(2, value);
	}
}
