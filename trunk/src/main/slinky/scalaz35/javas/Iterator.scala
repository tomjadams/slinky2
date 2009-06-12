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
   * Converts a <code>java.util.Enumeration</code> to a scala iterator.
   */
  implicit def EnumerationIterator[A](e: java.util.Enumeration[A]) = new Iterator[A] {
    def hasNext = e.hasMoreElements
    def next = e.nextElement
  }

}
