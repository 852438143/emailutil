package com.markliu.emailutil.emailsender.entities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 发送的邮件
 * 
 * @auther SunnyMarkLiu
 * @time Apr 12, 2016 4:08:05 PM
 */
public class EmailInfo {

	/**
	 * 接收方的邮箱地址
	 */
	private String[] toAddress;

	/**
	 * 邮件主题
	 */
	private String subject;

	/**
	 * 邮件内容
	 */
	private String content;

	/**
	 * 附件
	 */
	private List<File> attachmentFiles = new ArrayList<File>();

	public EmailInfo() {
	}

	public String[] getToAddress() {
		return toAddress;
	}

	public EmailInfo setToAddress(String[] toAddress) {
		this.toAddress = toAddress;
		return this;
	}

	public String getSubject() {
		return subject;
	}

	public EmailInfo setSubject(String subject) {
		this.subject = subject;
		return this;
	}

	public String getContent() {
		return content;
	}

	public EmailInfo setContent(String content) {
		this.content = content;
		return this;
	}

	public List<File> getAttachmentFiles() {
		return attachmentFiles;
	}

	public EmailInfo setAttachmentFiles(List<File> attachmentFiles) {
		this.attachmentFiles = attachmentFiles;
		return this;
	}

}
