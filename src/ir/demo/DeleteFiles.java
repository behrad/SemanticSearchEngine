package ir.demo;


import java.io.IOException;

import ir.store.Directory;
import ir.store.FSDirectory;
import ir.index.IndexReader;
import ir.index.Term;

class DeleteFiles {
  public static void main(String[] args) {
    try {
      Directory directory = FSDirectory.getDirectory("demo index", false);
      IndexReader reader = IndexReader.open(directory);

//       Term term = new Term("path", "pizza");
//       int deleted = reader.delete(term);

//       System.out.println("deleted " + deleted +
// 			 " documents containing " + term);

      for (int i = 0; i < reader.maxDoc(); i++)
	reader.delete(i);

      reader.close();
      directory.close();

    } catch (Exception e) {
      System.out.println(" caught a " + e.getClass() +
			 "\n with message: " + e.getMessage());
    }
  }
}
