/** Sllist is a list of integers, which hides the terrible truth
* of the naked ness within **/

public class SLList {
    private static class IntNode {
        public int item;
        public IntNode next;

        public IntNode(int i, IntNode n){
            item = i;
            next =n;
        }
    }
    private IntNode first; //创建一个叫做 first 的 IntNode 盒子

    /** The first item(if it exists)is at sentinel.next 这是一个 invariant **/
    private IntNode sentinel;

    private int size; //代表数据结构中的当前的 amount information

    /** Create a empty SLList.**/
    public SLList(){
        sentinel = new IntNode(63,null);  // 要先创建 sentinel，数字随便写，不需要问它的情况
        sentinel.next = null;
        size = 0;
    }


    public SLList(int x){   // A constructor for SLList
        sentinel = new IntNode(63,null);
        sentinel.next = new IntNode(x,null);
        //first = new IntNode(x,null); // first 指向一个装有 x,null的数据盒子
        size = 1;
    }

    /** adds x to the front of the list **/
    public void addFirst(int x){

        sentinel.next = new IntNode(x,sentinel.next);
        //sentinel = new IntNode(x,sentinel) //错了！因为sentinel 永远不变，不能 re-assign sentinel
        //first = new IntNode(x,first);
        size = size+1;
    }

    /** Returns the first item in the list.**/
    public int getFirst(){
        //return first.item;
        // return sentinel.next 错了！ 不要问 sentinel 的 opinion ，它只是忠实的伙伴
        return sentinel.next.item;
    }

    /** Add an item to the end of the list.**/
    public void addLast(int x){
        size += 1 ;

        IntNode p = sentinel;

        /**
        if (first == null){
            first = new IntNode(x,null);
            return;
        }
        IntNode p = first;
         **/

       //move p until it reaches the end.
        while(p.next != null){
            p = p.next;
        }
        p.next = new IntNode(x,null);
    }

    public int size(){
        return size;
    }

    /** Return the size of the list.
    public int size(){
        IntNode p = first;
        int totalsize = 0;
        while(p.next != null){
            p = p.next;
            totalsize ++;
        }
        return 1+totalsize;
    }

    * Use Recursive method to return the size of the list
    private static int sizerecursive(IntNode p){
        if(p.next == null){
            return 1;
        }
        return 1+sizerecursive(p.next);
    }
    public int sizerecursive(){
        return sizerecursive(first);
    }
    **/

    public static void main(String[] args){
        //SLList L = new SLList(10); // 创建了 a list of one integer, namely: 10
        SLList L = new SLList();
        L.addLast(20);
        //L.addFirst(10);
        //L.addFirst(5);
        //L.addLast(20);

        //System.out.println(L.getFirst());
        System.out.println(L.size());
       // System.out.println(L.size());
        //System.out.println(L.sizerecursive());
    }

}



