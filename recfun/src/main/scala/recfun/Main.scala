package recfun
import common._

import scala.annotation.tailrec

object Main {
  def main(args: Array[String]) {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row)
        print(pascal(col, row) + " ")
      println()
    }
  }

  /**
   * Exercise 1
   */
  def pascal(c: Int, r: Int): Int = {
    if( c == 0 || c == r ) 1 // base case: (c,r) is triangle edge
    else { pascal(c-1, r-1) + pascal(c, r-1) } // recursion: sum of two numbers from previous row
  }
  /**
   * Exercise 2
   */
  def balance(chars: List[Char]): Boolean = {
    @tailrec
    def check(chars: List[Char], count: Int): Boolean = {
      // base case: string is empty || count is below 0 (parens don't match)
      if (chars.isEmpty) count == 0
      else if (count < 0) false

      // recursion: count parentheses
      else {
        val head = chars.head
        if (head == '(') check(chars.tail, count + 1)
        else if (head == ')') check(chars.tail, count - 1)
        else check(chars.tail, count)
      }
    }
    check(chars, 0)
    // what if no parens in sentence..?
  }


  /**
   * Exercise 3
   */
  def countChange(money: Int, coins: List[Int]): Int = {
    // base case: money is exactly spent
    if( money == 0 ) 1
    else if( money < 0 || coins.isEmpty) 0 // money is below 0 or no coins left to use

    // recursion
    else {
      countChange(money - coins.head, coins) +  // use head coin
      countChange(money, coins.tail) // keep money, skip to next coin
    }
  }

}
