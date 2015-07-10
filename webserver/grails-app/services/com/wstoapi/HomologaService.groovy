package com.wstoapi

import grails.transaction.Transactional
import maxipublica.Dictionaryws
import rest.RestService

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Transactional
class HomologaService {

    def restService = new RestService()

    def homologaData(def dataMap, def userId, def dealerId, def accessToken) {

        def dataMapProcess = processIdMXP(dataMap, accessToken)
        def jsonVehicle = createJsonVehicle(dataMapProcess, userId, dealerId)

        jsonVehicle

    }

    def homologaDataUpdate(def dataMap, def userId, def dealerId, def accessToken){

        def dataMapProcess = processIdMXP(dataMap, accessToken)
        def jsonVehicleUPD = createJsonVehicleUPD(dataMapProcess, userId, dealerId)

        jsonVehicleUPD
    }


    def createJsonVehicle(def dataMap, def userId, def dealerId) {


        def jsonVehicleForPost = [
                stock_number: dataMap.StockNumber,
                vehicle:[
                        version:[
                                category_id:dataMap.VersionMPId,
                                name:dataMap.Version
                        ],
                        year:[
                                name:dataMap.Year
                        ],
                        model:[
                                category_id:dataMap.ModelMPId,
                                name:dataMap.Model
                        ],
                        mark:[
                                category_id:dataMap.MarkMPId,
                                name:dataMap.Mark
                        ]
                ],
                kilometers:dataMap.Kilometers,
                price:dataMap.Price,
                description:dataMap.Description,
                number_plate:"NO",
                condition:'usado', //dataMap.StatusVehicleMPId,
                dealer:[
                        dealer_ID:dealerId,
                        seller_contact:[
                                id:userId
                        ]
                ],
                images:[

                ],
                attributes:getAttributes(dataMap.TypeCurrency, dataMap.ExteriorColor, dataMap.InteriorColor, dataMap.TypeVestureMPId, dataMap.TypeTransmissionMPId, dataMap.TypeVehicleMPId),
                equipment:getEquipment(dataMap.Equipment),
                published_sites:[
                        /*mercadoLibre:[
                                publish:"true",
                                official_store_id:"235"
                        ]*/
                        mlm:[
                                status:"waiting",
                                action:"to_publish",
                                official_store_id:"235"
                        ]
                ]

        ]

        jsonVehicleForPost
    }


    def createJsonVehicleUPD(def dataMap, def userId, def dealerId) {


        def jsonVehicleForPut = [
                kilometers:dataMap.Kilometers,
                price:dataMap.Price,
                description:dataMap.Description,
                attributes:getAttributes(dataMap.TypeCurrency, dataMap.ExteriorColor, dataMap.InteriorColor, dataMap.TypeVestureMPId, dataMap.TypeTransmissionMPId, dataMap.TypeVehicleMPId),
                equipment:getEquipment(dataMap.Equipment)


        ]

        jsonVehicleForPut
    }

    def createJsonImages (def listImages){

        def images = []

        listImages.each{
            if(it){
                images.add("url":it)
            }
        }

        def jsonImages = [
                images:images
        ]


        jsonImages
    }

    def getAttributes (def TypeCurrency,
                       def ExteriorColor,
                       def InteriorColor,
                       def TypeVestureMPId,
                       def TypeTransmissionMPId,
                       def TypeVehicleMPId ){

        def jsonAttributes = []

        try{
            jsonAttributes = [

                    id:"attributes_group",
                    label:"Ficha Tecnica",
                    currencies:[
                            id:TypeCurrency.toUpperCase()
                    ],
                    colorExt:[
                            id:ExteriorColor.toUpperCase()
                    ],
                    colorInt:[
                            id:InteriorColor.toUpperCase()
                    ],
                    vesture:[
                            id:TypeVestureMPId.toUpperCase()
                    ],
                    transmission:[
                            id:TypeTransmissionMPId.toUpperCase()
                    ],
                    body_type:[
                            id:TypeVehicleMPId.toUpperCase()
                    ]

            ]
        }catch(Exception e){
            jsonAttributes = []
        }

        jsonAttributes
    }

    def getEquipment(def ids){

        List listId = []
        if(ids){
          def listIds = ids.split('@')
            listIds.each{
                listId.add(it.toString())
            }
        }

        def jsonEquipment = [:]
        def classifiedsGruops = []


        String type = 'ACC'
        String site = 'APC'

        def resultEquipment = Dictionaryws.findAllByTypeAndSiteAndValueIdSiteInList(type, site, listId )

        if(resultEquipment){

            jsonEquipment.id = "equipment_group"
            jsonEquipment.label = 'Equipamiento'

            resultEquipment.each{
                if(!classifiedsGruops.contains(it.groupId)){
                    classifiedsGruops.add(it.groupId)
                }
            }

            classifiedsGruops.each{ groups ->

                jsonEquipment."${groups}" = [:]
                jsonEquipment."${groups}".id = groups


                resultEquipment.each{
                    if(it.groupId == groups) {
                        if(!jsonEquipment."${groups}"."${it.valueIdMXP}"){
                            jsonEquipment."${groups}"."${it.valueIdMXP}" = [:]
                        }
                        jsonEquipment."${groups}"."${it.valueIdMXP}".id = "${it.valueIdMXP}"
                    }
                }

            }

        }else{
            jsonEquipment=[:]
        }

        jsonEquipment
    }

