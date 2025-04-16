import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.LinkedList;

public class HashtableMap<KeyType, ValueType> implements MapADT<KeyType, ValueType> {
  protected LinkedList<Pair>[] table = null;
  
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
  public void put(Object key, Object value) throws IllegalArgumentException {
    // TODO Auto-generated method stub
    
  }
  
  /**
   * Checks whether a key maps to a value in this collection.
   * @param key the key to check
   * @return true if the key maps to a value, and false is the
   *         key doesn't map to a value
   */
  @Override
  public boolean containsKey(Object key) {
    // TODO Auto-generated method stub
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
    // TODO Auto-generated method stub
    return null;
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
    // TODO Auto-generated method stub
    return null;
  }
  
  /**
   * Removes all key,value pairs from this collection.
   */
  @Override
  public void clear() {
    // TODO Auto-generated method stub
    
  }
  
  /**
   * Retrieves the number of keys stored in this collection.
   * @return the number of keys stored in this collection
   */
  @Override
  public int getSize() {
    // TODO Auto-generated method stub
    return 0;
  }
  
  /**
   * Retrieves this collection's capacity.
   * @return the size of the underlying array for this collection
   */
  @Override
  public int getCapacity() {
    // TODO Auto-generated method stub
    return 0;
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
    assertEquals("dog", map.remove("dog"));
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
    assertEquals(1, map.get("golfish"));
    
    //tests the exception is thrown when a key doens't exist
    assertThrows(NoSuchElementException.class, () -> {
      map.get("snake");
    });
  }
}
