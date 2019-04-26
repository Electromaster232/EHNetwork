package mineplex.hub.mail;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.plugin.java.JavaPlugin;

import mineplex.core.database.DBPool;
import mineplex.core.database.RepositoryBase;
import mineplex.database.Tables;
import mineplex.database.tables.records.MailRecord;

import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.impl.DSL;

public class MailRepository extends RepositoryBase
{
	private MailManager _manager;

	public MailRepository(JavaPlugin plugin, MailManager manager)
	{
		super(plugin, DBPool.ACCOUNT);
		
		_manager = manager;
	}

	@Override
	protected void initialize()
	{

	}

	@Override
	protected void update()
	{

	}

	public PlayerMailData loadMailData(ResultSet resultSet) throws SQLException
	{
		PlayerMailData data = new PlayerMailData();


		return data;
	}

	public PlayerMailData loadMailData(UUID uuid)
	{
		PlayerMailData data = new PlayerMailData();

		DSLContext context = DSL.using(getConnection());

		Result<MailRecord> resultSet = context.selectFrom(Tables.mail).where(Tables.mail.accountId.eq(DSL.select(Tables.accounts.id)
			.from(Tables.accounts)
			.where(Tables.accounts.uuid.eq(uuid.toString())))
		).and(Tables.mail.deleted.isFalse()).fetch();

		for (MailRecord record : resultSet)
		{
			data.getMessages().add(createMessage(record));
		}

		return data;
	}

	private MailMessage createMessage(MailRecord record)
	{
		return new MailMessage(_manager, record.getId(), record.getSender(), record.getMessage(), (record.getArchived() & 0x01) != 0, (record.getDeleted() & 0x01) != 0, record.getTimeSent());
	}

	public boolean archive(MailMessage mailMessage)
	{
		DSLContext context = DSL.using(getConnection());

		int recordsUpdated = context.update(Tables.mail).set(Tables.mail.archived, (byte) 1).where(Tables.mail.id.eq(mailMessage.getMessageId())).execute();

		return recordsUpdated == 1;
	}
}
