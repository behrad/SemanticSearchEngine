/**
Program: VectorViewer.java
Description: View the underlying Document Vector for the Index
Date: 02/03/2001
Developer: Ullas Nambiar

 **/

package ir.crawl;
import ir.index.*;
import java.io.*;
/** Demonstrates how to access the underlying Data Structure to retrieve Term and Document Frequencies*/
public class VectorViewer {

    int count=0;
    //display the vector
	/**
		   For retrieving the (docNo,Freq) pair for each term call

		    TermDocs termdocs = reader.termDocs(termval);

		   For retrieving the (docNo,Freq,(pos1,......posn)) call
		    TermPositions termpositions = termval.termPositions(termval)

		 */
    public void  showVector()
    {
	// lists the vector
	try{
	IndexReader reader = IndexReader.open("result3index");
	System.out.println(" Number of Docs in Index :" + reader.numDocs());
	TermEnum termenum = reader.terms();
	System.out.println("Printing the Terms and the Frequency \n");
	while(termenum.next())
	    {
		count++;
		Term termval = termenum.term();
		System.out.println("The Term :" + termval.text() + " Frequency :"+termenum.docFreq());
		/**
		   Add following here to
		   retrieve the <docNo,Freq> pair for each term call
		    TermDocs termdocs = reader.termDocs(termval);

		  to retrieve the <docNo,Freq,<pos1,......posn>> call
		    TermPositions termpositions = termval.termPositions(termval)

		**/

	    }
	System.out.println(" Total terms : " + count);

		}catch(IOException e){
	    System.out.println("IO Error has occured: "+ e);
	    return;
	}
    }


    public static void main(String[] args)
    {
	VectorViewer vectorViewer = new VectorViewer();
	vectorViewer.showVector();
	 System.out.println(" Total terms : " + vectorViewer.count);

    }

}


