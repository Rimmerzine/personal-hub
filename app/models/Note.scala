package models

import play.api.libs.json.{Json, OFormat}

case class Note(title: String, body: String)

object Note {
  implicit val format: OFormat[Note] = Json.format[Note]
}