    def validateDataMap( def dataMap){

        Map response = [
                status:"0",
                message:"OK"
        ]


        if(!dataMap.User){
            response.status = "1"
            response.message = "El Usuario es requerido"
        }
        if(!dataMap.Pass){
            response.status = "1"
            response.message = "La Contraseña es requerida"
        }

        if(!dataMap.StockNumber){
            response.status = "8"
            response.message = "El NumInventarioEmpresa es requerido"
        }
        if(!dataMap.Price){
            response.status = "8"
            response.message = "El Precio es requerido"
        }
        if(!dataMap.TypeCurrency){
            response.status = "8"
            response.message = "El TipoMoneda es requerido"
        }
        if(!dataMap.Kilometers){
            response.status = "8"
            response.message = "El Kilometraje es requerido"
        }
        if(!dataMap.ExteriorColor){
            response.status = "8"
            response.message = "El ColorExterior es requerido"
        }
        if(!dataMap.InteriorColor){
            response.status = "8"
            response.message = "El ColorExterior es requerido"
        }
        if(!dataMap.Mark){
            response.status = "8"
            response.message = "La Marca es requerida"
        }
        if(!dataMap.MarkMPId){
            response.status = "8"
            response.message = "La MarcaIDAutoplaza es requerida"
        }
        if(!dataMap.Model){
            response.status = "8"
            response.message = "El Modelo es requerido"
        }
        if(!dataMap.ModelMPId){
            response.status = "8"
            response.message = "El ModeloIDAutoplaza es requerido"
        }
        if(!dataMap.Version){
            response.status = "8"
            response.message = "El Submodelo es requerdio"
        }
        if(!dataMap.VersionMPId){
            response.status = "8"
            response.message = "El SubmodeloIDAutoplaza es requerido"
        }
        if(!dataMap.Year){
            response.status = "8"
            response.message = "El Anno es requerido"
        }
        if(!dataMap.TypeVehicleMPId){
            response.status = "8"
            response.message = "El TipoVehicleIDAutoplaza es requerido"
        }
        if(!dataMap.TypeVestureMPId){
            response.status = "8"
            response.message = "El TipoVestiduraIDAutoplaza es requerido"
        }
        if(!dataMap.TypeTransmissionMPId){
            response.status = "8"
            response.message = "El TipoTransmisionIDAutoplaza es requerido"
        }
        if(!dataMap.StatusVehicleMPId){
            response.status = "8"
            response.message = "El StatusVehiculoIDAutoplaza es requerido"
        }
        if(!dataMap.Action){
            response.status = "8"
            response.message = "El EventoArealizar es requerido"
        }


        response
    }

    def processIdMXP ( def dataMap, def accessToken){

        dataMap.MarkMPId    = processCatalog("MAR", dataMap.MarkMPId)
        dataMap.ModelMPId   = processCatalog("MOD", dataMap.ModelMPId)
        dataMap.VersionMPId = processCatalog("VER", dataMap.VersionMPId)

        if(dataMap.VersionMPId == 'VER-1'){
            dataMap.VersionMPId = getVersionNotCatalog(dataMap.ModelMPId, dataMap.Year,accessToken )
        }


        def typeCurrency        = processAttributes('CURRENCIE',dataMap.TypeCurrency)
        def exteriorColor       = processAttributes('COLOREXT', dataMap.ExteriorColor)
        def interiorColor       = processAttributes('COLORINT', dataMap.InteriorColor)
        def typeVestureId       = processAttributes('VESTURE', dataMap.TypeVestureMPId)
        def typeTransmissionId  = processAttributes('TRANSMISION', dataMap.TypeTransmissionMPId)
        def typeVehicleId       = processAttributes('BODY', dataMap.TypeVehicleMPId)

        dataMap.TypeCurrency            = typeCurrency ? typeCurrency : 'CURRENCIE-MXN'
        dataMap.ExteriorColor           = exteriorColor ? exteriorColor : 'COLOREXT-OTRO'
        dataMap.InteriorColor           = interiorColor ? interiorColor : 'COLORINT-OTRO'
        dataMap.TypeVestureMPId         = typeVestureId ? typeVestureId : 'VESTURE-OTRO'
        dataMap.TypeTransmissionMPId    = typeTransmissionId ? typeTransmissionId : 'TRANS-AUTOMATICA'
        dataMap.TypeVehicleMPId         = typeVehicleId ? typeVehicleId : 'BODY-SEDAN'




        dataMap

    }

    def processCatalog (def nivel, def value){
        value = (value.contains(nivel)) ? value : nivel+value
        value
    }


    def getVersionNotCatalog(def modeloId, def valueYear, def accessToken){


        def versionId = "-1"
        def yearId = getYearIdFromModeloAndYear(modeloId, valueYear, accessToken)
        if(yearId != "0"){
            def versiones = getCategoryCatalog(yearId, accessToken)
            if(versiones.status == HttpServletResponse.SC_OK){
                versionId = versiones.data.children_categories[0].category_id
            }
        }

        versionId

    }

    def getYearIdFromModeloAndYear(def modeloId, def valueYear, def accessToken){


        def yearId = "0"
        def result = getCategoryCatalog(modeloId, accessToken)

        if (result.status == HttpServletResponse.SC_OK){
            result.data.children_categories.each{
                 if(it.name == valueYear){
                     yearId = it.category_id
                 }
            }
        }

        yearId

    }


    def getCategoryCatalog(def categoryId, def accessToken){

        def queryParams = [
                access_token:accessToken
        ]


        def result = restService.getResource("/catalog/MX/MLM/${categoryId}/", queryParams)

        result
    }

    def processAttributes(String type, def valueSite ){

       String groupId      = 'attributes_group'
       String groupName    = 'Ficha Tecnica'
       String site         = 'APC'

       def resultAttribute = Dictionaryws.findByGroupIdAndGroupNameAndTypeAndSiteAndValueIdSite(groupId,
       groupName, type, site, valueSite.toString())


       if(resultAttribute){
           resultAttribute.valueIdMXP
       }else{
          return null
       }
    }
}
