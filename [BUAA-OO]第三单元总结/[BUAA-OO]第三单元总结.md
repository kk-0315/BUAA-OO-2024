# [BUAA-OO]第三单元总结

## 前言

本单元是主题是“**规格化设计**”，主要是要求我们掌握**JML**规格语言，包括基于规格语言进行代码实现，或是自己编写符合要求的JML规格语言。其实相比于“规格设计单元”，我更愿意称之为算法单元，因为规格仅仅是完成任务最基本的要求，是一种契约，你的实现不一定要原原本本按着规格来写，相反的，这样的实现是Junit的要求。而且往往这样的实现时间复杂度极高，可能有着**TLE**的风险。所以我们需要采用一些巧妙的算法提高我们实现的效率，例如并查集、dfs深度搜索等。

看往年学长的博客，很多人在质疑学习JML这样冷门的语言的意义。我相信课程组要我们学习的肯定不单单是这个语言本身，更是一种契约式编程的思想，以及做测评机的思想（毕竟这个Junit就很有写测评机那味。虽然可能我们平常的编程并不一定需要用到JML，但是在一些大规模的、绝对不能出错的大型系统的代码编写过程中，JML就能发挥他的作用，避免我们出错影响整个系统的功能。

在阅读题目之前，我们首先需要了解基本的JML规格语言，这一点可以参照课程组给出的`JML Level 0`手册和`第三单元推送`（被称作保姆级教程），不求完全掌握书写能力，但是至少需要“**看得懂**”。



## 本单元的测试过程

### 对一些测试方法的理解

