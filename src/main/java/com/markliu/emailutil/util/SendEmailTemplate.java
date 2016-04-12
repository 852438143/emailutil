package com.markliu.emailutil.util;

import javax.mail.Session;

import com.markliu.emailutil.entities.EmailInfo;
import com.markliu.emailutil.entities.EmailServerInfo;
import com.markliu.emailutil.service.EmailServerService;

/**
 * 
 * 
 * @auther SunnyMarkLiu
 * @time Apr 12, 2016 5:43:46 PM
 */
public class SendEmailTemplate {

	public static boolean sendEmail(EmailServerInfo emailServerInfo, EmailInfo email) {
		EmailServerService emailServerService = new EmailServerService();
		// 如果登陆成功，则进行发送邮件
		
		Session sendMailSession = emailServerService.loginEmailServer(emailServerInfo);
		if (sendMailSession != null) {
			System.out.println(emailServerInfo.getMailServerHost() + " 登陆成功！");
			System.out.println("正在发送邮件...");
			boolean result = emailServerService.sendEmail(sendMailSession, emailServerInfo, email);
			if (result) {
				System.out.println("发送成功！");
			} else {
				System.out.println("发送失败！");
			}
			return result;
		} else {
			System.out.println(emailServerInfo.getMailServerHost() + " 登陆失败！");
			return false;
		}
	}
}
