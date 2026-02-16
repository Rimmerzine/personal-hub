package forms

import models.NoteDetail
import org.scalatestplus.play.PlaySpec
import play.api.data.FormError

class NoteDetailFormSpec extends PlaySpec {

  "NoteForm" must {
    "produce a note with the form data" when {
      "bound with valid form data" in {
        val boundForm = NoteDetailForm.form.bind(Map(
          NoteDetailForm.titleKey -> "test-title",
          NoteDetailForm.bodyKey -> "test-body"
        ))

        boundForm.errors mustBe Seq.empty[FormError]
        boundForm.value mustBe Some(NoteDetail("test-title", "test-body"))
      }
    }
    "produce form errors" when {
      "bound with invalid form data" in {
        val boundForm = NoteDetailForm.form.bind(Map(
          NoteDetailForm.titleKey -> "",
          NoteDetailForm.bodyKey -> ""
        ))

        boundForm.value mustBe None
        boundForm.errors mustBe Seq(
          FormError(NoteDetailForm.titleKey, "error.note.title.empty")
        )
      }
      "bound with no form data" in {
        val boundForm = NoteDetailForm.form.bind(Map.empty[String, String])

        boundForm.value mustBe None
        boundForm.errors mustBe Seq(
          FormError(NoteDetailForm.titleKey, "error.note.title.empty")
        )
      }
    }
  }

}
