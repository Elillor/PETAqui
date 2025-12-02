  const API_URL = "http://localhost:8080/api/admin";

  async function loadSection(section) {

    const content = document.getElementById("admin-content");
    if (!content) return;

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

    content.innerHTML = '<div class="spinner-border"></div> Cargando...';

    try {
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

      if (section === "usuaris") {
        console.log("Usuaris:", data);
        let html = `<h2>Gestió de ${section}</h2>`;
        html += `<div class="table-responsive">`;
        html += `<table class="table table-bordered border-primary">`;
        html += `<thead><tr><th scope="col">codiUs</th><th scope="col">nomUs</th>
                  <th scope="col">cognom1</th><th scope="col">cognom2</th><th scope="col">clauPas</th><th scope="col">emailUs</th>
                  <th scope="col">rolUs</th><th scope="col">Acciones</th></tr></thead>`;

        data.forEach((usuari) => {
          html += `<tr>`;

          html += `<td scope="row">${usuari.codiUs}</td>`;
          html += `<td>${usuari.nomUs}</td><td>${usuari.cognom1}</td><td>${usuari.cognom2}</td><td>${usuari.clauPas}</td><td>${usuari.emailUs}</td><td>${usuari.rolUs}</td>`;

          html += `<td>
                          <button class="btn btn-sm btn-warning"data-bs-toggle="tooltip" data-bs-placement="top" title="Editar" onclick="editUsuari('${usuari.codiUs})"><i class="bi bi-pencil-square"></i></button>
                          <button class="btn btn-sm btn-warning" data-bs-toggle="tooltip" data-bs-placement="top" title="Eliminar"onclick="deleteItem('${section}', ${usuari.codiUs})"><i class="bi bi-trash"></i></button>
                          </td>`;
        });

        html += "</tbody></table></div>";
        html += `<button class="btn btn-primary" onclick="addUsuari()">+ Añadir ${section.slice(
          0,
          -1
        )}</button>`;

        content.innerHTML = html;
      }

      if (section === "animals") {
        console.log("animals:", data);
        let html = `<h2>Gestion de ${section}</h2>`;
        html += `<div class="table-responsive">`;
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
          html += `<td>${animal.nomAn}</td><td>${animal.dataNeix}</td><td>${animal.sexe}</td><td>${animal.especie}</td><td>${animal.numXip}</td><td>${animal.esAdoptat}</td><td>${animal.descripcio}</td><td>${animal.fotoPerfil}</td><td>${animal.codiProt}</td>`;

          html += `<td>
                          <button class="btn btn-sm btn-warning"data-bs-toggle="tooltip" data-bs-placement="top" title="Editar" onclick="editanimal(${animal.numId})"><i class="bi bi-pencil-square"></i></button>
                          <button class="btn btn-sm btn-warning" data-bs-toggle="tooltip" data-bs-placement="top" title="Eliminar"onclick="deleteItem('${section}', ${animal.numId})"><i class="bi bi-trash"></i></button>
                          </td>`;
        });

        html += "</tbody></table></div>";
        html += `<button class="btn btn-primary" onclick="addanimal()">+ Añadir ${section.slice(
          0,
          -1
        )}</button>`;

        content.innerHTML = html;
      }

      if (section === "protectores") {
        console.log("protectores:", data);
        let html = `<h2>Gestion de ${section}</h2>`;
        html += `<div class="table-responsive">`;
        html += `<table class="table table-bordered border-primary">`;
        html += `<thead><tr><th scope="col">codiProt</th><th scope="col">nomProt</th>
                  <th scope="col">direccio</th><th scope="col">codiPostal</th><th scope="col">ciutat</th><th scope="col">tlfProt</th>
                  <th scope="col">emailProt</th><th scope="col">coordenadaX</th><th scope="col">coordenadaY</th><th scope="col">Acciones</th></tr></thead>`;

        data.forEach((protectora) => {
          html += `<tr>`;
          html += `<td scope="row">${protectora.codiProt}</td>`;
          html += `<td>${protectora.nomProt}</td><td>${protectora.direccio}</td><td>${protectora.codiPostal}</td><td>${protectora.ciutat}</td><td>${protectora.tlfProt}</td><td>${protectora.emailProt}</td><td>${protectora.coordenadaX}</td><td>${protectora.coordenadaY}</td>`;

          html += `<td>
                          <button class="btn btn-sm btn-warning"data-bs-toggle="tooltip" data-bs-placement="top" title="Editar" onclick="editprotectora(${protectora.codiProt})"><i class="bi bi-pencil-square"></i></button>
                          <button class="btn btn-sm btn-warning" data-bs-toggle="tooltip" data-bs-placement="top" title="Eliminar"onclick="deleteItem('${section}', ${protectora.codiProt})"><i class="bi bi-trash"></i></button>
                          </td>`;
        });

        html += "</tbody></table></div>";
        html += `<button class="btn btn-primary" onclick="addprotectora()">+ Añadir ${section.slice(
          0,
          -1
        )}</button>`;

        content.innerHTML = html;
      }
    } catch (err) {
      content.innerHTML = `<div class="alert alert-danger">Error: ${err.message}</div>`;
    }
  }

  function deleteItem(section, id) {
    if (!confirm("¿Eliminar este elemento?")) return;
    fetch(`${API_URL}/${section}/${id}`, {
      method: "DELETE",
      headers: { "Content-Type": "application/json" },
    }).then(() => loadSection(section));
  }

  /*function addanimal() {
    window.location.href = "/formCrearanimal";
  }

  function editanimal(id) {
    if (!id) {
      console.error("Id no valido:", id);
      return;
    }
    window.location.href = `/animals/${id}/editar`;
  }

  function addprotectora() {
    window.location.href = "/formCrearprotectora";
  }

  function editprotectora(id) {
    if (!id) {
      console.error("Id no valido:", id);
      return;
    }
    window.location.href = `/protectores/${id}/editar`;
  }*/

  function addUsuario() {
    window.location.href = "login.html";
  }

  function editUsuario(codiUs) {
    if (!codiUs) {
      console.error("Id no valido:", codiUs);
      return;
    }
    window.location.href = "perfil.html";
  }

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