# Project0 编码思路

### Project0 Assignment 

Project0 需要我们构建 [2048 游戏](https://gabrielecirulli.github.io/2048/)的核心逻辑，已经整合了所有的GUI代码，处理按键，以及大量其他脚手架，我们要做的是在 Model.java 文件中填写4个方法，管理用户进行游戏操作时发生的行为。这四个方法分别需要实现：

* public static boolean emptySpaceExists(Board b)：遍历 board ，如果存在空的 tile，返回 true
* public static boolean maxTileExists(Board b) ：如果 board 上有值为 2048 的 tile，返回true。 （拼出 2048 ，游戏获胜了）
* public static boolean atLeastOneMoveExists(Board b)：
    * if  there is empty space or  maxtile ; return true; 有 empty space 
    * else there are two adjcent tiles with the same value.  如果没有 emptyspace, 有相邻元素也可以 merge
*  public boolean tilt(Side side)：Main Task: Building the Game Logic 实现，操作后实现移动所有 tile，更新 score，setChanged() 更改 board 状态
    * 只考虑 Up 的情况:
        * UpNoMerge —— done
        * UpBasicMerge —— done
        * **UpTripleMerge & UpTrickyMerge** —— TBD 

### 代码如何解决

```
public static boolean emptySpaceExists(Board b)
```

 实际上就是对一个二维数组进行遍历，如果有 null 值就返回 true。Board b 是一个二维数组 b.size() 返回数组的长度。b.tile(col, row) 表示 Board 上的 tile ，tile 为 null 则表示存在 empty space. 


```
public static boolean maxTileExists(Board b) 
```

这个方法的逻辑同上，不过注意 b.tile(col, row) 值为 null 是有意义的，表示 empty space，并不是 `NullPointerException`，所以需要做一次判断跳过这里，让遍历继续，否则会报 `java.lang.NullPointerException`


```
public static boolean atLeastOneMoveExists(Board b)
```

这里考察的是双层循环的知识，在 Up&down 方向上找是否有 two adjacent tiles， col 作为外层循环不变，处理 row 的循环条件；在 Left&Right 的情况下，row 作为外层循环不变，处理 col 的循环条件。



```
public boolean tilt(Side side)
```

NoMerge

* setboard 到 NORTH，开始处理 merge 的情况
* 首先 atLeastOneMoveExists 有 empty space，或者如果没有 emptyspace, 有相邻元素也可以 merge
    * Nomerge: 
        * if  相邻元素一个不为0，一个为0；
        * else 相邻元素都不为0而且两个值不一样
            * else 相邻元素不为0且两个值一样，count++  BasicMerge:
                * if count = 3 Triple Merge 
                    * 处理 board move 情况


* UpTripleMerge: 

```
only the leading 2 tiles should merge
```


现在fromTo数组将为每个索引指明原始位置的块所去的位置。结果将有最终的值。从这两个信息中，您还可以知道哪些块被合并了。当result[fromTo[i]] != row[i]时，初始位置i的块被合并。你也知道一个block将移动的距离:i - fromTo[i]。简而言之，您拥有为每个块设置动画的所有信息。


* UpTrickyMerge: 

```
*/** A tricky merge.*
* ** * The tricky part here is that the 4 tile on the bottom row shouldn't*
* ** * merge with the newly created 4 tile on the top row. If you're failing*
* ** * this test, try seeing how you can ensure that the bottom 4 tile doesn't*
* ** * merge with the newly created 4 tile on top.*/*
```

    * 这里棘手的部分是最下面一行的 tile 即使 value 一样，也不应该 与最上面的 tile 合并，因为一次 move 只merge 一次
    * 如果这个测试失败，试着看看你如何能确保底部的 tile 不和 新创建的 top tile 合并


```
 Tile t = b.tile(col,row)
Tile tmove = board.move( c,r, t) 
```

*  相当于把 tile 从 (col,row)移到了 （c,r）位置
* 再返回这次移动是不是 merge

```
boolean changed // 
```

/* Tilt  the board toward SIDE 。如果改变了板子，则返回true。

* 1. 如果两个Tile物体在运动方向上**相邻而且有相同的值**，它们合并到一个Tile，并且 value 是原来的两倍，这个 new value 被添加到 score instance variable 实例变量中
* 2. 合并后的 tile 将不会在该 tile 上再次合并。所以每一步，每一个 tile 最多只能是 be part of at most one merge(perhaps zero)
* 3. 当三个相邻的 tile 在运动方向上有相同的值，*然后在运动方向上的  two leading tiles 合并，*而后面的 tile 没有。

### 编程体验
Project0 是 CS61B 课程中的第一个项目作业，这个项目代码里用到了许多课程没有涉及到以及不会涉及到的 Java 语法，这里作者的想法是，我们真正工作中遇到的项目代码基本上是这样，使用了不熟悉或者完全没见过的语法和编码逻辑，我们必须要用加 log 或 debug 的方式去理解，我觉得这是 CS61B 课程区别于其他课程的高明之处，课程内容不掉书袋，很有实用性。里面的 quiz 也对促进思考，理解游戏规则，代码逻辑起到了很大的作用。我想在完成其他项目的时候，也非常需要用某些方式来检验自己的思路是否正确。（例如本篇源码分析）
