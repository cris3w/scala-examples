package com.examples.SideEffects.PureDataAccess.UsingMonad.impure

import scalaz.Id.Id

import com.examples.SideEffects.PureDataAccess.commons.Domain._
import com.examples.SideEffects.PureDataAccess.UsingMonad.pure._


// data access impl (slick, nosql...)

object IdStore extends Store[Id] {

  // store operators

  def putUser(user: User): Int = 1

  def putGroup(group: Group): Int = 1

  def getGroup(gid: Int): Group = Group(Some(gid), "github", "Madrid", true)

  def getUser(uid: Int): User = User(Some(uid), "cris")

  def putJoin(join: JoinRequest): JoinRequest = join.copy(jid = Some(1))

  def putMember(member: Member): Member = member.copy(mid = Some(1))

  // composition operators

  def returns[A](a: A): A = a

  def doAndThen[A, B](fa: A)(f: A => B): B = f(fa)
}
