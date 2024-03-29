/**
 * This class is generated by jOOQ
 */
package ehnetwork.database.tables.records;

import ehnetwork.database.tables.FieldMonster;

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
public class FieldMonsterRecord extends org.jooq.impl.UpdatableRecordImpl<FieldMonsterRecord> implements java.io.Serializable, java.lang.Cloneable, org.jooq.Record9<java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Double, java.lang.String, java.lang.Integer, java.lang.Integer> {

	private static final long serialVersionUID = 38794932;

	/**
	 * Setter for <code>Account.fieldMonster.id</code>.
	 */
	public void setId(java.lang.Integer value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>Account.fieldMonster.id</code>.
	 */
	public java.lang.Integer getId() {
		return (java.lang.Integer) getValue(0);
	}

	/**
	 * Setter for <code>Account.fieldMonster.server</code>.
	 */
	public void setServer(java.lang.String value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>Account.fieldMonster.server</code>.
	 */
	public java.lang.String getServer() {
		return (java.lang.String) getValue(1);
	}

	/**
	 * Setter for <code>Account.fieldMonster.name</code>.
	 */
	public void setName(java.lang.String value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>Account.fieldMonster.name</code>.
	 */
	public java.lang.String getName() {
		return (java.lang.String) getValue(2);
	}

	/**
	 * Setter for <code>Account.fieldMonster.type</code>.
	 */
	public void setType(java.lang.String value) {
		setValue(3, value);
	}

	/**
	 * Getter for <code>Account.fieldMonster.type</code>.
	 */
	public java.lang.String getType() {
		return (java.lang.String) getValue(3);
	}

	/**
	 * Setter for <code>Account.fieldMonster.mobMax</code>.
	 */
	public void setMobMax(java.lang.Integer value) {
		setValue(4, value);
	}

	/**
	 * Getter for <code>Account.fieldMonster.mobMax</code>.
	 */
	public java.lang.Integer getMobMax() {
		return (java.lang.Integer) getValue(4);
	}

	/**
	 * Setter for <code>Account.fieldMonster.mobRate</code>.
	 */
	public void setMobRate(java.lang.Double value) {
		setValue(5, value);
	}

	/**
	 * Getter for <code>Account.fieldMonster.mobRate</code>.
	 */
	public java.lang.Double getMobRate() {
		return (java.lang.Double) getValue(5);
	}

	/**
	 * Setter for <code>Account.fieldMonster.center</code>.
	 */
	public void setCenter(java.lang.String value) {
		setValue(6, value);
	}

	/**
	 * Getter for <code>Account.fieldMonster.center</code>.
	 */
	public java.lang.String getCenter() {
		return (java.lang.String) getValue(6);
	}

	/**
	 * Setter for <code>Account.fieldMonster.radius</code>.
	 */
	public void setRadius(java.lang.Integer value) {
		setValue(7, value);
	}

	/**
	 * Getter for <code>Account.fieldMonster.radius</code>.
	 */
	public java.lang.Integer getRadius() {
		return (java.lang.Integer) getValue(7);
	}

	/**
	 * Setter for <code>Account.fieldMonster.height</code>.
	 */
	public void setHeight(java.lang.Integer value) {
		setValue(8, value);
	}

	/**
	 * Getter for <code>Account.fieldMonster.height</code>.
	 */
	public java.lang.Integer getHeight() {
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
	public org.jooq.Row9<java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Double, java.lang.String, java.lang.Integer, java.lang.Integer> fieldsRow() {
		return (org.jooq.Row9) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row9<java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Double, java.lang.String, java.lang.Integer, java.lang.Integer> valuesRow() {
		return (org.jooq.Row9) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field1() {
		return FieldMonster.fieldMonster.id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field2() {
		return FieldMonster.fieldMonster.server;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field3() {
		return FieldMonster.fieldMonster.name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field4() {
		return FieldMonster.fieldMonster.type;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field5() {
		return FieldMonster.fieldMonster.mobMax;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Double> field6() {
		return FieldMonster.fieldMonster.mobRate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field7() {
		return FieldMonster.fieldMonster.center;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field8() {
		return FieldMonster.fieldMonster.radius;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field9() {
		return FieldMonster.fieldMonster.height;
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
		return getServer();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value3() {
		return getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value4() {
		return getType();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value5() {
		return getMobMax();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Double value6() {
		return getMobRate();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value7() {
		return getCenter();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value8() {
		return getRadius();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value9() {
		return getHeight();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FieldMonsterRecord value1(java.lang.Integer value) {
		setId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FieldMonsterRecord value2(java.lang.String value) {
		setServer(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FieldMonsterRecord value3(java.lang.String value) {
		setName(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FieldMonsterRecord value4(java.lang.String value) {
		setType(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FieldMonsterRecord value5(java.lang.Integer value) {
		setMobMax(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FieldMonsterRecord value6(java.lang.Double value) {
		setMobRate(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FieldMonsterRecord value7(java.lang.String value) {
		setCenter(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FieldMonsterRecord value8(java.lang.Integer value) {
		setRadius(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FieldMonsterRecord value9(java.lang.Integer value) {
		setHeight(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FieldMonsterRecord values(java.lang.Integer value1, java.lang.String value2, java.lang.String value3, java.lang.String value4, java.lang.Integer value5, java.lang.Double value6, java.lang.String value7, java.lang.Integer value8, java.lang.Integer value9) {
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached FieldMonsterRecord
	 */
	public FieldMonsterRecord() {
		super(FieldMonster.fieldMonster);
	}

	/**
	 * Create a detached, initialised FieldMonsterRecord
	 */
	public FieldMonsterRecord(java.lang.Integer id, java.lang.String server, java.lang.String name, java.lang.String type, java.lang.Integer mobMax, java.lang.Double mobRate, java.lang.String center, java.lang.Integer radius, java.lang.Integer height) {
		super(FieldMonster.fieldMonster);

		setValue(0, id);
		setValue(1, server);
		setValue(2, name);
		setValue(3, type);
		setValue(4, mobMax);
		setValue(5, mobRate);
		setValue(6, center);
		setValue(7, radius);
		setValue(8, height);
	}
}
