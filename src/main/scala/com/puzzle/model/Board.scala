package com.puzzle.model

import com.puzzle.model.error.{DefaultBoardValidation, MissedCellWithCoordinates, MissedEmptyCell}

/**
 * Interface for Board used to keep actual board
 * and perform some actions with it
 */
trait Board {
  /**
   * Size of board, board could be only as square
   */
  val gridSize: Int

  /**
   * Set of cells that board contains
   */
  val cells: Set[Cell]

  /**
   * Move action, search whatever empty cell close,
   * and if it is, switch with current
   *
   * @param cellCoordinates coordinates of Cell to be moved
   * @return new board if close empty cell exists or current board
   */
  def move(cellCoordinates: Coordinates): Board

  /**
   * Switch cell method, just switch coordinates of cells
   *
   * @param current current cell
   * @param target  target cell
   * @return new board with switched cells
   */
  def switchCell(current: Cell, target: Cell): Board
}

object DefaultBoard {
  def fifteenBoard: Board = createBoard(4)

  /**
   * Creates iterator of cells, remove last and add empty cell
   *
   * @param gridSize size of board
   * @return
   */
  def createBoard(gridSize: Int): Board =
    if (gridSize <= 1)
      throw DefaultBoardValidation(isGridSizeCorrect = false, isCellsCountCorrect = true, isEmptyCellExists = true)
    else {
      val cellsWithoutEmpty: Set[Cell] = (1 to gridSize).flatMap { line =>
        (1 to gridSize).map { column =>
          DefaultCell(Coordinates(line, column), gridSize * (line - 1) + column)
        }
      }.init.toSet

      val cells = cellsWithoutEmpty + EmptyCell(Coordinates(gridSize, gridSize))
      apply(cells, gridSize)
    }

  def apply(cells: Set[Cell], gridSize: Int): DefaultBoard = {
    val isGridSizeCorrect = gridSize <= 1
    val isCellsCountCorrect = cells.size != gridSize * gridSize
    val isEmptyCellExists = !cells.exists(_.isInstanceOf[EmptyCell])

    if (isGridSizeCorrect || isCellsCountCorrect || isEmptyCellExists)
      throw DefaultBoardValidation(isGridSizeCorrect, isCellsCountCorrect, isEmptyCellExists)
    else
      new DefaultBoard(cells, gridSize)
  }
}

case class DefaultBoard(cells: Set[Cell], gridSize: Int) extends Board {

  def move(cellCoordinates: Coordinates): Board =
    cells.find(_.coordinates == cellCoordinates) match {
      case None => throw MissedCellWithCoordinates(cellCoordinates)
      case Some(_: EmptyCell) => this
      case Some(cell: DefaultCell) =>
        cells.find(_.isInstanceOf[EmptyCell]) match {
          case Some(emptyCell: EmptyCell) if cell.isClose(emptyCell) =>
            switchCell(cell, emptyCell)
          case Some(_: EmptyCell) => this
          case None => throw MissedEmptyCell
        }
    }

  def switchCell(current: Cell, target: Cell): Board = {
    val newCells = cells - current - target ++ current.switch(target)

    this.copy(newCells)

  }

}
