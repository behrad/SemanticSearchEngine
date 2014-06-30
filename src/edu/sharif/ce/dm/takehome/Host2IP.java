package edu.sharif.ce.dm.takehome;

import java.net.URLEncoder;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * There's also a webservice available at http://www.webservicex.net but no worth to add
 * a web service client to such a work. Hope Ghabool-e Allah!
 *
 * User: Jrad
 * Date: Jan 26, 2006
 * Time: 5:13:52 PM
 */
public class Host2IP {

    public static String resolveHost( String hostName ) throws Exception {
        return doStuff( hostName );
    }

    private static String doStuff( String hostName ) throws Exception {
        String http_url = "http://baremetal.com/cgi-bin/dnsip";
        try {
            // Construct data
            String data = URLEncoder.encode("target", "UTF-8") + "=" + URLEncoder.encode( hostName, "UTF-8" );
            // Send data
            URL url = new URL( http_url );
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod( "POST" );
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write( data );
            wr.flush();
            // Get the response
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            String ip = "";
            while ((line = rd.readLine()) != null) {
//                System.out.println( line );
                ip = parse( hostName, line );
                if( ip.length() > 0 ) {
                    break;
                }
            }
            wr.close();
            rd.close();
            return ip;
        } catch (Exception e) {
            throw new Exception( "Could not read location information from the IP adress.", e );
        }
    }

    private static String parse( String hostName, String input ) {
        // search for -> 65.214.43.44<br>
        final String prop_name = hostName + " -> ";
//        System.out.println("XXXXX " + input.indexOf( prop_name ) );
        if( input.indexOf( prop_name ) >= 0 ) { // has the required property (= html input tag with specified name)
            return input.split( prop_name )[ 1 ].split( "<br>" )[0];
        }
        return "";
    }

    public static void main( String args[] ) throws Exception {
        String ip = Host2IP.resolveHost( "ce.sharif.edu" );
        System.out.println( ip );
    }
}
