package com.puzzle.util

import cats.effect.Sync
import cats.effect.concurrent.Semaphore

/**
 * Simple interface to block mutable operations
 *
 * @tparam F context to be used
 */
trait ConcurrentExecutor[F[_]] {

  type R

  protected val semaphore: Semaphore[F]

  implicit val F: Sync[F]

  /**
   * Resource to be used
   */
  protected val resource: R

  protected def execute[A](f: R => A): F[A] =
    semaphore.withPermit(F.delay(f(resource)))

  protected def executeFlat[A](f: R => F[A]): F[A] =
    semaphore.withPermit(f(resource))

}
