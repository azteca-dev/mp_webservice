package com.wstoapi

import grails.transaction.Transactional
import maxipublica.Logws
import com.wstoapi.exceptions.BadRequestException

import org.joda.time.format.ISODateTimeFormat
import org.joda.time.format.DateTimeFormatter

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

    // TODO aqui aplicamos el paginado y todo lo que necesitan
    def getLogs(def params){

        Map jsonResult = [:]
        def queryMap = [:]

        def logsResults
        def logResults = []

        def date_from
        def date_to

        def offset  = params.offset ? Integer.parseInt(params.offset) : 0
        def limit   = params.limit ? Integer.parseInt(params.limit):Constants.DEFAULT_SEARCH_LIMIT

        limit = limit>Constants.MAX_SEARCH_LIMIT ? Constants.MAX_SEARCH_LIMIT : limit

        def SEARCH_PARAMS_MAP = [
                status:"response.status",
                date_from:"dateRegistered",
                date_to : "dateRegistered"
        ]

        params.each{ key , value ->
            def newKey = SEARCH_PARAMS_MAP[key]
            if(newKey){
                queryMap[newKey] = value
            }
        }

        def logwsCriteria = Logws.createCriteria()


        if(!queryMap){
            logsResults = logwsCriteria.list(sort:"dateRegistered", order:"desc", offset:offset, max:limit){
            }
        }else{

            logsResults = logwsCriteria.list(sort:"dateRegistered", order:"desc", offset:offset, max:limit){

                params.each{ key, value ->

                    def newKey = SEARCH_PARAMS_MAP[key]
                    if(newKey && (newKey!='dateRegistered')){
                        eq(newKey, value)
                    }
                }

                if(params.date_from){
                    try{
                        date_from = ISODateTimeFormat.dateTimeParser().parseDateTime(params.date_from).toDate()
                        ge("dateRegistered", date_from)

                    }catch(Exception e){
                        throw new BadRequestException("Wrong date format in date_from. Must be ISO json format")
                    }
                }

                if(params.date_to){
                    try{
                        date_to = ISODateTimeFormat.dateTimeParser().parseDateTime(params.date_to).toDate()
                        le("registrationDate", date_to)
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
                    origin:it.origin,
                    response:it.response,
                    tech:it.tech,
                    date:it.dateRegistered
            )

        }

        jsonResult.total = logsResults.totalCount
        jsonResult.offset = offset
        jsonResult.limit = limit
        jsonResult.results = logResults

        jsonResult

        /*def logwsResult = Logws.findAll()
        logwsResult*/

    }
}
