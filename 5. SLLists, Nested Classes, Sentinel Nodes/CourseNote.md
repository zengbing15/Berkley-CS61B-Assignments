## 2.2 SLLists

## 本章要点

在第2.1章中，我们构建了IntList类，使用的是 **naked recursive data structure** 裸递归数据结构。为了正确地使用IntList，程序员必须理解并利用递归，即使是简单的与列表相关的任务。这限制了它对新手程序员的有用性，并可能引入一种全新的棘手错误，在实践中，IntList的使用起来相当笨拙，导致代码难以阅读和维护。

受IntList经验的启发，我们现在将构建一个新类SLList，它更类似于程序员在现代语言中使用的列表实现。我们将通过迭代地添加一系列改进来做到这一点。

* Improvement #1 Rebaranding: IntList 改成 IntNode
* Improvement #2: Bureaucracy : SLList 类充当列表用户和 naked data structure 之间的中间人。
* Improvement #3: Public vs. Private  控制访问，private  class 不能被外面使用
* Improvement #4: Nested Classes    比如 IntNode 只被 SLList 使用， 只是 a supporting character 所以挪进来了
    * 如果 nested class 不用 外部 class 的 instance variables/methods ,声明成 **static** 也就是说 static class 不能访问外部的 class’s instance variables/method
    * 添加 size() 时候要注意：如果 size() 用 recursive 方式，就要用 new programming pattern：
        * a **public method** that speaks the language of mortals
        * a **private method** that speaks the language of the gods
        * 这两个方法使用了同样的命名 size 叫做重载overload： two methods with the same name but different signatures are **overloaded**. 
* Improvement #5: Caching  保存重要数据以加快检索速度。
* Improvement #6: The Empty List  虽然更容易表示 empty list:  setting front to null，但在 empty list 后面加元素，会 run  into trouble
    * Improvement #6b: Sentinel Nodes  我们编码中要尽量避免去推理特殊情况。人类只有这么多的工作记忆，因此我们希望尽可能地控制复杂性。
        * empty list 没有 null(空指针)，而是指向 sentinel node , 是我们 faithful companion 等待其他 data 在后面排队。—— 总是存在的，就像在教室第一排的第一个人, 我们不用担心 sentinel node 会不存在，它永远在那里。
        * 总结：我们 遇到了个问题：SLLists had a null first 有一些 SLLists 没有，所以我们修复了这个情况，用 sentinel node ,让所有数据都在一个 sentinel node 后面。这样处理 SLList （addFirst/addLast/getFirst/etc.） handled the same way. 不需要考虑 special cases. 

**Invariants ：创建 sentinel node 的这个思维是设置 invariant**

* An invariant 保证在代码执行期间为真的条件(假设代码中没有bug)
* 真正理解 sentinel node 有多方便，需要真正思考并实践。在Project1中会获得大量的实践。但等到读完这本书的下一部分2.3 后再开始Project1。

## 笔记

### Improvement #1: Rebranding

```
public class IntList {
    public int first;
    public IntList tail;

    public IntList(int f, IntList t){
        first = f;
        tail = t;
    }
```

这个结构叫做 **naked recursive data structure**

* naked linked lists 很难用，因为 Users 可能需要非常了解引用关系才能考虑递归，还是让 users 轻松些吧

### Improvement #2: Bureaucracy



```
/** Sllist is a list of integers, which hides the terrible truth
* of the naked ness within **/

public class SLList {
    public IntNode first; //创建一个叫做 first 的 IntNode 盒子

    public SLList(int x){   // A constructor for SLList
        first = new IntNode(x,null); // first 指向一个装有 x,null的数据盒子
    }
    public static void main(String[] args){
        SLList L = new SLList(10); // 创建了 a list of one integer, namely: 10
        
    }
}
```

这有什么好处？
如果我是个 programmer，我不用写  `new SLList( 10,null ) `SLList 没有这个 property ，只要给 x 就可
比较一下这两种声明：

