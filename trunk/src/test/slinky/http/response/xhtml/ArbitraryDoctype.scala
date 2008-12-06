package slinky.http.response.xhtml;

import fjs.test.Arbitrary
import fjs.test.Arbitrary.arb
import fj.test.Gen.elements
import Doctype.doctypes

object ArbitraryDoctype {
  implicit def arbitraryDigit(): Arbitrary[Doctype] = 
    arb(elements(doctypes: _*))
}
