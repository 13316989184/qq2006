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
//代码文件：chapter29/QQ2006/src/main/kotlin/com/a51work6/qq/server/UserDAO.kt
package com.a51work6.qq.server

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class UserDAO {

    // 按照主键查询
    fun findById(id: String): Map<String, String>? {

        var list: List<Map<String, String>> = emptyList()
        //连接数据库
        Database.connect(URL, user = DB_USER, password = DB_PASSWORD, driver = DRIVER_CLASS)
        //操作数据库
        transaction {
            //添加SQL日志
            logger.addLogger(StdOutSqlLogger)
            list = Users.select { Users.user_id.eq(id) }.map {
                val row = mutableMapOf<String, String>()
                row["user_id"] = it[Users.user_id]
                row["user_pwd"] = it[Users.user_pwd]
                row["user_name"] = it[Users.user_name]
                row["user_icon"] = it[Users.user_icon]
                //Lambda表达式返回数据
                row
            }
        }
        return if (list.isEmpty()) null else list.first()
    }

    // 查询好友 列表
    fun findFriends(id: String): List<Map<String, String>>? {

        var list: List<Map<String, String>> = emptyList()
        //连接数据库
        Database.connect(URL, user = DB_USER, password = DB_PASSWORD, driver = DRIVER_CLASS)
        //操作数据库
        transaction {
            //添加SQL日志
            logger.addLogger(StdOutSqlLogger)
            val userList1 = Friends.slice(Friends.user_id2).select {
                Friends.user_id1.eq(id)
            }.map {
                it[Friends.user_id2]
            }

            val userList2 = Friends.slice(Friends.user_id1).select {
                Friends.user_id2.eq(id)
            }.map {
                it[Friends.user_id1]
            }

            list = Users.select {
                Users.user_id.inList(userList1 + userList2)
            }.map {
                val row = mutableMapOf<String, String>()
                row["user_id"] = it[Users.user_id]
                row["user_pwd"] = it[Users.user_pwd]
                row["user_name"] = it[Users.user_name]
                row["user_icon"] = it[Users.user_icon]
                //Lambda表达式返回数据
                row
            }
        }
        return list
    }
}
