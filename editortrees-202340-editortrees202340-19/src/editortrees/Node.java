package editortrees;

import java.util.ArrayList;

import editortrees.EditTree.TreeInfo;

/**
 * A node in a height-balanced binary tree with rank. Except for the NULL_NODE,
 * one node cannot belong to two different trees.
 * 
 * @author <<Adam Field>>
 * @author <<Cameron Watson>>
 */
public class Node {
	
	DisplayableNodeWrapper displayableNodeWrapper;

	enum Code {
		SAME, LEFT, RIGHT;

		
		//afda
		// Used in the displayer and debug string
		public String toString() {
			switch (this) {
			case LEFT:
				return "/";
			case SAME:
				return "=";
			case RIGHT:
				return "\\";
			default:
				throw new IllegalStateException();
			}
		}
	}

	// The fields would normally be private, but for the purposes of this class,
	// we want to be able to test the results of the algorithms in addition to the
	// "publicly visible" effects

	char data;
	Node left, right; // subtrees
	int rank; // inorder position of this node within its own subtree.
	Code balance;
	
	
	// Feel free to add other fields that you find useful.
	// You probably want a NULL_NODE, but you can comment it out if you decide
	// otherwise.
	// The NULL_NODE uses the "null character", \0, as it's data and null children,
	// but they could be anything since you shouldn't ever actually refer to them in
	// your code.
	static final Node NULL_NODE = new Node('\0', null, null);
	// Node parent; You may want parent, but think twice: keeping it up-to-date
	// takes effort too, maybe more than it's worth.

	public Node(char data, Node left, Node right) {
		displayableNodeWrapper = new DisplayableNodeWrapper(this);
		this.data = data;
		this.left = left;
		this.right = right;
		  
	}

	public Node(char data) {
		// Make a leaf
		this(data, NULL_NODE, NULL_NODE);
		balance = Code.SAME;
		rank = 0;
	}
	
	
	public boolean hasLeft() {
		return this.left != NULL_NODE;
	}

	public boolean hasRight() {
		return this.right != NULL_NODE;
	}

	public boolean hasParent() {
		return false;
	}

	public Node getParent() {
		return NULL_NODE;
	}
	

	// Provided to you to enable testing, please don't change.
	int slowHeight() {
		if (this == NULL_NODE) {
			return -1;
		}
		return Math.max(left.slowHeight(), right.slowHeight()) + 1;
	}

	// Provided to you to enable testing, please don't change.
	public int slowSize() {
		if (this == NULL_NODE) {
			return 0;
		}
		return left.slowSize() + right.slowSize() + 1;
	}
	

	
	public Node add(char ch, int pos, TreeInfo info) {
		if (this == NULL_NODE) {
	        Node newNode = new Node(ch);
	        newNode.rank = 0;
	        return newNode;
	    }
		
		
	    if (pos <= rank) {
	        left = left.add(ch, pos, info);
	        rank++;
	        return checkLeftBalance(info);
	        
	       
	    } else {
	        right = right.add(ch, pos - rank - 1, info);
	        return checkRightBalance(info);
	    }
			
		
	}

	/**
	 * this is a method that corrects balances specifically for 
	 * the add() method. This is because checking balances
	 * for the delete method would require different logic
	 * @param info
	 * @return
	 */
	public Node checkLeftBalance(TreeInfo info) {
		  if(balance.equals(Code.SAME)) {
			  
	        	if(info.continueBalance) {
	        		this.balance = Code.LEFT;
	        		info.previousTip = Code.LEFT;
	        	}
	        	
	        } else if(balance.equals(Code.LEFT)) {
	        	
				if(info.continueBalance) {
					info.continueBalance = false;
	        		return (info.previousTip == null || info.previousTip.equals(Code.LEFT)) ? singleRotateRight(info) : doubleRotateRight(info);
	        		
	        	}
	        } else if(balance.equals(Code.RIGHT)) {
	        	
	        	if(info.continueBalance) balance = Code.SAME;
	        	info.continueBalance = false;
	        	
	        }
	
	        return this;
	}
	
