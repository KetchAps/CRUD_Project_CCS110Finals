package normie.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class EmailValidator {
	
	/*
	 * Code's Source:
	 * https://stackoverflow.com/questions/15269507/how-to-validate-a-jtextfield-of-email-id-with-a-regex-in-swing-code
	 * 
	 */
	
	private final String regEx = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	
	private Matcher matcher;
	private Pattern pattern;
	
	public EmailValidator() {
		pattern = Pattern.compile(regEx);
		
	}
	
	public boolean validate(String q) {
		matcher = pattern.matcher(q);
		return matcher.matches();
	}
	
}
