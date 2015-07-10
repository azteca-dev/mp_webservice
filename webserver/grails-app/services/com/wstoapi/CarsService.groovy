package com.wstoapi

import grails.transaction.Transactional

import org.codehaus.groovy.grails.web.util.WebUtils

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Transactional
class CarsService {

    def authenticateService = new AuthenticateService()
    def homologaService
    def publicaService
    def logwsService

    static expose=['cxf']

    def Publicar_Anuncio(

            String usuario,
            String contrasenna,
            String NumInventarioEmpresa,
            String DescripconAuto,
            String Precio,
            String TipoMoneda,
            String Tipo_PrecioIDAutoplaza,
            String Kilometraje,
            String Tipo_KilometrajeIDAutoplaza,
            String ColorExterior,
            String ColorInterior,
            String Marca,
            String MarcaIDAutoplaza,
            String Modelo,
            String ModeloIDAutoplaza,
            String Submodelo,
            String SubmodeloIDInterno,
            String SubmodeloIDAutoplaza,
            String Anno,
            String TipoVehiculoIDAutoplaza,
            String TipoVestiduraIDAutoplaza,
            String TipoTransmisionIDAutoplaza,
            String StatusVehiculoIDAutoplaza,
            String EmpresaIDAutoplaza,
            String EmpresaIDinterno,
            String AutoBlindado,
            String AutoAccidentadoRecuperado,
            String NumSerieAuto,
            String Equipamiento,
            String UrlFoto1,
            String UrlFoto2,
            String UrlFoto3,
            String UrlFoto4,
            String UrlFoto5,
            String UrlFoto6,
            String UrlFoto7,
            String UrlFoto8,
            String UrlFoto9,
            String UrlFoto10,
            String UrlFoto11,
            String UrlFoto12,
            String UrlFoto13,
            String UrlFoto14,
            String UrlFoto15,
            String UrlFoto16,
            String UrlFoto17,
            String UrlFoto18,
            String UrlFoto19,
            String UrlFoto20,
            String UrlFoto21,
            String UrlFoto22,
            String UrlFoto23,
            String UrlFoto24,
            String UrlFoto25,
            String UrlFoto26,
            String UrlFoto27,
            String UrlFoto28,
            String UrlFoto29,
            String UrlFoto30,
            String UrlFoto31,
            String UrlFoto32,
            String EventoArealizar


    ) {

        // TODO eliminar estas trazas [INICIO] .... ......
        def requestCliente = WebUtils.retrieveGrailsWebRequest().getCurrentRequest()

        println "Lo que recibimos es hola:"
        println "QueryString---> "+requestCliente.getQueryString()
        println "headerNames---> "+requestCliente.headerNames
        println "parts---> "+requestCliente.parts
        println "method---> "+requestCliente.method
        println "requestURL---> "+requestCliente.requestURL
        println "ContentLenght---> "+requestCliente.contentLength
        println "ContentType---> "+requestCliente.contentType
        println "MetaPropertiesValues---> "+requestCliente.metaPropertyValues


        println "getRequestUrl--->"+requestCliente.getRequestURL()
        println "getAttributes(javax.servlet.forward.request_uri)--->"+requestCliente.getAttribute("javax.servlet.forward.request_uri")
        println "getAttribute(javax.servlet.forward.query_string)--->"+requestCliente.getAttribute("javax.servlet.forward.query_string")
        println "User-Agent--->"+requestCliente.getHeader("User-Agent")
        println "getServletPath()--->"+requestCliente.getServletPath()
        println "getContentType()--->"+requestCliente.getContentType()

        println "getParameterMap()--->"+requestCliente.getParameterMap()

        println "getProtocol()--->"+requestCliente.getProtocol()
        println "getScheme()--->"+requestCliente.getScheme()

        println "getParameterNames()--->"+requestCliente.getParameterNames()





        requestCliente.getAttributeNames().each{
            println "getAttributeNames--->>>"+it
        }

        println "Request Complete!! ..."
        requestCliente.each{
            println "--"+it
        }

        // TODO eliminar estas trazas [FIN] .... ......

        def response = "0"
        def accessToken
        def jsonVehicle
        def jsonVehicleUPD
        def userId
        def dealerId
        def logMap

        def dataLogMapOrigin =[

                usuario                     : usuario,
                contrasenna                 : contrasenna,
                NumInventarioEmpresa        : NumInventarioEmpresa,
                DescripconAuto              : DescripconAuto,
                Precio                      : Precio,
                TipoMoneda                  : TipoMoneda,
                Tipo_PrecioIDAutoplaza      : Tipo_PrecioIDAutoplaza,
                Kilometraje                 : Kilometraje,
                Tipo_KilometrajeIDAutoplaza : Tipo_KilometrajeIDAutoplaza,
                ColorExterior               : ColorExterior,
                ColorInterior               : ColorInterior,
                Marca                       : Marca,
                MarcaIDAutoplaza            : MarcaIDAutoplaza,
                Modelo                      : Modelo,
                ModeloIDAutoplaza           : ModeloIDAutoplaza,
                Submodelo                   : Submodelo,
                SubmodeloIDInterno          : SubmodeloIDInterno,
                SubmodeloIDAutoplaza        : SubmodeloIDAutoplaza,
                Anno                        : Anno,
                TipoVehiculoIDAutoplaza     : TipoVehiculoIDAutoplaza,
                TipoVestiduraIDAutoplaza    : TipoVestiduraIDAutoplaza,
                TipoTransmisionIDAutoplaza  : TipoTransmisionIDAutoplaza,
                StatusVehiculoIDAutoplaza   : StatusVehiculoIDAutoplaza,
                EmpresaIDAutoplaza          : EmpresaIDAutoplaza,
                EmpresaIDinterno            : EmpresaIDinterno,
                AutoBlindado                : AutoBlindado,
                AutoAccidentadoRecuperado   : AutoAccidentadoRecuperado,
                NumSerieAuto                : NumSerieAuto,
                Equipamiento                : Equipamiento,
                UrlFoto1                    : UrlFoto1,
                UrlFoto2                    : UrlFoto2,
                UrlFoto3                    : UrlFoto3,
                UrlFoto4                    : UrlFoto4,
                UrlFoto5                    : UrlFoto5,
                UrlFoto6                    : UrlFoto6,
                UrlFoto7                    : UrlFoto7,
                UrlFoto8                    : UrlFoto8,
                UrlFoto9                    : UrlFoto9,
                UrlFoto10                   : UrlFoto10,
                UrlFoto11                   : UrlFoto11,
                UrlFoto12                   : UrlFoto12,
                UrlFoto13                   : UrlFoto13,
                UrlFoto14                   : UrlFoto14,
                UrlFoto15                   : UrlFoto15,
                UrlFoto16                   : UrlFoto16,
                UrlFoto17                   : UrlFoto17,
                UrlFoto18                   : UrlFoto18,
                UrlFoto19                   : UrlFoto19,
                UrlFoto20                   : UrlFoto20,
                UrlFoto21                   : UrlFoto21,
                UrlFoto22                   : UrlFoto22,
                UrlFoto23                   : UrlFoto23,
                UrlFoto24                   : UrlFoto24,
                UrlFoto25                   : UrlFoto25,
                UrlFoto26                   : UrlFoto26,
                UrlFoto27                   : UrlFoto27,
                UrlFoto28                   : UrlFoto28,
                UrlFoto29                   : UrlFoto29,
                UrlFoto30                   : UrlFoto30,
                UrlFoto31                   : UrlFoto31,
                UrlFoto32                   : UrlFoto32,
                EventoArealizar             : EventoArealizar
        ]

        def DataWsMap = [

                User                    : usuario,
                Pass                    : contrasenna,
                StockNumber             : NumInventarioEmpresa,
                Description             : DescripconAuto,
                Price                   : Precio,
                TypeCurrency            : TipoMoneda,
                TypePriceMPId           : Tipo_PrecioIDAutoplaza,
                Kilometers              : Kilometraje,
                TypeKilometersMPId      : Tipo_KilometrajeIDAutoplaza,
                ExteriorColor           : ColorExterior,
                InteriorColor           : ColorInterior,
                Mark                    : Marca,
                MarkMPId                : MarcaIDAutoplaza,
                Model                   : Modelo,
                ModelMPId               : ModeloIDAutoplaza,
                Version                 : Submodelo,
                VersionIntId            : SubmodeloIDInterno,
                VersionMPId             : SubmodeloIDAutoplaza,
                Year                    : Anno,
                TypeVehicleMPId         : TipoVehiculoIDAutoplaza,
                TypeVestureMPId         : TipoVestiduraIDAutoplaza,
                TypeTransmissionMPId    : TipoTransmisionIDAutoplaza,
                StatusVehicleMPId       : StatusVehiculoIDAutoplaza,
                DealerMPId              : EmpresaIDAutoplaza,
                DealerIntId             : EmpresaIDinterno,
                VehicleArmour           : AutoBlindado,
                VehicleAccidentRecovered: AutoAccidentadoRecuperado,
                NumSerie                : NumSerieAuto,
                Equipment               : Equipamiento,
                Pic1                    : UrlFoto1,
                Pic2                    : UrlFoto2,
                Pic3                    : UrlFoto3,
                Pic4                    : UrlFoto4,
                Pic5                    : UrlFoto5,
                Pic6                    : UrlFoto6,
                Pic7                    : UrlFoto7,
                Pic8                    : UrlFoto8,
                Pic9                    : UrlFoto9,
                Pic10                   : UrlFoto10,
                Pic11                   : UrlFoto11,
                Pic12                   : UrlFoto12,
                Pic13                   : UrlFoto13,
                Pic14                   : UrlFoto14,
                Pic15                   : UrlFoto15,
                Pic16                   : UrlFoto16,
                Pic17                   : UrlFoto17,
                Pic18                   : UrlFoto18,
                Pic19                   : UrlFoto19,
                Pic20                   : UrlFoto20,
                Pic21                   : UrlFoto21,
                Pic22                   : UrlFoto22,
                Pic23                   : UrlFoto23,
                Pic24                   : UrlFoto24,
                Pic25                   : UrlFoto25,
                Pic26                   : UrlFoto26,
                Pic27                   : UrlFoto27,
                Pic28                   : UrlFoto28,
                Pic29                   : UrlFoto29,
                Pic30                   : UrlFoto30,
                Pic31                   : UrlFoto31,
                Pic32                   : UrlFoto32,
                Action                  : EventoArealizar

        ]


        String remoteAddress
        try{
            def request = WebUtils.retrieveGrailsWebRequest().getCurrentRequest()
            remoteAddress = request.getRemoteAddr()
        }catch(Exception e){
            remoteAddress = 'sin_direccion'
        }

        def validateData = homologaService.validateDataMap(DataWsMap)

        if(validateData.status != "0"){
            response = validateData.status+"-"+validateData.message
            logMap = logwsService.createMapLog(remoteAddress,dataLogMapOrigin,response, [:], Constants.LOG_STATUS_ERROR, Constants.LOG_ACTION_BEGIN, '', '', '', '')
            logwsService.createLog(logMap)

        }else {

            def result = authenticateService.login(DataWsMap.User, DataWsMap.Pass)

            if (result.data.access_token) {
                accessToken = result.data.access_token
                userId      = result.data.user_id
                def resultDealer = authenticateService.getDealer(userId, accessToken)

                if (resultDealer.data.dealer_id){

                    dealerId = resultDealer.data.dealer_id
                    def vehicleId = publicaService.searchVehicle(DataWsMap.StockNumber, dealerId, accessToken)


                    if(vehicleId != 0){


                            jsonVehicleUPD = homologaService.homologaDataUpdate(DataWsMap, userId, dealerId)
                            def respUpdApiVehicle = publicaService.updateVehicle(vehicleId, jsonVehicleUPD, accessToken)

                            if(respUpdApiVehicle.status == HttpServletResponse.SC_OK || respUpdApiVehicle.status == HttpServletResponse.SC_CREATED ) {
                                response = "0 - Actualizamos el vehiculo correctamente"
                                logMap = logwsService.createMapLog(remoteAddress, dataLogMapOrigin, response, [ status:respUpdApiVehicle.status], Constants.LOG_STATUS_SUCCESSFUL, Constants.LOG_ACTION_UPDATE, userId, dealerId, vehicleId, '')
                                logwsService.createLog(logMap)
                            }else{
                                response = "8 - No pudimos actualizar el vehiculo"
                                logMap = logwsService.createMapLog(remoteAddress, dataLogMapOrigin, response, respUpdApiVehicle, Constants.LOG_STATUS_ERROR, Constants.LOG_ACTION_UPDATE, userId, dealerId, vehicleId, '')
                                logwsService.createLog(logMap)
                            }


                        try {
                            def respUpdateImages = publicaService.updateImages(DataWsMap, accessToken, respUpdApiVehicle.data.id)
                            if(respUpdApiVehicle == HttpServletResponse.SC_CREATED || respUpdApiVehicle.status == HttpServletResponse.SC_OK) {
                                logMap = logwsService.createMapLog(remoteAddress, dataLogMapOrigin, response, respUpdateImages, Constants.LOG_STATUS_SUCCESSFUL, Constants.LOG_ACTION_UPDATE, userId, dealerId, vehicleId, '')
                            }else{
                                logMap = logwsService.createMapLog(remoteAddress, dataLogMapOrigin, response, respUpdateImages, Constants.LOG_STATUS_ERROR, Constants.LOG_ACTION_UPDATE, userId, dealerId, vehicleId, '')
                            }
                            logwsService.createLog(logMap)
                        }catch(Exception e){
                            response = "0 - Actualizamos el vehiculo, pero no las fotos"
                            logMap = logwsService.createMapLog(remoteAddress, dataLogMapOrigin, response, [:], Constants.LOG_STATUS_ERROR, Constants.LOG_ACTION_UPDATE, userId, dealerId, vehicleId, '')
                            logwsService.createLog(logMap)
                        }

                    }else{

                        try{
                            jsonVehicle = homologaService.homologaData(DataWsMap, userId, dealerId)
                            def respApiVehicle = publicaService.createVehicle(jsonVehicle, accessToken)

                            if (respApiVehicle.data.id){
                                response = "0 - "+respApiVehicle.data.id
                                logMap = logwsService.createMapLog(remoteAddress,dataLogMapOrigin,response, [status:respApiVehicle.status], Constants.LOG_STATUS_SUCCESSFUL, Constants.LOG_ACTION_INSERT, userId, dealerId, respApiVehicle.data.id, '')
                                logwsService.createLog(logMap)

                                def respImagesProcess = publicaService.postImages(DataWsMap, accessToken, respApiVehicle.data.id, respApiVehicle.data)
                                if(respImagesProcess.status == HttpServletResponse.SC_OK || respImagesProcess.status == HttpServletResponse.SC_CREATED ){
                                    logMap = logwsService.createMapLog(remoteAddress,dataLogMapOrigin,response, respImagesProcess, Constants.LOG_STATUS_SUCCESSFUL, Constants.LOG_ACTION_INSERT, userId, dealerId, respApiVehicle.data.id, '')
                                }else{
                                    logMap = logwsService.createMapLog(remoteAddress,dataLogMapOrigin,response, respImagesProcess, Constants.LOG_STATUS_ERROR, Constants.LOG_ACTION_INSERT, userId, dealerId, respApiVehicle.data.id, '')
                                }
                                logwsService.createLog(logMap)
                            }else{
                                response = "8 - "+respApiVehicle.data.message
                                logMap = logwsService.createMapLog(remoteAddress,dataLogMapOrigin, response, respApiVehicle, Constants.LOG_STATUS_ERROR, Constants.LOG_ACTION_INSERT, userId, dealerId, '', '')
                                logwsService.createLog(logMap)
                            }
                        }catch(Exception e){
                            response = "-1 - No se pudo hacer el update con vehicle sin fotos error:"+e.message.toString()
                            logMap = logwsService.createMapLog(remoteAddress, dataLogMapOrigin, response, [:], Constants.LOG_STATUS_ERROR, Constants.LOG_ACTION_UPDATE, userId, dealerId, '', '')
                            logwsService.createLog(logMap)
                        }

                    }


                }else{

                    response = "8 - El usuario no tiene un dealer asociado"
                    logMap = logwsService.createMapLog(remoteAddress,dataLogMapOrigin,response, resultDealer, Constants.LOG_STATUS_ERROR, Constants.LOG_ACTION_BEGIN, '', '', '', '')
                    logwsService.createLog(logMap)
                }

            } else {

                response = "1 - El usuario y/o contraseña no son validos"
                logMap = logwsService.createMapLog(remoteAddress,dataLogMapOrigin,response, result, Constants.LOG_STATUS_ERROR, Constants.LOG_ACTION_BEGIN, '', '', '', '')
                logwsService.createLog(logMap)
            }

        }

        return  response

    }


