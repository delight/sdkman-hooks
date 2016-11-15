package controllers

import domain.{Candidate, Platform}
import play.api.Logger
import play.api.mvc.{Action, _}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class HooksController extends Controller {

  val PostHook = "post"
  val PreHook = "pre"

  def hook(phase: String, candidate: String, version: String, uname: String) = Action.async { request =>
    Future {
      val platformName = Platform(uname).getOrElse(Platform.Universal).name
      Logger.info(s"$phase install hook requested for: $candidate $version $platformName")

      (phase, candidate, version, platformName) match {
        case (PostHook, Candidate.Java, "8u111", Platform.Linux.name) => Ok(views.html.java_8u111_linux())
        case (PostHook, Candidate.Java, _, _) => NotFound
        case (PostHook, _, _, _) => Ok(views.html.default_post(candidate, version, platformName))
        case (PreHook, _, _, _) => Ok(views.html.default_pre(candidate, version, platformName))
      }
    }
  }
}