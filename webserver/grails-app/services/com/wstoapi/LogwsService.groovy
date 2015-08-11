package com.wstoapi

import grails.transaction.Transactional
import maxipublica.Logws
import com.wstoapi.exceptions.BadRequestException

import org.joda.time.LocalTime
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.ISODateTimeFormat
import groovy.time.TimeCategory
import java.util.Calendar
import java.util.Date
import com.wstoapi.AuthenticateService
import com.wstoapi.PublicaService
import com.wstoapi.ValidAccess

@Transactional
class LogwsService {
    def AuthenticateService
    def PublicaService
    def validAccess = new ValidAccess()

    def createLog(def dataMap) {


        Logws newlogws = new Logws(

                action : dataMap.action,
                origin : dataMap.origin,
                response : dataMap.response,
                tech : dataMap.tech

        )

        if(newlogws.validate()){
            newlogws.save(flush: true)
        }else{
            newlogws.errors.each{
                println it
            }
        }

    }

    def createMapLog(
            def ipAddress,
            def originMap,
            def webServiceResponse,
            def apiResponse,
            def status,
            def action,
            def user,
            def dealer,
            def vehicleId,
            def mlmId){

        def responseLogMap = [
                webservice:webServiceResponse,
                //api:apiResponse,
                status:status
        ]
        def techMap =[
                ip: ipAddress,
                user: user,
                dealer: dealer,
                mxpid: vehicleId,
                mlmid: mlmId,
        ]
        def logMap = [
                action : action,
                origin : originMap,
                response : responseLogMap,
                tech : techMap
        ]

        logMap
    }


    def getLogs(def params){
        Map jsonResult = [:]
        def queryMap = [:]

        def logsResults
        def logResults = []
        def distincMap = [:]

        def date_from
        def date_to
        def contador = 0

        if (!params.access_token){
            throw new BadRequestException ("You must provide an access_token")
        }

        def access_token = validAccess.validAccessToken(params.access_token)
        def user_id = params.access_token.split('_')[2]

        if(!access_token.toString().equals("admin")){
            throw new BadRequestException ("You need to be admin")
        }

        //Redondea el offset a un numero entero y si no puede lo deja en 0
        def offset  = params.offset ? Integer.parseInt(params.offset) : 0
        def limit   = params.limit ? Integer.parseInt(params.limit):Constants.DEFAULT_SEARCH_LIMIT

        limit = limit>Constants.MAX_SEARCH_LIMIT ? Constants.MAX_SEARCH_LIMIT : limit

        def SEARCH_PARAMS_MAP = [
                status:"response.status",
                date_from:"dateRegistered",
                date_to : "dateRegistered",
                order_by_id:"order_id",
                order_by_date:"orderDateRegistered",
                stock_number:"origin.NumInventarioEmpresa",
                user:"tech.user",
                usuario:"origin.usuario",
                action_:"action"
        ]

        //parametros de busqueda para el criteria
        def featuresQueryMap =[
                sort:"dateRegistered",
                order:"desc",
                offset:offset,
                max:limit
        ]

        //Valida que el orden por ID sea con paramatros adecuados
        if(params.order_by_id){
            if(params.order_by_id != 'desc' && params.order_by_id != 'asc'){
                throw new BadRequestException('The allowed values for the field order_by_id are [desc, asc] ')
            }
        }

        //Valida que el orden por fecha sea con paramatros adecuados
        if(params.order_by_date){
            if(params.order_by_date != 'desc' && params.order_by_date != 'asc'){
                throw new BadRequestException('The allowed values for the field order_by_date are [desc, asc] ')
            }
        }

        //itera cada una de las variables que vienen a travez de la URL variable = valor
        params.each{ key , value ->
            //Busca la variable en el mapa
            def newKey = SEARCH_PARAMS_MAP[key]
            //Si encuentra esa variable en el mapa regresa el valor de la key que encontro dentro de ese mapa
            if(newKey){
                //Asigna el valor de la variable que itera en ese momento a una key nueva que se genera en el mapa de querys
                //correspondiente al valor que encontro en el SEARCH_PARAMS_MAP
                queryMap[newKey] = value
                //queryMap.push(newKey, value)
                if(newKey == 'order_id'){
                    //parametros de busqueda para el criteria
                    featuresQueryMap =[
                            sort:"_id",
                            order:value,
                            offset:offset,
                            max:limit
                    ]
                }
                if(newKey == 'orderDateRegistered'){
                    //parametros de busqueda para el criteria
                    featuresQueryMap =[
                            sort:"dateRegistered",
                            order:value,
                            offset:offset,
                            max:limit
                    ]
                }
            }
        }



        def logwsCriteria = Logws.createCriteria()


        if(!queryMap){
            logsResults = logwsCriteria.list(featuresQueryMap){
            }
        }else{

            logsResults = logwsCriteria.list(featuresQueryMap){

                params.each{ key, value ->

                    def newKey = SEARCH_PARAMS_MAP[key]
                    if(newKey && (newKey!='dateRegistered' && newKey!='order_id' && newKey!='orderDateRegistered')){
                      //en dado caso de que no exista ninguna de los parametros establecivos busca por el que se especifica en la newKey
                        //println newKey
                        if(newKey.equals("tech.user")){
                            eq(newKey, Integer.valueOf(value))
                        }else if(newKey.equals("origin.usuario")){
                            ilike(newKey, "%"+value+"%")
                        }else{
                            eq(newKey, value)
                        }
                    }
                }

                if(params.date_from){
                    try{
                        date_from = ISODateTimeFormat.dateTimeParser().parseDateTime(params.date_from).toDate()
                        ge("dateRegistered", fechaMasTiempo(date_from))

                    }catch(Exception e){
                        throw new BadRequestException("Wrong date format in date_from. Must be ISO json format")
                    }
                }

                if(params.date_to){
                    try{
                        date_to = ISODateTimeFormat.dateTimeParser().parseDateTime(params.date_to).toDate()
                        le("dateRegistered", fechaMasTiempo(date_to))
                    }catch(Exception e){
                        throw new BadRequestException("Wrong date format in date_to. Must be ISO json format")
                    }
                }

                if(params.date_from && params.date_to){

                    if(date_to < date_from){
                        throw new BadRequestException("Invalid date range, date date_to must be greater than date date_from")
                    }

                }
            }
        }
        
        logsResults.each{
            logResults.add(
                id:it.id,
                action:it.action,
                date:fechaMenosTiempo(it.dateRegistered),
                origin:it.origin,
                response:it.response,
                tech:it.tech
            )
        }
        
        jsonResult.total = logsResults.totalCount
        jsonResult.offset = offset
        jsonResult.limit = limit
        jsonResult.results = logResults

        jsonResult

    }

