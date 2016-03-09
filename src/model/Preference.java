package model;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlRootElement;

import utils.PathUtil;
@XmlRootElement
public class Preference
{
	public String songFile = null;
	public double stageWidth = -1;
	public double stageHeight = -1;
	public double volume = -1;
	public boolean playback = false;
	private static final String FILE = ".preference";
	private static File file = null;
	
	static
	{
		try
		{
			file = new File(PathUtil.getFullPath(FILE));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void stocker()
	{
		if (file == null) 
		{
			return;
		}
		try
		{
			Marshaller m = JAXBContext
					.newInstance(Preference.class)
					.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			m.marshal(this, file);
		}
		catch (JAXBException e)
		{
			e.printStackTrace();
		}
	}
	public static Preference charger()
	{
		if (file == null || !file.exists()) return null;
		try
		{
			return (Preference) JAXBContext
					.newInstance(Preference.class)
					.createUnmarshaller()
					.unmarshal(file);
		}
		catch (JAXBException e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