    def Borrar_Anuncio(String usuario, String contraseña, String empresaID_, String NumInventarioCliente){


        def dataLogMapOrigin =[
                usuario:usuario,
                contraseña:contraseña,
                empresaID_:empresaID_,
                NumInventarioCliente:NumInventarioCliente
        ]

        def dataMap = [
                usuario:usuario,
                pass:contraseña,
                dealerId:empresaID_,
                stockNumber:NumInventarioCliente
        ]


        def accessToken
        def userId
        def dealerId
        def response
        def logMap

        String remoteAddress
        try{
            def request = WebUtils.retrieveGrailsWebRequest().getCurrentRequest()
            remoteAddress = request.getRemoteAddr()
        }catch(Exception e){
            remoteAddress = 'sin_direccion'
        }

        if(!dataMap.usuario){
            response = "8 - El Usuario es requerido"
            logMap = logwsService.createMapLog(remoteAddress,dataLogMapOrigin, response, [:], Constants.LOG_STATUS_ERROR, Constants.LOG_ACTION_DELETE, '', '', '', '')
            logwsService.createLog(logMap)
            return response
        }
        if(!dataMap.pass){
            response = "8 - La contraseña es requerida"
            logMap = logwsService.createMapLog(remoteAddress,dataLogMapOrigin, response, [:], Constants.LOG_STATUS_ERROR, Constants.LOG_ACTION_DELETE, '', '', '', '')
            logwsService.createLog(logMap)
            return response
        }
        if(!dataMap.stockNumber){
            response = "8 - El NumeroInventarioCliente es requerido"
            logMap = logwsService.createMapLog(remoteAddress,dataLogMapOrigin, response, [:], Constants.LOG_STATUS_ERROR, Constants.LOG_ACTION_DELETE, '', '', '', '')
            logwsService.createLog(logMap)
            return response
        }

        def result = authenticateService.login(dataMap.usuario, dataMap.pass)

        if (result.data.access_token) {
            accessToken = result.data.access_token
            userId      = result.data.user_id
            def resultDealer = authenticateService.getDealer(userId, accessToken)

            if (resultDealer.data.dealer_id) {

                dealerId = resultDealer.data.dealer_id

                def vehicleId = publicaService.searchVehicle(dataMap.stockNumber, dealerId, accessToken)

                if(vehicleId != 0) {

                    def resultDeleted = publicaService.deleteVehicle(vehicleId, accessToken)

                    if(resultDeleted.data.message){
                        response = "0 - El vehiculo con numero de inventario "+ dataMap.stockNumber+ "fue borrado"
                        logMap = logwsService.createMapLog(remoteAddress,dataLogMapOrigin, response, resultDeleted, Constants.LOG_STATUS_SUCCESSFUL, Constants.LOG_ACTION_DELETE, userId, dealerId, vehicleId, '')
                        logwsService.createLog(logMap)
                    }else{
                        response = "0 - Ocurrio un problema con el borrado"
                        logMap = logwsService.createMapLog(remoteAddress,dataLogMapOrigin, response, resultDeleted, Constants.LOG_STATUS_ERROR, Constants.LOG_ACTION_DELETE, userId, dealerId, vehicleId, '')
                        logwsService.createLog(logMap)
                    }
                }else{
                   response = "2 - El vehiculo con numero de  inventario ya fue borrado o no fue encontrado"
                   logMap = logwsService.createMapLog(remoteAddress,dataLogMapOrigin, response, [:], Constants.LOG_STATUS_SUCCESSFUL, Constants.LOG_ACTION_DELETE, userId, dealerId, vehicleId, '')
                   logwsService.createLog(logMap)
                }

            }else{
                response = "8 - El usuario no tiene un dealer asociado"
                logMap = logwsService.createMapLog(remoteAddress,dataLogMapOrigin, response, resultDealer, Constants.LOG_STATUS_ERROR, Constants.LOG_ACTION_DELETE, userId, '', '', '')
                logwsService.createLog(logMap)
            }

        }else {
            response = "1 - El usuario y/o contraseña no son validos"
            logMap = logwsService.createMapLog(remoteAddress,dataLogMapOrigin, response, [:], Constants.LOG_STATUS_ERROR, Constants.LOG_ACTION_DELETE, '', '', '', '')
            logwsService.createLog(logMap)
        }

        return response
    }
}
