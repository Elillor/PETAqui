document.addEventListener('DOMContentLoaded', ()=>{
    
    const carouselInner = document.getElementById('carouselInnerAdoptats');
    if(!carouselInner) return;

    const url = 'http://localhost:8080/api/animals/adoptats';

    function getImatgeAnimal(animal){
        if(!animal.fotoPerfil){
            return 'img/no-image-available.png';
        }
        const carpImatges = animal.especie
            .toLowerCase()
            .normalize('NFD')
            .replace(/[\u0300-\u036f]/g, '');
        return `/img/Animals/${carpImatges}/${animal.fotoPerfil}`;
    }

    // Función que maneja el clic en los enlaces protegidos (Més Info)
    function handleProtectedDetailClick(event) {
        // Usamos la función expuesta por auth.js
        if (!window.isUserLoggedIn()) { 
            event.preventDefault(); // Bloquear navegación
            alert('Necessites iniciar sessió per veure el detall de l\'animal.');
            return;
        }
        // Si está logeado, el navegador sigue el href por defecto.
    }

    function crearTargetaAnimal(animal){

        const edat = animal.edat;

        let edatText;

        if(edat.anys === 0) {
            edatText = `${edat.mesos} mesos`;
        } else if (edat.mesos === 0) {
            edatText = `${edat.anys} anys`;
        } else {
            edatText = `${edat.anys} anys i ${edat.mesos} mesos`;
        }

        return `
        <div class="col">
            <div class="card h-100 animal-card-custom shadow">
                <img src="${getImatgeAnimal(animal)}" class="card-img-top card-img-custom" alt="Foto de ${animal.nom}" fetchpriority="high">
                <div class="card-body">
                    <h3 class="card-title text-general">${animal.nomAn}</h3>
                    <span class="badge species-tag-custom mb-2">${animal.especie}</span>
                    <p class="card-text small text-secondary d-flex justify-content-between m-2">
                    <span>📍${animal.localitatProtectora} </span>
                    <span>🐾 ${edatText}</span>
                    </p>
                    <a href="detall-animal.html?id=${animal.numId}" 
                       class="btn btn-primary btn-sm w-100 btn-card-detail-custom protected-detail-link">Més Info</a>
                </div>
            </div>
        </div>
        `;
    }

    function dividirGrups(array, midaGrup=3){
        const grups = [];
        for(let i=0; i<array.length; i+=midaGrup){
            grups.push(array.slice(i, i + midaGrup));
        }
        return grups;
    }

    function crearCarouselItems(animals){
        const grupsAnimals = dividirGrups(animals, 3);
        let itemsHTML = '';

        grupsAnimals.forEach((grup, index) => {
            const esActiu = index === 0 ? 'active' : '';
            const cards = grup.map(animal => crearTargetaAnimal(animal)).join('');
            itemsHTML += `
            <div class="carousel-item ${esActiu}">
                <div class="row row-cols-1 row-cols-md-2 row-cols-lg-3 g-4 justify-content-center">
                    ${cards}
                </div>
            </div>
            `;
        });

        carouselInner.innerHTML = itemsHTML;

        // 🚨 Aplicar protección a los enlaces "Més Info" después de insertarlos
        const detailLinks = carouselInner.querySelectorAll('.protected-detail-link');
        
        detailLinks.forEach(link => {
            link.addEventListener('click', handleProtectedDetailClick);
        });
        
        // Inicialización de carrusel (se mantiene)
        const carousel = document.getElementById('adoptionCarousel');

        if(carousel){
            const existing = bootstrap.Carousel.getInstance(carousel);
            if (existing) {
                existing.dispose();
            }
            new bootstrap.Carousel(carousel, {
                interval: 5000 
            });
        }
    }

    async function fetchAnimalsAdoptats(){
        try{
            const response = await fetch(url);
            if(!response.ok){
                throw new Error(`Error al obtenir els animals adoptats: ${response.status}`);
            }
            const animals = await response.json();
            crearCarouselItems(animals);
        }catch(error){
            console.error('Error en la petició:', error);
            carouselInner.innerHTML = `
                <div class="carousel-item active">
                    <div class="container text-center py-5">
                        <p class="text-danger">No s'han pogut carregar els animals adoptats.</p>
                    </div>
                </div>
            `;
        }
    }

    fetchAnimalsAdoptats();
});