> **黑盒测试(Black-box testing)**，[软件测试](https://zh.wikipedia.org/wiki/软件测试)的主要方法之一，也可以称为功能测试、数据驱动测试或基于规格说明的测试。测试者不了解[程序](https://zh.wikipedia.org/wiki/程序)的内部情况，不需具备应用程序的代码、内部结构和编程语言的专门知识。只知道程序的输入、输出和[系统](https://zh.wikipedia.org/wiki/系统)的功能，这是从用户的角度针对软件界面、功能及外部结构进行测试，而不考虑程序内部逻辑结构。测试案例是依应用系统应该做的功能，照规范、规格或要求等设计。测试者选择有效输入和无效输入来验证是否正确的输出。
>
> [黑盒测试 - 维基百科，自由的百科全书 (wikipedia.org)](https://zh.wikipedia.org/zh-my/黑盒测试)

理解：

- 黑箱测试是一种测试方法，测试人员不需要了解内部的代码结构或实现细节。
- 测试人员基于软件的需求规格说明书和功能需求，设计测试用例，以验证软件是否符合预期的功能和行为。
- 黑箱测试主要关注软件的功能性和用户体验，而不关注内部实现。
- 我们平时的测评、自己的对拍器都是黑箱测试的一种。

> **白盒测试**（white-box testing）又称**透明盒测试**（glass box testing）、**结构测试**（structural testing）等，[软件测试](https://zh.wikipedia.org/wiki/软件测试)的主要方法之一，也称结构测试、逻辑驱动测试或基于程序本身的测试。测试应用程序的内部结构或运作，而不是测试应用程序的功能（即[黑盒测试](https://zh.wikipedia.org/wiki/黑盒测试)）。在白盒测试时，以编程语言的角度来设计测试案例。测试者输入资料验证资料流在程序中的流动路径，并确定适当的输出，类似测试电路中的节点。测试者了解待测试[程序](https://zh.wikipedia.org/wiki/程序)的内部结构、[算法](https://zh.wikipedia.org/wiki/算法)等[信息](https://zh.wikipedia.org/wiki/信息)，这是从程序设计者的角度对程序进行的测试。
>
> [白盒测试 - 维基百科，自由的百科全书 (wikipedia.org)](https://zh.wikipedia.org/zh-my/白盒测试)

理解：

- 白箱测试是一种测试方法，测试人员需要了解软件的内部结构、代码逻辑和实现细节。
- 测试人员基于代码的逻辑路径，设计测试用例，以验证代码的正确性、覆盖率和性能等方面。
- 白箱测试通常用于检查代码是否符合设计规范、是否存在潜在的错误和漏洞等。
- 我们写的Junit就是白盒测试的一种，根据JML规格测试每一部分的实现。

> 在[电脑编程](https://zh.wikipedia.org/wiki/计算机编程)中，**单元测试**（英语：Unit Testing）又称为**模块测试** [[来源请求\]](https://zh.wikipedia.org/wiki/Wikipedia:列明来源) ，是针对[程序模块](https://zh.wikipedia.org/wiki/模組_(程式設計))（[软件设计](https://zh.wikipedia.org/wiki/软件设计)的最小单位）来进行正确性检验的测试工作。程序单元是应用的最小可测试部件。在[过程化编程](https://zh.wikipedia.org/wiki/過程化編程)中，一个单元就是单个程序、函数、过程等；对于面向对象编程，最小单元就是方法，包括基类（超类）、抽象类、或者派生类（子类）中的方法。
>
> [单元测试 - 维基百科，自由的百科全书 (wikipedia.org)](https://zh.wikipedia.org/zh-my/单元测试)

理解：

- 单元测试是针对软件中的最小单元（通常是函数、方法或类）进行的测试。
- 单元测试旨在验证单个单元的功能是否正确，通常是在开发过程中由开发人员编写和执行的。
- 单元测试可以帮助发现和解决单元级别的问题，提高代码的质量和稳定性。
- 我们的Junit也利用到了单元测试的思想，通过测试各个函数的实现，定位bug的位置。

> 功能测试：按照测试软件的各个功能划分进行有条理的测试，在功能测试部分要保证测试项覆盖所有功能和各种功能条件组合。
>
> [软件测试 - 维基百科，自由的百科全书 (wikipedia.org)](https://zh.wikipedia.org/zh-my/软件测试)

理解：

- 功能测试是验证软件的各项功能是否符合需求和规格说明的测试。
- 功能测试涵盖了软件的各个功能模块，测试人员根据功能需求设计测试用例，以确保软件的功能性能符合预期。

> **整合测试**又称**组装测试**，即对[程序模块](https://zh.wikipedia.org/w/index.php?title=程序模块&action=edit&redlink=1)采用一次性或增值方式组装起来，对系统的[接口](https://zh.wikipedia.org/wiki/接口)进行正确性检验的测试工作。整合测试一般在[单元测试](https://zh.wikipedia.org/wiki/单元测试)之后、[系统测试](https://zh.wikipedia.org/wiki/系统测试)之前进行。实践表明，有时模块虽然可以单独工作，但是并不能保证组装起来也可以同时工作。
>
> [集成测试 - 维基百科，自由的百科全书 (wikipedia.org)](https://zh.wikipedia.org/zh-my/集成测试)

- 集成测试是将各个单元或模块组合在一起进行测试，验证它们之间的交互和集成是否正确。
- 集成测试旨在检查各个模块之间的接口和通信是否正常，以及集成后的系统是否符合整体设计要求。
- 在本单元的作业中也可以见到，比如各个方法实现均正确，但是实现的先后顺序也可能影响正确性。

> [软件测试](https://zh.wikipedia.org/wiki/软件测试)中的**[压力测试](https://zh.wikipedia.org/wiki/压力测试)**是在超过正常运作条件以外的条件下运作系统，以确认[健壮性](https://zh.wikipedia.org/wiki/健壮性_(计算机科学))的方式。压力测试对于[关键任务](https://zh.wikipedia.org/wiki/关键任务)软件格外的重要，但可以适用于各种的软件。压力测试一般较强调软件在高负载下的健壮性、[可用性](https://zh.wikipedia.org/wiki/可用性)及[异常处理](https://zh.wikipedia.org/wiki/异常处理)，以及哪些在一般使用环境下算是正常行为。
>
> 例如一个网站设计容量是100个人同时点击，压力测试就要是采用120个同时点击的条件测试。
>
> [压力测试 (软件) - 维基百科，自由的百科全书 (wikipedia.org)](https://zh.wikipedia.org/zh-my/壓力測試_(軟體))

理解：

- 压力测试是测试软件在极限条件下的性能和稳定性。
- 压力测试通常通过模拟大量并发用户或高负载情况，来评估系统在压力下的表现，以确定系统的性能瓶颈和改进空间。
- 本单元的数据可以达到10000条之多，考验程序在高负载的情况下是否会出现tle等相关的bug。

> 回归测试**(regression test)**指在软件维护阶段，为了检测代码修改而引入的错误所进行的测试活动。回归测试是软件维护阶段的重要工作，有研究表明，回归测试带来的耗费占软件生命周期的1/3总费用以上。
>
> 与普通的测试不同，在回归测试过程开始的时候，测试者有一个完整的测试用例集可供使用，因此，如何根据代码的修改情况对已有测试用例集进行有效的复用是回归测试研究的重要方向，此外，回归测试的研究方向还涉及自动化工具，面向对象回归测试，测试用例优先级，回归测试用例补充生成等。
>
> - 测试原有功能
> - 测试新加入的功能是否有side effect
>
> [软件测试 - 维基百科，自由的百科全书 (wikipedia.org)](https://zh.wikipedia.org/zh-my/软件测试#回归测试)

理解：

- 回归测试是在对软件进行修改或更新后，重新执行之前的测试用例，以确保修改不会影响软件原有功能的测试。
- 回归测试旨在捕获因修改而引入的新错误或导致的功能退化，以确保软件的稳定性和可靠性。

### 测试工具

本单元主要使用了课程组要求的Junit进行测试，利用前面提到的白盒测试和单元测试等思路，对部分较为复杂的方法进行测试。

### 数据构造

数据构造核心目标是**增加覆盖率，减少无效数据**，例如在hw9中，我构造了：

- 没人
- 有人没关系
- 有人有关系无三角关系
- 全关系
- 随机关系

等等类型，尽可能做到所有情况全覆盖。

在hw10和hw11的数据构造中均是利用这种思想。

## 本单元架构设计：图模型构建和维护策略

### hw9

在本次作业中要求我们根据JML完成一个检查两点之间连通性的函数，如下所示：

```java
/*@ public normal_behavior
      @ requires containsPerson(id1) &&
      @          containsPerson(id2);
      @ assignable \nothing;
      @ ensures \result == (\exists Person[] array; array.length >= 2;
      @                     array[0].equals(getPerson(id1)) &&
      @                     array[array.length - 1].equals(getPerson(id2)) &&
      @                      (\forall int i; 0 <= i && i < array.length - 1;
      @                      array[i].isLinked(array[i + 1])));
      @ also
      @ public exceptional_behavior
      @ signals (PersonIdNotFoundException e) !containsPerson(id1);
      @ signals (PersonIdNotFoundException e) containsPerson(id1) &&
      @                                       !containsPerson(id2);
      @*/
    public /*@ pure @*/ boolean isCircle(int id1, int id2) throws PersonIdNotFoundException;
```

观察JML规格可以发现，若要检查两个人的连通性需要找到一条路径从A->B，这瞬间让我想到在数据结构课上学的**dfs深度优先**搜索算法，事实上我们当然可以这样做，复杂度也并不高，但也不算最优。在查阅相关资料和学长学姐的博客之后，我发现了一个陌生而又巧妙的方法：**并查集**。

#### 并查集

**定义：**第一眼看到这个词感到很陌生，什么是并查集呢？简单理解就是用集合中的一个元素代表所有的元素，在本次作业中，这个集合中的元素应该是相互连通的。为了更好的实现并查集以及相关操作，我新建了一个类`DisjointSet`用来存储节点的关系和添加、查找、删除等维护并查集的方法。`DisjointSet`类中有两个成员变量：

```java
private HashMap<Integer, Integer> rep;//存储节点之间的连接关系
private HashMap<Integer, Integer> rank;//存储每个节点所在树的深度（高度）
```

**并查集的优化：**

- **路径压缩：**当我们在查找一个元素所在集合的**代表元**时，可以将寻找过程中遇到的所有元素的**直接上级**设置为代表元，这样就不用每次查找代表元都遍历一次，减小了复杂度。![路径压缩](C:\Users\17451\Desktop\OO\[BUAA-OO]第三单元总结\路径压缩.png)

具体实现如下：

```java
public int find(int id) {
    int pre = id;
    while (pre != rep.get(pre)) { //不是根节点，继续往上爬
        pre = rep.get(pre);
    }

    //这时pre是代表元，是根节点
    int now = id;
    while (now != pre) {
        int father = rep.get(now);//保存下一个节点
        rep.replace(now, pre);//把所有节点的父节点设置为代表元
        now = father;//继续移动
    }
    return pre;
}
```

这样我们在查找的过程中也能够顺便完成路径的压缩，一举两得！

- **按秩合并：**为了代表元到距离较长的节点个数尽量少，我们可以把简单的树往复杂的树上合并。![按秩合并](C:\Users\17451\Desktop\OO\[BUAA-OO]第三单元总结\按秩合并.png)

*（图取自Hyggge学长的博客）*

具体实现如下：

```java
public int merge(int id1, int id2) {
    int pre1 = find(id1);
    int pre2 = find(id2);
    if (pre1 == pre2) {
        return -1;
    }

    int rank1 = rank.get(id1);
    int rank2 = rank.get(id2);
    if (rank1 < rank2) {
        rep.replace(pre1, pre2);
    } else if (rank1 == rank2) {
        rank.replace(pre1, rank1 + 1);
        rep.replace(pre2, pre1);
    } else {
        rep.replace(pre2, pre1);
    }
    return 0;
}
```

当我们在`addPerson`和`addRelation`的时候应该分别调用`add`和`merge`方法：

```java
public void addPerson(Person person) throws EqualPersonIdException {
    //...
    disjointset.add(person.getId());
    //...
}
public void addRelation(int id1, int id2, int value)
            throws PersonIdNotFoundException, EqualRelationException {
    //...
    disjointset.merge(id1,id2);
    //...
}
```

**并查集的维护：**

此外我们还注意到，在`modifyRelation`方法里可能涉及到关系的**删除**，此时我们应该怎样**维护并查集**呢？

我采用这样的方法：对于两个personA和B，首先重置A、B的直接上级为自身；接着用dfs深度优先算法搜索与A连通的所有点，将其直接上级都设置为A；如果此时B的直接上级也为A，说明B原先与A连通，**不用做任何事**；如果B的直接上级为B，那么同样**dfs搜索与B连通的所有点，其直接上级都设置为B**。

具体代码如下：

```java
public void sub(MyPerson person1, MyPerson person2) {
    int id1 = person1.getId();
    int id2 = person2.getId();
    rep.replace(id1, id1);
    rep.replace(id2, id2);
    HashMap<Integer, Boolean> visited1 = new HashMap<>();
    dfs(person1, visited1, rep, id1);
    if (rep.get(id2) == id2) {
        HashMap<Integer, Boolean> visited2 = new HashMap<>();
        dfs(person2, visited2, rep, id2);
    }
}
```

当我们在`modifyRelation`中遇到需要删边时调用这个方法即可。

#### 动态维护策略

本次作业的两个方法`queryBlockSum`和`queryTripleSum`还涉及到对**连通块和三元环的计数**，但是每次查询的时候都遍历一遍未免也太慢了，于是我打算分别维护一个**静态变量**`blockSum`和`tripleSum`，**在每次加边或减边时更新**，即采用**动态维护**的策略。

具体来说：

**维护blockSum：**

加人(ap)则`blockSum++`，

如果加边前，两人不在同一个集合（代表元不同），那么`blockSum--`，

如果减边后，两人不在同一个集合，且减边前两人在同一集合（显而易见），那么`blockSum++`。

**维护tripleSum：**

如果加边前，遍历所有与A**相连的点**，每有一个点与B相连，`tripleSum++`。

```java
public void updateTripleSum(int id1, int id2) {
    MyPerson myPerson1 = (MyPerson) getPerson(id1);
    MyPerson myPerson2 = (MyPerson) getPerson(id2);
    for (Integer key : myPerson1.getAcquaintance().keySet()) {
        if (myPerson1.getAcquaintance().get(key).isLinked(myPerson2)) {
            tripleSum++;
        }
    }


}
```

如果减边后，遍历所有与A**相连的点**，每有一个点与B相连，`tripleSum--`。

```java
public void subTripleSum(int id1, int id2) {
    MyPerson myPerson1 = (MyPerson) getPerson(id1);
    MyPerson myPerson2 = (MyPerson) getPerson(id2);
    for (Integer key : myPerson1.getAcquaintance().keySet()) {
        if (myPerson1.getAcquaintance().get(key).isLinked(myPerson2)) {
            tripleSum--;
        }
    }
}
```

### hw10

本次作业主要有以下几个方法需要进行**性能上的优化**，主要是通过**维护变量**来完成：

`queryTagValueSum、queryTagAgeVar、queryBestAcquaintance、queryShortestPath`

#### queryTagValueSum

通过在`MyTag`类中维护一个**私有变量**（注意不是静态变量）`valueSum`，

- 当每次往`Tag`中**加人时**更新其值，方法是对`Tag`中的人进行遍历
- 当每次**加边/改边后**更新其值，方法是遍历person1和person2共同的邻居的`tag`，如果同时含有person1和person2则该`tag`的`valueSum`增加`2*value`的值。
- 当每次**删边前**更新其值，方法是遍历person1和person2共同的邻居的`tag`，如果同时含有person1和person2则该`tag`的`valueSum`减少`2 * getPerson(id1).queryValue(getPerson(id2))`的值。
- 当每次从`tag`中**删人后**更新其值，方法是遍历`tag`中的人，若有人与之邻接则`valueSum -= persons.get(key).queryValue(person) * 2;`

#### queryTagAgeVar

通过在`MyTag`中维护两个私有变量`ageSum`和`agePowSum`，在每次**加人减人时**更新其值。

调用`queryTagAgeVar`方法时：

```java
return persons.isEmpty() ? 0 : (agePowSum - 2 * getAgeMean() * ageSum + persons.size() *
                getAgeMean() * getAgeMean()) / persons.size();
```

```java
public int getAgeMean() {
        return persons.isEmpty() ? 0 : ageSum / persons.size();
    }
```

#### queryBestAcquaintance

在`MyPerson`中维护一个**私有变量**`bestAcquaintanceId`，**初始值为自身id**。

- 当每次**加边时**，分别对person1和person2更新该值，具体逻辑是：

  如果person2无BestAcquaintance，即`id2.BestAcquaintanceId==id2`，则**设为id1**，

  如果新增加的这条关系的value等于目前id2和BestAcquaintanceId的value，则取id1和BestAcquaintanceId中**小的那个**，

  如果新增加的这条关系的value大于目前id2和BestAcquaintanceId的value，则**设为id1**。

- 每次**改边时**，思路大致相同：

```java
if (value > 0) {
    int nowValue = getPerson(id1).queryValue(getPerson(id2)) + value;
    changeBestAcquaintanceId(id1, id2, nowValue);
    changeBestAcquaintanceId(id2, id1, nowValue);
}
//...
if (value < 0) {
    if (((MyPerson) getPerson(id1)).getBestAcquaintanceId() == id2) {
        ((MyPerson) getPerson(id1)).modifyBestAcquaintanceId();
    }
    if (((MyPerson) getPerson(id2)).getBestAcquaintanceId() == id1) {
        ((MyPerson) getPerson(id2)).modifyBestAcquaintanceId();
    }
}
```

- 每次**删边后**：

```java
if (((MyPerson) getPerson(id1)).getBestAcquaintanceId() == id2) {
    ((MyPerson) getPerson(id1)).modifyBestAcquaintanceId();
}
if (((MyPerson) getPerson(id2)).getBestAcquaintanceId() == id1) {
    ((MyPerson) getPerson(id2)).modifyBestAcquaintanceId();
}
```

#### queryShortestPath

主要采用**堆优化的优先队列算法**

首先我们可以新建一个`Node`类，方便我们取出数据，并且我们需要重写`compareTo`(**注意直接相减可能导致int的溢出**），以便我们进行比较排序

```java
private int acqId;
private int value;

public Node(int acqId, int value) {
    this.acqId = acqId;
    this.value = value;
}

@Override
public int compareTo(Node o) {
    return Integer.compare(this.value,o.value);
}
```

直接上代码：

```java
if (id1 == id2) {
    return 0;
} else {
    HashMap<Integer, Boolean> visited = new HashMap<>();
    PriorityQueue<Node> heap = new PriorityQueue<>();
    HashMap<Integer, Integer> distance = new HashMap<>();
    for (Integer key : persons.keySet()) {
        visited.put(key, false);
        distance.put(key, Integer.MAX_VALUE);
    }
    distance.replace(id1, -1);
    heap.add(new Node(id1, -1));
    while (!heap.isEmpty()) {
        Node cur = heap.poll();
        int curId = cur.getAcqId();
        if (curId == id2) { // 如果当前节点是id2，直接返回结果
            return distance.get(id2);
        }
        if (visited.get(curId)) {
            continue;
        }
        visited.put(curId, true);
        MyPerson person = (MyPerson) persons.get(curId);
        for (Integer key : person.getAcquaintance().keySet()) {
            if (!visited.get(key) && 1 + distance.get(curId) < distance.get(key)) {
                distance.put(key, distance.get(curId) + 1);
                heap.add(new Node(key, distance.get(key)));
            }
        }
    }
    return distance.get(id2);
}
```

注意到这里我们需要理解，一个人到另一个人的**最短路径指的是经过的最少的人的个数（点）而非路径长度（边）**。因此一个人到自身的最短路径的距离是0，到相邻的人的最短距离也是0。

### hw11

本次作业没有需要进行性能优化的，所有方法的复杂度均在可以接受的范围内。只需要读懂规格正确实现就可以了，相对简单。



## 性能问题及规格和实现关系的理解

本单元作业中由于优化恰当，没有出现因性能问题而扣分。

在我看来规格仅仅是一种**契约**，他指明了方法需要实现的功能是什么，相应的变与不变的约束，而针对一种特定的规格可能会有很多实现方法，因此我们在编程时还需要特别注意代码运行的效率，否则很容易TLE。

所以我们要掌握**规格与实现分离**的思想，摒弃盲目翻译JML的做法，同时注意JML可能含有的潜在条件。

## Junit测试方法

首先JML中的`ensure`很好地为我们提供了编写Junit的思路，因为Junit本质就是检查各种各样的条件是否正确，因此我们需要将JML的所有**ensure进行断言**（如果是pure方法则需要对前后所有状态进行比较），此外一个有趣的例子是：

```JML
@ ensures (\forall int i; 0 <= i && i < \old(messages.length);
@          (!(\old(messages[i]) instanceof EmojiMessage) ==> \not_assigned(\old(messages[i])) &&
@           (\exists int j; 0 <= j && j < messages.length; messages[j].equals(\old(messages[i])))));
```

比如对于这个例子，我身边的很多同学的逻辑是：

```java
for(int i=0;i<oldmessages.length;i++){
    if(!oldmessages[i] instanceof EmojiMessage){
        assertTrue(hasMessage(oldMessages[j],nowMessages));
    }
}
```

但是我其实更倾向于：

```java
for(int i=0;i<oldmessages.length;i++){
    assertTure(!(oldMessages[j] instanceof MyEmojiMessage  || hasMessage(oldMessages[j], nowMessages));
}
```

也就是利用我们大一下学到的数理逻辑的知识直接对蕴含式进行翻译，我想这样**更加符合断言以及JML的思想**。

关于**Junit测试检验代码实现与规格的一致性的效果**我认为是很好的，因为Junit类似一种白箱测试，既检查了结果的正确性，又检查了前后状态的一致性是否符合规格描述，而且不是简单的输入输出检查，而是基于**逻辑和覆盖率**的检查，因此可以很好地检查出程序中潜在的错误，是帮助我们正确实现规格的好帮手。

## 心得体会

其实不管很多往届的学长学姐们对本单元多有诟病，认为本单元学的JML在实际开发出并无用处，但是我认为存在即合理，课程组花一整个单元的任务让我们学习JML的使用一定有其良苦用心（能体会到助教ggjj们编写JML和维护评测机确实很辛苦），我们一定要认识到JML带给我们的诸多优势——**高可靠性、高可复用性、便于测试**，体会契约式编程给我们带来的帮助，它不仅是一种帮助验证程序正确性的辅助工具，更要明白它对于一些复杂且重要系统（例如：航空航天控制系统）的重要性，因为我们在一些场景下，任何细微的疏忽和错误都可能带来十分严重的后果，并且这样的场景下是无法通过编造测试数据来实现测试全覆盖的，此时规格化设计便是最好的检测方法。

其实我更认为这单元也像一次**算法实践**，我们需要考虑方法实现的时间复杂度，对任何超过O(n^2)的方法进行性能上的优化。在本单元里我也接触了很多的算法实践——**并查集、堆优化的优先队列、变量维护**等等，我学到了很多也思考了很多，收获了很多。

这单元较前两个单元难度较小，因为思路已经在JML中体现了，最不济也就是直接翻译也可以完成基本功能的实现。但是如果要在强测中得到高分，切忌轻视本单元的任何一个方法，因为很有可能因为对JML理解的不全面而遗漏掉部分隐蔽的条件，甚至完全违背JML的要求；也有可能因为没有进行性能上的优化导致一堆ctle。总之，本单元考察的还是我们是否**细心**，包括是否**细心阅读规格**，是否**细心构造数据进行测试**。只有二者兼顾才能较好地完成本单元的作业。不过，三次作业能够囊括的知识毕竟很少，我对"契约式编程"也只是浅尝辄止的程度，以后还要多多探索和使用。

