/**
Program: ir.crawlAnalyser.java
Date: 01/20/2001
Desc: A filter that stems and removes stopwords
Written by: Ullas Nambiar(mallu@asu.edu)
**/

package ir.crawl;

/** Implementation of a custom analyzer to stem the terms*/
import java.io.*;

import ir.analysis.Analyzer;
import ir.analysis.TokenStream;
import ir.analysis.PorterStemFilter;
import ir.analysis.StopAnalyzer;


public class MyAnalyser extends Analyzer {

    /** Method to Stem the tokens and remove stop words.*/
    public final TokenStream tokenStream(Reader reader)
    {
	return new PorterStemFilter(new StopAnalyzer().tokenStream(reader));
    }
}







