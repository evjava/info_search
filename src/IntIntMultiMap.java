import com.almworks.integers.*;

import java.util.ConcurrentModificationException;

public class IntIntMultiMap implements IntIntIterable {
  // todo replace with 1<<32
  public static long FACTOR = 1000;
  public static IntIntMultiMap EMPTY = new IntIntMultiMap();

  // documents and positions
  private LongAmortizedSet set = new LongAmortizedSet();

  public boolean put(int key, int value) {
    return set.include(key * FACTOR + value);
  }

  public IntIterator getKey(final int key) {
    final LongIterator it = set.tailIterator(key * FACTOR);
    if (!it.hasNext()) return IntIterator.EMPTY;
    return new IntFindingIterator() {
      @Override
      protected boolean findNext() throws ConcurrentModificationException {
        if (!it.hasNext()) return false;
        int cur = right(it.value());
        if (cur >= (key + 1) * FACTOR) return false;

        myNext = cur;
        return true;
      }
    };
  }

  protected int left(long val) {
    return (int) (val / FACTOR);
  }

  protected int right(long val) {
    return (int) (val % FACTOR);
  }

  public LongIterator longIterate() {
    return set.iterator();
  }

  @Override
  public IntIntIterator iterator() {
    final LongIterator it = set.iterator();
    return new IntIntFindingIterator() {
      @Override
      protected boolean findNext() throws ConcurrentModificationException {
        if (!it.hasNext()) return false;
        long val = it.nextValue();
        myNextLeft = IntIntMultiMap.this.left(val);
        myNextRight = IntIntMultiMap.this.right(val);
        return true;
      }
    };
  }
}
