package com.puzzle.helper

import com.puzzle.model.error.RandomNumberLoverThenZero
import org.slf4j.LoggerFactory

import scala.util.Random

/**
 * Trait for number randomizer
 * Helps to create tests with pseudo random numbers
 */
trait RandomizeAlgorithm {
  /**
   * Random number generator
   *
   * @param range max number to be created, should be greater then 0
   * @return random int from 0 to range
   */
  def nextInt(range: Int): Int
}

class DefaultRandomizeAlgorithm extends RandomizeAlgorithm {
  private[this] val log = LoggerFactory.getLogger(this.getClass)

  def nextInt(range: Int): Int =
    if (range <= 0) {
      throw RandomNumberLoverThenZero
    } else
      Random.nextInt(range)
}

object DefaultRandomizeAlgorithm {
  def apply: DefaultRandomizeAlgorithm = new DefaultRandomizeAlgorithm()
}

