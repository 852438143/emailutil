package com.markliu.emailutil.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
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
import com.markliu.emailutil.entities.EmailServerHostAndPort;
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
	 * 获取配置的邮箱服务器的信息
	 * @return
	 */
	public EmailServerInfo getConfigEmailServerInfo() {
		// 读取配置文件
		Properties properties = new Properties();
		InputStream inStream = null;
		try {
			// 获取类路径(/)下的配置文件
			inStream = getClass().getResourceAsStream("/emailServerConfig.properties");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("配置文件加载失败");
			return null;
		}
		try {
			properties.load(inStream);
			String mailServer_POP3Host = properties.getProperty("mailServer_POP3Host");
			String mailServer_SMTPHost = properties.getProperty("mailServer_SMTPHost");
			String myEmailAddress = properties.getProperty("myEmailAddress");
			String userName = properties.getProperty("userName");
			String password = properties.getProperty("password");
			String validate = properties.getProperty("validate");
			
			EmailServerInfo emailServerInfo = new EmailServerInfo();
			
			if (mailServer_POP3Host != null && !("".equals(mailServer_POP3Host.trim()))) {
				emailServerInfo.setMailServerPOP3Host(mailServer_POP3Host.trim());
			}
			if (mailServer_SMTPHost != null && !("".equals(mailServer_SMTPHost.trim()))) {
				emailServerInfo.setMailServerSMTPHost(mailServer_SMTPHost.trim());
			}
			if (userName != null && !("".equals(userName.trim()))) {
				emailServerInfo.setUserName(userName.trim());
			}
			if (password != null && !("".equals(password.trim()))) {
				emailServerInfo.setPassword(password.trim());
			}
			if (myEmailAddress != null && !("".equals(myEmailAddress.trim()))) {
				emailServerInfo.setMyEmailAddress(myEmailAddress.trim());
			}
			if (validate != null && !("".equals(validate.trim()))) {
				boolean isValidate = "true".equals(validate.trim()) ? true : false;
				emailServerInfo.setValidate(isValidate);
			}
			
			System.out.println("--------邮件服务器配置信息--------");
			System.out.println(emailServerInfo.toString());
			return emailServerInfo;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (inStream != null) {
				try {
					inStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	

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
	 * 转发第 msgnum 份邮件, 并补充正文和附件
	 * 
	 * @param emailServerInfo
	 * @param msgnum
	 * @param content
	 * @param attachmentFiles
	 * @param forwardAddress
	 * @return
	 */
	public boolean forwardEmail(EmailServerInfo emailServerInfo,	int msgnum, 
					String content, String[] attachmentFiles, String[] forwardAddress) {
		Properties properties = new Properties();
		properties.put("mail.store.protocol", "pop3");
		properties.put("mail.pop3s.host",
				emailServerInfo.getMailServerPOP3Host());
		properties.put("mail.pop3s.port", EmailServerHostAndPort.POP3_TLS_PORT);
		properties.put("mail.pop3.starttls.enable", "true");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.host",
				emailServerInfo.getMailServerSMTPHost());
		properties.put("mail.smtp.port", EmailServerHostAndPort.SMTP_PORT);

		Session session = Session.getDefaultInstance(properties);
		try {
			// Get a Store object and connect to the current host
			Store store = session.getStore("pop3s");
			store.connect(emailServerInfo.getMailServerPOP3Host(),
					emailServerInfo.getMyEmailAddress(),
					emailServerInfo.getPassword());// change the user and
													// password accordingly

			// Create a Folder object and open the folder
			Folder folder = store.getFolder("inbox");
			folder.open(Folder.READ_ONLY);
			Message message = folder.getMessage(msgnum);
			// Get all the information from the message
			String to = InternetAddress.toString(message.getRecipients(Message.RecipientType.TO));

			// 转发的邮件信息
			Message forward = new MimeMessage(session);
			
			// 设置转发的地址
			if (forwardAddress != null && forwardAddress.length > 0) {
				Address[] addresses = new Address[forwardAddress.length];
				for (int i = 0; i < forwardAddress.length; i++) {
					addresses[i] = new InternetAddress(forwardAddress[i]);
				}
				forward.setRecipients(Message.RecipientType.TO,	addresses);
			} else {
				// 如果未设置转发地址，默认转发给原来的人
				String from = InternetAddress.toString(message.getFrom());
				forward.setRecipients(Message.RecipientType.TO,
			               InternetAddress.parse(from));
			}
			
			forward.setSubject("Fwd: " + message.getSubject());
			forward.setFrom(new InternetAddress(to));

			// Create the message part
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			// Create a multipart message
			Multipart multipart = new MimeMultipart();
			// set content
			messageBodyPart.setContent(message, "message/rfc822");
			// Add part to multi part
			multipart.addBodyPart(messageBodyPart);
			// Associate multi-part with message
			
			// Multipart is a container that holds multiple body parts.
            BodyPart bodyPart = new MimeBodyPart();  
            bodyPart.setContent(content, "text/html; charset=UTF-8");  
            multipart.addBodyPart(bodyPart);    
            if(attachmentFiles != null && attachmentFiles.length > 0){ // 存在附件  
                for(String fileName : attachmentFiles) { // 遍历所有的附件
                	bodyPart=new MimeBodyPart();  
                    FileDataSource fds=new FileDataSource(fileName); //得到数据源  
                    bodyPart.setDataHandler(new DataHandler(fds)); //得到附件本身并至入BodyPart  
                    bodyPart.setFileName(fds.getName());  //得到文件名同样至入BodyPart
                    multipart.addBodyPart(bodyPart);  
                }    
            }
            // 设置邮件消息的主要内容
            forward.setContent(multipart); //Multipart加入到信件
            
			forward.saveChanges();

			Transport t = session.getTransport("smtp");
			try {
				// connect to the smpt server using transport instance
				t.connect(emailServerInfo.getUserName(), emailServerInfo.getPassword());
				t.sendMessage(forward, forward.getAllRecipients());
			} finally {
				t.close();
			}

			// close the store and folder objects
			folder.close(false);
			store.close();
			
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 回复第 msgnum 份邮件
	 * @param emailServerInfo
	 * @param msgnum
	 * @param replyEmail
	 * @return
	 */
	public boolean replyEmail(EmailServerInfo emailServerInfo, int msgnum, String content, String[] attachmentFiles) {
		
		Properties properties = new Properties();
		properties.put("mail.store.protocol", "pop3");
		properties.put("mail.pop3s.host", emailServerInfo.getMailServerPOP3Host());
		properties.put("mail.pop3s.port", EmailServerHostAndPort.POP3_TLS_PORT);
		properties.put("mail.pop3.starttls.enable", "true");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", emailServerInfo.getMailServerSMTPHost());
		properties.put("mail.smtp.port", EmailServerHostAndPort.SMTP_PORT);
		Session session = Session.getDefaultInstance(properties);
		
		Store store = null;
		Folder folder = null;
		try {
			// Get a Store object and connect to the current host
			store = session.getStore("pop3s");
			store.connect(emailServerInfo.getMailServerPOP3Host(),
					emailServerInfo.getMyEmailAddress(),
					emailServerInfo.getPassword());// change the user and
													// password accordingly

			folder = store.getFolder("INBOX");
			folder.open(Folder.READ_ONLY);

			Message message = folder.getMessage(msgnum);
			String to = InternetAddress.toString(message.getRecipients(Message.RecipientType.TO));
			
			Message replyMessage = new MimeMessage(session);
			replyMessage = (MimeMessage) message.reply(false);
			replyMessage.setFrom(new InternetAddress(to));
			
			// 设置回复的邮件地址
			replyMessage.setReplyTo(message.getReplyTo());
			
			// Multipart is a container that holds multiple body parts.
			Multipart bodyPartContainer = new MimeMultipart();  
            BodyPart bodyPart = new MimeBodyPart(); 
            // 设置回复的正文
            bodyPart.setContent(content, "text/html; charset=UTF-8");  
            bodyPartContainer.addBodyPart(bodyPart);    
            
            if(attachmentFiles != null && attachmentFiles.length > 0){ // 存在附件  
                for(String fileName : attachmentFiles) { // 遍历所有的附件
                	bodyPart=new MimeBodyPart();  
                    FileDataSource fds=new FileDataSource(fileName); //得到数据源  
                    bodyPart.setDataHandler(new DataHandler(fds)); //得到附件本身并至入BodyPart  
                    bodyPart.setFileName(fds.getName());  //得到文件名同样至入BodyPart
                    bodyPartContainer.addBodyPart(bodyPart);  
                }    
            }
            // 设置邮件消息的主要内容
            replyMessage.setContent(bodyPartContainer); //Multipart加入到信件
            
			// Send the message by authenticating the SMTP server
			// Create a Transport instance and call the sendMessage
			Transport t = session.getTransport("smtp");
			try {
				// connect to the smpt server using transport instance
				t.connect(emailServerInfo.getMyEmailAddress(),
						emailServerInfo.getPassword());
				t.sendMessage(replyMessage, replyMessage.getAllRecipients());
				return true;
			} finally {
				t.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			// close the store and folder objects
			if (folder != null) {
				try {
					folder.close(false);
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			}
			if (store != null) {
				try {
					store.close();
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	/**
	 * 按照收件箱邮件的顺序删除第 msgnum 号邮件
	 * 
	 * @param deleteMailSession
	 * @param emailServerInfo
	 * @param msgnum
	 * @return
	 */
	public boolean deleteEmailByMsgNum(EmailServerInfo emailServerInfo, int msgnum) {
		try {
	        // get the session object
	        Properties properties = new Properties();
	        properties.put("mail.store.protocol", "pop3");
	        properties.put("mail.pop3s.host", emailServerInfo.getMailServerPOP3Host());
	        properties.put("mail.pop3s.port", "995");
	        properties.put("mail.pop3.starttls.enable", "true");
	        Session deleteMailSession = Session.getDefaultInstance(properties);
	        
			// create the POP3 store object and connect with the pop server
			Store store = deleteMailSession.getStore("pop3s");

			store.connect(emailServerInfo.getMailServerPOP3Host(),
					emailServerInfo.getUserName(), emailServerInfo.getPassword());

			// create the folder object and open it
			Folder emailFolder = store.getFolder("INBOX");
			emailFolder.open(Folder.READ_WRITE);

			// retrieve the messages from the folder in an array and print it
			Message message = emailFolder.getMessage(msgnum);

			// set the DELETE flag to true
			message.setFlag(Flags.Flag.DELETED, true);
			// expunges the folder to remove messages which are marked deleted
			/*
			 * 注意此处需要删除本地数据库的邮件和实际邮箱的邮件，需要添加事务！ 
			 */
			emailFolder.close(true);
			store.close();
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
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
	
	/**
	 * 读取所有邮件
	 * @param sendMailSession
	 * @param emailServerInfo
	 * @return
	 */
	public List<ReadEmailInfo> readAllEmailInfos(Session sendMailSession, EmailServerInfo emailServerInfo) {
		
		List<ReadEmailInfo> allEmailInfos = null;
		Store store = null;
        try {
            store = sendMailSession.getStore("pop3");
            store.connect(emailServerInfo.getUserName(), emailServerInfo.getPassword());
 
            FetchingEmailUtil fetchingEmailUtil = new FetchingEmailUtil();
            
            allEmailInfos = fetchingEmailUtil.fetchingAllEmailInfos(store, true);
            
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
	        p.put("mail.pop3.host", emailServerInfo.getMailServerPOP3Host());
	        p.put("mail.pop3.port", EmailServerHostAndPort.POP3_PORT);
	        p.put("mail.pop3.auth", emailServerInfo.isValidate() ? "true" : "false");
	        p.put("mail.pop3s.starttls.enable", "true");
		} else {
			p.put("mail.smtp.host", emailServerInfo.getMailServerSMTPHost());
			p.put("mail.smtp.port", EmailServerHostAndPort.SMTP_PORT);
			p.put("mail.smtp.auth", emailServerInfo.isValidate() ? "true" : "false");
			p.put("mail.smtp.starttls.enable", "true");
		}
		return p;
	}

}
