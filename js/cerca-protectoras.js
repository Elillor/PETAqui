// Variable global per Leaflet
let map;
let marker;

// NOVES VARIABLES PER GEOLOCALITZACIÓ
const MAX_DISTANCE_KM = 50; // Límit de distància per filtrar (50km)
let userLocation = null;
// FI NOVES VARIABLES

// URL base del backend (a través del proxy d'Apache)
const API_URL = '/api/protectores'; // ← sense http://localhost:8080 gràcies al ProxyPass

document.addEventListener('DOMContentLoaded', () => {
    // ===============================================
    // 1. MOCK DATA (COORDINADES USANT DECIMAL POINT)
    // ===============================================
    /*const MOCK_PROTECTORAS = [
        // Latitud i Longitud (exemple de Barcelona, Lleida, etc.)
        { id: 1, name: "L'Arca de Noè", location: 'Barcelona', address: 'Carrer Fictici, 1, 08001 Barcelona', email: 'arca@email.cat', phone: '931 111 111', web: 'https://arca.cat', lat: 41.3851, lng: 2.1734 },
        { id: 2, name: 'APAP - Associació Protectora de Lleida', location: 'Lleida', address: 'Plaça Rural, 5, 25000 Lleida', email: 'apap@email.cat', phone: '973 222 222', web: '', lat: 41.6167, lng: 0.6215 },
        { id: 3, name: 'Petits Peluts de Tarragona', location: 'Tarragona', address: 'Ronda Gats, 10, 43001 Tarragona', email: 'peluts@email.cat', phone: '977 333 333', web: 'https://peluts.org', lat: 41.1182, lng: 1.2443 },
        { id: 4, name: 'Refugi de Girona', location: 'Girona', address: 'Av. Gran, 20, 17001 Girona', email: 'refugi@email.cat', phone: '972 444 444', web: '', lat: 41.9794, lng: 2.8214 },
        { id: 5, name: 'Exòtics BCN', location: 'Barcelona', address: 'Carrer Exòtic, 3, 08005 Barcelona', email: 'exotics@email.cat', phone: '931 555 555', web: 'https://exoticsbcn.com', lat: 41.4005, lng: 2.1901 },
        { id: 6, name: 'Gatets Felices', location: 'Tarragona', address: 'Carrer Feliç, 15, 43003 Tarragona', email: 'gatets@email.cat', phone: '600 666 666', web: '', lat: 41.1105, lng: 1.2505 },
        { id: 7, name: 'Tots els amics', location: 'Lleida', address: 'Plaça Amiga, 7, 25002 Lleida', email: 'amics@email.cat', phone: '973 777 777', web: '', lat: 41.6160, lng: 0.6120 },
    ];*/

    // ===============================================
    // 2. ELEMENTOS DEL DOM
    // ===============================================
    const protectoraSelect = document.getElementById('protectora-select');
    const selectedProtectoraName = document.getElementById('selected-protectora-name');
    const protectoraAddress = document.getElementById('protectora-address');
    const protectoraEmail = document.getElementById('protectora-email');
    const protectoraPhone = document.getElementById('protectora-phone');
    const protectoraWeb = document.getElementById('protectora-web');
    const textSearchInput = document.getElementById('text-search-input');
    const searchFormText = document.getElementById('search-form-text');
    const proximitySearchBtn = document.getElementById('proximity-search-btn');
    
    // Configuració del mapa
    const DEFAULT_CENTER = [41.60, 1.80]; // Centre de Catalunya (Lat/Lng)
    const MAP_ZOOM_DEFAULT = 8;
    const MAP_ZOOM_DETAIL = 13;

    // ===============================================
    // 3. FUNCIONES DE MAPA (Leaflet)
    // ===============================================
    
    /**
     * Inicialitza el mapa amb Leaflet.
     */
    function initMap() {
        // Assegurem que la llibreria Leaflet estigui carregada
        if (typeof L === 'undefined') {
             console.error("Leaflet no ha carregat correctament.");
             // Si el mapa falla, almenys carreguem una llista buida
             populateProtectoraSelect([]);
             return;
        }

        const mapElement = document.getElementById("map-container");

        // Crea el mapa centrat al centre de Catalunya
        map = L.map(mapElement).setView(DEFAULT_CENTER, MAP_ZOOM_DEFAULT);

        // Afegeix el layer d'OpenStreetMap
        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
            maxZoom: 19,
            attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
        }).addTo(map);

        // Inicialitza el marcador, però sense posició visible inicialment
        marker = L.marker(DEFAULT_CENTER, { opacity: 0 }).addTo(map);

        // MODIFICACIÓ CLAU: Després d'inicialitzar el mapa, carreguem una llista VUITA
        // Les protectores només es mostraran després d'una cerca.
        populateProtectoraSelect([]); 
    }
    
    /**
     * Actualitza la posició del mapa i el marcador.
     */
    function updateMap(lat, lng, name) {
        if (!map || !marker) return; 

        const position = [lat, lng];
        
        // Moure el mapa al nou centre i zoom
        map.setView(position, MAP_ZOOM_DETAIL);
        
        // Actualitzar la posició del marcador
        marker.setLatLng(position);
        marker.bindPopup(`<b>${name}</b>`).openPopup();
        marker.setOpacity(1); // Fer visible
    }
    
    // ===============================================
    // 4. FUNCIONES DE RENDERIZADO
    // ===============================================

    /**
     * Omple el selector amb les protectores disponibles.
     * @param {Array} protectorasList - Llista de protectores (filtrada o completa) a renderitzar.
     */
    function populateProtectoraSelect(protectorasList = []) { // Default a llista buida
        if (!protectoraSelect) return;
        
        // Guardem el valor seleccionat actualment per intentar mantenir-lo
        const currentSelectedId = protectoraSelect.value;
        // El placeholder del select s'ha modificat a l'HTML
        let optionsHtml = '<option value="">Selecciona directament una protectora</option>'; 
        let isCurrentSelectedInFilteredList = false;

        protectorasList.forEach(protectora => {
            if (protectora.codiProt == currentSelectedId) {
                isCurrentSelectedInFilteredList = true;
            }
            // Si la protectora té distància (cerca per proximitat), l'afegim al text
            //const distanceText = protectora.distance ? ` - ${protectora.distance.toFixed(1)} km` : ''; 
            const location = protectora.provincia || 'Ubicació desconeguda';
            optionsHtml += `<option value="${protectora.codiProt}">${protectora.nomProt} (${location})</option>`;
        });

        protectoraSelect.innerHTML = optionsHtml;
        
        // Si la llista filtrada és buida, o la selecció no hi és, o és el placeholder inicial
        if (protectorasList.length === 0 || !isCurrentSelectedInFilteredList) {
             protectoraSelect.value = ""; // Assegurem que es torna a l'opció per defecte
             renderProtectoraDetails(null);
        } else if (isCurrentSelectedInFilteredList) {
             // Intentem restaurar la selecció
             protectoraSelect.value = currentSelectedId;
             const selectedProtectora = protectorasList.find(p => p.codiProt == currentSelectedId);
             renderProtectoraDetails(selectedProtectora);
        }
    }
    /**
     * Mostra els detalls de la protectora seleccionada i actualitza el mapa.
     */
    function renderProtectoraDetails(protectora) {
        if (!protectora) {
            selectedProtectoraName.textContent = "Tria una protectora per veure'n els detalls.";
            protectoraAddress.innerHTML = 'Direcció Protectora: ';
            protectoraEmail.innerHTML = 'Email Protectora: ';
            protectoraPhone.innerHTML = 'Telèfon Protectora: ';
            protectoraWeb.innerHTML = 'Web Protectora Opcional: ';
            
            // Amagar el marcador i centrar el mapa al default
            if (marker) marker.setOpacity(0);
            if (map) map.setView(DEFAULT_CENTER, MAP_ZOOM_DEFAULT);
            return;
        }

        // 1. Actualitzar el títol
        selectedProtectoraName.textContent = protectora.nomProt; 
        
        // 2. Remplenar els camps
        protectoraAddress.innerHTML = `Direcció Protectora: <strong>${protectora.adresa}</strong>`;
        protectoraEmail.innerHTML = `Email Protectora: <strong>${protectora.emailProt}</strong>`;
        protectoraPhone.innerHTML = `Telèfon Protectora: <strong>${protectora.tlfProt}</strong>`;
        
        if (protectora.url) {
             protectoraWeb.innerHTML = `Web Protectora Opcional: <a href="${protectora.url}" target="_blank"><strong>${protectora.url.replace(/^https?:\/\//, '')}</strong></a>`;
        } else {
             protectoraWeb.innerHTML = 'Web Protectora Opcional: <em>No disponible</em>';
        }
        
        // 3. Actualitzar el mapa
        if (protectora.latitud && protectora.longitud) {
            updateMap(protectora.latitud, protectora.longitud, protectora.nomProt);
        }
    }

    // Carrega totes les protectores
    async function fetchAllProtectoras() {
        try {
            const response = await fetch(API_URL);
            if (!response.ok) {
                throw new Error(`Error al carregar les protectores: ${response.statusText}`);
            };
            const data = await response.json();
            populateProtectoraSelect(data);
        } catch (error) {
            console.error("Error:", error);
            alert("Hi ha hagut un error carregant les protectores. Si us plau, intenta-ho més tard.");
        }
    }
    // ===============================================
    // 5. LÓGICA DE FILTRADO (TEXTUAL I PROXIMITAT)
    // ===============================================

    async function applyTextFilter(searchText) {
        //let currentList = MOCK_PROTECTORAS;

        if (!searchText.trim()) {
            fetchAllProtectoras();
            return;
            //const lowerCaseFilter = searchText.toLowerCase();
            
            // Filtrar per nom o localització (ciutat)
            //currentList = MOCK_PROTECTORAS.filter(p => 
                //p.name.toLowerCase().includes(lowerCaseFilter) || 
                //p.location.toLowerCase().includes(lowerCaseFilter)
            //);
        //} else {
             // Si el text de cerca està buit després de fer clic a "Buscar",
             // carreguem una llista buida per complir amb el requisit.
             //currentList = [];
        }
        try {
            // 1. Cerca per nom
            const reponseNom = await fetch(`${API_URL}?nomProt=${encodeURIComponent(searchText)}`);
            if (reponseNom.ok) {
                const dataNom = await reponseNom.json();
                //Convertim a array si es un sol objecte
                const protectoras = Array.isArray(dataNom) ? dataNom : [dataNom];
                populateProtectoraSelect(protectoras);
                return;
            }

            // 2. Cerca per provincia
            const reponseProvincia = await fetch(`${API_URL}?provincia=${encodeURIComponent(searchText)}`);
            if (reponseProvincia.ok) {
                const dataProvincia = await reponseProvincia.json();
                populateProtectoraSelect(dataProvincia);
                return;
            }
        // Actualitzar el select amb la llista filtrada.
            populateProtectoraSelect([]);
        // Netejar la ubicació de l'usuari si es fa una cerca de text
        //userLocation = null; 
        } catch (error) {
            console.error("Error en la cerca textual:", error);
            alert("Hi ha hagut un error realitzant la cerca. Si us plau, intenta-ho més tard.");
            populateProtectoraSelect([]); // Buidem la llista en cas d'error
        }
    }

    /**
     * Converteix graus a radians.
     */
    function deg2rad(deg) {
        return deg * (Math.PI / 180);
    }
    
    /**
     * Calcula la distància en quilòmetres entre dos punts GPS (Fórmula de Haversine).
     */
    function getDistanceFromLatLonInKm(lat1, lon1, lat2, lon2) {
        const R = 6371; // Radi de la Terra en km
        const dLat = deg2rad(lat2 - lat1);
        const dLon = deg2rad(lon2 - lon1); 
        const a = 
            Math.sin(dLat / 2) * Math.sin(dLat / 2) +
            Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2); 
        const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)); 
        return R * c; 
    }

    async function filterByProximity() {
        // Si la ubicació ja està guardada, filtrem directament
        //if (userLocation) {
            //performProximityFilter(userLocation);
            //return;
        //}

        // Si no està guardada, intentem obtenir-la
        if (!navigator.geolocation) {
            //console.log("Intentant obtenir ubicació de l'usuari...");
            alert("El teu navegador no suporta la Geolocalització per a la cerca per proximitat.");
            return;
        }
        protectoraSelect.innerHTML = '<option value="">Obtenint la teva ubicació...</option>';
        try {
            //console.log("Intentant obtenir ubicació de l'usuari...");
            const position = await new Promise((resolve, reject) => {
                navigator.geolocation.getCurrentPosition( resolve, reject, { 
                    enableHighAccuracy: true, 
                    timeout: 5000 
                });
            });
            userLocation = {
                lat: position.coords.latitude,
                lng: position.coords.longitude
            };
            // Carrega totes les protectores i filtra per distància
            const response = await fetch(API_URL);
            if (!response.ok) {
                throw new Error(`Error al carregar les protectores`);
            };
            const allProtectoras = await response.json();

            const nearbyProtectoras = allProtectoras
                .map(p => ({
                    ...p,
                    distance: getDistanceFromLatLonInKm(
                        userLocation.lat, 
                        userLocation.lng, 
                        p.latitud,
                         p.longitud
                    )
                }))
                .filter(p => p.distance <= MAX_DISTANCE_KM)
                .sort((a, b) => a.distance - b.distance); // Ordenar per distància (més a prop primer)

            if (nearbyProtectoras.length === 0) {
                alert(`No s'han trobat protectores a menys de ${MAX_DISTANCE_KM}km de la teva ubicació.`);
                populateProtectoraSelect([]); // Buidem la llista
            } else {
                // Afegim la distància al nom per al select
                const optionsHtml = '<option value="">Selecciona directament una protectora</option>' +
                    nearbyProtectoras.map(protectora => {
                        const location = protectora.provincia || 'Ubicació desconeguda';
                        return `<option value="${protectora.codiProt}">${protectora.nomProt} (${location}) - ${protectora.distance.toFixed(1)} km</option>`;
                    }).join('');
                protectoraSelect.innerHTML = optionsHtml;
            }
        } catch (error) {
            console.error("Error obtenint ubicació o carregant protectores:", error);
            alert("No es pot obtenir la teva ubicació");
        }
    }
                /*(position) => {
                    userLocation = {
                        lat: position.coords.latitude,
                        lng: position.coords.longitude
                    };
                    performProximityFilter(userLocation);
                },
                (error) => {
                    console.warn("Error obtenint ubicació:", error.message);
                    alert("No es pot obtenir la teva ubicació. Assegura't de donar permís al navegador.");
                    populateProtectoraSelect([]); // Si falla, buidem la llista
                },
                { enableHighAccuracy: true, timeout: 5000, maximumAge: 0 }
            );
        } else {
            alert("El teu navegador no suporta la Geolocalització per a la cerca per proximitat.");
            populateProtectoraSelect([]); // Si no hi ha suport, buidem la llista
        }
    }

    function performProximityFilter(location) {
        const { lat, lng } = location;
        
        // Calcular distàncies
        const nearbyProtectoras = MOCK_PROTECTORAS
            .map(p => ({
                ...p,
                distance: getDistanceFromLatLonInKm(lat, lng, p.lat, p.lng)
            }))
            .filter(p => p.distance <= MAX_DISTANCE_KM)
            .sort((a, b) => a.distance - b.distance); // Ordenar per distància (més a prop primer)

        // Notificar l'usuari
        if (nearbyProtectoras.length === 0) {
            alert(`No s'han trobat protectores a menys de ${MAX_DISTANCE_KM}km de la teva ubicació.`);
            populateProtectoraSelect([]); // Buidem la llista
        } else {
            alert(`Trobades ${nearbyProtectoras.length} protectores a menys de ${MAX_DISTANCE_KM}km. Llistat actualitzat i ordenat per proximitat.`);
            populateProtectoraSelect(nearbyProtectoras);
        }
        
        // Netejar la cerca de text
        textSearchInput.value = '';
    }*/


    // ===============================================
    // 6. EVENT LISTENERS
    // ===============================================
    
    // A. Control del formulario de búsqueda (Text Search)
    if (searchFormText) {
        searchFormText.addEventListener('submit', (e) => {
            e.preventDefault(); 
            //const searchText = textSearchInput.value.trim();
            applyTextFilter(textSearchInput.value); 
        });
    }

    // B. Control del select (Selector de Protectora)
    if (protectoraSelect) {
        protectoraSelect.addEventListener('change', (e) => {
            const selectedId = parseInt(e.target.value);
            
            if (selectedId) {
                //const selectedProtectora = MOCK_PROTECTORAS.find(p => p.id === selectedId);
                fetch(`${API_URL}/${selectedId}`)
                    .then(response => response.json())
                    .then(renderProtectoraDetails)
                    .catch(error => console.error("Error:", error));
            } else {
                renderProtectoraDetails(null); 
            }
        });
    }

    // C. Control del botó de cerca per proximitat (NOU)
    if (proximitySearchBtn) {
        proximitySearchBtn.addEventListener('click', () => {
            filterByProximity(); 
        });
    }
    
    // D. Càrrega Inicial del Mapa i Llistat
    initMap(); 
    fetchAllProtectoras();
});