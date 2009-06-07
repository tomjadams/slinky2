// Copyright Tony Morris 2008-2009
// This software is released under an open source BSD licence.

// $LastChangedRevision: 169 $
// $LastChangedDate: 2009-03-24 15:09:07 +1000 (Tue, 24 Mar 2009) $


package slinky.scalaz35.javas

/**
 * Functions over a <code>java.io.InputStream</code>.
 *
 * @see java.io.InputStream
 * @author <a href="mailto:code@tmorris.net">Tony Morris</a>
 * @version $LastChangedRevision: 169 $<br>
 *          $LastChangedDate: 2009-03-24 15:09:07 +1000 (Tue, 24 Mar 2009) $<br>
 *          $LastChangedBy: tonymorris $
 */
object InputStream {
  /**
   * Converts the given <code>InputStream</code> to an iterator.
   */
  implicit def InputStreamByteIterator(in: java.io.InputStream) = 
    new Iterator[Byte] {
      var i: Int = _
      var b = false
      var h = true
      
      def next = if(hasNext) {
        b = false
        i.toByte
      } else error("Iterator.next (no more elements)")
     
      def hasNext = {
        if(b) h
        else if(h) {
          i = in.read
          b = true
          if(i == -1)
            h = false
          h
        } else false
      }
  }

  /**
   * Converts the given <code>Iterator</code> to an <code>InputStream</code>.
   */
  implicit def ByteIteratorInputStream(i: Iterator[Byte]) =
    new java.io.InputStream {
      def read = if(i.hasNext) i.next.toInt else -1
    }

  /**
   * Converts the given <code>InputStream</code> to a stream.
   */
  implicit def InputStreamByteStream(in: java.io.InputStream): Stream[Byte] = {
      val c = in.read
      if(c == -1) Stream.empty
      else Stream.cons(c.toByte, in)
    }
}
