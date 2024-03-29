​    

**一、****分支与角色说明**

Git 分支类型

master 分支（主分支） 稳定版本

develop 分支（开发分支） 最新版本

release 分支（发布分支） 发布新版本

hotfix 分支（热修复分支） 修复线上Bug

feature 分支（特性分支） 实现新特性

Gitlab 角色与项目角色对应关系

Owner（拥有者） Git 管理员

Master（管理员） 开发主管

Developer（开发者） 开发人员

Reporter（报告者） 测试人员

Guest（观察者） 其他人员

 

**二、****分支简明使用流程**

1、每开发一个新功能，创建一个 feature 分支，多人在此分支上开发；

2、提测时，将 master 分支和需要提测的分支汇总到一个 release 分支，发布测试环境；

3、发现bug时，在feature分支上debug后，再次回到2；

4、发布生产环境后，将 release 分支合并到 master 分支，删除release分支；

 

**三、****创建新项目（master分支）**

开发主管提交代码初始版本到master 分支，并推送至Gitlab系统；

开发主管在Gitlab 系统中设置master 分支为Protectd 分支（保护分支）；

Protected 分支不允许Developer 角色推送代码，但Master 角色可以推送代码；

 

**四、****进行项目开发（develop分支）**

开发主管在master 分支上创建develop 分支（开发分支），并推送至Gitlab系统；

master 分支与develop 分支一样，有且仅有一个；

对于非并行项目可以使用develop分支开发方式，对于多人并行开发项目，使用feature分支开发方式，但develop和feature开发方式不应同时使用；

 

**五、****开发新特性（feature分支）**

每个新需求或新的研究创建一个feature 分支；

命名规范：

f-分支创建日期-新特性关键字，例如：f-20150508-满立减；

推荐使用feature 分支，但feature 分支的生命周期不能跨一次迭代；

 

**六、****发布测试环境（release分支）**

开发负责人需完成以下任务：

\1. 确认要发布的feature 分支上的功能是否开发完毕并提交；

\2. 创建release 分支（发布分支），将所有要发布的分支逐个合并到release分支，有如下情况：

①.feature分支（可能有多个）

②.master分支（期间可能有其他release版本更新到了master）

\3. 命名规则：r-分支创建日期-新特性和待发布版本号，例如：r-201505081712-买立减v1.0.0，版本可根据需要添加;

\4. 删除本次发布的所有feature分支;

\5. 发布到测试环境，通知测试;

 

**七、****修复待发布版本中的Bug（feature分支）**

如果发现bug，开发人员在feature 分支上修复测试人员提交给自己的bug，修复完成后，由负责人再次创建 release 分支，发布测试环境。

 

**八、****发布正式环境**

开发负责人需完成以下任务：

\1. 根据修复后的release分支再次将master合并，打包发布生产环境;

\2. 确认发布成功，并线上验收通过后，将release分支合并到master分支;

\3. 在master分支上创建标签，命名规则：tag-日期-新特性和版本号，例如：tag-201505081712-买立减v1.0.0，版本可根据需要添加，作为发版里程碑标记;

\4. 删除对应release 分支;

 

**九、****修复线上Bug（hotfix分支）**

线上的不同版本出现了bug怎么办？开发负责人需完成以下任务：

\1. 从master 分支某个tag 上创建一个hotfix 分支（热修复分支），一般是最新的tag应该和当前生产环境对应；

命名规则：h-分支创建日期-bug名称和待发布版本号，例如：h-201705081614-购物车点击没反应v1.0.1;

\2. 开发人员完成Bug 修复，提交hotfix分支到测试环境验收通过;

\3. 再次发布正式环境流程;

\4. 将hotfix 分支合并到master分支;

\5. 在master分支上创建标签，命名规则：tag-日期-新特性和版本号，例如：tag-201505081712-买立减v1.0.0，版本可根据需要添加，作为发版里程碑标记;

\6. 删除hotfix 分支;

 

**十、****Git** **的特别注意事项**

由于 git 分支是基于指针的概念，所以分支速度非常快，当多个分支时，实际指针指向的是同一个文件，当文件被修改时，使用的是暂存区保存修改，此时并未提交到相应分支。

所以切换分支的时候，暂存区只有一个，所以切换分支之前，一定要将所有修改commit到当前分支，否则会在其他分支看到修改的内容，引起误解。

 

以上规范仅供参考，具体实践请依据团队具体情况自行调整。。。