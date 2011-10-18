package scalex
package http

import scalex.model.{Def, Block}
import com.github.ornicar.paginator.Paginator

object Formatter {

  def apply(query: String, paginator: Paginator[Def]): JsonObject = JsonObject(
    "query" -> query,
    "nbResults" -> paginator.nbResults,
    "page" -> paginator.currentPage,
    "nbPages" -> paginator.nbPages,
    "results" -> (paginator.currentPageResults.toList map { fun =>
      JsonObject(
        "name" -> fun.name,
        "qualifiedName" -> fun.qualifiedName,
        "typeParams" -> fun.showTypeParams,
        "resultType" -> fun.resultType,
        "valueParams" -> fun.paramSignature,
        "signature" -> fun.signature,
        "package" -> fun.pack,
        "parent" -> JsonObject(
          "name" -> fun.parent.name,
          "qualifiedName" -> fun.parent.qualifiedName,
          "typeParams" -> fun.parent.showTypeParams
        ),
        "comment" -> (fun.comment map { com =>
          JsonObject(
            "short" -> block(com.short),
            "body" -> block(com.body),
            "authors" -> (com.authors map block),
            "see" -> (com.see map block),
            "result" -> optionBlock(com.result),
            "throws" -> JsonObject(com.throws map { case (k, v) => (k.replace("_", ".") -> block(v)) }),
            "typeParams" -> JsonObject(com.typeParams map { case (k, v) => (k.replace("_", ".") -> block(v)) }),
            "valueParams" -> JsonObject(com.valueParams map { case (k, v) => (k.replace("_", ".") -> block(v)) }),
            "version" -> optionBlock(com.version),
            "since" -> optionBlock(com.since),
            "todo" -> (com.todo map block),
            "note" -> (com.note map block),
            "example" -> (com.example map block),
            "constructor" -> optionBlock(com.constructor),
            "source" -> com.source
          )
        })
      ) removeNones
    })
  )

  private def optionBlock(b: Option[Block]): JsonObject =
    JsonObject("html" -> (b map (_.html)), "txt" -> (b map (_.txt)))

  private def block(b: Block): JsonObject =
    JsonObject("html" -> b.html, "txt" -> b.txt)
}