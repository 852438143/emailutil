package com.markliu.emailutil.entities;

import java.util.Date;

/**
 * 读取邮件的POJO
 * 
 * @auther SunnyMarkLiu
 * @time Apr 12, 2016 10:20:32 PM
 */
public class ReadEmailInfo extends EmailInfo {

	private String fromAddress;
	
	/**
	 * 发送的时间
	 */
	private Date sentDate;

	/**
	 * 是否需要邮件回执
	 */
	private boolean needReply;

	/**
	 * 是否已读
	 */
	private boolean isReaded;
	
	/**
	 * 是否包含附件
	 */
	private boolean containsAttachments;
	
	/**
	 * 抄送
	 */
	private String carbonCopy;
	
	private String darkCopy;
	
	public String getDarkCopy() {
		return darkCopy;
	}

	public void setDarkCopy(String darkCopy) {
		this.darkCopy = darkCopy;
	}

	public String getCarbonCopy() {
		return carbonCopy;
	}

	public void setCarbonCopy(String carbonCopy) {
		this.carbonCopy = carbonCopy;
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public Date getSentDate() {
		return sentDate;
	}

	public void setSentDate(Date sentDate) {
		this.sentDate = sentDate;
	}

	public boolean isNeedReply() {
		return needReply;
	}

	public void setNeedReply(boolean needReply) {
		this.needReply = needReply;
	}

	public boolean isReaded() {
		return isReaded;
	}

	public void setReaded(boolean isReaded) {
		this.isReaded = isReaded;
	}

	public boolean isContainsAttachments() {
		return containsAttachments;
	}

	public void setContainsAttachments(boolean containsAttachments) {
		this.containsAttachments = containsAttachments;
	}

}
