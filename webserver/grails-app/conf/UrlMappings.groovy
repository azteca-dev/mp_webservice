class UrlMappings {

	static mappings = {

        "/logs"{
            controller = "Logws"
            action = [GET:'index']
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
