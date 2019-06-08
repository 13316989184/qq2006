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
//代码文件：chapter29/QQ2006/src/main/kotlin/com/a51work6/qq/client/Client.kt
package com.a51work6.qq.client

import com.beust.klaxon.Parser
import java.net.DatagramSocket

// 操作命令代码
const val COMMAND_LOGIN = 1 // 登录命令
const val COMMAND_LOGOUT = 2 // 注销命令
const val COMMAND_SENDMSG = 3 // 发消息命令
const val COMMAND_REFRESH = 4 // 刷新好友列表命令
// 服务器端IP
const val SERVER_IP = "127.0.0.1"
// 服务器端端口号
const val SERVER_PORT = 7788

var socket = DatagramSocket()
//JSON解析器
val parser: Parser = Parser()

fun main(args: Array<String>) {
    // 设置超时1秒，不再等待接收数据
    socket.soTimeout = 1000
    println("客户端运行...")
    LoginFrame().isVisible = true
}