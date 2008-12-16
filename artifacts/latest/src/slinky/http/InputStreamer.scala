package slinky.http

import java.io.InputStream

/**
 * Takes an input-stream to an environment for bytes.
 *
 * @author <a href="mailto:research@workingmouse.com">Tony Morris</a>
 * @version $LastChangedRevision<br>
 *          $LastChangedDate$<br>
 *          $LastChangedBy$
 */
sealed trait InputStreamer[I[_]] {
  /**
   * Transforms the given input-stream to an environment for bytes.
   */
  def apply(in: InputStream): I[Byte]
}

import scalaz.javas.InputStream._

/**
 * Functions over values that take an input-stream to an environment for bytes.
 *
 * @author <a href="mailto:research@workingmouse.com">Tony Morris</a>
 * @version $LastChangedRevision<br>
 *          $LastChangedDate$<br>
 *          $LastChangedBy$
 */
object InputStreamer {
  /**
   * Constructs an input-streamer from the given function.
   */
  def inputStreamer[I[_]](f: InputStream => I[Byte]) = new InputStreamer[I] {
    def apply(in: InputStream) = f(in)
  }

  /**
   * An input-streamer for <code>scala.Stream</code> that reads off the input-stream.
   */
  implicit val StreamInputStreamer: InputStreamer[Stream] = inputStreamer[Stream](i => i)

  /**
   * An input-streamer for <code>scala.Iterator</code> that reads off the input-stream. 
   */
  implicit val IteratorInputStreamer: InputStreamer[Iterator] = inputStreamer[Iterator](i => i)
}
