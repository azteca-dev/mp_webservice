import maxipublica.Dictionaryws

class BootStrap {

    def init = { servletContext ->
        development{

            if(Dictionaryws.count() == 0){
                def dictionary01 = new Dictionaryws(
                        groupId: 'test',
                        groupName: 'test',
                        type:'TEST',
                        site: 'APC',
                        valueIdSite: 'testSite',
                        valueIdMXP: 'testmxp'
                )

                if(!dictionary01.save()){
                    println "No se pudo crear un item de la clase de dominio Dictionaryws"
                    dictionary01.errors.each{
                        println it
                    }
                }
            }
        }
    }
    def destroy = {
    }
}
