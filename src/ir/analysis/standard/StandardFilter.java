package ir.analysis.standard;


import ir.analysis.*;

/** Normalizes tokens extracted with {@link StandardTokenizer}. */

public final class StandardFilter extends TokenFilter
  implements StandardTokenizerConstants  {


  /** Construct filtering <i>in</i>. */
  public StandardFilter(TokenStream in) {
    super(in);
  }

  private static final String APOSTROPHE_TYPE = tokenImage[APOSTROPHE];
  private static final String ACRONYM_TYPE = tokenImage[ACRONYM];
  
  /** Returns the next token in the stream, or null at EOS.
   * <p>Removes <tt>'s</tt> from the end of words.
   * <p>Removes dots from acronyms.
   */
  public final ir.analysis.Token next() throws java.io.IOException {
    ir.analysis.Token t = input.next();

    if (t == null)
      return null;

    String text = t.termText();
    String type = t.type();

    if (type == APOSTROPHE_TYPE &&		  // remove 's
	(text.endsWith("'s") || text.endsWith("'S"))) {
      return new ir.analysis.Token
	(text.substring(0,text.length()-2),
	 t.startOffset(), t.endOffset(), type);

    } else if (type == ACRONYM_TYPE) {		  // remove dots
      StringBuffer trimmed = new StringBuffer();
      for (int i = 0; i < text.length(); i++) {
	char c = text.charAt(i);
	if (c != '.')
	  trimmed.append(c);
      }
      return new ir.analysis.Token
	(trimmed.toString(), t.startOffset(), t.endOffset(), type);

    } else {
      return t;
    }
  }
}
