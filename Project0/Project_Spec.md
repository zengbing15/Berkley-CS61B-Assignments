## Project0 Spec 笔记：

### The Game 游戏规则

* 1.两个相同值的tile合并成包含初始数的两倍的瓦片。
* 2.合并后的贴图将不会在那个倾斜位置再次合并。有[X, 2, 2, 4]，其中X表示一个空白区域，我们将砖块向左移动，我们应该得到[4,4,X, X]，而不是[8,X, X, X]。这是因为最左边的4已经是合并的一部分，所以不应该再合并。
   —— 也就是一次操作，只合并一次，即使运动方向上有相邻的相同值，不过再操作一次还是可以的。
* 3.当运动方向上的三个相邻贴图有相同的数字时，那么运动方向上的前两个贴图合并，而后面的贴图不合并。例如，如果我们使用[X, 2, 2, 2]并将砖块向左移动，我们将得到[4,2,X, X]而不是[2,4,X, X]。


### Assignment Philosophy and Program Design

介绍 Project0 的 design pattern: MVC & Observer 大概了解即可，需要了解 overall architecture of the code 

high level skeleton code tour：https://www.youtube.com/watch?v=3YbIOga6ZdQ

* Tile ： 唯一需要用的是 value(),返回 tile 的值，例如 Tile t 对应一个值为8的 tile,那么 t.value() 返回8
    * toString() 调用这个会返回 tile 的 value,col,row ——也就是一串代表 tile 的字符
* Side
    * 用Side s = Side.north 这样的语法
    * 举例：像public static void printSide(sides)这样的函数，用:printSide(Side.NORTH) 会把 NORTH 传给函数
    * 就是表示 side 东南西北，里面具体的函数应该用不上
* Model 游戏的状态
    * 会用到 board 和 score 变量
    * hashCode() equals() 会在接下来的课程中学到
* Board board 的状态
    * private Tile[][] values
    * //67
        public Tile tile(int col, int row) {
        return vtile(col, row, viewPerspective);
        }

### My Assignment 修改和完成 Model class ：
* public static boolean emptySpaceExists(Board b)：使用Board类的 tile(int col, int row)和 size()方法；用 TestEmptySpace.java 文件夹进行测试
    * 快速写方法的概述：https://www.youtube.com/watch?v=13rdFndFNXc&feature=youtu.be
        * 运行 TestEmptySpace.java 看报错，必须用 tile() 例如 b.tile(0,0)
* public static boolean maxTileExists(Board b) 
    * 如果棋盘上的任何一张牌都等于获胜牌2048，该方法将返回true。 就是拼出 2048 ，游戏获胜了
    * 注意要 用MAX_PIECE ，是 Model 类中的常量 
    * TestMaxTileExists.java中的测试应该会通过
* public static boolean atLeastOneMoveExists(Board b)
    * 如果有任何有效操作（上，下，左，右），应该返回true,
    * 有效 move ： board 上至少有一个空位；有两个相邻的 tile 有相同值
    * TestAtLeastOneMoveExists.java 
* Main Task: Building the Game Logic 实现 public boolean tilt(Side side)
    * 通过 TestEmptySpace, TestMaxTileExists and TestAtLeastOneMoveExists.再做这个，挺难的有心理准备
    * tilt 方法 是移动所有的 tile ，还要做两件事：
        * 更新 score instance variable ,以反映所有tile合并的总值:将两个4合并成8，两个2s合并成4，所以分数应该增加8 + 4 = 12
        * 如果 board 有任何变化，我们必须 set the changed *local variable* to true 因为在 tilt skeleton 调用了一个 setChanged()方法，将通知 GUI 有东西要绘制，所以我们自己 only modifying the changed local variable.
        * *only call* ** *move* ** *on a given tile once per call to* ** *tilt*. 所有tile的移动必须使用 board 类的 move()每次调用 tilt 时，只在给定的 tile 上调用一次 move
    * 如何开始编写这种方法的快速概述 https://www.youtube.com/watch?v=abFbbK1QY2k&feature=youtu.be
        * 控制代码复杂度很重要，如果不 elegant 很可能会实现不出来—— 所以要特别思考代码逻辑
        * 通过3个测试后，run main() 会弹出board上面有任意数字；每次run 都会弹出不一样的
        * 给出了错误行为代码作为示例
        * 要利用 helper method
        * 如果走到死胡同，不要怕删掉代码重新开始
    * Tips
        * 建议从只考虑 up 方向开始，也就是 side.NORTH ；提供了一个 TestUpOnly 类：testUpNoMerge、testUpBasicMerge、testUpTripleMerge和testUpTrickyMerge。
        * 考虑如下几点：
            * 在遍历行时候，从第3行开始遍历 safe，因为 第一行和第二行会变，move/merge 新的 tile
            * 创建一个或多个 helper method ,比如处理 board 中的 单个 row 用一个；返回所需的行值 用 一个
            * only call move on a given tile once. 示例代码：困难的部分是弄清楚每个贴图应该在哪一行结束
                * Tile t = board.tile(3, 0)
                    board.move(3, 3, t);
            * 只需要另外两行代码就可以处理其他三个方向! Board类有一个 *setViewingPerspective(sides)函数* ，它将改变tile的行为并移动类，使它们的行为就像给定的 Side 是 NORTH 一样。
            * 测试 tile 变化：https://forms.gle/pubhRx4fxYnPTGNX8
            * 测试 move 代码：https://docs.google.com/forms/d/e/1FAIpQLSfEmvFXmrUPo-Aj6__h51Xuo88HvjmEPiv_nX-SYS7HVp1Phg/viewscore?viewscore=AE0zAgDUMYd4uAwV0tJQknbrkIj6CjJwlAnWwXnc5IwmBiDNwrbEs65U9AcJFxKBMsQbNRM
            * quiz 最终的代码示例：
* Testing
    * TestEmptySpace：讲了怎么去看测试的报错
    * TestMaxTileExists：maxTileExists方法应该只寻找max tile 而不是其他任何东西(即不应该寻找空空间)
    * Testatleastemoveexists：atleastemoveexists方法依赖于emptySpaceExists方法，所以要通过TestEmptySpace中的所有测试
    * TestUpOnly: check the correctness of your tilt method 检查 tilt method, only in the up (Side.NORTH) 
        * 建议在纸和笔上写下你的代码所采取的步骤，这样你就可以首先理解为什么你的板看起来是这样的，然后想出一个修复方案。这些测试只调用tilt一次。
    * TestModel：集成测试，上面测试通过才用这个集成测试
### materials:
* 3个quiz
    * 理解游戏规则：https://forms.gle/xW74vQnK7dZAjS6eA
    * 理解 tile 变化：https://forms.gle/pubhRx4fxYnPTGNX8
    * 理解 move 操作代码：https://forms.gle/AGrhEFbwfMJ7qwaB6
* video：
    * high level skeleton code tour：https://www.youtube.com/watch?v=3YbIOga6ZdQ
    * get started writing emptySpaceExists(Board b) 方法：https://youtu.be/13rdFndFNXc
    * get started writing tilt(Side side) 方法：https://www.youtube.com/watch?v=abFbbK1QY2k&feature=youtu.be
