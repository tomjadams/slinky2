package slinky.http

import collection.immutable.{Map, HashMap}
import scalaz.control.{Semigroup, FoldLeft, Monad}
import scalaz.control.ParamorphismW._
import scalaz.control.FoldRightW._
import scalaz.list.NonEmptyList
import scalaz.control.FunctorW._
import java.net.URLDecoder.decode

/**
 * Utility functions not specific to any particular context within HTTP.
 *
 * @author <a href="mailto:research@workingmouse.com">Tony Morris</a>
 * @version $LastChangedRevision<br>
 *          $LastChangedDate$<br>
 *          $LastChangedBy$
 */
object Util {
  /**
   * Splits the given argument by ampersand (<code>&</code>) then each list again by the equals sign (<code>=</code>).
   * e.g. <code>parameters("a=b&c=d&e=f") == [("a", "b"), ("c", "d"), ("e", "f")]</code> 
   */
  def parameters(p: List[Char]): List[(List[Char], List[Char])] =
    p.selectSplit(_ != '&') > (_ !- (_ == '=') match { case (a, b) => (a, decode(b.mkString).drop(1).toList) })

  /**
   * Returns a map of key/value pairs where only the first value for the given key is available.
   */
  def mapHeads[K, C](p: Map[K, NonEmptyList[C]]) = p.transform((h, v) => v.head)

  trait AsHashMap[T[_], SM[_]] {
    def apply[K, V](kvs: T[(K, V)])(implicit f: FoldLeft[T], s: Semigroup[SM[V]], md: Monad[SM]): Map[K, SM[V]]
  }

  /**
   * Indexes the given sequence of key/value pairs using an empty hash-map. The key/value pair type constructor must
   * support fold-left and the index is created in constant space.
   */
  def asHashMap[T[_], SM[_]] = new AsHashMap[T, SM] {
    def apply[K, V](kvs: T[(K, V)])(implicit f: FoldLeft[T], s: Semigroup[SM[V]], md: Monad[SM]) =
      asMap[T, SM](new HashMap[K, SM[V]], kvs)
  }

  /**
   * Indexes the given sequence of key/value pairs using the given map. The key/value pair type constructor must support
   * fold-left and the index is created in constant space.
   */
  def asMap[T[_], SM[_]] = new {
    def apply[K, V](e: Map[K, SM[V]], kvs: T[(K, V)])
      (implicit f: FoldLeft[T], s: Semigroup[SM[V]], md: Monad[SM]): Map[K, SM[V]] =
      f.foldLeft[Map[K, SM[V]], (K, V)](kvs, e, (m, kv) => m + ((kv._1, m.get(kv._1) match {
        case None => md.pure(kv._2)
        case Some(vv) => s.append(md.pure(kv._2), vv)
      })))
  }
}
