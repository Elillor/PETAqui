// js/admin.js
// Archivo de administración del frontend para gestionar usuarios, animales y protectoras.
// Proporciona funciones para listar secciones, crear/editar/eliminar elementos y construir formularios dinámicos.

// URL base de la API de administración
const API_URL = "http://localhost:8080/api/admin";

// Carga la sección solicitada ('usuaris', 'animals', 'protectores') y renderiza HTML en el contenedor #admin-content
// Comprueba además que exista un usuario administrador en localStorage antes de permitir acceso.
async function loadSection(section) {
  const content = document.getElementById("admin-content");
  if (!content) return;

  // Verificar sesión de administrador guardada en localStorage
  const currentAdminJSON = localStorage.getItem("currentUser");
  if (!currentAdminJSON) {
    alert("Tens que iniciar sessió com a administrador.");
    window.location.href = "login.html";
    return;
  }
  const adminData = JSON.parse(currentAdminJSON);
  if (adminData.rolUs !== "ADMIN") {
    alert("Accés denegat. Només per a administradors.");
    window.location.href = "index.html";
    return;
  }

  // Indicador de carga mientras se obtiene la información
  content.innerHTML = '<div class="spinner-border"></div> Cargando...';

  try {
    // Petición GET a la API para la sección solicitada
    const response = await fetch(`${API_URL}/${section}`, {
      headers: {
        "Content-Type": "application/json",
      },
    });
    console.log(response);
    if (!response.ok) {
      const error = await response.text();
      throw new Error(error || "Error al cargar");
    }

    const data = await response.json();

    // Renderizado de la tabla para la sección 'usuaris' (usuarios)
    if (section === "usuaris") {
      console.log("Usuaris:", data);
      let html = `<h2>Gestió de ${section}</h2>`;
      html += `<div class="taula-admin-scroll">`; // ← SCROLL HORIZONTAL
      html += `<table class="table table-bordered border-primary">`;
      html += `<thead><tr><th scope="col">codiUs</th><th scope="col">nomUs</th>
                <th scope="col">cognom1</th><th scope="col">cognom2</th><th scope="col">clauPas</th><th scope="col">emailUs</th>
                <th scope="col">rolUs</th><th scope="col">Acciones</th></tr></thead>`;

      data.forEach((user) => {
        html += `<tr>`;
        html += `<td scope="row">${user.codiUs}</td>`;
        html += `<td>${user.nomUs}</td><td>${user.cognom1}</td><td>${user.cognom2}</td><td>${user.clauPas}</td><td>${user.emailUs}</td><td>${user.rolUs}</td>`;
        html += `<td>
                          <button class="btn btn-sm btn-warning" data-bs-toggle="tooltip" data-bs-placement="top" title="Editar" onclick="editUsuari('${user.codiUs}')"><i class="bi bi-pencil-square"></i></button>
                          <button class="btn btn-sm btn-warning" data-bs-toggle="tooltip" data-bs-placement="top" title="Eliminar"onclick="deleteItem('${section}', ${user.codiUs})"><i class="bi bi-trash"></i></button>
                          </td>`;
      });

      html += "</tbody></table></div>"; // ← TANCAM DIV SCROLL
      html += `<button class="btn btn-primary" onclick="addUsuari()">+ Añadir Usuari</button>`;
      content.innerHTML = html;
    }

    // Renderizado para 'animals' (animales)
    if (section === "animals") {
      console.log("animals:", data);
      let html = `<h2>Gestió de ${section}</h2>`;
      html += `<div class="taula-admin-scroll">`; // ← SCROLL HORIZONTAL
      html += `<table class="table table-bordered border-primary">`;
      html += `<thead><tr><th scope="col">numId</th><th scope="col">nomAn</th>
                <th scope="col">dataNeix</th><th scope="col">sexe</th><th scope="col">especie</th><th scope="col">numXip</th>
                <th scope="col">adoptat</th><th scope="col">descripció</th><th scope="col">fotoPerfil</th><th scope="col">codiProt</th><th scope="col">Accion</th></tr></thead>`;

      data.forEach((animal) => {
        html += `<tr>`;
        if (animal.esAdoptat === false) {
          animal.esAdoptat = "no adoptat";
        } else {
          animal.esAdoptat = "adoptat";
        }
        html += `<td scope="row">${animal.numId}</td>`;
        html += `<td>${animal.nomAn}</td><td>${animal.dataNeix}</td><td>${animal.sexe}</td><td>${animal.especie}</td><td>${animal.numXip}</td><td>${animal.esAdoptat}</td><td>${animal.descripcio}</td><td>${animal.fotoPerfil}</td><td>${animal.protectora.codiProt}</td>`;
        html += `<td>
                          <button class="btn btn-sm btn-warning"data-bs-toggle="tooltip" data-bs-placement="top" title="Editar" onclick="editAnimal('${animal.numId}')"><i class="bi bi-pencil-square"></i></button>
                          <button class="btn btn-sm btn-warning" data-bs-toggle="tooltip" data-bs-placement="top" title="Eliminar"onclick="deleteItem('${section}', ${animal.numId})"><i class="bi bi-trash"></i></button>
                          </td>`;
      });

      html += "</tbody></table></div>"; // ← TANCAM DIV SCROLL
      html += `<button class="btn btn-primary" onclick="addAnimal()">+ Añadir Animal</button>`;
      content.innerHTML = html;
    }

    // Renderizado para 'protectores' (protectora / refugios)
    if (section === "protectores") {
      console.log("protectores:", data);
      let html = `<h2>Gestió de ${section}</h2>`;
      html += `<div class="taula-admin-scroll">`; // ← SCROLL HORIZONTAL
      html += `<table class="table table-bordered border-primary">`;
      html += `<thead><tr><th scope="col">codiProt</th><th scope="col">nomProt</th>
                <th scope="col">adresa</th><th scope="col">codiPostal</th><th scope="col">localitat</th><th scope="col">provincia</th><th scope="col">url</th>
                <th scope="col">longitud</th><th scope="col">latitud</th><th scope="col">tlfProt</th><th scope="col">emailProt</th><th scope="col">Acciones</th></tr></thead>`;

      data.forEach((protectora) => {
        html += `<tr>`;
        html += `<td scope="row">${protectora.codiProt}</td>`;
        html += `<td>${protectora.nomProt}</td><td>${protectora.adresa}</td><td>${protectora.codiPostal}</td><td>${protectora.localitat}</td><td>${protectora.provincia}</td><td>${protectora.url}</td><td>${protectora.longitud}</td><td>${protectora.latitud}</td><td>${protectora.tlfProt}</td><td>${protectora.emailProt}</td>`;
        html += `<td>
                          <button class="btn btn-sm btn-warning"data-bs-toggle="tooltip" data-bs-placement="top" title="Editar" onclick="editProtectora('${protectora.codiProt}')"><i class="bi bi-pencil-square"></i></button>
                          <button class="btn btn-sm btn-warning" data-bs-toggle="tooltip" data-bs-placement="top" title="Eliminar"onclick="deleteItem('${section}', ${protectora.codiProt})"><i class="bi bi-trash"></i></button>
                          </td>`;
      });

      html += "</tbody></table></div>"; // ← TANCAM DIV SCROLL
      html += `<button class="btn btn-primary" onclick="addProtectora()">+ Añadir Protectora</button>`;
      content.innerHTML = html;
    }
  } catch (err) {
    content.innerHTML = `<div class="alert alert-danger">Error: ${err.message}</div>`;
  }
}

