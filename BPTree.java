/**
 * Filename: BPTree.java 
 * Project: Final Project - Food List 
 * Authors: Epic lecture 4 
 * Julie Book - jlsauer@wisc.edu 
 * David Billmire - dbillmire@wisc.edu
 * Mark Connell - mconnell2@wisc.edu
 * Michelle Lindblom - mlindblom@wisc.edu
 *
 * Semester: Fall 2018 Course: CS400
 * 
 * Due Date: 12/2/18 11:59 pm Version: 1.0
 * 
 * Credits: none
 * Bugs: no known bugs
 */
package application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

/**
 * Implementation of a B+ tree to allow efficient access to
 * many different indexes of a large data set. 
 * BPTree objects are created for each type of index
 * needed by the program.  BPTrees provide an efficient
 * range search as compared to other types of data structures
 * due to the ability to perform log_m N lookups and
 * linear in-order traversals of the data items.
 * 
 * @author sapan (sapan@cs.wisc.edu)
 *
 * @param <K> key - expect a string that is the type of id for each item
 * @param <V> value - expect a user-defined type that stores all data for a food item
 */
public class BPTree<K extends Comparable<K>, V> implements BPTreeADT<K, V> {

    // Root of the tree
    private Node root;
    
    // Branching factor is the number of children nodes 
    // for internal nodes of the tree
    private int branchingFactor;
    
    
    /**
     * Public constructor
     * 
     * @param branchingFactor 
     */
    public BPTree(int branchingFactor) {
        this.branchingFactor = branchingFactor;
    	if (branchingFactor <= 2) {
            throw new IllegalArgumentException(
               "Illegal branching factor: " + branchingFactor);
        }
        root = new InternalNode();     
    }
    
    /**
     * Inserts a new key-value pair in to the tree
     * @param key - Key to insert
     * @param value - value to insert
     */
    @Override
    public void insert(K key, V value) {
        root.insert(key, value);
        if(root.isOverflow()) {
        	Node newChild = root.split();
        	List<Node> children = new ArrayList<Node>();
        	children.add(root);
        	children.add(newChild);
        	List<K> keys = new ArrayList<K>();
        	keys.add(newChild.getFirstLeafKey());
        	Node newRoot = new InternalNode(keys,children);
        	root = newRoot;
        }
    }
    
   /**
    * Searches for values whose key value matches a specified search criteria.
    * Returns all values for which <key> <comparator> is true. Comparators
    * are ==, =>, or <=
    * 
    * @param key - key value to search for
    * @param comparator - comparison operation to use for the search.
    * 
    * @return list of values whose keys match the criteria
    */
    @Override
    public List<V> rangeSearch(K key, String comparator) {
    	if (!comparator.contentEquals(">=") && 
            !comparator.contentEquals("==") && 
            !comparator.contentEquals("<=") )
            return new ArrayList<V>();
        return root.rangeSearch(key, comparator);
    }
    
    /**
     * Generates a string representation of the tree.
     * @return string describing the tree
     */
    @Override
    public String toString() {
        Queue<List<Node>> queue = new LinkedList<List<Node>>();
        queue.add(Arrays.asList(root));
        StringBuilder sb = new StringBuilder();
        while (!queue.isEmpty()) {
            Queue<List<Node>> nextQueue = new LinkedList<List<Node>>();
            while (!queue.isEmpty()) {
                List<Node> nodes = queue.remove();
                sb.append('{');
                Iterator<Node> it = nodes.iterator();
                while (it.hasNext()) {
                    Node node = it.next();
                    sb.append(node.toString());
                    if (it.hasNext())
                        sb.append(", ");
                    if (node instanceof BPTree.InternalNode)
                        nextQueue.add(((InternalNode) node).children);
                }
                sb.append('}');
                if (!queue.isEmpty())
                    sb.append(", ");
                else {
                    sb.append('\n');
                }
            }
            queue = nextQueue;
        }
        return sb.toString();
    }
    
    
    /**
     * This abstract class represents any type of node in the tree
     * This class is a super class of the LeafNode and InternalNode types.
     * 
     * @author sapan
     */
    private abstract class Node {
        
        // List of keys
        List<K> keys;
        
        /**
         * Package constructor
         */
        Node() {
            keys = new ArrayList<K>();
        }
        
        /**
         * Inserts key and value in the appropriate leaf node 
         * and balances the tree if required by splitting
         *  
         * @param key
         * @param value
         */
        abstract void insert(K key, V value);

