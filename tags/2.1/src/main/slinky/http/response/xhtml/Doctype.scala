package slinky.http.response.xhtml

/**
 * XHTML doctypes.
 *
 * @author <a href="mailto:research@workingmouse.com">Tony Morris</a>
 * @version $LastChangedRevision<br>
 *          $LastChangedDate$<br>
 *          $LastChangedBy$
 */
sealed trait Doctype {
  /**
   * A string representation of the doctype, suitable for use in the header of an XHTML page.
   */
  val asString: String
}
private final case object Transitional extends Doctype {
  val asString = """<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">"""
}
private final case object Strict extends Doctype {
  val asString = """<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">"""
}
private final case object Frameset extends Doctype {
  val asString = """<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">"""
}

/**
 * XHTML doctypes.
 */
object Doctype {
  /**
   * All XHTML doctypes.
   */
  val doctypes = List(transitional, strict, frameset)

  /**
   * The XHTML Transitional doctype.
   */
  val transitional: Doctype = Transitional

  /**
   * The XHTML Strict doctype.
   */
  val strict: Doctype = Strict

  /**
   * The XHTML Frameset doctype.
   */
  val frameset: Doctype = Frameset
}
