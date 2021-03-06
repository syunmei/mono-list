package controllers

import jp.t2v.lab.play2.auth.AuthConfig
import play.api.Logger
import play.api.mvc.Results._
import play.api.mvc.{ RequestHeader, Result }
import services.UserService

import scala.concurrent.{ ExecutionContext, Future }
import scala.reflect._

trait AuthConfigSupport extends AuthConfig {

  // ユーザーを識別するためのIDの型
  override type Id = String

  // ユーザーを表す型
  override type User = models.User

  //認可のために使われる型(今回は利用しない)
  override type Authority = Nothing

  override implicit val idTag: ClassTag[Id] = classTag[Id]

  val userService: UserService

  override def sessionTimeoutInSeconds: Int = 3600

  //　ログインセッションを保存するためのトークンアクセッサ
  override lazy val tokenAccessor = new RememberMeTokenAccessor(sessionTimeoutInSeconds)

  override def resolveUser(id: String)(implicit context: ExecutionContext): Future[Option[User]] =
    Future {
      userService.findByEmail(id).get
    }

  override def loginSucceeded(request: RequestHeader)(implicit context: ExecutionContext): Future[Result] =
    Future.successful(
      Redirect(routes.HomeController.index())
    )

  override def logoutSucceeded(request: RequestHeader)(implicit context: ExecutionContext): Future[Result] =
    Future.successful {
      Redirect(routes.HomeController.index())
    }

  override def authenticationFailed(request: RequestHeader)(implicit context: ExecutionContext): Future[Result] =
    Future.successful(
      Redirect(routes.AuthController.index())
    )

  override def authorizationFailed(request: RequestHeader, user: User, authority: Option[Nothing])(
      implicit context: ExecutionContext
  ): Future[Result] = Future.successful(
    Forbidden("no permission")
  )

  override def authorize(user: User, authority: Nothing)(implicit context: ExecutionContext): Future[Boolean] =
    Future.successful {
      true
    }
}
