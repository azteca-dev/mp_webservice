package maxipublica

class Logws {

    static constraints = {

        action nullable: false, blank:false, inList: ['insert', 'update', 'delete', 'begin']

    }

    String action
    Map origin          = [:]
    Map response        = [:]
    Map tech            = [:]
    Date dateRegistered = new Date()


}
