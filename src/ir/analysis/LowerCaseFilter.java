package ir.analysis;


import java.io.IOException;

/**
 * Normalizes token text to lower case.
 *
 */
public final class LowerCaseFilter extends TokenFilter {
  public LowerCaseFilter(TokenStream in) {
    super(in);
  }

  public final Token next() throws IOException {
    Token t = input.next();

    if (t == null)
      return null;

    t.termText = t.termText.toLowerCase();

    return t;
  }
}
