package repositories.mocks

import models.Note
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.{reset, when}
import org.scalatest.{BeforeAndAfterEach, Suite}
import org.scalatestplus.mockito.MockitoSugar
import repositories.NotesRepository

import scala.concurrent.Future

trait MockNotesRepository extends MockitoSugar with BeforeAndAfterEach {
  suite: Suite =>

  override def beforeEach(): Unit = {
    super.beforeEach()
    reset(mockRepository)
  }

  val mockRepository: NotesRepository = mock[NotesRepository]

  def mockFindNotes(notes: Seq[Note]): Unit = {
    when(mockRepository.findNotes())
      .thenReturn(Future.successful(notes))
  }

  def mockFindNote(id: String)(result: Option[Note]): Unit = {
    when(mockRepository.findNote(ArgumentMatchers.eq(id)))
      .thenReturn(Future.successful(result))
  }

  def mockSaveNote(note: Note): Unit = {
    when(mockRepository.saveNote(ArgumentMatchers.eq(note)))
      .thenReturn(Future.successful(true))
  }

}
