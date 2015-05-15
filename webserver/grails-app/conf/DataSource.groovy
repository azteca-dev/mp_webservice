environments {
    development {
        grails {
            mongo {
                host = "localhost"
                databaseName = "mp_ws"
            }
        }
    }
    test {
        grails {
            mongo {
                host = "localhost"
                databaseName = "mp_ws"
            }
        }
    }
    production {
        grails {
            mongo {

                // replicaSet = []
                host = "localhost"
                username = ""
                password = ""
                databaseName = "mp_ws"
            }
        }
    }
}
