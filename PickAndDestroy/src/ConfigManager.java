import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import pulpcore.CoreSystem;
import pulpcore.scene.Scene2D;


public class ConfigManager
{
	public final void load(Scene2D scene)
	{
	}
	
	public final void reloadFile()
	{
		String line;
		URL url = null;
		try
		{
			url = new URL( CoreSystem.getBaseURL(), "config.txt" );
			
		}
		catch(MalformedURLException e)
		{
			
		}
		
		try
		{
			InputStream in = url.openStream();
			BufferedReader bf = new BufferedReader(new InputStreamReader(in));
			
			while((line = bf.readLine()) != null)
			{
				
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
}
