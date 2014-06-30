package edu.sharif.ce.dm.takehome.db;

import java.sql.*;

/**
 * @author Jrad
 *         Date: Feb 1, 2006
 *         Time: 1:57:38 AM
 */
public final class DBUtil {

    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    private static final String CONNECTION_URL = "jdbc:mysql://localhost/crawl";

    private static final String USERNAME = "";

    private static final String PASSWORD = "";

    private static final String DATA = "crawledpage";

    private static final String URL = "url";

    private static final String FILENAME = "filename";

    private static final String CLASS = "class";

    private static Connection getConnection() throws Exception {
        try {
            Class.forName( JDBC_DRIVER );
            return DriverManager.getConnection( CONNECTION_URL, USERNAME, PASSWORD );
        } catch( SQLException e ) {
            throw new Exception( e );
        }
    }

    public static String[] read( String filename ) throws Exception {
        String[] results = { "", "" };
        Connection conn = getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement( "select " + CLASS + ", " + URL + " from " + DATA + " where " + FILENAME + "='" + filename +  "'" );
            ResultSet rs = ps.executeQuery();
            if( rs.next() ) {
                results[ 0 ] = rs.getString( 1 );
                results[ 1 ] = rs.getString( 2 );
            }
            close( conn );
        } catch( SQLException e ) {
            close( conn );
            throw new Exception( e );
        }
        return results;
    }

    private static void close( Connection conn ) {
        if( conn != null ) {
            try {
                conn.close();
            } catch( SQLException e ) {
                e.printStackTrace();
            }
        }
    }
}
