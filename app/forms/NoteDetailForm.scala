package forms

import models.{Note, NoteDetail}
import play.api.data.Form
import play.api.data.Forms.{default, mapping, text}

object NoteDetailForm {

  val titleKey: String = "note-title"
  val bodyKey: String = "note-body"

  val form: Form[NoteDetail] = Form(
    mapping(
      titleKey -> default(text, "").verifying("error.note.title.empty", _.nonEmpty),
      bodyKey -> default(text, "")
    )(NoteDetail.apply)(NoteDetail.unapply)
  )

}