    def getSearch(def params){
        Map jsonResult = [:]
        def queryMap = [:]

        def logsResults
        def logResults = []
        def distincMap = [:]
        def featuresQueryMap = [:]
        def date_from
        def date_to
        def contadorOffset = 1
        def contadorLimit = 1

        if (!params.access_token){
            throw new BadRequestException ("You must provide an access_token")
        }

        def access_token = validAccess.validAccessToken(params.access_token)
        def user_id = params.access_token.split('_')[2]

        if(!access_token.toString().equals("admin") && !access_token.toString().equals("developer")){
            throw new BadRequestException ("You need to be developer or admin")
        }
        
        //Redondea el offset a un numero entero y si no puede lo deja en 0
        def offset  = params.offset ? Integer.parseInt(params.offset) : 0
        def limit   = params.limit ? Integer.parseInt(params.limit):Constants.DEFAULT_SEARCH_LIMIT

        limit = limit>Constants.MAX_SEARCH_LIMIT ? Constants.MAX_SEARCH_LIMIT : limit

        def SEARCH_PARAMS_MAP = [
                status: "response.status",
                date_from: "dateRegistered",
                date_to :  "dateRegistered",
                order_by_id: "order_id",
                order_by_date: "orderDateRegistered",
                stock_number: "origin.NumInventarioEmpresa",
                user: "tech.user",
                usuario: "origin.usuario",
                action_: "action",
                active: "action"
        ]

        //parametros de busqueda para el criteria
        if (params.usuario && !params.distinct_) {
            featuresQueryMap =[
                sort:"dateRegistered",
                order:"desc"
                //offset:offset,
                //max:limit
            ]
        }

        //Valida que el orden por ID sea con paramatros adecuados
        if(params.order_by_id){
            if(params.order_by_id != 'desc' && params.order_by_id != 'asc'){
                throw new BadRequestException('The allowed values for the field order_by_id are [desc, asc] ')
            }
        }

        //Valida que el orden por fecha sea con paramatros adecuados
        if(params.order_by_date){
            if(params.order_by_date != 'desc' && params.order_by_date != 'asc'){
                throw new BadRequestException('The allowed values for the field order_by_date are [desc, asc] ')
            }
        }

        //itera cada una de las variables que vienen a travez de la URL variable = valor
        params.each{ key , value ->
            //Busca la variable en el mapa
            def newKey = SEARCH_PARAMS_MAP[key]
            //Si encuentra esa variable en el mapa regresa el valor de la key que encontro dentro de ese mapa
            if(newKey){
                //Asigna el valor de la variable que itera en ese momento a una key nueva que se genera en el mapa de querys
                //correspondiente al valor que encontro en el SEARCH_PARAMS_MAP
                queryMap[newKey] = value
                //queryMap.push(newKey, value)
                if (params.usuario) {
                    if(newKey == 'order_id'){
                        //parametros de busqueda para el criteria
                        featuresQueryMap =[
                                sort:"_id",
                                order:value
                                //offset:offset,
                                //max:limit
                        ]
                    }
                    if(newKey == 'orderDateRegistered'){
                        //parametros de busqueda para el criteria
                        featuresQueryMap =[
                                sort:"dateRegistered",
                                order:value
                                //offset:offset,
                                //max:limit
                        ]
                    }
                }
            }
        }



        def logwsCriteria = Logws.createCriteria()


        if(!queryMap){
            logsResults = logwsCriteria.list(featuresQueryMap){
            }
        }else{
            logsResults = logwsCriteria.list(featuresQueryMap){

                params.each{ key, value ->

                    def newKey = SEARCH_PARAMS_MAP[key]
                    if(newKey && (newKey!='dateRegistered' && newKey!='order_id' && newKey!='orderDateRegistered')){
                      //en dado caso de que no exista ninguna de los parametros establecivos busca por el que se especifica en la newKey
                        if(newKey.equals("tech.user")){
                            eq(newKey, Integer.valueOf(value))
                        }else if(newKey.equals("origin.usuario")){
                            ilike(newKey, "%"+value+"%")
                        }else if(newKey.equals("action")){
                            or{
                                eq(newKey, "insert")
                                eq(newKey, "update")
                                eq(newKey, "republish")
                            }
                        }else{
                            eq(newKey, value)
                        }
                    }
                }

                if(params.date_from){
                    try{
                        date_from = ISODateTimeFormat.dateTimeParser().parseDateTime(params.date_from).toDate()
                        ge("dateRegistered", fechaMasTiempo(date_from))

                    }catch(Exception e){
                        throw new BadRequestException("Wrong date format in date_from. Must be ISO json format")
                    }
                }

                if(params.date_to){
                    try{
                        date_to = ISODateTimeFormat.dateTimeParser().parseDateTime(params.date_to).toDate()
                        le("dateRegistered", fechaMasTiempo(date_to))
                    }catch(Exception e){
                        throw new BadRequestException("Wrong date format in date_to. Must be ISO json format")
                    }
                }

                if(params.date_from && params.date_to){

                    if(date_to < date_from){
                        throw new BadRequestException("Invalid date range, date date_to must be greater than date date_from")
                    }

                }
            }
        }
        
        if(params.distinct_){
            logsResults.each{
            //for(def it : logsResults.keySet()){
                if(it.origin.NumInventarioCliente){
                    if(!distincMap.containsKey(it.origin.NumInventarioCliente)){
                        distincMap.put(it.origin.NumInventarioCliente, it)
                    }else{
                        if(it.dateRegistered > distincMap.get(it.origin.NumInventarioCliente).dateRegistered){
                            distincMap.put(it.origin.NumInventarioCliente,it) //= it
                        }
                    }
                }
                if(it.origin.NumInventarioEmpresa){
                    if(!distincMap.containsKey(it.origin.NumInventarioEmpresa)){
                        distincMap.put(it.origin.NumInventarioEmpresa, it)
                    }else{
                        if(it.dateRegistered > distincMap.get(it.origin.NumInventarioEmpresa).dateRegistered){
                            distincMap.put(it.origin.NumInventarioEmpresa,it) //= it
                        }
                    }
                }
            }
        }else{
            logsResults.each{
                if(offset <= contadorOffset && limit >= contadorLimit){
                    logResults.add(
                        id:it.id,
                        action:it.action,
                        date:fechaMenosTiempo(it.dateRegistered),
                        origin:it.origin,
                        response:it.response,
                        tech:it.tech
                    )
                    contadorLimit ++
                }
                if (contadorLimit == limit) {
                    true
                }
                contadorOffset ++
            }
        }

        
        

       
        if (distincMap.size() > 0) {
            jsonResult.total = distincMap.size()
            distincMap.each{key, value ->
                if((offset <= contadorOffset) && (limit >= contadorLimit)){
                    logResults.add(
                        id:value.id,
                        action:value.action,
                        date:fechaMenosTiempo(value.dateRegistered),
                        origin:value.origin,
                        response:value.response,
                        tech:value.tech
                    )
                    contadorLimit ++
                }
                if (contadorLimit == limit) {
                    true
                }
                contadorOffset ++
            }
        }else{
            jsonResult.total = logsResults.totalCount
        }
        jsonResult.offset = offset
        jsonResult.limit = limit
        
        jsonResult.results = logResults

        jsonResult

    }

