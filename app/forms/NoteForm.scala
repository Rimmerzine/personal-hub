package forms

import models.Note
import play.api.data.Form
import play.api.data.Forms.{default, mapping, text}

object NoteForm {

  val titleKey: String = "note-title"
  val bodyKey: String = "note-body"

  val form: Form[Note] = Form(
    mapping(
      titleKey -> default(text, "").verifying("error.note.title.empty", _.nonEmpty),
      bodyKey -> default(text, "")
    )(Note.apply)(Note.unapply)
  )

}