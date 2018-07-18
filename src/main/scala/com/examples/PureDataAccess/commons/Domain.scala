package com.examples.PureDataAccess.commons


object Domain {

  case class User(uid: Option[Int], name: String)

  case class Group(gid: Option[Int], name: String, city: String, must_approve: Boolean)

  case class Member(mid: Option[Int] = None, uid: Int, gid: Int)

  case class JoinRequest(jid: Option[Int] = None, uid: Int, gid: Int)

  type JoinResponse = Either[JoinRequest, Member]

}
