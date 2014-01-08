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

  def templateType(source: TemplateSource): AnyRef = macro templateType_impl

  def templateType_impl(c: Context)(source: c.Expr[TemplateSource]): c.Expr[AnyRef] = {
    import c.universe._

    // extract list of parameters from template source -> parses twice
    val extracted = attributes(source.splice.text)

    // construct list of parameters and refs
    val valParams = ListBuffer[ValDef]()
    val refs = ListBuffer[Ident]()
    extracted foreach {
      a =>
        val name = newTermName(a.name.value)

        // build type -> support more types
        val tpe = a.className.value match {
          case "String" => typeOf[String]
          case "Int" => typeOf[Int]
          case _ => typeOf[String]
        }

        valParams += ValDef(Modifiers(Flag.PARAM), name, TypeTree(tpe), EmptyTree)
        refs += Ident(name)
    }

    // build list of attributes
    val map1: Map[String, c.Expr[Any]] = refs map { ref => ref.name.decoded -> reify(c.Expr[Any](ref).splice) } toMap

    // construct body of method -> layout
    val rhs = reify {
      val str = te.layout(source.splice, map1)
      str
    }

    // create function


    //  Expr(Function(
    //    List(ValDef(Modifiers(PARAM), newTermName("a"), Ident(scala.Int), EmptyTree),
    //      ValDef(Modifiers(PARAM), newTermName("b"), Ident(scala.Int), EmptyTree)),
    //    Literal(Constant(()))))


    val defdef = DefDef(Modifiers(), c.fresh(newTermName("templateType$")), List(), List(valParams.toList), TypeTree(), rhs.tree)

    // what type to return?
    // (String, String, ...) => Int
    // Function

//    c.Expr(defdef)
    c.Expr[Unit](Literal(Constant()))
  }

}

