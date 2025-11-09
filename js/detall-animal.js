document.addEventListener('DOMContentLoaded', () => {
    
    // CONFIGURACIÓN CLAVE: URL Base de tu API
    // Se usará para construir la URL: http://localhost:8080/api/animals/enzo
    const API_BASE_URL = 'http://localhost:8080/api/animals'; 

    // ----------------------------------------------------
    // 1. Función para obtener el ID de la URL (Sin cambios)
    // ----------------------------------------------------
    const getAnimalIdFromUrl = () => {
        const params = new URLSearchParams(window.location.search);
        return params.get('id');
    };

    // ----------------------------------------------------
    // 2. Función para obtener los detalles del Backend (Nuevo)
    // ----------------------------------------------------
    async function fetchAnimalDetails(animalId) {
        if (!animalId) {
            renderError('No s\'ha especificat cap animal (ID no trobat a la URL).');
            return;
        }

        try {
            // Construye la URL para un único animal (ej: /api/animals/enzo)
            const url = `${API_BASE_URL}/${animalId}`;
            
            const response = await fetch(url);
            
            if (response.status === 404) {
                throw new Error('Animal no trobat (404). Comprova la ID.');
            }
            if (!response.ok) {
                throw new Error(`Error HTTP: Status ${response.status}`);
            }
            
            // Asume que el backend devuelve el objeto Animal completo
            const animal = await response.json();
            
            // Renderiza los detalles en la página
            renderAnimalDetails(animal);

        } catch (error) {
            console.error("Error al carregar els detalls de l'animal:", error);
            renderError(`Error de connexió o de dades: ${error.message}`);
        }
    }
    
    // ----------------------------------------------------
    // 3. Funciones de Renderizado (Con cambios ligeros)
    // ----------------------------------------------------

    const renderError = (message) => {
        const container = document.getElementById('animal-detail-content');
        container.innerHTML = `<div class="col-12 text-center"><p class="alert alert-danger">${message}</p></div>`;
        document.getElementById('animal-name-breadcrumb').textContent = 'Error';
        document.title = 'Error - PETAqui';
    };

    const renderAnimalDetails = (animal) => {
        // --- Metadatos y Títulos ---
        document.title = `${animal.name} - Detall Animal - PETAqui`;
        document.getElementById('animal-name-breadcrumb').textContent = animal.name;
        document.getElementById('animal-name').textContent = animal.name;
        
        // --- Imagen y CTA ---
        document.getElementById('animal-image').src = animal.img || 'img/placeholder-default.jpg';
        document.getElementById('animal-image').alt = `Imatge de ${animal.name}`;
        
        // Generar enlace de contacto con la protectora (si el backend lo proporciona)
        const protectoraEmail = animal.protectoraEmail || 'adopcions@petaqui.cat';
        document.getElementById('adoption-cta').href = `mailto:${protectoraEmail}?subject=Interessat%20en%20l'adopció%20de%20${animal.name}`;

        // --- Protectora y Descripción ---
        const protectoraLink = document.getElementById('protectora-name');
        protectoraLink.textContent = animal.protectora || 'Protectora Desconeguda';
        protectoraLink.href = animal.protectoraWeb || '#'; 
        document.getElementById('animal-description').textContent = animal.description || 'Sense descripció.';
        document.getElementById('animal-status').textContent = animal.status || 'Estat Desconegut';
        
        // Asignar color al status 
        const statusBadge = document.getElementById('animal-status');
        // (La lógica de las clases CSS debe estar en tu CSS o aquí)
        statusBadge.classList.remove('bg-info', 'bg-warning', 'bg-success');
        if (animal.status && animal.status.toLowerCase().includes('adopció')) {
            statusBadge.classList.add('bg-success');
        } else if (animal.status && animal.status.toLowerCase().includes('pendent')) {
            statusBadge.classList.add('bg-warning');
        } else {
            statusBadge.classList.add('bg-info');
        }


        // --- Detalls Ràpids ---
        document.getElementById('detail-species').textContent = animal.species || '-';
        document.getElementById('detail-age').textContent = animal.age || '-';
        document.getElementById('detail-gender').textContent = animal.gender || '-';
        document.getElementById('detail-size').textContent = animal.size || '-';
        document.getElementById('detail-location').textContent = animal.location || '-';
        
        // --- Caràcter i Convivència ---
        document.getElementById('detail-kids').textContent = animal.kids || '-';
        document.getElementById('detail-dogs').textContent = animal.dogs || '-';
        document.getElementById('detail-cats').textContent = animal.cats || '-';
        document.getElementById('detail-energy').textContent = animal.energy || '-';

        // --- Informació Mèdica ---
        document.getElementById('detail-health').textContent = animal.health || '-';
        document.getElementById('detail-vax').textContent = animal.vax || '-';
        document.getElementById('detail-sterilized').textContent = animal.sterilized || '-';
        document.getElementById('detail-chip').textContent = animal.chip || '-';
    };


    // ----------------------------------------------------
    // 4. Inicialización
    // ----------------------------------------------------
    const animalId = getAnimalIdFromUrl();
    
    // Iniciar la carga de los detalles
    fetchAnimalDetails(animalId);
});