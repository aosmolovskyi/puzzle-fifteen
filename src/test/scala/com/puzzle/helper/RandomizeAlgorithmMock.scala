package com.puzzle.helper

class RandomizeAlgorithmMock(randomList: List[Int]) extends RandomizeAlgorithm {
  var counter: Int = 0

  override def nextInt(range: Int): Int = {
    val result = randomList(counter)

    counter = counter + 1

    result
  }
}

object RandomizeAlgorithmMock {
  def apply(randomList: List[Int]): RandomizeAlgorithmMock = new RandomizeAlgorithmMock(randomList)
}
