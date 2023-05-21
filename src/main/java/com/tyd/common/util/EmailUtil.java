package com.tyd.common.util;

import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Properties;

/**
 * 创建简单邮件
 * @description: MailTest
 * @author: 庄霸.liziye
 * @create: 2021-11-23 14:44
 **/
public class EmailUtil {
    public static void main(String[] args) throws Exception {
        //创建邮件
        Properties props = new Properties();                // 用于连接邮件服务器的参数配置
        Session session= Session.getInstance(props);        // 根据参数配置，创建会话对象
        MimeMessage message = new MimeMessage(session);     // 创建邮件对象

        /*
         * 也可以读取本地的eml 创建MimeMessage对象
         * new MimeMessage(session, new FileInputStream("D://email//test.eml"));
         */

        // From: 发件人
        // 其中 InternetAddress 的三个参数分别为: 发件人邮箱, 显示的昵称(只用于显示, 没有特别的要求), 昵称的字符集编码
        message.setFrom(new InternetAddress("XXXXXX", "不肯过江东", "UTF-8"));

        // To: 收件人
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress("XXXXXX", "USER_CC", "UTF-8"));
        // To: 增加收件人（可选）
        //message.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress("xxxx@xxxx.com", "USER_DD", "UTF-8"));
        // Cc: 抄送（可选）
        //message.setRecipient(MimeMessage.RecipientType.CC, new InternetAddress("xxxx@xxxx.com", "USER_EE", "UTF-8"));
        // Bcc: 密送（可选）
        //message.setRecipient(MimeMessage.RecipientType.BCC, new InternetAddress("xxxx@xxxx.com", "USER_FF", "UTF-8"));

        // Subject: 邮件主题
        message.setSubject("测试邮件发送", "UTF-8");

        // Content: 邮件正文（可以使用html标签）
        message.setContent("我在人民广场吃着炸鸡", "text/html;charset=UTF-8");

        // 设置显示的发件时间
        message.setSentDate(new Date());

        // 保存前面的设置
        message.saveChanges();

        // 将该邮件保存到本地
        OutputStream out = new FileOutputStream("D:\\email\\test.eml");
        message.writeTo(out);
        out.flush();
        out.close();
    }
}