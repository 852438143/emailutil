package com.markliu.emailutil;

import java.io.FileNotFoundException;
import java.util.List;

import org.junit.Test;

import com.markliu.emailutil.entities.EmailServerHostAndPort;
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
		EmailServerInfo emailServerInfo = new EmailServerInfo();
		emailServerInfo.setMailServerHost(EmailServerHostAndPort.NetEase163_POP3_SERVER);
		emailServerInfo.setMailServerPort(EmailServerHostAndPort.POP3_PORT);
		emailServerInfo.setValidate(true);
		emailServerInfo.setUserName("xxxxxx@163.com");
		emailServerInfo.setPassword("xxxxxx"); // 注意使用的是开通 SMTP 协议的授权码
		emailServerInfo.setMyEmailAddress("xxxxxx@163.com");		
		
		List<ReadEmailInfo> emailInfos = EmailTemplateUtil.getAllEmailInfos(emailServerInfo);
		System.out.println("邮件数目：" + emailInfos.size());
		for (ReadEmailInfo emailInfo : emailInfos) {
			System.out.println(emailInfo.toString());
		}
	}
	
	@Test
	public void testReadEmailTemplate() throws FileNotFoundException {
		EmailServerInfo emailServerInfo = new EmailServerInfo();
		emailServerInfo.setMailServerHost(EmailServerHostAndPort.NetEase163_POP3_SERVER);
		emailServerInfo.setMailServerPort(EmailServerHostAndPort.POP3_PORT);
		emailServerInfo.setValidate(true);
		emailServerInfo.setUserName("xxxxxx@163.com");
		emailServerInfo.setPassword("xxxxxx"); // 注意使用的是开通 SMTP 协议的授权码
		emailServerInfo.setMyEmailAddress("xxxxxx@163.com");		
		
		ReadEmailInfo emailInfo = EmailTemplateUtil.getLatestOneEmailInfo(emailServerInfo);
		System.out.println(emailInfo.toString());
	}
}
