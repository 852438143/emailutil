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
public class ForwardEmailTest {

	@Test
	public void testForwardEmail() throws FileNotFoundException {
		
		// 获取配置的登陆邮件服务器的信息
		EmailServerInfo emailServerInfo = EmailTemplateUtil.getConfigEmailServerInfo();
		
		String content = "这是转发的附加内容";
		String[] attachmentFiles = {"E:\\Photos\\bob.jpeg"};  // 设置附件
		// 设置转发的地址
		String[] forwardAddress = {"1291833546@qq.com", "2051459265@qq.com"};
		
		if (EmailTemplateUtil.forwardEmail(emailServerInfo, 61, content, attachmentFiles, forwardAddress)) {
			System.out.println("转发成功！");
		} else {
			System.out.println("转发失败！");
		}
	}
}
