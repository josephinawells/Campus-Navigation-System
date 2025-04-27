import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.List;


public class HashtableMap<KeyType, ValueType> implements MapADT<KeyType, ValueType> {
  protected LinkedList<Pair>[] table = null;
   private int size;
    
  protected class Pair {

    public KeyType key;
    public ValueType value;

    public Pair(KeyType key, ValueType value) {
      this.key = key;
      this.value = value;
    }
  }

  @SuppressWarnings("unchecked")
  public HashtableMap(int capacity) {
      table = (LinkedList<Pair>[]) new LinkedList[capacity];
      size = 0;
  }
    
  public HashtableMap(){// with default capacity = 64
    this(64);
  }
  
  /**
   * Adds a new key,value pair/mapping to this collection.It is ok that the value is null.
   * @param key the key of the key,value pair
   * @param value the value that key maps to
   * @throws IllegalArgumentException if key already maps to a value
   * @throws NullPointerException if key is null
   */
  @Override
  public void put(KeyType key, ValueType value) throws IllegalArgumentException {
  //if the key is null then excpetion is thrown
    if (key == null) {
      throw new NullPointerException("Key can't be null");
    }

    //index is calculated using hash code
    int index = Math.abs(key.hashCode()) % table.length;

    //if the table is empty then a new LinkedList is created
    if (table[index] == null) {
      table[index] = new LinkedList<>();
    }
    
    //goes through the table and if the key is already found in the table exception is thrown
    for (Pair pair : table[index]) {
      if (pair.key.equals(key)) {
        throw new IllegalArgumentException("Key is already in the table");
      }
    }

    //key is added to the table
    table[index].add(new Pair(key, value));
    size++;

    //table capacity doubled if load factor is greater or equal to 80%
    if ((double) size / table.length >= 0.8) {
      resize();
    }
    
  }
  
  /**
   * Checks whether a key maps to a value in this collection.
   * @param key the key to check
   * @return true if the key maps to a value, and false is the
   *         key doesn't map to a value
   */
  @Override
  public boolean containsKey(Object key) {
    //index calculated using hash code
    int index = Math.abs(key.hashCode()) % table.length;
    //finds the bucket at the index
    LinkedList<Pair> bucket = table[index];
    
    if (bucket != null) {
      //goes through the bucket and finds wehre the keys match
      for (int i = 0; i < bucket.size(); i++) {
        if (bucket.get(i).key.equals(key)) {
          //if the key is found return true
          return true;
        }
      }
    }
    //if key isn't found return false
    return false;
  }
  
  /**
   * Retrieves the specific value that a key maps to.
   * @param key the key to look up
   * @return the value that key maps to
   * @throws NoSuchElementException when key is not stored in this
   *         collection
   */
  @Override
  public ValueType get(Object key) throws NoSuchElementException {
 //calculates index
    int index = Math.abs(key.hashCode()) % table.length;
    LinkedList<Pair> bucket = table[index];
    if (bucket != null) {
      //goes through the list in the bucket to find the wanted key
      for (int i = 0; i < bucket.size(); i++) {
        Pair pair = bucket.get(i);
        //when found return that key's value
        if (pair.key.equals(key)) {
          return pair.value;
        }
      }
    }
    //if not found then exception is thrown
    throw new NoSuchElementException("key does not exist in collection");
  }
  
  /**
   * Remove the mapping for a key from this collection.
   * @param key the key whose mapping to remove
   * @return the value that the removed key mapped to
   * @throws NoSuchElementException when key is not stored in this
   *         collection
   */
  @Override
  public ValueType remove(Object key) throws NoSuchElementException {
     //calculate index
    int index = Math.abs(key.hashCode()) % table.length;
    LinkedList<Pair> bucket = table[index];
    if (bucket != null) {
      //go through the bucket
      for (int i = 0; i < bucket.size(); i++) {
        Pair pair = bucket.get(i);
        if (pair.key.equals(key)) {
          //gets the value type of what we want to remove
          ValueType value = pair.value;
          bucket.remove(i);
          size--;
          return value;
        }
      }
    }
    //if key not found then exception thrown
    throw new NoSuchElementException("key does not exist in collection");
  }
  
