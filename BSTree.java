import java.util.HashSet;

// Class: Implementation of BST in A2
// Implement the following functions according to the specifications provided in Tree.java

public class BSTree extends Tree {

    private BSTree left, right;     // Children.
    private BSTree parent;          // Parent pointer.
        
    public BSTree(){  
        super();
        // This acts as a sentinel root node
        // How to identify a sentinel node: A node with parent == null is SENTINEL NODE
        // The actual tree starts from one of the child of the sentinel node!.
        // CONVENTION: Assume right child of the sentinel node holds the actual root! and left child will always be null.
    }    

    public BSTree(int address, int size, int key){
        super(address, size, key); 
    }

    private int compare(BSTree a, BSTree b){
        if(a.key > b.key) return 1;
        if(a.key < b.key) return -1;
        if(a.address > b.address) return 1;
        if(a.address < b.address) return -1;
        return 0;
    }

    public BSTree Insert(int address, int size, int key) 
    { 
        BSTree newnode = new BSTree(address, size, key);
        BSTree current = this;
        while(current.parent != null) current = current.parent; 
        // Assert: current points at sentinel node
        if(current.right == null){                   // Empty BSTree
            current.right = newnode;
            newnode.parent = current;
            return newnode;
        }    
        current = current.right;
        // Assert: current points at root of BSTree
        while(true){
            int com = compare(current, newnode);
            if(com == 0) return null;             //Node already present in the tree
            if(com == 1){
                if(current.left == null){
                    current.left = newnode;
                    newnode.parent = current;
                    return newnode;
                }
                current = current.left;
            }
            else{
                if(current.right == null){
                    current.right = newnode;
                    newnode.parent = current;
                    return newnode;
                }
                current = current.right;
            }
        }
    }

    private void nullify(BSTree a){
        a.right = a.left = a.parent = null;
        a.size = a.address = a.key = 0;
    }

    public boolean Delete(Dictionary e)
    { 
        if(e == null) return false;
        BSTree current = this;
        while(current.parent != null) current = current.parent;
        if(current.right == null) return false;
        current = current.right;
        while(current != null){
            if(current.key == e.key){
                if((current.size == e.size) && (current.address == e.address)){
                    // When required node has 2 children :
                    if((current.left != null) && (current.right != null)){
                        BSTree successor = current.getNext();
                        current.key = successor.key; 
                        current.size = successor.size; 
                        current.address = successor.address; 
                        if(successor.parent.right == successor) successor.parent.right = successor.right;
                        else successor.parent.left = successor.right;
                        if(successor.right != null) successor.right.parent = successor.parent;
                        nullify(successor);
                    }
                    // When required node has no left child :
                    else if(current.left == null){
                        if(current.parent.left == current) current.parent.left = current.right;
                        else current.parent.right = current.right;
                        if(current.right != null) current.right.parent = current.parent;
                        nullify(current);
                    }
                    // When required node has only left child :
                    else if(current.right == null){
                        if(current.parent.left == current) current.parent.left = current.left;
                        else current.parent.right = current.left;
                        current.left.parent = current.parent;
                        nullify(current);
                    }
                    return true;
                }
            }
            if((current.key > e.key) || ((current.key == e.key) && (current.address > e.address))) current = current.left;
            else current = current.right;
        }
        return false;
    }
        
    public BSTree Find(int key, boolean exact)
    { 
        BSTree current = this;
        while(current.parent != null) current = current.parent;
        if(current.right == null) return null;
        current = current.right;
        if(exact){
            BSTree store = null;
            while(current != null){
                if(current.key < key) current = current.right;
                else if(current.key > key) current = current.left;
                else{
                    store =current; current = current.left;
                }
            }
            return store;
        }
        else{
            BSTree minnode = null;
            while(current != null){
                if(current.key >= key){
                    if((minnode == null) || (compare(minnode, current) == 1)){
                        minnode = current;
                    }
                    current = current.left;               
                }
                else current = current.right;
            }
            return minnode;
        }
    }

    public BSTree getFirst()
    { 
        BSTree current = this;
        if((current.parent == null) && (current.right == null)) return null;     // Empty list
        while(current.parent != null) current = current.parent;
        current = current.right;
        //Assert: current is not the sentinel node
        while(current.left != null) current = current.left;
        return current;
    }

    private BSTree firstRightAncestor(){
        BSTree current = this;
        while((current.parent != null) && (current.parent.right == current)) current = current.parent;
        return current.parent;
    }

    public BSTree getNext()
    { 
        BSTree current = this;
        if(current.parent == null) return null;                // Corner case when getNext() is called on the sentinel node
        if(current.right != null) {
            current = current.right;
            while(current.left != null) current = current.left;
            return current;
        }
        return current.firstRightAncestor();
    }

    private boolean isSentinel(){
        return ((this.address == -1) && (this.size == -1) && (this.key == -1) && (this.left == null));
    }

    private boolean checkLoop(BSTree cur, HashSet<BSTree> h){
        if(cur == null) return true;
        if(h.contains(cur)) return false;
        if((cur.right != null) && ((cur.right.parent != cur) || (compare(cur, cur.right) != -1))) return false;
        if((cur.left != null) && ((cur.left.parent != cur) || (compare(cur, cur.left) != 1))) return false;
        h.add(cur);
        return (checkLoop(cur.left, h) && checkLoop(cur.right, h));
    }

    public boolean sanity()
    { 
        BSTree current, fast, slow;
        current = fast = slow = this;
        if((this.parent == null) && (!this.isSentinel())) return false;    // when this is sentinel node but doesn't satisfy condition
        while((fast != null) && (fast.parent != null)){
            fast = fast.parent.parent;
            slow = slow.parent;
            if(slow == fast) return false;
        } 
        //Assert: No loop in parent pointers from current to sentinel node
        while(current.parent != null) current = current.parent;
        if(!current.isSentinel()) return false;
        if(current.right == null) return true;
        //Assert: Non-empty list
        if(current.right.parent != current) return false;
        current = current.right;
        HashSet<BSTree> hs = new HashSet<BSTree>();
        if(!checkLoop(current, hs)) return false;
        //Assert: No loop found in BST and also all the pointers are well set.
        if(current == null) return true;
        BSTree last = current.getFirst();
        current = last.getNext();
        for( ; current != null; current = current.getNext()){
            if(compare(current, last) != 1) return false;            // BST property not satisfied during inorder traversal.
            last = current;
        }
        return true;
    }

}


 