```
IntNode X = new IntNode(10,null)
SLList Y = new SLList(10)
```

SLList 隐藏了 null link 这一细节，不过目前还不太有用，让我们 添加一个addFirst和getFirst方法作为简单的热身方法。


### addFirst and getFirst

```
 SLList L = new SLList(15);
 L.addFirst(10);
 L.addFirst(5);
 int x = L.getFirst();
 
 IntList L = new IntList(15,null);
 L = new IntList(10,L);
 L = new IntList(5,L);
 int x = L.first;
```


和 原来的 IntList 对比：SLList 只要使用提供的方法就可以了,对比它们俩的数据结构：

* IntList：我们处理 a list 的时候，我们需要 pointers directly into the raw stuff
* SLList: 有一个 middleman 来处理

本质上，SLList 类充当列表用户和 naked data structure 之间的中间人。正如上面在IntList中提到的，有一个潜在的不希望IntList用户拥有指向IntList中间的变量的可能性。正如奥维德(Ovid)所说:凡人不可能不死就见到神，所以也许最好是SLList在那里充当我们的中介。

### Improvement #3: Public vs. Private

```
private IntNode first  // 不能在 SLList class 外面使用
```

可以用 **private** 阻止tricky 操作, 因为通过不了 compile  error：first has private access in SLList.

* 阻止其他 class 使用 members：variable、method、constructor

Why Restrict Access？ —— Hide implementation details from users of your class.  不用 user 理解，我自己修改 private method 也安全。
用车举例：

* Public : 踏板
*  Private: Fuel line

用 public 表示，会表示你声明 you will never ever remove it 


### Improvement #4: Nested Classes

* IntNode 应该是 SLList 的 feature，而不是一个单独的 class file ，所以可move it inside of  anther class declaration 因为 IntNode 在 SLList 的故事中，只是 a supporting character 
* SLList 是 boss
* IntNode 是 subordinate class 从属 class
* 一般会是 nested class 在前,变量声明在下面

```
public class SLList {
   
    private static class IntNode { // nested class definition,如果需要可以设为 private
        public int item;           // static 表示，IntNode 不能访问 first、addFirst、getFirst
        public IntNode next;

        public IntNode(int i, IntNode n){
            item = i;
            next =n;
        }
    }
    private IntNode first; //创建一个叫做 first 的 IntNode 盒子 
    
```

### Why Nested Classes?

Nested Class are useful 当  a class 从属于其他 class 而不能独立成为一个 class ,比如 IntNode 只被 SLList 使用，it’s subordinate to the SLList class.

* nested class 设为 private，如果其他 class never use it

所以这里 IntNode 可以设为 private ，因为其他 class 应该不需要用了

### Static Nested class

如果 nested class 不用 外部 class 的 instance variables/methods ,声明成 **static**

* Static class 不能访问外部的 class’s instance variables/methods

会在 Project1 中有实践

Exercise 2.2.2 删除足够的 static 让 program 编译通过
注意：这里的 static 不能删掉，因为用的是 `Government class, Government greaterTreasury(Government a, Government b) `还在 ` public class Government` 的作用域里面，所以可以用 a.treasury 
要是删了下面的 `Government favorite = Government.greaterTreasury(a, b)`; 会报：non-static method greaterTreasury(Government,Government) cannot be referenced from a static context

