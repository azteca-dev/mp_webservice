package com.maxipublica

import grails.transaction.Transactional
import maxipublica.Logws

@Transactional
class LogwsService {

    def createLog(def dataMap) {


        Logws newlogws = new Logws(

                section         : dataMap.section,
                user            : dataMap.user,
                description     : dataMap.description,
                data            : dataMap.data

        )

        if(newlogws.validate()){
            newlogws.save(flush: true)
        }

    }

    def getLogs(){

        def logwsResult = Logws.findAll()
        logwsResult

    }
}
