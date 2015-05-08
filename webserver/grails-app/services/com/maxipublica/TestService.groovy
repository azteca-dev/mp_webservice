package com.maxipublica

import grails.transaction.Transactional

@Transactional
class TestService {

        static expose=['cxf']



        String holaMundo(){
            println "Entro el metodo que paso"
            return "Hola desde el webservice en groovy"
        }

        String HolaUser(String nombre){
            return "Hola Tu  nombre es ${nombre} ? "
        }
}
