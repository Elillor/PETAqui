package com.buscadorpelut.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.buscadorpelut.Model.Protectora;
import com.buscadorpelut.Repository.ProtectoraRepository;

@Service
public class ProtectoraService {

    @Autowired
    private ProtectoraRepository protectoraRepository;

    public List<Protectora>getAllProtectores(){
        return protectoraRepository.findAll();
    }

    public Optional<Protectora>getProtectoraByCodiProt(Long codiProt){
        if(codiProt == null){
            return Optional.empty();
        }
        return protectoraRepository.findByCodiProt(codiProt);
    }

    public Optional<Protectora>getProtectoraByNomProt(String nomProt ){
        return protectoraRepository.findProtectoraByNomProt(nomProt);
    }

    public List<Protectora>getProtectoresByAdresa(String adresa){
        return protectoraRepository.findByAdresa(adresa);
    }

    public List<Protectora>getProtectoresByCodiPostal(String codiPostal){
        return protectoraRepository.findByCodiPostal(codiPostal);
    }

    public List<Protectora>getProtectoresByLocalitat(String localitat){
        return protectoraRepository.findByLocalitat(localitat);
    }

    public List<Protectora>getProtectoresByProvincia(String provincia){
        return protectoraRepository.findByProvincia(provincia);
    }

    public Optional<Protectora>getProtectoraByLongitudAndLatitud(double longitud, double latitud){
        return protectoraRepository.findByLongitudAndLatitud(longitud, latitud);
    }

    public Optional<Protectora>getProtectoraByEmailProt(String emailProt){
        return protectoraRepository.findByEmailProt(emailProt);
    }

    

}