# Git并行工作流程规范设计记录

# 目录

- [1. 介绍](#1-介绍)
- [2. 颜色的选择](#2-颜色的选择)
- [3. 制作记录](#3-制作记录)
- [4. 相关文章](#4-相关文章)

# 内容

# 1. 介绍

为了解决 [Git Flow](https://links.jianshu.com/go?to=https%3A%2F%2Fnvie.com%2Fposts%2Fa-successful-git-branching-model%2F) 的不足，我精心设计并制作了 [Git并行工作流程规范](https://www.jianshu.com/p/d7a3a4935440)；规范有了，但在实施规范的过程中会遇到各种技术难题，为了完整性，我又研究并提供了实施规范过种中各种问题的解决方案，并把这些方案撰写成文 [Git技巧和问题解决方案](https://www.jianshu.com/p/d21838dc5947)；同时，为了 形像地表达 [Git并行工作流程规范](https://www.jianshu.com/p/d7a3a4935440) 中的 分支流转规范 和 合并多个提交问题的解决思路，我为其专门绘制了一张 分支流转规范图 和 合并中间提交段动画，如下：



![img](https:////upload-images.jianshu.io/upload_images/3987507-7e00083506650ad5.jpg?imageMogr2/auto-orient/strip|imageView2/2/w/1062/format/webp)

分支流转规范图


*分支流转规范图的相关内容详见[Git并行工作流程规范](https://www.jianshu.com/p/d7a3a4935440)*





![img](https:////upload-images.jianshu.io/upload_images/3987507-7a50311fabfd0498.gif?imageMogr2/auto-orient/strip|imageView2/2/w/480/format/webp)

合并中间提交段动画


*合并中间提交段动画的相关内容详见[Git中合并多个提交的各种方法](https://www.jianshu.com/p/1268c8704e1b)*



小小的一张照，却用了不少心思：

- 根据色彩心理学挑选分支代表色；
- 根据配色理论和配色工具调整代表色的协调性；
- 为了方便 制作 和 排版，还专门制作了几个 Omni 型版：[标注-Omni型版](https://www.jianshu.com/p/307e9c475460)、[Git-Omni型版](https://www.jianshu.com/p/e384534f5846)；

可能懂设计的朋友会提出纠正的地方：

> 界面中不建议使用三种心上的颜色；

我认同这种观点，但觉得这句话说的不够严谨 或者 不完全正确，我的理解如下：

> 一个设计单元中不应包含多于三种色系的颜色（并不是三种颜色）；

不过我个人喜欢让 逻辑凌驾于美感之上，所以，在 分支流转规范图 的设计中，我是以 颜色的心理意义 与 分支的意义 相匹配 为优先原则 来定分支的主色，然后才根据配色理论和工具来调整这些颜色的协调性；所以设计出的 分支流转规范图 可以不是最好看的，但应该是非常有深意；

# 2. 颜色的选择

- 发布分支： 绿色；代表健康、安全
  - 释义：发布分支上的变更都应该无害的、无问题的，能正常运算的，这与绿色传达的健康、安全之意相吻合；
- 测试分支：蓝色；代表严谨、规范、认真
  - 释义：测试分支通常是要被严格、谨慎、认真 的测试的，这与蓝色传达的严谨、规范、认真之意相吻合；
- 预发布分支：使用与发布分支相近的颜色，最好使用介于 测试分支的颜色 和 发布分支的颜色 之间的颜色；
  - 释义：因为 预发布分支 是在正式发布之前做试运行的，所以预发布分支的颜色应与 发布分支 相近；又因为 预发布分支 是从 测试分支 转过来，将来要流转到 发布分支 上的，所以 预发布分支 最好使用介于 测试分支的颜色 和 发布分支的颜色 之间的颜色；
- 合并分支：使用 被合并的几个分支的颜色的混合色，或者 与测试分支相近的颜色；
  - 释义：因为 合并分支 是 被合并的若干分支的综合，所以 合并分支 可以使用 被合并的几个分支的颜色的混合色；如果这个 混合色 不理想，也可以使用 与测试分支相近的颜色，因为 合并分支的目的多数是为了测试，即使不是为了测试，合并分支下一步多数也是要被流转到测试分支的；
- 修复分支：红色；代表严重、错误、问题；
  - 释义：修复分支是用来修复问题的，这与红色传达的严重、错误、问题之意相吻合；
- 功能分支：黄色；代表努力、上进、目标
  - 释义：功能分支是为实现一个 或 多个 功能而开设的分支，这通常是一个较大的任务，这与黄色传达的努力、上进、目标之意相吻合；
- 连接提交的箭头：蓝色；代表严谨、规范、认真；
  - 释义：虽然每个分支中的箭头可以与分支的颜色保持一致，但考虑到：
    - 分支之间存在着共同的提交，这些提交之间的箭头不适合用某一分支的颜色；
    - 跨分支的箭头，指定颜色有些麻烦；
       所以，所以建议箭头用同一颜色为最好；既然所以箭头都用相同的颜色，那么箭头的颜色应该用版本控制系统的代表色，而版本控制系统是记录变更的一个系统，它通常具有严谨、规范、稳定的特点，这与蓝色传达的严谨、规范、认真之意相吻合；

# 3. 制作记录

下面是设计与制作过程的记录图：



![img](https:////upload-images.jianshu.io/upload_images/3987507-ca6495bdc3e8d9c6.png?imageMogr2/auto-orient/strip|imageView2/2/w/1200/format/webp)

分支流转规范的制作记录



![img](https:////upload-images.jianshu.io/upload_images/3987507-03a8a9aa2bbe29b5.png?imageMogr2/auto-orient/strip|imageView2/2/w/1200/format/webp)

颜色设计记录



![img](https:////upload-images.jianshu.io/upload_images/3987507-cdbeca039abba526.png?imageMogr2/auto-orient/strip|imageView2/2/w/1200/format/webp)

配色设计色轮的使用记录



![img](https:////upload-images.jianshu.io/upload_images/3987507-61f6eb716904ef54.png?imageMogr2/auto-orient/strip|imageView2/2/w/1200/format/webp)

Adobe配色设计记录



![img](https:////upload-images.jianshu.io/upload_images/3987507-1922ca1d85e02898.png?imageMogr2/auto-orient/strip|imageView2/2/w/1200/format/webp)

合并中间提交段动画的制作记录

# 4. 相关文章

- [Git并行工作流程规范](https://www.jianshu.com/p/d7a3a4935440)
- [Git基础教程](https://www.jianshu.com/p/fd40460ffb37)
- [Git命令大全](https://www.jianshu.com/p/15a4dee9c5df)
- [Git技巧和问题解决方案](https://www.jianshu.com/p/d21838dc5947)
- [Git中合并多个提交的各种方法](https://www.jianshu.com/p/1268c8704e1b)
- [Git并行工作流程规范设计记录](https://www.jianshu.com/p/7f4b47d2507d)
- [弃用SVN选择Git的理由](https://www.jianshu.com/p/bdc9a46c3394)
- [Git和Subversion的命令的对比](https://www.jianshu.com/u/7ecaba2d594c)
- [分布式和集中式版本控制系统的区别](https://www.jianshu.com/p/7d55f32b7c9f)
- [Git的存储机制](https://www.jianshu.com/p/caa4695af535)



作者：科研者
链接：https://www.jianshu.com/p/7f4b47d2507d
来源：简书
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。