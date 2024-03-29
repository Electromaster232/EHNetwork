/**
 * This class is generated by jOOQ
 */
package ehnetwork.database.tables.records;

import ehnetwork.database.tables.ClanEnemies;

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
public class ClanEnemiesRecord extends org.jooq.impl.UpdatableRecordImpl<ClanEnemiesRecord> implements java.io.Serializable, java.lang.Cloneable, org.jooq.Record9<java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Boolean, java.sql.Timestamp, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer> {

	private static final long serialVersionUID = -1319061144;

	/**
	 * Setter for <code>Account.clanEnemies.id</code>.
	 */
	public void setId(java.lang.Integer value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>Account.clanEnemies.id</code>.
	 */
	public java.lang.Integer getId() {
		return (java.lang.Integer) getValue(0);
	}

	/**
	 * Setter for <code>Account.clanEnemies.clanId</code>.
	 */
	public void setClanId(java.lang.Integer value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>Account.clanEnemies.clanId</code>.
	 */
	public java.lang.Integer getClanId() {
		return (java.lang.Integer) getValue(1);
	}

	/**
	 * Setter for <code>Account.clanEnemies.otherClanId</code>.
	 */
	public void setOtherClanId(java.lang.Integer value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>Account.clanEnemies.otherClanId</code>.
	 */
	public java.lang.Integer getOtherClanId() {
		return (java.lang.Integer) getValue(2);
	}

	/**
	 * Setter for <code>Account.clanEnemies.initiator</code>.
	 */
	public void setInitiator(java.lang.Boolean value) {
		setValue(3, value);
	}

	/**
	 * Getter for <code>Account.clanEnemies.initiator</code>.
	 */
	public java.lang.Boolean getInitiator() {
		return (java.lang.Boolean) getValue(3);
	}

	/**
	 * Setter for <code>Account.clanEnemies.timeFormed</code>.
	 */
	public void setTimeFormed(java.sql.Timestamp value) {
		setValue(4, value);
	}

	/**
	 * Getter for <code>Account.clanEnemies.timeFormed</code>.
	 */
	public java.sql.Timestamp getTimeFormed() {
		return (java.sql.Timestamp) getValue(4);
	}

	/**
	 * Setter for <code>Account.clanEnemies.clanScore</code>.
	 */
	public void setClanScore(java.lang.Integer value) {
		setValue(5, value);
	}

	/**
	 * Getter for <code>Account.clanEnemies.clanScore</code>.
	 */
	public java.lang.Integer getClanScore() {
		return (java.lang.Integer) getValue(5);
	}

	/**
	 * Setter for <code>Account.clanEnemies.otherClanScore</code>.
	 */
	public void setOtherClanScore(java.lang.Integer value) {
		setValue(6, value);
	}

	/**
	 * Getter for <code>Account.clanEnemies.otherClanScore</code>.
	 */
	public java.lang.Integer getOtherClanScore() {
		return (java.lang.Integer) getValue(6);
	}

	/**
	 * Setter for <code>Account.clanEnemies.clanKills</code>.
	 */
	public void setClanKills(java.lang.Integer value) {
		setValue(7, value);
	}

	/**
	 * Getter for <code>Account.clanEnemies.clanKills</code>.
	 */
	public java.lang.Integer getClanKills() {
		return (java.lang.Integer) getValue(7);
	}

	/**
	 * Setter for <code>Account.clanEnemies.otherClanKills</code>.
	 */
	public void setOtherClanKills(java.lang.Integer value) {
		setValue(8, value);
	}

	/**
	 * Getter for <code>Account.clanEnemies.otherClanKills</code>.
	 */
	public java.lang.Integer getOtherClanKills() {
		return (java.lang.Integer) getValue(8);
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
	// Record9 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row9<java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Boolean, java.sql.Timestamp, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer> fieldsRow() {
		return (org.jooq.Row9) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row9<java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Boolean, java.sql.Timestamp, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer> valuesRow() {
		return (org.jooq.Row9) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field1() {
		return ClanEnemies.clanEnemies.id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field2() {
		return ClanEnemies.clanEnemies.clanId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field3() {
		return ClanEnemies.clanEnemies.otherClanId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Boolean> field4() {
		return ClanEnemies.clanEnemies.initiator;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.sql.Timestamp> field5() {
		return ClanEnemies.clanEnemies.timeFormed;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field6() {
		return ClanEnemies.clanEnemies.clanScore;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field7() {
		return ClanEnemies.clanEnemies.otherClanScore;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field8() {
		return ClanEnemies.clanEnemies.clanKills;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field9() {
		return ClanEnemies.clanEnemies.otherClanKills;
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
		return getClanId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value3() {
		return getOtherClanId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Boolean value4() {
		return getInitiator();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.sql.Timestamp value5() {
		return getTimeFormed();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value6() {
		return getClanScore();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value7() {
		return getOtherClanScore();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value8() {
		return getClanKills();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value9() {
		return getOtherClanKills();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ClanEnemiesRecord value1(java.lang.Integer value) {
		setId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ClanEnemiesRecord value2(java.lang.Integer value) {
		setClanId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ClanEnemiesRecord value3(java.lang.Integer value) {
		setOtherClanId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ClanEnemiesRecord value4(java.lang.Boolean value) {
		setInitiator(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ClanEnemiesRecord value5(java.sql.Timestamp value) {
		setTimeFormed(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ClanEnemiesRecord value6(java.lang.Integer value) {
		setClanScore(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ClanEnemiesRecord value7(java.lang.Integer value) {
		setOtherClanScore(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ClanEnemiesRecord value8(java.lang.Integer value) {
		setClanKills(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ClanEnemiesRecord value9(java.lang.Integer value) {
		setOtherClanKills(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ClanEnemiesRecord values(java.lang.Integer value1, java.lang.Integer value2, java.lang.Integer value3, java.lang.Boolean value4, java.sql.Timestamp value5, java.lang.Integer value6, java.lang.Integer value7, java.lang.Integer value8, java.lang.Integer value9) {
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached ClanEnemiesRecord
	 */
	public ClanEnemiesRecord() {
		super(ClanEnemies.clanEnemies);
	}

	/**
	 * Create a detached, initialised ClanEnemiesRecord
	 */
	public ClanEnemiesRecord(java.lang.Integer id, java.lang.Integer clanId, java.lang.Integer otherClanId, java.lang.Boolean initiator, java.sql.Timestamp timeFormed, java.lang.Integer clanScore, java.lang.Integer otherClanScore, java.lang.Integer clanKills, java.lang.Integer otherClanKills) {
		super(ClanEnemies.clanEnemies);

		setValue(0, id);
		setValue(1, clanId);
		setValue(2, otherClanId);
		setValue(3, initiator);
		setValue(4, timeFormed);
		setValue(5, clanScore);
		setValue(6, otherClanScore);
		setValue(7, clanKills);
		setValue(8, otherClanKills);
	}
}
