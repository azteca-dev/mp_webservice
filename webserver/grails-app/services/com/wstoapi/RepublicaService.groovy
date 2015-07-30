package com.wstoapi

import grails.transaction.Transactional

//import org.apache.ivy.plugins.conflict.ConflictManager
import com.wstoapi.exceptions.BadRequestException
import com.wstoapi.exceptions.ConflictException
import com.wstoapi.exceptions.NotFoundException
import java.text.MessageFormat
//import grails.converters.*

import com.wstoapi.HomologaService
import com.wstoapi.CarsService
import com.wstoapi.ValidAccess
import maxipublica.Logws

@Transactional
class RepublicaService {
	def HomologaService
	def CarsService
	/*
	{
		"by":"user",
		"user":"distribuidores71538@exagono.net",
		"date_from":"2015-07-28T00:00:00",
		"date_to":"2015-07-29T10:25:00"
	}
	{	
		"by":"array",
		"array":[13322, 13321, 13320, 13319, 13318, 13317, 13316, 13315]
	}
	{
		"by":"date",
		"date_from":"2015-07-28T00:00:00",
		"date_to":"2015-07-29T10:25:00"
	}
	*/

	def republica(def json, def params){
		
		def validAccess = new ValidAccess()
		def datosCriteria
		def resultCriteria
		def resultArray
		def fecha_to
		def fecha_from
		def resultFinal = [:]
		resultFinal.status = 200
        resultFinal.message = "success"
		resultFinal.listado = []

		if (!params.access_token){
            throw new BadRequestException ("You must provide an access_token")
        }

        def access_token = validAccess.validAccessToken(params.access_token)
        def user_id = params.access_token.split('_')[2]
        
        if(!access_token.toString().equals("admin")){
            throw new BadRequestException ("You need to be admin")
        }

		if (json.size() == 0) {
			throw new NotFoundException ("Please send a JSON")
		}

		if(json?.by.equals("user")){
			datosCriteria = Logws.createCriteria()

			if (!json?.user || json?.user.equals("")) {
				throw new NotFoundException ("Please send an user")
			}

			if (!json?.date_from || json?.date_from.equals("")) {
				throw new NotFoundException ("Please send an date_from")
			}

			if (!json?.date_to || json?.date_to.equals("")) {
				throw new NotFoundException ("Please send an date_to")
			}

			fecha_from = HomologaService.fechaMasTiempo(json?.date_from)
			fecha_to = HomologaService.fechaMasTiempo(json?.date_to)

			if (fecha_to < fecha_from) {
				throw new BadRequestException ("Invalid date range, date date_to must be greater than date date_from")
			}

			resultCriteria = datosCriteria.list{
				ilike("origin.usuario", "%"+json?.user+"%")
				ge("dateRegistered", fecha_from)
				le("dateRegistered", fecha_to)
			}
			resultCriteria.each{
				resultFinal.listado.add(CarsService.Publicar_Anuncio(
					it.origin.usuario,
					it.origin.contrasenna,
					it.origin.NumInventarioEmpresa,
					it.origin.DescripconAuto,
					it.origin.Precio,
					it.origin.TipoMoneda,
					it.origin.Tipo_PrecioIDAutoplaza,
					it.origin.Kilometraje,
					it.origin.Tipo_KilometrajeIDAutoplaza,
					it.origin.ColorExterior,
					it.origin.ColorInterior,
					it.origin.Marca,
					it.origin.MarcaIDAutoplaza,
					it.origin.Modelo,
					it.origin.ModeloIDAutoplaza,
					it.origin.Submodelo,
					it.origin.SubmodeloIDInterno,
					it.origin.SubmodeloIDAutoplaza,
					it.origin.Anno,
					it.origin.TipoVehiculoIDAutoplaza,
					it.origin.TipoVestiduraIDAutoplaza,
					it.origin.TipoTransmisionIDAutoplaza,
					it.origin.StatusVehiculoIDAutoplaza,
					it.origin.EmpresaIDAutoplaza,
					it.origin.EmpresaIDinterno,
					it.origin.AutoBlindado,
					it.origin.AutoAccidentadoRecuperado,
					it.origin.NumSerieAuto,
					it.origin.Equipamiento,
					it.origin.UrlFoto1,
					it.origin.UrlFoto2,
					it.origin.UrlFoto3,
					it.origin.UrlFoto4,
					it.origin.UrlFoto5,
					it.origin.UrlFoto6,
					it.origin.UrlFoto7,
					it.origin.UrlFoto8,
					it.origin.UrlFoto9,
					it.origin.UrlFoto10,
					it.origin.UrlFoto11,
					it.origin.UrlFoto12,
					it.origin.UrlFoto13,
					it.origin.UrlFoto14,
					it.origin.UrlFoto15,
					it.origin.UrlFoto16,
					it.origin.UrlFoto17,
					it.origin.UrlFoto18,
					it.origin.UrlFoto19,
					it.origin.UrlFoto20,
					it.origin.UrlFoto21,
					it.origin.UrlFoto22,
					it.origin.UrlFoto23,
					it.origin.UrlFoto24,
					it.origin.UrlFoto25,
					it.origin.UrlFoto26,
					it.origin.UrlFoto27,
					it.origin.UrlFoto28,
					it.origin.UrlFoto29,
					it.origin.UrlFoto30,
					it.origin.UrlFoto31,
					it.origin.UrlFoto32,
					"2"
				))
			}
		}

		if(json?.by.equals("array")){
			if (!json?.array.size() == 0) {
				throw new BadRequestException ("Please send an array with data")
			}
			json?.array.each{
				resultArray = Logws.findById(it)
				resultFinal.listado.add(CarsService.Publicar_Anuncio(
					resultArray.origin.usuario,
					resultArray.origin.contrasenna,
					resultArray.origin.NumInventarioEmpresa,
					resultArray.origin.DescripconAuto,
					resultArray.origin.Precio,
					resultArray.origin.TipoMoneda,
					resultArray.origin.Tipo_PrecioIDAutoplaza,
					resultArray.origin.Kilometraje,
					resultArray.origin.Tipo_KilometrajeIDAutoplaza,
					resultArray.origin.ColorExterior,
					resultArray.origin.ColorInterior,
					resultArray.origin.Marca,
					resultArray.origin.MarcaIDAutoplaza,
					resultArray.origin.Modelo,
					resultArray.origin.ModeloIDAutoplaza,
					resultArray.origin.Submodelo,
					resultArray.origin.SubmodeloIDInterno,
					resultArray.origin.SubmodeloIDAutoplaza,
					resultArray.origin.Anno,
					resultArray.origin.TipoVehiculoIDAutoplaza,
					resultArray.origin.TipoVestiduraIDAutoplaza,
					resultArray.origin.TipoTransmisionIDAutoplaza,
					resultArray.origin.StatusVehiculoIDAutoplaza,
					resultArray.origin.EmpresaIDAutoplaza,
					resultArray.origin.EmpresaIDinterno,
					resultArray.origin.AutoBlindado,
					resultArray.origin.AutoAccidentadoRecuperado,
					resultArray.origin.NumSerieAuto,
					resultArray.origin.Equipamiento,
					resultArray.origin.UrlFoto1,
					resultArray.origin.UrlFoto2,
					resultArray.origin.UrlFoto3,
					resultArray.origin.UrlFoto4,
					resultArray.origin.UrlFoto5,
					resultArray.origin.UrlFoto6,
					resultArray.origin.UrlFoto7,
					resultArray.origin.UrlFoto8,
					resultArray.origin.UrlFoto9,
					resultArray.origin.UrlFoto10,
					resultArray.origin.UrlFoto11,
					resultArray.origin.UrlFoto12,
					resultArray.origin.UrlFoto13,
					resultArray.origin.UrlFoto14,
					resultArray.origin.UrlFoto15,
					resultArray.origin.UrlFoto16,
					resultArray.origin.UrlFoto17,
					resultArray.origin.UrlFoto18,
					resultArray.origin.UrlFoto19,
					resultArray.origin.UrlFoto20,
					resultArray.origin.UrlFoto21,
					resultArray.origin.UrlFoto22,
					resultArray.origin.UrlFoto23,
					resultArray.origin.UrlFoto24,
					resultArray.origin.UrlFoto25,
					resultArray.origin.UrlFoto26,
					resultArray.origin.UrlFoto27,
					resultArray.origin.UrlFoto28,
					resultArray.origin.UrlFoto29,
					resultArray.origin.UrlFoto30,
					resultArray.origin.UrlFoto31,
					resultArray.origin.UrlFoto32,
					"2"
				))
			}
		}

		if(json?.by.equals("date")){


			if (!json?.date_from || json?.date_from.equals("")) {
				throw new NotFoundException ("Please send an date_from")
			}

			if (!json?.date_to || json?.date_to.equals("")) {
				throw new NotFoundException ("Please send an date_to")
			}

			datosCriteria = Logws.createCriteria()
			fecha_from = HomologaService.fechaMasTiempo(json?.date_from)
			fecha_to = HomologaService.fechaMasTiempo(json?.date_to)

			if (fecha_to < fecha_from) {
				throw new BadRequestException ("Invalid date range, date date_to must be greater than date date_from")
			}

			resultCriteria = datosCriteria.list{
				ge("dateRegistered", fecha_from)
				le("dateRegistered", fecha_to)
			}
			resultCriteria.each{
				resultFinal.listado.add(CarsService.Publicar_Anuncio(
					it.origin.usuario,
					it.origin.contrasenna,
					it.origin.NumInventarioEmpresa,
					it.origin.DescripconAuto,
					it.origin.Precio,
					it.origin.TipoMoneda,
					it.origin.Tipo_PrecioIDAutoplaza,
					it.origin.Kilometraje,
					it.origin.Tipo_KilometrajeIDAutoplaza,
					it.origin.ColorExterior,
					it.origin.ColorInterior,
					it.origin.Marca,
					it.origin.MarcaIDAutoplaza,
					it.origin.Modelo,
					it.origin.ModeloIDAutoplaza,
					it.origin.Submodelo,
					it.origin.SubmodeloIDInterno,
					it.origin.SubmodeloIDAutoplaza,
					it.origin.Anno,
					it.origin.TipoVehiculoIDAutoplaza,
					it.origin.TipoVestiduraIDAutoplaza,
					it.origin.TipoTransmisionIDAutoplaza,
					it.origin.StatusVehiculoIDAutoplaza,
					it.origin.EmpresaIDAutoplaza,
					it.origin.EmpresaIDinterno,
					it.origin.AutoBlindado,
					it.origin.AutoAccidentadoRecuperado,
					it.origin.NumSerieAuto,
					it.origin.Equipamiento,
					it.origin.UrlFoto1,
					it.origin.UrlFoto2,
					it.origin.UrlFoto3,
					it.origin.UrlFoto4,
					it.origin.UrlFoto5,
					it.origin.UrlFoto6,
					it.origin.UrlFoto7,
					it.origin.UrlFoto8,
					it.origin.UrlFoto9,
					it.origin.UrlFoto10,
					it.origin.UrlFoto11,
					it.origin.UrlFoto12,
					it.origin.UrlFoto13,
					it.origin.UrlFoto14,
					it.origin.UrlFoto15,
					it.origin.UrlFoto16,
					it.origin.UrlFoto17,
					it.origin.UrlFoto18,
					it.origin.UrlFoto19,
					it.origin.UrlFoto20,
					it.origin.UrlFoto21,
					it.origin.UrlFoto22,
					it.origin.UrlFoto23,
					it.origin.UrlFoto24,
					it.origin.UrlFoto25,
					it.origin.UrlFoto26,
					it.origin.UrlFoto27,
					it.origin.UrlFoto28,
					it.origin.UrlFoto29,
					it.origin.UrlFoto30,
					it.origin.UrlFoto31,
					it.origin.UrlFoto32,
					"2"
				))
			}
		}
		resultFinal
		//log.findById
    }
}
