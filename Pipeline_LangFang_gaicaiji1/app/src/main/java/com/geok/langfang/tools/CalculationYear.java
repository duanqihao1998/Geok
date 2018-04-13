package com.geok.langfang.tools;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalculationYear {

	Calendar c;

	public CalculationYear() {
		c = Calendar.getInstance();
	}

	/*
	 * 获得年份列表，列表中包含当前年份以及以前的五年
	 */

	public List<String> getYear() {
		List<String> list = new ArrayList<String>();
		list.add(c.get(Calendar.YEAR) + "");
		list.add(c.get(Calendar.YEAR) - 1 + "");
		list.add(c.get(Calendar.YEAR) - 2 + "");
		list.add(c.get(Calendar.YEAR) - 3 + "");
		list.add(c.get(Calendar.YEAR) - 4 + "");
		list.add(c.get(Calendar.YEAR) - 5 + "");
		return list;
	}

	public List<String> getYear_search() {
		List<String> list = new ArrayList<String>();
		int i = 0;
		while ((c.get(Calendar.YEAR) - i) > 1999) {
			list.add(c.get(Calendar.YEAR) - i + "");
			i++;
		}
		return list;
	}
}
