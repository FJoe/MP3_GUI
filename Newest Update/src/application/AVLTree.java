package application;

public class AVLTree
{

	SongElement root;
	
	public AVLTree()
	{
		root = null;
	}
	
	public void insert(String name, String artist, String album, String year)
	{
		root = insert(name, artist, album, year, root);
	}
	
	int height(SongElement node)
	{
		return node == null ? -1 : node.height;
	}
	
	int max(int lefth, int righth)
	{
		return lefth > righth ? lefth : righth;
	}
	
	SongElement insert(String name, String artist, String album, String year, SongElement node)
	{
		String cmprName = (name + artist).toUpperCase();
		
		if(node == null)
			node = new SongElement(name, artist, album, year);
		else if(cmprName.compareTo(node.cmprName) < 0)
		{
			node.left = insert(name, artist, album, year, node.left);
			if(height(node.left) - height(node.right) == 2)
				if(cmprName.compareTo(node.left.cmprName) < 0)
					node = rotateLeft(node);
				else
					node = doubleRotateLeft(node);
		}
		else if(cmprName.compareTo(node.cmprName) > 0)
		{
			node.right = insert(name, artist, album, year, node.right);
			if(height(node.right) - height(node.left) == 2)
				if(cmprName.compareTo(node.right.cmprName) > 0)
					node = rotateRight(node);
				else
					node = doubleRotateRight(node);
		}
		else
			;//insert pop-up dialogue
		
		if(cmprName.compareTo(node.cmprName) == 0)
			node.height = max(height(node.left), height(node.right));
		else
			node.height = max(height(node.left), height(node.right)) + 1;
		return node;
	}//end insert method

	SongElement rotateLeft(SongElement node2)
	{
		SongElement node1 = node2.left;
		node2.left = node1.right;
		node1.right = node2;
		node2.height = max(height(node2.left), height(node2.right)) + 1;
		node1.height = max(height(node1.left), node2.height) + 1;
		return node1;
	}
	
	SongElement rotateRight(SongElement node1)
	{
		SongElement node2 = node1.right;
		node1.right = node2.left;
		node2.left = node1;
		node1.height = max(height(node1.left), height(node1.right)) + 1;
		node2.height = max(height(node2.right), node1.height) + 1;
		return node2;
	}
	
	SongElement doubleRotateLeft(SongElement node3)
	{
		node3.left = rotateRight(node3.right);
		return rotateLeft(node3);
	}
	
	SongElement doubleRotateRight(SongElement node3)
	{
		node3.right = rotateLeft(node3.right);
		return rotateRight(node3);
	}
	
	public boolean search(String name, String artist)
	{
		SongElement n = root;
		String cmprName = (name + artist).toUpperCase();
		boolean found = false;
		while(n != null && !found)
		{
			String nName = n.cmprName;
			if(cmprName.compareTo(nName) < 0)
				n = n.left;
			else if(cmprName.compareTo(nName) > 0)
				n = n.right;
			else
			{
				found = true;
				break;
			}
		}//end while loop
		return found;
	}
	
	SongElement minNode(SongElement node)
	{
		SongElement n = node;
		while(n.left != null)
		{
			n = n.left;
		}
		return n;
	}
	
	SongElement delete(SongElement root, String name)
	{
		if(root == null)
			return root;
		
		String cmprName = name.toUpperCase();

		if(cmprName.compareTo(root.cmprName) < 0)
			root.left = delete(root.left, name);
		else if(cmprName.compareTo(root.cmprName) > 0)
			root.right = delete(root.right, name);
		else
		{
			
			if((root.left == null) || (root.right == null))
			{
				SongElement temp = null;
				if(temp == root.left)
					temp = root.right;
				else
					temp = root.left;
				
				if(temp == null)
				{
					temp = root;
					root = null;
				}
				else
					root = temp;
			}//end left/right = null if-block
			else
			{
				SongElement temp = minNode(root.right);
				root.name = temp.name;
				root.cmprName = temp.cmprName;
				root.album = temp.album;
				root.artist = temp.artist;
				root.year = temp.year;
				root.right = delete(root.right, temp.name);
			}
		}//end "else block"
		
		if(root == null)
			return root;
		root.height = max(height(root.left), height(root.right)) + 1;
		
		int balance = height(root.left) - height(root.right);
		if(balance > 1 && (height(root.left.left) - height(root.left.right)) >= 0)
			return rotateRight(root);
		if(balance > 1 && (height(root.left.left) - height(root.left.right)) < 0)
			return doubleRotateRight(root);
		if(balance < -1 && (height(root.right.left) - height(root.right.right)) <= 0)
			return rotateLeft(root);
		if(balance < -1 && (height(root.right.left) - height(root.right.right)) > 0)
			return doubleRotateLeft(root);
		
		return root;
	}//end delete method
	
	void printTree(SongElement node)
	{
		SongElement n = node;
		if(n != null)
		{
			printTree(n.left);
			System.out.println(n);
			printTree(n.right);
		}
	}
}//end AVLTree class


