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
//代码文件：chapter29/QQ2006/src/main/kotlin/com/a51work6/qq/server/Server.kt
package com.a51work6.qq.server

import com.a51work6.qq.client.parser
import com.a51work6.qq.client.socket
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.beust.klaxon.int
import com.beust.klaxon.json
import java.lang.StringBuilder
import java.net.DatagramPacket
import java.net.DatagramSocket

// 操作命令代码
const val COMMAND_TEST_LOGIN = 1 // 登录命令
const val COMMAND_TEST_LOGOUT = 2 // 注销命令
const val COMMAND_TEST_SENDMSG = 3 // 发消息命令
const val COMMAND_TEST_REFRESH = 4 // 刷新好友列表命令

const val SERVER_TEST_PORT = 7788


fun main(args: Array<String>) {
    println("服务器启动，监听自己的端口$SERVER_TEST_PORT")

    var parserTest: Parser = Parser()
    var dao = UserDAO()
    val clientList = mutableListOf<ClientInfo>()

    DatagramSocket(SERVER_TEST_PORT).use { socket ->
        while (true) {
            var bufferTest = ByteArray(1024)
            var packet = DatagramPacket(bufferTest, bufferTest.size)
            socket.receive(packet)

            val jsonTestString = String(bufferTest, 0, packet.length)
            val addressTest = packet.address
            val portTest = packet.port
            println("服务端接受客服端消息：$jsonTestString")

            val jsonObjectTest = parser.parse(StringBuilder(jsonTestString)) as JsonObject
            val cmd = jsonObjectTest.int("command")

            when (cmd) {
                COMMAND_TEST_LOGIN -> {
                    val userId = jsonObjectTest["user_id"] as String
                    val userPwd = jsonObjectTest["user_pwd"] as String
                    val user = dao.findById(userId)
                    if (user != null && userPwd == user["user_pwd"]) {
                        val sendJsonObj = JsonObject(user)
                        sendJsonObj["result"] = "0"
                        val cInfo = ClientInfo(portTest, addressTest, userId)
                        if (clientList.none() { it.userId == userId }) {
                            clientList.add(cInfo)
                        }
                        // 去除好友用户列表

                        val friends = dao.findFriends(userId)?.map {
                            val friend = it.toMutableMap()
                            val fid = it["user_id"]
                            if (clientList.any { it.userId == fid }) friend["online"] = "1" else friend["online"] = "0"
                            friend
                        }!!.map {
                            JsonObject(it)
                        }

                        sendJsonObj["friends"] = json {
                            array(friends)
                        }

                        println("服务器发送消息成功，消息${sendJsonObj.toJsonString()}")
                        bufferTest = sendJsonObj.toJsonString().toByteArray()
                        packet = DatagramPacket(bufferTest, bufferTest.size, addressTest, portTest)
                        socket.send(packet)
                    } else {
                        val jsonObj = json {
                            obj("result" to "-1")
                        }
                        println("服务器给用户登录失败，消息：${jsonObj.toJsonString()}")
                        bufferTest = jsonObj.toJsonString().toByteArray()
                        packet = DatagramPacket(bufferTest, bufferTest.size, addressTest, portTest)
                        socket.send(packet)
                    }
                }
            }
            if (clientList.isEmpty()) {
                continue
            }

            val jsonObj = JsonObject()
            jsonObj["command"] = COMMAND_TEST_REFRESH

            val userIdList = clientList.map {
                it.userId
            }
            jsonObj["OnlineUserList"] = json {
                array(userIdList)
            }
            println("服务器向客户端发送消息，刷新用户列表：${jsonObj.toJsonString()}")

            clientList.forEach {
                bufferTest = jsonObj.toJsonString().toByteArray()
                packet = DatagramPacket(bufferTest, bufferTest.size, addressTest, portTest)
                socket.send(packet)
            }

        }
    }

}

