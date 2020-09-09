package com.puzzle.manager

import cats.effect.concurrent.{MVar, MVar2, Semaphore}
import cats.effect.{Concurrent, Sync, Timer}
import cats.syntax.flatMap._
import cats.syntax.functor._
import com.puzzle.model.Board
import com.puzzle.util.StateManager

/**
 * Board state manager
 * To hold state MVar2 is used
 *
 * @tparam F context to be used
 */
trait BoardStateManager[F[_]] extends StateManager[F, Board] {

  type R = MVar2[F, Board]

}

class DefaultBoardStateManager[F[_]](protected val semaphore: Semaphore[F],
                                     protected val resource: MVar2[F, Board])
                                    (implicit val F: Sync[F]) extends BoardStateManager[F] {


  def getState: F[Board] = executeFlat(r => r.read)

  def setState(state: Board): F[Unit] = executeFlat(r => r.swap(state).void)
}

object DefaultBoardStateManager {
  def apply[F[_] : Concurrent : Timer](board: Board): F[DefaultBoardStateManager[F]] = {
    for {
      semaphore <- Semaphore[F](1)
      boardF <- MVar[F].of(board)
    } yield new DefaultBoardStateManager(semaphore, boardF)
  }
}
