package repositories.utils

import org.bson.conversions.Bson
import org.mongodb.scala.model.Filters.empty
import org.mongodb.scala.model.ReplaceOptions
import org.mongodb.scala.{Document, MongoCollection}
import play.api.libs.json.{JsError, JsSuccess, Json, OFormat}

import scala.concurrent.{ExecutionContext, Future}

abstract class BaseMongoRepository[T](mongoComponent: MongoComponent, collectionName: String)
                                     (implicit format: OFormat[T], executionContext: ExecutionContext) {

  lazy val mongoCollection: MongoCollection[Document] = {
    mongoComponent
      .database
      .getCollection(collectionName)
  }

  def insert(entity: T): Future[T] = {
    val doc = Document(Json.stringify(Json.toJson(entity)))
    mongoCollection
      .insertOne(doc)
      .toFuture()
      .map(_ => entity)
  }

  def find(filter: Bson = empty(), projection: Option[Bson] = None): Future[Seq[T]] = {
    mongoCollection
      .find(filter)
      .projection(projection.getOrElse(empty()))
      .map(documentToModel)
      .toFuture()
  }

  def findOne(filter: Bson = empty(), projection: Option[Bson] = None): Future[Option[T]] =
    mongoCollection
      .find(filter)
      .projection(projection.getOrElse(empty()))
      .first()
      .toFutureOption()
      .map(_.map(documentToModel))

  def replace(filter: Bson, entity: T): Future[Boolean] = {
    val doc = Document(Json.stringify(Json.toJson(entity)))
    val options = new ReplaceOptions().upsert(true)
    mongoCollection
      .replaceOne(filter, doc, options)
      .toFuture()
      .map(_.wasAcknowledged)
  }

  def delete(filter: Bson): Future[Long] =
    mongoCollection
      .deleteMany(filter)
      .toFuture()
      .map(_.getDeletedCount)

  def count(filter: Bson): Future[Long] =
    mongoCollection
      .countDocuments(filter)
      .toFuture()

  private def documentToModel(document: Document): T = {
    Json.parse(document.toJson()).validate[T] match {
      case JsSuccess(value, _) => value
      case JsError(errors) => throw new RuntimeException(
        s"Failed to deserialize Mongo document: $errors"
      )
    }
  }

}
