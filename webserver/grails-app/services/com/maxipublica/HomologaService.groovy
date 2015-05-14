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



    def getAttributes(def TypeCurrency,
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
