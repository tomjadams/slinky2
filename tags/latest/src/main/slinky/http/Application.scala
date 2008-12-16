package slinky.http

import response.Response
import request.Request

/**
 * A web application that transforms a request to a response.
 *
 * @author <a href="mailto:research@workingmouse.com">Tony Morris</a>
 * @version $LastChangedRevision<br>
 *          $LastChangedDate$<br>
 *          $LastChangedBy$
 */
trait Application[IN[_], OUT[_]] {
  /**
   * Transform the given request to a response.
   */
  def apply(implicit req: Request[IN]): Response[OUT]
}

/**
 * Functions over web applications that transforms a request to a response.
 *
 * @author <a href="mailto:research@workingmouse.com">Tony Morris</a>
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
