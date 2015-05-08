package com.maxipublica

import grails.transaction.Transactional

@Transactional
class WebMaxipublicaService {

    def authenticateService = new AuthenticateService()
    def homologaService
    def publicaService

    static expose=['cxf']

    def Publicar_Anuncio(

            String Usuario,
            String Contraseña,
            String NumInventarioEmpresa,
            String Descripcion,
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
            String EmpresaIDInterno,
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

        def response = "0" // el anuncio se publico correctamente
        def accessToken
        def jsonVehicle
        def userId
        def dealerId

        //primer paso, verificamos los datos authenticamos
        //obtenmos los datos y hacemos las validaciones
        //guardamos un log con los datos que se accedieron
        //si todo sale bien entonce hacemos la homologacion de datos
        //mandamos a publicar a la api de vehicles
        // obtenemos la respuesta

        //Verificar si antes del mapeo revisamos si algunos datos son obligatorios

        if(!Usuario && !Contraseña){
            response = "1"
        }else {


            def DataWsMap = [

                    User                    : Usuario,
                    Pass                    : Contraseña,
                    StockNumber             : NumInventarioEmpresa,
                    Description             : Descripcion,
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
                    DealerIntId             : EmpresaIDInterno,
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
            def result = authenticateService.login(Usuario, Contraseña)

            if (result.data.access_token) {
                accessToken = result.data.access_token
                userId      = result.data.user_id
                def resultDealer = authenticateService.getDealer(userId, accessToken)

                if (resultDealer.data.dealer_id){
                    dealerId = resultDealer.data.dealer_id
                }
                jsonVehicle = homologaService.homologaData(DataWsMap, userId, dealerId)

                def respApiVehicle = publicaService.createVehicle(jsonVehicle, accessToken)

                println "El access_token es"+accessToken
                println "El json que queremos publicar es"+jsonVehicle
                println "El json de respuesta de la api de vehicle es"+respApiVehicle

                if (respApiVehicle.data.id){
                    response = respApiVehicle.data.id
                }else{
                    response = "8"
                }




            } else {
                response = "1" //"EL usuario y/o contraseña no son validos"
            }
        }

        return  response

    }
}