// >>>>>>>>>>>>>>>>>>>>>  FUNCIONES PARA LAS ACCIONES  >>>>>>>>>>>>>>>>>>>

// <<<<<  PARA USUARIOS  >>>>>>>>
// Abre el formulario para crear un usuario nuevo
function addUsuari() {
  formulari('usuaris', null);
}

// Recupera un usuario por id y abre el formulario para edición
async function editUsuari(codiUs) {
  try{
    const response = await fetch(`${API_URL}/usuaris/${codiUs}`);
    if (!response.ok) throw new Error('Usuari no trobat');
    const usuari = await response.json();
    formulari('usuaris', usuari);
  }catch (error){
    alert(`Error: ${error.message}`);
  }
}

// <<<<<<   PARA ANIMALES  >>>>>>>>
// Abrir formulario para añadir animal
function addAnimal() {
    formulari('animals', null);
}

// Obtener animal por id y abrir formulario de edición
async function editAnimal(numId) {
    try {
        const response = await fetch(`${API_URL}/animals/${numId}`);
        if (!response.ok) throw new Error('Animal no trobat');
        const animal = await response.json();
        formulari('animals', animal);
    } catch (err) {
        alert(`Error: ${err.message}`);
    }
}

// <<<<<< PARA PROTECTORAS >>>>>>>>
// Abrir formulario para crear protectora
function addProtectora() {
    formulari('protectores', null);
}

