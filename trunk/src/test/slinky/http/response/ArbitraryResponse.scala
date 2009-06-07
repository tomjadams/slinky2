package slinky.http.response;

import fjs.test.Arbitrary
import fjs.test.Arbitrary.{arbList,arbSChar,arbSTuple2}
import Response.response
import ArbitraryStatusLine.arbitraryStatusLine
import ArbitraryResponseHeader.arbitraryResponseHeader
import scalaz.NonEmptyList
import slinky.TestUtil.ArbitraryNonEmptyList.ArbitraryNonEmptyList
import fjs.data.List.List_ScalaList

object ArbitraryResponse {
  def arbitraryResponse[OUT[_]](implicit arb:Arbitrary[OUT[Byte]]): Arbitrary[Response[OUT]] = 
    arbitraryStatusLine <*> (arbList[(ResponseHeader, NonEmptyList[Char])] <*> (arb > (s => rh => l => response[OUT](l, rh, s))))
  
  import fjs.test.Arbitrary.{arbStream,arbSByte}
  implicit val arbitraryStreamResponse: Arbitrary[Response[Stream]] = arbitraryResponse[Stream]
}
