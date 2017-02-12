package songMP3Package;

public class MP3Main {

	public static void main(String args[])
	{
		AVLTree tree = new AVLTree();
		tree.insert("Starboy", "The Weekend", "?", "2015");
		tree.insert("starboy", "Justin Bieber", "IDK", "2005");
		tree.insert("Starboy", "Abby Lee", ">>>", "2008");
		tree.insert("Step It Up", "RuPaul", "LOL", "2");
		
		tree.printTree(tree.root);
	}
}
