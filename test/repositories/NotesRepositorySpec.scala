package repositories

import models.{Note, NoteDetail}
import repositories.utils.RepositorySpec

class NotesRepositorySpec extends RepositorySpec {

  override def beforeEach(): Unit = {
    super.beforeEach()

    notesRepository.mongoCollection.drop().toFuture().futureValue
  }

  "findNotes" should {
    "return all notes in the collection" when {
      "the collection does not contain any notes" in {
        notesRepository.findNotes().futureValue mustBe Seq.empty[Note]
      }
      "the collection contains notes" in {
        notesRepository.insert(noteOne).futureValue
        notesRepository.insert(noteTwo).futureValue

        notesRepository.findNotes().futureValue mustBe Seq(noteOne, noteTwo)
      }
    }
  }

  "findNote" should {
    "return None when there are no notes found with the id provided in the collection" in {
      notesRepository.findNote("test-id-one").futureValue mustBe None
    }
    "return a note when a matching note exists in the collection" in {
      notesRepository.insert(noteOne).futureValue
      notesRepository.insert(noteTwo).futureValue

      notesRepository.findNote("test-id-one").futureValue mustBe Some(noteOne)
      notesRepository.findNote("test-id-two").futureValue mustBe Some(noteTwo)
    }
  }

  "saveNote" should {

    "insert a new note when it does not exist" in {
      notesRepository.saveNote(noteOne).futureValue mustBe true
      notesRepository.findNote(noteOne.id).futureValue mustBe Some(noteOne)
    }

    "update an existing note with the same title" in {
      notesRepository.saveNote(noteOne).futureValue

      val updated = noteOne.copy(detail = noteOne.detail.copy(body = "updated-body"))

      notesRepository.saveNote(updated).futureValue mustBe true
      notesRepository.findNote(noteOne.id).futureValue mustBe Some(updated)
    }

  }

  lazy val notesRepository: NotesRepository = application.injector.instanceOf[NotesRepository]

  lazy val noteOne: Note = Note("test-id-one", NoteDetail("test-title-one", "test-body-one"))
  lazy val noteTwo: Note = Note("test-id-two", NoteDetail("test-title-two", "test-body-two"))

}
