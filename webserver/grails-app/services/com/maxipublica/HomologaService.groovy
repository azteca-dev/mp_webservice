package com.maxipublica

import grails.transaction.Transactional

@Transactional
class HomologaService {

    def homologaData(def dataMap, def userId, def dealerId) {

        def jsonVehicle = createJsonVehicle(dataMap, userId, dealerId)

        jsonVehicle

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
                condition:dataMap.StatusVehicleMPId,
                dealer:[
                        dealer_ID:dealerId,
                        seller_contact:[
                                id:userId
                        ]
                ],
                images:[

                ],
                attributes:getAttributes(dataMap.TypeCurrency, dataMap.ExteriorColor, dataMap.InteriorColor, dataMap.TypeVestureMPId, dataMap.TypeTransmissionMPId, dataMap.TypeVehicleMPId),
                equipment:getEquipment()

        ]

        jsonVehicleForPost
    }

    def createJsonImages (def urlImage){

        def images = []
        images << [url:urlImage]
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

    def getEquipment(){
        def jsonEquipment = [

                id:"equipment_group",
                label:"Equipamiento",
                electric_group:[
                        id:"electric_group",
                        label:"Equipo eléctrico",
                        power_mirrors:[
                                id:"power_mirrors",
                                label:"Espejos laterales"
                        ],
                        power_door_locks:[
                                id:"power_door_locks",
                                label:"Seguros"
                        ]
                ],
                security_group:[
                        id:"security_group",
                        label:"Seguridad",
                        disc_brakes:[
                                id:"disc_brakes",
                                label:"Frenos de disco traseros"
                        ]
                ]

        ]

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

    /*


                user_type: 'normal',
                //tags: tags,
                email: account.email.trim().toLowerCase(),
                password: account.password.trim(),
                phone: [
                        area_code: "",
                        number: account.phone.replaceAll("[^\\s0-9().\\-\\*\\/\\#\\+]+\$", "")
                ],
                country_id: "MX",
                site_id: "MLM",
                company: [
                        corporate_name: "",
                        corporate_identification: ""
                ],
                city: account.address.ciudad,
                //state: findStateId(account.address.estado),
                address: account.address.calle,
                identification: [
                        type: account.rfc ? "RFC" : "",
                        number: account.rfc
                ],
                context: context << [pending_registration: "override"],
                confirmed_registration: true
     */
}
