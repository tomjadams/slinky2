package slinky.http.request;

import fjs.test.Arbitrary
import fjs.test.Arbitrary.{arbList,arbSChar,arbSTuple2}
import Request.request
import ArbitraryLine.arbitraryLine
import ArbitraryRequestHeader.arbitraryRequestHeader
import scalaz.list.NonEmptyList
import scalaz.list.ArbitraryNonEmptyList.ArbitraryNonEmptyList
import fjs.data.List.List_ScalaList

object ArbitraryRequest {
  def arbitraryRequest[IN[_]](implicit arb:Arbitrary[IN[Byte]]): Arbitrary[Request[IN]] = 
    arbitraryLine <*> (arbList[(RequestHeader, NonEmptyList[Char])] <*> (arb > (s => rh => l => request[IN](l, rh, s))))
  
  import fjs.test.Arbitrary.{arbStream,arbSByte}
  implicit val arbitraryStreamRequest: Arbitrary[Request[Stream]] = arbitraryRequest[Stream]
}
