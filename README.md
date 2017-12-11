本网站是本人研究生时期做的一个课程设计，为一个类知乎类型的问答网站
网站依赖spring boot搭建  版本 1.3.6  注意：因为1.5以上的版本不再支持velocity

操作系统： windows 10

模板语言：Velovity

ORM框架： Mybatis 1.1.1

数据库 ：MySQL 5.7  Redis 3.2

单元测试：Junit4

1.登陆注册模块

用户名注册合法性，防止注入，密码salt+MD5加密

加ticket，下发cookie，记住用户登陆状态

使用SpringMVC拦截器Interceptor，每访问页面就查询用户是否登录

2.添加问题模块

对html标签进行过滤，防止XSS攻击 

3.评论模块

对问题，评论 的评论

4.站内信

5.点赞
对评论点赞，通过Redis的set类型实现

6.异步化
通过异步队列优化整个网站的运行，具体实现在async包中。EventProducer将产生的事件加到队列中（通过redis的list实现），然后EventConsumer将自己感兴趣的事件取出交给Handler处理。

这样一些比较卡，或者耗时的操作就可以通过异步队列实现。 比如别人给你的评论点赞之后，通过站内信通知，就可以异步化.

7.JavaMail
邮件发送

8.关注模块
关注问题，关注用户，通过redis的Sortedset有序集合实现，用关注的时间排序。
