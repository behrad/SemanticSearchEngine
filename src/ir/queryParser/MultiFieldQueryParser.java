package ir.queryParser;


import ir.analysis.Analyzer;
import ir.queryParser.CharStream;
import ir.queryParser.ParseException;
import ir.queryParser.QueryParser;
import ir.queryParser.QueryParserTokenManager;
import ir.search.BooleanQuery;
import ir.search.Query;

/**
 * A QueryParser which constructs queries to search multiple fields.
 *
 */
public class MultiFieldQueryParser extends QueryParser
{
    public static final int NORMAL_FIELD     = 0;
    public static final int REQUIRED_FIELD   = 1;
    public static final int PROHIBITED_FIELD = 2;

    public MultiFieldQueryParser(QueryParserTokenManager tm)
    {
        super(tm);
    }

    public MultiFieldQueryParser(CharStream stream)
    {
        super(stream);
    }

    public MultiFieldQueryParser(String f, Analyzer a)
    {
        super(f, a);
    }

    /**
     * <p>
     * Parses a query which searches on the fields specified.
     * <p>
     * If x fields are specified, this effectively constructs:
     * <pre>
     * <code>
     * (field1:query) (field2:query) (field3:query)...(fieldx:query)
     * </code>
     * </pre>
     *
     * @param query Query string to parse
     * @param fields Fields to search on
     * @param analyzer Analyzer to use
     * @throws ParseException if query parsing fails
     * @throws TokenMgrError if query parsing fails
     */
    public static Query parse(String query, String[] fields, Analyzer analyzer)
	throws ParseException
    {
        BooleanQuery bQuery = new BooleanQuery();
        for (int i = 0; i < fields.length; i++)
        {
            Query q = parse(query, fields[i], analyzer);
            bQuery.add(q, false, false);
        }
        return bQuery;
    }

    /**
     * <p>
     * Parses a query, searching on the fields specified.
     * Use this if you need to specify certain fields as required,
     * and others as prohibited.
     * <p><pre>
     * Usage:
     * <code>
     * String[] fields = {"filename", "contents", "description"};
     * int[] flags = {MultiFieldQueryParser.NORMAL FIELD,
     *                MultiFieldQueryParser.REQUIRED FIELD,
     *                MultiFieldQueryParser.PROHIBITED FIELD,};
     * parse(query, fields, flags, analyzer);
     * </code>
     * </pre>
     *<p>
     * The code above would construct a query:
     * <pre>
     * <code>
     * (filename:query) +(contents:query) -(description:query)
     * </code>
     * </pre>
     *
     * @param query Query string to parse
     * @param fields Fields to search on
     * @param flags Flags describing the fields
     * @param analyzer Analyzer to use
     * @throws ParseException if query parsing fails
     * @throws TokenMgrError if query parsing fails
     */
    public static Query parse(String query, String[] fields, int[] flags,
	Analyzer analyzer)
	throws ParseException
    {
        BooleanQuery bQuery = new BooleanQuery();
        for (int i = 0; i < fields.length; i++)
        {
            Query q = parse(query, fields[i], analyzer);
            int flag = flags[i];
            switch (flag)
            {
                case REQUIRED_FIELD:
                    bQuery.add(q, true, false);
                    break;
                case PROHIBITED_FIELD:
                    bQuery.add(q, false, true);
                    break;
                default:
                    bQuery.add(q, false, false);
                    break;
            }
        }
        return bQuery;
    }
}
