package forms

import models.Note
import org.scalatestplus.play.PlaySpec
import play.api.data.FormError

class NoteFormSpec extends PlaySpec {

  "NoteForm" must {
    "produce a note with the form data" when {
      "bound with valid form data" in {
        val boundForm = NoteForm.form.bind(Map(
          NoteForm.titleKey -> "test-title",
          NoteForm.bodyKey -> "test-body"
        ))

        boundForm.errors mustBe Seq.empty[FormError]
        boundForm.value mustBe Some(Note("test-title", "test-body"))
      }
    }
    "produce form errors" when {
      "bound with invalid form data" in {
        val boundForm = NoteForm.form.bind(Map(
          NoteForm.titleKey -> "",
          NoteForm.bodyKey -> ""
        ))

        boundForm.value mustBe None
        boundForm.errors mustBe Seq(
          FormError(NoteForm.titleKey, "error.note.title.empty")
        )
      }
      "bound with no form data" in {
        val boundForm = NoteForm.form.bind(Map.empty[String, String])

        boundForm.value mustBe None
        boundForm.errors mustBe Seq(
          FormError(NoteForm.titleKey, "error.note.title.empty")
        )
      }
    }
  }

}
