package slinky.http.servlet

import scalaz.OptionW._
import scalaz.Functor._
import scalaz.LazyIdentity._
import Util.Nel._

/**
 * A wrapper around Java Servlet <code>HttpServlet</code>.
 *
 * @author <a href="mailto:code@tmorris.net">Tony Morris</a>
 * @version $LastChangedRevision<br>
 *          $LastChangedDate$<br>
 *          $LastChangedBy$
 */
sealed trait HttpServlet {
  /**
   * The wrapped HTTP servlet.
   */
  val servlet: javax.servlet.http.HttpServlet

  /**
   * Returns a potential resource loaded with using the servlet container. 
   */
  def resource(path: String) = servlet.getServletContext.getResourceAsStream(path).onull map (x => x)
}

import scalaz.NonEmptyList
import slinky.scalaz35.javas.InputStream._
import slinky.http.request.Request

/**
 * A wrapper around Java Servlet <code>HttpServlet</code>.
 */
object HttpServlet {
  /**
   * Wraps the given Java Servlet.
   */
  implicit def HttpServletServlet(s: javax.servlet.http.HttpServlet): HttpServlet = new HttpServlet {
    val servlet = s
  }

  /**
   * Unwraps the given HTTP servlet into a servlet.
   */
  implicit def ServletHttpServlet(s: HttpServlet) = s.servlet

  /**
   * Loads a resource at the given path. If that resource is found, return the result of applying the given function,
   * otherwise return the given value.
   */
  def resource[A](path: String, found: Iterator[Byte] => A, notFound: => A)(implicit s: HttpServlet) =
    s.resource(path) map (found(_)) getOrElse notFound

  /**
   * Loads a resource at the path of the given request. If that resource is found, return the result of applying the
   * given function, otherwise return the given value.
   */
  def resource[A](found: Iterator[Byte] => A, notFound: => A)(implicit s: HttpServlet, request: Request[IN] forSome { type IN[_] }): A =
    resource(request.path.mkString, found, notFound)

  /**
   * Loads a resource at the given path. If that resource is found, return the result of applying the given function,
   * otherwise return the given value.
   */
  implicit def Resource(path: NonEmptyList[Char]): { def ?[A](found: Iterator[Byte] => A, notFound: => A)(implicit s: HttpServlet): A } = new {
    def ?[A](found: Iterator[Byte] => A, notFound: => A)(implicit s: HttpServlet) =
      s.resource(path) map (found(_)) getOrElse notFound
  }
}
