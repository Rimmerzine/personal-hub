package views.mocks

import org.mockito.ArgumentMatchers
import org.mockito.Mockito.{reset, when}
import org.scalatest.{BeforeAndAfterEach, Suite}
import org.scalatestplus.mockito.MockitoSugar
import play.api.data.Form
import play.api.mvc.Call
import scalatags.Text.all.html
import views.CreateNote

trait MockCreateNote extends MockitoSugar with BeforeAndAfterEach {
  suite: Suite =>

  override def beforeEach(): Unit = {
    super.beforeEach()
    reset(mockView)
  }

  val mockView: CreateNote = mock[CreateNote]

  def mockCreateNote(postAction: Call, form: Form[_]): Unit = {
    when(mockView(
      ArgumentMatchers.eq(postAction),
      ArgumentMatchers.eq(form)
    )(ArgumentMatchers.any(), ArgumentMatchers.any()))
      .thenReturn(html())
  }

}
