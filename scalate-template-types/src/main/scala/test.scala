import org.fusesource.scalate.TemplateSource

/**
 * Created by stefan on 05.01.14.
 */
object test extends App {

  val source = TemplateSource.fromFile(getClass.getResource("/tpl1.jade").getFile)
  //val fn = generateType.templateType(source)


  import scala.reflect.runtime.universe._
  println(showRaw(reify {
    (a: Int, b: Int) => ""
  }))

//  println(showRaw(reify {
//    (a: Int, b: Int) => {
//    }
//  }))
//  Expr(Function(
//    List(ValDef(Modifiers(PARAM), newTermName("a"), Ident(scala.Int), EmptyTree),
//      ValDef(Modifiers(PARAM), newTermName("b"), Ident(scala.Int), EmptyTree)),
//    Literal(Constant(()))))


//  println(showRaw(reify {
//    def render(a: String, b: Int): String = { "" }
//    render _
//  }))
//
//  Expr(Block(List(
//    DefDef(Modifiers(), newTermName("render"), List(), List(
//      List(ValDef(Modifiers(PARAM), newTermName("a"), Select(Ident(scala.Predef), newTypeName("String")), EmptyTree),
//        ValDef(Modifiers(PARAM), newTermName("b"), Ident(scala.Int), EmptyTree))),
//      Select(Ident(scala.Predef), newTypeName("String")), Literal(Constant("")))),
//    Block(List(), Function(List(ValDef(Modifiers(PARAM | SYNTHETIC), newTermName("a"), TypeTree(), EmptyTree),
//      ValDef(Modifiers(PARAM | SYNTHETIC), newTermName("b"), TypeTree(), EmptyTree)),
//      Apply(Ident(newTermName("render")), List(Ident(newTermName("a")), Ident(newTermName("b")))))
//  )))
//
//    render _
//  }))


}