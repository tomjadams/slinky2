package slinky.http.request;

import fjs.test.Arbitrary
import fjs.test.Arbitrary._
import ArbitraryMethod.arbitraryMethod
import ArbitraryUri.arbitraryUri
import ArbitraryVersion.arbitraryVersion
import Line.line

object ArbitraryLine {
  implicit val arbitraryLine: Arbitrary[Line] = 
    arbitraryMethod <*> (arbitraryUri <*> (arbitraryVersion > (v => u => m => line(m,u,v))))
}
