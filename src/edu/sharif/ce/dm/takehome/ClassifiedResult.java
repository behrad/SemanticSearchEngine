package edu.sharif.ce.dm.takehome;

import org.apache.lucene.document.Document;

import java.net.URL;


/**
 * @author Jrad
 *         Date: Feb 1, 2006
 *         Time: 1:54:09 AM
 */
public class ClassifiedResult {

    private Document document;

    private String filename;

    private Location geoLocation;

    private String category;

    private URL url;

    private String summary;

    private String title;

    public ClassifiedResult( Document document ) {
        this.document = document; // for further acess if needed
        setFilename( document.get( "url" ) );
        setSummary( document.get( "summary" ) );
        setTitle( document.get( "title" ) );
    }

//    public Document getDocument() {
//        return document;
//    }

//    public void setDocument( Document document ) {
//        this.document = document;
//    }

    public String getFilename() {
        return filename;
    }

    public void setFilename( String filename ) {
        this.filename = filename;
    }

    public Location getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation( Location geoLocation ) {
        this.geoLocation = geoLocation;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory( String category ) {
        this.category = category;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl( URL url ) {
        this.url = url;
//        resovleGeoLocation();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle( String title ) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary( String summary ) {
        this.summary = summary;
    }

    public void resovleGeoLocation() throws Exception {
        if( getGeoLocation() == null ) {
            if( getUrl() != null ) {
                setGeoLocation( IP2Location.resolveIp( Host2IP.resolveHost( getUrl().getHost() ) ) );
            }
        }
    }
}
