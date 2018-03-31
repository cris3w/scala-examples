package com.examples.SideEffects.PureDataAccess.FromScratch.pure

import com.examples.SideEffects.PureDataAccess.commons.Domain._


sealed abstract class StoreProgram[A]

case class PutUser(user: User) extends StoreProgram[Int]

case class PutGroup(group: Group) extends StoreProgram[Int]

case class GetUser(id: Int) extends StoreProgram[User]

case class GetGroup(id: Int) extends StoreProgram[Group]

case class PutJoin(join: JoinRequest) extends StoreProgram[JoinRequest]

case class PutMember(member: Member) extends StoreProgram[Member]

case class Sequence[U, V](program: StoreProgram[U], next: U => StoreProgram[V]) extends StoreProgram[V]

case class Returns[U](value: U) extends StoreProgram[U]


object StoreProgram {

  implicit object StoreDeep extends Store[StoreProgram] {

    // store operators

    def putUser(user: User): StoreProgram[Int] = PutUser(user)

    def putGroup(group: Group): StoreProgram[Int] = PutGroup(group)

    def getGroup(gid: Int): StoreProgram[Group] = GetGroup(gid)

    def getUser(uid: Int): StoreProgram[User] = GetUser(uid)

    def putJoin(join: JoinRequest): StoreProgram[JoinRequest] = PutJoin(join)

    def putMember(member: Member): StoreProgram[Member] = PutMember(member)

    // monad operators

    def returns[A](a: A): StoreProgram[A] = Returns(a)

    def doAndThen[A, B](fa: StoreProgram[A])(f: A => StoreProgram[B]): StoreProgram[B] = Sequence(fa, f)
  }

}
