package slinky.http.request

import fjs.test.Arbitrary
import fjs.test.Arbitrary.{arb,arbAlphaNumString}
import fj.test.Gen
import fj.test.Gen.{oneOf,elements}
import fjs.test.Gen.{SGen_Gen,Gen_SGen}
import Method.{StringMethod, methods}
import fjs.data.List.ScalaList_List
import fjs.F.ScalaFunction_F
import scalaz.control.FunctorW.OptionFunctor
import scalaz.list.ArbitraryNonEmptyList._

object ArbitraryMethod {
  implicit val arbitraryMethod: Arbitrary[Method] =
    arb(oneOf(List[Gen[Method]](
      elements[Method](methods.toArray: _*),
      ArbitraryNonEmptyList[Char].gen.map(m => (m: Method)).filter(!methods.contains(_)))))
}
