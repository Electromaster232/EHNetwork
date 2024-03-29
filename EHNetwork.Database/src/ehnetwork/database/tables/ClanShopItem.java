/**
 * This class is generated by jOOQ
 */
package ehnetwork.database.tables;

import ehnetwork.database.Account;
import ehnetwork.database.Keys;
import ehnetwork.database.tables.records.ClanShopItemRecord;

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
public class ClanShopItem extends org.jooq.impl.TableImpl<ClanShopItemRecord> implements java.io.Serializable, java.lang.Cloneable {

	private static final long serialVersionUID = 1330993982;

	/**
	 * The reference instance of <code>Account.clanShopItem</code>
	 */
	public static final ClanShopItem clanShopItem = new ClanShopItem();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<ClanShopItemRecord> getRecordType() {
		return ClanShopItemRecord.class;
	}

	/**
	 * The column <code>Account.clanShopItem.id</code>.
	 */
	public final org.jooq.TableField<ClanShopItemRecord, java.lang.Integer> id = createField("id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>Account.clanShopItem.shopName</code>.
	 */
	public final org.jooq.TableField<ClanShopItemRecord, java.lang.String> shopName = createField("shopName", org.jooq.impl.SQLDataType.VARCHAR.length(50).nullable(false), this, "");

	/**
	 * The column <code>Account.clanShopItem.shopPage</code>.
	 */
	public final org.jooq.TableField<ClanShopItemRecord, java.lang.String> shopPage = createField("shopPage", org.jooq.impl.SQLDataType.VARCHAR.length(50).nullable(false), this, "");

	/**
	 * The column <code>Account.clanShopItem.slot</code>.
	 */
	public final org.jooq.TableField<ClanShopItemRecord, java.lang.Integer> slot = createField("slot", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>Account.clanShopItem.material</code>.
	 */
	public final org.jooq.TableField<ClanShopItemRecord, java.lang.String> material = createField("material", org.jooq.impl.SQLDataType.VARCHAR.length(50).nullable(false), this, "");

	/**
	 * The column <code>Account.clanShopItem.data</code>.
	 */
	public final org.jooq.TableField<ClanShopItemRecord, java.lang.Byte> data = createField("data", org.jooq.impl.SQLDataType.TINYINT.nullable(false).defaulted(true), this, "");

	/**
	 * The column <code>Account.clanShopItem.lore</code>.
	 */
	public final org.jooq.TableField<ClanShopItemRecord, java.lang.String> lore = createField("lore", org.jooq.impl.SQLDataType.VARCHAR.length(50), this, "");

	/**
	 * The column <code>Account.clanShopItem.displayName</code>.
	 */
	public final org.jooq.TableField<ClanShopItemRecord, java.lang.String> displayName = createField("displayName", org.jooq.impl.SQLDataType.VARCHAR.length(50).nullable(false), this, "");

	/**
	 * The column <code>Account.clanShopItem.price</code>.
	 */
	public final org.jooq.TableField<ClanShopItemRecord, java.lang.Integer> price = createField("price", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>Account.clanShopItem.bulkCount</code>.
	 */
	public final org.jooq.TableField<ClanShopItemRecord, java.lang.Integer> bulkCount = createField("bulkCount", org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaulted(true), this, "");

	/**
	 * Create a <code>Account.clanShopItem</code> table reference
	 */
	public ClanShopItem() {
		this("clanShopItem", null);
	}

	/**
	 * Create an aliased <code>Account.clanShopItem</code> table reference
	 */
	public ClanShopItem(java.lang.String alias) {
		this(alias, ClanShopItem.clanShopItem);
	}

	private ClanShopItem(java.lang.String alias, org.jooq.Table<ClanShopItemRecord> aliased) {
		this(alias, aliased, null);
	}

	private ClanShopItem(java.lang.String alias, org.jooq.Table<ClanShopItemRecord> aliased, org.jooq.Field<?>[] parameters) {
		super(alias, Account.Account, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Identity<ClanShopItemRecord, java.lang.Integer> getIdentity() {
		return Keys.IDENTITY_clanShopItem;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.UniqueKey<ClanShopItemRecord> getPrimaryKey() {
		return Keys.KEY_clanShopItem_PRIMARY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.UniqueKey<ClanShopItemRecord>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<ClanShopItemRecord>>asList(Keys.KEY_clanShopItem_PRIMARY, Keys.KEY_clanShopItem_shop_page_slot_UNIQUE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ClanShopItem as(java.lang.String alias) {
		return new ClanShopItem(alias, this);
	}

	/**
	 * Rename this table
	 */
	public ClanShopItem rename(java.lang.String name) {
		return new ClanShopItem(name, null);
	}
}
