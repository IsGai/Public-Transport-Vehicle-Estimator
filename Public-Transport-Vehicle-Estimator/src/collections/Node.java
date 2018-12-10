/*
 * Class description: Implementation of Node for use with linked list.
 */
package collections;

import java.io.Serializable;

public class Node<E> implements Serializable
{
	private static final long serialVersionUID = 1L;
private E data;
private Node<E> link;   

/*
 * Description: Constructor for the class.
 * 
 * Arguments: initialData - the data the node will contain upon creation.
 * initialLink - the link the node will contain upon creation.
 * 
 * Precondition: Types of initialData should match the specified node type of
 * initialLink.
 * 
 * Postcondition: An object of type Node will be created.
 * 
 * Throws: None.
 */
public Node(E initialData, Node<E> initialLink)
{
   data = initialData;
   link = initialLink;
}


/*
 * Description: Creates a node and adds a link of itself to the calling object.
 * 
 * Arguments: element - the element that will go in the node being created.
 * 
 * Precondition: Types should properly match.
 * 
 * Postcondition: A node will be created with the link of the calling object
 * amd element passed to the method. The calling object will now link to the
 * new node.
 * 
 * Throws: None.
 */
public void addNodeAfter(E element)   
{
   link = new Node<E>(element, link);
}          


//Accesor method for data.
public E getData( )   
{
   return data;
}


//Accesor method for link.
public Node<E> getLink( )
{
   return link;                                               
} 
 
 
/*
 * Description: Copies a chain of nodes.
 * 
 * Arguments: source- the chain of nodes you are copying from.
 * 
 * Precondition: None.
 * 
 * Postcondition: A copy of the chain of nodes will exist and the head will
 * be returned.
 * 
 * Throws: None.
 */
public static <E> Node<E> listCopy(Node<E> source)
{
   Node<E> copyHead;
   Node<E> copyTail;
   
   
   if (source == null)
      return null;
      
   
   copyHead = new Node<E>(source.data, null);
   copyTail = copyHead;
   

   while (source.link != null)
   {
      source = source.link;
      copyTail.addNodeAfter(source.data);
      copyTail = copyTail.link;
   }


   return copyHead;
}



/*
 * Description: Finds length of node chain.
 * 
 * Arguments: head - the first node.
 * 
 * Precondition: None.
 * 
 * Postcondition: An int representing the chain/list length will be returned.
 * 
 * Throws: None.
 */  
public static <E> int listLength(Node<E> head)
{
   Node<E> cursor;
   int answer;
   
   answer = 0;
   for (cursor = head; cursor != null; cursor = cursor.link)
      answer++;
     
   return answer;
}
       

/*
 * Description: Searches a linked list for a datum.
 * 
 * Arguments: head - the head node of the linked list.
 * target - the datum you are searching for.
 * 
 * Precondition: Object types should be compatible.
 * 
 * Postcondition: If found it will return the first node containing the
 * desired datum.
 * 
 * Throws: None.
 * 
 */
public static <E> Node<E> listSearch(Node<E> head, E target)
{
   Node<E> cursor;
   
   if (target == null)
   {  
      for (cursor = head; cursor != null; cursor = cursor.link)
         if (cursor.data == null)
            return cursor;
   }
   else
   { 
      for (cursor = head; cursor != null; cursor = cursor.link)
         if (target.equals(cursor.data))
            return cursor;
   }
     
   return null;
}                                           

/*
 * Description: Removes the node after the calling object.
 * 
 * Arguments: None.
 * 
 * Precondition: There should be a node after the called object.
 * 
 * Postcondition: The desired node will be removed.
 * 
 * Throws: None.
 */  
public void removeNodeAfter( )   
{
   link = link.link;
}          

//Mutator for data. 
public void setData(E newData)   
{
   data = newData;
}                                                               

//Mutator for link. 
public void setLink(Node<E> newLink)
{                    
   link = newLink;
}
}
        