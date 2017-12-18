# FBDP Project2 股票新闻标题舆情分析
@张竞艺 151278047 

[TOC]

## 实验目标
使用多种机器学习算法对文本进行情感判别，包括KNN、决策树、朴素贝叶斯、支持向量机等，学习如何进行模型训练，如何进行分类预测。要求使用至少两种分类方法。

## 数据集说明
样本集：指标有negative、 neutral、positive词性的数据集
测试集：指待分类股票标题数据集

实验设计说明，包括主要设计思路、算法设计、程序和各个类的设计说明
程序运行和实验结果说明和分析
性能、扩展性等方面存在的不足和可能的改进之处


## 主要设计思路
本次实验主要要分解成如下几个步骤：

1. 数据预处理
    1. 从原始数据集中提取新闻标题并分词
    2. 数据清洗（清洗分词后仍出现的非中文字符）
    3. 对样本集三个情感标签下的词组分别进行词频统计
2. 文本向量化
    1. 分别对样本集和测试集中的词组计算tf-idf值
    2. 为了方便后续处理，将tf-idf值*10000进行扩大
    3. 根据一个词和对应的tf-idf值将文本转化成向量数组
3. 特征选择
    - 根据样本集中tf-idf值，在三类情感中每类选出500词共1500词作为特征词
4. 模型训练
    - 用KNN算法输入训练模型
    - 用NaiveBayes算法训练模型 
5. 分类
    - 用KNN算法实验测实现分类
    - 用NaiveBayes算法实现分类


## 程序说明
###数据预处理
#### segment.java

- 对新闻标题和样本集的内容进行分词
- 数据清洗：用正则表达式除去非中文字符

```java
String titles = StringUtils.strip(tempseg.toString().replaceAll("[,.%/(A-Za-z0-9)]",""),"[]");
titles = titles.replace("\\s+"," ");
```

###文本向量化
####Tfidf.java

> TF-IDF是一种统计方法，用以评估一字词对于一个文件集或一个语料库中的其中一份文件的重要程度。字词的重要性随着它在文件中出现的次数成正比增加，但同时会随着它在语料库中出现的频率成反比下降。TF-IDF加权的各种形式常被搜索引擎应用，作为文件与用户查询之间相关程度的度量或评级。除了TF-IDF以外，因特网上的搜索引擎还会使用基于链接分析的评级方法，以确定文件在搜寻结果中出现的顺序。
> 
> 来源：百度百科

TF（term frequency）即一个词在该文本中的词频  
IDF（inverse document frequency）是指逆向文件频率  
一个词的Tf-Idf值标识着它对于该文本的重要性，即一个词在该文本中出现的次数越多而在整个语料库中出现的次数越少就越能说明这个词能在很大程度上代表这个文本。故Tf-Idf相对于单纯的词频统计来说能够使得在所有文本中都出现的词如“股票”“公司”“新闻”等的权重下降，从而突出能够代表文本的特征词  
因此可以用Tf-Idf值可以过滤常用词并保留重要词，总而可以进行特征选择

Tf-Idf的java实现为分别计算一个词的tf值和idf值，然后将两者相乘（并扩大10000倍）作为其Tf-Idf值，并和对应的词组映射在哈希表最后将结果按照tf-idf值降序输出

tf值计算：

```
int wordLen = cutwords.size();
HashMap<String, Integer> intTF = TfIdf.normalTF(cutwords);
Iterator iter = intTF.entrySet().iterator(); 
while(iter.hasNext()){
    Map.Entry entry = (Map.Entry)iter.next();
    resTF.put(entry.getKey().toString(), Float.parseFloat(entry.getValue().toString()) / wordLen);
}
```
idf值计算：

```
float value = (float)Math.log(docNum / Float.parseFloat(entry.getValue().toString()));
```

        
tf-idf计算：

```
while(iter.hasNext()){
    Map.Entry entry = (Map.Entry)iter.next();
    String word = entry.getKey().toString();
    Float value = (float)Float.parseFloat(entry.getValue().toString())*idfs.get(word)*10000;
    TfIdf.put(word, value);
}
```

