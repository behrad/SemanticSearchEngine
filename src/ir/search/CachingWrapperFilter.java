package ir.search;


import ir.index.IndexReader;
import java.util.BitSet;
import java.util.WeakHashMap;
import java.util.Map;
import java.io.IOException;

/**
 * Wraps another filters result and caches it.  The caching
 * behavior is like {@link QueryFilter}.  The purpose is to allow
 * filters to simply filter, and then wrap with this class to add
 * caching, keeping the two concerns decoupled yet composable.
 */
public class CachingWrapperFilter extends Filter {
  private Filter filter;

  /*@todo What about serialization in RemoteSearchable?  Caching won't work.
   *       Should transient be removed?
   */
  private transient Map cache;

  /**
   * @param filter Filter to cache results of
   */
  public CachingWrapperFilter(Filter filter) {
    this.filter = filter;
  }

  public BitSet bits(IndexReader reader) throws IOException {
    if (cache == null) {
      cache = new WeakHashMap();
    }

    synchronized (cache) {  // check cache
      BitSet cached = (BitSet) cache.get(reader);
      if (cached != null) {
        return cached;
      }
    }

    final BitSet bits = filter.bits(reader);

    synchronized (cache) {  // update cache
      cache.put(reader, bits);
    }

    return bits;
  }

  public String toString() {
    return "CachingWrapperFilter("+filter+")";
  }
}
