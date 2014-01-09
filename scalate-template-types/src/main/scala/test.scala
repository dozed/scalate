import org.fusesource.scalate.TemplateSource

/**
 * Created by stefan on 05.01.14.
 */
object test extends App {

  val fn: (String, Int) => String = generateType.templateType("/home/stefan/Code/funk/scalate/scalate-template-types/target/classes/tpl1.jade")
  println(fn("test", 123))

  import scala.reflect.runtime.universe._
  println(showRaw(reify { Map("a" -> "b", "c" -> "d") }))

  val function = (a: Int, b: Int) => ""
  def functionDef(a: Int, b: Int) = ""
  val block = { "string" }


//  Expr(Function(
//    List(ValDef(Modifiers(PARAM), newTermName("a"), Ident(scala.Int), EmptyTree),
//      ValDef(Modifiers(PARAM), newTermName("b"), Ident(scala.Int), EmptyTree)),
//    Literal(Constant(()))))


//  Expr(Block(List(
//    DefDef(Modifiers(), newTermName("render"), List(), List(
//      List(ValDef(Modifiers(PARAM), newTermName("a"), Select(Ident(scala.Predef), newTypeName("String")), EmptyTree),
//        ValDef(Modifiers(PARAM), newTermName("b"), Ident(scala.Int), EmptyTree))),
//      Select(Ident(scala.Predef), newTypeName("String")), Literal(Constant("")))),
//    Block(List(), Function(List(ValDef(Modifiers(PARAM | SYNTHETIC), newTermName("a"), TypeTree(), EmptyTree),
//      ValDef(Modifiers(PARAM | SYNTHETIC), newTermName("b"), TypeTree(), EmptyTree)),
//      Apply(Ident(newTermName("render")), List(Ident(newTermName("a")), Ident(newTermName("b")))))
//  )))


}