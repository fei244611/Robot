package com.example.feifei.robot.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatMessage
{

	/**
	 * 消息类型
	 */
	private String type ;
	/**
	 * 消息内容
	 */
	private String msg;
	/**
	 * 日期的字符串格式
	 */
	private String dateStr;


	public ChatMessage()
	{
	}

	public ChatMessage(String type, String msg)
	{
		super();
		this.type = type;
		this.msg = msg;
        SimpleDateFormat sim=new SimpleDateFormat("MM/dd HH:mm");
        Date date=new Date();
        String str=sim.format(date);
		setDate(str);
	}

	public String getDateStr()
	{
		return dateStr;
	}


	public void setDate(String date)
	{
		this.dateStr = date;

	}
	

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getMsg()
	{
		return msg;
	}

	public void setMsg(String msg)
	{
		this.msg = msg;
	}

}
