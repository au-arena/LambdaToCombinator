import com.app._
import org.junit.Assert._
import org.junit.Test

class ConversionTest {

  @Test
  def testOKConversion1 = {

    val exp = Abs(Var("x"),Abs(Var("y"),Var("x")))

    val expConverted = Convertor.convert(exp)

    val expected = LambdaTerm.K

    assertNotNull(expConverted)
    assertEquals(expected, expConverted)

  }
}
