package com.markliu.emailutil;

import org.junit.Test;

import com.markliu.emailutil.entities.EmailServerInfo;
import com.markliu.emailutil.util.EmailTemplateUtil;

/**
 * 
 * 
 * @auther SunnyMarkLiu
 * @time Apr 14, 2016 10:34:01 AM
 */
public class DeleteEmailTest {

	@Test
	public void testDeleteEmail() {
		
		// 获取配置的登陆邮件服务器的信息
		EmailServerInfo emailServerInfo = EmailTemplateUtil.getConfigEmailServerInfo();
		
		if (EmailTemplateUtil.deleteEmailByMsgNum(emailServerInfo, 59)) {
			System.out.println("删除成功！");
		} else {
			System.out.println("删除失败！");
		}
	}
}
