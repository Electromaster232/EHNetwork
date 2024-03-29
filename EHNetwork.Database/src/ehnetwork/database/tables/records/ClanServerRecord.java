/**
 * This class is generated by jOOQ
 */
package ehnetwork.database.tables.records;

import ehnetwork.database.tables.ClanServer;

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
public class ClanServerRecord extends org.jooq.impl.UpdatableRecordImpl<ClanServerRecord> implements java.io.Serializable, java.lang.Cloneable, org.jooq.Record2<java.lang.Integer, java.lang.String> {

	private static final long serialVersionUID = -306321343;

	/**
	 * Setter for <code>Account.clanServer.id</code>.
	 */
	public void setId(java.lang.Integer value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>Account.clanServer.id</code>.
	 */
	public java.lang.Integer getId() {
		return (java.lang.Integer) getValue(0);
	}

	/**
	 * Setter for <code>Account.clanServer.serverName</code>.
	 */
	public void setServerName(java.lang.String value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>Account.clanServer.serverName</code>.
	 */
	public java.lang.String getServerName() {
		return (java.lang.String) getValue(1);
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Record2<java.lang.Integer, java.lang.String> key() {
		return (org.jooq.Record2) super.key();
	}

	// -------------------------------------------------------------------------
	// Record2 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row2<java.lang.Integer, java.lang.String> fieldsRow() {
		return (org.jooq.Row2) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row2<java.lang.Integer, java.lang.String> valuesRow() {
		return (org.jooq.Row2) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field1() {
		return ClanServer.clanServer.id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field2() {
		return ClanServer.clanServer.serverName;
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
	public java.lang.String value2() {
		return getServerName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ClanServerRecord value1(java.lang.Integer value) {
		setId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ClanServerRecord value2(java.lang.String value) {
		setServerName(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ClanServerRecord values(java.lang.Integer value1, java.lang.String value2) {
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached ClanServerRecord
	 */
	public ClanServerRecord() {
		super(ClanServer.clanServer);
	}

	/**
	 * Create a detached, initialised ClanServerRecord
	 */
	public ClanServerRecord(java.lang.Integer id, java.lang.String serverName) {
		super(ClanServer.clanServer);

		setValue(0, id);
		setValue(1, serverName);
	}
}
