/*
* Created by 智捷课堂
* 本书网站：www.51work6.com 
* 智捷课堂在线视频：www.zhijieketang.com
* 智捷课堂微信公共号：zhijieketang
* 邮箱：eorient@sina.com
* Java&Kotlin读者服务QQ群：547370999
* 【配套电子书】网址：
*       百度阅读：
*       https://yuedu.baidu.com/ebook/9ab2a82bf342336c1eb91a37f111f18583d00ca2
*/
//代码文件：chapter29/QQ2006/src/main/kotlin/com/a51work6/qq/server/DBSchema.kt
package com.a51work6.qq.server

import org.jetbrains.exposed.sql.Table

const val URL = "jdbc:mysql://localhost:3306/qq?useSSL=false&verifyServerCertificate=false"
const val DRIVER_CLASS = "com.mysql.jdbc.Driver"
const val DB_USER = "root"
const val DB_PASSWORD = "123456"

/* 用户表 */
object Users : Table() {
    //声明表中字段
    val user_id = varchar("user_id", length = 80).primaryKey()  /* 用户Id  */
    val user_pwd = varchar("user_pwd", length = 25)         /* 用户密码 */
    val user_name = varchar("user_name", length = 80)       /* 用户名 */
    val user_icon = varchar("user_icon", length = 100)      /* 用户头像 */
}

/* 用户好友表Id1和Id2互为好友 */
object Friends : Table() {
    val user_id1 = varchar("user_id1", length = 10).primaryKey() /* 用户Id1  */
    val user_id2 = varchar("user_id2", length = 10).primaryKey() /* 用户Id2  */
}