package com.app

/**
 * This class and its subclasses encode a lambda-term defined by the following grammar:
 * LT ::= x | \x.(LT) | (LT,LT) where x denotes a variables, \x.(LT) denotes a lambda-abstraction
 * and (LT,LT) denotes a lambda-application.
 */
sealed abstract class LambdaTerm{

  /**
   * This function retrieves the set of free variables for a given term (i.e. not bound by an abstraction operator)
   * @return
   */
  def getFreeVariables():Set[Var]

  override
  def toString:String = {

    this match {
      case LambdaTerm.I => "I"
      case LambdaTerm.K => "K"
      case LambdaTerm.S => "S"
      case LambdaTerm.C => "C"
      case LambdaTerm.B => "B"

      case Var(x) => x
      case App(e1, e2) => "(" + e1 + e2 + ")"
      case Abs(x, e) => "\\" + x + ".(" + e + ")"

    }

  }

  /**
   * Display the expression without unnecessary parenthesis (assuming left associativity)
   * @return
   */
  def toSimplifiedString:String = {

    if( !this.isCombinatoryTerm() )
      "The term is not a combinatory term"

    this match {
      case LambdaTerm.I => "I"
      case LambdaTerm.K => "K"
      case LambdaTerm.S => "S"
      case LambdaTerm.C => "C"
      case LambdaTerm.B => "B"

      case App(e1,e2:Abs) => e1.toSimplifiedString + e2.toSimplifiedString
      case App(e1,e2:App) => e1.toSimplifiedString + "(" + e2.toSimplifiedString + ")"

      case _ => ""
    }

  }

  /**
   * This function determines whether the lambda-term is also a combinatory term
   * (i.e. a term satisfying the following grammar: CT ::= x | C | (CT,CT) where x denotes a variable,
   * C a primitive combinator and (CT,CT) an application)
   * @return
   */
  def isCombinatoryTerm():Boolean = {

    this match {
      case LambdaTerm.I => true
      case LambdaTerm.K => true
      case LambdaTerm.S => true
      case LambdaTerm.C => true
      case LambdaTerm.B => true

      case e:Abs => false
      case e:Var => true
      case App(e1,e2) => e1.isCombinatoryTerm() && e2.isCombinatoryTerm()

      case _ => false
    }

  }

}

case class Var(value:String) extends LambdaTerm{
  override
  def getFreeVariables():Set[Var] = {
    Set(this)
  }
}

case class App(t1:LambdaTerm, t2:LambdaTerm) extends LambdaTerm{
  override
  def getFreeVariables():Set[Var] = {
    t1.getFreeVariables() ++ t2.getFreeVariables()
  }
}

case class Abs(x:Var, t:LambdaTerm) extends LambdaTerm{
  override
  def getFreeVariables():Set[Var] = {
    t.getFreeVariables() - x
  }
}

/**
 * Companion object containing a set of combinators
 */
object LambdaTerm {

  /**
   * The constant lambda-term I: \x.x
   */
  val I:Abs = Abs(Var("x"),Var("x"))

  /**
   * The constant lambda-term K: \x.\y.x
   */
  val K:Abs = Abs(Var("x"),Abs(Var("y"),Var("x")))

  /**
   * The constant lambda-term S: \x.\y.\z.xy(yz)
   */
  val S:Abs = Abs(Var("x"),Abs(Var("y"),Abs(Var("z"),App(App(Var("x"),Var("z")),App(Var("x"),Var("z"))) )))

  /**
   * The constant lambda-term C: \x.\y.\z.xzy
   */
  val C:Abs = Abs(Var("x"),Abs(Var("y"),Abs(Var("z"), App(App(Var("x"),Var("z")),Var("y")) )))

  /**
   * The constant lambda-term B: \x.\y.\z.x(yz)
   */
  val B:Abs = Abs(Var("x"),Abs(Var("y"),Abs(Var("z"), App(Var("x"),App(Var("y"),Var("z"))) )))


}
