package com.magnustiberius.andrea.yacc.dao;

import java.nio.charset.StandardCharsets;

public class Token {
	String type;
	String value;
	int begin;
	int end;
	byte[] byteValue;
	String strValue;
	int line;
	
	public void fill(byte[] input) {
		int sz = end - begin;
		byteValue = new byte[(int) sz];
		int j = 0;
		for(int i=begin; i<end; i++) {
			byteValue[j] = input[i];
			j++;
		}
		strValue = new String(byteValue, StandardCharsets.UTF_8);
		System.out.println("Token [" + begin + ", " + end + " ]  [line:" + line + "] [" + strValue + "]");
	}
	
	public byte[] getByteValue() {
		return byteValue;
	}

	public void setByteValue(byte[] byteValue) {
		this.byteValue = byteValue;
	}

	public String getStrValue() {
		return strValue;
	}

	public void setStrValue(String strValue) {
		this.strValue = strValue;
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line-1;
	}

	public int getBegin() {
		return begin;
	}
	public void setBegin(int begin) {
		this.begin = begin;
	}
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
