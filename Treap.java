import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

//Kai Engwall
//I pledge my Honor I have abided by the Stevens Honor System

public class Treap<E extends Comparable<E>>{
	private static class Node<E>{
		//Data Fields
		public E data; // key for the search
		public int priority; // random heap priority
		public Node<E> left;
		public Node<E> right;
		
		//Constructor
		public Node(E data, int priority) {
			this.data = data;
			this.priority = priority;
		}
		
		Node<E> rotateRight(){
			Node<E> root = this;
			Node<E> c = root.left;
			Node<E> T2 = c.right;
			c.right = root;
			root.left = T2;
			return c;
		}
		
		Node<E> rotateLeft(){
			Node<E> root = this;
			Node<E> c = root.right;
			Node<E> T2 = c.left;
			c.left = root;
			root.right = T2;
			return c;
		}
		public String toString() {
			return "(key=" + data + ", " + "priority=" + priority +")"+"\n";
		}
		
	}
	//Data Fields
	private Random priorityGenerator;
	private Node<E> root;
	private ArrayList<Integer> pList;
	
	//Constructors
	public Treap() {
		pList = new ArrayList<Integer>();
		root = null;
		priorityGenerator = new Random();
	}
	
	public Treap(long seed) {
		pList = new ArrayList<Integer>();
		root = null;
		priorityGenerator = new Random(seed);
	}
	/*
	 * @param  E key, data value to be added to Treap along with a random priority
	 * @return returns true if the Node is added to the Treap, false if the key is already in the Treap 
	 * */
	public boolean add(E key) {
		int random = priorityGenerator.nextInt();
		while(pList.indexOf(random) != -1) {
			random = priorityGenerator.nextInt();
		}
		return add(key, random);
	}
	
	/*
	 * @param  E key, data value to be added to Treap 
	 * @param int priority
	 * @return returns true if the Node is added to the Treap, false if the key/priority is already in the Treap 
	 * */
	public boolean add(E key, int priority) {
		if(pList.indexOf(priority) != -1) {
			return false;
		}
		else {
			pList.add(priority);
		}
		if(root == null) {
			root = new Node<E>(key,priority);
			return true;
		}
		Stack<Node <E>> s = new Stack<Node <E>>();
		Node<E> current = root;
		//Increments current until it reaches its spot as per BST Node addition
		while(current!=null) {
			int i = current.data.compareTo(key);
			if(i<0) {
				s.push(current);
				current = current.right;
			}
			else if(i>0) {
				s.push(current);
				current = current.left;
			}
			else {
				return false;
			}
		}
		if(s.peek().data.compareTo(key)<0) {
			current = new Node<E>(key,priority);
			s.peek().right = current;
			reheapI(s,current);
		}
		else {
			current = new Node<E>(key,priority);
			s.peek().left = current;
			reheapI(s,current);
		}
		return true;
		
	}
		
	/*
	 * @param Stack<Node<E>> s contains the Nodes in the path when adding the new Node to the Treap in a BST matter
	 * @oaram Node<E> current is the node that is added 
	 * Reheaps the Treap iteratively, starting at the current node, or where the Node is added
	 * */
	private void reheapI(Stack<Node<E>> s,Node<E> current) {
		
		
			while(!s.empty() && s.peek().priority < current.priority) {
				if(current == s.peek().right) {
					Node<E> p1 = s.pop();
					if(!s.empty()) {
						Node<E> p2 = s.peek();
						//p2.right = p1.rotateLeft();
						if(p2.right == p1) {
							p2.right = p1.rotateLeft();
						}
						else if(p2.left == p1) {
							p2.left = p1.rotateLeft();
						}
					}
					else  {
						current = p1.rotateLeft();
						root = current;
					}
					
				}
				else if(current == s.peek().left) {
					Node<E> p1 = s.pop();
					if(!s.empty()) {
						Node<E> p2 = s.peek();
						if(p2.right == p1) {
							p2.right = p1.rotateRight();
						}
						else if(p2.left == p1){
							p2.left = p1.rotateRight();
						}
						
					}
					else {
						current = p1.rotateRight();
						root = current;
					}
				}
			}
		
	}
	
	/*
	 * @param Stack<Node<E>> s is the Stack containing the parent Nodes of the Node to be removed
	 * @param Node<E> current is the Node being removed
	 * Performs the algorithm to remove the Node from the Treap and keep the BST ordering and Treap ordering
	 * 
	 * */
		
