package slinky.http.servlet

import scalaz.control.Each
import javax.servlet.http.Cookie
import response.Response

/**
 * A wrapper around Java Servlet <code>HttpServletResponse</code>.
 *
 * @author <a href="mailto:code@tmorris.net">Tony Morris</a>
 * @version $LastChangedRevision<br>
 *          $LastChangedDate$<br>
 *          $LastChangedBy$
 */
sealed trait HttpServletResponse {
  /**
   * The wrapped HTTP servlet response.
   */
  val response: javax.servlet.http.HttpServletResponse

  /**
   * Sets the given header to the given value on this response.
   */
  def update(header: String, value: String) = response.setHeader(header, value)

  /**
   * Sets the given header to the given values on this response.
   */
  def update[V[_]](header: String, value: V[String])(implicit v: Each[V]) = v.each[String](response.addHeader(header, _), value)

  /**
   * Sets the given header to the given value on this response.
   */
  def update(header: String, value: Int) = response.setIntHeader(header, value)

  /**
   * Sets the given header to the given value on this response.
   */
  def update(header: String, value: Long) = response.setDateHeader(header, value)

  /**
   * Sets the given header to the given cookie value on this response.
   */
  def update(header: String, cookie: Cookie) = response.addCookie(cookie)

  /**
   * Side-effects against this HTTP servlet response using the given response.
   */
  def respond[OUT[_]](res: Response[OUT])(implicit e: Each[OUT]) {
    response.setStatus(res.line.status)

    res.headers.foreach { case (h, v) => response.setHeader(h, List.toString(v)) }

    val out = response.getOutputStream
    e.each[Byte](out.write(_), res.body)
  }
}

/**
 * A wrapper around Java Servlet <code>HttpServletResponse</code>.
 */
object HttpServletResponse {
  /**
   * Wraps the given Java Servlet HTTP response.
   */
  implicit def HttpServletResponseResponse(r: javax.servlet.http.HttpServletResponse): HttpServletResponse = new HttpServletResponse {
    val response = r
  }

  /**
   * Unwraps the given HTTP response into a servlet HTTP response.
   */
  implicit def ResponseHttpServletResponse(response: HttpServletResponse) = response.response
}