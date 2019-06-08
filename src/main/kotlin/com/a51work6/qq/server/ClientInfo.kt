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
//代码文件：chapter29/QQ2006/src/main/kotlin/com/a51work6/qq/server/ClientInfo.kt
package com.a51work6.qq.server
import java.net.InetAddress

data class ClientInfo(
        val port: Int,// 客户端端口号
        val address: InetAddress,// 客户端IP地址
        val userId: String)// 用户Id