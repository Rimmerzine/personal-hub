package controllers

import models.{Note, NoteDetail}
import play.api.mvc.Result
import play.api.test.Helpers._
import repositories.mocks.MockNotesRepository
import views.mocks.MockNotesHub

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class NotesHubControllerSpec extends BaseControllerSpec
  with MockNotesHub
  with MockNotesRepository {

  object TestNotesHubController extends NotesHubController(
    mockView, mockRepository
  )

  "show" must {
    "return OK with HTML content" when {
      "there were notes available in the collection" in {
        val notes: Seq[Note] = Seq(Note("test-id", NoteDetail("test-title", "test-body")))

        mockFindNotes(notes)
        mockNotesHub(notes)

        val result: Future[Result] = TestNotesHubController.show()(request)

        status(result) mustBe OK
        contentType(result) mustBe Some(HTML)
      }
      "there was not a note available in session" in {
        mockFindNotes(Seq.empty[Note])
        mockNotesHub(Seq.empty[Note])

        val result: Future[Result] = TestNotesHubController.show()(request)

        status(result) mustBe OK
        contentType(result) mustBe Some(HTML)
      }
    }
  }

}
