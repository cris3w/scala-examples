package com.examples.SideEffects.PureDataAccess.UsingMonad.pure

import com.examples.SideEffects.PureDataAccess.commons.Domain._


object ServicesWithSomeSugar {
  import Monad.Syntax._, Store.Syntax._

  def join[F[_]: Monad: Store](request: JoinRequest): F[JoinResponse] =
    getUser(request.uid) flatMap { _ =>
      getGroup(request.gid) flatMap { group =>
        if (group.must_approve) putJoin(request) map (Left(_))
        else putMember(Member(None, request.uid, request.gid)) map (Right(_))
      }
    }
}


object Services {
  import Monad.Syntax._, Store.Syntax._

  def join[F[_]: Monad: Store](request: JoinRequest): F[JoinResponse] =
    for {
      _ <- getUser(request.uid)
      group <- getGroup(request.gid)
      result <- cond(group.must_approve)(
        left = putJoin(request),
        right = putMember(Member(None, request.uid, request.gid)))
    } yield result
}
