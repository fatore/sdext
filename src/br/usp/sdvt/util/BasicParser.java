package br.usp.sdvt.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BasicParser {
	
	public static Date parseDate(String str) {
		
		if (str.equals("")) {
//			System.err.print("EMPTY DATE FIELD.\t");
		}
		try {
			return new SimpleDateFormat("dd/MM/yy").parse(str);
		} catch (Exception e) {
			try {
				return new SimpleDateFormat("ddMMyyyy").parse(str);
			} catch (Exception e2) {
				try {
					return new SimpleDateFormat("dd-MMM-yy").parse(str);
				} catch (Exception e3){
//					System.err.println("EMPTY DATE FIELD. SETTING AS NULL");
				}
			}
		}
//		System.err.println("INVALID DATE: [" + str + "]");
		return null;
	}
	
	public static Long parseLong(String str) {
		str = str.replace(" ", "");
		if (str.equals("#NE#") || str.equals("#NI#") || str.equals("")) {
//			System.err.println("EMPTY NUMERIC FIELD. SETTING AS -1");
			return new Long(-1);
		}
		return Long.parseLong(str);
	}
	
	public static String parseStr(String str) {
		if (str.equals("#NE#") || str.equals("#NI#")) {
//			System.err.println("EMPTY TEXT FIELD. SETTING AS NULL");
			return null;
		}
		return str;
	}
}
