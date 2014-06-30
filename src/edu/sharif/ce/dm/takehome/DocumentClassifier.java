package edu.sharif.ce.dm.takehome;

import org.apache.lucene.document.Document;
import edu.sharif.ce.dm.takehome.db.DBUtil;
import java.net.URL;


/**
 * @author Jrad
 *         Date: Feb 1, 2006
 *         Time: 1:52:16 AM
 */
public final class DocumentClassifier {

    public static ClassifiedResult toClassifiedResult( Document doc ) throws Exception {
        ClassifiedResult cr = new ClassifiedResult( doc );
        String filename = preprocessFileName( cr.getFilename() );
        String[] results = DBUtil.read( filename );

        // forgive the magix! just wanna put it to work ;)
        cr.setCategory( results[0] );
        cr.setUrl( new URL( results[1] ) );

        cr.resovleGeoLocation();
        return cr;
    }

    /**
     * To produce required file name index in crawdled page database from lucene local file path
     *
     * @param filename A lucene generated relative file path like: root/subdir/.../010101.html
     * @return Corresponding file name without directory structure fitting into our database file name domain
     */
    private static String preprocessFileName( String filename ) {
        String s = filename;
        if( filename.indexOf( "/" ) >= 0 ) {
            s = filename.substring( filename.lastIndexOf( "/" ) + 1 );
            if( s.indexOf( "." ) > 0 )
                s = s.split( "\\." )[0];
        }
        return s;
    }
}