	private void delete_helper(Stack<Node<E>> s,Node<E> current) {
		//Checks to see if root needs to be removed
		if(current == root) {
			//If root has no children, set it to null
			if(root.left == null && root.right == null) {
				root = null;
			}
			//If root has both children, check which priority is highest, then perform RotateRIght/rotateLeft based on that
			else if(root.left!=null && root.right!=null) {
				if(root.left.priority > root.right.priority) {
					root = root.rotateRight();
					s.push(root);
				}
				else {
					root = root.rotateLeft();
					s.push(root);
				}
			}
			//Checks to see if root has only left child
			if(root.left!=null && root.right == null) {
				root = root.rotateRight();
				s.push(root);
			}
			//Checks to see if root has only right child.
			else if(root.right!=null && root.left == null) {
				root = root.rotateLeft();
				s.push(root);
			}
			
		}
		
		//Breaks the while loop when the current Node has no children.
		while(!s.empty() && (current.left != null || current.right != null)) {
			//Checks to see if current Node has both children.
			if(current.left!=null && current.right!=null) {
				if(current.left.priority > current.right.priority) {
					//If the current Node is the right child, then:
					if(s.peek().right == current) {
						//sets the parent Node right child to the RotateRight, then holds this Node in a variable in order to save it as the next parent of current.
						s.peek().right = current.rotateRight();
						Node<E> hold = s.peek().right;
						current = s.pop().right.right;
						s.push(hold);
					}
					//If the current Node is the left child, then:
					else if(s.peek().left == current){
						//sets the parent Node left child to the RotateRight, then holds this Node in a variable in order to save it as the next parent of current.
						s.peek().left = current.rotateRight();
						Node<E> hold = s.peek().left;
						current = s.pop().left.right;
						s.push(hold);
					}	
					
					}
				else {
					if(s.peek().right == current) {
						//sets the parent Node right child to the RotateLeft, then holds this Node in a variable in order to save it as the next parent of current.
						s.peek().right = current.rotateLeft();
						Node<E> hold = s.peek().right;
						current = s.pop().right.left;
						s.push(hold);
					}
					else if(s.peek().left == current){
						//sets the parent Node left child to the RotateLeft, then holds this Node in a variable in order to save it as the next parent of current.
						s.peek().left = current.rotateLeft();
						Node<E> hold = s.peek().left;
						current = s.pop().left.left;
						s.push(hold);
					}
				}
			}
			//Checks to see if ony the left child exists
			else if(current.left !=null && current.right == null) {
				if(s.peek().right == current) {
					//Sets the parent Node right child to the rotateRight, then holds this Node as a variable to add it to the stack as the next parent of current.
					s.peek().right = current.rotateRight();
					Node<E> hold = s.peek().right;
					current = s.pop().right.right;
					s.push(hold);
				}
				else if(s.peek().left == current){
					//Sets the parent Node left child to the rotateRIght, then holds this Node as a variable to add it to the stack as the next parent of current.
					s.peek().left = current.rotateRight();
					Node<E> hold = s.peek().left;
					current = s.pop().left.right;
					s.push(hold);
				}	
			}
			//Checks if only the right child exists
			else if(current.left == null && current.right != null) {
				if(s.peek().right == current) {
					//Sets the parent Node right child to the rotateLeft, then holds this Node as a variable to add it to the stack as the next parent of current.
					s.peek().right = current.rotateLeft();
					Node<E> hold = s.peek().right;
					current = s.pop().right.left;
					s.push(hold);
				}
				else if(s.peek().left == current){
					//Sets the parent Node left child to the rotateLeft, then holds this Node as a variable to add it to the stack as the next parent of current.
					s.peek().left = current.rotateLeft();
					Node<E> hold = s.peek().left;
					current = s.pop().left.left;
					s.push(hold);
				}
			}
		}
		//Checks to see where the Node is positioned relative to its parent to delete it. 
		if(!s.empty()) {
			if(s.peek().right == current) {
				s.peek().right = null;
			}
			else {
				s.peek().left = null;
			}
		}
	}
	
	/*
	 * @returns false if the Node with E key is not found, returns true if the Node is removed
	 * @param E key is the key of the Node to be removed
	 * Finds the Node with the paramater E key, uses Treap deletion to remove the key
	 * */
	
