package ir.search.spans;


import java.io.IOException;

import java.util.Iterator;
import java.util.Collection;

import ir.index.IndexReader;
import ir.index.Term;

import ir.search.Query;
import ir.search.Weight;
import ir.search.Searcher;
import ir.search.Scorer;
import ir.search.Explanation;
import ir.search.Similarity;

class SpanWeight implements Weight {
  private Searcher searcher;
  private float value;
  private float idf;
  private float queryNorm;
  private float queryWeight;

  private Collection terms;
  private SpanQuery query;

  public SpanWeight(SpanQuery query, Searcher searcher) {
    this.searcher = searcher;
    this.query = query;
    this.terms = query.getTerms();
  }

  public Query getQuery() { return query; }
  public float getValue() { return value; }

  public float sumOfSquaredWeights() throws IOException {
    idf = this.query.getSimilarity(searcher).idf(terms, searcher);
    queryWeight = idf * query.getBoost();         // compute query weight
    return queryWeight * queryWeight;             // square it
  }

  public void normalize(float queryNorm) {
    this.queryNorm = queryNorm;
    queryWeight *= queryNorm;                     // normalize query weight
    value = queryWeight * idf;                    // idf for document
  }

  public Scorer scorer(IndexReader reader) throws IOException {
    return new SpanScorer(query.getSpans(reader), this,
                          query.getSimilarity(searcher),
                          reader.norms(query.getField()));
  }

  public Explanation explain(IndexReader reader, int doc)
    throws IOException {

    Explanation result = new Explanation();
    result.setDescription("weight("+getQuery()+" in "+doc+"), product of:");
    String field = ((SpanQuery)getQuery()).getField();

    StringBuffer docFreqs = new StringBuffer();
    Iterator i = terms.iterator();
    while (i.hasNext()) {
      Term term = (Term)i.next();
      docFreqs.append(term.text());
      docFreqs.append("=");
      docFreqs.append(searcher.docFreq(term));

      if (i.hasNext()) {
        docFreqs.append(" ");
      }
    }

    Explanation idfExpl =
      new Explanation(idf, "idf(" + field + ": " + docFreqs + ")");

    // explain query weight
    Explanation queryExpl = new Explanation();
    queryExpl.setDescription("queryWeight(" + getQuery() + "), product of:");

    Explanation boostExpl = new Explanation(getQuery().getBoost(), "boost");
    if (getQuery().getBoost() != 1.0f)
      queryExpl.addDetail(boostExpl);
    queryExpl.addDetail(idfExpl);

    Explanation queryNormExpl = new Explanation(queryNorm,"queryNorm");
    queryExpl.addDetail(queryNormExpl);

    queryExpl.setValue(boostExpl.getValue() *
                       idfExpl.getValue() *
                       queryNormExpl.getValue());

    result.addDetail(queryExpl);

    // explain field weight
    Explanation fieldExpl = new Explanation();
    fieldExpl.setDescription("fieldWeight("+field+":"+query.toString(field)+
                             " in "+doc+"), product of:");

    Explanation tfExpl = scorer(reader).explain(doc);
    fieldExpl.addDetail(tfExpl);
    fieldExpl.addDetail(idfExpl);

    Explanation fieldNormExpl = new Explanation();
    byte[] fieldNorms = reader.norms(field);
    float fieldNorm =
      fieldNorms!=null ? Similarity.decodeNorm(fieldNorms[doc]) : 0.0f;
    fieldNormExpl.setValue(fieldNorm);
    fieldNormExpl.setDescription("fieldNorm(field="+field+", doc="+doc+")");
    fieldExpl.addDetail(fieldNormExpl);

    fieldExpl.setValue(tfExpl.getValue() *
                       idfExpl.getValue() *
                       fieldNormExpl.getValue());

    result.addDetail(fieldExpl);

    // combine them
    result.setValue(queryExpl.getValue() * fieldExpl.getValue());

    if (queryExpl.getValue() == 1.0f)
      return fieldExpl;

    return result;
  }
}
