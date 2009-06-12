// Copyright Tony Morris 2008-2009
// This software is released under an open source BSD licence.

// $LastChangedRevision: 169 $
// $LastChangedDate: 2009-03-24 15:09:07 +1000 (Tue, 24 Mar 2009) $


package slinky.scalaz35.javas

/**
 * Functions over a <code>scala.Iterator</code>.
 *
 * @see scala.Iterator
 * @author <a href="mailto:code@tmorris.net">Tony Morris</a>
 * @version $LastChangedRevision: 169 $<br>
 *          $LastChangedDate: 2009-03-24 15:09:07 +1000 (Tue, 24 Mar 2009) $<br>
 *          $LastChangedBy: tonymorris $
 */
object Iterator {
  /**
   * Converts a <code>java.util.Iterator</code> to a scala iterator.
   */
  implicit def JavaIteratorIterator[A](i: java.util.Iterator[A]) = new Iterator[A] {
    def next = i.next
    def hasNext = i.hasNext
  }

  /**
   * Converts a scala iterator to a <code>java.util.Iterator</code>.
   */
  implicit def IteratorJavaIterator[A](i: Iterator[A]): java.util.Iterator[A] = new java.util.Iterator[A] {
    def next = i.next
    def hasNext = i.hasNext
    def remove = i.remove
  }

  /**
   * Converts a <code>java.util.Enumeration</code> to a scala iterator.
   */
  implicit def EnumerationIterator[A](e: java.util.Enumeration[A]) = new Iterator[A] {
    def hasNext = e.hasMoreElements
    def next = e.nextElement
  }

  /**
   * Converts an iterator to a <code>java.util.Enumeration</code>.
   */
  implicit def IteratorEnumeration[A](i: Iterator[A]) = new java.util.Enumeration[A] {
    def hasMoreElements = i.hasNext
    def nextElement = i.next
  }

  /**
   * Converts an iterator to a stream.
   */
  implicit def IteratorStream[A](i: java.util.Iterator[A]): Stream[A] = {
      if(i.hasNext) {
        val x = i.next
        Stream.cons(x, i)
      } else
        Stream.empty
    }
}
