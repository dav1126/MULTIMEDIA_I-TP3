package utils;

import java.io.File;
import java.net.URLDecoder;

import javafx.scene.media.Media;

public class MediaUtil
{
	public static String getMediaFileName(Media media)
	{
		try
		{
			return URLDecoder.decode((new File(media.getSource())).getName(), "UTF-8");
		}
		catch (Exception e)
		{
			System.out.println("Fichier non valide: " + media.getSource());
			return "Inconnu";
		}
	}
}
