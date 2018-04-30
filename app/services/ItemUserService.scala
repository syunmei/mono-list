package services

import models.Item
import scalikejdbc.{ AutoSession, DBSession }

import scala.util.Try

trait ItemUserService {

  def want(userId: Long, itemId: Long): Boolean

  def doNotWant(userId: Long, itemId: Long): Boolean

  def have(userId: Long, itemId: Long): Boolean

  def doNotHave(userId: Long, itemId: Long): Boolean

  def getItemsByUserId(userId: Long)(implicit dbSession: DBSession = AutoSession): Try[Seq[Item]]

}
