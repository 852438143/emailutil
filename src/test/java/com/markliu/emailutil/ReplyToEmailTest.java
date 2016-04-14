package com.markliu.emailutil;

import java.io.FileNotFoundException;

import org.junit.Test;

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
	public void testReplyToEmail() throws FileNotFoundException {
		
		// 获取配置的登陆邮件服务器的信息
		EmailServerInfo emailServerInfo = EmailTemplateUtil.getConfigEmailServerInfo();
		
		String content = "这是回复内容";
		String[] attachmentFiles = {"E:\\Photos\\bob.jpeg"};
		
		if (EmailTemplateUtil.replyEmail(emailServerInfo, 61, content, attachmentFiles)) {
			System.out.println("回复成功！");
		} else {
			System.out.println("回复失败！");
		}
	}
}
