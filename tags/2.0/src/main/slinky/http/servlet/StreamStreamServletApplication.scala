package slinky.http.servlet

/**
 * A servlet web application with a request body and response body made up of a stream of bytes.
 *
 * @author <a href="mailto:research@workingmouse.com">Tony Morris</a>
 * @version $LastChangedRevision<br>
 *          $LastChangedDate$<br>
 *          $LastChangedBy$
 */
trait StreamStreamServletApplication {
  /**
   * The servlet web application.
   */
  val application: ServletApplication[Stream, Stream]
}

/**
 * A servlet web application with a request body and response body made up of a stream of bytes.
 */
object StreamStreamServletApplication {
  /**
   * Constructs a stream/stream servlet web application from the given argument.
   */
  def application(a: ServletApplication[Stream, Stream]) = new StreamStreamServletApplication {
    val application = a
  }
}
