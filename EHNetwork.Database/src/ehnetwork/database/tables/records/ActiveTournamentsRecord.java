/**
 * This class is generated by jOOQ
 */
package ehnetwork.database.tables.records;

import ehnetwork.database.tables.ActiveTournaments;

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
public class ActiveTournamentsRecord extends org.jooq.impl.UpdatableRecordImpl<ActiveTournamentsRecord> implements java.io.Serializable, java.lang.Cloneable, org.jooq.Record5<java.lang.String, java.sql.Date, java.sql.Date, java.lang.Integer, org.jooq.types.UByte> {

	private static final long serialVersionUID = -1633347474;

	/**
	 * Setter for <code>Account.activeTournaments.name</code>.
	 */
	public void setName(java.lang.String value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>Account.activeTournaments.name</code>.
	 */
	public java.lang.String getName() {
		return (java.lang.String) getValue(0);
	}

	/**
	 * Setter for <code>Account.activeTournaments.start_date</code>.
	 */
	public void setStart_date(java.sql.Date value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>Account.activeTournaments.start_date</code>.
	 */
	public java.sql.Date getStart_date() {
		return (java.sql.Date) getValue(1);
	}

	/**
	 * Setter for <code>Account.activeTournaments.end_date</code>.
	 */
	public void setEnd_date(java.sql.Date value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>Account.activeTournaments.end_date</code>.
	 */
	public java.sql.Date getEnd_date() {
		return (java.sql.Date) getValue(2);
	}

	/**
	 * Setter for <code>Account.activeTournaments.is_gamemode</code>.
	 */
	public void setIs_gamemode(java.lang.Integer value) {
		setValue(3, value);
	}

	/**
	 * Getter for <code>Account.activeTournaments.is_gamemode</code>.
	 */
	public java.lang.Integer getIs_gamemode() {
		return (java.lang.Integer) getValue(3);
	}

	/**
	 * Setter for <code>Account.activeTournaments.server_id</code>.
	 */
	public void setServer_id(org.jooq.types.UByte value) {
		setValue(4, value);
	}

	/**
	 * Getter for <code>Account.activeTournaments.server_id</code>.
	 */
	public org.jooq.types.UByte getServer_id() {
		return (org.jooq.types.UByte) getValue(4);
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Record1<java.lang.String> key() {
		return (org.jooq.Record1) super.key();
	}

	// -------------------------------------------------------------------------
	// Record5 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row5<java.lang.String, java.sql.Date, java.sql.Date, java.lang.Integer, org.jooq.types.UByte> fieldsRow() {
		return (org.jooq.Row5) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row5<java.lang.String, java.sql.Date, java.sql.Date, java.lang.Integer, org.jooq.types.UByte> valuesRow() {
		return (org.jooq.Row5) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field1() {
		return ActiveTournaments.activeTournaments.name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.sql.Date> field2() {
		return ActiveTournaments.activeTournaments.start_date;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.sql.Date> field3() {
		return ActiveTournaments.activeTournaments.end_date;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field4() {
		return ActiveTournaments.activeTournaments.is_gamemode;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<org.jooq.types.UByte> field5() {
		return ActiveTournaments.activeTournaments.server_id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value1() {
		return getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.sql.Date value2() {
		return getStart_date();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.sql.Date value3() {
		return getEnd_date();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value4() {
		return getIs_gamemode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.types.UByte value5() {
		return getServer_id();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ActiveTournamentsRecord value1(java.lang.String value) {
		setName(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ActiveTournamentsRecord value2(java.sql.Date value) {
		setStart_date(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ActiveTournamentsRecord value3(java.sql.Date value) {
		setEnd_date(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ActiveTournamentsRecord value4(java.lang.Integer value) {
		setIs_gamemode(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ActiveTournamentsRecord value5(org.jooq.types.UByte value) {
		setServer_id(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ActiveTournamentsRecord values(java.lang.String value1, java.sql.Date value2, java.sql.Date value3, java.lang.Integer value4, org.jooq.types.UByte value5) {
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached ActiveTournamentsRecord
	 */
	public ActiveTournamentsRecord() {
		super(ActiveTournaments.activeTournaments);
	}

	/**
	 * Create a detached, initialised ActiveTournamentsRecord
	 */
	public ActiveTournamentsRecord(java.lang.String name, java.sql.Date start_date, java.sql.Date end_date, java.lang.Integer is_gamemode, org.jooq.types.UByte server_id) {
		super(ActiveTournaments.activeTournaments);

		setValue(0, name);
		setValue(1, start_date);
		setValue(2, end_date);
		setValue(3, is_gamemode);
		setValue(4, server_id);
	}
}
