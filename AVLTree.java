import java.util.*;

// Class: Height balanced AVL Tree
// Binary Search Tree

public class AVLTree extends BSTree {
    
    private AVLTree left, right;     // Children. 
    private AVLTree parent;          // Parent pointer. 
    private int height;  // The height of the subtree
        
    public AVLTree() { 
        super();
        // This acts as a sentinel root node
        // How to identify a sentinel node: A node with parent == null is SENTINEL NODE
        // The actual tree starts from one of the child of the sentinel node !.
        // CONVENTION: Assume right child of the sentinel node holds the actual root! and left child will always be null.
        
    }

    public AVLTree(int address, int size, int key) { 
        super(address, size, key);
        this.height = 0;
    }

    // Implement the following functions for AVL Trees.
    // You need not implement all the functions. 
    // Some of the functions may be directly inherited from the BSTree class and nothing needs to be done for those.
    // Remove the functions, to not override the inherited functions.
    
    // Helper functions below :

    // Returns +1 if first argument has a higher priority, -1 for lesser priority and 0 for equal priority. TC => O(1).
    private int compare(AVLTree a, AVLTree b){
        if(a.key > b.key) return 1;
        if(a.key < b.key) return -1;
        if(a.address > b.address) return 1;
        if(a.address < b.address) return -1;
        return 0;
    }

    // Returns root of AVL Tree when called from any node and null if tree is empty. TC => O(logn)
    private AVLTree root(AVLTree a){
        while(a.parent != null) a = a.parent; 
        return a.right;
    }

    // Iteratively updates the heights of all nodes from a to root(a), along the path. TC => O(logn)
    private void updateHeights(AVLTree a){
        if(a == null) return;
        while(a.parent != null){
            int l = ht(a.left), r = ht(a.right);
            a.height = Math.max(l, r) + 1;
            a = a.parent;
        }
    }

    // Checks if a given node is imbalanced or not. TC => O(1).
    private boolean isImbalanced(AVLTree a){
        // Assert : a is not null.
        int l = ht(a.left) , r = ht(a.right);
        return (Math.abs(l-r) > 1);
    }

    //Returns the height of a given node. TC => O(1)
    private int ht(AVLTree a){
        if(a==null) return -1;
        return a.height;
    }

    // Only called on a node which is imbalanced, rebalances it using some rotation and then updates heights in O(logn).
    private void rebalance(AVLTree a){
        if(ht(a.left) - ht(a.right) > 1){
            if(ht(a.left.left) - ht(a.left.right) >= 0) a = rightRotate(a);   // R - R rotate
            else a = leftRightRotate(a);                                      // L - R rotate
            a.right.height = Math.max(ht(a.right.left), ht(a.right.right)) + 1;
            updateHeights(a.left);
        }
        else{
            if(ht(a.right.right) >= ht(a.right.left)) a = leftRotate(a);       // L - L rotate
            else a = rightLeftRotate(a);                                       // R - L rotate
            a.left.height = Math.max(ht(a.left.left), ht(a.left.right)) + 1;
            updateHeights(a.right);
        }
    }

    // All 4 rotation functions mentioned below take O(1) time.
    private AVLTree leftRotate(AVLTree a){
        AVLTree node = a.right;
        a.right = node.left;
        if(node.left != null) node.left.parent = a;
        node.parent = a.parent;
        if(a.parent.right == a) a.parent.right = node;
        else a.parent.left = node;
        node.left = a;
        a.parent = node;
        return node; 
    }

    private AVLTree rightRotate(AVLTree a){
        AVLTree node = a.left;
        a.left = node.right;
        if(node.right != null) node.right.parent = a;
        node.parent = a.parent;
        if(a.parent.right == a) a.parent.right = node;
        else a.parent.left = node;
        node.right = a;
        a.parent = node;
        return node;
    }

    private AVLTree leftRightRotate(AVLTree a){
        a.left = leftRotate(a.left);
        return rightRotate(a);
    }

    private AVLTree rightLeftRotate(AVLTree a){
        a.right = rightRotate(a.right);
        return leftRotate(a);
    }