    def logsUpdate(def params){
        def logsResults
        def logwsCriteria
        def result = [:]
        def arrayLogs = []
        def oauth
        def vehicle
        def findLog
        def mlmID

        /*if (!params.access_token){
            throw new BadRequestException ("You must provide an access_token")
        }

        def access_token = validAccess.validAccessToken(params.access_token)
        def user_id = params.access_token.split('_')[2]
        
        if(!access_token.toString().equals("admin")){
            throw new BadRequestException ("You need to be admin")
        }*/

        oauth = AuthenticateService.login("wm@maxipublica.com", "6W8Z67YjCJ78G3r")

        logwsCriteria = Logws.createCriteria()
        logsResults = logwsCriteria.list{
            and{
                ne("tech.mxpid", "")
                ne("tech.mxpid", 0)
                eq("tech.mlmid", "")
                ne("tech.mlmid", "not_found_id")
            }
        }

        logsResults.each{
            arrayLogs.add(it)
        }
        println "WS_MP total: " + arrayLogs.size()
        result.termine = []
        result.no_encontrados = []
        arrayLogs.each{
            try{
                vehicle = PublicaService.searchVehicle("/vehicletest/" + it.tech.mxpid, [access_token:oauth.data.access_token]).data
                if(vehicle.error.equals("not_found") || vehicle.message.equals("The carId not found")){
                    vehicle = PublicaService.searchVehicle("/vehicletest/search2", [id:it.tech.mxpid, status:"closed", access_token:oauth.data.access_token]).data.result
                    if(!vehicle.message.equals("not found")){
                        println "WS_MP Entre al IF: " + it.tech.mxpid + " - " + vehicle[0].published_sites.mlm.id
                        findLog = Logws.findById(it.id)
                        if(vehicle[0].published_sites.mlm.id.equals("")) {
                            findLog.tech.mlmid = "not_found_id"
                            mlmID = "not_found_id"
                            result.termine.add("autoID: ${it.tech.mxpid} - mlmID: ${mlmID} - logID: ${it.id}")
                        }else{
                            findLog.tech.mlmid = vehicle[0].published_sites.mlm.id
                            mlmID = vehicle[0].published_sites.mlm.id
                            result.termine.add("autoID: ${it.tech.mxpid} - mlmID: ${mlmID} - logID: ${it.id}")
                        }
                        findLog.save(flush : true)
                    }else{
                        result.no_encontrados.add(it.tech.mxpid)
                        println "WS_MP No encontrado en vehicle: " + it.tech.mxpid
                    }
                }else{
                    println "WS_MP Entre al ELSE: " + it.tech.mxpid + " - " + vehicle.published_sites.mlm.id
                    findLog = Logws.findById(it.id)
                    if(vehicle.published_sites.mlm.id.equals("")){
                        findLog.tech.mlmid = "not_found_id"
                        mlmID = "not_found_id"
                        result.termine.add("autoID: ${it.tech.mxpid} - mlmID: ${mlmID} - logID: ${it.id}")
                    }else{
                        findLog.tech.mlmid = vehicle.published_sites.mlm.id
                        mlmID = vehicle.published_sites.mlm.id
                        result.termine.add("autoID: ${it.tech.mxpid} - mlmID: ${mlmID} - logID: ${it.id}")
                    }
                    findLog.save(flush : true)
                }
            }catch(Exception e){
                println "WS_MP Ocurrio un error" + e + ", autoID: " + it.tech.mxpid + ", mlmID: " + mlmID + " logID: " + it.id
            }
        }
        println "WS_MP resultado final encontrados: " + result.termine
        println "WS_MP resultado final no encontrados: " + result.no_encontrados
        result
    }

    //Cuando se agrege la hora correcta al tomcat quitar este metodo y su llamada en la linea 215
    private def fechaMenosTiempo(def value){
        def ultimaF
        use(TimeCategory){
            ultimaF = value-5.hour
            //ultimaF = ultimaF.format("yyyy-MM-dd")+"T"+"00:00:00.000"
        }
        return ultimaF
    }
    //Cuando se agrege la hora correcta al tomcat quitar este metodo y su llamada en las lineas 186 y 196
    private def fechaMasTiempo(def value){
        def ultimaF
        use(TimeCategory){
            ultimaF = value+5.hour
            //ultimaF = ultimaF.format("yyyy-MM-dd")+"T"+"00:00:00.000"
        }
        return ultimaF
    }
}
