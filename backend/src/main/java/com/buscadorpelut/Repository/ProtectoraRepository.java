package com.buscadorpelut.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.buscadorpelut.Model.Protectora;

@Repository
public interface ProtectoraRepository extends JpaRepository<Protectora,Long>{

    Optional<Protectora> findByCodiProt(long codiProt);

    Optional<Protectora> findProtectoraByNomProt(String nomProt);

    List<Protectora> findByAdresa(String adresa);

    List<Protectora> findByCodiPostal(String codiPostal);

    List<Protectora> findByLocalitat(String localitat);

    List<Protectora> findByProvincia(String provincia);

    Optional<Protectora> findByLongitudAndLatitud(double longitud, double latitud);

    Optional<Protectora> findByEmailProt(String emailProt);



    

}