        /**
         * Gets the first leaf key of the tree
         * 
         * @return key
         */
        abstract K getFirstLeafKey();
        
        /**
         * Gets the new sibling created after splitting the node
         * 
         * @return Node
         */
        abstract Node split();
        
        /*
         * (non-Javadoc)
         * @see BPTree#rangeSearch(java.lang.Object, java.lang.String)
         */
        abstract List<V> rangeSearch(K key, String comparator);

        /**
         * 
         * @return boolean
         */
        abstract boolean isOverflow();
        
        public String toString() {
            return keys.toString();
        }
        
    
    } // End of abstract class Node
    
    /**
     * This class represents an internal node of the tree.
     * This class is a concrete sub class of the abstract Node class
     * and provides implementation of the operations
     * required for internal (non-leaf) nodes.
     * 
     * @author sapan
     */
    private class InternalNode extends Node {

        // List of children nodes
        List<Node> children;
        
        /**
         * Package constructor
         */
        InternalNode() {
            super();
            children = new ArrayList<Node>();
            //add empty leaf nodes to children so root works
            children.add(new LeafNode());
        }
        
        /**
         * Split constructor
         */
        InternalNode(List<K> keys, List<Node> children){
        	this.keys = keys;
        	this.children = children;
        }
        
        /**
         * Returns the first leaf key of the first child node
         */
        K getFirstLeafKey() {
        	Node child = children.get(0);
            return child.getFirstLeafKey();   
        }
        
        /**
         * Checks if the current number of children exceeds the branching factor
         */
        boolean isOverflow() {
        	return (children.size() > branchingFactor);
        }
        
        /**
         * inserts a new key-value pair into the node
         */
        void insert(K key, V value) {
        	int childIndex = getChildIndex(key);
            Node child = children.get(childIndex);
            child.insert(key, value);
                        
            if(child.isOverflow()) {
            	Node rightSplitChild = child.split();
            	children.add(childIndex+1, rightSplitChild);
            	keys.add(childIndex, rightSplitChild.getFirstLeafKey());
            }
        }
        
        /**
         * Gets the index of the child that corresponds to the pased in key
         * @param key
         * @return index of child
         */
		private int getChildIndex(K key) {
			int childIndex = 0;
            while(childIndex<keys.size() && (keys.get(childIndex).compareTo(key) < 0)){
            	childIndex++;
            }
			return childIndex;
		}
        
        /**
         * Splits a node into two nodes
         * @return new Right child of the split node
         */
        Node split() {
            
        	//split children
            int length = children.size();
            int midpoint = length/2;            
            List<Node> rightChildren = new ArrayList<Node>(children.subList(midpoint, length));
            children = new ArrayList<Node>(children.subList(0, midpoint));
            
            //split keys
            int keyLength = keys.size();
            int numRightKeys = rightChildren.size()-1;
            List<K> rightKeys = new ArrayList<K>(keys.subList(keyLength - numRightKeys, keyLength));
            keys = new ArrayList<K>(keys.subList(0, children.size()-1));
            
            //make and return the right child
            return new InternalNode(rightKeys, rightChildren);
        }
        
        /**
         * Performs a range search on an internal node
         * 
         * @param key - key value to compare child node keys to
         * @param comparator - comparator for the key comparison
         */
        List<V> rangeSearch(K key, String comparator) {
        	Node child = children.get(getChildIndex(key));
        	return child.rangeSearch(key,comparator);            
        }
    
    } // End of class InternalNode
    
    
    /**
     * This class represents a leaf node of the tree.
     * This class is a concrete sub class of the abstract Node class
     * and provides implementation of the operations that
     * required for leaf nodes.
     * 
     * @author sapan
     */
    private class LeafNode extends Node {
        
        // List of values
        List<V> values;
        
        // Reference to the next leaf node
        LeafNode next;
        
        // Reference to the previous leaf node
        LeafNode previous;
        
        /**
         * Package constructor
         */
        LeafNode() {
            super();
            values = new ArrayList<V>();
        }
        
        /**
         * Split constructor
         */
        LeafNode(LeafNode previousNode, List<K> keys, List<V> values){
        	this.keys = keys;
        	this.values = values;
        	previous = previousNode;
        }
        
        /**
         * Returns the key of the first leaf
         */
        K getFirstLeafKey() {
            return keys.get(0);
        }
        
        /**
         * Determines if the current number of keys exceed the branching factor
         */
        boolean isOverflow() {
            return (keys.size() > branchingFactor);
        }
        
