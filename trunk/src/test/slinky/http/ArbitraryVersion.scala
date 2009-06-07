package slinky.http

import Function.curried
import fjs.test.Arbitrary
import fjs.test.Arbitrary.arb
import fj.test.Gen
import fj.test.Gen.{elements, oneOf}
import slinky.TestUtil.ArbitraryDigit.arbDigit
import scalaz.Digit
import Version.{version,version10,version11}
import fjs.data.List.ScalaList_List
 
object ArbitraryVersion {
  implicit val arbitraryVersion: Arbitrary[Version] =
    arb(oneOf(List[Gen[Version]](
      elements[Version](version10,version11), 
      (for(major <- arbDigit; minor <- arbDigit) yield version(major, minor)).gen)))
}
