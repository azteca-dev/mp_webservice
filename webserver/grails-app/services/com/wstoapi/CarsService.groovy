package com.wstoapi

import grails.transaction.Transactional

import org.codehaus.groovy.grails.web.util.WebUtils

import javax.servlet.http.HttpServletResponse


@Transactional
class CarsService {

    def authenticateService = new AuthenticateService()
    def homologaService
    def publicaService
    def logwsService
    def updateToPublish

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

        def DataWsMap = [:]
        def dataLogMapOrigin = [:]
        def response = "0"
        def accessToken
        def jsonVehicle
        def jsonVehicleUPD
        def userId
        def dealerId
        def logMap

        dataLogMapOrigin =[

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

        DataWsMap = [

                User                    : usuario,
                Pass                    : contrasenna,
                StockNumber             : NumInventarioEmpresa,
                Description             : "Numinv: " + NumInventarioEmpresa + ", " + DescripconAuto,
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
            response = validateData.status + " - " + validateData.error + ": " +validateData.message
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

                    //Esta parte del if es para verificar si se realiza una actualizacion o un POST nuevo
                    //(vehicleId != 0) es una actualizacion
                    if(vehicleId != 0){
                            jsonVehicleUPD = homologaService.homologaDataUpdate(DataWsMap, userId, dealerId, accessToken)
                            def respUpdApiVehicle = publicaService.updateVehicle(vehicleId, jsonVehicleUPD, accessToken)
                            try {
                            if(respUpdApiVehicle.status == HttpServletResponse.SC_OK || respUpdApiVehicle.status == HttpServletResponse.SC_CREATED || respUpdApiVehicle.status == 200 || respUpdApiVehicle.status == 201) {
                                if(EventoArealizar == "2"){
                                    response = "200 - successfull: stock_number - " + (respUpdApiVehicle.data.stock_number ? respUpdApiVehicle.data.stock_number : "") + ", ws_id - " + (respUpdApiVehicle.data.id ? respUpdApiVehicle.data.id : "") + ". Actualizamos el vehiculo correctamente al republicar"
                                    //logMap = logwsService.createMapLog(remoteAddress, dataLogMapOrigin, response, respUpdApiVehicle, Constants.LOG_STATUS_SUCCESSFUL, Constants.LOG_ACTION_REPUBLISH, userId, dealerId, vehicleId, '')
                                }else{
                                    response = "200 - successfull: stock_number - " + (respUpdApiVehicle.data.stock_number ? respUpdApiVehicle.data.stock_number : "") + ", ws_id - " + (respUpdApiVehicle.data.id ? respUpdApiVehicle.data.id : "") + ". Actualizamos el vehiculo correctamente"
                                    //logMap = logwsService.createMapLog(remoteAddress, dataLogMapOrigin, response, respUpdApiVehicle, Constants.LOG_STATUS_SUCCESSFUL, Constants.LOG_ACTION_UPDATE, userId, dealerId, vehicleId, '')
                                }
                                /*logwsService.createLog(logMap)*/
                            }else{
                                if(EventoArealizar == "2"){
                                    response = "400 - bad_request: stock_number - " + (respUpdApiVehicle.data.stock_number ? respUpdApiVehicle.data.stock_number : "") + ", ws_id - " + (respUpdApiVehicle.data.id ? respUpdApiVehicle.data.id : "")  + ". No pudimos actualizar el vehiculo al republicar"
                                    //logMap = logwsService.createMapLog(remoteAddress, dataLogMapOrigin, response, [ status:respUpdApiVehicle.status, jsonEnvio:jsonVehicleUPD], Constants.LOG_STATUS_ERROR, Constants.LOG_ACTION_REPUBLISH, userId, dealerId, vehicleId, '')
                                }else{
                                    response = "400 - bad_request: stock_number - " + (respUpdApiVehicle.data.stock_number ? respUpdApiVehicle.data.stock_number : "") + ", ws_id - " + (respUpdApiVehicle.data.id ? respUpdApiVehicle.data.id : "")  + ". No pudimos actualizar el vehiculo"
                                    //logMap = logwsService.createMapLog(remoteAddress, dataLogMapOrigin, response, [ status:respUpdApiVehicle.status, jsonEnvio:jsonVehicleUPD], Constants.LOG_STATUS_ERROR, Constants.LOG_ACTION_UPDATE, userId, dealerId, vehicleId, '')
                                }
                                /*logwsService.createLog(logMap)*/
                            }
                        
                            def respUpdateImages = publicaService.updateImages(DataWsMap, accessToken, respUpdApiVehicle.data.id)
                            
                            if(respUpdateImages == HttpServletResponse.SC_CREATED || respUpdateImages.status == HttpServletResponse.SC_OK || respUpdateImages.status == 200 || respUpdateImages.status == 201) {
                                if(EventoArealizar == "2"){
                                    response += ". Actualizamos correctamente las imagenes al republicar"
                                    logMap = logwsService.createMapLog(remoteAddress, dataLogMapOrigin, response, [vehicle:respUpdApiVehicle, status:respUpdateImages], Constants.LOG_STATUS_SUCCESSFUL, Constants.LOG_ACTION_REPUBLISH, userId, dealerId, vehicleId, '')
                                }else{
                                    response += ". Actualizamos correctamente las imagenes"
                                    logMap = logwsService.createMapLog(remoteAddress, dataLogMapOrigin, response, [vehicle:respUpdApiVehicle, status:respUpdateImages], Constants.LOG_STATUS_SUCCESSFUL, Constants.LOG_ACTION_UPDATE, userId, dealerId, vehicleId, '')
                                }
                            }else{
                                if(EventoArealizar == "2"){
                                    response += ". No pudimos actualizar las imagenes al republicar"
                                    logMap = logwsService.createMapLog(remoteAddress, dataLogMapOrigin, response, [vehicle:respUpdApiVehicle, status:respUpdateImages], Constants.LOG_STATUS_ERROR, Constants.LOG_ACTION_REPUBLISH, userId, dealerId, vehicleId, '')
                                }else{
                                    response += ". No pudimos actualizar las imagenes"
                                    logMap = logwsService.createMapLog(remoteAddress, dataLogMapOrigin, response, [vehicle:respUpdApiVehicle, status:respUpdateImages], Constants.LOG_STATUS_ERROR, Constants.LOG_ACTION_UPDATE, userId, dealerId, vehicleId, '')
                                }
                            }
                            updateToPublish = publicaService.updateVehicle(respUpdApiVehicle.data.id, [published_sites:[mlm:[status:"waiting",action:"to_publish"]]], accessToken)
                            logwsService.createLog(logMap)
                        }catch(Exception e){
                            response = "400 - error: Algo malo ocurrio al actualizar el vehiculo"
                            logMap = logwsService.createMapLog(remoteAddress, dataLogMapOrigin, response, [:], Constants.LOG_STATUS_ERROR, Constants.LOG_ACTION_UPDATE, userId, dealerId, vehicleId, '')
                            logwsService.createLog(logMap)
                        }
                    }else{
                        try{
                            jsonVehicle = homologaService.homologaData(DataWsMap, userId, dealerId, accessToken)
                            def respApiVehicle = publicaService.createVehicle(jsonVehicle, accessToken)

                            if (respApiVehicle.data.id){
                                response = "200 - successfull: stock_number - " + (respApiVehicle.data.stock_number ? respApiVehicle.data.stock_number : "") + ", ws_id - " + (respApiVehicle.data.id ? respApiVehicle.data.id : "")
                                //logMap = logwsService.createMapLog(remoteAddress,dataLogMapOrigin,response, [status:respApiVehicle], Constants.LOG_STATUS_SUCCESSFUL, Constants.LOG_ACTION_INSERT, userId, dealerId, respApiVehicle.data.id, '')
                                //logwsService.createLog(logMap)

                                def respImagesProcess = publicaService.postImages(DataWsMap, accessToken, respApiVehicle.data.id, respApiVehicle.data)
                                if(respImagesProcess.status == HttpServletResponse.SC_OK || respImagesProcess.status == HttpServletResponse.SC_CREATED || respImagesProcess.status == 200 || respImagesProcess.status == 201){
                                    response += ". Se insertaron correctamente las imagenes al publicar"
                                    logMap = logwsService.createMapLog(remoteAddress,dataLogMapOrigin,response, [vehicle:respApiVehicle, images:respImagesProcess], Constants.LOG_STATUS_SUCCESSFUL, Constants.LOG_ACTION_INSERT, userId, dealerId, respApiVehicle.data.id, '')
                                }else{
                                    response += ". No se pudo insertar correctamente las imagenes al publicar"
                                    logMap = logwsService.createMapLog(remoteAddress,dataLogMapOrigin,response, [vehicle:respApiVehicle, images:respImagesProcess], Constants.LOG_STATUS_ERROR, Constants.LOG_ACTION_INSERT, userId, dealerId, respApiVehicle.data.id, '')
                                }
                                logwsService.createLog(logMap)
                            }else{
                                //Se busca en el catalogo las marcas posibles para el modelo dado por FORD
                                def referenceModel = homologaService.processCatalog("MAR", DataWsMap.MarkMPId)
                                def referenceModels = homologaService.getModelCatalog(referenceModel, result.data.access_token)
                                def arrayModelos = '"'+'modeloID'+'":"'+'modelo",'
                                referenceModels.data.children_categories.each{
                                    arrayModelos += ('"' + it.categoryId.replace('MOD', '') + '":"'+ it.name +'",')
                                }
                                //Esta linea posiblemente retorne el estatus del catalogo
                                response = "404 - " + respApiVehicle.data.message + (referenceModels.data.status ? "" : (", Intentar alguno de los siguientes IDs: " + arrayModelos))
                                logMap = logwsService.createMapLog(remoteAddress,dataLogMapOrigin, response, [status:respApiVehicle.status, jsonEnvio:jsonVehicle], Constants.LOG_STATUS_ERROR, Constants.LOG_ACTION_INSERT, userId, dealerId, '', '')
                                logwsService.createLog(logMap)
                            }
                        }catch(Exception e){
                            response = "400 - bad_request: "+e.message.toString()
                            logMap = logwsService.createMapLog(remoteAddress, dataLogMapOrigin, response, [:], Constants.LOG_STATUS_ERROR, Constants.LOG_ACTION_UPDATE, userId, dealerId, '', '')
                            logwsService.createLog(logMap)
                        }

                    }


                }else{

                    response = "400 - bad_request: El usuario no tiene un dealer asociado"
                    logMap = logwsService.createMapLog(remoteAddress,dataLogMapOrigin,response, resultDealer, Constants.LOG_STATUS_ERROR, Constants.LOG_ACTION_BEGIN, '', '', '', '')
                    logwsService.createLog(logMap)
                }

            } else {

                response = "400 - bad_request: El usuario y/o contraseña no son validos"
                logMap = logwsService.createMapLog(remoteAddress,dataLogMapOrigin,response, result, Constants.LOG_STATUS_ERROR, Constants.LOG_ACTION_BEGIN, '', '', '', '')
                logwsService.createLog(logMap)
            }

        }
        return  response


