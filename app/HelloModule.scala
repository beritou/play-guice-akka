package modules

import akka.actor.{Actor, ActorSystem, Props}
import com.google.inject.{AbstractModule, ImplementedBy, Inject}
import com.google.inject.name.Names
import modules.HelloActor.SayHello

class HelloModule extends AbstractModule {
  def configure() = bind(classOf[Hello]).annotatedWith(Names.named("en")).to(classOf[AkkaHello])
}

@ImplementedBy(classOf[AkkaHello])
trait Hello {
  def sendMessageToAkka(message: String): Unit
}

class AkkaHello @Inject()(system: ActorSystem) extends Hello {
  def sendMessageToAkka(message: String): Unit = {
    val helloActor = system.actorOf(HelloActor.props)
    helloActor ! SayHello(message)
  }
}

class HelloActor extends Actor {

  import HelloActor._

  def receive = {
    case SayHello(message: String) => println(message)
  }
}

object HelloActor {
  def props = Props[HelloActor]

  case class SayHello(name: String)

}
