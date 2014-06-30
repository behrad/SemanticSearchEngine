package ir.search.spans;


import java.io.IOException;

import java.util.Collection;

import ir.index.IndexReader;
import ir.search.Query;
import ir.search.Weight;
import ir.search.Searcher;

/** Base class for span-based queries. */
public abstract class SpanQuery extends Query {
  /** Expert: Returns the matches for this query in an index.  Used internally
   * to search for spans. */
  public abstract Spans getSpans(IndexReader reader) throws IOException;

  /** Returns the name of the field matched by this query.*/
  public abstract String getField();

  /** Returns a collection of all terms matched by this query.*/
  public abstract Collection getTerms();

  protected Weight createWeight(Searcher searcher) {
    return new SpanWeight(this, searcher);
  }

}

