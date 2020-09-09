package com.puzzle.helper

import cats.instances.tuple._
import cats.syntax.bifunctor._
import com.puzzle.model.error.{MissedCells, MissedEmptyCell}
import com.puzzle.model.{Board, DefaultCell, EmptyCell}

import scala.annotation.tailrec

/**
 * Algorithm to shuffle a board
 */
trait ShuffleAlgorithm {
  /**
   * @param board current board
   * @return new shuffled board that could be solved
   */
  def shuffleBoard(board: Board): Board
}

class DefaultShuffleAlgorithm(randomizeAlgorithm: RandomizeAlgorithm) extends ShuffleAlgorithm {

  def shuffleBoard(board: Board): Board = shuffleBoardRec(board)

  @tailrec
  private def getInvCount(cells: List[DefaultCell], currentIndex: Int, targetIndex: Int, invCount: Int): Int = {
    if (currentIndex < cells.size - 1)
      if (targetIndex < cells.size) {
        val isInvPossible = cells(currentIndex).value > cells(targetIndex).value

        if (isInvPossible)
          getInvCount(cells, currentIndex, targetIndex + 1, invCount + 1)
        else
          getInvCount(cells, currentIndex, targetIndex + 1, invCount)

      } else getInvCount(cells, currentIndex + 1, currentIndex + 2, invCount)
    else invCount
  }

  private[helper] def isBoardSolvable(board: Board): Boolean = {

    val (defaultCells: Set[DefaultCell], emptyCellOpt: Option[EmptyCell]) =
      board.cells
        .partition(_.isInstanceOf[DefaultCell])
        .bimap(_.map(_.asInstanceOf[DefaultCell]), _.headOption.map(_.asInstanceOf[EmptyCell]))

    val invCount = getInvCount(defaultCells.toList.sortBy(_.coordinates), 0, 1, 0)

    emptyCellOpt match {
      case None => throw MissedEmptyCell
      case Some(emptyCell) =>
        val isInvCountEven = invCount % 2 == 0
        if (board.gridSize % 2 == 0) {
          val isLineEven = emptyCell.coordinates.line % 2 == 0
          isLineEven == isInvCountEven // InvCount and Line of Empty cell parity should be equal
        } else
          isInvCountEven
    }
  }

  /**
   * Method shuffle board and then check
   * could it be solved, if no - it recursively
   * shuffle board again
   */
  @tailrec
  private def shuffleBoardRec(board: Board): Board = {

    /**
     * To shuffle board it switch each element
     * 1 time with random element.
     * If element the same - nothing happened
     */
    @tailrec
    def boardShuffle(board: Board, index: Int): Board = {
      if (index < board.cells.size) {
        val randomIndex = randomizeAlgorithm.nextInt(board.cells.size)
        val newBoardOpt = for {
          currentCell <- board.cells.find(_.value == index)
          targetCell <- board.cells.find(_.value == randomIndex)
        } yield board.switchCell(currentCell, targetCell)
        newBoardOpt match {
          case None => throw MissedCells(index, randomIndex)
          case Some(newBoard) => boardShuffle(newBoard, index + 1)
        }
      }
      else board
    }

    val newBoard = boardShuffle(board, 0)

    if (isBoardSolvable(newBoard))
      newBoard
    else shuffleBoardRec(board)
  }
}

object DefaultShuffleAlgorithm {
  def apply(randomizeAlgorithm: RandomizeAlgorithm): DefaultShuffleAlgorithm = new DefaultShuffleAlgorithm(randomizeAlgorithm)
}
