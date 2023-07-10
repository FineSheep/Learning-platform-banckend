## 项目介绍

该项目为校园防诈骗服务平台。（本人毕业设计）项目包含几大模块，文章游览、答题对战、在线资讯、消息中心、文章中心、个人中心。

> 在老师的要求下修改了好多次，最终成功的变成自己也不不想看的样子。:dizzy_face:

- 文章中心：采用markdown语法展示文章内容，集成评论框，可发表评论。
- 答题对战：分为个人答题以及匹配答题:hotsprings:。个人答题系统随机抽取，匹配答题系统随机匹配另一在线玩家进行答题游戏。
- 在线资讯：采用定时任务自动爬取相关网站:japan::japan:。
- 消息中心：3类消息，点赞收藏、系统、评论。
- 文章中心：文章发表以及预览，采用markdown编辑器实现效果。
- 个人中心：修改个人信息、密码、头像。

前端地址：[前端地址](https://github.com/FineSheep/learning-platform-fronted)

后端地址：[后端地址](https://github.com/FineSheep/learning-platform-banckend)

## 后端技术

- springboot+mybatisPlus+mysql
- redis
- 使用x-spring-file-storage实现七牛云服务器实现图片上传
- jwt续约以及强制登录
- 邮件发送
- 定时任务+爬虫
- easy-excel实现excel上传
- websocket实现匹配对战
- knife4j接口文档
- aop实现简单的权限控制（后面弃用，改用ruoyi:cold_sweat:）

## 前端技术

> 前端小趴菜一个，还是为了写毕设学的vue，代码稀碎，嘿嘿嘿:hankey:

- vue2+vuex+vue-router+axios
- md编辑器
- websocket

---

还去网上巴拉了一个别人页面硬塞进去。。。。总算是熬到了毕业，大家加油(ง •_•)ง

> 我应该没有把数据库密码、服务器啥的传上来吧？大家手下留情。:innocent:
