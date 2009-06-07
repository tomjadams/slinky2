package slinky

import fjs.test.Arbitrary
import fjs.test.Arbitrary.arb
import fj.test.Gen.elements
import scalaz.Digit

import fjs.test.Arbitrary
import fjs.test.Arbitrary._
import scalaz.NonEmptyList
import scalaz.NonEmptyList.nel

object TestUtil {
  object ArbitraryDigit {
    implicit def arbDigit(): Arbitrary[Digit] = arb(elements[Digit](Digit.digits.toArray: _*))
  }
  
  object ArbitraryNonEmptyList {
    implicit def ArbitraryNonEmptyList[A](implicit aa: Arbitrary[A]): Arbitrary[NonEmptyList[A]] =
      aa >>= (arbSList(aa), a => (as: List[A]) => nel(a, as))
  }
}
