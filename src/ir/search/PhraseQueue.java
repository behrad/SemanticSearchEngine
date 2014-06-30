package ir.search;


import ir.util.PriorityQueue;

final class PhraseQueue extends PriorityQueue {
  PhraseQueue(int size) {
    initialize(size);
  }

  protected final boolean lessThan(Object o1, Object o2) {
    PhrasePositions pp1 = (PhrasePositions)o1;
    PhrasePositions pp2 = (PhrasePositions)o2;
    if (pp1.doc == pp2.doc) 
      return pp1.position < pp2.position;
    else
      return pp1.doc < pp2.doc;
  }
}
