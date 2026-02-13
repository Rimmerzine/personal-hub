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

  def findFirstNote: Future[Option[Note]] = {
    findOne()
  }

  def saveNote(note: Note): Future[Boolean] = {
    replace(equal("title", note.title), note)
  }

}
