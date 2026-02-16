package models

import play.api.libs.json.{Json, OFormat}

case class Note(id: String, detail: NoteDetail)

object Note {
  implicit val format: OFormat[Note] = Json.format[Note]
}