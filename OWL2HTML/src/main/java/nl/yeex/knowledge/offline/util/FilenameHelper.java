package nl.yeex.knowledge.offline.util;

public class FilenameHelper {
	private static final String ILLEGAL_FILENAME_CHARACTER_REPLACEMENT = "x";
	
	
	/**
	 * Private constructor for Utility class.
	 */
	public FilenameHelper() {
		super();
	}
	
	/**
	 * Use the label to get a valid filename for this entity.
	 * It will replace the illegal filename characters with a valid character replacement.
	 * For performance reasons, the result will be stored in a variable so the replacement is done only once.
	 *  
	 * @return
	 */
	public static String getFilename(String label) {
		//  remove the illegal filename characters \ / : * ? " < > |  from the label.
		return  removeIllegalFilenameCharacters(label);
	}
	


	private static String[] REPLACEMENT = new String[Character.MAX_VALUE];
	static {
	    for(int i = Character.MIN_VALUE; i < Character.MAX_VALUE; i++)  {
	        REPLACEMENT[i] = Character.toString(Character.toLowerCase((char) i));
	    }
	    REPLACEMENT['?'] =  ILLEGAL_FILENAME_CHARACTER_REPLACEMENT;
	    REPLACEMENT['/'] =  ILLEGAL_FILENAME_CHARACTER_REPLACEMENT;
	    REPLACEMENT['\\'] = ILLEGAL_FILENAME_CHARACTER_REPLACEMENT;
	    REPLACEMENT[':'] = ILLEGAL_FILENAME_CHARACTER_REPLACEMENT;
	    REPLACEMENT['*'] = ILLEGAL_FILENAME_CHARACTER_REPLACEMENT;
	    REPLACEMENT['"'] = ILLEGAL_FILENAME_CHARACTER_REPLACEMENT;
	    REPLACEMENT['>'] = ILLEGAL_FILENAME_CHARACTER_REPLACEMENT;
	    REPLACEMENT['<'] = ILLEGAL_FILENAME_CHARACTER_REPLACEMENT;
	    REPLACEMENT['|'] = ILLEGAL_FILENAME_CHARACTER_REPLACEMENT;	
	    REPLACEMENT[' '] = "_";	
	}

	/**
	 * One of the fastest algorithms to replace illegal characters.
	 * 
	 * @param word
	 * @return the word with the illegal character replacements.
	 */
	public static String removeIllegalFilenameCharacters(String word) {
	    StringBuilder result = new StringBuilder(word.length());
	    for(int i=0; i < word.length() ;i++)  {
	        result.append(REPLACEMENT[word.charAt(i)]);
	    }
	    return result.toString();
	}
}
