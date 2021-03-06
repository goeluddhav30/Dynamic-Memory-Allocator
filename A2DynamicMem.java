// Class: A2DynamicMem
// Implements Degragment in A2. No other changes should be needed for other functions.

public class A2DynamicMem extends A1DynamicMem {
      
    public A2DynamicMem() {  super(); }

    public A2DynamicMem(int size) { super(size); }

    public A2DynamicMem(int size, int dict_type) { super(size, dict_type); }

    // In A2, you need to test your implementation using BSTrees and AVLTrees. 
    // No changes should be required in the A1DynamicMem functions. 
    // They should work seamlessly with the newly supplied implementation of BSTrees and AVLTrees
    // For A2, implement the Defragment function for the class A2DynamicMem and test using BSTrees and AVLTrees. 
    //Your BST (and AVL tree) implementations should obey the property that keys in the left subtree <= root.key < keys in the right subtree. How is this total order between blocks defined? It shouldn't be a problem when using key=address since those are unique (this is an important invariant for the entire assignment123 module). When using key=size, use address to break ties i.e. if there are multiple blocks of the same size, order them by address. Now think outside the scope of the allocation problem and think of handling tiebreaking in blocks, in case key is neither of the two. 
    public void Defragment() {
        Dictionary newTree;
        Dictionary finalTree;
        if(type == 2){
            newTree = new BSTree();
            finalTree = new BSTree();
        }
        else if(type == 3){
            newTree = new AVLTree();
            finalTree = new AVLTree();
        }
        else return;
        Dictionary current = freeBlk.getFirst();
        if((current == null) || (current.getNext() == null)) return;   
        // Assert : freeBlk has more than 1 element
        for( ; current != null ; current = current.getNext()){
            newTree.Insert(current.address, current.size, current.address);
        }
        current = newTree.getFirst().getNext();
        Dictionary last = newTree.getFirst();
        while(current != null){
            if(last.address+last.size == current.address){
                int la = last.address, ls = last.size, cs = current.size;
                newTree.Delete(current);
                newTree.Delete(last);
                last = newTree.Insert(la, ls+cs, la);
                current = last.getNext();
            }
            else{
                last = current;
                current = current.getNext();
            }
        } 
        for( current = newTree.getFirst(); current != null; current = current.getNext()){
            finalTree.Insert(current.address, current.size, current.size);
        }
        freeBlk = finalTree;
        newTree = finalTree = null;
        return ;
    }
}