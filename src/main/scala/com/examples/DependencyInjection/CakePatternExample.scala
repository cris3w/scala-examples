package com.examples.DependencyInjection


// example based on a Jonas Boner's post:
// http://jonasboner.com/real-world-scala-dependency-injection-di/


case class User(username: String, password: String)


trait UserRepositoryComponent {

  val userRepository: UserRepository

  class UserRepository {

    def authenticate(user: User): User = {
      println(s"Authenticating user: ${user.username}")
      user
    }
  }
}


trait UserServiceComponent {

  // using self-type annotation to declare the dependencies this component requires
  this: UserRepositoryComponent =>

  class UserService {

    def authenticate(username: String, password: String): User =
      userRepository.authenticate(User(username, password))
  }
}


object ComponentRegistry
  extends UserServiceComponent with UserRepositoryComponent {

  val userRepository = new UserRepository
  val userService = new UserService
}


object CakePatternExample extends App {

  import ComponentRegistry._

  userService.authenticate("admin", "user")

}
