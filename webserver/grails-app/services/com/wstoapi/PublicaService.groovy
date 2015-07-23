package com.wstoapi

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

        def result = restService.postResource("/vehicletest/", queryParams, jsonVehicle)

        result

    }

    def updateImages(def dataMap, def accessToken, def vehicleId){

        def queryParams =[
                access_token: accessToken
        ]

        def bodyImagen
        def resultUpdateImage

        def listaImages=[]

        // por el momento solo podemos modificar 15 imagenes
        if(dataMap.Pic1){listaImages.add(dataMap.Pic1)}
        if(dataMap.Pic2){listaImages.add(dataMap.Pic2)}
        if(dataMap.Pic3){listaImages.add(dataMap.Pic3)}
        if(dataMap.Pic4){listaImages.add(dataMap.Pic4)}
        if(dataMap.Pic5){listaImages.add(dataMap.Pic5)}
        if(dataMap.Pic6){listaImages.add(dataMap.Pic6)}
        if(dataMap.Pic7){listaImages.add(dataMap.Pic7)}
        if(dataMap.Pic8){listaImages.add(dataMap.Pic8)}
        if(dataMap.Pic9){listaImages.add(dataMap.Pic9)}
        if(dataMap.Pic10){listaImages.add( dataMap.Pic10)}
        if(dataMap.Pic11){listaImages.add(dataMap.Pic11)}
        if(dataMap.Pic12){listaImages.add(dataMap.Pic12)}
        if(dataMap.Pic13){listaImages.add(dataMap.Pic13)}
        if(dataMap.Pic14){listaImages.add(dataMap.Pic14)}
        if(dataMap.Pic15){listaImages.add(dataMap.Pic15)}

        if(listaImages.size() > 0) {

            def imagesVehicle = restService.getResource("/images/${vehicleId}")
            if(imagesVehicle.data.images){
                   def resultDelImages = restService.deleteResource("/images/${vehicleId}/", queryParams)
                   if(resultDelImages.status == HttpServletResponse.SC_OK){
                       bodyImagen = homologaService.createJsonImages(listaImages)
                       resultUpdateImage = restService.postResource("/images/${vehicleId}/", queryParams, bodyImagen)
                   }

            }else{

                bodyImagen = homologaService.createJsonImages(listaImages)
                resultUpdateImage = restService.postResource("/images/${vehicleId}/", queryParams, bodyImagen)
            }
        }else{
            resultPostImage = [
                    status:HttpServletResponse.SC_NOT_FOUND,
                    message: "No se enviaron fotos en la actualización",
                    error: "not_found"
            ]
        }

        resultUpdateImage

    }

    def postImages(def dataMap, def accessToken, def vehicleId, def jsonPostVehicle){



        def queryParams =[
                access_token: accessToken
        ]

        def resultPostImage
        def bodyImagen

        def listaImages=[]

        // por el momento solo podemos enviar 15 imagenes
        if(dataMap.Pic1){listaImages.add(dataMap.Pic1)}
        if(dataMap.Pic2){listaImages.add(dataMap.Pic2)}
        if(dataMap.Pic3){listaImages.add(dataMap.Pic3)}
        if(dataMap.Pic4){listaImages.add(dataMap.Pic4)}
        if(dataMap.Pic5){listaImages.add(dataMap.Pic5)}
        if(dataMap.Pic6){listaImages.add(dataMap.Pic6)}
        if(dataMap.Pic7){listaImages.add(dataMap.Pic7)}
        if(dataMap.Pic8){listaImages.add(dataMap.Pic8)}
        if(dataMap.Pic9){listaImages.add(dataMap.Pic9)}
        if(dataMap.Pic10){listaImages.add( dataMap.Pic10)}
        if(dataMap.Pic11){listaImages.add(dataMap.Pic11)}
        if(dataMap.Pic12){listaImages.add(dataMap.Pic12)}
        if(dataMap.Pic13){listaImages.add(dataMap.Pic13)}
        if(dataMap.Pic14){listaImages.add(dataMap.Pic14)}
        if(dataMap.Pic15){listaImages.add(dataMap.Pic15)}


        if(listaImages.size() > 0) {
            bodyImagen = homologaService.createJsonImages(listaImages)
            resultPostImage = restService.postResource("/images/${vehicleId}/", queryParams, bodyImagen)
        }
        else{


            jsonPostVehicle.published_sites = [mlm:[
                    status:"waiting",
                    action:"to_publish"
                    //official_store_id:"235" Lo quitamos por peticion de Erick que le dijo Miguel 14-07-2015
            ]]

            jsonPostVehicle << [origin_update:"webservice"]

            def respUpdApiVehicle = updateVehicle(vehicleId, jsonPostVehicle, accessToken)

            resultPostImage = [
                    status:HttpServletResponse.SC_NOT_FOUND,
                    message:'Se envió sin fotos',
                    error:"not_found"
            ]
        }

        resultPostImage


    }

    def searchVehicle(def stockNumber, def dealerId, def accessToken){

        def queryParams =[
                access_token:accessToken,
                stock_number:stockNumber,
                dealer_id:dealerId
        ]
        def vehicleId = 0

        def result = restService.getResource("/vehicletest/search/", queryParams)

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

        def result = restService.putResource("/vehicletest/${vehicleId}", queryParams, jsonUpdate )

    }



    def deleteVehicle(def vehicleId, def accessToken){

        def queryParams = [
                access_token:accessToken
        ]

        def result = restService.deleteResource("/vehicletest/${vehicleId}/", queryParams)

        result
    }




}
