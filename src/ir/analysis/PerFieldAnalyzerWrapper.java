package ir.analysis;


import java.io.Reader;
import java.util.Map;
import java.util.HashMap;

/**
 * This analyzer is used to facilitate scenarios where different
 * fields require different analysis techniques.  Use {@link #addAnalyzer}
 * to add a non-default analyzer on a field name basis.
 * See TestPerFieldAnalyzerWrapper.java for example usage.
 */
public class PerFieldAnalyzerWrapper extends Analyzer {
  private Analyzer defaultAnalyzer;
  private Map analyzerMap = new HashMap();


  /**
   * Constructs with default analyzer.
   *
   * @param defaultAnalyzer Any fields not specifically
   * defined to use a different analyzer will use the one provided here.
   */
  public PerFieldAnalyzerWrapper(Analyzer defaultAnalyzer) {
    this.defaultAnalyzer = defaultAnalyzer;
  }

  /**
   * Defines an analyzer to use for the specified field.
   *
   * @param fieldName field name requiring a non-default analyzer.
   * @param analyzer non-default analyzer to use for field
   */
  public void addAnalyzer(String fieldName, Analyzer analyzer) {
    analyzerMap.put(fieldName, analyzer);
  }

  public TokenStream tokenStream(String fieldName, Reader reader) {
    Analyzer analyzer = (Analyzer) analyzerMap.get(fieldName);
    if (analyzer == null) {
      analyzer = defaultAnalyzer;
    }

    return analyzer.tokenStream(fieldName, reader);
  }
}
