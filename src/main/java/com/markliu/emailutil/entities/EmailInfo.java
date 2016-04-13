package com.markliu.emailutil.entities;

import java.util.ArrayList;
import java.util.Arrays;
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
	 * 待上传附件的路径及名称、或下载附件的地址及名称
	 */
	private List<String> attachmentFiles = new ArrayList<String>();

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

	public List<String> getAttachmentFiles() {
		return attachmentFiles;
	}

	public EmailInfo setAttachmentFiles(List<String> attachmentFiles) {
		this.attachmentFiles = attachmentFiles;
		return this;
	}

	@Override
	public String toString() {
		return "{\n[EmailInfo \n\ttoAddress=" + Arrays.toString(toAddress)
				+ ", \n\tsubject=" + subject + ", \n\tcontent=" + content
				+ ", \n\tattachmentFiles=" + attachmentFiles + "\n]";
	}

}
