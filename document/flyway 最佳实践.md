1. SQL 的文件名
开发环境和生产环境的 migration SQL 不共用. 开发过程往往是多人协作开发, DB migration 也相对比较频繁, 所以 SQL 脚本会很多. 而生产环境 DB migration 往往由 DBA 完成, 每次升级通常需要提交一个 SQL 脚本.

(1). 开发环境 SQL 文件建议采用时间戳作为版本号. 
开发环境 SQL 文件建议采用时间戳作为版本号, 多人一起开发不会导致版本号争用, 同时再加上生产环境的版本号, 这样的话, 将来手工 merge 成生产环境 V1.2d migration 脚本也比较方便, SQL 文件示例:
V20180317.10.59__V1.2_Unique_User_Names.sql
V20180317.14.59__V1.2_Add_SomeTables.sql

(2). 生产环境 SQL 文件, 应该是手动 merge 开发环境的 SQL 脚本, 版本号按照正常的版本, 比如 V2.1.5_001__Unique_User_Names.sql

2. migration 后的SQL 脚本不应该再被修改.

3. spring.flyway.outOfOrder 取值 true /false
对于开发环境, 可能是多人协作开发, 很可能先 apply 了自己本地的最新 SQL 代码, 然后发现其他同事早先时候提交的 SQL 代码还没有 apply, 所以 开发环境应该设置 spring.flyway.outOfOrder=true, 这样 flyway 将能加载漏掉的老版本 SQL 文件; 而生产环境应该设置 spring.flyway.outOfOrder=false

4. 多个系统公用要 DB schema 
很多时候多个系统公用一个 DB schema , 这时候使用 spring.flyway.table 为不同的系统设置不同的 metadata 表, 缺省为 flyway_schema_history


1.多服务时,flyway放在单独一个服务统一管理.
2.初始1.0的sql. 拿现有环境完整的ddl,dml.
3.后期db版本管理,再使用以上实践方式.
 