package com.magnustiberius.andrea.yacc.storage;

import java.util.HashMap;

import org.springframework.stereotype.Service;

@Service
public class Inventory {

	HashMap<String, String> inv = new HashMap<>();
	
	public Boolean add(String key, String value ) {
		inv.put(key, value);
		return true;
	}
	
	public String get(String key) {
		if (inv.containsKey(key)) {
			String rv = inv.get(key);
			return rv;
		}
		return "";
	}
	
}
