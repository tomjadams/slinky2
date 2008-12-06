package slinky.http.request

import scalaz.control.{Semigroup, FoldLeft}
import scalaz.control.MonadW._
import scalaz.control.PlusW._
import scalaz.control.FoldLeftW.foldleft
import scalaz.list.NonEmptyList
import scalaz.list.NonEmptyList._
import scalaz.memo.Memo._
import Util.{asHashMap, mapHeads, parameters}

/**
 * HTTP request.
 * <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec5.html#sec5">RFC 2616 Section 5 Request</a>.
 *
 * @author <a href="mailto:research@workingmouse.com">Tony Morris</a>
 * @version $LastChangedRevision<br>
 *          $LastChangedDate$<br>
 *          $LastChangedBy$
 */
sealed trait Request[IN[_]] {
  /**
   * <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec5.html#sec5.1">§</a>
   */
  val line: Line

  /**
   * <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec5.html#sec5.3">§</a>
   */
  val headers: List[(RequestHeader, NonEmptyList[Char])]

  /**
   * <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec4.html#sec4.3">§</a>
   */
  val body: IN[Byte]

  import Request.request

  /**
   * Adds the given request header and value to a request.
   */
  def apply(h: RequestHeader, v: NonEmptyList[Char]) = request[IN](line, (h, v) :: headers, body)

  /**
   * Sets the status line of a request.
   */
  def apply(l: Line): Request[IN] = request[IN](l, headers, body)

  /**
   * Returns the first value that corresponds to the given request header.
   */
  def apply(h: RequestHeader) = headersMapHeads.get(h)

  /**
   * Sets the request method of the status line of a request.
   */
  def apply(m: Method): Request[IN] = apply(line(m))

  /**
   * Sets the request URI of the status line of a request.
   */
  def apply(u: Uri): Request[IN] = apply(line(u))

  /**
   * Sets the request version of the status line of a request.
   */
  def apply(v: Version): Request[IN] = apply(line(v))

  /**
   * Sets the body of a request.
   */
  def >>(b: IN[Byte]) = request[IN](line, headers, b)

  /**
   * Appends the given value to the body of a request.
   */
  def |+|(b: IN[Byte])(implicit s: Semigroup[IN[Byte]]) = request[IN](line, headers, s.append(body, b))

  /**
   * Deletes all headers of the given value from a request.
   */
  def -(h: RequestHeader) = request[IN](line, headers filter { case (k, _) => h != k }, body)

  /**
   * A map of request header values offering optimal seek time.
   */
  lazy val headersMap = asHashMap[List, NonEmptyList](headers)
  
  /**
   * A map of the first request header values offering optimal seek time.
   */
  lazy val headersMapHeads = mapHeads(headersMap)

  private val m = immutableHashMapMemo[FoldLeft[IN], List[(List[Char], List[Char])]]

  /**
   * Provides look up for POST request parameters in the request body. Only the first invocation uses the given
   * fold-left and subsequent invocations look-up using a memoisation table (scoped to each request).
   */
  def post(implicit f: FoldLeft[IN]) = new {
    val parameters = m(f => Util.parameters(foldleft[IN](body)(f).list > (_.toChar)))(f)
    lazy val parametersMap = asHashMap[List, NonEmptyList](parameters)
    lazy val parametersMapHeads = mapHeads(parametersMap)
    def |(p: String) = parametersMapHeads.get(p.toList)
    def ||(p: String): List[List[Char]] = parametersMap.get(p.toList)
  }

  /**
   * Returns the first occurrence of the given request parameter in the request URI.
   */
  def !(p: String) = line.uri.parametersMapHeads >>= (_.get(p.toList))

  /**
   * Returns all occurrences of the given request parameter in the request URI.
   */
  def !!(p: String) = OptionNonEmptyListList(line.uri.parametersMap >>= (_.get(p.toList)))

  /**
   * Returns <code>true</code> if the given request parameter occurs in the request URI.
   */
  def !?(p: String) = this ! p isDefined

  /**
   * Returns <code>false</code> if the given request parameter occurs in the request URI.
   */
  def ~!?(p: String) = this ! p isEmpty

  /**
   * Returns the first occurrence of the given request parameter in the request body.
   */
  def |(p: String)(implicit f: FoldLeft[IN]) = post | p

  /**
   * Returns <code>true</code> if the given request parameter occurs in the request body.
   */
  def |?(p: String)(implicit f: FoldLeft[IN]) = this | p isDefined

  /**
   * Returns <code>false</code> if the given request parameter occurs in the request body.
   */
  def ~|?(p: String)(implicit f: FoldLeft[IN]) = this | p isEmpty

  /**
   * Returns all occurrences of the given request parameter in the request body.
   */
  def ||(p: String)(implicit f: FoldLeft[IN]) = post || p

  /**
   * Returns the first occurrence of the given request parameter in the request URI if it exists or in the request body
   * otherwise.
   */
  def !|(p: String)(implicit f: FoldLeft[IN]) = this.!(p) <+> |(p)

  /**
   * Returns the first occurrence of the given request parameter in the request body if it exists or in the request URI
   * otherwise.
   */
  def |!(p: String)(implicit f: FoldLeft[IN]) = |(p) <+> this.!(p)

  /**
   * The request method of the status line.
   */
  def method = line.method

  /**
   * The request URI of the status line.
   */
  def uri = line.uri

  /**
   * The request version of the status line.
   */
  def version = line.version

  /**
   * The request path of the request URI of the status line.
   */
  def path = line.uri.path

