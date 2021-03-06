package ir.search;


import ir.index.IndexReader;
import ir.index.Term;
import java.io.IOException;

/** Implements the wildcard search query. Supported wildcards are <code>*</code>, which
 * matches any character sequence (including the empty one), and <code>?</code>,
 * which matches any single character. Note this query can be slow, as it
 * needs to iterate over all terms. In order to prevent extremely slow WildcardQueries,
 * a Wildcard term must not start with one of the wildcards <code>*</code> or
 * <code>?</code>.
 * 
 * @see WildcardTermEnum
 */
public class WildcardQuery extends MultiTermQuery {
  public WildcardQuery(Term term) {
    super(term);
  }

  protected FilteredTermEnum getEnum(IndexReader reader) throws IOException {
    return new WildcardTermEnum(reader, getTerm());
  }
    
}
