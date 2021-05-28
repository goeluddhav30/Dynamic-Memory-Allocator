// Implements Dictionary using Doubly Linked List (DLL)
// Implement the following functions using the specifications provided in the class List

public class A1List extends List {

    private A1List  next; // Next Node
    private A1List prev;  // Previous Node 

    public A1List(int address, int size, int key) { 
        super(address, size, key);
    }
    
    public A1List(){
        super(-1,-1,-1);
        // This acts as a head Sentinel

        A1List tailSentinel = new A1List(-1,-1,-1); // Intiate the tail sentinel
        
        this.next = tailSentinel;
        tailSentinel.prev = this;
    }

    public A1List Insert(int address, int size, int key)
    {
        if(this.next == null) return null;
        // Assert: this != tail sentinel as it can never be accessed from the outside
        A1List newnode = new A1List(address, size, key); 
        newnode.next = this.next;
        newnode.prev = this;
        this.next.prev = newnode;
        this.next = newnode;
        return newnode;
    }

    public boolean Delete(Dictionary d) 
    {
        if(d == null) return false;
        A1List current;
        for( current = this.getFirst(); current != null; current = current.getNext()){
            if(current.key == d.key){
                if((current.address == d.address) && (current.size == d.size)){
                    current.prev.next = current.next;
                    current.next.prev = current.prev;
                    current.key = current.size = current.address = 0;
                    current.prev = current.next = null;
                    return true;
                }    
            }
        }
        // Assert: Either d is not found in the DLL or it is a sentinel node
        return false;
    }

    public A1List Find(int k, boolean exact)
    { 
        A1List current;
        for( current = this.getFirst(); current != null; current = current.getNext()){
            if(exact){
                if(current.key == k) return current;
            }
            else if(current.key >= k) return current;
        }
        return null;
    }

    public A1List getFirst()
    {
        A1List current = this;
        if(current.prev == null) current = current.next;
        else if(current.next == null) current = current.prev;
        // Assert: Node isn't sentinel if list is non-empty.
        if((current.next == null) || (current.prev == null))   // If node is sentinel, return null.
            return null;                                       // Empty list
        // Assert: Non-empty list && current doesn't point to a sentinel node.
        while(current.prev.prev != null) current = current.prev;
        return current;
    }
    
    public A1List getNext() 
    {
        A1List current = this;
        if((current.next == null) || (current.next.next == null))    // tail.getNext() returns null
            return null;
        return current.next;
    }

    public boolean isSentinel(){             // Only checks if all parameters are -1 or not
        return ((this.key == -1) && (this.size == -1) && (this.address == -1));
    }

    public boolean sanity()
    {
        try{
            A1List current, fast, slow;
            current = fast = slow = this;
            while((fast != null) && (fast.next != null)){
                fast = fast.next.next;
                slow = slow.next;
                if(slow == fast){           // loop found in forward iteration
                    return false;
                    }
            }
            fast = slow = current;
            while((fast != null) && (fast.prev != null)){
                fast = fast.prev.prev;
                slow = slow.prev;
                if(slow == fast){          // loop found in backward iteration
                    return false;
                }
            }
            if(current.prev == null){
                if((!current.isSentinel()) || (current.next.prev != current)) return false;          // Checking conditions of head node
                current = current.next;
            }
            else if(current.next == null){
                if((!current.isSentinel()) || (current.prev.next != current)) return false;          // Checking conditions of tail node
                current = current.prev;
            }   
            // Assert: Node isn't sentinel if list is non-empty
            if((current.next == null) || (current.prev == null)){
                if(!current.isSentinel()) return false;
                return true;
            }
            // Assert: Non-empty list and current points to some node in the list
            A1List temp = current;
            while(temp.next != null){
                if(temp != temp.next.prev) return false;
                temp = temp.next;
            }
            if(!temp.isSentinel()) return false;
            temp = current;
            while(temp.prev != null){
                if(temp != temp.prev.next) return false;
                temp = temp.prev;
            }
            if(!temp.isSentinel()) return false;
            return true;
        }
        catch(Exception e){
            return false;
        }
    }

}


