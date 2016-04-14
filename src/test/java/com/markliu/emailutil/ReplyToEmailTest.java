package com.markliu.emailutil;

import java.io.FileNotFoundException;

import org.junit.Test;

import com.markliu.emailutil.entities.EmailServerHostAndPort;
import com.markliu.emailutil.entities.EmailServerInfo;
import com.markliu.emailutil.util.EmailTemplateUtil;

/**
 * 
 * 
 * @auther SunnyMarkLiu
 * @time Apr 14, 2016 10:34:01 AM
 */
public class ReplyToEmailTest {

	@Test
	public void testDeleteEmail() throws FileNotFoundException {
		EmailServerInfo emailServerInfo = new EmailServerInfo();
		emailServerInfo.setMailServerPOP3Host(EmailServerHostAndPort.NetEase163_POP3_SERVER);
		emailServerInfo.setMailServerSMTPHost(EmailServerHostAndPort.NetEase163_SMTP_SERVER);
		emailServerInfo.setValidate(true);
		emailServerInfo.setUserName("xxxxxx@163.com");
		emailServerInfo.setPassword("xxxxxx"); // 注意使用的是开通 SMTP、 POP、IMAP 协议的授权码
		emailServerInfo.setMyEmailAddress("xxxxxx@163.com");
		
		String content = "这是回复内容";
		String[] attachmentFiles = {"E:\\Photos\\bob.jpeg"};
		
		if (EmailTemplateUtil.replyEmail(emailServerInfo, 59, content, attachmentFiles)) {
			System.out.println("回复成功！");
		} else {
			System.out.println("回复失败！");
		}
	}
}
