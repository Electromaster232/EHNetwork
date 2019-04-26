package mineplex.core.common.jsonchat;

public class ChildJsonMessage extends JsonMessage
{
	private JsonMessage _parent;
	
	public ChildJsonMessage(String text)
	{
		this(new StringBuilder(), text);
	}
	
	public ChildJsonMessage(StringBuilder builder, String text)
	{
		this(null, builder, text);
	}
	
	public ChildJsonMessage(JsonMessage parent, StringBuilder builder, String text)
	{
		super(builder, text);

		_parent = parent;
	}
	
	public ChildJsonMessage add(String text)
	{
		Builder.append("}, ");
		return new ChildJsonMessage(_parent, Builder, text);
	}
	
	@Override
	public ChildJsonMessage color(String color)
	{
		super.color(color);

		return this;
	}

	@Override
	public ChildJsonMessage bold()
	{
		super.bold();

		return this;
	}
	
	@Override
	public ChildJsonMessage click(String action, String value)
	{
		super.click(action, value);
		
		return this;
	}
	
	@Override
	public ChildJsonMessage hover(String action, String value)
	{
		super.hover(action, value);
		
		return this;
	}
	
	@Override
	public String toString()
	{
		Builder.append("}");
		
		if (_parent != null)
		{
			Builder.append("]");
			return _parent instanceof ChildJsonMessage ? ((ChildJsonMessage)_parent).toString() : _parent.toString();
		}
		else
			return Builder.toString();
	}
}