// Obtener protectora por id y abrir formulario de edición
async function editProtectora(codiProt) {
    try {
        const response = await fetch(`${API_URL}/protectores/${codiProt}`);
        if (!response.ok) throw new Error('Protectora no trobada');
        const protectora = await response.json();
        formulari('protectores', protectora);
    } catch (err) {
        alert(`Error: ${err.message}`);
    }
}

// <<<<<<<<  FUNCIÓN ELIMINAR (GENÉRICA PARA TODAS LAS SECCIONES)  >>>>>>>>>
// Pide confirmación y hace DELETE al endpoint correspondiente. Recarga la sección si se elimina correctamente.
async function deleteItem(section, id) {
  if (!confirm("¿Eliminar este elemento?")) return;
  try{
    const response = await fetch(`${API_URL}/${section}/${id}`, {
            method: "DELETE",
            headers: { "Content-Type": "application/json" }
        });

        if (response.ok) {
            alert('Eliminat correctament');
            loadSection(section);
        } else {
            throw new Error('Error en eliminar');
        }
    }catch(error){
      alert(`Error: ${error.message}`);
    }
}

// <<<<<<<<  FUNCIÓN QUE CREA LOS FORMULARIOS DINÁMICAMENTE >>>>>>>>>>>>>>
// Construye el HTML del formulario dependiendo de la sección y del item (si se está editando).
// Nota: devuelve el HTML y además añade el listener de submit que llama a submitFormulari.
async function formulari(section, item) {
  const title = item ? `Editar ${section.slice(0, -1)}` : `Afegir ${section.slice(0, -1)}`;
  let Html = `<h3>${title}</h3><form id="admin-form">`;

  if (section === 'usuaris') {
      Html += `
            <div class="d-flex flex-column align-items-center">
              <input type="hidden" id="codiUs" value="${item?.codiUs || ''}">
              <div class="mb-3 d-flex align-items-center" style="width: 100%; max-width: 500px;">
                  <label for="nomUs" class="form-label me-3" style="min-width: 120px;">Nom:</label>
                  <input type="text" class="form-control" id="nomUs" value="${item?.nomUs || ''}" required style="max-width: 300px;">
              </div>
              <div class="mb-3 d-flex align-items-center" style="width: 100%; max-width: 500px;">
                <label for="cognom1" class="form-label me-3" style="min-width: 120px;">Cognom 1:</label>
                <input type="text" class="form-control" id="cognom1" value="${item?.cognom1 || ''}" required style="max-width: 300px;">
              </div>
              <div class="mb-3 d-flex align-items-center" style="width: 100%; max-width: 500px;">
                <label for="cognom2" class="form-label me-3" style="min-width: 120px;">Cognom 2:</label>
                <input type="text" class="form-control" id="cognom2" value="${item?.cognom2 || ''}" style="max-width: 300px;">
              </div>
              <div class="mb-3 d-flex align-items-center" style="width: 100%; max-width: 500px;">
                <label for="emailUs" class="form-label me-3" style="min-width: 120px;">Correu electrònic:</label>
                <input type="email" class="form-control" id="emailUs" value="${item?.emailUs || ''}" required style="max-width: 300px;">
              </div>
              <div class="mb-3 d-flex align-items-center" style="width: 100%; max-width: 500px;">
                <label for="clauPas" class="form-label me-3" style="min-width: 120px;">Contrasenya:</label>
                <input type="password" class="form-control" id="clauPas" 
                      value="${item?.clauPas || ''}"required
                      style="max-width: 300px;">
              </div>
              <div class="mb-3 d-flex align-items-center" style="width: 100%; max-width: 500px;">
                <label for="rolUs" class="form-label me-3" style="min-width: 120px;">Rol:</label>
                <select class="form-control" id="rolUs" style="max-width: 300px;">
                  <option value="ADOPTANT" ${!item || item.rolUs === 'ADOPTANT' ? 'selected' : ''}>ADOPTANT</option>
                  <option value="ADMIN" ${item?.rolUs === 'ADMIN' ? 'selected' : ''}>ADMIN</option>
                </select>
              </div>
            </div>
            `;
  } else if (section === 'animals') {
      Html += `
          <div class="d-flex flex-column align-items-center">
              <input type="hidden" id="numId" value="${item?.numId || ''}">
              <div class="mb-3 d-flex align-items-center" style="width: 100%; max-width: 500px;">
                  <label for="nomAn" class="form-label me-3" style="min-width: 120px;">Nom:</label>
                  <input type="text" class="form-control" id="nomAn" value="${item?.nomAn || ''}" required style="max-width: 300px;">
              </div>
              <div class="mb-3 d-flex align-items-center" style="width: 100%; max-width: 500px;">
                  <label for="dataNeix" class="form-label me-3" style="min-width: 120px;">Data Neixament:</label>
                  <input type="date" class="form-control" id="dataNeix" value="${item?.dataNeix || ''}" required style="max-width: 300px;">
              </div>
              <div class="mb-3 d-flex align-items-center" style="width: 100%; max-width: 500px;">
                  <label for="sexe" class="form-label me-3" style="min-width: 120px;">Sexe:</label>
                  <input type="text" class="form-control" id="sexe" value="${item?.sexe || ''}" required style="max-width: 300px;">
              </div>
              <div class="mb-3 d-flex align-items-center" style="width: 100%; max-width: 500px;">
                  <label for="especie" class="form-label me-3" style="min-width: 120px;">Especie:</label>
                  <input type="text" class="form-control" id="especie" value="${item?.especie || ''}" required style="max-width: 300px;">
              </div>
              <div class="mb-3 d-flex align-items-center" style="width: 100%; max-width: 500px;">
                  <label for="numXip" class="form-label me-3" style="min-width: 120px;">Numero Xip:</label>
                  <input type="text" class="form-control" id="numXip" value="${item?.numXip || ''}" style="max-width: 300px;">
              </div>
              <div class="mb-3 d-flex align-items-center" style="width: 100%; max-width: 500px;">
                <label for="esAdoptat" class="form-label me-3" style="min-width: 120px;">esAdoptat:</label>
                <select class="form-control" id="esAdoptat" style="max-width: 300px;">
                  <option value="false" ${!item || item.esAdoptat === 'false' ? 'selected' : ''}>ADOPTAT</option>
                  <option value="true" ${item?.esAdoptat === 'true' ? 'selected' : ''}>NO ADOPTAT</option>
                </select>
              </div>
              <div class="mb-3 d-flex align-items-center" style="width: 100%; max-width: 500px;">
                  <label for="descripcio" class="form-label me-3" style="min-width: 120px;">Descripció:</label>
                  <textarea class="form-control" id="descripcio" style="max-width: 300px;"rows="5">${item?.descripcio || ''}</textarea>
              </div>
              <div class="mb-3 d-flex align-items-center" style="width: 100%; max-width: 500px;">
                  <label for="fotoPerfil" class="form-label me-3" style="min-width: 120px;">Foto Perfil:</label>
                  <input type="text" class="form-control" id="fotoPerfil" value="${item?.fotoPerfil || ''}" required style="max-width: 300px;">
              </div>
              <div class="d-flex align-items-center mb-3" style="width: 100%; max-width: 500px;">
                  <label for="codiProt" class="form-label me-3" style="min-width: 120px;">Protectora:</label>
                  <select class="form-control" id="codiProt" style="max-width: 300px;">
                      ${await ProtectorasSelect(item?.codiProt)}
                  </select>
              </div>
          </div>
          `;
  } else if (section === 'protectores') {
      Html += `
          <div class="d-flex flex-column align-items-center">
              <input type="hidden" id="codiProt" value="${item?.codiProt || ''}">
              <div class="mb-3 d-flex align-items-center" style="width: 100%; max-width: 500px;">
                  <label for="nomProt" class="form-label me-3" style="min-width: 120px;">Nom:</label>
                  <input type="text" class="form-control" id="nomProt" value="${item?.nomProt || ''}" required style="max-width: 300px;">
              </div>
              <div class="mb-3 d-flex align-items-center" style="width: 100%; max-width: 500px;">
                  <label for="adresa" class="form-label me-3" style="min-width: 120px;">Adresa:</label>
                  <input type="text" class="form-control" id="adresa" value="${item?.adresa || ''}" required style="max-width: 300px;">
              </div>
              <div class="mb-3 d-flex align-items-center" style="width: 100%; max-width: 500px;">
                  <label for="codiPostal" class="form-label me-3" style="min-width: 120px;">Codi Postal:</label>
                  <input type="text" class="form-control" id="codiPostal" value="${item?.codiPostal || ''}"style="max-width: 300px;">
              </div>
              <div class="mb-3 d-flex align-items-center" style="width: 100%; max-width: 500px;">
                  <label for="localitat" class="form-label me-3" style="min-width: 120px;">Localitat:</label>
                  <input type="text" class="form-control" id="localitat" value="${item?.localitat || ''}" required style="max-width: 300px;">
              </div>
              <div class="mb-3 d-flex align-items-center" style="width: 100%; max-width: 500px;">
                  <label for="provincia" class="form-label me-3" style="min-width: 120px;">Codi Provincia:</label>
                  <input type="text" class="form-control" id="provincia" value="${item?.provincia || ''}" required style="max-width: 300px;">
              </div>
              <div class="mb-3 d-flex align-items-center" style="width: 100%; max-width: 500px;">
                  <label for="url" class="form-label me-3" style="min-width: 120px;">Web:</label>
                  <input type="text" class="form-control" id="url" value="${item?.url || ''}"style="max-width: 300px;">
              </div>
              <div class="mb-3 d-flex align-items-center" style="width: 100%; max-width: 500px;">
                  <label for="emailProt" class="form-label me-3" style="min-width: 120px;">Correu Electrónic:</label>
                  <input type="email" class="form-control" id="emailProt" value="${item?.emailProt || ''}"style="max-width: 300px;">
              </div>
              <div class="mb-3 d-flex align-items-center" style="width: 100%; max-width: 500px;">
                  <label for="longitud" class="form-label me-3" style="min-width: 120px;">Longitud:</label>
                  <input type="number" class="form-control" id="longitud" value="${item?.longitud || ''}" required style="max-width: 300px;">
              </div>
              <div class="mb-3 d-flex align-items-center" style="width: 100%; max-width: 500px;">
                  <label for="latitud" class="form-label me-3" style="min-width: 120px;">Latitud:</label>
                  <input type="number" class="form-control" id="latitud" value="${item?.latitud || ''}" required style="max-width: 300px;">
              </div>
              <div class="mb-3 d-flex align-items-center" style="width: 100%; max-width: 500px;">
                  <label for="tlfProt" class="form-label me-3" style="min-width: 120px;">Telefon:</label>
                  <input type="text" class="form-control" id="tlfProt" value="${item?.tlfProt || ''}"style="max-width: 300px;">
              </div>
              
          </div>
      `;
  }

  Html += `
      <button type="submit" class="btn btn-primary">Desar</button>
      <button type="button" class="btn btn-secondary ms-2" onclick="tancaFormulari('${section}')">Cancel·lar</button>
  </form>`;

  // Insertar el formulario generado en el DOM
  document.getElementById('admin-content').innerHTML = Html;

  // Añadir manejador para el submit del formulario
  document.getElementById('admin-form').addEventListener('submit', (e) => {
      e.preventDefault();
      submitFormulari(section, item);
  });
}