####word2vec.java

文本向量化主要思路：

1. 准备特征词`features.txt`（包含1500个特征词）
2. 对样本集/测试集中每个文本建立一个1500维的数组（分别对应1500个特征词），将文本中的词组和特征词进行比对，若样本集/测试集中出现了某个特征词则把该词的tf-idf值传给数组，若没有则为0
3. 根据后序算法所需输入格式相应调整输出格式

关键部分代码：

```
while ((temp0 = br1.readLine()) != null) {
    temp1 = temp0.split(" ");
    k = dic.indexOf(temp1[0]);
    if (k != -1) {
        vec[k] = temp1[1];
    }
}
```

###KNN模型训练及分类

KNN就是根据某种距离度量检测未知数据与已知数据的距离，统计其中距离最近的k个已知数据的类别，以多数投票的形式确定未知数据的类别。

源程序共定义了三个class文件，分别是：

- KNNNode：KNN结点类，用来存储最近邻的k个元组相关的信息
- KNN：KNN算法主体类 
- TestKNN：KNN算法测试类 

####TestKNN.java

` public void read（）` ：读取文件中的数据，存储为数组的形式（以嵌套链表的形式实现）List<List<Double>> datas

`main` ：读入样本集和测试集的数据，然后输出测试集的分类

在此程序中由于需要分为negative、neutral、positive三类，故k=3

####KNN.java

此程序定义了一个大小为k的优先级队列来存储k个最近邻节点  
优先级队列初始默认是距离越远越优先  
根据算法中的实现，将与测试集最近的k个节点保存下来

####KNNNode.java

用来存储最近邻的k个元组相关的信息

####KNN输入文件
训练集：knntrain.txt
格式：【tfidf值1】【tfidf值2】···【tfidf值1500】【 分类标号】
示例：（以二维数据为例）

```
0.1887    0.3276    -1
0.8178    0.7703    1
0.6761    0.4849    -1
0.6022    0.6878    -1
0.1759    0.8217    -1
0.2607    0.3502    1
0.2875    0.6713    -1
0.9160    0.7363    -1
0.1615    0.2564    1
0.2653    0.9452    1
0.0911    0.4386    -1
```
测试集：knndata.txt
格式：【tfidf值1】【tfidf值2】···【tfidf值1500】
示例：（以二维数据为例）

```
0.9516    0.0326
0.9203    0.5612
0.0527    0.8819
0.7379    0.6692
0.2691    0.1904
0.4228    0.3689
0.5479    0.4607
0.9427    0.9816
```

####KNN分类结果
股票数据集KNN算法输出结果部分截图：

![](media/15134914723501/15136040760630.jpg)

### NaiveBayes模型训练及分类

####代码结构
NaiveBayesMain.java 主程序入口   
NaiveBayesConf.java 用于处理配置文件  
NaiveBayesTrain.java 用于训练过程的MapReduce 描述   
NaiveBayesTrainData.java 在测试过程之前，读取训练后数据   
NaiveBayesTest.java 用于测试（分类）过程的MapReduce 描述


####配置文件
配置文件NBayes.conf用于描述分类内容
格式：

-  第一行，第一个是分类的个数N，后面跟着N个字符串（空格分隔）每个代表类名
-  第二行，第一个是类中属性的个数M，后面跟着M个<字符串，整数>的分组
-  第二行的每个分组中的字符串是属性名，整数是该属性最大值

举例说明：3个分类，类名为cl1,cl2,cl3；分类有3个属性（即词组），为p1,p2,p3

```
3 cl1 cl2 cl3 
p1 10000 p2 10000 p3 10000
```

NBayes.train
用来存放训练集
每一行描述一个训练向量，每行第一个为类名，后面接M个值，空格分隔，代表此向量各属性值  
举例：

```
cl1 3 4 6
cl2 1 8 7
```

 NBayes.test
用来存放测试集
每一行描述一个训练向量，每行第一个该变量ID，后面接M个值，空格分隔，代表此向量各属性值
举例：

```
1 6 9 3
2 4 8 1
```
####分类结果

