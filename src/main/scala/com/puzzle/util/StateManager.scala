package com.puzzle.util

/**
 * Abstract structure to save different states
 *
 * @tparam F context to be used
 * @tparam A state value
 */
trait StateManager[F[_], A] extends ConcurrentExecutor[F] {

  def getState: F[A]

  def setState(state: A): F[Unit]

}
