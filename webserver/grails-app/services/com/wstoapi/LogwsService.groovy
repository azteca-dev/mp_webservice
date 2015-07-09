package com.wstoapi

import grails.transaction.Transactional
import maxipublica.Logws

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
    def getLogs(){

        def logwsResult = Logws.findAll()
        logwsResult

    }
}
