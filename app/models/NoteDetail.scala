package models

import play.api.libs.json.{Json, OFormat}

case class NoteDetail(title: String, body: String) {
  val bodySummary: String = body match {
    case text if text.length <= 150 => text.take(150)
    case text => text.take(147) + "..."
  }
}

object NoteDetail {
  implicit val format: OFormat[NoteDetail] = Json.format[NoteDetail]
}