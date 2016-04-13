# emailutil
登陆163邮箱、读取收件箱的邮件信息、发送邮件. 后期扩展到其他邮件服务器。

# How to use

## Fetching Email
	@Test
	public void testReadEmail() throws FileNotFoundException {
		// 设置登陆邮件服务器的信息
		EmailServerInfo emailServerInfo = new EmailServerInfo();
		emailServerInfo.setMailServerHost(EmailServerHostAndPort.NetEase163_POP3_SERVER);
		emailServerInfo.setMailServerPort(EmailServerHostAndPort.POP3_PORT);
		emailServerInfo.setValidate(true);
		emailServerInfo.setUserName("xxxxxx@163.com"); // change accordingly
		emailServerInfo.setPassword("xxxxxx"); // 注意使用的是开通 SMTP 协议的授权码
		emailServerInfo.setMyEmailAddress("xxxxxx@163.com");		
		
		// 读取最近的一份邮件
		ReadEmailInfo emailInfo = SendEmailTemplate.getLatestOneEmailInfo(emailServerInfo);
		System.out.println(emailInfo.toString());
	}
	
## Sending Email

	@Test
	public void testSendEmailTemplate() throws FileNotFoundException {
		EmailServerInfo emailServerInfo = new EmailServerInfo();
		emailServerInfo.setMailServerHost(EmailServerHostAndPort.NetEase163_SMTP_SERVER);
		emailServerInfo.setMailServerPort(EmailServerHostAndPort.SMTP_PORT);
		emailServerInfo.setValidate(true);
		emailServerInfo.setUserName("xxxxxx@163.com"); // change accordingly
		emailServerInfo.setPassword("xxxxxx"); // 注意使用的是开通 SMTP 协议的授权码
		emailServerInfo.setMyEmailAddress("xxxxxx@163.com");		
		
		// 构建邮件 email
		EmailInfo email = new EmailInfo();
		String[] toes = {"xxxxxx@qq.com", "xxxxxx@qq.com"}; // change accordingly
		email.setToAddress(toes).setSubject("test 主题2");
		
		StringBuffer content = new StringBuffer();
		
		// 发送链接有的邮箱服务器可能将此邮件识别为垃圾邮件。
		content.append("<h2><font color=red>Header标题</font></h2><br/>")
        .append("<span class=\"test\">this is content!内容</span>")  
		email.setContent(content.toString());
		
		// 设置上传的附件
		email.getAttachmentFiles().add("E:\\Photos\\bob.jpeg");
		email.getAttachmentFiles().add("E:\\heartocat.png");
		email.getAttachmentFiles().add("E:\\Music\\Alison Krauss - When You Say Nothing At All.mp3");
		
		SendEmailTemplate.sendEmail(emailServerInfo, email);
	}

更新...