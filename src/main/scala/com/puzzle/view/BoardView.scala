package com.puzzle.view

import com.puzzle.model.Cell.Value
import com.puzzle.model.error.MissedCellView
import com.puzzle.model.{Board, Cell}
import scalafx.scene.layout.Pane

import scala.annotation.tailrec
import scala.collection._
import scala.language.implicitConversions

object BoardView {
  val PADDING = 15
  val CELL_MARGIN = 15
  private val CELL_SIZE = 100

  def apply(clickHandler: Cell => Board, board: Board): BoardView =
    new BoardView(clickHandler, board)

  def getGridSize(board: Board): Double =
    PADDING + board.gridSize * (CELL_SIZE + CELL_MARGIN)

}

final class BoardView(clickHandler: Cell => Board, board: Board)
  extends BaseView[Board, Pane](board) {


  val root: Pane = new Pane
  private val cellViews: Map[Value, CellView] = createView(element.cells, Map())

  setSize()(element, element)
  updateCellViews()(element, element)
  onDataChange(setSize())
  onDataChange(updateCellViews())


  private def setSize(): ChangeHandler = (_, to) => {
    val gridSize = BoardView.getGridSize(to)
    root.setPrefSize(gridSize, gridSize)
  }


  private def updateCellViews(): ChangeHandler = (_, to) => {

    @tailrec
    def rec(cellSet: Set[Cell], updated: Set[Cell], created: Set[Cell]): (Set[Cell], Set[Cell]) = {
      if (cellSet.nonEmpty) {
        val currentCell = cellSet.head
        val viewOpt = cellViews.get(currentCell.value)
        viewOpt match {
          case None => throw MissedCellView(currentCell)
          case Some(view) =>
            view render currentCell
            rec(cellSet.tail, updated ++ Set(currentCell), created)
        }
      } else updated -> created
    }

    rec(to.cells, Set(), Set())
  }

  @tailrec
  private def createView(cellSet: Set[Cell], viewsMap: Map[Value, CellView]): Map[Value, CellView] = {
    if (cellSet.nonEmpty) {
      val currentCell = cellSet.head
      val view = new CellView(handleCellClick, currentCell)
      root.children add view.root
      view render currentCell
      createView(cellSet.tail, viewsMap ++ Map(currentCell.value -> view))
    } else viewsMap
  }


  private def handleCellClick(cell: Cell): Unit = {
    render(clickHandler(cell))
  }
}