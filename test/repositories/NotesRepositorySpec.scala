package repositories

import models.Note
import repositories.utils.RepositorySpec

class NotesRepositorySpec extends RepositorySpec {

  override def beforeEach(): Unit = {
    super.beforeEach()

    notesRepository.mongoCollection.drop().toFuture().futureValue
  }

  "findFirstNote" should {

    "return None when there are no notes in the collection" in {
      notesRepository.findFirstNote.futureValue mustBe None
    }

    "return the first inserted note when notes exist in the collection" in {
      notesRepository.insert(noteOne).futureValue
      notesRepository.insert(noteTwo).futureValue

      notesRepository.findFirstNote.futureValue mustBe Some(noteOne)
    }

  }

  "saveNote" should {

    "insert a new note when it does not exist" in {
      notesRepository.saveNote(noteOne).futureValue mustBe true
      notesRepository.findFirstNote.futureValue mustBe Some(noteOne)
    }

    "update an existing note with the same title" in {
      notesRepository.saveNote(noteOne).futureValue

      val updated = noteOne.copy(body = "updated-body")

      notesRepository.saveNote(updated).futureValue mustBe true
      notesRepository.findFirstNote.futureValue mustBe Some(updated)
    }

  }

  lazy val notesRepository: NotesRepository = application.injector.instanceOf[NotesRepository]

  lazy val noteOne: Note = Note("test-title-one", "test-body-one")
  lazy val noteTwo: Note = Note("test-title-two", "test-body-two")

}
