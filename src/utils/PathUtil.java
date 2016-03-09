package utils;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;

import model.Preference;

public final class PathUtil
{
	private static String programDir = null;
	
	public static String getProgramDir() throws IOException
	{
		if (programDir == null)
		{
			String dir = (new File(Preference.class
					.getProtectionDomain()
					.getCodeSource()
					.getLocation()
					.getPath()))
					.getParentFile()
					.getCanonicalPath();
					
		programDir = URLDecoder.decode(dir, "UTF-8");
		}
		return programDir;
	}
	
	public static String getFullPath(String programRelativePath) throws IOException
	{
		return getProgramDir() + File.separator + programRelativePath;
	}
}
