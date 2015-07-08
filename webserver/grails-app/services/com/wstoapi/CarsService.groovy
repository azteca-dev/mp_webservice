package com.wstoapi

import grails.transaction.Transactional

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

        def response = "0"
        def accessToken
        def jsonVehicle
        def jsonVehicleUPD
        def userId
        def dealerId
        def logMap


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

        def validateData = homologaService.validateDataMap(DataWsMap)


        if(validateData.status != "0"){
            response = validateData.status+"-"+validateData.message
        }else {

            def result = authenticateService.login(DataWsMap.User, DataWsMap.Pass)

            if (result.data.access_token) {
                accessToken = result.data.access_token
                userId      = result.data.user_id
                def resultDealer = authenticateService.getDealer(userId, accessToken)


                logMap = [
                        section:"pre-publication",
                        user:DataWsMap.User,
                        description:"Contiene los datos iniciales antes de enviar a la api de vehicle de maxipublica",
                        data:[numInventario:DataWsMap.StockNumber,access_token:accessToken, user_id:userId, data:DataWsMap]
                ]
                logwsService.createLog(logMap)

                if (resultDealer.data.dealer_id){

                    dealerId = resultDealer.data.dealer_id

                    def vehicleId = publicaService.searchVehicle(DataWsMap.StockNumber, dealerId, accessToken)



                    if(vehicleId != 0){

                        response = "0 - Actualizamos el vehiculo correctamente"
                        jsonVehicleUPD = homologaService.homologaDataUpdate(DataWsMap, userId, dealerId)

                        def respUpdApiVehicle = publicaService.updateVehicle(vehicleId, jsonVehicleUPD, accessToken)
                        logMap = [
                                section:"update",
                                user:DataWsMap.User,
                                description:"Contiene los datos de la publicacion en la api de vehicle despues del update",
                                data:[numInventario:DataWsMap.StockNumber,vehicle_id:respUpdApiVehicle.data.id, user_id:userId, json_enviado:jsonVehicleUPD]
                        ]
                        logwsService.createLog(logMap)
                        try {
                            publicaService.updateImages(DataWsMap, accessToken, respUpdApiVehicle.data.id)
                        }catch(Exception e){
                            response = "0 - Actualizamos el vehiculo, pero no las fotos"
                        }

                    }else{

                        try{
                            jsonVehicle = homologaService.homologaData(DataWsMap, userId, dealerId)
                            def respApiVehicle = publicaService.createVehicle(jsonVehicle, accessToken)

                            if (respApiVehicle.data.id){
                                logMap = [
                                        section:"publication",
                                        user:DataWsMap.User,
                                        description:"Contiene los datos de la publicacion en la api de vehicle",
                                        data:[numInventario:DataWsMap.StockNumber,vehicle_id:respApiVehicle.data.id, user_id:userId, json_enviado:jsonVehicle]
                                ]
                                logwsService.createLog(logMap)
                                publicaService.postImages(DataWsMap, accessToken, respApiVehicle.data.id, respApiVehicle.data)
                                response = "0 - "+respApiVehicle.data.id
                            }else{
                                response = "8 - "+respApiVehicle.data.message
                            }
                        }catch(Exception e){
                            response = "-1 - No se pudo hacer el update con vehicle sin fotos error:"+e.message.toString()
                        }


                    }


                }else{
                    response = "8 - El usuario no tiene un dealer asociado"
                }

            } else {
                response = "1 - El usuario y/o contraseña no son validos"
            }

        }

        return  response

    }


    def Borrar_Anuncio(String usuario, String contraseña, String empresaID_, String NumInventarioCliente){

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

        if(!dataMap.usuario){
            return "8 - El Usuario es requerido"
        }
        if(!dataMap.pass){
            return "8 - La contraseña es requerida"
        }
        if(!dataMap.stockNumber){
            return "8 - El NumeroInventarioCliente es requerido"
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

                    def logMap = [
                            section:"deleted-publication",
                            user:dataMap.usuario,
                            description:"Se manda a borrar un vehicle ["+vehicleId+"]",
                            data:[numInventario:dataMap.stockNumber,access_token:accessToken, user_id:userId, vehicle_Id:vehicleId]
                    ]
                    logwsService.createLog(logMap)

                    def resultDeleted = publicaService.deleteVehicle(vehicleId, accessToken)

                    if(resultDeleted.data.message){
                        response = "0 - El vehiculo con numero de inventario "+ dataMap.stockNumber+ "fue borrado"
                    }else{
                        response = "0 - Ocurrio un problema con el borrado"
                    }
                }else{
                   response = "2 - El vehiculo con numero de  inventario ya fue borrado o no fue encontrado"
                }

            }else{
                response = "8 - El usuario no tiene un dealer asociado"
            }

        }else {
            response = "1 - El usuario y/o contraseña no son validos"
        }

        return response
    }
}
