package com.magnustiberius.andrea.yacc.lexer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.magnustiberius.andrea.yacc.dao.Token;
import com.magnustiberius.andrea.yacc.storage.Inventory;


@Service
public class Tokenizer {

	@Autowired
	Inventory inventory;
	
	int curptr;
	String inputData;
	char ch;
	int line;
	int col;
	
	public String readFromInputStream(InputStream inputStream)
	  throws IOException {
	    StringBuilder resultStringBuilder = new StringBuilder();
	    try (BufferedReader br
	      = new BufferedReader(new InputStreamReader(inputStream))) {
	        String line;
	        while ((line = br.readLine()) != null) {
	            resultStringBuilder.append(line).append("\n");
	        }
	    }
	  return resultStringBuilder.toString();
	}	
	
	public void resetCounter() {
	    curptr = 0;
	    line = 0;
	    col = 1;
	}
	
	public String add(String ID, String fileName) throws IOException {
	    Class clazz = Tokenizer.class;
	    InputStream inputStream = clazz.getResourceAsStream(fileName);
	    inputData = readFromInputStream(inputStream);	
	    inventory.add(ID, inputData);
	    resetCounter();
	    return ID;
	}
	
	public Token getNext() {
		ch = inputData.charAt(curptr);
		if (ch == '/') {
			
		}
		return null;
	}
	
}
