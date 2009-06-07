package slinky.http.response;

import fj.test.Gen
import fjs.test.Arbitrary
import fjs.test.Arbitrary.{arb,arbAlphaNumString}
import fj.test.Gen.{oneOf,elements}
import fjs.test.Gen.{SGen_Gen,Gen_SGen}
import fjs.data.List.ScalaList_List
import fjs.F.ScalaFunction_F
import scalaz.Digit
import slinky.TestUtil.ArbitraryDigit.arbDigit
import Status.{status, statuses}

object ArbitraryStatus {  
  implicit val arbitraryStatus: Arbitrary[Status] =
    arb(oneOf(
      List[Gen[Status]](elements[Status](statuses: _*),
          (for(a <- arbDigit; b <- arbDigit; c <- arbDigit) yield status(a, b, c)) filter (!statuses.contains(_)) gen)))
}
