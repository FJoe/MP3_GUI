package application;

import java.util.Comparator;

public class SongElement
{

	SongElement left, right;
	public String name;
	public String cmprName;
	public String artist;
	public String album;
	public String year;
	int height;
	
	public SongElement()
	{
		left = null;
		right = null;
		name = null;
		cmprName = null;
		artist = null;
		album = null;
		year = null;
		height = 0;
	}
	
	public SongElement(String name, String artist, String album, String year)
	{
		this.name = name;
		cmprName = (name + artist).toUpperCase();
		this.artist = artist;
		this.album = album;
		this.year = year;
	}
	
	public String toString()
	{
		return "'" + name + "' by '" + artist + "'";
	}
	
	public boolean equals(Object o)
	{
		if(o == null || !(o instanceof SongElement))
			return false;
		
		SongElement s2 = (SongElement)o;
		return cmprName.equals(s2.cmprName);
	}
	
	public static class Comparators
	{
		public static Comparator<SongElement> NAME = new Comparator<SongElement>()
		{
			@Override
			public int compare(SongElement s1, SongElement s2)
			{
				return s1.cmprName.compareTo(s2.cmprName);
			}
		};
	}
}