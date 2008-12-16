package slinky.http.request;

import fj.test.Gen.{oneOf,value}
import fjs.test.Gen.SGen_Gen
import fjs.test.Arbitrary
import fjs.data.Option._
import fjs.test.Arbitrary.{arb, arbAlphaNumString, arbOption, arbSChar}
import fjs.F.ScalaFunction_F
import fjs.data.List.ScalaList_List
import Uri.uri
import scalaz.list.NonEmptyList._
import scalaz.list.ArbitraryNonEmptyList.ArbitraryNonEmptyList

object ArbitraryUri {
  implicit val arbitraryUri: Arbitrary[Uri] =
    for(c <- arbSChar; p <- arbAlphaNumString; s <- arbOption[String])
      yield uri(nel((c % 128).toChar, p.toList), s.map((x: String) => x.toList))
}
