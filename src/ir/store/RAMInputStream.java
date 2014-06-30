package ir.store;


/**
 * A memory-resident {@link InputStream} implementation.
 *
 */

class RAMInputStream extends InputStream implements Cloneable {
  private RAMFile file;
  private int pointer = 0;

  public RAMInputStream(RAMFile f) {
    file = f;
    length = file.length;
  }

  public void readInternal(byte[] dest, int destOffset, int len) {
    int remainder = len;
    int start = pointer;
    while (remainder != 0) {
      int bufferNumber = start/BUFFER_SIZE;
      int bufferOffset = start%BUFFER_SIZE;
      int bytesInBuffer = BUFFER_SIZE - bufferOffset;
      int bytesToCopy = bytesInBuffer >= remainder ? remainder : bytesInBuffer;
      byte[] buffer = (byte[])file.buffers.elementAt(bufferNumber);
      System.arraycopy(buffer, bufferOffset, dest, destOffset, bytesToCopy);
      destOffset += bytesToCopy;
      start += bytesToCopy;
      remainder -= bytesToCopy;
    }
    pointer += len;
  }

  public void close() {
  }

  public void seekInternal(long pos) {
    pointer = (int)pos;
  }
}
