package maxipublica

class Logws {

    static constraints = {

        section nullable:false, blank:false
        user    nullable:false, blank:false
        description nullable: true, blank:true

    }

    String  section
    String  user
    String  description
    Map     data
    Date    dateRegistered = new Date()


}
