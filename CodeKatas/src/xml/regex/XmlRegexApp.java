package xml.regex;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XmlRegexApp {

	public static void main (String[] args) throws java.lang.Exception {
		
		String m = "";
		try {
			String filePath = "cds_2_result.xml";
			m = new String(
				 Files.readAllBytes(Paths.get(filePath)),
				 Charset.forName("UTF-16LE") //little endian UCS-2
			);
	    } catch (Exception e) {
			e.printStackTrace();
		}
		//CHECK correct Encoding representation: 
		//System.out.println(m+"\n\n");
		new XmlRegexApp() .find(m, "Attributes");
		new XmlRegexApp() .findCaseSensitive(m, "ATTRIBUTES");
	}
	
	public String[] find(String from, String tagName) {
		Pattern p = Pattern.compile("<"+tagName+"([\\s\\S]*?)</"+tagName+">");
		Matcher mr = p.matcher(from);
		List<String> matches = new ArrayList<String>();
		while(mr.find()) {
			System.out.println("Found a " + mr.group() + "\n____end-found");
			matches.add(mr.group());
		}
		return (String[]) matches.toArray(new String[0]);
	}
	
	public String[] findCaseSensitive(String from, String tagName) {
		from = from.toUpperCase();
		tagName = tagName.toUpperCase();
		Pattern p = Pattern.compile("<"+tagName+"([\\s\\S]*?)</"+tagName+">");
		Matcher mr = p.matcher(from);
		List<String> matches = new ArrayList<String>();
		while(mr.find()) {
			System.out.println("Found a " + mr.group() + "\n____end-found");
			matches.add(mr.group());
		}
		return (String[]) matches.toArray(new String[0]);
	}

}