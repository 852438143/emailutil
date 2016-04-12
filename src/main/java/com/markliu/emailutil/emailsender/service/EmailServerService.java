package com.markliu.emailutil.emailsender.service;

import java.io.File;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.markliu.emailutil.emailsender.entities.EmailInfo;
import com.markliu.emailutil.emailsender.entities.EmailServerInfo;

/**
 * 
 * 
 * @auther SunnyMarkLiu
 * @time Apr 12, 2016 4:47:31 PM
 */
public class EmailServerService {

	/**
	 * 根据 EmailServerInfo 信息登陆邮件服务器，返回mail回话对象
	 * 
	 * @param emailServerInfo
	 * @return
	 */
	public Session loginEmailServer(EmailServerInfo emailServerInfo) {
		Session sendMailSession = null;
		Authenticator authentication = null;
		
		try {
			Properties properties = getProperties(emailServerInfo);
			// 如果需要身份认证，则创建一个密码验证器
			if (emailServerInfo.isValidate()) {
				authentication = new EmailAuthenticator(emailServerInfo);
			}
			// 获取回话对象
			sendMailSession = Session.getDefaultInstance(properties, authentication);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return sendMailSession;
	}

	/**
	 * 发送邮件
	 * @param sendMailSession
	 * @param emailServerInfo
	 * @param email
	 * @return
	 */
	public boolean sendEmail(Session sendMailSession, EmailServerInfo emailServerInfo, EmailInfo email) {
		try {
			// 根据session创建一个邮件消息
			Message mailMessage = new MimeMessage(sendMailSession);
			// 创建邮件发送者地址
			Address from = new InternetAddress(emailServerInfo.getMyEmailAddress());
			// 设置邮件消息的发送者
			mailMessage.setFrom(from);
			
			// 创建邮件的接收者地址，并设置到邮件消息中
			String[] toAddresseStrings = email.getToAddress();
			Address[] toAddresse = new InternetAddress[toAddresseStrings.length];
			for(int i = 0 ; i < toAddresseStrings.length; i++) {
				toAddresse[i] = new InternetAddress(toAddresseStrings[i]);
			}
			mailMessage.setRecipients(Message.RecipientType.TO, toAddresse);
			
			// 设置邮件消息的主题
			mailMessage.setSubject(email.getSubject());
			
			// Multipart is a container that holds multiple body parts.
			Multipart bodyPartContainer = new MimeMultipart();  
            MimeBodyPart bodyPart = new MimeBodyPart();  
            bodyPart.setContent(email.getContent(), "text/html;charset=gb2312");  
            bodyPartContainer.addBodyPart(bodyPart);    
            if(!email.getAttachmentFiles().isEmpty()){ // 存在附件  
                for(File file : email.getAttachmentFiles()) { // 遍历所有的附件
                	bodyPart=new MimeBodyPart();  
                    FileDataSource fds=new FileDataSource(file); //得到数据源  
                    bodyPart.setDataHandler(new DataHandler(fds)); //得到附件本身并至入BodyPart  
                    bodyPart.setFileName(fds.getName());  //得到文件名同样至入BodyPart
                    bodyPartContainer.addBodyPart(bodyPart);  
                }    
            }
            // 设置邮件消息的主要内容
            mailMessage.setContent(bodyPartContainer); //Multipart加入到信件
            
			// 设置邮件消息发送的时间
			mailMessage.setSentDate(new Date());
			
			// 发送邮件
			Transport.send(mailMessage);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 获得邮件会话属性
	 * <b>注：此处需要适配 SMTP、POP3、IMAP</>
	 * 
	 */
	private Properties getProperties(EmailServerInfo emailServerInfo) {
		Properties p = new Properties();
		p.put("mail.smtp.host", emailServerInfo.getMailServerHost());
		p.put("mail.smtp.port", emailServerInfo.getMailServerPort());
		p.put("mail.smtp.auth", emailServerInfo.isValidate() ? "true" : "false");
		return p;
	}
	
	/**
	 * 密码验证器
	 *
	 */
	private class EmailAuthenticator extends Authenticator {
		private String userName = null;
		private String password = null;

		public EmailAuthenticator(EmailServerInfo emailServerInfo) {
			this.userName = emailServerInfo.getUserName();
			this.password = emailServerInfo.getPassword();
		}
		
		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(userName, password);
		}
	}
}
