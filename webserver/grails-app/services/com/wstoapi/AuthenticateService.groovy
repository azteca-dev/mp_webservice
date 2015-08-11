package com.wstoapi

import grails.transaction.Transactional
import rest.RestService
@Transactional
class AuthenticateService {

    def restService = new RestService()

    def login( def user, def password){

        def body = [
                email:user,
                password:password
        ]

        def result = restService.postResource("/oauth/", body)

        result


    }

    def getDealer (def userId, def accessToken){

        def parameters = [
                access_token:accessToken
        ]

        def result = restService.getResource("/users/${userId}/", parameters)

        result
    }
}
