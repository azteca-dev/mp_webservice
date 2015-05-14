package com.maxipublica

import grails.transaction.Transactional
import rest.RestService

@Transactional
class PublicaService {

    def restService = new RestService()
    def homologaService

    def createVehicle(def jsonVehicle, def accessToken) {

        def queryParams = [
                access_token:accessToken
        ]

        def result = restService.postResource("/vehicle/", queryParams, jsonVehicle)

        result

    }

    def postImages(def dataMap, def accessToken, def vehicleId){

        def queryParams =[
                access_token: accessToken
        ]

        def bodyImagen
        def resultPostImage

        def listaImages=[]

        // por el momento solo podemos enviar 15 imagenes
        listaImages << dataMap.Pic1
        listaImages << dataMap.Pic2
        listaImages << dataMap.Pic3
        listaImages << dataMap.Pic4
        listaImages << dataMap.Pic5
        listaImages << dataMap.Pic6
        listaImages << dataMap.Pic7
        listaImages << dataMap.Pic8
        listaImages << dataMap.Pic9
        listaImages << dataMap.Pic10
        listaImages << dataMap.Pic11
        listaImages << dataMap.Pic12
        listaImages << dataMap.Pic13
        listaImages << dataMap.Pic14
        listaImages << dataMap.Pic15

        listaImages.each{
            if(it){
                bodyImagen = homologaService.createJsonImages(it)
                println "Post Pic= token:${accessToken}, vehicleId:${vehicleId}, json:${bodyImagen}"
                resultPostImage = restService.postResource("/images/${vehicleId}/", queryParams, bodyImagen)
                println "Resultado Pic = ${resultPostImage}"
            }else{
                println "No hay foto a postear"
            }
        }

    }
}
