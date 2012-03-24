import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;

import pulpcore.CoreSystem;

public class ConfigFile
{
	String name;
	
	Hashtable<String,Integer> map = new Hashtable<String,Integer> ();
	
	public ConfigFile ( String name )
	{
		this.name = name;
	}
	
	public final int getValue ( String name )
	{
		Integer i = (Integer) map.get ( name );
		if ( i == null )
		{
			throw new IllegalArgumentException ( "Unknown config variable name : " + name );
		}
		return i;
	}
	
	public final void reloadFile()
	{
		String line;
		URL url = null;
		try
		{
			url = new URL( CoreSystem.getBaseURL(), name );
		}
		catch(MalformedURLException e)
		{
			throw new IllegalArgumentException ( "Unknown config filename : " + name );
		}
		
		try
		{
			map.clear ();
			InputStream in = url.openStream();
			BufferedReader bf = new BufferedReader(new InputStreamReader(in));
			int lineNumber = 0;
			while((line = bf.readLine()) != null)
			{
				if ( line.trim ().length () > 0 )
				{
					String[] tokens = line.split ( "=" );
					if ( tokens.length != 2 )
					{
						System.out.println ( "Error on line " + lineNumber );
					}
					else
					{
						String name = tokens[0].trim ();
						String value = tokens[1].trim ();
						try
						{
							Integer valueAsInt = Integer.parseInt ( value );
							map.put ( name, valueAsInt );
						}
						catch ( Exception e )
						{
							System.out.println ( "Error on line " + lineNumber + " value is not a number" );
						}
					}
				}

				lineNumber++;
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
}
