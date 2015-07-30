package com.wstoapi

import grails.transaction.Transactional
import maxipublica.Dictionaryws
import rest.RestService

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.joda.time.LocalTime
import groovy.time.TimeCategory
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.ISODateTimeFormat
import java.util.Calendar
import java.util.Date

@Transactional
class HomologaService {

    def restService = new RestService()
    def marcaGeneral = ""
    def modeloGeneral = ""
    def annioGeneral = ""
    def versionGeneral = ""

    private void setMarcaGeneral(String marcaGeneral){
        this.marcaGeneral = marcaGeneral
    }
    private String getMarcaGeneral(){
        return this.marcaGeneral
    }


    private void setModeloGeneral(String modeloGeneral){
        this.modeloGeneral = modeloGeneral
    }
    private String getModeloGeneral(){
        return this.modeloGeneral
    }


    private void setAnnioGeneral(String annioGeneral){
        this.annioGeneral = annioGeneral
    }
    private String getAnnioGeneral(){
        return this.annioGeneral
    }


    private void setVersionGeneral(String versionGeneral){
        this.versionGeneral = versionGeneral
    }
    private String getVersionGeneral(){
        return this.versionGeneral
    }





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
                        mlm:[
                                status:"waiting",
                                action:"to_publish"
                                //official_store_id:"235" Lo quitamos por peticion de Erick que le dijo Miguel 14-07-2015
                        ]
                ],
                origin_create:"webservice"

        ]

        jsonVehicleForPost
    }


    def createJsonVehicleUPD(def dataMap, def userId, def dealerId) {


        def jsonVehicleForPut = [
                kilometers:dataMap.Kilometers,
                price:dataMap.Price,
                description:dataMap.Description,
                attributes:getAttributes(dataMap.TypeCurrency, dataMap.ExteriorColor, dataMap.InteriorColor, dataMap.TypeVestureMPId, dataMap.TypeTransmissionMPId, dataMap.TypeVehicleMPId),
                equipment:getEquipment(dataMap.Equipment),
                published_sites:[
                        mlm:[
                                status:"waiting",
                                action:"to_publish"
                                //official_store_id:"235" Lo quitamos por peticion de Erick que le dijo Miguel 14-07-2015
                        ]
                ],
                origin_update:"webservice"


        ]

        jsonVehicleForPut
    }

    def createJsonImages (def listImages, def stockNumber){

        def images = []

        listImages.each{
            if(it && it.contains("AutoID="+stockNumber)){
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
            response.status = "400"
            response.message = "El Usuario es requerido"
            response.error = "bad_request"
        }
        if(!dataMap.Pass){
            response.status = "400"
            response.message = "La Contraseña es requerida"
            response.error = "bad_request"
        }

        if(!dataMap.StockNumber){
            response.status = "400"
            response.message = "El NumInventarioEmpresa es requerido"
            response.error = "bad_request"
        }
        if(!dataMap.Price){
            response.status = "400"
            response.message = "El Precio es requerido"
            response.error = "bad_request"
        }
        if(!dataMap.TypeCurrency){
            response.status = "400"
            response.message = "El TipoMoneda es requerido"
            response.error = "bad_request"
        }
        if(!dataMap.Kilometers){
            response.status = "400"
            response.message = "El Kilometraje es requerido"
            response.error = "bad_request"
        }
        if(!dataMap.ExteriorColor){
            response.status = "400"
            response.message = "El ColorExterior es requerido"
            response.error = "bad_request"
        }
        if(!dataMap.InteriorColor){
            response.status = "400"
            response.message = "El ColorExterior es requerido"
            response.error = "bad_request"
        }
        if(!dataMap.Mark){
            response.status = "400"
            response.message = "La Marca es requerida"
            response.error = "bad_request"
        }
        if(!dataMap.MarkMPId){
            response.status = "400"
            response.message = "La MarcaIDAutoplaza es requerida"
            response.error = "bad_request"
        }
        if(!dataMap.Model){
            response.status = "400"
            response.message = "El Modelo es requerido"
            response.error = "bad_request"
        }
        if(!dataMap.ModelMPId){
            response.status = "400"
            response.message = "El ModeloIDAutoplaza es requerido"
            response.error = "bad_request"
        }
        if(!dataMap.Version){
            response.status = "400"
            response.message = "El Submodelo es requerdio"
            response.error = "bad_request"
        }
        if(!dataMap.VersionMPId){
            response.status = "400"
            response.message = "El SubmodeloIDAutoplaza es requerido"
            response.error = "bad_request"
        }
        if(!dataMap.Year){
            response.status = "400"
            response.message = "El Anno es requerido"
            response.error = "bad_request"
        }
        if(!dataMap.TypeVehicleMPId){
            response.status = "400"
            response.message = "El TipoVehicleIDAutoplaza es requerido"
            response.error = "bad_request"
        }
        if(!dataMap.TypeVestureMPId){
            response.status = "400"
            response.message = "El TipoVestiduraIDAutoplaza es requerido"
            response.error = "bad_request"
        }
        if(!dataMap.TypeTransmissionMPId){
            response.status = "400"
            response.message = "El TipoTransmisionIDAutoplaza es requerido"
            response.error = "bad_request"
        }
        if(!dataMap.StatusVehicleMPId){
            response.status = "400"
            response.message = "El StatusVehiculoIDAutoplaza es requerido"
            response.error = "bad_request"
        }
        if(!dataMap.Action){
            response.status = "400"
            response.message = "El EventoArealizar es requerido"
            response.error = "bad_request"
        }


        response
    }

    def processIdMXP ( def dataMap, def accessToken){

        dataMap.MarkMPId    = processCatalog("MAR", dataMap.MarkMPId)
        dataMap.ModelMPId   = processCatalog("MOD", dataMap.ModelMPId)
        dataMap.VersionMPId = processCatalog("VER", dataMap.VersionMPId)

        //Valida si no existe la version del catalogo que ellos estan mandando
        if(!existCatalog(dataMap.VersionMPId, accessToken)){
            //ahora validamos que exista el modelo que ellos estan mandanto            
            //dataMap.VersionMPId = getVersionNotCatalog(dataMap.ModelMPId, dataMap.Year,accessToken )

            //ahora validamos que exista el modelo que ellos estan mandando            
            if(existCatalog(dataMap.ModelMPId, accessToken)){
                dataMap.ModelMPId = getVersionNotCatalog(dataMap.ModelMPId, accessToken )
                dataMap.VersionMPId = getVersionGeneral()
            }else if(existCatalog(dataMap.MarkMPId, accessToken)){
                dataMap.MarkMPId = getModeloNotCatalog(dataMap.MarkMPId, accessToken)
                dataMap.ModelMPId = getModeloGeneral()
                dataMap.VersionMPId = getVersionGeneral()
            }
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


    def existCatalog (def versionId, def accessToken){

        def getResutlVersion = getCategoryCatalog(versionId, accessToken)

        if(getResutlVersion.status == HttpServletResponse.SC_OK){
            return true
        }else{
            return false
        }
    }

    //debe de Buscar Otra Version
    def getVersionNotCatalog(def modeloId, def valueYear, def accessToken){


        def versionId = "-1"
        def yearId = getYearIdFromModeloAndYear(modeloId, valueYear, accessToken)
        if(yearId != "0"){
            def versiones = getCategoryCatalog(yearId, accessToken)
            if(versiones.status == HttpServletResponse.SC_OK){
                versionId = versiones.data.children_categories.each{
                    if (it.category_id.substring(0, 7).equals("VEROTRO")) {
                        versionId = it.category_id
                    }
                }
            }
        }

        versionId

    }
    //Retorna el año que se esta buscando si no debe de retornar Otro Años length()
    def getYearIdFromModeloAndYear(def modeloId, def valueYear, def accessToken){


        def yearId = "0"
        def yearOtro = "0"
        def control = false
        def result = getCategoryCatalog(modeloId, accessToken)

        if (result.status == HttpServletResponse.SC_OK){
            result.data.children_categories.each{
                if(it.name == valueYear){
                    yearId = it.category_id
                }
                if (it.category_id.substring(0, 7).equals("YEROTRO")) {
                    yearOtro = it.category_id
                }
            }
        }

        if ((yearId == "0") && (yearOtro != "0")) {
            yearId = yearOtro
        }

        yearId

    }

    //Asigna ModeloGeneral, AnnioGeneral y VersionGeneral
    def getVersionNotCatalog(def modeloId, def accessToken){
        def marOtro = "-1"
        def modOtro = "-1"
        def yerOtro = "-1"
        def verOtro = "-1"
        def result = getCategoryCatalog(modeloId, accessToken)

        setModeloGeneral(modOtro)
        setAnnioGeneral(yerOtro)
        setVersionGeneral(verOtro)

        if(result.data.children_categories){
            result.data.children_categories.each{
                if((it.category_id.length() >= 7) && (it.category_id.substring(0, 7).equals("YEROTRO"))) {
                    yerOtro = it.category_id
                }
            }
        }

        result = getCategoryCatalog(yerOtro, accessToken)
        if(result.data.children_categories){
            result.data.children_categories.each{
                if((it.category_id.length() >= 7) && (it.category_id.substring(0, 7).equals("VEROTRO"))) {
                    verOtro = it.category_id
                }
            }
        }

        setModeloGeneral(modeloId)
        setAnnioGeneral(yerOtro)
        setVersionGeneral(verOtro)

        modeloId

    }

    //Deve de buscar Otra Marca
    //Verificar que es lo que retorna para saber si retornar la version o la marca que recive puesto que puede que exista o no
    //Asigna MarcaGeneral, ModeloGeneral, AnnioGeneral y VersionGeneral
    private def getModeloNotCatalog(def marcaId, def accessToken){
        def marOtro = "-1"
        def modOtro = "-1"
        def yerOtro = "-1"
        def verOtro = "-1"
        def result = getCategoryCatalog(marcaId, accessToken)

        setMarcaGeneral(marOtro)
        setModeloGeneral(modOtro)
        setAnnioGeneral(yerOtro)
        setVersionGeneral(verOtro)
        
        if(result.data.children_categories) {
            result.data.children_categories.each{
                if((it.category_id.length() >= 7) && (it.category_id.substring(0, 7).equals("MODOTRO"))) {
                    modOtro = it.category_id
                }
            }
        }

        result = getCategoryCatalog(modOtro, accessToken)
        if(result.data.children_categories){
            result.data.children_categories.each{
                if((it.category_id.length() >= 7) && (it.category_id.substring(0, 7).equals("YEROTRO"))) {
                    yerOtro = it.category_id
                }
            }
        }

        result = getCategoryCatalog(yerOtro, accessToken)
        if(result.data.children_categories){
            result.data.children_categories.each{
                if((it.category_id.length() >= 7) && (it.category_id.substring(0, 7).equals("VEROTRO"))) {
                    verOtro = it.category_id
                }
            }
        }

        setMarcaGeneral(marcaId)
        setModeloGeneral(modOtro)
        setAnnioGeneral(yerOtro)
        setVersionGeneral(verOtro)

        marcaId

        //marcaId
    }

    //Recupera una categoria en especifico
    def getCategoryCatalog(def categoryId, def accessToken){

        def queryParams = [
                access_token:accessToken
        ]


        def result = restService.getResource("/catalog/MX/MLM/${categoryId}/", queryParams)

        result
    }

    def getModelCatalog(def categoryId, def accessToken){

        def queryParams = [
                access_token:accessToken
        ]

        def result = restService.getResource("/catalog/MX/MXP/${categoryId}/", queryParams)

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

    //Cuando se agrege la hora correcta al tomcat quitar este metodo y su llamada en la linea 215
    def fechaMenosTiempo(def value){
        def ultimaF
        def fechaISO = ISODateTimeFormat.dateTimeParser().parseDateTime(value).toDate()
        use(TimeCategory){
            ultimaF = fechaISO-5.hour
        }
        return ultimaF
    }
    //Cuando se agrege la hora correcta al tomcat quitar este metodo y su llamada en las lineas 186 y 196
    def fechaMasTiempo(def value){
        def ultimaF
        def fechaISO = ISODateTimeFormat.dateTimeParser().parseDateTime(value).toDate()
        use(TimeCategory){
            ultimaF = fechaISO+5.hour
        }
        return ultimaF
    }
}
