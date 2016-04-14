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
public class ForwardEmailTest {

	@Test
	public void testForwardEmail() throws FileNotFoundException {
		EmailServerInfo emailServerInfo = new EmailServerInfo();
		emailServerInfo.setMailServerPOP3Host(EmailServerHostAndPort.NetEase163_POP3_SERVER);
		emailServerInfo.setMailServerSMTPHost(EmailServerHostAndPort.NetEase163_SMTP_SERVER);
		emailServerInfo.setValidate(true);
		emailServerInfo.setUserName("xxxxxx@163.com");
		emailServerInfo.setPassword("xxxxxx"); // 注意使用的是开通 SMTP、 POP、IMAP 协议的授权码
		emailServerInfo.setMyEmailAddress("xxxxxx@163.com");
		
		String content = "这是转发的附加内容";
		String[] attachmentFiles = {"E:\\Photos\\bob.jpeg"};  // 设置附件
		// 设置转发的地址
		String[] forwardAddress = {"xxxxxx@qq.com", "xxxxxx@qq.com"};
		
		if (EmailTemplateUtil.forwardEmail(emailServerInfo, 61, content, attachmentFiles, forwardAddress)) {
			System.out.println("转发成功！");
		} else {
			System.out.println("转发失败！");
		}
	}
}
