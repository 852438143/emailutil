# emailutil
登陆163邮箱、读取收件箱的邮件信息、发送邮件. 后期扩展到其他邮件服务器。

# How to use

## 类路径下配置 emailServerConfig.properties 

	# POP 邮件服务器的地址
	mailServer_POP3Host = pop.163.com
	# SMTP 邮件服务器的地址
	mailServer_SMTPHost = smtp.163.com
	# 邮箱地址
	myEmailAddress = xxxxxx@163.com
	# 邮箱用户名
	userName = xxxxxx
	# 注意使用的是开通 SMTP、 POP、IMAP 协议的授权码
	password = xxxxxx
	# 是否需要验证
	validate = true
 
## Fetching Emails
	@Test
	public void testReadEmail() throws FileNotFoundException {

		// 获取配置的登陆邮件服务器的信息
		EmailServerInfo emailServerInfo = EmailTemplateUtil.getConfigEmailServerInfo();
		
		// 读取最近的一份邮件
		ReadEmailInfo emailInfo = EmailTemplateUtil.getLatestOneEmailInfo(emailServerInfo);
		System.out.println(emailInfo.toString());
	}
	
## Sending Emails

	@Test
	public void testSendEmailTemplate() throws FileNotFoundException {

		// 获取配置的登陆邮件服务器的信息
		EmailServerInfo emailServerInfo = EmailTemplateUtil.getConfigEmailServerInfo();
		
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
		
		EmailTemplateUtil.sendEmail(emailServerInfo, email);
	}

## Deleting Emails

	@Test
	public void testDeleteEmail() throws FileNotFoundException {

		// 获取配置的登陆邮件服务器的信息
		EmailServerInfo emailServerInfo = EmailTemplateUtil.getConfigEmailServerInfo();
		
		// 删除第一份邮件
		if (EmailTemplateUtil.deleteEmailByMsgNum(emailServerInfo, 1)) {
			System.out.println("删除成功！");
		} else {
			System.out.println("删除失败！");
		}
	}
	
## Replying Emails

	@Test
	public void testDeleteEmail() throws FileNotFoundException {

		// 获取配置的登陆邮件服务器的信息
		EmailServerInfo emailServerInfo = EmailTemplateUtil.getConfigEmailServerInfo();
		
		String content = "这是回复内容";
		String[] attachmentFiles = {"E:\\Photos\\bob.jpeg"}; // 设置回复的附件
		
		// 回复第一份邮件
		if (EmailTemplateUtil.replyEmail(emailServerInfo, 1, content, attachmentFiles)) {
			System.out.println("回复成功！");
		} else {
			System.out.println("回复失败！");
		}
	}
	
## Forwarding Emails

	@Test
	public void testForwardEmail() throws FileNotFoundException {

		// 获取配置的登陆邮件服务器的信息
		EmailServerInfo emailServerInfo = EmailTemplateUtil.getConfigEmailServerInfo();
		
		String content = "这是转发的附加内容";
		String[] attachmentFiles = {"E:\\Photos\\bob.jpeg"};  // 设置附件
		// 设置转发的地址
		String[] forwardAddress = {"xxxxxx@qq.com", "xxxxxx@qq.com"};
		
		if (EmailTemplateUtil.forwardEmail(emailServerInfo, 61, content, attachmentFiles, forwardAddress)) {
			System.out.println("转发成功！");
		} else {
			System.out.println("转发失败！");
		}
	}
