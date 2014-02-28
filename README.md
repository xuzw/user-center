user-center
===========

本账号注册验证平台，使用HTTP协议接受注册新用户、或验证用户密码的请求。

注册新用户（POST）请求示例：
    URL: http://localhost:9798/user-center/register
    REQUEST BODY: {"userName":"中文用户名", "password":"密码"}
    
    成功则返回: {"metaDate":{"result":0,"timestamp":1384395832465}}
    内部错误则返回: {"metaDate":{"result":9999,"timestamp":1384395846603}}
    失败则返回: {"metaDate":{"result":1,"timestamp":1384395846603}}
        结果码1：用户名太短
        结果码2：用户名太长
        结果码3：用户名无效、不合法
        结果码10：重复注册    

验证用户密码（POST）请求示例：
    URL: http://localhost:9798/user-center/verify
    REQUEST BODY: {"userName":"中文用户名", "password":"密码"}
    
    成功则返回: {"metaDate":{"result":0,"timestamp":1384395912792}}
    内部错误则返回: {"metaDate":{"result":9999,"timestamp":1384395846603}}
    失败则返回: {"metaDate":{"result":1,"timestamp":1384395934672}}
    
    注: 验证时忽略尾部多余的空格，"密码  "，和"密码"是等价的，
        这是Mysql数据库特性。

数据库表结构
    CREATE TABLE `User` (
        `id` varchar(64) NOT NULL ,
        `name` varchar(64) NOT NULL ,
        `password` varchar(64) NOT NULL ,
        `dateCreated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
        PRIMARY KEY (`name`),
        UNIQUE INDEX `id` USING BTREE (`id`) 
    )
