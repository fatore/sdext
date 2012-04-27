package br.usp.sdext.parsers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BasicParser {
	
	public static Date parseDate(String str) { // TODO: verificar quando ano eh 0002 por exemplo
		
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
	
	public static String parseStr(String str) {
		Pattern pattern = Pattern.compile("\\s+");
		Matcher matcher = pattern.matcher(str);
		str = matcher.replaceAll(" ");
		
		pattern = Pattern.compile("\\bNÃO\\b");
		matcher = pattern.matcher(str);
		if (matcher.find()) {
			return null;
		}
		if (str.contains("#")) {
			return null;
		}
		if (str.equals("#NE#") || str.equals("#NI#") || str.equals("")) {
			return null;
		}
		return str.trim().toUpperCase();
	}
	
	public static Long parseLong(String str) {
		if ((str = parseStr(str)) == null) return null;
		str = str.replace(" ", "");
		str = str.replace("-", "");
		if (str.equals("")) return null;
		Long no =  Long.parseLong(str);
		return (no <= 0 ) ? null : no;
	}
	
	public static Integer parseInt(String str) {
		if ((str = parseStr(str)) == null) return null;
		str = str.replace(" ", "");
		if (str.equals("")) return null;
		Integer no = Integer.parseInt(str);
		return (no <= 0 ) ? null : no;
	}
	
	public static Float parseFloat(String str) {
		if ((str = parseStr(str)) == null) return null;
		str = str.replace(" ", "");
		str = str.replace(",", ".");
		if (str.equals("")) return null;
		Float no = Float.parseFloat(str); 
		return (no <= 0 ) ? null : no;
	}
	
	public static void main(String[] args){
		String teste = "--";
		System.out.println(teste);
		System.out.println(BasicParser.parseLong(teste));
    }
}
