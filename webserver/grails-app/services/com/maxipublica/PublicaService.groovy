package com.maxipublica

import grails.transaction.Transactional
import rest.RestService

import javax.servlet.http.HttpServletResponse

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

    def updateImages(def dataMap, def accessToken, def vehicleId){

        def queryParams =[
                access_token: accessToken
        ]

        def bodyImagen
        def resultPostImage

        def listaImages=[]

        // por el momento solo podemos modificar 15 imagenes
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

        if(listaImages.size() > 0) {

            def imagesVehicle = restService.getResource("/images/${vehicleId}")
            if(imagesVehicle.data.images){
                   def resultDelImages = restService.deleteResource("/images/${vehicleId}/", queryParams)
                   if(resultDelImages.status == HttpServletResponse.SC_OK){
                       bodyImagen = homologaService.createJsonImages(listaImages)
                       resultPostImage = restService.postResource("/images/${vehicleId}/", queryParams, bodyImagen)
                   }

            }else{

                bodyImagen = homologaService.createJsonImages(listaImages)
                resultPostImage = restService.postResource("/images/${vehicleId}/", queryParams, bodyImagen)
            }
        }

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

        if(listaImages.size() > 0) {
            bodyImagen = homologaService.createJsonImages(listaImages)
            resultPostImage = restService.postResource("/images/${vehicleId}/", queryParams, bodyImagen)
        }


    }

    def searchVehicle(def stockNumber, def dealerId){

        def queryParams =[
                stock_number:stockNumber,
                dealer_id:dealerId
        ]
        def vehicleId = 0

        def result = restService.getResource("/vehicle/search/", queryParams)

        if (result.data.total > 0){
            result.data.results.each{
                vehicleId = it.id
            }
        }

        vehicleId
    }

    def updateVehicle(def vehicleId, def jsonUpdate, def accessToken){

        def queryParams = [
                access_token:accessToken
        ]

        def result = restService.putResource("/vehicle/${vehicleId}", queryParams, jsonUpdate )

    }



    def deleteVehicle(def vehicleId, def accessToken){

        def queryParams = [
                access_token:accessToken
        ]

        def result = restService.deleteResource("/vehicle/${vehicleId}/", queryParams)

        result
    }


}
