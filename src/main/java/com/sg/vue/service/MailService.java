package com.sg.vue.service;

import com.sg.vue.converter.ResponseResult;
import com.sg.vue.model.ao.MailDTO;
import com.sun.mail.util.MailSSLSocketFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class MailService {

    @Value("${qq.sqm:xxxx}")
    String sqm;

    @Value("${qq.sendMail:xxx@qq.com}")
    String sendMail;

    public ResponseResult<String> sendMail(MailDTO mailDTO) {
        if (StringUtils.isBlank(sqm) || StringUtils.isBlank(sendMail)) {
            return ResponseResult.fail("-1", "请检查配置,授权码或发送方邮箱地址为空");
        }
        Properties properties = new Properties();
        //设置QQ邮件服务器
        properties.setProperty("mail.host", "smtp.qq.com");
        //邮件发送协议
        properties.setProperty("mail.transport.protocol", "smtp");
        //需要验证用户名密码
        properties.setProperty("mail.smtp.auth", "true");
        Transport transport = null;
        try {
            //还要设置SSL加密，加上以下代码即可
            MailSSLSocketFactory mailSSLSocketFactory = new MailSSLSocketFactory();
            mailSSLSocketFactory.setTrustAllHosts(true);
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.ssl.socketFactory", mailSSLSocketFactory);

            //使用JavaMail发送邮件的5个步骤
            //1、创建定义整个应用程序所需环境信息的 Session 对象
            Session session = Session.getDefaultInstance(properties, new Authenticator() {
                @Override
                public PasswordAuthentication getPasswordAuthentication() {
                    //发件人用户名，授权码
                    return new PasswordAuthentication(sendMail, sqm);
                }
            });

            //开启Session的debug模式，这样就可以查看程序发送Email的运行状态
            session.setDebug(true);
            //2、通过session得到transport对象
            transport = session.getTransport();
            //3、使用用户名和授权码连上邮件服务器qq.com
            transport.connect("smtp.qq.com", sendMail, sqm);
            //4、创建邮件：写邮件
            //注意需要传递Session
            MimeMessage message = new MimeMessage(session);
            //指明邮件的发件人
            message.setFrom(new InternetAddress(sendMail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(mailDTO.getRecMail()));
            message.setSubject(mailDTO.getTitle());
            message.setContent(mailDTO.getContent(), "text/html;charset=UTF-8");
            //5、发送邮件
            transport.sendMessage(message, message.getAllRecipients());

            return ResponseResult.success("邮件发送成功");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (transport != null) {
                try {
                    //6、关闭连接
                    transport.close();
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        }
        return ResponseResult.fail("-1", "邮件发送失败");
    }
}
