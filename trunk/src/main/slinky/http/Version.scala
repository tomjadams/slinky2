package slinky.http

import scalaz.{Digit, _0, _1}
import scalaz.OptionW._
import scalaz.BooleanW._
import scalaz.Scalaz._
import java.lang.Character.isDigit

/**
 * HTTP version.
 * <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec3.html#sec3.1">RFC 2616 Section 3.1 HTTP Version</a>.
 *
 * @author <a href="mailto:code@tmorris.net">Tony Morris</a>
 * @version $LastChangedRevision<br>
 *          $LastChangedDate$<br>
 *          $LastChangedBy$
 */
sealed trait Version {
  /**
   * HTTP Version major number.
   */
  val major: Digit

  /**
   * HTTP Version major number.
   */
  val minor: Digit

  /**
   * A string representation of this version
   */
  def asString = "HTTP/" + major.toInt + "." + minor.toInt
}

/**
 * HTTP version.
 * <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec3.html#sec3.1">RFC 2616 Section 3.1 HTTP Version</a>.
 */
object Version {
  /**
   * Extracts the major and minor numbers of the given version.
   */
  def unapply(v: Version): Option[(Digit, Digit)] =
    Some(v.major, v.minor)

  /**
   * Constructs a version with the given major and minor numbers.
   */
  def version(maj: Digit, min: Digit): Version = new Version {
    val major = maj
    val minor = min
  }

  /**
   * A version for HTTP/1.0.
   */
  val version10 = version(_1, _0)
  
  /**
   * A version for HTTP/1.1.
   */
  val version11 = version(_1, _1)

  /**
   * Returns a string representation for the given version.
   */
  implicit def VersionString(v: Version) = v.asString

  /**
   * Returns a string representation for the given version.
   */
  implicit def ListVersion = StringVersion _ compose List.toString

  /**
   * Returns a potential version for the given string of the form <code>HTTP/major/minor</code>.
   */
  implicit def StringVersion(s: String): Option[Version] =
    if(s.length < 8)
      None
    else {
      val major = s charAt 5
      val minor = s charAt 7

      List(major, minor).traverse[Option](scalaz.Traverse.ListTraverse)((c: Char) => isDigit(c).option(c.toLong - 48L)) map { case List(maj, min) => version(maj, min) }
    }
}
