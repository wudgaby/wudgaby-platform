分支种类
主分支（master）

开发分支（develop)

功能分支（feature)

修复分支（hotfix）

预发布分支（release）

 

分支描述
Master：主分支，创建 Repository 时默认会生成一个 master 分支。通常情况下 master 分支是受保护的（Protected）。master 分支保存的是稳定的已发布到线上的代码，会使用 tag 来记录发布的版本（tag命名为：tag  + “-” + “版本号”）。master 分支是不允许提交代码的，只能将代码合并（merge）到 master。在蓝绿部署的情况下，绿色部署环境需要部署此分支代码。

Develop：开发分支，从 master 创建。需要注意的是，develop分支的代码是通过feature分支合并而来的。通常情况下我们是不会在 develop 上开发的，因为你不能确定这些是否需要上线（或者说无法确定在哪次迭代上线）。

Feature：功能分支，从 develop 创建。feature 分支是用来开发新功能的，通常情况下新功能开发完毕后会合并的 develop。

Release：预发布分支 从 develop 创建。当一次迭代的功能开发并自测完成后，就可以创建发布分支。该分支通常用于测试，我们不能在该分支上完成除Bug 修复外的其他工作。测试完成后，我们需要将 release 分支合并到 master 进行发布。发布完成后在 master 打上 tag 记录此次发布的版本。在蓝绿部署的情况下，蓝色部署环境需要部署此分支代码。

Hotfix：修复分支，从 master 创建。当我们发现线上 Bug 时，会从 master 分支上对应的 tag 处创建新的 hotfix 分支，用来修复 bug。通常情况下，紧急修复的发布相对简单，在 Bug 修复并测试完成后，可直接合并到 master 进行发布（注意：如果在蓝绿部署的情况下，需要将bug修复之后的代码重新打包，并部署到蓝色环境下等待测试通过后，再将代码合并到master上）。发布完成后在 master打上 tag 记录此次发布的版本，并将 hotfix 合并到 develop。

 

分支命名规范
主分支（master）

开发分支（develop)

功能分支（feature)： feature-版本号

修复分支（hotfix）： hotfix-禅道bug号(当前解决了的bug号)-日期(YYYYMMDD)

预发布分支（release）：release-版本号

 

多人协作开发分支使用流程

![](https://img-blog.csdnimg.cn/20200521174159289.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTMyMTQxNTE=,size_16,color_FFFFFF,t_70)


在多人协作开发的情况下，所有分支需要全部上传到云仓库。
Master分支用来部署生产环境，release分支用来部署预发布环境。
Master、develop、release分支上严禁提交代码,只支持代码合并。
当生产环境发生紧急bug时，需要通过hotfix分支进行bug修复。 bug修复后将hotfix分支打包发布到预发布环境，待测试通过后再将代码合并到master与develop分支上。
当预发布环境产生bug时，代表当前开发的功能版本存在缺陷。 bug修复在原feature分支上修复即可。Bug修复后将代码依次合并到develop和release分支上。
Release、feature分支至少要多保存一个版本。例如：当前feature分支在开发1.2功能需求，既当前feature分支名称为feature-1.2，那么git仓库中release分支和feature分支至少要留存feature-1.1和release-1.1版本的分支。


单人开发分支使用流程

![](https://img-blog.csdnimg.cn/20200521174214846.png)

在单人开发的情况下，master、develop分支需要上传到云仓库，feature分支只在本地保存即可。
Master分支用来部署生产环境，develop分支用来部署预发布环境。当生产环境
Master分支上严禁提交代码,只支持代码合并。
当生产环境发生紧急bug时，需要通过feature分支进行bug修复，既创建分支：feature-bug-日期。 bug修复后将feature-bug分支打包发布到预发布环境，待测试通过后再将代码合并到master与develop分支上，然后并删除此bug分支。
当预发布环境产生bug时，代表当前开发的功能版本存在缺陷。 bug修复在原feature分支上修复即可。Bug修复后将代码依次合并到develop分支上。
Develop分支允许小规模代码提交，例如配置文件修改，参数类型修改。如有代码逻辑修改需要创建新分支。
feature分支至少要多保存一个版本。例如：当前feature分支在开发1.2功能需求，既当前feature分支名称为feature-1.2，那么git仓库中feature分支至少要留存feature-1.1版本的分支