package slinky.http.request

import fjs.data.List.ScalaList_List
import fjs.test.Arbitrary
import fjs.test.Arbitrary.arb
import fjs.test.Gen.SGen_Gen
import fj.test.Gen
import fj.test.Gen.{oneOf,elements}
import ArbitraryEntityHeader.arbitraryEntityHeader
import ArbitraryGeneralHeader.arbitraryGeneralHeader
import RequestHeader.{entity,general, headers}

object ArbitraryRequestHeader {
  implicit val arbitraryRequestHeader: Arbitrary[RequestHeader] =
    arb(oneOf(List(
        elements[RequestHeader](headers.map(_._2).toArray: _*),
        (arbitraryEntityHeader > (entity(_))).gen: Gen[RequestHeader],
        (arbitraryGeneralHeader > (general(_))).gen: Gen[RequestHeader])))
}
