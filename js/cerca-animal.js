document.addEventListener("DOMContentLoaded", () => {
  const animalResultsContainer = document.getElementById("animal-results");
  const searchForm = document.querySelector("form");
  const resultsTitle = document.querySelector("h4");
  const selectEspecie = searchForm.querySelector("select");
  const paginationNav = document.querySelector(
    'nav[aria-label="Paginació de resultats"]'
  );
  //const paginationContainers = document.querySelector('.pagination-custom');

  // CONFIGURACIÓN CLAVE: URL de tu API Backend
  const API_URL = "http://localhost:8080/api/animals";

  // Ajusta la paginación según tu diseño
  const ANIMALS_PER_PAGE = 3;
  let currentAnimals = []; // Almacenará los animales cargados por el backend
  let currentPage = 1;

  //Genera la URL de la imatge
  function getAnimalUrlImatge(animal) {
    if (!animal.fotoPerfil) {
      return "/img/placeholder-default.jpg";
    }
    const carpeta = animal.especie
      .toLowerCase()
      .normalize("NFD")
      .replace(/[\u0300-\u036f]/g, "");
    return `/img/Animals/${carpeta}/${animal.fotoPerfil}`;
  }

  // ----------------------------------------------------
  // 1. Funciones de Renderizado (Sin Cambios)
  // ----------------------------------------------------

  //const createAnimalCard = (animal) => {
  function createAnimalCard(animal) {
    const imatgeUrl = getAnimalUrlImatge(animal);

    //Obtenim l'edat en format text
    const edat = animal.edat;

    let edatText;

    if(edat.anys === 0) {
      edatText = `${edat.mesos} mesos`;
    } else if (edat.mesos === 0) {
      edatText = `${edat.anys} anys`;
    } else {
      edatText = `${edat.anys} anys i ${edat.mesos} mesos`;
    }

    const sexe = animal.sexe;
    
    return `
            <div class="col-md-6 col-lg-4 mb-4">
                <div class="card animal-card-custom h-100">
                    <img src="${
                      imatgeUrl || "img/placeholder-default.jpg"
                    }" class="card-img-top card-img-custom" alt="Imatge de ${animal.nomAn}
                    ">
                    <div class="card-body d-flex flex-column">
                        <h3 class="card-title">${animal.nomAn}</h3>
                        <div class="d-flex justify-content-between align-items-center mb-2">
                            <span class="badge species-tag-custom me-2">${
                              animal.especie
                            }</span>
                            <small class="text-muted">${edatText}</small>
                        </div>
                        <p class="card-text small mb-2">
                            📍 ${edatText} | ${sexe}
                        </p>
                        <a href="detall-animal.html?id=${
                          animal.numId
                        }" class="btn btn-primary btn-card-detail-custom mt-auto">
                            Més detalls &rarr;
                        </a>
                    </div>
                </div>
            </div>
        `;
  }

  //const renderAnimals = (animals, page) => {
  function renderAnimals(animals, page) {
    currentPage = page;
    const start = (page - 1) * ANIMALS_PER_PAGE;
    const end = start + ANIMALS_PER_PAGE;
    const paginatedAnimals = animals.slice(start, end);

    if (paginatedAnimals.length === 0) {
      animalResultsContainer.innerHTML =
        '<div class="col-12 text-center"><p class="alert alert-warning">No s\'ha trobat cap animal amb aquests filtres.</p></div>';
    } else {
      animalResultsContainer.innerHTML = paginatedAnimals
        .map(createAnimalCard)
        .join("");
    }

    renderPagination(animals.length);
    resultsTitle.textContent = `Resultats (${animals.length} trobats)`;
  }

  //const renderPagination = (totalAnimals) => {
  function renderPagination(totalAnimals) {
    const totalPages = Math.ceil(totalAnimals / ANIMALS_PER_PAGE);
    if (totalPages <= 1) {
      paginationNav.innerHTML = "";
      return;
    }
    let paginationHtml = "";
    // Botón Anterior
    paginationHtml += `
            <li class="page-item ${currentPage === 1 ? "disabled" : ""}">
                <a class="page-link" href="#" data-page="${
                  currentPage - 1
                }">Anterior</a>
            </li>`;

    // Botones de Páginas
    for (let i = 1; i <= totalPages; i++) {
      paginationHtml += `
                <li class="page-item ${i === currentPage ? "active" : ""}"> 
                    <a class="page-link" href="#" data-page="${i}">${i}</a>
                </li>`;
    }

    // Botón Siguiente
    paginationHtml += `
            <li class="page-item ${
              currentPage === totalPages ? "disabled" : ""
            }">
                <a class="page-link" href="#" data-page="${
                  currentPage + 1
                }">Següent</a>
            </li>`;

    paginationNav.innerHTML = ` 
            <ul class="pagination pagination-custom justify-content-center">
                ${paginationHtml}
            </ul>`;
  }

  // ----------------------------------------------------
  // 2. Lógica de Carga y Fetch (Actualizado)
  // ----------------------------------------------------

  /**
   * Carga los animales del backend, con o sin filtros.
   * @param {URLSearchParams} [params=new URLSearchParams()] - Parámetros de filtro para la URL.
   */
  //async function fetchAnimals(params = new URLSearchParams()) {
  async function fetchAnimals(especie = null) {
    try {
      animalResultsContainer.innerHTML =
        '<div class="col-12 text-center"><p class="text-acento">Carregant animals...</p></div>';

      // Construye la URL completa con los parámetros (ej: /api/animals?species=Gos)

      let url = API_URL;
      if (especie && especie !== "Totes") {
        url += `?especie=${encodeURIComponent(especie)}`;
      }
      const response = await fetch(url);

      if (!response.ok) {
        // Manejar errores de respuesta HTTP (404, 500, etc.)
        throw new Error(`HTTP error! Status: ${response.status}`);
      }

      // El backend devuelve un array de objetos Animal
      const animals = await response.json();

      // Almacena los resultados y renderiza la primera página
      currentAnimals = animals;
      renderAnimals(currentAnimals, 1);
    } catch (error) {
      console.error(
        "Error al connectar amb el backend o carregar els animals:",
        error
      );
      animalResultsContainer.innerHTML = `<div class="col-12 text-center">
                    <p class="alert alert-danger">Error al connectar amb el servidor (${error.message}). Intenta-ho de nou més tard.</p>
                </div>`;
      resultsTitle.textContent = `Resultats (0 trobats)`;
    }
  }

  // ----------------------------------------------------
  // 3. Event Listeners
  // ----------------------------------------------------

  // 3.1 Submissió del Formulari de Filtre
  searchForm.addEventListener("submit", (event) => {
    event.preventDefault();
    fetchAnimals(selectEspecie.value);
  });

  // 3.2 Clics de Paginació
  paginationNav.addEventListener("click", (event) => {
    //const target = event.target.matches('.page-link');
    const clickedLink = event.target.closest(".page-link");
    if (clickedLink) {
      event.preventDefault();
      const newPage = parseInt(clickedLink.dataset.page);

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
  fetchAnimals();
});
