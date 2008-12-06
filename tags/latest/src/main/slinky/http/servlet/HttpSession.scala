package slinky.http.servlet

import scalaz.OptionW.onull

/**
 * A wrapper around Java Servlet <code>HttpSession</code>.
 *
 * @author <a href="mailto:research@workingmouse.com">Tony Morris</a>
 * @version $LastChangedRevision<br>
 *          $LastChangedDate$<br>
 *          $LastChangedBy$
 */
sealed trait HttpSession {
  /**
   * The wrapped HTTP session.
   */
  val session: javax.servlet.http.HttpSession

  /**
   * Returns the attribute associated with the given value.
   */
  def apply(attr: String) = onull(session.getAttribute(attr))

  /**
   * Deletes the attribute associated with the given value.
   */
  def -=(attr: String) = session.removeAttribute(attr)

  /**
   * Sets the given attribute name to the given value.
   */
  def update[A](attr: String, value: A) = session.setAttribute(attr, value)
}

/**
 * A wrapper around Java Servlet <code>HttpSession</code>.
 */
object HttpSession {
  /**
   * Wraps the given HTTP session.
   */
  implicit def HttpSessionSession(s: javax.servlet.http.HttpSession): HttpSession = new HttpSession {
    val session = s
  }

  /**
   * Unwraps the given HTTP session into a servlet session.
   */
  implicit def SessionHttpSession(session: HttpSession) = session.session
}
