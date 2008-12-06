package slinky.http.response;

import fjs.test.Arbitrary
import fjs.test.Arbitrary.{arb,arbString,arbSList,arbSChar}
import ArbitraryVersion.arbitraryVersion
import ArbitraryStatus.arbitraryStatus
import StatusLine.statusLine

object ArbitraryStatusLine {
  implicit val arbitraryStatusLine: Arbitrary[StatusLine] = 
    arbitraryVersion <*> (arbitraryStatus <*> (arbSList[Char] > (l => s => v => statusLine(v,s,l))))
}
