package mineplex.core.map;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import mineplex.core.common.util.UtilServer;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

public class MapText
{
	private static HashMap<Character, BufferedImage> _characters = new HashMap<Character, BufferedImage>();

	private void loadCharacters()
	{
		try
		{
			InputStream inputStream = getClass().getResourceAsStream("ascii.png");
			BufferedImage image = ImageIO.read(inputStream);

			char[] text = new char[]
				{
						' ', '!', '"', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/', '0', '1', '2', '3', '4',
						'5', '6', '7', '8', '9', ':', ';', '<', '=', '>', '?', '@', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
						'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '[', '\\', ']', '^',
						'_', '`', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
						't', 'u', 'v', 'w', 'x', 'y', 'z', '{', '|', '}', '~'
				};

			int x = 0;
			int y = 16;

			for (char c : text)
			{
				grab(c, image, x, y);

				if (x < 15 * 8)
				{
					x += 8;
				}
				else
				{
					x = 0;
					y += 8;
				}
			}

			inputStream.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private static void grab(Character character, BufferedImage image, int imageX, int imageY)
	{
		BufferedImage newImage = image.getSubimage(imageX, imageY, 8, 8);

		int width = character == ' ' ? 4 : 0;

		if (width == 0)
		{
			for (int x = 0; x < 8; x++)
			{
				width++;
				boolean foundNonTrans = false;

				for (int y = 0; y < 8; y++)
				{
					int pixel = newImage.getRGB(x, y);

					if ((pixel >> 24) != 0x00)
					{
						foundNonTrans = true;
						break;
					}
				}

				if (!foundNonTrans)
				{
					break;
				}
			}
		}

		newImage = newImage.getSubimage(0, 0, width, 8);

		_characters.put(character, newImage);
	}

	private ArrayList<String> split(String text)
	{
		ArrayList<String> returns = new ArrayList<String>();
		int lineWidth = 0;
		String current = "";

		for (String word : text.split("(?<= )"))
		{
			int length = 0;

			for (char c : word.toCharArray())
			{
				length += _characters.get(c).getWidth();
			}

			if (lineWidth + length >= 127)
			{
				lineWidth = 0;
				returns.add(current);
				current = "";
			}

			current += word;
			lineWidth += length;
		}

		returns.add(current);

		return returns;
	}

	public ItemStack getMap(boolean sendToServer, String... text)
	{
		if (_characters.isEmpty())
		{
			loadCharacters();
		}

		BufferedImage image = new BufferedImage(128, 128, BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();
		int height = 1;

		for (String string : text)
		{
			for (String line : split(string))
			{
				int length = 1;

				for (char c : line.toCharArray())
				{
					BufferedImage img = _characters.get(c);

					if (img == null)
					{
						System.out.print("Error: '" + c + "' has no image associated");
						continue;
					}

					g.drawImage(img, length, height, null);

					length += img.getWidth();
				}

				height += 8;
			}
		}

		MapView map = Bukkit.createMap(Bukkit.getWorlds().get(0));

		for (MapRenderer r : map.getRenderers())
		{
			map.removeRenderer(r);
		}

		map.addRenderer(new ImageMapRenderer(image));

		ItemStack item = new ItemStack(Material.MAP);

		item.setDurability(map.getId());
		
		if (sendToServer)
		{
			for (Player player : UtilServer.getPlayers())
				player.sendMap(map);
		}

		return item;
	}
}