还有另一种解释：[What is the reason behind “non-static method cannot be referenced from a static context”? [duplicate]](https://stackoverflow.com/questions/290884/what-is-the-reason-behind-non-static-method-cannot-be-referenced-from-a-static)

```
public class Government {
    ...
    public static Government greaterTreasury(Government a, Government b) {
        if (a.treasury > b.treasury) {
            return a;
        }
        return b;
    }

    ...
    public class Explorer {
        public void doStuff(Government a, Government b) {
            Government favorite = Government.greaterTreasury(a, b);
            System.out.println("The best government has treasury " + favorite.treasury);
        }
    }
}
```

### addLast() and size()

目前我们做了如下 improvements：

* Rebranding: `IntList` - `IntNode`
* Bureaucracy: `SLList`
* Access Control: `public` -` private`
* Nested Class: Bring `IntNode` into `SLList`

接下来，为了 促进接下来的改进，并为我们的SLList类提供更多的功能, let’s add:

* .addLast(int x)
* .size()

在写 size() 时候，如果试图用 recursive method,会被卡住，出现奇怪的问题这是因为：

* size() 是为 SLList 写的，而 SLList 的 data structure 不是 recursive 的，并没有 SLList pointers，我们在之前的 improvements 中把 naked recursive data structure 用加入中间人的方式隐藏了，所以要用 recursive method 就要用这个 **common pattern** when working with recursive data structure:
    * a **public method** that speaks the language of mortals
    * a **private method** that speaks the language of the gods

```
    /** Use Recursive method to return the size of the list**/
    private static int sizerecursive(IntNode p){ // 与底层naked data structure 交互的private method
        if(p.next == null){
            return 1;
        }
        return 1+sizerecursive(p.next);
    }
    public int sizerecursive(){  // the middleman:public method 需要
        return sizerecursive(first);
    }
```

所以总结一下，addLast() 用 iterative 之前讲过，而如果 size() 用 recursive 方式，就要用 new programming pattern.

这里，我们有两个方法，都命名为sizerecursive 。这在Java中是允许的，因为它们有不同的参数。我们说 two methods with the same name but different signatures are **overloaded**. 。有关重载方法的更多信息，请参阅Java的官方文档。


### Improvement #5: Caching

考虑我们上面写的size方法。假设大小为1000的列表需要2s。我们预计，对于大小为1,000,000的列表，size方法将花费2,000s，因为计算机要遍历1000倍的列表项才能到达最后。对于大型列表，size 方法太慢不行，所以我们要让 size() 运算的更快一些

我的建议是，我们不用每次调用大小的时候都匆忙地计算大小。这可能会花很长时间，我们要先留出一些关于大小的信息:


```
   /** adds x to the front of the list **/
    public void addFirst(int x){
        first = new IntNode(x,first);
        size = size+1;
    }

 ...
    /** Add an item to the end of the list.**/
    public void addLast(int x){
        size += 1 ;

        IntNode p = first;
       /** move p until it reaches the end.**/
        while(p.next != null){
            p = p.next;
        }
        p.next = new IntNode(x,null);
    }

    public int size(){
        return size;
    }
```

解决: Maintain a special size variable that **caches** the size of the list:

* **Caching:**putting aside data to speed up retrieval. 保存重要数据以加快检索速度。

Object-oriented programming approach means that we let the object control everything. 面向对象编程方法意思是代码里让 object 控制所有的事，我们告诉 object - addFirst()/getFirst()/addLast()/size() ， 你们不只是 do all of your tasks，还要 track the size.

### Improvement #6: The Empty List

总结 SLList vs. IntList 对比之后的好处：

*  size() 更快更方便
* 用 SLList 不用看到 IntList class
    * 用起来更简单
    * 更有效的addFirst方法
    * avoid errors or malfeasance
* 更容易表示 empty list:  setting front to null:

```
  private int size; //代表数据结构中的当前的 amount information

    /** Create a empty SLList.**/
    public SLList(){
        first = null;
        size = 0;
    }
    
    SLList **L** = new *SLList*();
    L.addLast(20);
```

不过有个 BUG：如果我们试着在 empty list 后面加元素，会 run  into trouble

```
Exception in thread "main" java.lang.NullPointerException: Cannot read field "next" because "p" is null
```

fix this bug:

```
 /** Add an item to the end of the list.**/
    public void addLast(int x){
        size += 1 ;

        if (first == null){
            first = new IntNode(x,null);
            return;
        }

        IntNode p = first;
       /** move p until it reaches the end.**/
        while(p.next != null){
            p = p.next;
        }
        p.next = new IntNode(x,null);
    }
```

不过 addLast() 有点 bloated,我们接下来会采用另一个更好的 an alternate strategy


### Improvement #6b: Sentinel Nodes

不过这个方法可能不会像接下来我要解释的东西那么 elegant  我们将介绍 sentinel node。

开始之前要给大家一点动力：具有特殊情况的代码很难推理。
人类只有这么多的工作记忆，所以当你需要思考代码关于规则外情况等等，它只是比我们熟悉的一种事物以一致的方式运行要难。
因此我们编码中要尽量避免去推理特殊情况。人类只有这么多的工作记忆，因此我们希望尽可能地控制复杂性。对于像SLList这样的简单数据结构，特殊情况的数量很少。更复杂的数据结构，比如树，可能会变得更加丑陋。

在你们练习之前，这看起来有点难理解，但是我希望这个例子告诉你为什么这些想法会让代码更简单，即使一开始可能看起来很疯狂。

我们需要 one consistent story ，not a bunch of separate rules 所以我们要让所有的SLList 是一样的，即使它们是空的。它们应该有相同的结构，相同的something ，发现这个“something”有时很棘手

**Solution:** Create a special node that is always there! —— sentinel node

* empty list 没有 null(空指针 )，它指向 sentinel node , 是我们 faithful companion 等待其他 data 在后面排队。—— 总是存在的，就像在教室第一排的第一个人, 我们不用担心 sentinel node 会不存在，它永远在那里。



```
 /** The first item(if it exists)is at sentinel.next **/
    private IntNode sentinel;
    private int size; //代表数据结构中的当前的 amount information

    /** Create a empty SLList.**/
    public SLList(){
        sentinel = new IntNode(63,null);  // 要先创建 sentinel，数字随便写，因为我们不问它的情况
        sentinel.next = null;
        size = 0;
    }


    public SLList(int x){   // A constructor for SLList
        sentinel = new IntNode(63,null);
        sentinel.next = new IntNode(x,null);
        size = 1;
    }

    /** adds x to the front of the list **/
    public void addFirst(int x){

        sentinel.next = new IntNode(x,sentinel.next);
        //sentinel = new IntNode(x,sentinel) //错了！因为sentinel 永远不变，不能 re-assign sentinel
        size = size+1;
    }

    /** Returns the first item in the list.**/
    public int getFirst(){
        // return sentinel.next 错了！不要问 sentinel 的 opinion ，它只是忠实的伙伴
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
```

虽然看起来复杂许多，不过在 larger system 比如 Project1(build the link list based deck), 这样会更简单，强烈建议用 sentinel node.

  a sentinel node is a dummy node , a faithful companion, always there for you.

* 重命名 first 为 sentinel
* sentinel  never null,  always 指向 the sentinel node
* sentinel node’s item 需要是 some integer, 任何数字都可以
* 修复 constructors and methods 与 sentinel nodes 兼容

总结：我们 遇到了个问题：SLLists had a null first 有一些 SLLists 没有，所以我们修复了这个情况，用 sentinel node ,让所有数据都在一个 sentinel node 后面。这样处理 SLList （addFirst/addLast/getFirst/etc.） handled the same way. 不需要考虑 special cases. 


### Invariants —— 创建 sentinel node 的这个思维是设置 invariant

An invariant 保证在代码执行期间为真的条件(假设代码中没有bug)

An SLList with a sentinel node 至少有如下 invariant:

* The sentinel reference alway points to a sentinel node.
* The first node(if it exists),is always at `sentinel.next`
* The size variable 始终是添加的 item 的总数。

Invariants 简化对代码的推理:

* 可以假设为真，以简化代码(例如 addLast() 不需要担心null)
* 必须确保methods preserve invariants.

因为它给了你一个清单，你可以思考的事情，有些 人会写在纸上，不过创建 invariant 更有效

要真正理解 sentinel node 有多方便，需要真正思考并实践。在Project1中会获得大量的实践。但等到读完这本书的下一部分2.3 后再开始Project1。