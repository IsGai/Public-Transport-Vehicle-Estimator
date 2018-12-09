package collections;



import java.io.Serializable;
import java.util.EmptyStackException;


public class LinkedStack<E> implements Cloneable, Serializable
{
	private static final long serialVersionUID = 1L;
private Node<E> head; 

/*
 * Description: Constructor for the class.
 * 
 * Arguments: None.
 * 
 * Precondition: Type used in place of generic should be cloneable.
 * 
 * Postcondition: An object of type LinkedStack will be created.
 * 
 * Throws: None.
 */   
public LinkedStack( )
{
   head = null;
}


/*
 * Description: Clones the calling object.
 * 
 * Arguments: None.
 * 
 * Precondition: Object type used in place of generic should be cloneable.
 * 
 * Postcondition: A clone of the called LinkedStack object will be returned.
 * 
 * Throws: Runtime exception - if cloneable was not implemented
 *  in the typed being used in the place of generic.
 */
@SuppressWarnings("unchecked")
public LinkedStack<E> clone( )       
{  
   LinkedStack<E> answer;
   
   try
   {
      answer = (LinkedStack<E>) super.clone( );
   }
   catch (CloneNotSupportedException e)
   { 
    
      throw new RuntimeException
      ("This class does not implement Cloneable");
  }
   
  
   answer.head = Node.listCopy(head);
   
   return answer;
}        


/*
 * Description: Checks if the stack is empty. 
 * 
 * Arguments: None.
 * 
 * Precondition: LinkedStack object should be initialized.
 * 
 * Postcondition: If stack is empty true will be returned. If not
 * false will be returned.
 * 
 * Throws: None.
 */
public boolean isEmpty( )
{
   return (head == null);
}


/*
 * Description: Checks the next item without removing it.
 * 
 * Arguments: None.
 * 
 * Precondition: Stack should not be empty.
 * 
 * Postcondition: The next item will be returned.
 * 
 * Throws: EmptyStackException - if the stack is empty.
 */  
public E peek( )   
{
   if (head == null)
      throw new EmptyStackException( );
   return head.getData( );
}


/*
 * Description: Pops the stack.
 * 
 * Arguments: None.
 * 
 * Precondition: Stack should not be empty.
 * 
 * Postcondition: The next index will be returned and the item will be removed
 * from the stack.
 * 
 * Throws: EmptyStackException - if the stack is empty.
 */   
public E pop( )
{
   E answer;
   
   if (head == null)    
      throw new EmptyStackException( );
   
   answer = head.getData( );
   head = head.getLink( );
   return answer;
}    


/*
 * Description: Add an item to the stack.
 * 
 * Arguments: item - The item you are adding.
 * 
 * Precondition: item should be the correct type.
 * 
 * Postcondition: The item will be added to the stack.
 * 
 * Throws: None.
 */   
public void push(E item)
{
   head = new Node<E>(item, head);
}
           

//Returns current size of calling object.
public int size( )   
{

   return Node.listLength(head);
}

}