  /**
   * Removes all key,value pairs from this collection.
   */
  @Override
  public void clear() {
     for (int i = 0; i < table.length; i++) {
      if (table[i] != null) {
        table[i] = null;
      }
    }
    size = 0;
  }
  
  /**
   * Retrieves the number of keys stored in this collection.
   * @return the number of keys stored in this collection
   */
  @Override
  public int getSize() {
      return size;
  }
  
  /**
   * Retrieves this collection's capacity.
   * @return the size of the underlying array for this collection
   */
  @Override
  public int getCapacity() {
     return table.length;
  }

    /**
   * Dynamically grows the hashtable by doubling its capacity and rehashing, whenever its load
   * factor becomes greater than or equal to 80%.
   */
  @SuppressWarnings("unchecked")
  private void resize() {
    //stores the old table
    LinkedList<Pair>[] pastTable = table;
    //doubles the capacity of the table
    table = (LinkedList<Pair>[]) new LinkedList[pastTable.length * 2];
    size = 0;

    //rehash everything that was in the table
    for (LinkedList<Pair> bucket : pastTable) {
      if (bucket != null) {
        for (Pair pair : bucket) {
          //reinserts what was already there
          put(pair.key, pair.value);
        }
      }
    }
  }

     /**                                                                                                         
     * Retrieves this collection's keys.                                                                         
     * @return a list of keys in the underlying array for this collection                                        
     */
    @Override
    public List<KeyType> getKeys(){
	List<KeyType> keys = new LinkedList<>();
	for(int i = 0; i < table.length; i++){
	    if(table[i] != null){
		for(Pair pair : table[i]){
		    keys.add(pair.key);
		}
	    }
	}
	return keys;
    }

    
  /**
   * Test the put() and get() methods from the HashtableMap class
   */
  @Test
  public void test1() {
    // creates a new hashtable
    HashtableMap<String, Integer> map = new HashtableMap<>(5);
    // puts objects in the map
    map.put("purple", 2);
    map.put("pink", 1);
    map.put("orange", 3);

    // checks that the get function gives the correct item with the given value
    assertEquals(2, map.get("purple"));
    assertEquals(1, map.get("pink"));
    assertEquals(3, map.get("orange"));
  }

  /**
   * Tests that the contains method works for objects in the hashtable and objects that are not
   * present
   */
  @Test
  public void test2() {
    HashtableMap<String, Integer> map = new HashtableMap<>(5);

    map.put("giraffe", 1);
    map.put("octopus", 2);

    // checking that the contain method has these keys
    assertTrue(map.containsKey("giraffe"));
    assertTrue(map.containsKey("octopus"));
    assertFalse(map.containsKey("zebra"));

  }

  /**
   * Tests that the remove method should correctly delete a key and reduce the size by one
   */
  @Test
  public void test3() {
    HashtableMap<String, Integer> map = new HashtableMap<>(10);

    map.put("goldfish", 1);
    map.put("dog", 2);
    map.put("cat", 3);

    // making sure what is removed is dog
    assertEquals(2, map.remove("dog"));
    // checks that the dog key isn't there
    assertFalse(map.containsKey("dog"));
    // checks that the size has decreased by one
    assertEquals(2, map.getSize());
  }

  /**
   * Tests that the clear method should empty the table completely
   */
  @Test
  public void test4() {
    HashtableMap<String, Integer> map = new HashtableMap<>(10);

    map.put("goldfish", 1);
    map.put("dog", 2);
    map.put("cat", 3);

    map.clear();

    // checks that the hashtable size is now 0
    assertEquals(0, map.getSize());
    // checks that the keys are not there
    assertFalse(map.containsKey("goldfish"));
    assertFalse(map.containsKey("dog"));
    assertFalse(map.containsKey("cat"));
  }

  /**
   * Tests the get method, and that it should throw NoSuchElementException when there are missing
   * keys
   */
  @Test
  public void test5() {
    HashtableMap<String, Integer> map = new HashtableMap<>(10);

    map.put("goldfish", 1);
    map.put("dog", 2);
    map.put("cat", 3);
   
    //tests it gives the correct value with the key
    assertEquals(1, map.get("goldfish"));
    
    //tests the exception is thrown when a key doens't exist
    assertThrows(NoSuchElementException.class, () -> {
      map.get("snake");
    });
  }
}