        /**
         * Inserts a new key-value pair into this node
         * @param key - key to insert
         * @param value - value to insert
         */
        void insert(K key, V value) {
            int counter = 0;
            while(counter<keys.size() && (keys.get(counter).compareTo(key) <= 0)){
            	counter++;
            }
            keys.add(counter, key);
            values.add(counter, value);
        }
        
        /**
         * Splits a leaf node and returns the new right-most node
         */
        Node split() {
            int length = keys.size();
            int midpoint = length/2;
            List<K> rightKeys = new ArrayList<K>(keys.subList(midpoint, length));
            List<V> rightValues = new ArrayList<V>(values.subList(midpoint, length));
            
            LeafNode rightSplitChild = new LeafNode(this, rightKeys,rightValues);
            if(this.next != null) {
            	BPTree<K, V>.LeafNode oldNext = this.next;
            	rightSplitChild.next = oldNext;
            	oldNext.previous = rightSplitChild;
            }
            this.next = rightSplitChild;
            
            //reduce current node's keys and values
            this.keys = new ArrayList<K>(keys.subList(0, midpoint));
            this.values = new ArrayList<V>(values.subList(0, midpoint));
            
            return rightSplitChild;
        }
        
        /**
         * Performs a range search on a leaf node
         * @param key - value to compare each key to
         * @param comparator - comparator to use when comparing keys
         */
        List<V> rangeSearch(K key, String comparator) {
        	//search index is equal to or greater than search key
        	int searchIndex = 0;
            while(searchIndex<keys.size() && (keys.get(searchIndex).compareTo(key) < 0)){
            	searchIndex++;
            }
            
            List<V> retVals = new ArrayList<V>();
            
            switch(comparator) {
            	case "==":
            		appendWhileMatching(key, searchIndex, retVals);
            		break;
            	case "<=":
            		if(previous != null) {
            			previous.previousRecurse(retVals);
            		}
            		for(int i = 0; i<searchIndex ; i++) {
            			retVals.add(values.get(i));
            		}
            		appendWhileMatching(key, searchIndex, retVals);
            		break;
            	case ">=":
            		appendGTE(key, searchIndex, retVals);
            		break;
            }
            
            
            return retVals;
        }
        
        /*
         * Recursive helper function used to build return value list
         */
        private void previousRecurse(List<V> retVals) {
        	if(previous != null) {
        		previous.previousRecurse(retVals);
        	}
        	retVals.addAll(values);
        }
        
        /**
         * Starts at searchIndex and appends matching keys to retVals, recursing to the next node
         */
		private void appendWhileMatching(K key, int searchIndex, List<V> retVals) {
			while (searchIndex < keys.size()) {
				if(keys.get(searchIndex).compareTo(key) != 0) {
					return;
				}
				retVals.add(values.get(searchIndex));
				searchIndex++;
			}
			if(next != null) {
				next.appendWhileMatching(key, 0, retVals);
			}	
		}
        
		/**
         * Starts at searchIndex and appends matching keys to retVals, recursing to the next node
         */
		private void appendGTE(K key, int searchIndex, List<V> retVals) {
			while (searchIndex < keys.size()) {
				retVals.add(values.get(searchIndex));
				searchIndex++;	
			}
			if(next != null) {
				next.appendGTE(key, 0, retVals);
			}
		}
		
    } // End of class LeafNode
    
    
    /**
     * Contains a basic test scenario for a BPTree instance.
     * It shows a simple example of the use of this class
     * and its related types.
     * 
     * @param args
     */
    public static void main(String[] args) {
        // create empty BPTree with branching factor of 3
        BPTree<Double, Double> bpTree = new BPTree<>(3);

        // create a pseudo random number generator
        Random rnd1 = new Random();

        // some value to add to the BPTree
        Double[] dd = {0.0d, 0.5d, 0.2d, 0.8d};

        // build an ArrayList of those value and add to BPTree also
        // allows for comparing the contents of the ArrayList 
        // against the contents and functionality of the BPTree
        // does not ensure BPTree is implemented correctly
        // just that it functions as a data structure with
        // insert, rangeSearch, and toString() working.
        List<Double> list = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            Double j = dd[rnd1.nextInt(4)];
            list.add(j);
            bpTree.insert(j, j);
            System.out.println("\n\nTree structure:\n" + bpTree.toString());
        }
        List<Double> filteredValues = bpTree.rangeSearch(-10.1d, "<=");
        System.out.println("Filtered values: " + filteredValues.toString());
    }

} // End of class BPTree
