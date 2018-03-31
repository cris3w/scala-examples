package com.examples.SideEffects.PureDataAccess.FromScratch.pure

import com.examples.SideEffects.PureDataAccess.commons.Domain._


trait Store[F[_]] {

  def putUser(user: User): F[Int]

  def putGroup(group: Group): F[Int]

  def getGroup(gid: Int): F[Group]

  def getUser(uid: Int): F[User]

  def putJoin(join: JoinRequest): F[JoinRequest]

  def putMember(member: Member): F[Member]

  // combinators

  def doAndThen[A, B](f: F[A])(next: A => F[B]): F[B]

  def returns[A](a: A): F[A]

  // derived combinators

  def map[A,B](f: F[A])(m: A => B): F[B] = doAndThen(f)(m andThen returns)

  def left[X, Y](x: F[X]): F[Either[X, Y]] = map[X,Either[X, Y]](x)(Left(_))

  def right[X, Y](y: F[Y]): F[Either[X, Y]] = map[Y,Either[X, Y]](y)(Right(_))

  def cond[X, Y](test: => Boolean,
                 x: => F[X],
                 y: => F[Y]): F[Either[X, Y]] =
    if (test) left(x) else right(y)
}


object Store {

  object Syntax {

    def putUser[F[_]](user: User)(implicit S: Store[F]): F[Int] = S.putUser(user)

    def putGroup[F[_]](group: Group)(implicit S: Store[F]): F[Int] = S.putGroup(group)

    def getGroup[F[_]](gid: Int)(implicit S: Store[F]): F[Group] = S.getGroup(gid)

    def getUser[F[_]](uid: Int)(implicit S: Store[F]): F[User] = S.getUser(uid)

    def putJoin[F[_]](join: JoinRequest)(implicit S: Store[F]): F[JoinRequest] = S.putJoin(join)

    def putMember[F[_]](member: Member)(implicit S: Store[F]): F[Member] = S.putMember(member)

    // combinators

    implicit class StoreOps[F[_], A](fa: F[A])(implicit S: Store[F]) {

      def flatMap[B](f: A => F[B]): F[B] = S.doAndThen(fa)(f)

      def map[B](f: A => B): F[B] = S.doAndThen(fa)(f andThen S.returns)
    }

    def cond[F[_], X, Y](test: => Boolean,
                         left: => F[X],
                         right: => F[Y])(implicit S: Store[F]): F[Either[X, Y]] = S.cond(test, left, right)

    def left[F[_], X, Y](x: F[X])(implicit S: Store[F]): F[Either[X, Y]] = S.left(x)

    def right[F[_], X, Y](y: F[Y])(implicit S: Store[F]): F[Either[X, Y]] = S.right(y)
  }

}
