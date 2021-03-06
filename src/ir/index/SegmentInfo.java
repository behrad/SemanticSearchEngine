package ir.index;


import ir.store.Directory;

final class SegmentInfo {
  public String name;				  // unique name in dir
  public int docCount;				  // number of docs in seg
  public Directory dir;				  // where segment resides

  public SegmentInfo(String name, int docCount, Directory dir) {
    this.name = name;
    this.docCount = docCount;
    this.dir = dir;
  }
}
