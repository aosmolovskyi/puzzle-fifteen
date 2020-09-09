package com.puzzle.helper

import com.puzzle.model.error.{MissedCells, MissedEmptyCell}
import com.puzzle.model._
import org.specs2.matcher.Matchers
import org.specs2.mutable.Spec


class ShuffleAlgorithmSpec extends Spec with Matchers {

  "ShuffleAlgorithm" should {
    "return correct result in simple case" in {
      val board = DefaultBoard.createBoard(2)
      val shuffleAlgorithm = DefaultShuffleAlgorithm(RandomizeAlgorithmMock(List(2, 1, 2, 3)))
      val correctCells: Set[Cell] = Set(
        DefaultCell(Coordinates(1, 1), 1),
        DefaultCell(Coordinates(2, 2), 2),
        DefaultCell(Coordinates(2, 1), 3),
        EmptyCell(Coordinates(1, 2)))

      shuffleAlgorithm.shuffleBoard(board) mustEqual DefaultBoard(correctCells, 2)
    }


    "return exception when empty cell missing" in {
      val board = new DefaultBoard(Set(
        DefaultCell(Coordinates(1, 1), 1),
        DefaultCell(Coordinates(1, 2), 2),
        DefaultCell(Coordinates(2, 1), 3),
        DefaultCell(Coordinates(2, 2), 4)),
        2)
      val shuffleAlgorithm = DefaultShuffleAlgorithm(RandomizeAlgorithmMock(List(0, 1, 2, 3)))


      shuffleAlgorithm.shuffleBoard(board) must throwA[MissedCells]

    }

    "return correct result for solvability on 2x2 case" in {
      val board = new DefaultBoard(Set(
        DefaultCell(Coordinates(1, 1), 1),
        DefaultCell(Coordinates(1, 2), 2),
        DefaultCell(Coordinates(2, 1), 3),
        EmptyCell(Coordinates(2, 2))),
        2)
      val shuffleAlgorithm = DefaultShuffleAlgorithm(RandomizeAlgorithmMock(List(0, 1, 2, 3)))

      shuffleAlgorithm.isBoardSolvable(board) mustEqual true
    }

    "return correct result for solvability on 2x2 case when could not be solved" in {
      val board = new DefaultBoard(Set(
        DefaultCell(Coordinates(1, 2), 1),
        DefaultCell(Coordinates(2, 1), 2),
        DefaultCell(Coordinates(2, 2), 3),
        EmptyCell(Coordinates(1, 1))),
        2)

      val shuffleAlgorithm = DefaultShuffleAlgorithm(RandomizeAlgorithmMock(List(0, 1, 2, 3)))

      shuffleAlgorithm.isBoardSolvable(board) mustEqual false
    }

    "return correct result for solvability on 3x3 case" in {
      val board = new DefaultBoard(Set(
        DefaultCell(Coordinates(1, 1), 1),
        DefaultCell(Coordinates(1, 2), 2),
        DefaultCell(Coordinates(1, 3), 3),
        DefaultCell(Coordinates(2, 1), 4),
        DefaultCell(Coordinates(2, 2), 5),
        DefaultCell(Coordinates(2, 3), 6),
        DefaultCell(Coordinates(3, 1), 7),
        DefaultCell(Coordinates(3, 3), 8),
        EmptyCell(Coordinates(3, 2)),
      ), 3)
      val shuffleAlgorithm = DefaultShuffleAlgorithm(RandomizeAlgorithmMock(List(0, 1, 2, 3)))

      shuffleAlgorithm.isBoardSolvable(board) mustEqual true
    }

    "return correct result for solvability on 3x3 case when could not be solved" in {
      val board = new DefaultBoard(Set(
        DefaultCell(Coordinates(1, 1), 8),
        DefaultCell(Coordinates(1, 2), 1),
        DefaultCell(Coordinates(1, 3), 2),
        DefaultCell(Coordinates(3, 3), 5),
        DefaultCell(Coordinates(2, 2), 4),
        DefaultCell(Coordinates(2, 3), 3),
        DefaultCell(Coordinates(3, 1), 7),
        DefaultCell(Coordinates(3, 2), 6),
        EmptyCell(Coordinates(2, 1)),
      ), 3)
      val shuffleAlgorithm = DefaultShuffleAlgorithm(RandomizeAlgorithmMock(List(0, 1, 2, 3)))

      shuffleAlgorithm.isBoardSolvable(board) mustEqual false
    }

    "return exception when empty cell missing in solvability check" in {
      val board = new DefaultBoard(Set(
        DefaultCell(Coordinates(1, 1), 1),
        DefaultCell(Coordinates(1, 2), 2),
        DefaultCell(Coordinates(2, 1), 3),
        DefaultCell(Coordinates(2, 2), 4)),
        2)
      val shuffleAlgorithm = DefaultShuffleAlgorithm(RandomizeAlgorithmMock(List(0, 1, 2, 3)))


      shuffleAlgorithm.isBoardSolvable(board) must throwA[MissedEmptyCell.type]

    }
  }

}
