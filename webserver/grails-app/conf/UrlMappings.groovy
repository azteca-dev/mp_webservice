class UrlMappings {

	static mappings = {

        "/"{
            controller = "Republica"
            action = [GET:'notAllowed']
        }

        "/logs"{
            controller = "Logws"
            action = [GET:'index']
        }

        "/republica"{
            controller = "Republica"
            action = [GET:'notAllowed', POST:'republica', PUT:'notAllowed', DELETE:'notAllowed']
        }

        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        //"/"(view:"/index")
        //"500"(view:'/error')*/
	}
}
