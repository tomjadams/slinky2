package slinky.http

import fjs.test.Arbitrary
import fjs.test.Arbitrary.{arb,arbAlphaNumString}
import fj.test.Gen
import fj.test.Gen.{oneOf,elements}
import fjs.test.Gen.{SGen_Gen,Gen_SGen}
import EntityHeader.{StringEntityHeader,entityHeaders}
import fjs.data.List.ScalaList_List
import fjs.F.ScalaFunction_F
import scalaz.control.FunctorW.OptionFunctor
import scalaz.OptionW.OptionOptionW

object ArbitraryEntityHeader {
  implicit val arbitraryEntityHeader: Arbitrary[EntityHeader] =    
    arb(oneOf(
      List[Gen[EntityHeader]](elements[EntityHeader](entityHeaders.toArray: _*),
        (for(h <- arbAlphaNumString if (h: Option[EntityHeader]) exists (!entityHeaders.contains(_))) yield h.get).gen)))
}
