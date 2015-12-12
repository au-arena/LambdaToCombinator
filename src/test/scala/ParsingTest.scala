import com.app.{Abs, App, TermIO, Var}
import org.junit.Assert._
import org.junit.Test

class ParsingTest {

  @Test
  def testOKparsing1() = {

    val exp = "(\\x.(\\y.(y (\\z.(\\t.((z (\\x.x)) x))))))"

    val expParsed = TermIO.parse(exp)

    val expected = Abs(Var("x"),
                      Abs(Var("y"),
                        App(Var("y"),
                          Abs(Var("z"),
                            Abs(Var("t"),
                              App( App(Var("z"),
                                Abs(Var("x"),
                                  Var("x"))),
                                Var("x")
                              )
                            )
                          )
                        )
                      )
                    )

    assertNotNull(expParsed)
    assertEquals(expected, expParsed)

  }

  @Test
  def testOKparsing2() = {

    val exp = "(\\x.(\\y.x))"

    val expParsed = TermIO.parse(exp)

    val expected = Abs(Var("x"),Abs(Var("y"),Var("x")))

    assertNotNull(expParsed)
    assertEquals(expected, expParsed)

  }


}
