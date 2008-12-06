package slinky.http.servlet

import HttpServletRequest._
import HttpServletResponse._

final class StreamStreamServlet extends
  SlinkyServlet[Stream, Stream, StreamStreamServletApplication](classOf[StreamStreamServletApplication])
