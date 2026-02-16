package repositories

import models.Note
import org.mongodb.scala.model.Filters.equal
import repositories.utils.{BaseMongoRepository, MongoComponent}

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class NotesRepository @Inject()(mongoComponent: MongoComponent)
                               (implicit executionContext: ExecutionContext) extends BaseMongoRepository[Note](
  mongoComponent = mongoComponent,
  collectionName = "notes"
) {

  def findNotes(): Future[Seq[Note]] = find()

  def findNote(id: String): Future[Option[Note]] = {
    findOne(equal("id", id))
  }

  def saveNote(note: Note): Future[Boolean] = {
    replace(equal("id", note.id), note)
  }

}
