# 后端项目

## 项目结构

- data: 包含了领域模型和访问数据库的模块
- api: 定义了服务的接口模块，被下面两个模块依赖
- backend-main: 即之前一直开发的后端模块
- batch: 用来进行批处理的模块

## 开发规范

1. **一定不要**将代码直接push到master分支上！基于dev分支来创建个人的开发分支，命名为dev-your_name之类的
2. dev和master分支会被自动构建和部署，个人的开发分支不会被自动构建
3. **具体实现代码**merge到dev分支前要确保进行过单元测试
4. 无论什么代码，在push到仓库前一定要确保**能通过编译，并且应用能够正常启动**！
5. 从仓库更新代码到本地dev分支、并切回个人分支后，要记得将dev分支上的改动merge到个人分支上
6. 将src/test/resources文件夹标记为"Test Resources Root"，如果idea没帮你做的话；并将测试用的资源放在这个文件夹内
7. 添加maven依赖时请关注是否产生冲突，并用exclude或修改版本等手段消除冲突
8. 每个模块有各自的ignore配置，请分模块配置，不要全部写在根目录的ignore配置下

## 开发指南

1. 在开发阶段，建议使用h2数据库进行调试（相关依赖已经添加）；因为h2是内存数据库，数据不会被持久化到硬盘上，所以需要时要导入初始数据，方法是在sql文件夹（已存在）下添加import.sql脚本，并将sql文件夹标记为资源文件夹（右键sql文件夹-Mark Directory as Resources Root）。sql文件夹下的文件不会被上传，可以放心编写。
2. 与测试有关的配置写在application-test.yml里面，并且在测试类上加上@ActiveProfiles("test")注解
3. 使用idea的Git工具进行Commit时，建议勾上窗口右边的"Optimize imports"选框
4. 鉴于目前使用了多模块，在运行应用前需要在运行配置中将Working Directory改为$MODULE_WORKING_DIR$
5. 如果需要启动调试，先启动BatchApplication，再启动BackendApplication