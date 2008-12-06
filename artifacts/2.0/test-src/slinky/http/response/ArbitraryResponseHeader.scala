package slinky.http.response;

import fjs.test.Arbitrary
import fjs.test.Arbitrary.arb
import fj.test.Gen
import fj.test.Gen.{oneOf, elements}
import fjs.data.List.ScalaList_List
import ArbitraryGeneralHeader.arbitraryGeneralHeader
import ArbitraryEntityHeader.arbitraryEntityHeader
import ResponseHeader.{general, entity, headers}

object ArbitraryResponseHeader {
  implicit val arbitraryResponseHeader: Arbitrary[ResponseHeader] = 
    arb(oneOf(List[Gen[ResponseHeader]](
        elements(headers.map(_._2): _*),
        (arbitraryGeneralHeader > general).gen,
        (arbitraryEntityHeader > entity).gen)))
}
