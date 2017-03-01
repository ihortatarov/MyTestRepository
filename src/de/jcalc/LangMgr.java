package de.jcalc;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Verwaltung aller Sprachdateien für<br>
 * Errors      (Fehlermeldungen)<br>
 * Warnings    (Warunungen)<br>
 * Nachrichten (Informationen)<br>
 * GuiTexte    (Anzeigetexte)
 * 
 * @author Aug
 * @version 1.0
 */
public class LangMgr 
{
	private final String errmsg = "de/jcalc/resource/errors";
	private final String warnmsg= "de/jcalc/resource/warnings";
	private final String msgmsg = "de/jcalc/resource/messages";
	private final String guitxt = "de/jcalc/resource/guitexts";
	
	private ResourceBundle err = null;
	private ResourceBundle warn = null;
	private ResourceBundle msg = null;
	private ResourceBundle gui = null;
	
	public LangMgr()
	{
		try
		{
			err = ResourceBundle.getBundle(errmsg,java.util.Locale.getDefault(),this.getClass().getClassLoader()); 
			warn = ResourceBundle.getBundle(warnmsg,java.util.Locale.getDefault(),this.getClass().getClassLoader());
			msg = ResourceBundle.getBundle(msgmsg,java.util.Locale.getDefault(),this.getClass().getClassLoader());
			gui = ResourceBundle.getBundle(guitxt,java.util.Locale.getDefault(),this.getClass().getClassLoader());
		}
		catch(MissingResourceException notfound)
		{
			System.err.println(notfound.getMessage());
			System.out.println("Load Default Language");
			err = ResourceBundle.getBundle(errmsg,java.util.Locale.ENGLISH,this.getClass().getClassLoader()); 
			warn = ResourceBundle.getBundle(warnmsg,java.util.Locale.ENGLISH,this.getClass().getClassLoader());
			msg = ResourceBundle.getBundle(msgmsg,java.util.Locale.ENGLISH,this.getClass().getClassLoader());
			gui = ResourceBundle.getBundle(guitxt,java.util.Locale.ENGLISH,this.getClass().getClassLoader());
		}
	}
	
	
	public String getError(String error)
	{
		try
		{
			return err.getString(error);
		}
		catch(MissingResourceException e)
		{
			e.printStackTrace();
			return "???";
		}
	}
	public String getError(String error, String def)
	{
		try
		{
			String s = getError(error);
			if(s == null || s.length() < 1) return def;
			return s;
		}
		catch(MissingResourceException e)
		{
			e.printStackTrace();
			return def;
		}
	}
	
	public String getWarn(String warning)
	{
		try
		{
			return warn.getString(warning);
		}
		catch(MissingResourceException e)
		{
			e.printStackTrace();
			return "???";
		}
	}
	public String getWarn(String warning, String def)
	{
		try
		{
			String s = getWarn(warning);
			if(s == null || s.length() < 1) return def;
			return s;
		}
		catch(MissingResourceException e)
		{
			e.printStackTrace();
			return def;
		}
	}
	
	public String getMessage(String message)
	{
		try
		{
			return msg.getString(message);
		}
		catch(MissingResourceException e)
		{
			e.printStackTrace();
			return "???";
		}
	}
	public String getMessage(String message, String def)
	{
		try
		{
			String s = getMessage(message);
			if(s == null || s.length() < 1) return def;
			return s;
		}
		catch(MissingResourceException e)
		{
			e.printStackTrace();
			return def;
		}
	}
	
	public String getGuiText(String text)
	{
		try
		{
			return gui.getString(text);
		}
		catch(MissingResourceException e)
		{
			e.printStackTrace();
			return "???";
		}
	}
	public String getGuiText(String text, String def)
	{
		try
		{
			String s = getGuiText(text);
			if(s == null || s.length() < 1) return def;
			return s;
		}
		catch(java.util.MissingResourceException e)
		{
			e.printStackTrace();
			return def;
		}
	}
}
