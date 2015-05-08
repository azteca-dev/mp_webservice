package com.maxipublica

import grails.transaction.Transactional
import rest.RestService

@Transactional
class PublicaService {

    def restService = new RestService()

    def createVehicle(def jsonVehicle, def accessToken) {

        def queryParams = [
                access_token:accessToken
        ]

        def result = restService.postResource("/vehicle/", queryParams, jsonVehicle)

        result

    }
}
