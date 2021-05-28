// Class: A1DynamicMem
// Implements DynamicMem
// Does not implement defragment (which is for A2).

public class A1DynamicMem extends DynamicMem {
      
    public A1DynamicMem() {
        super();
    }

    public A1DynamicMem(int size) {
        super(size);
    }

    public A1DynamicMem(int size, int dict_type) {
        super(size, dict_type);
    }

    public void Defragment() {
        return ;
    }

    public void showBlock(Dictionary d){
        Dictionary current;
        for( current = d.getFirst(); current != null; current = current.getNext()){
            System.out.println(current.address+" "+current.size+" "+current.key);
        }
    }

    // In A1, you need to implement the Allocate and Free functions for the class A1DynamicMem
    // Test your memory allocator thoroughly using Doubly Linked lists only (A1List.java).
    // While inserting into the list, only call insert at the head of the list
    // Please note that ALL insertions in the DLL (used either in A1DynamicMem or used independently as the “dictionary” class implementation) are to be made at the HEAD (from the front).
    // Also, the find-first should start searching from the head (irrespective of the use for A1DynamicMem). Similar arguments will follow with regards to the ROOT in the case of trees (specifying this in case it was not so trivial to anyone of you earlier)
    public int Allocate(int blockSize) {
        if(blockSize <= 0) return -1;
        Dictionary match = freeBlk.Find(blockSize, false);
        if(match == null) return -1;
        allocBlk.Insert(match.address, blockSize, match.address);
        if(match.size > blockSize){
            freeBlk.Insert(match.address+blockSize, match.size-blockSize, match.size-blockSize);
        }
        int val = match.address;
        freeBlk.Delete(match);
        return val;
    } 
    // return 0 if successful, -1 otherwise
    public int Free(int startAddr) {
        Dictionary match = allocBlk.Find(startAddr, true);
        if(match == null) return -1;
        freeBlk.Insert(match.address, match.size, match.size);
        allocBlk.Delete(match);
        return 0;
    }
}