package controllers

import org.mockito.ArgumentMatchers
import org.mockito.Mockito.{reset, when}
import org.scalatest.BeforeAndAfterEach
import org.scalatestplus.mockito.MockitoSugar
import play.api.mvc.Result
import play.api.test.Helpers._
import scalatags.Text.all.html
import views.Home

import scala.concurrent.Future

class HomeControllerSpec extends BaseControllerSpec with MockitoSugar with BeforeAndAfterEach {

  val mockView: Home = mock[Home]

  override def beforeEach(): Unit = {
    super.beforeEach()
    reset(mockView)
  }

  object TestHomeController extends HomeController(
    mockView
  )

  "show" must {
    "return OK with HTML content" in {
      when(mockView()(ArgumentMatchers.any())).thenReturn(html())

      val result: Future[Result] = TestHomeController.show()(request)

      status(result) mustBe OK
      contentType(result) mustBe Some(HTML)
    }
  }

}
