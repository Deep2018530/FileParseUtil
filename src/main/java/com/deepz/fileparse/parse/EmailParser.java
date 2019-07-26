package com.deepz.fileparse.parse;

import com.deepz.fileparse.vo.StructableEmailVo;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.mail.util.MimeMessageParser;
import org.apache.commons.mail.util.MimeMessageUtils;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author zhangdingping
 * @date 2019/7/24 16:43
 * @description
 */
@com.deepz.fileparse.annotation.Parser(fileType = "eml")
public class EmailParser implements Parser<StructableEmailVo> {

    @Override
    public StructableEmailVo parse(String path) {
        File file = new File(path);
        MimeMessage mimeMessage = null;
        try {
            mimeMessage = MimeMessageUtils.createMimeMessage(null, file);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        MimeMessageParser parser = new MimeMessageParser(mimeMessage);

        StructableEmailVo emailVo = new StructableEmailVo();

        try {
            //邮件的收件人
            List<Address> to = parser.getTo();
            //邮件的“抄送"收件人
            List<Address> cc = parser.getCc();
            //邮件的"密件抄送"收件人
            List<Address> bcc = parser.getBcc();
            //消息的“发件人"字段
            String from = parser.getFrom();
            //电子邮件的"回复"地址
            String replyTo = parser.getReplyTo();
            //邮件主题
            String subject = parser.getSubject();

            //调用parse()才能提取内容(官方api对该方法的原文解释"Does the actual extraction.")
            MimeMessageParser parse = parser.parse();
            String plainContent = null;
            String htmlContent = null;
            if (parse.hasPlainContent()) {
                //纯邮件内容
                plainContent = parse.getPlainContent();
                //HTML邮件内容
                htmlContent = parse.getHtmlContent();
            }
            BeanUtils.copyProperties(emailVo, parser);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return emailVo;
    }
}
