package com.puzzle.util

import cats.effect.IO
import org.specs2.execute.{AsResult, Result, Failure => SpecFailure}
import org.specs2.matcher.MatchersImplicits._
import org.specs2.matcher.{Expectable, ExpectationsCreation, MatchResult, Matcher}

import scala.util.{Failure, Success, Try}

trait IOMatchers extends ExpectationsCreation {

  class IOAsResult[T](io: IO[T])(implicit asResult: AsResult[T]) {
    def run: Result = {
      Try(io.unsafeRunSync()) match {
        case Success(value) => asResult.asResult(value)
        case Failure(th) =>
          checkResultFailure(SpecFailure(s"Failure due to ${th.getMessage}", stackTrace = th.getStackTrace.toList))
      }
    }
  }

  implicit class IOMatchable[T](m: Matcher[T]) {

    def io: Matcher[IO[T]] = new Matcher[IO[T]] {
      override def apply[S <: IO[T]](t: Expectable[S]): MatchResult[S] = {

        val syncFailCapture: IO[T] = t.value
        try {
          val r = new IOAsResult(syncFailCapture.map(t => createExpectable(t).applyMatcher(m))).run
          result(r.isSuccess, r.message, r.message, t)
        } catch {
          case th: Throwable =>
            val r = createExpectable(throw th).applyMatcher(m).toResult
            result(r.isSuccess, r.message, r.message, t)
        }
      }
    }

  }

}
