package com.puzzle.manager

import cats.effect.{ContextShift, IO, Timer}
import com.puzzle.helper.ShuffleAlgorithm
import com.puzzle.model.{Board, Cell, DefaultCell, EmptyCell}
import com.puzzle.view.BoardView

/**
 * Abstract Game manager
 * Gives possibility to implement different types of game representation
 *
 * @tparam F context to be used
 * @tparam A return type for run method, could be Unit
 */
trait GameManager[F[_], A] {

  val bsm: BoardStateManager[F]

  val shuffleAlgorithm: ShuffleAlgorithm

  /**
   * Starts the game loop
   *
   * @return some useful info or Unit
   */
  def run: F[A]

}

class JFXGameManagerImpl(board: Board,
                         val bsm: BoardStateManager[IO],
                         val shuffleAlgorithm: ShuffleAlgorithm)
                        (implicit cs: ContextShift[IO],
                         timer: Timer[IO]) extends GameManager[IO, BoardView] {

  /**
   * On each click on the cell takes board and either move cell or shuffle a board
   * If for clicked cell no close empty cell board will not change
   *
   * @return some useful info or Unit
   */
  override def run: IO[BoardView] = IO.delay {

    val clickHandler: Cell => Board = {
      case cell: DefaultCell =>
        (for {
          newBoard <- bsm.getState.map(_.move(cell.coordinates))
          _ <- bsm.setState(newBoard)
        } yield newBoard).unsafeRunSync()


      case _: EmptyCell =>
        (for {
          newBoard <- bsm.getState.map(shuffleAlgorithm.shuffleBoard)
          _ <- bsm.setState(newBoard)
        } yield newBoard).unsafeRunSync()
    }

    BoardView(clickHandler, board)
  }
}

object JFXGameManagerImpl {
  def apply(board: Board,
            bsm: BoardStateManager[IO],
            shuffleAlgorithm: ShuffleAlgorithm)
           (implicit cs: ContextShift[IO],
            timer: Timer[IO]): JFXGameManagerImpl =
    new JFXGameManagerImpl(board, bsm, shuffleAlgorithm)
}
