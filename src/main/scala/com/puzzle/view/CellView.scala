package com.puzzle.view

import com.puzzle.model.{Cell, DefaultCell}
import javafx.scene.{layout => jfxsl}
import scalafx.Includes._
import scalafx.animation._
import scalafx.geometry.Insets
import scalafx.scene.control.Label
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout._
import scalafx.scene.paint.Color
import scalafx.scene.text.Font
import scalafx.util.Duration

object CellView {
  private val CELL_SIZE = 100
  private val TRANSITION_DURATION: Duration = Duration(500)


  private def getBackground(data: Cell): jfxsl.Background = Background
    .sfxBackground2jfx(data match {
      case _: DefaultCell => new Background(
        Array(new BackgroundFill(
          Color.Black, CornerRadii.Empty, Insets.Empty
        ))
      )
      case _ => Background.Empty
    })

  private def getLabel(data: Cell): String = data match {
    case cell: DefaultCell => cell.value.toString
    case _ => "  "
  }

  private def getXPos(data: Cell): Double =
    BoardView.PADDING + (CellView.CELL_SIZE + BoardView.CELL_MARGIN) * (data.coordinates.line - 1)

  private def getYPos(data: Cell): Double =
    BoardView.PADDING + (CellView.CELL_SIZE + BoardView.CELL_MARGIN) * (data.coordinates.column - 1)
}

final class CellView(clickHandler: Cell => Unit, value: Cell)
  extends BaseView[Cell, Pane](value) {


  lazy val root: Pane = pane
  private val label = new Label() {
    font = new Font(48)
    textFill = Color.White
    text <== mapData(CellView.getLabel)
  }

  private val pane = new StackPane() {
    prefWidth = CellView.CELL_SIZE
    prefHeight = CellView.CELL_SIZE
    background <== mapData(CellView.getBackground)
    children = label
    onMouseClicked = handleClick
  }

  translate(element, element)
  onDataChange(translateLoop)

  private def translateLoop(from: Cell, to: Cell): Unit = if (from != to) translate(from, to)

  private def translate(from: Cell, to: Cell): Unit = {
    val transition = newTransition

    transition.stop()

    transition.fromX = CellView.getXPos(from)
    transition.fromY = CellView.getYPos(from)

    transition.toX = CellView.getXPos(to)
    transition.toY = CellView.getYPos(to)

    transition.play()

    ()
  }

  private def newTransition: TranslateTransition =
    new TranslateTransition(
      CellView.TRANSITION_DURATION,
      pane
    )

  private def handleClick(e: MouseEvent): Unit =
    clickHandler(element)

}
