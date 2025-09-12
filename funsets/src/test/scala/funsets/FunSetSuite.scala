package funsets

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

/**
 * This class is a test suite for the methods in object FunSets. To run
 * the test suite, you can either:
 *  - run the "test" command in the SBT console
 *  - right-click the file in eclipse and chose "Run As" - "JUnit Test"
 */
@RunWith(classOf[JUnitRunner])
class FunSetSuite extends FunSuite {


  /**
   * Link to the scaladoc - very clear and detailed tutorial of FunSuite
   *
   * http://doc.scalatest.org/1.9.1/index.html#org.scalatest.FunSuite
   *
   * Operators
   *  - test
   *  - ignore
   *  - pending
   */

  /**
   * Tests are written using the "test" operator and the "assert" method.
   */
  test("string take") {
    val message = "hello, world"
    assert(message.take(5) == "hello")
  }

  /**
   * For ScalaTest tests, there exists a special equality operator "===" that
   * can be used inside "assert". If the assertion fails, the two values will
   * be printed in the error message. Otherwise, when using "==", the test
   * error message will only say "assertion failed", without showing the values.
   *
   * Try it out! Change the values so that the assertion fails, and look at the
   * error message.
   */
  test("adding ints") {
    assert(1 + 2 === 3)
  }

  
  import FunSets._

  test("contains is implemented") {
    assert(contains(x => true, 100))
  }
  
  /**
   * When writing tests, one would often like to re-use certain values for multiple
   * tests. For instance, we would like to create an Int-set and have multiple test
   * about it.
   * 
   * Instead of copy-pasting the code for creating the set into every test, we can
   * store it in the test class using a val:
   * 
   *   val s1 = singletonSet(1)
   * 
   * However, what happens if the method "singletonSet" has a bug and crashes? Then
   * the test methods are not even executed, because creating an instance of the
   * test class fails!
   * 
   * Therefore, we put the shared values into a separate trait (traits are like
   * abstract classes), and create an instance inside each test method.
   * 
   */

  trait TestSets {
    val s1 = singletonSet(1)
    val s2 = singletonSet(2)
    val s3 = singletonSet(3)
  }

  /**
   * This test is currently disabled (by using "ignore") because the method
   * "singletonSet" is not yet implemented and the test would fail.
   * 
   * Once you finish your implementation of "singletonSet", exchange the
   * function "ignore" by "test".
   */
  test("singletonSet(1) contains 1") {
    
    /**
     * We create a new instance of the "TestSets" trait, this gives us access
     * to the values "s1" to "s3". 
     */
    new TestSets {
      /**
       * The string argument of "assert" is a message that is printed in case
       * the test fails. This helps identifying which assertion failed.
       */
      assert(contains(s1, 1), "Singleton")
    }
  }

  test("union contains all elements") {
    new TestSets {
      val s = union(s1, s2)

      println("The given sets are: ")
      printSet(s1)
      printSet(s2)

      print("The result of union is set is: ")
      printSet(s)

      assert(contains(s, 1), "Union 1")
      assert(contains(s, 2), "Union 2")
      assert(!contains(s, 3), "Union 3")
    }
  }

  test("intersect returns only common elements") {
    new TestSets {
      val s = union(s1, s2)    // {1, 2}
      val r = union(s1, s3)    // {1, 3}
      val i = intersect(s, r)  // {1}

      println("The given sets are: ")
      printSet(s)
      printSet(r)

      print("The result of intersect is: ")
      printSet(i)

      assert(contains(i, 1), "Intersect should contain 1")
      assert(!contains(i, 2), "Intersect should not contain 2")
      assert(!contains(i, 3), "Intersect should not contain 3")
    }
  }

  test("diff return elements in the first set that's not in the other set") {
    new TestSets {
      val s = union(s1, s2)   // {1, 2}
      val r = union(s1, s3)   // {1, 3}

      println("The given sets are: ")
      printSet(s)
      printSet(r)

      val d = diff(s, r)      // {2}

      print("The result of diff is: ")
      printSet(d)

      assert(!contains(d, 1), "1 is not the difference")
      assert(contains(d, 2), "2 is the difference")
      assert(!contains(d, 3), "3 is not the difference")
    }
  }

  test("filter return elements that satisfy the predicate") {
    new TestSets {
      val s = union(s2, s3)    // {2, 3}
      val isEven: Int => Boolean = x => x % 2 == 0

      println("predicate: the number is even")
      println("The given set is: ")
      printSet(s)
      println("predicate: the number is even")

      val f = filter(s, isEven)    // {2}
      print("The result of filter is: ")
      printSet(f)

      assert(contains(f, 2), "2 should be included after filtering")
      assert(!contains(f, 3), "3 should not be included after filtering")
    }
  }

  test("forall should return true if all elements satisfy the predicate") {
    new TestSets {
      val s = union(s2, s3)    // {2, 3}

      println("The given set is: ")
      printSet(s)

      assert(!forall(s, x => x % 2 == 0), "not all elements are even (3 fails)")
      assert(forall(s2, x => x % 2 == 0), "all elements in {2} are even")
    }
  }

  test("exists should return true if at least one element satisfies the predicate") {
    new TestSets {
      val s = union(s2, s3)    // {2, 3}

      println("The given set is: ")
      printSet(s)

      assert(exists(s, x => x % 2 == 0), "there exists an even element")
      assert(exists(s, x => x % 2 != 0), "there exists an odd element")
      assert(!exists(s, x => x > 10), "no element greater than 10 exists")
    }
  }

  test("map should modify all elements in the set") {
    new TestSets {
      val s = union(s2, s3)   // {2, 3}


      println("The given set is: ")
      printSet(s)
      println("all elements are doubled for map operation")

      val m = map(s, x => x * 2)    // {4, 6}

      print("The result of map is: ")
      printSet(m)

      assert(contains(m, 4), "4 should be in the mapped set")
      assert(contains(m, 6), "6 should be in the mapped set")
      assert(!contains(m, 2), "2 should not be in the mapped set")
      assert(!contains(m, 3), "3 should not be in the mapped set")
    }
  }

}
