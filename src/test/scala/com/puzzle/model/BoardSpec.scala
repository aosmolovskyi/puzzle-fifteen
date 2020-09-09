package com.puzzle.model

import com.puzzle.model.error.{DefaultBoardValidation, MissedCellWithCoordinates, MissedEmptyCell}
import org.specs2.matcher.Matchers
import org.specs2.mutable.Spec


class BoardSpec extends Spec with Matchers {

  "Board" should {
    "correctly creates" in {
      DefaultBoard.fifteenBoard
      success
    }

    "return exception when gridSize negative" in {
      DefaultBoard.createBoard(-1) must throwA[DefaultBoardValidation]
    }

    "return exception when cells size is wrong" in {
      DefaultBoard.apply(Set(DefaultCell(Coordinates(1, 1), 1), EmptyCell(Coordinates(1, 2))), 2) must throwA[DefaultBoardValidation]
    }

    "return exception when miss empty cell" in {
      DefaultBoard.apply(Set(DefaultCell(Coordinates(1, 1), 1), DefaultCell(Coordinates(1, 2), 2)), 2) must throwA[DefaultBoardValidation]
    }

    "have correct values when creates 2x2" in {
      val board = DefaultBoard.createBoard(2)
      val correctCells: Set[Cell] = Set(
        DefaultCell(Coordinates(1, 1), 1),
        DefaultCell(Coordinates(1, 2), 2),
        DefaultCell(Coordinates(2, 1), 3),
        EmptyCell(Coordinates(2, 2)))
      board.gridSize mustEqual 2
      board.cells mustEqual correctCells
    }

    "have correct values when creates 3x3" in {
      val board = DefaultBoard.createBoard(3)
      val correctCells: Set[Cell] = Set(
        DefaultCell(Coordinates(1, 1), 1),
        DefaultCell(Coordinates(1, 2), 2),
        DefaultCell(Coordinates(1, 3), 3),
        DefaultCell(Coordinates(2, 1), 4),
        DefaultCell(Coordinates(2, 2), 5),
        DefaultCell(Coordinates(2, 3), 6),
        DefaultCell(Coordinates(3, 1), 7),
        DefaultCell(Coordinates(3, 2), 8),
        EmptyCell(Coordinates(3, 3)),
      )
      board.gridSize mustEqual 3
      board.cells mustEqual correctCells
    }

    "correctly move cell" in {
      val board = DefaultBoard.createBoard(2)
      val correctCells: Set[Cell] = Set(
        DefaultCell(Coordinates(1, 1), 1),
        DefaultCell(Coordinates(1, 2), 2),
        DefaultCell(Coordinates(2, 2), 3),
        EmptyCell(Coordinates(2, 1)))

      board.move(Coordinates(2, 1)) mustEqual DefaultBoard(correctCells, 2)
    }

    "correctly move empty cell" in {
      val board = DefaultBoard.createBoard(2)
      val correctCells: Set[Cell] = Set(
        DefaultCell(Coordinates(1, 1), 1),
        DefaultCell(Coordinates(1, 2), 2),
        DefaultCell(Coordinates(2, 1), 3),
        EmptyCell(Coordinates(2, 2)))

      board.move(Coordinates(2, 2)) mustEqual DefaultBoard(correctCells, 2)
    }

    "correctly move when cell isn't close to empty" in {
      val board = DefaultBoard.createBoard(2)
      val correctCells: Set[Cell] = Set(
        DefaultCell(Coordinates(1, 1), 1),
        DefaultCell(Coordinates(1, 2), 2),
        DefaultCell(Coordinates(2, 1), 3),
        EmptyCell(Coordinates(2, 2)))

      board.move(Coordinates(1, 1)) mustEqual DefaultBoard(correctCells, 2)
    }

    "return exception when not correct coordinates" in {
      val board = DefaultBoard.createBoard(2)

      board.move(Coordinates(3, 2)) must throwA[MissedCellWithCoordinates]
    }

    "return exception when miss empty cell during move" in {
      val board = new DefaultBoard(Set(
        DefaultCell(Coordinates(1, 1), 1),
        DefaultCell(Coordinates(1, 2), 2),
        DefaultCell(Coordinates(2, 1), 3),
        DefaultCell(Coordinates(2, 2), 4)),
        2)

      board.move(Coordinates(2, 2)) must throwA[MissedEmptyCell.type]
    }
  }

}
