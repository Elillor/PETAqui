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
    // 4. Inicialización
    // ----------------------------------------------------
    const animalId = getAnimalIdFromUrl();
    
   

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
    // 3. Función para renderizar los detalles en el HTML (MODIFICADA)
    // ----------------------------------------------------

    //Metode per obtenir la url de la imatge
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

   

    const renderAnimalDetails = (animal) => {
        // Asignar el nombre para el título de la página
        document.title = `Adopta ${animal.nomAn || 'Animal'} - PETAqui`;

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
        
        const imatgeUrl = getAnimalUrlImatge(animal)

        // --- Detalls Ràpids ---
        document.getElementById('animal-image').src = imatgeUrl || 'img/placeholder-default.jpg';
        document.getElementById('animal-image').alt = `Imatge de ${animal.nomAn|| 'l\'animal'}`;
        
        document.getElementById('detail-name').textContent = animal.nomAn || 'Animal Desconegut';
        document.getElementById('detail-species').textContent = animal.especie || 'N/A';
        
        // Detalls ràpids requerits pel mockup
        document.getElementById('detail-age').textContent = `Edat: ${edatText || 'N/A'}`;
        document.getElementById('detail-gender').textContent = `Sexe: ${animal.sexe || 'N/A'}`;

        // --- Descripció ---
        document.getElementById('detail-description').textContent = animal.descripcio || 'Aquest animal encara no té una descripció completa, però està esperant la seva nova llar.';
        
        // --- Dades de la Protectora (Simulades o afegides) ---
        // NOTA: Si aquestes dades vénen de l'API de l'animal, caldria afegir-les a la crida.
        // Aquí les deixo amb valors de placeholder fins que es connectin.
        document.getElementById('detail-shelter-name').textContent = animal.shelterName || 'Protectora PETAqui';
        document.getElementById('detail-shelter-address').textContent = animal.shelterAddress || 'Contactar per més detalls.';
        document.getElementById('adoption-cta').href = `mailto:${animal.shelterEmail || 'contacte@petaqui.cat'}`;
    };

     // Iniciar la carga de los detalles
    fetchAnimalDetails(animalId);

    
    
});