	public boolean delete(E key) {
		Node<E> current = root;
		Stack<Node<E>> s = new Stack();
		while(current.data.compareTo(key)!=0) {
			//Checks to see if both current.right and current.left exist
			if(current.right !=null && current.left!=null) {	
				if(key.compareTo(current.data) > 0) {
					s.push(current);
					current = current.right;
				}
				else if(key.compareTo(current.data) < 0) {
					s.push(current);
					current = current.left;
				}
			}
			//Checks to see if only current.right exists and current.left is null
			else if(current.right!=null && current.left == null) {
				if(key.compareTo(current.right.data) == 0) {
					current = current.right;
					
				}
				else if(key.compareTo(current.data) > 0) {
					s.push(current);
					current = current.right;
				}
			}
			//Checks to see if current.right is null and current.left exists
			else if(current.right == null && current.left !=null) {
				if(key.compareTo(current.left.data) == 0) {
					current = current.left;
				}
				else if(key.compareTo(current.data) < 0) {
					s.push(current);
					current = current.left;
				}
			}
			//Checks to see if current.right and current.left are both null
			else if(current.right == null && current.left == null) {
				return false;
			}
		}
		delete_helper(s,current);
		return true;
		
		
	}
	/*
	 * @param Node<E> root the root of the Subtree to start the find method
	 * @param E key the key of the Node to be found
	 * @returns false if key not found in the subTree, returns true if found
	 * */
	public boolean find(Node<E> root, E key) {
		Node<E> current = root;
		while(current.data.compareTo(key)!=0) {
			//Checks to see if both current.right and current.left exist
			if(current.right !=null && current.left!=null) {	
				if(key.compareTo(current.data) > 0) {
					current = current.right;
				}
				else if(key.compareTo(current.data) < 0) {
					current = current.left;
				}
			}
			//Checks to see if only current.right exists and current.left is null
			else if(current.right!=null && current.left == null) {
				if(key.compareTo(current.right.data) == 0) {
					current = current.right;
					
				}
				else if(key.compareTo(current.data) > 0) {
					current = current.right;
				}
			}
			//Checks to see if current.right is null and current.left exists
			else if(current.right == null && current.left !=null) {
				if(key.compareTo(current.left.data) == 0) {
					current = current.left;
				}
				else if(key.compareTo(current.data) < 0) {
					current = current.left;
				}
			}
			//Checks to see if current.right and current.left are both null
			else if(current.right == null && current.left == null) {
				return false;
			}
		}
		return true;
	}
	/*
	 * @param E key the key of the Node to be found
	 * @returns false if key not found in the Tree, returns true if found
	 * */
	public boolean find(E key) {
		Node<E> current = root;
		while(current.data.compareTo(key)!=0) {
			//Checks to see if both current.right and current.left exist
			if(current.right !=null && current.left!=null) {	
				if(key.compareTo(current.data) > 0) {
					current = current.right;
				}
				else if(key.compareTo(current.data) < 0) {
					current = current.left;
				}
			}
			//Checks to see if only current.right exists and current.left is null
			else if(current.right!=null && current.left == null) {
				if(key.compareTo(current.right.data) == 0) {
					current = current.right;
					
				}
				else if(key.compareTo(current.data) > 0) {
					current = current.right;
				}
			}
			//Checks to see if current.right is null and current.left exists
			else if(current.right == null && current.left !=null) {
				if(key.compareTo(current.left.data) == 0) {
					current = current.left;
				}
				else if(key.compareTo(current.data) < 0) {
					current = current.left;
				}
			}
			//Checks to see if current.right and current.left are both null
			else if(current.right == null && current.left == null) {
				return false;
			}
		}
		return true;
	}
	private StringBuilder toString_helper(Node<E> current, int level) {
		StringBuilder r = new StringBuilder();
		for(int i = 1; i < level;i++) {
			r.append("  ");
		}
		if(current == null) {
			r.append("null\n");
		}
		else {
			r.append(current.toString());
			r.append(toString_helper(current.left,level+1));
			r.append(toString_helper(current.right,level+1));
		}
		return r;
	}
	
	/*
	 * @returns the toString representation of the Treap
	 * */
	public String toString() {
		return toString_helper(root,1).toString();
	}
	
//	public static void main(String[] args) {
////		Treap<String> t1 = new Treap<String>();
////		
////		t1.add("g",80);
////		
////		t1.add("j",65);
////		t1.add("a",60);
////		//t1.add("i",93);
////		t1.add("p", 99);
////		t1.add("u",75);
////		t1.add("r",40);
////		t1.add("z",47);
////		t1.add("w",32);
////		t1.add("v",21);	
////		t1.add("x",25);
////
////		System.out.println(t1);
////		System.out.println(t1.delete("p"));
////		System.out.println(t1);
//		//System.out.println(t1.find(t1.root.left,"z"));
//		
////		Treap<Integer>testTree = new Treap <Integer>();
////		testTree.add(4);
////		testTree.add(2);
////		testTree.add(1);
////		testTree.add(6);
////		testTree.add(6 ,70);
////		testTree.add(1 ,84);
////		testTree.add(3 ,12);
////		testTree.add(5 ,83);
////		testTree.add(7 ,26);
//		//System.out.println(testTree.add(123,26));
////		System.out.println(testTree);
//		//System.out.println(testTree.toString().substring(21,27));
//		//System.out.println(testTree.toString().substring(0,20));
//		//System.out.println(testTree.find(3));
//		
//		Treap<String> test2 = new Treap<String>();
//		test2.add("a");
//		test2.add("b");
//		test2.add("c");
//		
//		System.out.println(test2);
//
//	}
}