	/**
	 * this is a method that corrects balances specifically for 
	 * the add() method. This is because checking balances
	 * for the delete method would require different logic
	 * @param info
	 * @return
	 */
	public Node checkRightBalance(TreeInfo info) {
		if (balance.equals(Code.SAME)) {
			
			if (info.continueBalance) {
				balance = Code.RIGHT;
				info.previousTip = Code.RIGHT;
			}
			
		} else if (balance.equals(Code.RIGHT)) {
			if (info.continueBalance) {
				info.continueBalance = false;
				return (info.previousTip == null || info.previousTip.equals(Code.RIGHT)) ? singleRotateLeft(info) : doubleRotateLeft(info);
			}
		} else if (balance.equals(Code.LEFT)) {
			if (info.continueBalance) balance = Code.SAME;
			info.continueBalance = false;
		}
        
    
		return this;
	}
	
	
	public Node doubleRotateLeft(TreeInfo info) {
		Code grandChildBalance = right.left.balance;
		
		right = right.singleRotateRight(info);
		
		Node returnNode =  singleRotateLeft(info);
		
		if(grandChildBalance.equals(Code.RIGHT)) {
			returnNode.left.balance = Code.LEFT;	
		} else if (grandChildBalance.equals(Code.LEFT)){
			returnNode.right.balance = Code.RIGHT;
		}
		
		return returnNode;
	}
	
	public Node doubleRotateRight(TreeInfo info) {
		Code grandChildBalance = left.right.balance;
		left = left.singleRotateLeft(info);
		Node returnNode = singleRotateRight(info);
		
		if(grandChildBalance.equals(Code.RIGHT)) {
			returnNode.left.balance = Code.LEFT;
		} else if (grandChildBalance.equals(Code.LEFT)){
			returnNode.right.balance = Code.RIGHT;
		} 
		
		return returnNode;
		
	}
	
    public Node singleRotateLeft(TreeInfo info) {
    	info.count++;
		Node grandChild = this.right.left;
		Node child = this.right;
		this.right = grandChild;
		child.left = this;
		this.balance = Code.SAME;
		child.balance = Code.SAME;
		child.rank += rank + 1;
		return child;
		
	}

	public Node singleRotateRight(TreeInfo info) {
		info.count++;
		Node grandChild = this.left.right;
		Node child = this.left;
		this.left = grandChild;
		child.right = this;
		this.balance = Code.SAME;
		child.balance = Code.SAME;
		this.rank = rank - child.rank -1;
		return child;
	
	}
	


	public ArrayList<String> toRankString(ArrayList<String> list) {
		if(this == NULL_NODE) return list;
		list.add(Character.toString(data) + rank);
		list = left.toRankString(list);
		list = right.toRankString(list);
		return list;
	}

	public char get(int pos) {
		if(pos < rank) return left.get(pos);
		if(pos > rank) return right.get(pos - rank - 1); 
		return data;
	}
	
	public boolean ranksMatchLeftSubtreeSize() {
	    if(this == NULL_NODE) {
	        return true;
	    }

	    int leftSize = left.slowSize();
	    if(leftSize != rank) {
	        return false;
	    }
	    return left.ranksMatchLeftSubtreeSize() && right.ranksMatchLeftSubtreeSize();
	}
	
	
	public ArrayList<String> toDebugString(ArrayList<String> list) {
		if(this == NULL_NODE) return list;
		list.add(Character.toString(data) + rank + balance.toString());
		

		list = left.toDebugString(list);
		list = right.toDebugString(list);
		return list;
	}


	public boolean balanceCodesAreCorrect() {
		if(this == NULL_NODE) return true;
		
		if(balance.equals(Code.SAME)) {
			if(left.slowHeight() != right.slowHeight()) return false;
		}
		if(balance.equals(Code.LEFT)) {
			if(left.slowHeight() <= right.slowHeight()) return false;
		}
		if(balance.equals(Code.RIGHT)) {
			if(left.slowHeight() >= right.slowHeight()) return false;
		}

		return left.balanceCodesAreCorrect() && right.balanceCodesAreCorrect();
	}

