package com.magnustiberius.andrea.yacc.lexer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.magnustiberius.andrea.yacc.storage.Inventory;


@Service
public class Tokenizer {

	@Autowired
	Inventory inventory;
	
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
	
	public String add(String ID, String fileName) throws IOException {
	    Class clazz = Tokenizer.class;
	    InputStream inputStream = clazz.getResourceAsStream(fileName);
	    String data = readFromInputStream(inputStream);	
	    inventory.add(ID, data);
	    return ID;
	}
	
}
