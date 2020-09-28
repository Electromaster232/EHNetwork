/**
 * This class is generated by jOOQ
 */
package ehnetwork.database.tables.records;

import ehnetwork.database.tables.Items;

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
public class ItemsRecord extends org.jooq.impl.UpdatableRecordImpl<ItemsRecord> implements java.io.Serializable, java.lang.Cloneable, org.jooq.Record4<java.lang.Integer, java.lang.String, java.lang.Integer, java.lang.Integer> {

	private static final long serialVersionUID = 2052433427;

	/**
	 * Setter for <code>Account.items.id</code>.
	 */
	public void setId(java.lang.Integer value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>Account.items.id</code>.
	 */
	public java.lang.Integer getId() {
		return (java.lang.Integer) getValue(0);
	}

	/**
	 * Setter for <code>Account.items.name</code>.
	 */
	public void setName(java.lang.String value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>Account.items.name</code>.
	 */
	public java.lang.String getName() {
		return (java.lang.String) getValue(1);
	}

	/**
	 * Setter for <code>Account.items.categoryId</code>.
	 */
	public void setCategoryId(java.lang.Integer value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>Account.items.categoryId</code>.
	 */
	public java.lang.Integer getCategoryId() {
		return (java.lang.Integer) getValue(2);
	}

	/**
	 * Setter for <code>Account.items.rarity</code>.
	 */
	public void setRarity(java.lang.Integer value) {
		setValue(3, value);
	}

	/**
	 * Getter for <code>Account.items.rarity</code>.
	 */
	public java.lang.Integer getRarity() {
		return (java.lang.Integer) getValue(3);
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
	public org.jooq.Row4<java.lang.Integer, java.lang.String, java.lang.Integer, java.lang.Integer> fieldsRow() {
		return (org.jooq.Row4) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row4<java.lang.Integer, java.lang.String, java.lang.Integer, java.lang.Integer> valuesRow() {
		return (org.jooq.Row4) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field1() {
		return Items.items.id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field2() {
		return Items.items.name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field3() {
		return Items.items.categoryId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field4() {
		return Items.items.rarity;
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
		return getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value3() {
		return getCategoryId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value4() {
		return getRarity();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ItemsRecord value1(java.lang.Integer value) {
		setId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ItemsRecord value2(java.lang.String value) {
		setName(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ItemsRecord value3(java.lang.Integer value) {
		setCategoryId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ItemsRecord value4(java.lang.Integer value) {
		setRarity(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ItemsRecord values(java.lang.Integer value1, java.lang.String value2, java.lang.Integer value3, java.lang.Integer value4) {
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached ItemsRecord
	 */
	public ItemsRecord() {
		super(Items.items);
	}

	/**
	 * Create a detached, initialised ItemsRecord
	 */
	public ItemsRecord(java.lang.Integer id, java.lang.String name, java.lang.Integer categoryId, java.lang.Integer rarity) {
		super(Items.items);

		setValue(0, id);
		setValue(1, name);
		setValue(2, categoryId);
		setValue(3, rarity);
	}
}