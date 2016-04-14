package com.markliu.emailutil.util;

import java.util.List;

import javax.mail.Session;

import com.markliu.emailutil.entities.EmailInfo;
import com.markliu.emailutil.entities.EmailServerInfo;
import com.markliu.emailutil.entities.ReadEmailInfo;
import com.markliu.emailutil.service.EmailServerService;

/**
 * 
 * 
 * @auther SunnyMarkLiu
 * @time Apr 12, 2016 5:43:46 PM
 */
public class EmailTemplateUtil {

	private static EmailServerService emailServerService = new EmailServerService();
	
	/**
	 * 回复第 msgnum 份邮件
	 * @param emailServerInfo
	 * @param msgnum
	 * @param content
	 * @param attachmentFiles
	 * @return
	 */
	public static boolean replyEmail(EmailServerInfo emailServerInfo, int msgnum, 
									String content, String[] attachmentFiles) {
		return emailServerService.replyEmail(emailServerInfo, msgnum, content, attachmentFiles);
	}
	
	
	/**
	 * 发送邮件的模板方法
	 * @param emailServerInfo
	 * @param email
	 * @return
	 */
	public static boolean sendEmail(EmailServerInfo emailServerInfo, EmailInfo email) {
		// 如果登陆成功，则进行发送邮件
		
		Session sendMailSession = emailServerService.loginEmailServer(emailServerInfo, false);
		if (sendMailSession != null) {
			System.out.println(emailServerInfo.getMailServerSMTPHost() + " 登陆成功！");
			System.out.println("正在发送邮件...");
			boolean result = emailServerService.sendEmail(sendMailSession, emailServerInfo, email);
			if (result) {
				System.out.println("发送成功！");
			} else {
				System.out.println("发送失败！");
			}
			return result;
		} else {
			System.out.println(emailServerInfo.getMailServerSMTPHost() + " 登陆失败！");
			return false;
		}
	}
	
	/**
	 * 获取所有邮件
	 * @param emailServerInfo
	 * @return
	 */
	public static List<ReadEmailInfo> getAllEmailInfos(EmailServerInfo emailServerInfo) {
		// 如果登陆成功，则进行发送邮件
		
		Session sendMailSession = emailServerService.loginEmailServer(emailServerInfo, true);
		if (sendMailSession != null) {
			System.out.println(emailServerInfo.getMailServerPOP3Host() + " 登陆成功！");
			System.out.println("正在读取邮件...");
			List<ReadEmailInfo> emailInfos = emailServerService.readAllEmailInfos(sendMailSession, emailServerInfo);
			return emailInfos;
		} else {
			System.out.println(emailServerInfo.getMailServerPOP3Host() + " 登陆失败！");
			return null;
		}
	}
	
	/**
	 * 获取最近的一份邮件，并保存附件
	 * 
	 * @param emailServerInfo
	 * @return
	 */
	public static ReadEmailInfo getLatestOneEmailInfo(EmailServerInfo emailServerInfo) {
		// 如果登陆成功，则进行发送邮件
		
		Session sendMailSession = emailServerService.loginEmailServer(emailServerInfo, true);
		if (sendMailSession != null) {
			System.out.println(emailServerInfo.getMailServerPOP3Host() + " 登陆成功！");
			System.out.println("正在读取邮件...");
			ReadEmailInfo emailInfo = emailServerService.getLatestOneEmailFromStore(sendMailSession, emailServerInfo);
			return emailInfo;
		} else {
			System.out.println(emailServerInfo.getMailServerPOP3Host() + " 登陆失败！");
			return null;
		}
	}
	
	/**
	 * 删除收件箱中第 msgnum 份邮件，本地数据库中需要保存读取邮件的 msgnum 序号！
	 * @param emailServerInfo
	 * @param msgnum
	 * @return
	 */
	public static boolean deleteEmailByMsgNum(EmailServerInfo emailServerInfo, int msgnum) {
		return emailServerService.deleteEmailByMsgNum(emailServerInfo, msgnum);
	}
}
