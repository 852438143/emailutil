package com.markliu.emailutil;

import java.io.FileNotFoundException;

import javax.mail.Session;

import org.junit.Test;

import com.markliu.emailutil.entities.EmailInfo;
import com.markliu.emailutil.entities.EmailServerInfo;
import com.markliu.emailutil.service.EmailServerService;
import com.markliu.emailutil.util.EmailTemplateUtil;

/**
 * 
 * 
 * @auther SunnyMarkLiu
 * @time Apr 12, 2016 3:42:02 PM
 */
public class SendEmailTest {

	@Test
	public void testSendEmailTemplate() throws FileNotFoundException {
		
		// 获取配置的登陆邮件服务器的信息
		EmailServerInfo emailServerInfo = EmailTemplateUtil.getConfigEmailServerInfo();
		
		// 构建邮件 email
		EmailInfo email = new EmailInfo();
		String[] toes = {"xxxxxx@qq.com", "xxxxxx@qq.com"};
		email.setToAddress(toes).setSubject("test 主题2");
		
		StringBuffer content = new StringBuffer();
		
		// 发送链接有的邮箱服务器可能将此邮件识别为垃圾邮件。
		content.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">")  
        .append("<html>")  
        .append("<head>")  
        .append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">")  
        .append("<title>测试邮件</title>")  
        .append("<style type=\"text/css\">")  
        .append(".test{font-family:\"Microsoft Yahei\";font-size: 18px;color: red;}")  
        .append("</style>")  
        .append("</head>")  
        .append("<body>")
        .append("<h2><font color=red>Header标题</font></h2><br/>")
        .append("<span class=\"test\">this is content!内容</span>")  
        .append("</body>")
        .append("</html>");  
		email.setContent(content.toString());
		
		// 设置上传的附件
		email.getAttachmentFiles().add("E:\\Photos\\bob.jpeg");
		email.getAttachmentFiles().add("E:\\heartocat.png");
//		email.getAttachmentFiles().add("E:\\Music\\Alison Krauss - When You Say Nothing At All.mp3");
		
		EmailTemplateUtil.sendEmail(emailServerInfo, email);
	}
	
	@Test
	public void testSendEmail() {

		// 获取配置的登陆邮件服务器的信息
		EmailServerInfo emailServerInfo = EmailTemplateUtil.getConfigEmailServerInfo();

		EmailServerService emailServerService = new EmailServerService();

		// 如果登陆成功，则进行发送邮件
		Session sendMailSession = emailServerService.loginEmailServer(emailServerInfo, false);
		if (sendMailSession != null) {
			System.out.println(emailServerInfo.getMailServerSMTPHost() + " 登陆成功！");
			// 构建邮件 email
			EmailInfo email = new EmailInfo();
			String[] toes = {"xxxxxx@qq.com", "xxxxxx@qq.com"};
			email.setToAddress(toes).setSubject("test 主题2")
					.setContent("this is content内容!");
			boolean result = emailServerService.sendEmail(sendMailSession, emailServerInfo, email);
			if (result) {
				System.out.println("发送成功！");
			} else {
				System.out.println("发送失败！");
			}
		} else {
			System.out.println(emailServerInfo.getMailServerSMTPHost() + " 登陆失败！");
		}
	}

}
