本网站是本人研究生时期做的一个课程设计，为一个类知乎类型的问答网站
网站依赖spring boot搭建  版本 1.3.6  注意：因为1.5以上的版本不再支持velocity

操作系统： windows 10

模板语言：Velovity

ORM框架： Mybatis 1.1.1

数据库 ：MySQL 5.7

单元测试：Junit4

1.登陆注册功能
用户名注册合法性，防止注入，密码salt+MD5加密
加ticket，下发cookie，记住用户登陆状态
使用SpringMVC拦截器Interceptor，每访问页面就查询用户是否登录

2.添加问题功能
对html标签进行过滤，防止XSS攻击 