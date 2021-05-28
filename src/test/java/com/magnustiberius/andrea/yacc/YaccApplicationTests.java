package com.magnustiberius.andrea.yacc;

import static org.hamcrest.CoreMatchers.containsString;

import java.io.IOException;
import java.io.InputStream;

import org.assertj.core.api.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.magnustiberius.andrea.yacc.dao.Token;
import com.magnustiberius.andrea.yacc.lexer.Tokenizer;

@SpringBootTest
class YaccApplicationTests {

	@Autowired
	Tokenizer tokenizer;
	
//	@Test
//	void contextLoads() {
//	}

	
	
	//@Test
	public void givenFileNameAsAbsolutePath_whenUsingClasspath_thenFileData() throws IOException {
		//tokenizer = new Tokenizer();
	    String expectedData = "Hello, world!";
	    
	    Class clazz = YaccApplicationTests.class;
	    InputStream inputStream = clazz.getResourceAsStream("/test.andreayacc");
	    String data = tokenizer.readFromInputStream(inputStream);

	    //Assert.assertThat(data, containsString(expectedData));
	}	

	@Test
	public void test1() throws IOException {
	    String ID = tokenizer.add("plsql","/test.andreayacc");
	    int i = 0;
	    Token t = tokenizer.getNext();
	    while (t != null && i < 100000) {
	    	t = tokenizer.getNext();
	    	i++;
	    }
	    System.out.println("ID>>" + ID);
	    //Assert.assertThat(data, containsString(expectedData));
	}	

	@Test
	public void test_plsql() throws IOException {
	    String ID = tokenizer.add("plsql","/tsql.andreayacc");
	    int i = 0;
	    Token t = tokenizer.getNext();
	    while (t != null && i < 100000) {
	    	t = tokenizer.getNext();
	    	i++;
	    }
	    System.out.println("ID>>" + ID);
	    //Assert.assertThat(data, containsString(expectedData));
	}	

	@Test
	public void test_tsqllexer() throws IOException {
	    String ID = tokenizer.add("plsql","/tsqllexer.andreayacc");
	    int i = 0;
	    Token t = tokenizer.getNext();
	    while (t != null && i < 100000) {
	    	t = tokenizer.getNext();
	    	i++;
	    }
	    System.out.println("ID>>" + ID);
	    //Assert.assertThat(data, containsString(expectedData));
	}	
	
	
}
