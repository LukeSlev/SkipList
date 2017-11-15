import java.util.ArrayList;

/**
 * Skip-List
 * The {@code SkipList} class
 * @param <K>           {@code K} key of each skip list node
 * @param <V>           {@code V} value of each skip list node
 */
public class SkipList<K extends Comparable<K>, V> {

    /**
     * The {@code Node} class for {@code SkipList}
     */
    private class Node {
        public K key;
        public V value;
        public ArrayList<Node> forwards = new ArrayList<Node>();
        public Node(K key, V value, int level) {
            this.key = key;
            this.value = value;
            for (int i = 0; i < level; i++)
                forwards.add(null);
        }
        public String toString() {
            return String.format("%s(%s,%d)", value, key, forwards.size());
        }
    }

    /**
     * Level of the skip list. An empty skip list has a level of 1
     */
    private int level = 1;

    /**
     * Size of the skip list
     */
    private int size = 0;
    
    /**
     * Root node of the red black tree
     */
    private Node head = new Node(null,null,1);

    /**
     * Search the Skip-List for a key
     * @param list       {@code SkipList<K,V>} the skip list to search
     * @param key       {@code K} key of the element
     * @return          {@code V} value of the found element
     */
    public V search(SkipList<K, V> list, K key) {
    		Node cur = list.head;
		for (int i=list.level-1; i>=0; i--){
			while (cur.forwards.get(i) != null && cur.forwards.get(i).key.compareTo(key) < 0) {
				cur = cur.forwards.get(i);
			}
		}
		cur = cur.forwards.get(0);
		if (cur != null && cur.key.compareTo(key) == 0) {
			return cur.value;
		} else {
			return null;
		}
    }
    
    /**
     * Search the Skip-List for a key and return the node
     * @param key       {@code K} key of the element
     * @param update    {@code ArrayList<Node>} copy of forwards arraylist used for updating
     * @return          {@code Node} found node
     */
    public Node search (K key, ArrayList<Node> update) {
		Node x = head;
		for (int i=level-1; i>=0; i--) {
		    while((x.forwards.get(i) != null) && ((x.forwards.get(i).key.compareTo(key) < 0))) {
		    		x = x.forwards.get(i);
		    }
		    update.set(i, x);
		  }
		return x.forwards.get(0);
    }	
    
    
    /**
     * Generates a random level for a new Node
     * @return          {@code int} random level
     */
    public int randomNode() {
    		int newLevel = 1;
    		int MaxLevel = 32;
    		while (java.lang.Math.random() < 0.5) {
    			newLevel += 1;
    		} return java.lang.Math.min(newLevel, MaxLevel);
    }
    
    /**
     * Adjust the level of the skip list for the addition of a new node
     * @param newLevel       {@code int} new level for the skip list
     */
    private void AdjustHead(int newLevel) {
        Node temp = head;
        head = new Node(null, null, newLevel);
        for (int i=0; i<=level-1; i++)
          head.forwards.set(i, temp.forwards.get(i));
        level = newLevel;
      }
    
    /**
     * Insert an new element into the skip list
     * @param key       {@code K} key of the new element
     * @param value     {@code V} value of the new element
     */
    public void insert(K key, V value) {
        // TODO: Lab 5 Part 1-1 -- skip list insertion
    		if (search(this, key) != null) {
    			return;
    		}
    		int newLevel = randomNode();
    		//System.out.println("n " + newLevel);
    		if (newLevel > level-1) {
    			AdjustHead(newLevel);
    		}
    		ArrayList<Node> update = new ArrayList<Node>();
    		for (int i=0; i<this.level;i++) {
    			update.add(null);
    		}
    		Node x = search(key, update);
		x = new Node(key, value, newLevel);
		for (int i=0; i<=newLevel-1; i++) {
			x.forwards.set(i, update.get(i).forwards.get(i));
		    update.get(i).forwards.set(i, x); 
		}
		size++;
    }
    

    /**
     * Remove an element by the key
     * @param key       {@code K} key of the element
     * @return          {@code V} value of the removed element
     */
    public V remove(K key) {
        // TODO: Lab 5 Part 1-2 -- skip list deletion
    		Node x = head;
    		V ret = null;
    		ArrayList<Node> update = new ArrayList<Node>();
		for (int i=0; i<this.level;i++) {
			update.add(null);
		}
    		x = search(key,update);
    		if (x.key.compareTo(key) == 0) {
    			for (int i=0;i<=level-1; i++) {
    				if (update.get(i).forwards.get(i) != x) {
    					break;
    				}
    				update.get(i).forwards.set(i,x.forwards.get(i));
    			}ret = x.value;
    			while (level > 1 && head.forwards.get(level-1) == null) {
    				level -= 1;
    			}
    			while (level < head.forwards.size()) {
    				head.forwards.remove(head.forwards.size()-1);
    			}
    		}
        return ret;
    }

    /**
     * Search for an element by the key
     * @param key       {@code K} key of the element
     * @return          {@code V} value of the target element
     */
    public V search(K key) {
        // TODO: Lab 5 Part 1-3 -- skip list node search
        
        return search(this, key);
    }

    /**
     * Get the level of the skip list
     * @return          {@code int} level of the skip list
     */
    public int level() {
        return level;
    }

    /**
     * Get the size of the skip list
     * @return          {@code int} size of the skip list
     */
    public int size() {
        return size;
    }

    /**
     * Print the skip list
     * @return          {@code String} the string format of the skip list
     */
    public String toString() {
        // TODO: Lab 5 Part 1-4 -- skip list printing
    		StringBuilder ret = new StringBuilder();
    		Node cur = head;
    		while(cur != null) {
    			for (int i=0; i<cur.forwards.size();i++) {
    				System.out.print(" [ " + cur.value + " ] ");
    			} System.out.println();
    			cur = cur.forwards.get(0);
		}

        return null;
    }

    /**
     * Main entry
     * @param args      {@code String[]} Command line arguments
     */
    public static void main(String[] args) {
        SkipList<Integer, String> list = new SkipList<Integer, String>();
        int[] keys = new int[10];
        for (int i = 0; i < 10; i++) {                          // Insert elements
            keys[i] = (int) (Math.random() * 200);
            //System.out.println(keys[i]);
            list.insert(keys[i], "\"" + keys[i] + "\"");
        }

        System.out.println(list);

        for (int i = 0; i < 10; i += 3) {
            int key = keys[i];
            // Search elements
            System.out.println(String.format("Find element             %3d: value=%s", key, list.search(key)));
            // Remove some elements
            System.out.println(String.format("Remove element           %3d: value=%s", key, list.remove(key)));
            // Search the removed elements
            System.out.println(String.format("Find the removed element %3d: value=%s", key, list.search(key)));
        }

        System.out.println(list);
    }

}
