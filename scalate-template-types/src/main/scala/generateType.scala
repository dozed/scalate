import java.io.File
import org.fusesource.scalate.jade.JadeParser
import org.fusesource.scalate.scaml.Attribute
import org.fusesource.scalate.{TemplateEngine, TemplateSource}
import scala.collection.mutable.ListBuffer
import scala.reflect.macros.{Universe, Context}

import language.experimental.macros

object generateType {

  val path = new File(getClass.getResource("/").getFile)
  val te = new TemplateEngine(Some(path))

  def attributes(jadeSource: String): List[Attribute] = {
    val statements = (new JadeParser).parse(jadeSource)

    statements flatMap {
      case x: Attribute => Some(x)
      case _ => None
    }
  }

  def templateType(tpl: String): AnyRef = macro templateType_impl

  def templateType_impl(c: Context)(tpl: c.Expr[String]): c.Expr[AnyRef] = {
    import c.universe._

    // construct list of parameters and refs
    val valParams = ListBuffer[ValDef]()
    val mapEntries = ListBuffer[Tree]()

    // extract filename string literal
    val Literal(Constant(fileName: String)) = tpl.tree
    // TODO load resource
    val source = TemplateSource.fromFile(fileName)

    // build list of params and map entries
    attributes(source.text) foreach {
      a =>
        val name = newTermName(a.name.value)

        // build type -> support more types, default arguments
        val tpe = a.className.value match {
          case "String" => typeOf[String]
          case "Int" => typeOf[Int]
          case _ => typeOf[String]
        }

        valParams += ValDef(Modifiers(Flag.PARAM), name, TypeTree(tpe), EmptyTree)
        mapEntries += reify(c.Expr[String](Literal(Constant(a.name.value))).splice -> c.Expr[Any](Ident(name)).splice).tree
    }

    // build attributes map tree
    val mapApply = Select(reify(Map).tree, newTermName("apply"))
    val map = c.Expr[Map[String, Any]](Apply(mapApply, mapEntries.toList))

    // construct layout tree
    val rhs = reify {
      val source = TemplateSource.fromFile(tpl.splice)
      val str = te.layout(source, map.splice)
      str
    }

    // create and return function
    // c.Expr[Unit](Literal(Constant()))
    c.Expr[AnyRef](Function(valParams.toList, rhs.tree))
  }

}

