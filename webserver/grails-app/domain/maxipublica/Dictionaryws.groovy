package maxipublica

class Dictionaryws {

    static constraints = {

        groupName   nullable:false, blank:false
        groupId     nullable:false, blank:false
        type        inList: ['COLOREXT', 'COLORINT', 'CURRENCIE', 'VESTURE', 'TRANSMISION', 'BODY', 'ACC', 'TEST']
        valueIdSite nullable:false, blank:false
        site        nullable:false, blank:false
        valueIdMXP  nullable:true, blank:true

    }

    String groupName
    String groupId
    String type
    String valueIdSite
    String site
    String valueIdMXP

}
