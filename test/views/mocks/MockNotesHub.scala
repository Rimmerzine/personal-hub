package views.mocks

import models.Note
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.{reset, when}
import org.scalatest.{BeforeAndAfterEach, Suite}
import org.scalatestplus.mockito.MockitoSugar
import play.api.data.Form
import scalatags.Text.all.html
import views.NotesHub

trait MockNotesHub extends MockitoSugar with BeforeAndAfterEach {
  suite: Suite =>

  override def beforeEach(): Unit = {
    super.beforeEach()
    reset(mockView)
  }

  val mockView: NotesHub = mock[NotesHub]

  def mockNotesHub(form: Form[Note]): Unit = {
    when(mockView(ArgumentMatchers.eq(form))(ArgumentMatchers.any(), ArgumentMatchers.any()))
      .thenReturn(html())
  }

}
