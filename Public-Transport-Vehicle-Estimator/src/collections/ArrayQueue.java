/*
 * Class description: Implementation of ArrayQueue data structure that 
 * implements Cloneable and Serializable.
 */
package collections;

import java.io.Serializable;
import java.util.NoSuchElementException;


public class ArrayQueue<E> implements Cloneable, Serializable {
	private static final long serialVersionUID = 1L;


	private E[] data;
	private int quantity;
	private int head;
	private int tail;

	/*
	 * Description: Constructor for when the calling program does not specify a
	 * starting size.
	 * 
	 * Arguments: None.
	 * 
	 * Precondition: Class used in place of generic should be cloneable.
	 * 
	 * Postcondition: New ArrayQueue of size 20 will be created.
	 * 
	 * Throws: None.
	 */
	public ArrayQueue() {
		final int INITIAL_CAPACITY = 20;
		quantity = 0;
		data = (E[]) new Object[INITIAL_CAPACITY];

	}

	/*
	 * Description: Constructor for when the calling program does specify a starting
	 * size.
	 * 
	 * Arguments: initialCapacity - The size you would like the ArrayQueue to start
	 * as.
	 * 
	 * Precondition: Class used in place of generic should be cloneable.
	 * 
	 * Postcondition: New ArrayQueue of size desired will be created.
	 * 
	 * Throws: None.
	 */
	public ArrayQueue(int initialCapacity) {
		if (initialCapacity < 0)
			throw new IllegalArgumentException("initialCapacity is negative: " + initialCapacity);
		quantity = 0;
		data = (E[]) new Object[initialCapacity];

	}

	/*
	 * Description: Clones the calling object.
	 * 
	 * Arguments: None.
	 * 
	 * Precondition: Class used in place of generic should be cloneable.
	 * 
	 * Postcondition: Returns a clone of calling object.
	 * 
	 * Throws: RuntimeException if class used in place of generic class is not
	 * cloneable.
	 */
	public ArrayQueue<E> clone() {
		ArrayQueue<E> answer;

		try {
			answer = (ArrayQueue<E>) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException("This class does not implement Cloneable");
		}

		answer.data = data.clone();

		return answer;
	}

	/*
	 * Description: Method to add new datum to ArrayQue.
	 * 
	 * Arguments: item - The data you would like to add.
	 * 
	 * Precondition: item should be of the same type as being used in ArrayQueue.
	 * 
	 * Postcondition: item will be added to the ArrayQueue.
	 * 
	 * Throws: None.
	 */
	public void add(E item) {
		if (quantity == data.length) {
			ensureCapacity(quantity * 2 + 1);
		}

		if (quantity == 0) {
			head = 0;
			tail = 0;
		} else
			tail = nextIndex(tail);

		data[tail] = item;
		quantity++;
	}

	/*
	 * Description: Method for extending the length of ArrayQueue used internally.
	 * 
	 * Arguments: minimumCapacity - The size the program would like to extend the
	 * ArrayQueue to.
	 * 
	 * Precondition: Logically mimimumCapacity should be larger than data.size.
	 * 
	 * Postcondition: ArrayQueue will be extended.
	 * 
	 * Throws: None.
	 */
	private void ensureCapacity(int minimumCapacity) {
		E[] biggerArray;
		int n1, n2;

		if (data.length >= minimumCapacity)
			return;
		else if (quantity == 0)
			data = (E[]) new Object[minimumCapacity];
		else if (head <= tail) {
			biggerArray = (E[]) new Object[minimumCapacity];
			System.arraycopy(data, head, biggerArray, head, quantity);
			data = biggerArray;
		} else {
			biggerArray = (E[]) new Object[minimumCapacity];
			n1 = data.length - head;
			n2 = tail + 1;
			System.arraycopy(data, head, biggerArray, 0, n1);
			System.arraycopy(data, 0, biggerArray, n1, n2);
			head = 0;
			tail = quantity - 1;
			data = biggerArray;
		}
	}

//Returns capacity.
	public int getCapacity() {
		return data.length;
	}

//True if empty and false if not empty.
	public boolean isEmpty() {
		return (quantity == 0);
	}

	/*
	 * Description: Method to find the next index after a passed index. Allows for
	 * circular array functionality.
	 * 
	 * Arguments: i - The prior index you are checking.
	 * 
	 * Precondition: i should not be beyond current capacity.
	 * 
	 * Postcondition: The next index will be returned.
	 * 
	 * Throws: None.
	 */
	private int nextIndex(int i) {
		if (++i == data.length)
			return 0;
		else
			return i;
	}

	/*
	 * Description: Removes the top datum.
	 * 
	 * Arguments: None.
	 * 
	 * Precondition: Should not be empty.
	 * 
	 * Postcondition: Returns the removed datum and removes it from the ArrayQueue.
	 * 
	 * Throws: Exception - if ArrayQueue is empty.
	 */
	public E remove() throws Exception {
		E answer;

		if (quantity == 0)
			throw new Exception("Queue underflow");
		answer = data[head];
		head = nextIndex(head);
		quantity--;
		return answer;
	}

//Returns current size.
	public int size() {
		return quantity;
	}

	/*
	 * Description: Trims ArrayQueue to size to save space.
	 * 
	 * Arguments: None.
	 * 
	 * Precondition: ArrayQueue object should be initialized.
	 * 
	 * Postcondition: ArrayQueue will be trimmed to size.
	 * 
	 * Throws: None.
	 */
	public void trimToSize() {
		E[] trimmedArray;
		int n1, n2;

		if (data.length == quantity)

			return;
		else if (quantity == 0)

			data = (E[]) new Object[0];
		else if (head <= tail) {
			trimmedArray = (E[]) new Object[quantity];
			System.arraycopy(data, head, trimmedArray, head, quantity);
			data = trimmedArray;
		} else {
			trimmedArray = (E[]) new Object[quantity];
			n1 = data.length - head;
			n2 = tail + 1;
			System.arraycopy(data, head, trimmedArray, 0, n1);
			System.arraycopy(data, 0, trimmedArray, n1, n2);
			head = 0;
			tail = quantity - 1;
			data = trimmedArray;
		}
	}
}
