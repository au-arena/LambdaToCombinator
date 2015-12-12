
This program is a Scala implementation of the following hackerrank problem : [Down with abstraction](https://www.hackerrank.com/challenges/down-with-abstractions). It consists of a simple CLI tool that transforms [lambda expressions](https://en.wikipedia.org/wiki/Lambda_calculus) to corresponding [combinators](https://en.wikipedia.org/wiki/Combinatory_logic).


#Build
At the root of the project type:

```./gradlew generateJar``` (on linux)

```gradlew.bat generateJar``` (on windows)

This gradle task builds a standalone jar in `build/libs/`. 


#Execute

As the Scala library is embedded in the jar, it is possible to execute it using directly the following java command:

```java -jar build/libs/LambdaToCombinator.jar --help```

or by relying on a local Scala installation by typing:

```scala build/libs/LambdaToCombinator.jar --help```

Regardless of the way the program is called, if the --help option is passed, the following message will be printed:

```
LambdaToCombinator 1.0 (Program converting lambda-expressions to SKIBC-basis combinators)

Note: The input grammar for lambda-expressions is the following:

EXP ::= VAR | '('EXP' 'EXP')' | '(''\'VARLIST'.'EXP')' 
VARLIST ::= VAR VARLIST0
VARLIST0 ::= epsilon | ' 'VAR VARLIST0 

Usage: LambdaToCombinator [options]

  -e <value> | --exp <value>
        exp: input a single lambda-expression
  -o <file> | --out <file>
        out: output the result to a file
  -i <file> | --in <file>
        in: input a set of lambda-expressions using a file (separated by a line break)
  --help
        Print the help menu
```

For instance the following command will transform the input lambda expression to a combinator:

```
scala build/libs/LambdaToCombinator.jar -e "(\x.(\y.(y (\z.(\t.((z (\x.x)) x))))))"
B(CI)(B(BK)(C(CII)))
```


#Explanation

The program operates for each lambda-expression the following syntactic transformation `T[ ]:L -> C` where L is the set of lambda-terms and C that of combinatory terms, according to the following algorithm:

1. T[λx.(E x)] => T\[E\] (if x is not free in E)    [eta-reduction] 
2. T[x] => x
3. T[(E₁ E₂)] => (T[E₁] T[E₂])
4. T[λx.E] => (K T[E]) (if x is not free in E)
5. T[λx.x] => I
6. T[λx.λy.E] => T\[λx.T\[λy.E\]\] (if x is free in E)
7. T[λx.(E₁ E₂)] => (S T[λx.E₁] T[λx.E₂]) (if x is free in both E₁ and E₂)
8. T[λx.(E₁ E₂)] => (C T[λx.E₁] T[E₂]) (if x is free in E₁ but not E₂)
9. T[λx.(E₁ E₂)] => (B T[E₁] T[λx.E₂]) (if x is free in E₂ but not E₁)
