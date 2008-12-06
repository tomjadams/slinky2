package slinky.http

import fjs.test.Arbitrary
import fjs.test.Arbitrary.arb
import fj.test.Gen.elements
import GeneralHeader.generalHeaders

object ArbitraryGeneralHeader {
  implicit val arbitraryGeneralHeader =
    arb(elements[GeneralHeader](generalHeaders.toArray: _*))
}
