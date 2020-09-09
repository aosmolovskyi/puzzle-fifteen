package com.puzzle.util

import cats.effect.{ContextShift, IO, Timer}
import org.specs2.specification.core.{Env, SpecificationStructure}

trait IOSpec {
  this: SpecificationStructure =>

  def env: Env

  implicit val CS: ContextShift[IO] = IO.contextShift(env.executionContext)
  implicit val T: Timer[IO] = IO.timer(env.executionContext)
}
