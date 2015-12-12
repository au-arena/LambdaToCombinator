package com.app

import java.io.{BufferedWriter, File, FileWriter, IOException}

import scala.io.Source
import scala.util.parsing.combinator._

object TermIO {

  /**
   * Takes an expression as a string and parses it to get a [[LambdaTerm]]
   * @param exp
   * @return
   */
  def parse(exp:String):LambdaTerm = {

    LambdaTermParser.parseAll(LambdaTermParser.lambdaTerm, exp) match {

      case LambdaTermParser.Success(result, _) => result

      case LambdaTermParser.NoSuccess(msg, _) => {
        throw new IllegalArgumentException("Failed: " + msg)
      }
    }

  }

  /**
   * Write a list of expression (as string) to the console
   * @param terms
   */
  def writeExpression(terms:List[String]):Unit = {
    terms.foreach( x=> println(x) )
  }


  /**
   * Write a list of expression (as string) to a file
   * @param terms
   * @param file
   */
  def writeExpression(terms:List[String], file:File):Unit = {

    var bw = None : Option[BufferedWriter]

    try{

      bw = Some(new BufferedWriter(new FileWriter(file)) )

      terms.foreach( x => bw.get.write(x + System.lineSeparator) )

    }catch {
      case e: IOException => throw e
    }finally {
      if(bw.isDefined)
        bw.get.close()
    }
  }


  def readExpression(file:File):List[String] = {

      Source.fromFile(file).getLines().toList
  }

}

/**
 * Parser combinator to parse a lambda-expression written as a string and returning a [[com.app.LambdaTerm]]
 */
object LambdaTermParser extends RegexParsers {

  override val skipWhitespace = false

  def SPACES  = "[ \t]+".r

  def variable:Parser[Var] = """\w+""".r ^^ (x => Var(x))

  def lambdaTerm:Parser[LambdaTerm] = (

    variable |

    "("~lambdaTerm~" ".r~lambdaTerm~")" ^^ { case "("~e1~" "~e2~")" => App(e1,e2) } |

    "("~"\\"~varList~"."~lambdaTerm~")" ^^ {

      case "("~"\\"~v~"."~e~")" =>

        var exp = e

        v.reverse.foreach( x => exp = Abs(x,exp) )

        exp
    }
    )

  def varList:Parser[List[Var]] = variable~varList0 ^^ { case v~v0 => List(v) ++ v0 }

  def varList0:Parser[List[Var]] = (

    " "~variable~varList0 ^^ { case " "~v~v0 => List(v) ++ v0 } |

    "" ^^ ( x => List.empty[Var] )

    )

}
