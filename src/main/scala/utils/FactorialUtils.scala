package utils

import actors.messages._
import messages.CalculationResult

import scala.collection.convert.WrapAsJava._

/**
  * Created by ldrygala on 2015-10-03.
  */
object FactorialUtils {
 def multiplyRange(start: Int, end: Int) = (start to end).foldLeft(BigInt(1))(_ * _)

 def calculateFactorialWithStandardScalaWay(factorialToCalculate: Int) = {
   val start = System.currentTimeMillis()
   val factorial = (1 to factorialToCalculate).foldLeft(BigInt(1))(_ * _)
   val end = System.currentTimeMillis()
   CalculateResult(factorial, end - start)
 }

 def calculateFactorialWithStandardJavaWay(factorialToCalculate: Int) = {
   val start = System.currentTimeMillis()
   val factorial = (1 to factorialToCalculate).foldLeft(BigInt(1))(_ * _)
   val end = System.currentTimeMillis()
   new CalculationResult(factorial.bigInteger, end - start)
 }

 def createMultiplyRanges(calculateFactorial: CalculateFactorial) = (1 to calculateFactorial.factorialNumber).grouped(calculateFactorial.partSize).map(range => MultiplyRange(range.head, range.last))

 def createMultiplyRanges(calculateFactorial: messages.CalculateFactorial): java.util.List[messages.MultiplyRange] = (1 to calculateFactorial.factorialNumber).grouped(calculateFactorial.partSize).map(range => new messages.MultiplyRange(range.head, range.last)).toSeq
}
