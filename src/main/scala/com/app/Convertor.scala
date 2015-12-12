package com.app


object Convertor {

  /**
   * Convert a lambda-term to a combinatory term
   * @param term
   * @return
   */
  def convert(term:LambdaTerm):LambdaTerm = {

    /**
     * Helper function to test whether a variable is free in a lambda-term
     * @param x the variable
     * @param t the lambda-term
     * @return
     */
    def isFreeIn(x:Var, t:LambdaTerm):Boolean = {
      t.getFreeVariables().contains(x)
    }

    term match {
      case LambdaTerm.B => LambdaTerm.B
      case LambdaTerm.C => LambdaTerm.C
      case LambdaTerm.I => LambdaTerm.I
      case LambdaTerm.K => LambdaTerm.K
      case LambdaTerm.S => LambdaTerm.S

      case Abs(x,App(e,y)) if( x==y && !isFreeIn(x,e) ) => convert(e) //eta-reduction
      case Var(x) => Var(x)
      case App(e1,e2) => App(convert(e1), convert(e2))
      case Abs(x,e) if !isFreeIn(x,e) => App(LambdaTerm.K,convert(e))
      case Abs(x1,x2) if x1==x2 => LambdaTerm.I
      case Abs(x,Abs(y,e)) if isFreeIn(x,e) => convert(Abs(x,convert(Abs(y,e))))
      case Abs(x,App(e1,e2)) if isFreeIn(x,e1) && isFreeIn(x,e2)  => App(App(LambdaTerm.S,convert(Abs(x,e1))),convert(Abs(x,e2)))
      case Abs(x,App(e1,e2)) if isFreeIn(x,e1) && !isFreeIn(x,e2) => App(App(LambdaTerm.C,convert(Abs(x,e1))),convert(e2))
      case Abs(x,App(e1,e2)) if !isFreeIn(x,e1) && isFreeIn(x,e2) => App(App(LambdaTerm.B,convert(e1)),convert(Abs(x,e2)))

      case _ => term
    }

  }



}