  /**
   * The query string of the request URI of the status line.
   */
  def queryString = line.uri.queryString

  /**
   * The request version major of the status line.
   */
  def versionMajor = line.version.major

  /**
   * The request version minor of the status line.
   */
  def versionMinor = line.version.minor

  /**
   * Returns <code>true</code> if the request path of the request URI satisfies the given condition.
   */
  def path(f: NonEmptyList[Char] => Boolean) = f(line.uri.path)

  /**
   * Returns <code>true</code> if the query string of the request URI satisfies the given condition.
   */
  def queryString(f: List[Char] => Boolean) = line.uri.queryString exists f

  /**
   * Returns <code>true</code> if the request path of the request URI equals the given value.
   */
  def pathEquals(s: String) = path(_.mkString == s)

  /**
   * Returns <code>true</code> if the request path starts with the given value.
   */
  def pathStartsWith(s: String) = path.mkString startsWith s 

  /**
   * Returns <code>true</code> if the query string of the request URI equals the given value. 
   */
  def queryStringEquals(s: String) = queryString(_.mkString == s)

  /**
   * Returns <code>true</code> if this request method is OPTIONS.
   */
  def isOptions = line.method == OPTIONS

  /**
   * Returns <code>true</code> if this request method is GET.
   */
  def isGet = line.method == GET

  /**
   * Returns <code>true</code> if this request method is HEAD.
   */
  def isHead = line.method == HEAD

  /**
   * Returns <code>true</code> if this request method is POST.
   */
  def isPost = line.method == POST

  /**
   * Returns <code>true</code> if this request method is PUT.
   */
  def isPut = line.method == PUT

  /**
   * Returns <code>true</code> if this request method is DELETE.
   */
  def isDelete = line.method == DELETE

  /**
   * Returns <code>true</code> if this request method is TRACE.
   */
  def isTrace = line.method == TRACE
}

/**
 * HTTP request.
 * <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec5.html#sec5">RFC 2616 Section 5 Request</a>.
 */
object Request {
  import scalaz.control.Monad

  /**
   * Construct a request with the given status line, headers and body.
   */
  def request[IN[_]](l: Line, h: List[(RequestHeader, NonEmptyList[Char])], b: IN[Byte]) = new Request[IN] {
    val line = l
    val headers = h
    val body = b
  }

  /**
   * Crashes the compiler <a href="http://lampsvn.epfl.ch/trac/scala/ticket/1227">Ticket 1227</a>.
   */
  object MethodPath {
    def unapply(r: Request[IN] forSome { type IN[_] }): Option[(Method, String)] =
      Some(r.line.method, List.toString(r.line.uri.path.toList))
  }

  /**
   * Crashes the compiler <a href="http://lampsvn.epfl.ch/trac/scala/ticket/1227">Ticket 1227</a>.
   */
  object MethodUri {
    def unapply(r: Request[IN] forSome { type IN[_] }): Option[(Method, Uri)] =
      Some(r.line.method, r.line.uri)
  }

  /**
   * Crashes the compiler <a href="http://lampsvn.epfl.ch/trac/scala/ticket/1227">Ticket 1227</a>.
   */
  object Path {
    def unapply(r: Request[IN] forSome { type IN[_] }): Option[(String)] =
      Some(List.toString(r.line.uri.path.toList))
  }

  /**
   * Crashes the compiler <a href="http://lampsvn.epfl.ch/trac/scala/ticket/1227">Ticket 1227</a>.
   */
  object Uri {
    def unapply(r: Request[IN] forSome { type IN[_] }): Option[Uri] =
      Some(r.line.uri)
  }

  /**
   * Crashes the compiler <a href="http://lampsvn.epfl.ch/trac/scala/ticket/1227">Ticket 1227</a>.
   */
  object Method {
    def unapply(r: Request[IN] forSome { type IN[_] }): Option[Method] =
      Some(r.line.method)
  }

  /**
   * Crashes the compiler <a href="http://lampsvn.epfl.ch/trac/scala/ticket/1227">Ticket 1227</a>.
   */
  object Version {
    def unapply(r: Request[IN] forSome { type IN[_] }): Option[Version] =
      Some(r.line.version)
  }

  /**
   * Workarounds to <a href="http://lampsvn.epfl.ch/trac/scala/ticket/1227">Ticket 1227</a> stream-body requests.
   */
  object Stream {
    import scala.Stream

    /**
     * Extracts the given request into request method and request path.
     */
    object MethodPath {
      def unapply(r: Request[Stream]): Option[(Method, String)] =
        Some(r.line.method, List.toString(r.line.uri.path.toList))
    }

    /**
     * Extracts the given request into request method and request URI.
     */
    object MethodUri {
      def unapply(r: Request[Stream]): Option[(Method, Uri)] =
        Some(r.line.method, r.line.uri)
    }

    /**
     * Extracts the given request into request path.
     */
    object Path {
      def unapply(r: Request[Stream]): Option[(String)] =
        Some(List.toString(r.line.uri.path.toList))
    }

    /**
     * Extracts the given request into request URI.
     */
    object Uri {
      def unapply(r: Request[Stream]): Option[Uri] =
        Some(r.line.uri)
    }

    /**
     * Extracts the given request into request method.
     */
    object Method {
      def unapply(r: Request[Stream]): Option[Method] =
        Some(r.line.method)
    }

    /**
     * Extracts the given request into request version.
     */
    object Version {
      def unapply(r: Request[Stream]): Option[Version] =
        Some(r.line.version)
    }
  }
}
