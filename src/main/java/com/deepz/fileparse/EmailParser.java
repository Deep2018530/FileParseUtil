package com.deepz.fileparse;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author zhangdingping
 * @date 2019/7/24 16:43
 * @description
 */
public class EmailParser extends FileParser {


    public Message parse(String path) throws IOException, MessagingException {

        File file = new File(path);

        Properties properties = System.getProperties();
        Properties props = System.getProperties();
        props.put("mail.host", "smtp.dummydomain.com");
        props.put("mail.transport.protocol", "smtp");

        Session mailSession = Session.getDefaultInstance(props, null);
        InputStream source = new FileInputStream(file);
        MimeMessage message = new MimeMessage(mailSession, source);

        System.out.println("Subject : " + message.getSubject());
        System.out.println("From : " + message.getFrom()[0]);
        System.out.println("--------------");
        System.out.println("Body : " + message.getContent());

        return message;
    }
}
