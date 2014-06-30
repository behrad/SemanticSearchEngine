package ir.search;


import ir.util.PriorityQueue;

import java.io.IOException;
import java.text.Collator;
import java.util.Locale;

/**
 * Expert: Collects sorted results from Searchable's and collates them.
 * The elements put into this queue must be of type FieldDoc.
 *
 * <p>Created: Feb 11, 2004 2:04:21 PM
 *
 */
class FieldDocSortedHitQueue
extends PriorityQueue {

	// this cannot contain AUTO fields - any AUTO fields should
	// have been resolved by the time this class is used.
	volatile SortField[] fields;

	// used in the case where the fields are sorted by locale
	// based strings
	volatile Collator[] collators;


	/**
	 * Creates a hit queue sorted by the given list of fields.
	 * @param fields Field names, in priority order (highest priority first).
	 * @param size  The number of hits to retain.  Must be greater than zero.
	 * @throws IOException
	 */
	FieldDocSortedHitQueue (SortField[] fields, int size)
	throws IOException {
		this.fields = fields;
		this.collators = hasCollators (fields);
		initialize (size);
	}


	/**
	 * Allows redefinition of sort fields if they are <code>null</code>.
	 * This is to handle the case using ParallelMultiSearcher where the
	 * original list contains AUTO and we don't know the actual sort
	 * type until the values come back.  The fields can only be set once.
	 * This method is thread safe.
	 * @param fields
	 */
	synchronized void setFields (SortField[] fields) {
		if (this.fields == null) {
			this.fields = fields;
			this.collators = hasCollators (fields);
		}
	}


	/** Returns the fields being used to sort. */
	SortField[] getFields() {
		return fields;
	}


	/** Returns an array of collators, possibly <code>null</code>.  The collators
	 * correspond to any SortFields which were given a specific locale.
	 * @param fields Array of sort fields.
	 * @return Array, possibly <code>null</code>.
	 */
	private Collator[] hasCollators (final SortField[] fields) {
		if (fields == null) return null;
		Collator[] ret = new Collator[fields.length];
		for (int i=0; i<fields.length; ++i) {
			Locale locale = fields[i].getLocale();
			if (locale != null)
				ret[i] = Collator.getInstance (locale);
		}
		return ret;
	}


	/**
	 * Returns whether <code>a</code> is less relevant than <code>b</code>.
	 * @param a ScoreDoc
	 * @param b ScoreDoc
	 * @return <code>true</code> if document <code>a</code> should be sorted after document <code>b</code>.
	 */
	protected final boolean lessThan (final Object a, final Object b) {
		final FieldDoc docA = (FieldDoc) a;
		final FieldDoc docB = (FieldDoc) b;
		final int n = fields.length;
		int c = 0;
		for (int i=0; i<n && c==0; ++i) {
			final int type = fields[i].getType();
			if (fields[i].getReverse()) {
				switch (type) {
					case SortField.SCORE:
						float r1 = ((Float)docA.fields[i]).floatValue();
						float r2 = ((Float)docB.fields[i]).floatValue();
						if (r1 < r2) c = -1;
						if (r1 > r2) c = 1;
						break;
					case SortField.DOC:
					case SortField.INT:
						int i1 = ((Integer)docA.fields[i]).intValue();
						int i2 = ((Integer)docB.fields[i]).intValue();
						if (i1 > i2) c = -1;
						if (i1 < i2) c = 1;
						break;
					case SortField.STRING:
						String s1 = (String) docA.fields[i];
						String s2 = (String) docB.fields[i];
						if (s2 == null) c = -1;      // could be null if there are
						else if (s1 == null) c = 1;  // no terms in the given field
						else if (fields[i].getLocale() == null) {
							c = s2.compareTo(s1);
						} else {
							c = collators[i].compare (s2, s1);
						}
						break;
					case SortField.FLOAT:
						float f1 = ((Float)docA.fields[i]).floatValue();
						float f2 = ((Float)docB.fields[i]).floatValue();
						if (f1 > f2) c = -1;
						if (f1 < f2) c = 1;
						break;
					case SortField.CUSTOM:
						c = docB.fields[i].compareTo (docA.fields[i]);
						break;
					case SortField.AUTO:
						// we cannot handle this - even if we determine the type of object (Float or
						// Integer), we don't necessarily know how to compare them (both SCORE and
						// FLOAT both contain floats, but are sorted opposite of each other). Before
						// we get here, each AUTO should have been replaced with its actual value.
						throw new RuntimeException ("FieldDocSortedHitQueue cannot use an AUTO SortField");
					default:
						throw new RuntimeException ("invalid SortField type: "+type);
				}
			} else {
				switch (type) {
					case SortField.SCORE:
						float r1 = ((Float)docA.fields[i]).floatValue();
						float r2 = ((Float)docB.fields[i]).floatValue();
						if (r1 > r2) c = -1;
						if (r1 < r2) c = 1;
						break;
					case SortField.DOC:
					case SortField.INT:
						int i1 = ((Integer)docA.fields[i]).intValue();
						int i2 = ((Integer)docB.fields[i]).intValue();
						if (i1 < i2) c = -1;
						if (i1 > i2) c = 1;
						break;
					case SortField.STRING:
						String s1 = (String) docA.fields[i];
						String s2 = (String) docB.fields[i];
						// null values need to be sorted first, because of how FieldCache.getStringIndex()
						// works - in that routine, any documents without a value in the given field are
						// put first.
						if (s1 == null) c = -1;      // could be null if there are
						else if (s2 == null) c = 1;  // no terms in the given field
						else if (fields[i].getLocale() == null) {
							c = s1.compareTo(s2);
						} else {
							c = collators[i].compare (s1, s2);
						}
						break;
					case SortField.FLOAT:
						float f1 = ((Float)docA.fields[i]).floatValue();
						float f2 = ((Float)docB.fields[i]).floatValue();
						if (f1 < f2) c = -1;
						if (f1 > f2) c = 1;
						break;
					case SortField.CUSTOM:
						c = docA.fields[i].compareTo (docB.fields[i]);
						break;
					case SortField.AUTO:
						// we cannot handle this - even if we determine the type of object (Float or
						// Integer), we don't necessarily know how to compare them (both SCORE and
						// FLOAT both contain floats, but are sorted opposite of each other). Before
						// we get here, each AUTO should have been replaced with its actual value.
						throw new RuntimeException ("FieldDocSortedHitQueue cannot use an AUTO SortField");
					default:
						throw new RuntimeException ("invalid SortField type: "+type);
				}
			}
		}
		return c > 0;
	}
}
