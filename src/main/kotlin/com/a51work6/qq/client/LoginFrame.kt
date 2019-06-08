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
//代码文件：chapter29/QQ2006/src/main/kotlin/com/a51work6/qq/client/LoginFrame.kt
package com.a51work6.qq.client

import com.beust.klaxon.JsonObject
import com.beust.klaxon.json
import com.beust.klaxon.string
import java.awt.Color
import java.awt.Font
import java.awt.Toolkit
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.net.DatagramPacket
import java.net.InetAddress
import javax.swing.*

class LoginFrame : JFrame() {

    // 获得当前屏幕的宽和高
    private val screenWidth = Toolkit.getDefaultToolkit().screenSize.getWidth()
    private val screenHeight = Toolkit.getDefaultToolkit().screenSize.getHeight()

    // 登录窗口宽和高
    private val frameWidth = 329
    private val frameHeight = 250

    // QQ号码文本框
    private var txtUserId = JTextField()
    // QQ密码框
    private var txtUserPwd = JPasswordField()

    // 蓝线面板
    private val paneLine: JPanel
        get() {

            val paneLine = JPanel().apply {
                layout = null
                setBounds(7, 54, 308, 118)
                border = BorderFactory.createLineBorder(Color(102, 153, 255), 1)
            }

            with(JLabel()) {
                //lblHelp
                setBounds(227, 47, 67, 21)
                font = Font("Dialog", Font.PLAIN, 12)
                foreground = Color(51, 51, 255)
                text = "忘记密码？"
                paneLine.add(this)
            }
            with(JLabel()) {
                //lblUserPwd
                text = "QQ密码"
                font = Font("Dialog", Font.PLAIN, 12)
                setBounds(21, 48, 54, 18)
                paneLine.add(this)
            }

            with(JLabel()) {
                //lblUserId
                text = "QQ号码↓"
                font = Font("Dialog", Font.PLAIN, 12)
                setBounds(21, 14, 55, 18)
                paneLine.add(this)
            }

            txtUserId.setBounds(84, 14, 132, 18)
            paneLine.add(this.txtUserId)

            txtUserPwd.setBounds(84, 48, 132, 18)
            paneLine.add(this.txtUserPwd)

            with(JCheckBox()) {
                //chbAutoLogin
                text = "自动登录"
                font = Font("Dialog", Font.PLAIN, 12)
                setBounds(79, 77, 73, 19)
                paneLine.add(this)
            }
            with(JCheckBox()) {
                //chbHideLogin
                text = "隐身登录"
                font = Font("Dialog", Font.PLAIN, 12)
                setBounds(155, 77, 73, 19)
                paneLine.add(this)
            }
            return paneLine
        }

    init {

        /// 初始化当前窗口
        iconImage = Toolkit.getDefaultToolkit().getImage(LoginFrame::class.java.getResource("/images/QQ.png"))
        title = "QQ登录"
        isResizable = false
        contentPane.layout = null
        // 设置窗口大小
        setSize(frameWidth, frameHeight)
        // 计算窗口位于屏幕中心的坐标
        val x = (screenWidth - frameWidth).toInt() / 2
        val y = (screenHeight - frameHeight).toInt() / 2
        // 设置窗口位于屏幕中心
        setLocation(x, y)

        // 添加蓝线面板
        contentPane.add(paneLine)

        with(JLabel()) {
            //lblImage
            icon = ImageIcon(LoginFrame::class.java.getResource("/images/QQll.JPG"))
            setBounds(0, 0, 325, 48)
            contentPane.add(this)
        }

        // 初始化登录按钮
        val btnLogin = JButton().apply {
            setBounds(152, 181, 63, 19)
            font = Font("Dialog", Font.PLAIN, 12)
            text = "登录"
            contentPane.add(this)
        }
        // 注册登录按钮事件监听器
        btnLogin.addActionListener {
            // 先进行用户输入验证，验证通过再登录
            val userId = txtUserId.text
            val password = String(txtUserPwd.password)

            val user = login(userId, password)
            if (user != null) {
                // 登录成功调转界面
                println("登录成功调转界面")
                FriendsFrame(user).isVisible = true
                // 设置登录窗口可见
                isVisible = false
            } else {
                JOptionPane.showMessageDialog(null, "您QQ号码或密码不正确")
            }
        }

        // 初始化取消按钮
        val btnCancel = JButton().apply {
            setBounds(233, 181, 63, 19)
            font = Font("Dialog", Font.PLAIN, 12)
            text = "取消"
            contentPane.add(this)
        }
        btnCancel.addActionListener { System.exit(0) /* 退出系统*/ }

        // 初始化【申请号码↓】按钮
        with(JButton()) {
            setBounds(14, 179, 99, 22)
            font = Font("Dialog", Font.PLAIN, 12)
            text = "申请号码↓"
            contentPane.add(this)
        }

        // 注册窗口事件
        addWindowListener(object : WindowAdapter() {
            // 单击窗口关闭按钮时调用
            override fun windowClosing(e: WindowEvent) {
                // 退出系统
                System.exit(0)
            }
        })
    }

    // 客户端向服务器发送登录请求
    private fun login(userId: String, password: String): Map<String, Any>? {

        val address = InetAddress.getByName(SERVER_IP)

        var jsonObj = json {
            obj("command" to COMMAND_LOGIN,
                    "user_id" to userId,
                    "user_pwd" to password)
        }
        // 字节数组
        var buffer = jsonObj.toJsonString().toByteArray()
        // 创建DatagramPacket对象
        var packet = DatagramPacket(buffer, buffer.size, address, SERVER_PORT)
        // 发送数据
        socket.send(packet)

        // 接收数据
        // 准备一个缓冲区
        buffer = ByteArray(1024)
        packet = DatagramPacket(buffer, buffer.size, address, SERVER_PORT)
        socket.receive(packet)

        val jsonString = String(buffer, 0, packet.length)
        println("从服务器返回的消息：$jsonString")
        jsonObj = parser.parse(StringBuilder(jsonString)) as JsonObject

        //登录失败
        if (jsonObj.string("result") == "-1") return null

        return jsonObj as Map<String, Any>?
    }
}