// <<<<<< FUNCION QUE DEPENDE DE LA ACCIÓN (POST O PUT)  >>>>>>>>>>>>
// Construye el objeto 'data' según la sección y decide si hace POST (crear) o PUT (editar)
// Observaciones importantes:
// - Para usuarios, si el campo de contraseña está vacío en edición, no se incluye en el payload.
// - Para animales, se crea un objeto protectora con { codiProt } si se selecciona.
// - La URL para PUT usa la id correspondiente (codiUs / numId / codiProt)
async function submitFormulari(section, item) {
  const isEdit = !!item;
  let data = {};

  if (section === 'usuaris') {
      data = {
          codiUs: isEdit ? parseInt(document.getElementById('codiUs').value) : null,
          nomUs: document.getElementById('nomUs').value,
          cognom1: document.getElementById('cognom1').value,
          cognom2: document.getElementById('cognom2').value || null,
          emailUs: document.getElementById('emailUs').value,
          rolUs: document.getElementById('rolUs').value
      };
      const clauPasInput = document.getElementById('clauPas');
      if (!item || (clauPasInput && clauPasInput.value.trim() !== '')) {
        data.clauPas = clauPasInput?.value || '';
      }
  } else if (section === 'animals') {
      const codiProtValue = document.getElementById('codiProt')?.value;
      let protectora = null;
      if (codiProtValue && codiProtValue !== "") {
        protectora = {codiProt:parseInt(codiProtValue)};
      }
      data = {
          numId: isEdit ? parseInt(document.getElementById('numId').value) : null,
          nomAn: document.getElementById('nomAn').value,
          dataNeix: document.getElementById('dataNeix').value,
          sexe: document.getElementById('sexe').value,
          especie: document.getElementById('especie').value,
          numXip: document.getElementById('numXip').value || null,
          esAdoptat: document.getElementById('esAdoptat').value === 'true',
          descripcio: document.getElementById('descripcio').value,
          fotoPerfil: document.getElementById('fotoPerfil').value,
          protectora: protectora
      };
  } else if (section === 'protectores') {
      data = {
          codiProt: isEdit ? parseInt(document.getElementById('codiProt').value) : null,
          nomProt: document.getElementById('nomProt').value,
          adresa: document.getElementById('adresa').value || null,
          codiPostal: document.getElementById('codiPostal').value,
          localitat: document.getElementById('localitat').value,
          provincia: document.getElementById('provincia').value,
          url: document.getElementById('url').value,
          longitud: parseFloat(document.getElementById('longitud').value) || 0,
          latitud: parseFloat(document.getElementById('latitud').value) || 0,
          tlfProt: document.getElementById('tlfProt').value || null,
          emailProt: document.getElementById('emailProt').value
      };
  }

  const url = isEdit 
      ? `${API_URL}/${section}/${data.codiUs || data.numId || data.codiProt}` 
      : `${API_URL}/${section}`;

  const method = isEdit ? 'PUT' : 'POST';

  try {
      const response = await fetch(url, {
          method: method,
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(data)
      });

      if (!response.ok) {
          const errorText = await response.text();
          throw new Error(errorText || 'Error al desar');
      }

      alert(isEdit ? 'Desat correctament' : 'Creat correctament');
      loadSection(section);

  } catch (err) {
      alert(`Error: ${err.message}`);
  }
}

// Cierra el formulario y recarga la lista de la sección indicada
function tancaFormulari(section) {
  loadSection(section);
}

// Obtener select de protectoras para formulario de animales
async function ProtectorasSelect(selected = null) {
  const res = await fetch(`${API_URL}/protectores`);
  const protectores = await res.json();
  let options = '<option value="">Sense assignar</option>';
  protectores.forEach(p => {
      options += `<option value="${p.codiProt}" ${p.codiProt == selected ? 'selected' : ''}>${p.nomProt} (${p.localitat})</option>`;
  });
  return options;
}

// Verificación inicial al cargar DOM: asegurar que el usuario actual es ADMIN antes de permitir acceso a la página
document.addEventListener("DOMContentLoaded", () => {
  const currentAdminJSON = localStorage.getItem("currentUser");
  if (!currentAdminJSON) {
    alert("Tens que iniciar sessió com a administrador.");
    window.location.href = "login.html";
    return;
  }
  const adminData = JSON.parse(currentAdminJSON);
  if (adminData.rolUs !== "ADMIN") {
    alert("Accés denegat. Només per a administradors.");
    window.location.href = "index.html";
    return;
  }
});