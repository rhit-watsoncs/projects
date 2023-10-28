package editortrees;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Stack;

import editortrees.Node.Code;



/**
 * A height-balanced binary tree with rank that could be the basis for a text
 * editor.
 * 
 * @author <<Adam Field>>
 * @author <<Cameron Watson>>
 * TODO: Acknowledge anyone else you got help
 *         from here, along with the help they provided:
 * 
 * 
 * 
 */
public class EditTree {

	Node root;
	private int size;
	private DisplayableBinaryTree display;
	private int rotationCount;
	
	/**
	 * Container class used to keep track of rotations,
	 * previous balance codes, and deleted characters.
	 * Used in add() and delete() methods
	 * @author fieldaj1
	 *
	 */
	class TreeInfo {
		int count;
		boolean continueBalance;
		Code previousTip;
		char deletedChar;
		public TreeInfo() { 
			count = 0; 
			continueBalance = true;
		}
	}
	
	/**
	 * Helper class used with the EditTree(String s) 
	 * constructor. Includes a node, its rank, height, and
	 * number of children. This is used to pass said information
	 * back up the tree to update ranks and balance codes
	 * @author fieldaj1
	 *
	 */
	class HeightRank {
		
		Node node;
		int rank;
		int height;
		int childCount;
		public HeightRank(int rank, int height, int childCount, Node node) {
			this.rank = rank;
			this.height = height;
			this.childCount = childCount;
			this.node = node;
		}
		
		
	}
	
	TreeInfo info = new TreeInfo();

	/**
	 * MILESTONE 1 Construct an empty tree
	 */
	public EditTree() {
		root = Node.NULL_NODE;
		rotationCount = 0;
	}

	
	/**
	 * MILESTONE 1 Construct a single-node tree whose element is ch
	 * 
	 * @param ch
	 */
	public EditTree(char ch) {
		root = new Node(ch);
		size = 1;
		rotationCount = 0;
	}

	/**
	 * MILESTONE 2 Make this tree be a copy of e, with all new nodes, but the same
	 * shape and contents. You can write this one recursively, but you may not want
	 * your helper to be in the Node class.
	 * 
	 * @param e
	 */
	public EditTree(EditTree e) {
		 root = copyTree(e, e.root);
		 size = e.size;
	}
	
	

	private Node copyTree(EditTree e, Node curNode) {
		if(curNode == null) return null;
		if(curNode == Node.NULL_NODE) return Node.NULL_NODE;
		
		Node left = copyTree(e, curNode.left);
		left.rank = curNode.left.rank;
		left.balance = curNode.left.balance;
		
		Node right = copyTree(e, curNode.right);
		right.rank = curNode.right.rank;
		right.balance = curNode.right.balance;
		
		Node node = new Node(curNode.data, left, right);
		node.rank = curNode.rank;
		node.balance = curNode.balance;
		return node;
	}


	/**
	 * MILESTONE 3 Create an EditTree whose toString is s. This can be done in O(N)
	 * time, where N is the size of the tree (note that repeatedly calling insert()
	 * would be O(N log N), so you need to find a more efficient way to do this.
	 * 
	 * @param s
	 */
	public EditTree(String s) {
		if(s == null || s.length() == 0) {
			root = Node.NULL_NODE;
			rotationCount = 0;
			return;
		}

		// used to determine whether the character at a given index has been added
		HashSet<Integer> set = new HashSet<Integer>();
		
		int startIndex = s.length() / 2;
		int power = 2;
		
		root = constructorHelper(s, startIndex, set, power).node;

	}
	
	/**
	 * Helper method for the EditTree(String) constructor. 
	 * It recursively adds nodes from the string in the same
	 * style as binary search. 
	 * @param s
	 * @param index
	 * @param set
	 * @param power
	 * @return
	 */
	private HeightRank constructorHelper(String s, int index, HashSet<Integer> set, int power) {
		if(index < 0 || index >= s.length() || set.contains(index)) return new HeightRank(0, 0, -1, Node.NULL_NODE);

		set.add(index);
		Node node = new Node(s.charAt(index));
		HeightRank hr = new HeightRank(0, 0, 0, node);
		
		// indexes for right and left children
		int rightIndex = (int) (index + Math.round(s.length() / Math.pow(2, power)));
		int leftIndex = (int) (index - Math.round(s.length() / Math.pow(2, power)));


		HeightRank leftHR = constructorHelper(s, leftIndex, set, power + 1);
		node.left = leftHR.node;
		hr.rank = leftHR.childCount + 1;
		
		HeightRank rightHR = constructorHelper(s, rightIndex, set, power + 1);
		node.right = rightHR.node;
		hr.childCount = hr.rank + rightHR.childCount + 1;
		
		node.rank = hr.rank;
		hr.height = Math.max(leftHR.height, rightHR.height) + 1;
		
		determineBalanceCode(node, leftHR, rightHR);
		
		size++;
		return hr;
	}


