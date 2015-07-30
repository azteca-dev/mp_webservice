package maxipublica

import grails.converters.JSON
import com.wstoapi.RepublicaService

import javax.servlet.http.HttpServletResponse
import com.wstoapi.exceptions.ConflictException
import com.wstoapi.exceptions.NotFoundException
import com.wstoapi.exceptions.BadRequestException
//import static org.springframework.http.HttpStatus.*
//import static org.springframework.http.HttpMethod.*
import org.springframework.dao.OptimisticLockingFailureException

class RepublicaController {

	def RepublicaService

	def setHeaders(){
		response.setContentType "application/json; charset=utf-8"
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE");
		response.setHeader("Access-Control-Max-Age", "86400");
		response.setHeader("Access-Control-Allow-Headers", "application/json;charset=UTF-8");
	}

	def renderException(def e){

		def statusCode
		def error

		try{
			statusCode = e.status
			error = e.error
		}catch(Exception ex){
			statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR
			error = "internal_server_error"
		}

		response.setStatus(statusCode)

		def mapExcepction = [
			message: e.getMessage(),
			status: statusCode,
			error: error
		]
		render mapExcepction as JSON
	}

	def notAllowed(){
		def method = request.method

		response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED)
		setHeaders()

		def mapResult = [
			message: "Method $method not allowed",
			status: HttpServletResponse.SC_METHOD_NOT_ALLOWED,
			error:"not_allowed"
		]
		render mapResult as JSON
	}

	def republica(){
		//def idRepublica = params.idRepublica
		def json = request.JSON
		def result

		setHeaders()
    	boolean needsProcessing = true
		int retryCounter = 0
		int maxretry=15

		while(needsProcessing && retryCounter < maxretry) {
      	try{
  			result = RepublicaService.republica(json, params)
  			response.setStatus(HttpServletResponse.SC_OK)
  			needsProcessing=false;
	        render result as JSON
	  		}catch(NotFoundException e){
	        	needsProcessing=false;
	  			renderException(e)
	  		}catch(BadRequestException e){
	        	needsProcessing=false;
	  			renderException(e)
	  		}catch(ConflictException e){
	        	needsProcessing=false;
	  			renderException(e)
	  		}catch(OptimisticLockingFailureException olfex) {
	          if((retryCounter += 1) >= maxretry) renderException(olfex);
	      	}catch(Exception e){
		        println "Republica Exception error----> "+e
		        needsProcessing=false;
		        renderException(e)
	        }
    	}
	}

}
