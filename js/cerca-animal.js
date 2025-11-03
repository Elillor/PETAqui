document.addEventListener('DOMContentLoaded', () => {
    const animalResultsContainer = document.getElementById('animal-results');
    const searchForm = document.querySelector('.col-md-4 form, .col-lg-3 form'); 
    const resultsTitle = document.querySelector('.col-md-8 h4, .col-lg-9 h4');
    const paginationContainer = document.querySelector('.pagination-custom'); // Contenidor de la paginació

    // ----------------------------------------------------
    // 1. Dades de Mock (Manté les dades de l'usuari)
    // ----------------------------------------------------
    const MOCK_ANIMALS = [
        { id: 'enzo', name: 'Enzo', species: 'Gos', location: 'Barcelona', age: '2 anys', img: 'img/placeholder-dog.jpg' },
        { id: 'kira', name: 'Kira', species: 'Gat', location: 'Tarragona', age: '6 mesos', img: 'img/placeholder-cat.jpg' },
        { id: 'leo', name: 'Leo', species: 'Exòtic', location: 'Lleida', age: '1 any', img: 'img/placeholder-exotic.jpg' },
        { id: 'max', name: 'Max', species: 'Gos', location: 'Girona', age: '4 anys', img: 'img/placeholder-dog.jpg' },
        { id: 'luna', name: 'Luna', species: 'Gat', location: 'Barcelona', age: '3 anys', img: 'img/placeholder-cat.jpg' },
        { id: 'nino', name: 'Nino', species: 'Exòtic', location: 'Tarragona', age: '8 mesos', img: 'img/placeholder-exotic.jpg' },
        { id: 'rocky', name: 'Rocky', species: 'Gos', location: 'Lleida', age: '1 any', img: 'img/placeholder-dog.jpg' },
        { id: 'mia', name: 'Mia', species: 'Gat', location: 'Girona', age: '5 anys', img: 'img/placeholder-cat.jpg' },
        { id: 'pipo', name: 'Pipo', species: 'Exòtic', location: 'Barcelona', age: '2 anys', img: 'img/placeholder-exotic.jpg' },
        { id: 'dana', name: 'Dana', species: 'Gos', location: 'Tarragona', age: '6 mesos', img: 'img/placeholder-dog.jpg' },
        { id: 'coco', name: 'Coco', species: 'Gat', location: 'Lleida', age: '4 anys', img: 'img/placeholder-cat.jpg' },
        { id: 'sky', name: 'Sky', species: 'Gos', location: 'Girona', age: '10 mesos', img: 'img/placeholder-dog.jpg' },
    ];


    // Variables d'estat per a la paginació i els resultats
    const ANIMALS_PER_PAGE = 3; // LÍMIT ESTABLERT: 3 animals per pàgina
    let currentPage = 1;
    let currentAnimals = []; // Els animals filtrats actualment (abans de la paginació)

    // ----------------------------------------------------
    // 2. Funcions de Renderització
    // ----------------------------------------------------

    /**
     * Genera l'HTML d'una targeta d'animal
     * @param {Object} animal - L'objecte de l'animal
     * @returns {string} L'HTML de la targeta
     */
    const createAnimalCard = (animal) => {
        return `
            <div class="col">
                <article class="card h-100 animal-card-custom shadow">
                    <img src="${animal.img}" class="card-img-top card-img-custom" alt="${animal.name}, un ${animal.species}" />
                    <div class="card-body d-flex flex-column">
                        <h5 class="card-title text-general">${animal.name}</h5>
                        <span class="badge species-tag-custom mb-2">${animal.species}</span>
                        <p class="card-text small text-secondary mb-3">📍 ${animal.location} | 🐾 ${animal.age}</p>
                        <a href="detall-animal.html?id=${animal.id}" class="btn btn-primary btn-sm w-100 mt-auto btn-card-detail-custom">Més Info</a>
                    </div>
                </article>
            </div>
        `;
    };

    /**
     * Dibuixa els animals de la pàgina actual i actualitza la paginació
     * @param {Array} animals - La llista completa d'animals filtrats
     * @param {number} page - El número de pàgina a mostrar
     */
    const renderAnimals = (animals, page = 1) => {
        currentAnimals = animals;
        currentPage = page;

        // Càlcul de la paginació
        const totalPages = Math.ceil(currentAnimals.length / ANIMALS_PER_PAGE);
        const startIndex = (currentPage - 1) * ANIMALS_PER_PAGE;
        const endIndex = startIndex + ANIMALS_PER_PAGE;
        
        // Animals a mostrar a la pàgina actual
        const animalsToDisplay = currentAnimals.slice(startIndex, endIndex);

        // Actualitza el títol de resultats
        resultsTitle.innerHTML = `Resultats (${currentAnimals.length} animals total)`;
        
        // Renderitza les targetes
        animalResultsContainer.innerHTML = animalsToDisplay.map(createAnimalCard).join('');

        // Renderitza la paginació
        renderPagination(totalPages);
    };

    /**
     * Genera i renderitza els botons de paginació
     * @param {number} totalPages - El nombre total de pàgines
     */
    const renderPagination = (totalPages) => {
        let paginationHTML = '';

        // Botó Anterior
        paginationHTML += `
            <li class="page-item ${currentPage === 1 ? 'disabled' : ''}">
                <a class="page-link" href="#" data-page="${currentPage - 1}">Anterior</a>
            </li>
        `;

        // Botons de números de pàgina
        for (let i = 1; i <= totalPages; i++) {
            paginationHTML += `
                <li class="page-item ${currentPage === i ? 'active' : ''}">
                    <a class="page-link" href="#" data-page="${i}">${i}</a>
                </li>
            `;
        }

        // Botó Següent
        paginationHTML += `
            <li class="page-item ${currentPage === totalPages ? 'disabled' : ''}">
                <a class="page-link" href="#" data-page="${currentPage + 1}">Següent</a>
            </li>
        `;
        
        // Afegeix l'HTML al contenidor i amaga la paginació si només hi ha 1 pàgina
        paginationContainer.innerHTML = paginationHTML;
        paginationContainer.closest('nav').style.display = totalPages > 1 ? 'flex' : 'none';
    };


    // ----------------------------------------------------
    // 3. Gestió d'Esdeveniments
    // ----------------------------------------------------

    // 3.1 Filtre per Formulari
    if (searchForm) {
        searchForm.addEventListener('submit', (event) => {
            event.preventDefault(); // Evita que la pàgina es recarregui

            // Obté els valors dels camps de filtre
            const species = searchForm.querySelector('select').value;
            const location = searchForm.querySelector('input[type="text"]').value.toLowerCase().trim();

            const filteredAnimals = MOCK_ANIMALS.filter(animal => {
                
                // 1. FILTRE D'ESPECIE
                const matchesSpecies = species === 'Totes' || animal.species === species;
                
                // 2. FILTRE DE LOCALITZACIÓ
                const matchesLocation = location === '' || animal.location.toLowerCase().includes(location);
                
                return matchesSpecies && matchesLocation;
            });

            // Mostra els resultats filtrats, començant per la Pàgina 1
            renderAnimals(filteredAnimals, 1); 
        });
    }

    // 3.2 Clics de Paginació
    paginationContainer.addEventListener('click', (event) => {
        const target = event.target.closest('.page-link');
        if (target) {
            event.preventDefault();
            const newPage = parseInt(target.dataset.page);
            
            // Comprova si la pàgina és vàlida abans de fer la crida
            const totalPages = Math.ceil(currentAnimals.length / ANIMALS_PER_PAGE);
            if (newPage >= 1 && newPage <= totalPages) {
                renderAnimals(currentAnimals, newPage);
            }
        }
    });

    // ----------------------------------------------------
    // 4. Càrrega Inicial
    // ----------------------------------------------------
    // Cargar tots els animals al inici, abans que s'apliquin filtres.
    renderAnimals(MOCK_ANIMALS, 1); 
});