	private void determineBalanceCode(Node node, HeightRank leftHR, HeightRank rightHR) {
		if(leftHR.height < rightHR.height) {
			node.balance = Code.RIGHT;
		} else if (leftHR.height > rightHR.height) {
			node.balance = Code.LEFT;
		} else {
			node.balance = Code.SAME;
		}		
	}


	public void show() {
		if (this.display == null) {
			this.display = new DisplayableBinaryTree(this, 960, 1080, true);
		} else {
			this.display.show(true);
		}
	}
	
	public void close() {
		if (this.display != null) {
			this.display.close();
		}
	}

	/**
	 * MILESTONE 1 return the string produced by an in-order traversal of this tree
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		InOrderIterator iterator = new InOrderIterator();
		while(iterator.hasNext()) {
			sb.append(iterator.next());
		}

		return sb.toString();
		
		
	}

	/**
	 * MILESTONE 1 Just modify the value of this.size whenever adding or removing a
	 * node. This is O(1).
	 * 
	 * @return the number of nodes in this tree, not counting the NULL_NODE if you
	 *         have one.
	 */
	public int size() {
		return this.size; // nothing else to do here.
	}

	/**
	 * MILESTONE 1
	 * 
	 * @param ch character to add to the end of this tree.
	 */
	public void add(char ch) {
		// Notes:
		// 1. Please document chunks of code as you go. Why are you doing what
		// you are doing? Comments written after the code is finalized tend to
		// be useless, since they just say WHAT the code does, line by line,
		// rather than WHY the code was written like that. Six months from now,
		// it's the reasoning behind doing what you did that will be valuable to
		// you!
		// 2. Unit tests are cumulative, and many things are based on add(), so
		// make sure that you get this one correct.
		
		add(ch, size);

	}

	/**
	 * MILESTONE 1
	 * 
	 * @param ch  character to add
	 * 
	 * @param pos character added in this in-order position Valid positions range
	 *            from 0 to the size of the tree, inclusive (if called with size, it
	 *            will append the character to the end of the tree).
	 * @throws IndexOutOfBoundsException if pos is negative or too large for this
	 *                                   tree.
	 */
	public void add(char ch, int pos) throws IndexOutOfBoundsException {
		 if (pos > size || pos < 0) throw new IndexOutOfBoundsException();
		    if (root == Node.NULL_NODE) {
		        root = new Node(ch);
		    } else {
		    	info.continueBalance = true;
		    	info.count = 0;
		    	info.previousTip = null;
		        root = root.add(ch, pos, info);
		        rotationCount += info.count;
		    }
		    size++;
		}

	/**
	 * MILESTONE 1 This one asks for more info from each node. You can write it
	 * similar to the arraylist-based toString() method from the BinarySearchTree
	 * assignment. However, the output isn't just the elements, but the elements AND
	 * ranks. Former students recommended that this method, while making it a little
	 * harder to pass tests initially, saves them time later since it catches weird
	 * errors that occur when you don't update ranks correctly. For the tree with
	 * root b and children a and c, it should return the string: [b1, a0, c0] There
	 * are many more examples in the unit tests.
	 * 
	 * @return The string of elements and ranks, given in an PRE-ORDER traversal of
	 *         the tree.
	 */
	public String toRankString() {
		ArrayList<String> list = new ArrayList<String>();
		list = root.toRankString(list);
		return list.toString();
	}

	/**
	 * MILESTONE 1
	 * 
	 * @param pos position in the tree
	 * @return the character at that position
	 * @throws IndexOutOfBoundsException if pos is negative or too big. Note that
	 *                                   the pos is now EXclusive of the size of the
	 *                                   tree, since there is no character there.
	 *                                   But you can still use your size
	 *                                   field/method to determine this.
	 */
	public char get(int pos) throws IndexOutOfBoundsException {
		if(pos < 0 || pos >= size) throw new IndexOutOfBoundsException();
		return root.get(pos);
	}

	// MILESTONE 1: They next two "slow" methods are useful for testing, debugging 
	// and the graphical debugger. They are each O(n) and don't make use of rank or 
	// size. In fact, they are the same as you used in an earlier assignment, so we 
	// are providing them for you.
	// Please do not modify them or their recursive helpers in the Node class.
	public int slowHeight() {
		return root.slowHeight();
	}

	public int slowSize() {
		return root.slowSize();
	}

	/**
	 * MILESTONE 1 Returns true iff (read as "if and only if") for every node in the
	 * tree, the node's rank equals the size of the left subtree. This will be used
	 * to check that your ranks are being updated correctly. So when you get a
	 * subtree's size, you should NOT refer to rank but find it brute-force, similar
	 * to slowSize(), and actually calling slowSize() might be a good first-pass.
	 * 
	 * For full credit, then refactor it to make it more efficient: do this in O(n)
	 * time, so in a single pass through the tree, and with only O(1) extra storage
	 * (so no temp collections).
	 * 
	 * Instead of using slowSize(), use the same pattern as the sum of heights
	 * problem in HW5. We put our helper class inside the Node class, but you can
	 * put it anywhere it's convenient.
	 * 
	 * PLEASE feel free to call this method (or its recursive helper) in your code
	 * while you are writing your add() method if rank isn't working correctly. You
	 * may also modify it to print WHERE it is failing. It may be most important to
	 * use in Milestone 2, when you are updating ranks during rotations. (We added
	 * some commented-out calls to this method there so show you how it can be
	 * used.)
	 * 
	 * @return True iff each node's rank correctly equals its left subtree's size.
	 */
	public boolean ranksMatchLeftSubtreeSize() {
		 return root.ranksMatchLeftSubtreeSize();
	}
	
	

