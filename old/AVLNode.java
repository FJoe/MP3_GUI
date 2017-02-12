package songMP3Package;

public class AVLNode
{

	AVLNode left, right;
	String name;
	String cmprName;
	String artist;
	String album;
	String year;
	int height;
	
	public AVLNode()
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
	
	public AVLNode(String name, String artist, String album, String year)
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
}
