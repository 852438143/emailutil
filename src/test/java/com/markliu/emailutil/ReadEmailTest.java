package com.markliu.emailutil;

import java.io.FileNotFoundException;
import java.util.List;

import org.junit.Test;

import com.markliu.emailutil.entities.EmailServerInfo;
import com.markliu.emailutil.entities.ReadEmailInfo;
import com.markliu.emailutil.util.EmailTemplateUtil;

/**
 * 
 * 
 * @auther SunnyMarkLiu
 * @time Apr 12, 2016 10:57:13 PM
 */
public class ReadEmailTest {

	@Test
	public void testReadAllEmails() throws FileNotFoundException {
		
		// 获取配置的登陆邮件服务器的信息
		EmailServerInfo emailServerInfo = EmailTemplateUtil.getConfigEmailServerInfo();
		
		List<ReadEmailInfo> emailInfos = EmailTemplateUtil.getAllEmailInfos(emailServerInfo);
		System.out.println("邮件数目：" + emailInfos.size());
		for (ReadEmailInfo emailInfo : emailInfos) {
			System.out.println(emailInfo.toString());
		}
	}
	
	@Test
	public void testReadEmailTemplate() throws FileNotFoundException {
		
		// 获取配置的登陆邮件服务器的信息
		EmailServerInfo emailServerInfo = EmailTemplateUtil.getConfigEmailServerInfo();
		
		ReadEmailInfo emailInfo = EmailTemplateUtil.getLatestOneEmailInfo(emailServerInfo);
		System.out.println(emailInfo.toString());
	}
}
