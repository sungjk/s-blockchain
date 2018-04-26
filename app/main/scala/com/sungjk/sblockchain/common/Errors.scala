package com.sungjk.sblockchain.common

object Errors {
	sealed trait Error extends Exception {
		val code: Int
	}

	sealed trait ClientError extends Error
	sealed trait InvalidRequest extends ClientError

	case object InvalidAction extends InvalidRequest { val code = 105 }

	case object NotFound extends InvalidRequest { val code = 404 }
	case object MethodNotAllowed extends InvalidRequest { val code = 405 }

	sealed trait ServerError extends Error
	case object InternalServerError extends Exception with ServerError { val code = 500 }

	case class UnknownError(message: String) extends Exception with ServerError {
		val code = 999
		val errorMessage: String = s"Error: $message"
	}

	def from(throwable: Throwable): Error = throwable match {
		case error: Error => error
		case _: scala.NotImplementedError =>
			UnknownError("NotSupported")
		case _ =>
			UnknownError(throwable.toString)
	}
}
