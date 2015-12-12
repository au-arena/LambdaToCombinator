package com.app

import java.io.File


object Manager {

  def main (args: Array[String]){

    /**
     * Class used as CLI arguments
     * @param exp
     * @param in
     * @param out
     * @param verbose
     */
    case class Config(exp: Option[String] = None , in: Option[File] = None, out: Option[File] = None, verbose: Boolean = false)

    /**
     * CLI parser
     */
    val parser = new scopt.OptionParser[Config]("LambdaToCombinator") {

      val grammarDef = System.lineSeparator +
        "Note: The input grammar for lambda-expressions is the following:" + System.lineSeparator + System.lineSeparator +
        "EXP ::= VAR | '('EXP' 'EXP')' | '(''\\'VARLIST'.'EXP')'  " + System.lineSeparator +
        "VARLIST ::= VAR VARLIST0" + System.lineSeparator +
        "VARLIST0 ::= epsilon | ' 'VAR VARLIST0 " + System.lineSeparator

      head(System.lineSeparator+"LambdaToCombinator","1.0","(Program converting lambda-expressions to SKIBC-basis combinators)" + System.lineSeparator + grammarDef  )

      opt[String]('e', "exp") action {()
        (x, c) => c.copy(exp = Some(x))
      } text("exp: input a single lambda-expression")

      opt[File]('o', "out") valueName("<file>") action { (x, c) =>
        c.copy(out = Some(x)) } text("out: output the result to a file")

      opt[File]('i', "in") valueName("<file>") action { (x, c) =>
        c.copy(in = Some(x)) } text("in: input a set of lambda-expressions using a file (separated by a line break)") validate {
        x => if(x.isFile) success else failure("The following file is not valid:" + x)
      }

      help("help") text("Print the help menu")
    }


        // parser.parse returns Option[C]
        parser.parse(args, Config()) match {

          case Some(config) =>

            var convertedTermList = None :Option[List[String]]

            //if a single expression is passed as a command line argument
            if(config.exp.isDefined){

              try{

                val parsedTerm = TermIO.parse(config.exp.get)
                val convertedTerm = Convertor.convert( parsedTerm )
                convertedTermList = Some(List( convertedTerm.toSimplifiedString ) )//only one element

              }catch{
                case e:IllegalArgumentException => println(e.getMessage)
              }

            }
            //if expressions are passed using a file
            else{

              config.in match{

                case None => {
                  println("No source to read from")
                }

                case Some(x) => {

                  val expressionList:List[String] = TermIO.readExpression(config.in.get)

                  convertedTermList = Some(expressionList.map( x => {
                    try{
                      Convertor.convert( TermIO.parse(x) ).toSimplifiedString
                    }catch{
                      case e:IllegalArgumentException => e.getMessage
                    }

                  }))

                }
              }

            }

            // print out the results if present
            if(convertedTermList.isDefined){

              config.out match{

                case None => {
                  //write to console
                  TermIO.writeExpression( convertedTermList.get )
                }
                case Some(x) => {
                  //write to file
                  TermIO.writeExpression( convertedTermList.get, config.out.get )
                }
              }
            }


          case None =>
            println("Bad argument(s)")
        }

  }


}
