document.addEventListener('DOMContentLoaded', () => {
    const animalResultsContainer = document.getElementById('animal-results');
    const searchForm = document.querySelector('.col-md-4 form, .col-lg-3 form'); 
    const resultsTitle = document.querySelector('.col-md-8 h4, .col-lg-9 h4');
    const paginationContainer = document.querySelector('.pagination-custom');
    
    // CONFIGURACIÓN CLAVE: URL de tu API Backend
    const API_URL = 'http://localhost:8080/api/animals'; 

    // Ajusta la paginación según tu diseño
    const ANIMALS_PER_PAGE = 6; 
    let currentAnimals = []; // Almacenará los animales cargados por el backend
    let currentPage = 1;

    // ----------------------------------------------------
    // 1. Funciones de Renderizado (Sin Cambios)
    // ----------------------------------------------------

    const createAnimalCard = (animal) => {
        // Asegúrate de que los IDs del backend coincidan con los IDs de la URL
        return `
            <div class="col-md-6 col-lg-4 mb-4">
                <div class="card animal-card-custom h-100">
                    <img src="${animal.img || 'img/placeholder-default.jpg'}" class="card-img-top card-img-custom" alt="Imatge de ${animal.name}">
                    <div class="card-body d-flex flex-column">
                        <h5 class="card-title">${animal.name}</h5>
                        <div class="d-flex justify-content-between align-items-center mb-2">
                            <span class="badge species-tag-custom me-2">${animal.species}</span>
                            <small class="text-muted">${animal.age || 'Edat desconeguda'}</small>
                        </div>
                        <p class="card-text small mb-2">
                            📍 ${animal.location || 'Localització desconeguda'}
                        </p>
                        <a href="detall-animal.html?id=${animal.id}" class="btn btn-primary btn-card-detail-custom mt-auto">
                            Més detalls &rarr;
                        </a>
                    </div>
                </div>
            </div>
        `;
    };

    const renderAnimals = (animals, page) => {
        currentPage = page;
        const start = (page - 1) * ANIMALS_PER_PAGE;
        const end = start + ANIMALS_PER_PAGE;
        const paginatedAnimals = animals.slice(start, end);

        if (paginatedAnimals.length === 0) {
            animalResultsContainer.innerHTML = 
                '<div class="col-12 text-center"><p class="alert alert-warning">No s\'ha trobat cap animal amb aquests filtres.</p></div>';
        } else {
            const html = paginatedAnimals.map(createAnimalCard).join('');
            animalResultsContainer.innerHTML = html;
        }

        renderPagination(animals.length);
        resultsTitle.textContent = `Resultats (${animals.length} trobats)`;
    };

    const renderPagination = (totalAnimals) => {
        const totalPages = Math.ceil(totalAnimals / ANIMALS_PER_PAGE);
        let paginationHtml = '';
        
        if (totalPages <= 1) {
            paginationContainer.innerHTML = '';
            return;
        }
        
        // Botón Anterior
        const prevPage = currentPage - 1;
        paginationHtml += `
            <li class="page-item ${currentPage === 1 ? 'disabled' : ''}">
                <a class="page-link" href="#" data-page="${prevPage}">Anterior</a>
            </li>`;

        // Botones de Páginas
        for (let i = 1; i <= totalPages; i++) {
            paginationHtml += `
                <li class="page-item ${i === currentPage ? 'active' : ''}" aria-current="${i === currentPage ? 'page' : ''}">
                    <a class="page-link" href="#" data-page="${i}">${i}</a>
                </li>`;
        }

        // Botón Siguiente
        const nextPage = currentPage + 1;
        paginationHtml += `
            <li class="page-item ${currentPage === totalPages ? 'disabled' : ''}">
                <a class="page-link" href="#" data-page="${nextPage}">Següent</a>
            </li>`;

        paginationContainer.innerHTML = `
            <ul class="pagination pagination-custom justify-content-center">
                ${paginationHtml}
            </ul>`;
    };

    // ----------------------------------------------------
    // 2. Lógica de Carga y Fetch (Actualizado)
    // ----------------------------------------------------

    /**
     * Carga los animales del backend, con o sin filtros.
     * @param {URLSearchParams} [params=new URLSearchParams()] - Parámetros de filtro para la URL.
     */
    async function fetchAnimals(params = new URLSearchParams()) {
        try {
            // Muestra un mensaje de carga
            animalResultsContainer.innerHTML = '<div class="col-12 text-center"><p class="text-acento">Carregant animals...</p></div>';
            
            // Construye la URL completa con los parámetros (ej: /api/animals?species=Gos)
            const url = `${API_URL}?${params.toString()}`;
            
            const response = await fetch(url);
            
            if (!response.ok) {
                // Manejar errores de respuesta HTTP (404, 500, etc.)
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            
            // Asume que el backend devuelve un array de objetos Animal
            const animals = await response.json();
            
            // La respuesta del backend debe tener la misma estructura que MOCK_ANIMALS
            
            // Almacena los resultados y renderiza la primera página
            currentAnimals = animals;
            renderAnimals(currentAnimals, 1);
            
        } catch (error) {
            console.error("Error al connectar amb el backend o carregar els animals:", error);
            animalResultsContainer.innerHTML = 
                `<div class="col-12 text-center">
                    <p class="alert alert-danger">Error al connectar amb el servidor (${error.message}). Intenta-ho de nou més tard.</p>
                </div>`;
            resultsTitle.textContent = `Resultats (0 trobats)`;
        }
    }

    // ----------------------------------------------------
    // 3. Event Listeners
    // ----------------------------------------------------

    // 3.1 Submissió del Formulari de Filtre
    searchForm.addEventListener('submit', (event) => {
        event.preventDefault();

        // Obtener valores del formulario
        const species = document.getElementById('filter-species').value.trim();
        const location = document.getElementById('filter-location').value.trim();
        // ... obtener otros filtros ...

        const queryParams = new URLSearchParams();
        
        // Solo añade el parámetro si no es 'Totes' y tiene valor
        if (species && species !== 'Totes') {
            queryParams.append('species', species);
        }
        if (location) {
            queryParams.append('location', location);
        }
        // ... añadir otros parámetros ...

        // Llama a la función de fetch con los parámetros de búsqueda
        fetchAnimals(queryParams);
    });

    // 3.2 Clics de Paginació
    paginationContainer.addEventListener('click', (event) => {
        const target = event.target.closest('.page-link');
        if (target) {
            event.preventDefault();
            const newPage = parseInt(target.dataset.page);
            
            // Comprueba si la página es válida
            const totalPages = Math.ceil(currentAnimals.length / ANIMALS_PER_PAGE);
            if (newPage >= 1 && newPage <= totalPages) {
                renderAnimals(currentAnimals, newPage);
            }
        }
    });


    // ----------------------------------------------------
    // 4. Càrrega Inicial al Iniciar la Pàgina
    // ----------------------------------------------------
    fetchAnimals(); // Carga la lista completa (sin parámetros)
});