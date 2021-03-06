package cn.edu.nju.software.service;

import javax.mail.EncodingAware;
import javax.mail.MessagingException;

import cn.edu.nju.software.dto.UserDto;
import cn.edu.nju.software.util.EncodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.io.File;

/**
 * Created by mengf on 2018/4/20 0020.
 */
@Service
@Slf4j
public class MailService {
    @Autowired
    private JavaMailSender sender;

    @Value("${spring.mail.username}")
    private String from;

    @Value("${server.port}")
    private Integer port;

    @Value("${server.location}")
    private String location;

    /**
     * 发送纯文本的简单邮件
     *
     * @param tos
     * @param subject
     * @param content
     */
    public void sendSimpleMail(String[] tos, String subject, String content) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(tos);
        message.setSubject(subject);
        message.setText(content);

        try {
            sender.send(message);
            log.info("简单邮件已经发送。");
        } catch (Exception e) {
            log.error("发送简单邮件时发生异常！", e);
        }
    }

    /**
     * 发送html格式的邮件
     *
     * @param tos
     * @param subject
     * @param content
     */
    public void sendHtmlMail(String[] tos, String subject, String content) {
        MimeMessage message = sender.createMimeMessage();

        try {
            //true表示需要创建一个multipart message
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(tos);
            helper.setSubject(subject);
            helper.setText(content, true);

            sender.send(message);
            log.info("html邮件已经发送。");
        } catch (MessagingException e) {
            log.error("发送html邮件时发生异常！", e);
        }
    }

    /**
     * 发送带附件的邮件
     *
     * @param tos
     * @param subject
     * @param content
     * @param filePath
     */
    public void sendAttachmentsMail(String[] tos, String subject, String content, String filePath) {
        MimeMessage message = sender.createMimeMessage();

        try {
            //true表示需要创建一个multipart message
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(tos);
            helper.setSubject(subject);
            helper.setText(content, true);

            FileSystemResource file = new FileSystemResource(new File(filePath));
            String fileName = filePath.substring(filePath.lastIndexOf(File.separator));
            helper.addAttachment(fileName, file);

            sender.send(message);
            log.info("带附件的邮件已经发送。");
        } catch (MessagingException e) {
            log.error("发送带附件的邮件时发生异常！", e);
        }
    }

    /**
     * 发送嵌入静态资源（一般是图片）的邮件
     *
     * @param tos
     * @param subject
     * @param content 邮件内容，需要包括一个静态资源的id，比如：<img src=\"cid:rscId01\" >
     * @param rscPath 静态资源路径和文件名
     * @param rscId   静态资源id
     */
    public void sendInlineResourceMail(String[] tos, String subject, String content, String rscPath, String rscId) {
        MimeMessage message = sender.createMimeMessage();

        try {
            //true表示需要创建一个multipart message
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(tos);
            helper.setSubject(subject);
            helper.setText(content, true);
            FileSystemResource res = new FileSystemResource(new File(rscPath));
            helper.addInline(rscId, res);
            sender.send(message);
            log.info("嵌入静态资源的邮件已经发送。");
        } catch (MessagingException e) {
            log.error("发送嵌入静态资源的邮件时发生异常！", e);
        }
    }

    public void sendActiveMail(String username, String mail) {
        //TODO 将一些常用的邮件HTML模版可以列一下
        String[] mails = new String[]{mail};
        String encode = EncodeUtil.encodeBase64(username.getBytes());
        String url = location + ":" + port + "/account/active/" + encode;
        StringBuffer content = new StringBuffer("亲爱的用户").append(username)
                .append(":<br/>").append("您好！<br/>").append("您激活账户的链接如下：<br/><hr/>")
                //.append("<a href='").append(location).append(":").append(port).append("/active/")
                //.append(encode).append("'>").append("AI测试平台激活专用链接......").append("</a>")
                .append(url).append("<hr/>").append("祝生活愉快！<br>").append("ai测试平台");
        log.info("mail content {}", content);
        sendHtmlMail(mails, "激活邮件", content.toString());
    }
}