	public int fastHeight() {
		if(this == NULL_NODE) return -1;
		
		if(balance.equals(Code.RIGHT)) {
			return 1 + right.fastHeight();
		} else {
			return 1 + left.fastHeight();
		}
	}
	
	
	public Node delete(int pos, TreeInfo info) {
	    if (pos < rank) {
	        left = left.delete(pos, info);
	        rank--;
	        return tipRightDelete(info);
	    } else if (pos > rank) {
	        right = right.delete(pos - rank - 1, info);
	        return tipLeftDelete(info);
	    } else {
	    	
	    	info.deletedChar = data;
	        if (left == NULL_NODE && right == NULL_NODE) {
	            return NULL_NODE;
	        } else if (left == NULL_NODE) {
	            return right;
	        } else if (right == NULL_NODE) {
	            return left;
	        } else {
	            Node successor = getInOrderSuccessor();
	            right = right.delete(0, info);
	            successor.left = left;
	            successor.right = right;
	            successor.balance = balance;
	            successor.rank = rank;

	            return successor.tipLeftDelete(info);
	            
	        }
	    }
	}

	/**
	 * this method handles correcting balances specifically
	 * for the delete() method. This is because its logic is different
	 * than that of the add() method.
	 * @param info
	 * @return
	 */
	private Node tipRightDelete(TreeInfo info) {
		if(!info.continueBalance) return this;
		
		if(balance.equals(Code.LEFT)) {
			balance = Code.SAME;
		} else if (balance.equals(Code.RIGHT)) {
			return rotateLeftOnDelete(info);
		} else{
			info.continueBalance = false;
			balance = Code.RIGHT;
		}
	
		return this;

	}

	/**
	 * this method handles correcting balances specifically
	 * for the delete() method. This is because its logic is different
	 * than that of the add() method.
	 * @param info
	 * @return
	 */
	private Node tipLeftDelete(TreeInfo info) {
		if(!info.continueBalance) return this;
		
		if(balance.equals(Code.RIGHT)) {
			balance = Code.SAME;
		} else if (balance.equals(Code.LEFT)) {
			return rotateRightOnDelete(info);
		} else {
			info.continueBalance = false;
			balance = Code.LEFT;
		}
	
		return this;
	}
	
	/**
	 * this is only used with delete since it takes into account
	 * the special case where the node's child is balanced
	 * @param info
	 * @return
	 */
	private Node rotateLeftOnDelete(TreeInfo info) {
		if(right.balance.equals(Code.RIGHT)) {
			return singleRotateLeft(info);
		} else if (right.balance.equals(Code.LEFT)){
			return doubleRotateLeft(info);
		} else {
			// Special case with deletion
			Node n = singleRotateLeft(info);
			n.balance = Code.LEFT;
			n.left.balance = Code.RIGHT;
			info.continueBalance = false;
			return n;
		}

	}
	
	
	/**
	 * this is only used with delete since it takes into account
	 * the special case where the node's child is balanced
	 * @param info
	 * @return
	 */
	private Node rotateRightOnDelete(TreeInfo info) {
		if(left.balance.equals(Code.LEFT)) {
			return singleRotateRight(info);
		} else if (left.balance.equals(Code.RIGHT)){
			return doubleRotateRight(info);
		} else {
			//Special case with deletion
			Node n = singleRotateRight(info);
			n.balance = Code.RIGHT;
			n.right.balance = Code.LEFT;
			info.continueBalance = false;
			return n;
		}
	}



	private Node getInOrderSuccessor() {
	    Node currNode = right;
	    
	    while (currNode.left != NULL_NODE) {
	        currNode = currNode.left;
	    }
	    return currNode;
	}
 
	public void get(int Lpos,int Rpos,StringBuilder sb) {
		if(this == NULL_NODE) return;
	
		
		//left
		if(Lpos < rank) {
			left.get(Lpos, Math.min(rank - 1, Rpos), sb);
		}
		//root
		if(Lpos <= rank && rank <= Rpos) {
			sb.append(this.data);
		}
		//right 
		if(Rpos > rank) {
			right.get(Math.max(Lpos - rank - 1, 0), Rpos - rank - 1, sb);
		}
	}
	
	
	
}