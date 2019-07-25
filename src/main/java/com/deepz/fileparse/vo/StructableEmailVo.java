package com.deepz.fileparse.vo;

import javax.mail.Address;
import java.util.List;

/**
 * @author zhangdingping
 * @date 2019/7/25 10:34
 * @description
 */
public class StructableEmailVo {

    /**
     * 邮件的收件人
     */
    private List<Address> to;
    /**
     * 邮件的“抄送"收件人
     */
    private List<Address> cc;
    /**
     * 邮件的"密件抄送"收件人
     */
    private List<Address> bcc;
    /**
     * 消息的“发件人"字段
     */
    private String from;
    /**
     * 电子邮件的"回复"地址
     */
    private String replyTo;
    /**
     * 邮件主题
     */
    private String subject;
    /**
     * 邮件内容
     */
    private String plainContent;

    /**
     * html邮件内容
     */
    private String htmlContent;

    public List<Address> getTo() {
        return to;
    }

    public void setTo(List<Address> to) {
        this.to = to;
    }

    public List<Address> getCc() {
        return cc;
    }

    public void setCc(List<Address> cc) {
        this.cc = cc;
    }

    public List<Address> getBcc() {
        return bcc;
    }

    public void setBcc(List<Address> bcc) {
        this.bcc = bcc;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPlainContent() {
        return plainContent;
    }

    public void setPlainContent(String plainContent) {
        this.plainContent = plainContent;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }
}
