package edu.sharif.ce.dm.takehome;

import java.net.URL;
import java.net.URLEncoder;
import java.net.HttpURLConnection;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Collection of utility methods to get required geo. location information from IP address
 * User: Jrad
 * Date: Jan 26, 2006
 * Time: 3:59:02 PM
 */
public class IP2Location {

    public static Location resolveIp( String ipAddress ) throws Exception {
        Location location = new Location( ipAddress );
        doStuff( ipAddress, location );
        return location;
    }

    private static void doStuff( String ipAddress, Location location ) throws Exception {
        String http_url = "http://www.geobytes.com/IpLocator.htm?GetLocation";
        try {
            // Construct data
            String data = URLEncoder.encode("GetLocation", "UTF-8") + "=" + URLEncoder.encode( "", "UTF-8" );
            data += "&" + URLEncoder.encode("ipaddress", "UTF-8") + "=" + URLEncoder.encode(ipAddress, "UTF-8");
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
            while ((line = rd.readLine()) != null) {
                parseAndUpdate( line, location );
            }
            wr.close();
            rd.close();
        } catch (Exception e) {
            throw new Exception( "Could not read location information from the IP adress.", e );
        }
    }

    private static void parseAndUpdate( String line, Location location ) {
        // a hack! considering ours tags appear in seperate lines
        String value = "";
        value = parseCountry( line );
        if( value.length() > 0 ) {
            location.setCountry( value );
            return;
        }
        value = parseCity( line );
        if( value.length() > 0 ) {
            location.setCity( value );
            return;
        }
        value = parseLatiute( line );
        if( value.length() > 0 ) {
            location.setLatitude( Float.parseFloat( value ) );
            return;
        }
        value = parseLongitute( line );
        if( value.length() > 0 ) {
            location.setLongitute( Float.parseFloat( value ) );
            return;
        }
    }

    // Country ro-no_bots_pls13
    // City ro-no_bots_pls17
    // latitude ro-no_bots_pls10
    // longitutde ro-no_bots_pls19
    // felan base!

    private static String parseCountry( String line ) {
        return getValue( line, "ro-no_bots_pls13" );
    }

    private static String parseCity( String line ) {
        return getValue( line, "ro-no_bots_pls17" );
    }

    private static String parseLongitute( String line ) {
        return getValue( line, "ro-no_bots_pls19" );
    }

    private static String parseLatiute( String line ) {
        return getValue( line, "ro-no_bots_pls10" );
    }

    private static String getValue( String input, String prop_name ) {
        final String value_prop = "value=\"";
        if( input.indexOf( prop_name ) > 0 ) { // has the required property (= html input tag with specified name)
            if( input.indexOf( value_prop ) > 0 ) {
                return input.split( value_prop )[ 1 ].split( "\"" )[0];
            }
        }
        return "";
    }

    public static void main( String[] args ) throws Exception {
        Location loc = IP2Location.resolveIp( Host2IP.resolveHost( "ce.sharif.edu" ) );
        System.out.println( loc.getCountry() );
        System.out.println( loc.getCity() );
        System.out.println( loc.getLatitude() );
        System.out.println( loc.getLongitute() );
    }

}