	/**
	 * MILESTONE 2 Similar to toRankString(), but adding in balance codes too.
	 * 
	 * For the tree with root b and a left child a, it should return the string:
	 * [b1/, a0=] There are many more examples in the unit tests.
	 * 
	 * @return The string of elements and ranks, given in an pre-order traversal of
	 *         the tree.
	 */
	public String toDebugString() {
		ArrayList<String> list = new ArrayList<String>();
		list = root.toDebugString(list);
		return list.toString();

	}

	/**
	 * MILESTONE 2 returns the total number of rotations done in this tree since it
	 * was created. A double rotation counts as two.
	 *
	 * @return number of rotations since this tree was created.
	 */
	public int totalRotationCount() {
		return rotationCount;
	}

	/**
	 * MILESTONE 2 Returns true iff (read as "if and only if") for every node in the
	 * tree, the node's balance code is correct based on its childrens' heights.
	 * Like ranksMatchLeftSubtreeSize() above, you'll need to compare your balance
	 * code to the actual brute-force height calculation. You may start with calling
	 * slowHeight(). But then, for full credit, do this in O(n) time, so in a single
	 * pass through the tree, and with only O(1) extra storage (so no temp
	 * collections). Instead of slowHeight(), use the same pattern as the sum of
	 * heights problem in HW5. We put our helper class inside the Node class, but
	 * you can put it anywhere it's convenient.
	 * 
	 * The notes for ranksMatchLeftSubtreeSize() above apply here - this method is
	 * to help YOU as the developer.
	 * 
	 * @return True iff each node's balance code is correct.
	 */
	public boolean balanceCodesAreCorrect() {
		return root.balanceCodesAreCorrect(); 
	}

	/**
	 * MILESTONE 2 Only write this one once your balance codes are correct. It will
	 * rely on correct balance codes to find the height of the tree in O(log n)
	 * time.
	 * 
	 * @return the height of this tree
	 */
	public int fastHeight() {
		return root.fastHeight(); 
	}

	/**
	 * MILESTONE 3
	 * 
	 * @param pos position of character to delete from this tree
	 * @return the character that is deleted
	 * @throws IndexOutOfBoundsException
	 */
	public char delete(int pos) throws IndexOutOfBoundsException {
		if (pos >= size || pos < 0 || size == 0) throw new IndexOutOfBoundsException();
		// Implementation requirement:
		// When deleting a node with two children, you normally replace the
		// node to be deleted with either its in-order successor or predecessor.
		// The tests assume assume that you will replace it with the
		// *successor*.
		info.count = 0;
		info.continueBalance = true;
	    root = root.delete(pos, info);
	    size--;
	    rotationCount += info.count;
	    return info.deletedChar;

	}

	/**
	 * MILESTONE 3 This method operates in O(length), where length is the
	 * parameter provided. The way to do this is to recurse/iterate only
	 * over the nodes of the tree (and possibly their children) that
	 * contribute to the output string.
	 * 
	 * @param pos    location of the beginning of the string to retrieve
	 * @param length length of the string to retrieve
	 * @return string of length that starts in position pos
	 * @throws IndexOutOfBoundsException unless both pos and pos+length-1 are
	 *                                   legitimate indexes within this tree.
	 */
	public String get(int pos, int length) throws IndexOutOfBoundsException {
		if(length <= 0 || pos < 0 || pos + length -1  >= size) {
			throw new IndexOutOfBoundsException();
		}
		StringBuilder sb = new StringBuilder();
		root.get(pos, pos + length  - 1, sb);
		return sb.toString();
	}

	// Feel free to add whatever other methods and helpers you need,
	// like for the graphical debugger.
	
	
class InOrderIterator implements Iterator<Character> {
		
		private Stack<Node> stack;
		private Character last;

		
		public InOrderIterator() {
			last = null;
			stack = new Stack<Node>();
			if(root != Node.NULL_NODE) pushLeftChain(root);
		}

		@Override
		public boolean hasNext() {
			return !stack.isEmpty();
		}

		@Override
		public Character next() {
			if(!hasNext()) throw new NoSuchElementException();
			Node temp = stack.pop();
			if(temp.right != Node.NULL_NODE) {
				pushLeftChain(temp.right);
			}
			last = temp.data;
			return last;
		
		}
		
		
		public void pushLeftChain(Node node) {
			while(node != Node.NULL_NODE) {
				stack.push(node);
				node = node.left;
			}
		}
		
	}

}