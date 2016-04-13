package com.markliu.emailutil.service;

import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.markliu.emailutil.entities.EmailInfo;
import com.markliu.emailutil.entities.EmailServerInfo;
import com.markliu.emailutil.entities.ReadEmailInfo;
import com.markliu.emailutil.util.FetchingEmailUtil;

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
	public Session loginEmailServer(EmailServerInfo emailServerInfo, boolean useReadProtocol) {
		Session sendMailSession = null;
		Authenticator authentication = null;
		
		try {
			Properties properties = getProperties(emailServerInfo, useReadProtocol);
			// 如果需要身份认证，则创建一个密码验证器
			if (emailServerInfo.isValidate()) {
				authentication = new Authenticator() {
					
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(emailServerInfo.getUserName(), emailServerInfo.getPassword());
					}
				};
			}
			// 获取回话对象
			sendMailSession = Session.getDefaultInstance(properties, useReadProtocol ? null : authentication);
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
            BodyPart bodyPart = new MimeBodyPart();  
            bodyPart.setContent(email.getContent(), "text/html; charset=UTF-8");  
            bodyPartContainer.addBodyPart(bodyPart);    
            if(!email.getAttachmentFiles().isEmpty()){ // 存在附件  
                for(String fileName : email.getAttachmentFiles()) { // 遍历所有的附件
                	bodyPart=new MimeBodyPart();  
                    FileDataSource fds=new FileDataSource(fileName); //得到数据源  
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
	 * 获取最近的一份邮件，并保存附件
	 * @param sendMailSession
	 * @param emailServerInfo
	 * @return
	 */
	public ReadEmailInfo getLatestOneEmailFromStore(Session sendMailSession, EmailServerInfo emailServerInfo) {
		ReadEmailInfo emailInfo = null;
		Store store = null;
        try {
            store = sendMailSession.getStore("pop3");
            store.connect(emailServerInfo.getUserName(), emailServerInfo.getPassword());
 
            FetchingEmailUtil fetchingEmailUtil = new FetchingEmailUtil();
            
            emailInfo = fetchingEmailUtil.fetchingLatestEmailFromStore(store, true);
            
            // close the store
	        return emailInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (store != null) {
                    store.close();
                }
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
	}
	
	public List<ReadEmailInfo> readAllEmailInfos(Session sendMailSession, EmailServerInfo emailServerInfo) {
		
		List<ReadEmailInfo> allEmailInfos = null;
		Store store = null;
        try {
            store = sendMailSession.getStore("pop3");
            store.connect(emailServerInfo.getUserName(), emailServerInfo.getPassword());
 
            FetchingEmailUtil fetchingEmailUtil = new FetchingEmailUtil();
            
            fetchingEmailUtil.fetchingLatestEmailFromStore(store, true);
            
            // close the store
	        return allEmailInfos;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (store != null) {
                    store.close();
                }
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
	}
	
	/**
	 * 获得邮件会话属性
	 * <b>注：此处需要适配 SMTP、POP3、IMAP</>
	 * 
	 */
	private Properties getProperties(EmailServerInfo emailServerInfo, boolean useReadProtocol) {
		Properties p = new Properties();
		if (useReadProtocol) {
	        p.put("mail.pop3.host", emailServerInfo.getMailServerHost());
	        p.put("mail.pop3.port", emailServerInfo.getMailServerPort());
	        p.put("mail.smtp.auth", emailServerInfo.isValidate() ? "true" : "false");
	        p.put("mail.pop3s.starttls.enable", "true");
		} else {
			p.put("mail.smtp.host", emailServerInfo.getMailServerHost());
			p.put("mail.smtp.port", emailServerInfo.getMailServerPort());
			p.put("mail.smtp.auth", emailServerInfo.isValidate() ? "true" : "false");
			p.put("mail.pop3s.starttls.enable", "true");
		}
		return p;
	}
	
}
