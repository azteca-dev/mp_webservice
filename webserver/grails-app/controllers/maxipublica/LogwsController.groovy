package maxipublica

import grails.converters.JSON

class LogwsController {

    def logwsService

    def index() {

        def result
        try{
            result = logwsService.getLogs(params)            
            render result as JSON
        }catch(Exception e){
            def mapException =[
                    message: e.getMessage()
            ]
            render mapException as JSON
        }

    }
}
