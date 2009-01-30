package slinky.http.request

/**
 * A request line.
 * <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec5.html#sec5.1">RFC 2616 Section 5.1 Request-Line</a>.
 *
 * @author <a href="mailto:code@tmorris.net">Tony Morris</a>
 * @version $LastChangedRevision<br>
 *          $LastChangedDate$<br>
 *          $LastChangedBy$
 */
sealed trait Line {
  /**
   * The <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec5.html#sec5.1.1">request method</a>.
   */
  val method: Method

  /**
   * The <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec5.html#sec5.1.2">request URI</a>.
   */
  val uri: Uri

  /**
   * The <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec3.html#sec3.1">request version</a>.
   */
  val version: Version

  import Line.line

  /**
   * Returns a request line with the given method and this URI and version.
   */
  def apply(m: Method): Line = line(m, uri, version)

  /**
   * Returns a request line with the given URI and this method and version.
   */
  def apply(u: Uri): Line = line(method, u, version)
  
  /**
   * Returns a request line with the given version and this method and URI.
   */
  def apply(v: Version): Line = line(method, uri, v)
}

import scalaz.list.NonEmptyList

/**
 * A request line.
 * <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec5.html#sec5.1">RFC 2616 Section 5.1 Request-Line</a>.
 */
object Line {
  /**
   * An extractor that always matches with the method, URI and version of the given request line.
   */
  def unapply(line: Line): Option[(Method, Uri, Version)] =
    Some(line.method, line.uri, line.version)

  /**
   * Construct a request line with the given method, URI and version
   */
  def line(m: Method, u: Uri, v: Version): Line = new Line {
    val method = m
    val uri = u
    val version = v
  }

  import Character.isSpace
  import scalaz.control.ApplicativeW._

  /**
   * Converts the given string into a potential request line.
   */
  implicit def ListLine(cs: List[Char]): Option[Line] = {
    def reverseTrim(c: List[Char]) = c.dropWhile(isSpace(_)).reverse.dropWhile(isSpace(_))
    val x = cs break (isSpace(_))
    val m: Option[Method] = x._1
    val y = x._2.reverse break (isSpace(_))
    val u: Option[Uri] = reverseTrim(y._2)
    val v: Option[Version] = reverseTrim(y._1)
    v <*> (u <*> (m > (m => u => v => line(m, u, v))))
  }
}