/**
  * Created by ldrygala on 2015-10-04.
  */

package actors.messages

case class CalculateFactorial(val factorialNumber: Int, val partSize: Int)
case class MultiplyRange(val start: Int, val end: Int)
case class CalculateResult(val result: BigInt, val executionTime: Long = 0)
