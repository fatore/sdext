package br.usp.sdvt.util.parsers;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BasicParser {
	
	public static Date parseDate(String str) {
		
		if (str.equals("")) {
			return null;
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
				}
			}
		}
		return null;
	}
	
	public static Long parseLong(String str) {
		str = str.replace(" ", "");
		if (str.equals("#NE#") || str.equals("#NI#") || str.equals("") || str.equals("-1") || str.equals("0")) {
			return null;
		}
		return Long.parseLong(str);
	}
	
	public static Integer parseInt(String str) {
		str = str.replace(" ", "");
		if (str.equals("#NE#") || str.equals("#NI#") || str.equals("") || str.equals("-1") || str.equals("0")) {
			return null;
		}
		return Integer.parseInt(str);
	}
	
	public static Float parseFloat(String str) {
		str = str.replace(" ", "");
		if (str.equals("#NE#") || str.equals("#NI#") || str.equals("") || str.equals("-1") || str.equals("0")) {
			return null;
		}
		return Float.parseFloat(str);
	}
	
	public static String parseStr(String str) {
		if (str.equals("#NE#") || str.equals("#NI#")) {
			return null;
		}
		return str.toUpperCase();
	}
}
