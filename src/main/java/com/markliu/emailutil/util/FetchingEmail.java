package com.markliu.emailutil.util;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeUtility;

public class FetchingEmail {
	
	private final static String ATTACHMENT_PATH = "E:\\email_attachments\\temp\\";

	public static void fetch(String pop3Host, String user, String password) {
		try {
			// create properties field
			Properties properties = new Properties();
			properties.put("mail.store.protocol", "pop3");
			properties.put("mail.pop3.host", pop3Host);
			properties.put("mail.pop3.port", "995");
			properties.put("mail.pop3.starttls.enable", "true");
			Session emailSession = Session.getDefaultInstance(properties);
			
//			emailSession.setDebug(true);

			// create the POP3 store object and connect with the pop server
			Store store = emailSession.getStore("pop3s");

			store.connect(pop3Host, user, password);

			// create the folder object and open it
			Folder emailFolder = store.getFolder("INBOX");
			emailFolder.open(Folder.READ_ONLY);

			// retrieve the messages from the folder in an array and print it
			Message[] messages = emailFolder.getMessages();
			System.out.println("messages.length---" + messages.length);

			//for (int i = 0; i < messages.length; i++) {
				Message message = messages[messages.length-1];
				System.out.println("---------------------------------");
				new FetchingEmail().writePart(message);
//				String line = reader.readLine();
//				if ("YES".equals(line)) {
//					message.writeTo(System.out);
//				} else if ("QUIT".equals(line)) {
//					break;
//				}
		//	}

			// close the store and folder objects
			emailFolder.close(false);
			store.close();

		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		String host = "pop.163.com";// change accordingly
		String mailStoreType = "pop3";
		String username = "SunnyMarkLiu@163.com";// change accordingly
		String password = "sqmmjlqwd992101";// change accordingly

		// Call method fetch
		fetch(host, username, password);

	}

	/*
	 * This method checks for content-type based on which, it processes and
	 * fetches the content of the message
	 */
	public void writePart(Part p) throws Exception {
		if (p instanceof Message)
			// Call methos writeEnvelope
			writeEnvelope((Message) p);

		System.out.println("----------------------------");
		System.out.println("CONTENT-TYPE: " + p.getContentType());

		// check if the content is plain text
		if (p.isMimeType("text/plain")) {
			System.out.println("This is plain text");
			System.out.println("---------------------------");
			System.out.println((String) p.getContent());
		}
		// check if the content has attachment
		else if (p.isMimeType("multipart/*")) {
			System.out.println("This is a Multipart");
			System.out.println("---------------------------");
			Multipart mp = (Multipart) p.getContent();
			int count = mp.getCount();
			for (int i = 0; i < count; i++)
				writePart(mp.getBodyPart(i));
		}
		// check if the content is a nested message
		else if (p.isMimeType("message/rfc822")) {
			System.out.println("This is a Nested Message");
			System.out.println("---------------------------");
			writePart((Part) p.getContent());
		}
		// check if the content is an inline image
		else if (p.isMimeType("image/jpeg")) {
			System.out.println("--------> image/jpeg");
			Object o = p.getContent();

			InputStream x = (InputStream) o;
			// Construct the required byte array
			System.out.println("x.length = " + x.available());

			int i = 0;
			byte[] bArray = new byte[x.available()];
			while ((i = (int) ((InputStream) x).available()) > 0) {
				int result = (int) (((InputStream) x).read(bArray));
				if (result == -1)
					i = 0;
				break;
			}
			FileOutputStream f2 = new FileOutputStream("E:\\email_attachments\\temp\\image.jpg");
			f2.write(bArray);
			f2.flush();
			f2.close();
		} else if (p.getContentType().contains("image/")) {
			System.out.println("content type" + p.getContentType());
			File f = new File("E:\\email_attachments\\temp\\image" + new Date().getTime() + ".jpg");
			DataOutputStream output = new DataOutputStream(
					new BufferedOutputStream(new FileOutputStream(f)));
			com.sun.mail.util.BASE64DecoderStream test = (com.sun.mail.util.BASE64DecoderStream) p
					.getContent();
			byte[] buffer = new byte[1024];
			int bytesRead;
			while ((bytesRead = test.read(buffer)) != -1) {
				output.write(buffer, 0, bytesRead);
			}
			output.flush();
			output.close();
		} else {
			Object o = p.getContent();
			if (o instanceof String) {
				System.out.println("This is a string");
				System.out.println("---------------------------");
				System.out.println((String) o);
			} else if (o instanceof InputStream) {
				System.out.println("--------------- 附件 InputStream ------------");
				InputStream is = (InputStream) o;
				System.out.println("p.getContentType():\n" + p.getContentType());
				System.out.println("-------------p.getContentType()--------------");
				
				String attachmentFileName = p.getDataHandler().getDataSource().getName();
				attachmentFileName = MimeUtility.decodeText(attachmentFileName);
				System.out.println("附件文件名：" + attachmentFileName);
				
				InputStream fileIn = p.getDataHandler().getDataSource().getInputStream();
				
				// 开启线程保存文件
				new SaveFileThread(fileIn, attachmentFileName).start();
			} else {
				System.out.println("This is an unknown type");
				System.out.println("---------------------------");
				System.out.println(o.toString());
			}
		}

	}

	/*
	 * This method would print FROM,TO and SUBJECT of the message
	 */
	public static void writeEnvelope(Message m) throws Exception {
		System.out.println("This is the message envelope");
		System.out.println("---------------------------");
		Address[] a;

		// FROM
		if ((a = m.getFrom()) != null) {
			for (int j = 0; j < a.length; j++)
				System.out.println("FROM: " + MimeUtility.decodeText(a[j].toString()));
		}

		// TO
		if ((a = m.getRecipients(Message.RecipientType.TO)) != null) {
			for (int j = 0; j < a.length; j++)
				System.out.println("TO: " + MimeUtility.decodeText(a[j].toString()));
		}

		// SUBJECT
		if (m.getSubject() != null)
			System.out.println("SUBJECT: " + m.getSubject());

	}
	/**
	 * 保存附件的线程
	 * @author dell
	 *
	 */
	private class SaveFileThread extends Thread {
		
		private String filename;
		private InputStream fileIn;

		public SaveFileThread(InputStream fileIn, String filename) {
			this.filename = filename;
			this.fileIn = fileIn;
		}
		
		@Override
		public void run() {
			FileOutputStream out = null;
			try {
				out = new FileOutputStream(ATTACHMENT_PATH + filename);
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = fileIn.read(buffer)) != -1) {
					out.write(buffer, 0, len);
				}
				out.flush();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (out != null) {
						out.close();
						out = null;
					}
					if (fileIn != null) {
						fileIn.close();
						fileIn = null;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}