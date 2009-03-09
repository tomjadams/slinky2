package slinky.http

import scalaz.control.Semigroup
import response.{Body, Response, OK}
import request.Request

/**
 * A web application that transforms a request to a response.
 *
 * @author <a href="mailto:code@tmorris.net">Tony Morris</a>
 * @version $LastChangedRevision<br>
 *          $LastChangedDate$<br>
 *          $LastChangedBy$
 */
trait Application[IN[_], OUT[_]] {
  /**
   * Transform the given request to a response.
   */
  def apply(implicit req: Request[IN]): Response[OUT]

  import Application.application

  def ^*^(x: String)(implicit b: Body[OUT, String], s: Semigroup[OUT[Byte]]): Application[IN, OUT] = application[IN, OUT](req => {
    val res = Application.this(req)
    if(res(ContentType).isDefined) res
    else if(req.isInternetExplorer)
      "<?xml-stylesheet type=\"text/xsl\" href=\"" + x + "\"?>" <<: res(ContentType, "application/xml")
    else
      res(ContentType, "application/xhtml+xml")
  })
}

/**
 * Functions over web applications that transforms a request to a response.
 *
 * @author <a href="mailto:code@tmorris.net">Tony Morris</a>
 * @version $LastChangedRevision<br>
 *          $LastChangedDate$<br>
 *          $LastChangedBy$
 */
object Application {
  /**
   * Create a web application from the given function.
   */
  def application[IN[_], OUT[_]](f: Request[IN] => Response[OUT]) = new Application[IN, OUT] {
    def apply(implicit req: Request[IN]) = f(req)
  }
}
