package slinky.http.servlet

import request.Request

/**
 * A servlet web application with a request body and response body made up of a stream of bytes.
 *
 * @author <a href="mailto:code@tmorris.net">Tony Morris</a>
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

import scalaz.OptionW._
import response.Response
import http.servlet.HttpServlet._
import http.response.OK
import http.response.StreamResponse.{response, statusLine}


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

  /**
   * Handles a request in such a way that if the given function produces no response then return the given response.
   */
  implicit def resourceOr(f: Request[Stream] => Option[Response[Stream]]) = new {
    def or(otherwise: Request[Stream] => Response[Stream]): ServletApplication[Stream, Stream] =
      new ServletApplication[Stream, Stream] {
      def application(implicit servlet: HttpServlet, servletRequest: HttpServletRequest, request: Request[Stream]) =
        f(request) | (request.path ? (in => response(statusLine(OK), Stream.fromIterator(in)), otherwise(request)))
    }
  }
}