    // Checks for an imbalance on every node on the path from a to its root, and calls rebalance() if required. 
    // TC => O(logn)
    private void checkBalance(AVLTree a){
        if((a == null) || (a.parent == null)) return;
        if(isImbalanced(a)) rebalance(a);
        checkBalance(a.parent);
    }

    // De-allocates a deleted node (nullyfying its pointers and setting int values to 0). TC => O(1)
    private void nullify(AVLTree a){
        a.right = a.left = a.parent = null;
        a.size = a.address = a.key = a.height = 0;
    }

    // Helper function for getNext(), called when right subtree of a node is 0. Returns the 1st right ancestor. 
    // TC => O(logn)
    private AVLTree firstrightAncestor(){
        AVLTree current = this;
        while(current.parent != null){
            if(compare(current, this) == 1) return current;
            current = current.parent;
        }
        return null;
    }

    // End of helper functions. Main functions defined below :

    // Creates and inserts the node depending on the values of the parameters passed as arguments, while maintaining
    // all the AVL tree invariants. TC => O(logn)
    @Override
    public AVLTree Insert(int address, int size, int key) 
    { 
        AVLTree newnode = new AVLTree(address, size, key);
        AVLTree current = this;
        while(current.parent != null) current = current.parent; 
        // Assert: current points at sentinel node
        if(current.right == null){                   // Empty BSTree
            current.right = newnode;
            newnode.parent = current;
            updateHeights(newnode);
            checkBalance(newnode);
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
                    updateHeights(newnode);
                    checkBalance(newnode);
                    return newnode;
                }
                current = current.left;
            }
            else{
                if(current.right == null){
                    current.right = newnode;
                    newnode.parent = current;
                    updateHeights(newnode);
                    checkBalance(newnode);
                    return newnode;
                }
                current = current.right;
            }
        }
    }

    // Finds and deletes the dictionary element passed as an argument, from the tree, while maintaining the 
    // invariants of an AVL tree, in O(logn) time
    @Override
    public boolean Delete(Dictionary e)
    {
        if(e == null) return false;
        AVLTree current = root(this);
        if(current == null) return false;
        // Assert : Tree is non-empty and current points to the actual root of the tree.
        while(current != null){
            if(current.key == e.key){
                if((current.size == e.size) && (current.address == e.address)){
                    // When required node has 2 children :
                    if((current.left != null) && (current.right != null)){
                        AVLTree successor = current.getNext();
                        current.key = successor.key; 
                        current.size = successor.size; 
                        current.address = successor.address; 
                        if(successor.parent.right == successor) successor.parent.right = successor.right;
                        else successor.parent.left = successor.right;
                        if(successor.right != null) successor.right.parent = successor.parent;
                        updateHeights(successor.parent);
                        checkBalance(successor.parent);
                        nullify(successor);
                    }
                    // When required node has no left child (Can have right child) :
                    else if(current.left == null){
                        if(current.parent.left == current) current.parent.left = current.right;
                        else current.parent.right = current.right;
                        if(current.right != null) current.right.parent = current.parent;  // When current is a leaf node.
                        updateHeights(current.parent);
                        checkBalance(current.parent);
                        nullify(current);
                    }
                    // When required node has only left child :
                    else if(current.right == null){
                        if(current.parent.left == current) current.parent.left = current.left;
                        else current.parent.right = current.left;
                        current.left.parent = current.parent;
                        updateHeights(current.parent);
                        checkBalance(current.parent);
                        nullify(current);
                    }
                    return true;
                }
            }
            // Use the search property of an AVL tree to reduce our search to the appropriate subtree of current :
            if((current.key > e.key) || ((current.key == e.key) && (current.address > e.address))) current = current.left;
            else current = current.right;
        }
        return false;
    }
        
    // Finds and returns a node in the tree with the required key and minimum address (depending on value of exact).
    // TC => O(logn).
    @Override
    public AVLTree Find(int k, boolean exact)
    { 
        AVLTree current = root(this);
        if(current == null) return null;
        //Assert : Tree is non-empty and current points to the actual root of the tree.
        if(exact){
            AVLTree store = null;
            // Finding node with key = k, and minimum address :
            while(current != null){
                if(current.key < k) current = current.right;
                else if(current.key > k) current = current.left;
                else{
                    store =current; current = current.left;
                }
            }
            return store;
        }
        else{
            AVLTree minnode = null;
            // Finding node with key >= k, and using address to break the tie if 2 nodes have same(min) key value.
            while(current != null){
                if(current.key >= k){
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

    // Returns the first element in the inorder traversal of tree. TC => O(logn).
    @Override
    public AVLTree getFirst()
    { 
        AVLTree current = root(this);
        if(current == null) return null;     // Empty list
        //Assert: current is non-empty root of AVLTree
        while(current.left != null) current = current.left;
        return current;
    }

    // Returns the next element in the inorder traversal of the tree. TC => O(logn).
    @Override
    public AVLTree getNext()
    {
        AVLTree current = this;
        if(current.parent == null) return null;             // Corner case when getNext() is called on the sentinel node
        if(current.right != null) {
            current = current.right;
            while(current.left != null) current = current.left;
            return current;
        }
        return current.firstrightAncestor();
    }

    // Some helper functions for checking sanity of given AVLTree : 

    // Checks if the invariants for sentinel node hold on the argument passed. TC => O(1).
    private boolean sentinelCheck(){
        return ((this.address == -1) && (this.size == -1) && (this.key == -1) && (this.left == null));
    }

    // Returns false if a loop is detected (if a node is visited twice) or if the pointers are not appropriately set for 
    // any node and both its children.   TC => O(n), as we traverse the entire tree (all nodes).
    private boolean checkLoop(AVLTree cur, HashSet<AVLTree> h){
        if(cur == null) return true;
        if(h.contains(cur)) return false;
        if((cur.right != null) && ((cur.right.parent != cur) || (compare(cur, cur.right) != -1))) return false;
        if((cur.left != null) && ((cur.left.parent != cur) || (compare(cur, cur.left) != 1))) return false;
        h.add(cur);
        return (checkLoop(cur.left, h) && checkLoop(cur.right, h));
    }

    // Checks for the height balance property at every node in O(n) time total.
    private boolean isHeightBalanced(AVLTree a){
        if(a == null) return true;
        // Check if height of node is 1 more than the maximum of height of both children. 
        if(ht(a) == Math.max(ht(a.left),ht(a.right)) + 1) return false;
        // Check if the difference in the heights of both children of node is more than 1 or not.
        if(isImbalanced(a)) return false;
        return (isHeightBalanced(a.left) && isHeightBalanced(a.right));
    }

    // Checks the sanity of the given AVL tree in O(n) space and O(n) time, for a tree with n nodes. 
    @Override
    public boolean sanity()
    { 
        AVLTree current, fast, slow;
        current = fast = slow = this;
        if((this.parent == null) && (!this.sentinelCheck())) return false; 
        // Checking for loop while traversing to the parent using Floyd's algorithm :
        while((fast != null) && (fast.parent != null)){
            fast = fast.parent.parent;
            slow = slow.parent;
            if(slow == fast) return false;
        } 
        //Assert: No loop in parent pointers from current to sentinel node
        while(current.parent != null) current = current.parent;
        // Assert : current points to sentinel node
        if(!current.sentinelCheck()) return false;
        if(current.right == null) return true;
        //Assert: Non-empty list
        if(current.right.parent != current) return false;
        current = current.right;
        //Assert : current is root of actual tree
        HashSet<AVLTree> hs = new HashSet<AVLTree>();
        if(!checkLoop(current, hs)) return false;
        //Assert: No loop found in BST and also all the pointers are well set.
        if(current == null) return true;
        AVLTree last = current.getFirst();
        current = last.getNext();
        for( ; current != null; current = current.getNext()){
            if(compare(current, last) != 1) return false;          // BST property not satisfied during inorder traversal.
            last = current;
        }
        current = root(this);
        return isHeightBalanced(current);
    }
}


