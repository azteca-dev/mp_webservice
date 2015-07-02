/*environments {
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
}*/
environments {
   development {
       grails {
           mongo {
               host = 'ds045082-a0.mongolab.com' 
               port = '45082' 
               username = '4mxp_multi_connect' 
               password = 'Pre.M4xianuncios'
               databaseName = 'premaxipublica'
           }
       }
   }
   test {
       grails {
           mongo {
               host = 'ds045082-a0.mongolab.com' 
               port = '45082' 
               username = '4mxp_multi_connect' 
               password = 'Pre.M4xianuncios'
               databaseName = 'premaxipublica'
               
           }
       }
   }
   production {
       grails {
           mongo {
               host = 'ds045082-a0.mongolab.com' 
               port = '45082' 
               username = '4mxp_multi_connect' 
               password = 'Pre.M4xianuncios'
               databaseName = 'premaxipublica'
           }
       }
   }
}