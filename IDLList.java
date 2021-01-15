import java.util.ArrayList;

//I pledge my Honor I have abided by the Stevens Honor System. X Kai Engwall
public class IDLList<E> {
	private static class Node<E>{
		//Data Fields
		private E data;
		private Node<E> next;
		private Node<E> prev;
		
		//Constructors
		Node (E elem){
			super();
			this.data = elem;
			this.next = null;
			this.prev = null;
		}
		Node (E elem, Node<E> prev, Node<E> next){
			super();
			this.data = elem;
			this.next = next;
			this.prev = prev;
		}
		
	}
	//Data Fields
	private Node<E> head;
	private Node<E> tail;
	private int size;
	private ArrayList<Node<E>> indices;
	
	//Constructor
	public IDLList () {
		this.head = null;
		this.tail = null;
		this.size = 0;	
		indices = new ArrayList<Node<E>>();
	}
	
	//Methods
	
	//Adds element to index and updates the indices ArrayList
	//@throws IllegalStateException when index less than zero or when index > size
	//@returns true
	public boolean add (int index, E elem) {
		if(index > size || index < 0) {
			throw new IllegalStateException("Index not in array, or at end");
		}
		if(index == 0) {
			return this.add(elem);
		}
		else if(index == size) {
			return this.append(elem);
		}
		else{
			Node<E> prevNode = indices.get(index-1);
			Node<E> afterNode = indices.get(index);
			Node<E> current = new Node<E>(elem,prevNode,afterNode);
			prevNode.next = current;
			afterNode.prev = current;
			indices.add(index,current);
			size++;
			
		}
		
		
		return true;
	}
	
	//Adds elem to the front of the List, updates indices ArrayList
	//@returns true
	public boolean add (E elem) {
		if(head == null) {
			head = new Node<E>(elem);
			tail = head;
			size++;
			indices.add(0,head);
		}
		else if(size ==1){
			head = new Node<E>(elem,null,head);
			tail.prev = head;
			size++;
			indices.add(0,head);
		}
		else if(size > 1){
			Node<E> current = new Node<E>(elem,null,head);
			head.prev = current;
			head = current;
			size++;
			indices.add(0,current);
		}
		return true;
	}
	
	//Ads elem to the end of the List, updates indices ArrayList
	//@returns true
	public boolean append(E elem) {
		if (head == null) {
			return this.add(elem);
		}
		else { 
			Node<E> current = new Node<E>(elem,tail,null);
			tail.next = current;
			tail = current;
			indices.add(current);
			size++;
		}
		return true;
	}
	
	//returns element at given index, uses indices for faster access
	//@throws IllegalStateException when index less than zero or when index > size
	//@returns element at given index.
	public E get (int index) {
		if(index > size || index < 0) {
			throw new IllegalStateException("Index not in array");
		}
		return indices.get(index).data;
	}
	
	//@returns head element
	public E getHead () {
		if(head == null) {
			throw new NullPointerException("List is empty");
		}
		return head.data;
	}
	
	//@returns tail element
	public E getLast () {
		if(tail == null) {
			throw new NullPointerException("List is empty");
		}
		return tail.data;
	}
	
	//@returns size
	public int size() {
		return size;
	}
	
	//removes the first element of the List, if list is empty it throws exception
	//@returns element removed
	//@throws IllegalStateException if list is empty
	public E remove () {
		if(head == null) {
			throw new IllegalStateException("List is empty");
		}
		
		Node<E> holder = head;
		head = head.next;
		indices.remove(0);
		size--;
		return holder.data;
	}
	
	//removes the last element of the List, if list is empty it throws exception
	//@returns element removed
	//@throws IllegalStateException if list is empty
	public E removeLast () {
		if(tail == null) {
			throw new IllegalStateException("List is empty");
		}
		if(size == 1) {
			return remove();
		}
		else {
			E holder = tail.data;
			tail = tail.prev;
			tail.next = null;
			indices.remove(indices.size()-1);
			size--;
			return holder;
		}
	}
	
	//removes the element at the index from the list.
	//@returns element removed
	//@throws IllegalStateException if list is empty, or index is not in list.
	public E removeAt (int index) {
		if(index > size || index < 0) {
			throw new IllegalStateException("Index not in array, or at end");
		}
		if(index == 0) {
			return this.remove();
		}
		else if(index == size-1) {
			return this.removeLast();
		}
		else {
			Node<E> prevNode = indices.get(index-1);
			Node<E> afterNode = indices.get(index+1);
			prevNode.next = afterNode;
			afterNode.prev = prevNode;
			Node<E> holder = indices.remove(index);
			size--;
			return holder.data;
		}
		
	}
	
	//removes the first occurence of elem in the List, returns false if the elem is not in the list
	//@returns true if elem in list, false otherwise
	//@throws when list is null
	public boolean remove(E elem) {
		if(head == null) {
			throw new IllegalStateException("List is empty");
		}
//		int index = indices.indexOf(elem);
//		System.out.println(index);
//		if(index >=0) {
//			this.removeAt(index);
//			return true;
//		}
//		else {
//			return false;
//		}
		Node<E> current = head;
		int index = 0;
		while(current!=null) {
			if(current.data== elem) {
				this.removeAt(index);
				return true;
			}
			else {
				current = current.next;
				index++;
			}
		}
		return false;
		
	}
	
	//returns the double linked list as a String representation
	public String toString() {
		StringBuilder s = new StringBuilder();
		
		Node<E> current = head;
		s.append("[");
		while(current!=null) {
			if(current.next == null){
				s.append(current.data.toString());
				current = current.next;
			}
			else{
				s.append(current.data.toString() + ",");
				current = current.next;
			}
		}
		s.append("]");
		return s.toString();
	}
	
//	public static void main(String[] args) {
//		IDLList<Integer> l1 = new IDLList<Integer>();
//		System.out.println(l1.toString());
//		
//	}

	
	
	
	
	
	
	
	
	
}
