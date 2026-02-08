package controllers

import org.mockito.ArgumentMatchers
import org.mockito.Mockito.{reset, when}
import org.scalatest.BeforeAndAfterEach
import org.scalatestplus.mockito.MockitoSugar
import play.api.mvc.Result
import play.api.test.Helpers._
import scalatags.Text.all.html
import views.NotesHub

import scala.concurrent.Future

class NotesHubControllerSpec extends BaseControllerSpec with MockitoSugar with BeforeAndAfterEach {

  val mockView: NotesHub = mock[NotesHub]

  override def beforeEach(): Unit = {
    super.beforeEach()
    reset(mockView)
  }

  object TestNotesHubController extends NotesHubController(
    mockView
  )

  "NotesHubController.show" must {
    "return OK with HTML content" in {
      when(mockView()(ArgumentMatchers.any())).thenReturn(html())

      val result: Future[Result] = TestNotesHubController.show()(request)

      status(result) mustBe OK
      contentType(result) mustBe Some(HTML)
    }
  }

  "NotesHubController.submit" must {
    "return SEE_OTHER and redirect to the notes hub url" in {
      val result = TestNotesHubController.submit()(request)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(controllers.routes.NotesHubController.show().url)
    }
  }

}