/*println "usuario: " + usuario
println "pass: " + contrasenna
println "stockNumber: " + NumInventarioEmpresa
println "description: " + DescripconAuto
println "precio: " + Precio
println "top moneda: " + TipoMoneda
println "tipo precio apc: " + Tipo_PrecioIDAutoplaza
println "kilometraje: " + Kilometraje
println "tipo de kilometraje apc: " + Tipo_KilometrajeIDAutoplaza
println "ColorExterior: " + ColorExterior
println "ColorInterior: " + ColorInterior
println "marca: " + Marca
println "marca apc: " + MarcaIDAutoplaza
println "modelo: " + Modelo
println "modelo apc: " + ModeloIDAutoplaza
println "Submodelo: " + Submodelo
println "SubmodeloIDInterno: " + SubmodeloIDInterno
println "SubmodeloIDAutoplaza: " + SubmodeloIDAutoplaza
println "año: " + Anno
println "TipoVehiculoIDAutoplaza: " + TipoVehiculoIDAutoplaza
println "TipoVestiduraIDAutoplaza: " + TipoVestiduraIDAutoplaza
println "TipoTransmisionIDAutoplaza: " + TipoTransmisionIDAutoplaza
println "StatusVehiculoIDAutoplaza: " + StatusVehiculoIDAutoplaza
println "EmpresaIDAutoplaza: " + EmpresaIDAutoplaza
println "EmpresaIDinterno: " + EmpresaIDinterno
println "AutoBlindado: " + AutoBlindado
println "AutoAccidentadoRecuperado: " + AutoAccidentadoRecuperado
println "NumSerieAuto: " + NumSerieAuto
println "Equipamiento: " + Equipamiento
println "foto: " + UrlFoto1
println "foto: " + UrlFoto2
println "foto: " + UrlFoto3
println "foto: " + UrlFoto4
println "foto: " + UrlFoto5
println "foto: " + UrlFoto6
println "foto: " + UrlFoto7
println "foto: " + UrlFoto8
println "foto: " + UrlFoto9
println "foto: " + UrlFoto10
println "foto: " + UrlFoto11
println "foto: " + UrlFoto12
println "foto: " + UrlFoto13
println "foto: " + UrlFoto14
println "foto: " + UrlFoto15
println "foto: " + UrlFoto16
println "foto: " + UrlFoto17
println "foto: " + UrlFoto18
println "foto: " + UrlFoto19
println "foto: " + UrlFoto20
println "foto: " + UrlFoto21
println "foto: " + UrlFoto22
println "foto: " + UrlFoto23
println "foto: " + UrlFoto24
println "foto: " + UrlFoto25
println "foto: " + UrlFoto26
println "foto: " + UrlFoto27
println "foto: " + UrlFoto28
println "foto: " + UrlFoto29
println "foto: " + UrlFoto30
println "foto: " + UrlFoto31
println "foto: " + UrlFoto32
println "evento a realizar: " + EventoArealizar
println ":-------------------------------D"*/
}


    def Borrar_Anuncio(String usuario, String contraseña, String empresaID_, String NumInventarioCliente){
        def DataWsMap = [:]
        def dataLogMapOrigin = [:]
        
        dataLogMapOrigin =[
                usuario:usuario,
                contraseña:contraseña,
                empresaID_:empresaID_,
                NumInventarioCliente:NumInventarioCliente
        ]

        dataMap = [
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
            response = "400 - bad_request: El Usuario es requerido"
            logMap = logwsService.createMapLog(remoteAddress,dataLogMapOrigin, response, [:], Constants.LOG_STATUS_ERROR, Constants.LOG_ACTION_DELETE, '', '', '', '')
            logwsService.createLog(logMap)
            return response
        }
        if(!dataMap.pass){
            response = "400 - bad_request: La contraseña es requerida"
            logMap = logwsService.createMapLog(remoteAddress,dataLogMapOrigin, response, [:], Constants.LOG_STATUS_ERROR, Constants.LOG_ACTION_DELETE, '', '', '', '')
            logwsService.createLog(logMap)
            return response
        }
        if(!dataMap.stockNumber){
            response = "400 - bad_request: El NumeroInventarioCliente es requerido"
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
                        response = "404 - not_found: El vehiculo con numero de inventario " + dataMap.stockNumber+ " fue borrado"
                        logMap = logwsService.createMapLog(remoteAddress,dataLogMapOrigin, response, resultDeleted, Constants.LOG_STATUS_SUCCESSFUL, Constants.LOG_ACTION_DELETE, userId, dealerId, vehicleId, '')
                        logwsService.createLog(logMap)
                    }else{
                        response = "400 - bad_request: Ocurrio un problema al borrar el vehiculo con stock_number: " + dataMap.stockNumber
                        logMap = logwsService.createMapLog(remoteAddress,dataLogMapOrigin, response, resultDeleted, Constants.LOG_STATUS_ERROR, Constants.LOG_ACTION_DELETE, userId, dealerId, vehicleId, '')
                        logwsService.createLog(logMap)
                    }
                }else{
                   response = "404 - not_found: El vehiculo con numero de inventario ${dataMap.stockNumber} ya fue borrado o no fue encontrado"
                   logMap = logwsService.createMapLog(remoteAddress,dataLogMapOrigin, response, [:], Constants.LOG_STATUS_SUCCESSFUL, Constants.LOG_ACTION_DELETE, userId, dealerId, vehicleId, '')
                   logwsService.createLog(logMap)
                }

            }else{
                response = "400 - bad_request: El usuario ${dataMap.usuario} no tiene un dealer asociado"
                logMap = logwsService.createMapLog(remoteAddress,dataLogMapOrigin, response, resultDealer, Constants.LOG_STATUS_ERROR, Constants.LOG_ACTION_DELETE, userId, '', '', '')
                logwsService.createLog(logMap)
            }

        }else {
            response = "400 - bad_request: El usuario y/o contraseña no son validos"
            logMap = logwsService.createMapLog(remoteAddress,dataLogMapOrigin, response, [:], Constants.LOG_STATUS_ERROR, Constants.LOG_ACTION_DELETE, '', '', '', '')
            logwsService.createLog(logMap)
        }
        return response
    }
}
