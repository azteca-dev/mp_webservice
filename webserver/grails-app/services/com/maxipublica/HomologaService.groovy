package com.maxipublica

import grails.transaction.Transactional

@Transactional
class HomologaService {

    def homologaData(def dataMap, def userId, def dealerId) {

        // vamos a ver como podemos procesar el equipamiento
        // vamos a ver como podemos procesar los attributos
        // vamos a ver como podemo procesar las imagenes

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
                attributes:getAttributes(),
                equipment:getEquipment()

        ]

        jsonVehicleForPost
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

    def getAttributes(){

        def jsonAttributes = [

                id:"attributes_group",
                label:"Ficha Tecnica",
                currencies:[
                        id:"CURRENCIE-MXN",
                        label:"Moneda",
                        value:"MXN"
                ],
                colorInt:[
                        id:"COLORINT-BEIGE",
                        label:"Color Interior",
                        value:"Beige"
                ],
                direction:[
                        id:"DIRECCION-ASISTIDA",
                        label:"Dirección",
                        value:"Asistida"
                ],
                vesture:[
                        id:"VESTURE-TELA",
                        label:"Vestidura",
                        value:"Tela"
                ],
                transmission:[
                        id:"TRANS-TRONIC",
                        label:"TRansmisión",
                        value:"Tronic"
                ],
                colorExt:[
                        id:"COLOREXT-AZUL",
                        label:"Color Exterior",
                        value:"Azul"
                ],
                body_type:[
                        id:"BODY-SEDAN",
                        label:"Tipo de carroceria",
                        value:"Sedan"
                ]

        ]

        jsonAttributes
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
