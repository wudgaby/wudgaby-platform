# Git Flow 的正确使用姿势

- ## Git Flow 的概念

   在使用Git的过程中如果没有清晰流程和规划，否则,每个人都提交一堆杂乱无章的commit,项目很快就会变得难以协调和维护。
   Git版本管理同样需要一个清晰的流程和规范。
   Vincent Driessen 为了解决这个问题提出了 [A Successful Git Branching Model](https://links.jianshu.com/go?to=http%3A%2F%2Fnvie.com%2Fposts%2Fa-successful-git-branching-model%2F)
   以下是基于Vincent Driessen提出的Git Flow 流程图

  ![img](https:////upload-images.jianshu.io/upload_images/1366859-eda8da6a7d2385ad.png?imageMogr2/auto-orient/strip|imageView2/2/w/1200/format/webp)

  gitflow.png

  

  ## Git Flow 的常用分支

  - ### Production 分支

  也就是我们经常使用的Master分支，这个分支最近发布到生产环境的代码，最近发布的Release， 这个分支只能从其他分支合并，不能在这个分支直接修改

  - ### Develop 分支

  这个分支是我们是我们的主开发分支，包含所有要发布到下一个Release的代码，这个主要合并与其他分支，比如Feature分支

  - ### Feature 分支

  这个分支主要是用来开发一个新的功能，一旦开发完成，我们合并回Develop分支进入下一个Release

  - ### Release分支

  当你需要一个发布一个新Release的时候，我们基于Develop分支创建一个Release分支，完成Release后，我们合并到Master和Develop分支

  - ### Hotfix分支

  当我们在Production发现新的Bug时候，我们需要创建一个Hotfix, 完成Hotfix后，我们合并回Master和Develop分支，所以Hotfix的改动会进入下一个Release

  ## Git Flow 如何使用

  - ### Master/Devlop 分支

  所有在Master分支上的Commit应该打上Tag，一般情况下Master不存在Commit，Devlop分支基于Master分支创建

  

  ![img](https:////upload-images.jianshu.io/upload_images/1366859-a88dba7312bd50e0.png?imageMogr2/auto-orient/strip|imageView2/2/w/614/format/webp)

  图侵删

  

  - ### Feature 分支

  Feature分支做完后，必须合并回Develop分支, 合并完分支后一般会删点这个Feature分支，毕竟保留下来意义也不大。

  

  ![img](https:////upload-images.jianshu.io/upload_images/1366859-d417601143f7bc82.png?imageMogr2/auto-orient/strip|imageView2/2/w/614/format/webp)

  图侵删

  - ### Release 分支

  Release分支基于Develop分支创建，打完Release分支之后，我们可以在这个Release分支上测试，修改Bug等。同时，其它开发人员可以基于Develop分支新建Feature (记住：一旦打了Release分支之后不要从Develop分支上合并新的改动到Release分支)发布Release分支时，合并Release到Master和Develop， 同时在Master分支上打个Tag记住Release版本号，然后可以删除Release分支了。

  

  ![img](https:////upload-images.jianshu.io/upload_images/1366859-539ffe4e5b73715e.png?imageMogr2/auto-orient/strip|imageView2/2/w/614/format/webp)

  图侵删

  - ### Hotfix 分支

  hotfix分支基于Master分支创建，开发完后需要合并回Master和Develop分支，同时在Master上打一个tag。

  

  ![img](https:////upload-images.jianshu.io/upload_images/1366859-091b03e7e4a6daa3.png?imageMogr2/auto-orient/strip|imageView2/2/w/614/format/webp)

  图侵删

  ## Git Flow 命令示例

  ### 创建 Devlop

  

  ```undefined
  git branch develop  
  git push -u origin develop
  ```

  ### 开始 Feature

  

  ```csharp
  # 通过develop新建feaeure分支
  git checkout -b feature develop
  # 或者, 推送至远程服务器:
  git push -u origin feature    
  
  # 修改md文件   
  git status
  git add .
  git commit    
  ```

  ### 完成 Feature

  

  ```bash
  git pull origin develop
  git checkout develop 
  
  #--no-ff：不使用fast-forward方式合并，保留分支的commit历史
  #--squash：使用squash方式合并，把多次分支commit历史压缩为一次
  
  git merge --no-ff feature
  git push origin develop
  
  git branch -d some-feature
  
  # 如果需要删除远程feature分支:
  git push origin --delete feature   
  ```

  ### 开始 Release

  

  ```css
  git checkout -b release-0.1.0 develop
  ```

  ### 完成 Release

  

  ```bash
  git checkout master
  git merge --no-ff release-0.1.0
  git push
  
  git checkout develop
  git merge --no-ff release-0.1.0
  git push
  
  
  git branch -d release-0.1.0
  git push origin --delete release-0.1.0   
  
  # 合并master/devlop分支之后，打上tag 
  git tag -a v0.1.0 master
  git push --tags
  ```

  ### 开始 Hotfix

  

  ```css
  git checkout -b hotfix-0.1.1 master  
  ```

  ### 完成 Hotfix

  

  ```css
  git checkout master
  git merge --no-ff hotfix-0.1.1
  git push
  
  
  git checkout develop
  git merge --no-ff hotfix-0.1.1
  git push
  
  git branch -d hotfix-0.1.1
  git push origin --delete  hotfix-0.1.1 
  
  
  git tag -a v0.1.1 master
  git push --tags
  ```

  ## 使用建议

  如果你的代码没有清晰流程和规划，那么强烈推荐使用Vincent Driessen 提出的GIt flow
   让你的代码管理骚起来。

  ## 结尾

  本站文章图片等等来源于网络,仅作为学习之用,版权归原作者所有.如果侵犯了您的权益,请来信告知,我会尽快删除.

  

  作者：qingwenLi
  链接：https://www.jianshu.com/p/41910dc6ef29
  来源：简书
  著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
