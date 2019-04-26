package ca.phinary.jedistest.api;

public interface Chat
{
	public void addListener(ChatListener chatListener);

	public void clearListeners();
}
