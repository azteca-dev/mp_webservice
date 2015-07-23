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

@Transactional
class LogwsService {

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
                api:apiResponse,
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

        def date_from
        def date_to

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
                        throw new BadRequestException("Invalid date range, date date_to must be greater then date date_from")
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
