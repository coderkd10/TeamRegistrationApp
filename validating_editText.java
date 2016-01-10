import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by arnavkansal on 10/01/16.
 */
public class validating_editText {
  // validates entries
  // type denotes is it the name, or the entry number being evaluated
  private boolean Validate(String testString, int type){
    final String Name_type = "^[\\p{L} .'-]+$";
    final String Entry_No="(\\d{4})((?:[a-zA-Z][a-zA-Z]+))(\\d{5})";
    if (type == 1) return isValid(testString, Name_type);
    return isValid(testString, Entry_No);
  }
  
  private boolean isValid(String test, String regex_pattern){
    Pattern regx = Pattern.compile(regex_pattern);
    Matcher match = regx.matcher(test);
    return match.matches();
  }
}
