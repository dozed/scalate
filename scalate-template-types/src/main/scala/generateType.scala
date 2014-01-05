import org.fusesource.scalate.jade.{JadeParser, JadeCodeGenerator}
import org.fusesource.scalate.scaml.Attribute
import org.fusesource.scalate.TemplateSource

/**
 * Created by stefan on 05.01.14.
 */
object generateType extends App {

  // todo create function for template text via ast and macros

  def attributes(jadeSource: String): List[Attribute] = {
    val statements = (new JadeParser).parse(jadeSource)

    statements flatMap {
      case x: Attribute => Some(x)
      case _ => None
    }
  }

  def templateType(source: TemplateSource) = {
    attributes(source.text) foreach println
  }


  val source = TemplateSource.fromFile(getClass.getResource("/tpl1.jade").getFile)
  templateType(source)


}
