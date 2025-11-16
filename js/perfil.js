document.addEventListener("DOMContentLoaded", () => {
  // ----------------------------------------------------
  // 1. Cargar y Mostrar Datos del Usuario
  // ----------------------------------------------------

  const profileCard = document.querySelector(".profile-card");
  if (!profileCard) {
    console.log(
      "No se encontró .profile-card. Esta no es la página de perfil."
    );
    return;
  }

  const currentUserJSON = localStorage.getItem("dadesPerfil");
  if (!currentUserJSON) {
    // Si no hay sesión, redirigir al login
    alert("Necessites iniciar sessió per veure el teu perfil.");
    window.location.href = "login.html";
    return;
  }

  const userData = JSON.parse(currentUserJSON);

  // Asignar los valores a los elementos de visualización (span o input readonly)
  document.getElementById("display-username").textContent = userData.nomUs;
  document.getElementById("display-handle").textContent = `@${
    userData.emailUs?.split("@")[0] || `usuari`
  }`;
  document.getElementById("display-name").value = userData.nomUs;
  document.getElementById("display-surname1").value = userData.cognom1;
  document.getElementById("display-surname2").value = userData.cognom2;
  document.getElementById("display-email").value = userData.emailUs;
  document.getElementById("display-password").value =
    userData.password || "********"; // Usamos la contraseña guardada

  // ----------------------------------------------------
  // 2. Lógica para Conmutar entre Visualización y Edición
  // ----------------------------------------------------
  const editButton = document.getElementById("editProfileBtn");

  let isEditing = false;

  // Función para conmutar el estado de los campos del formulario
  function toggleEditMode(enable) {
    const editableFields = profileCard.querySelectorAll(".editable-field");

    editableFields.forEach((field) => {
      field.readOnly = !enable;
      if (enable) {
        field.classList.add("editing"); // Opcional: añadir clase para estilos de edición
      } else {
        field.classList.remove("editing");
      }
    });

    // Conmutar botones
    if (enable) {
      editButton.textContent = "Guardar Canvis";
      editButton.classList.remove("btn-primary");
      editButton.classList.add("btn-success");
      isEditing = true;
    } else {
      editButton.textContent = "Editar Perfil";
      editButton.classList.remove("btn-success");
      editButton.classList.add("btn-primary");
      isEditing = false;
    }
  }

  // Manejador del botón principal (Editar/Guardar)
  editButton.addEventListener("click", () => {
    if (isEditing) {
      // Modo Guardar Canvis
      simulateSaveProfile();
      toggleEditMode(false); // Volver a modo visualización
    } else {
      // Modo Editar Perfil
      toggleEditMode(true);
    }
  });

  // ----------------------------------------------------
  // 3. Simulación de Guardado de Perfil
  // ----------------------------------------------------
  function simulateSaveProfile() {
    // 1. Capturar los nuevos valores
    const newUserData = {
      username: document.getElementById("display-username").textContent, // El username no es editable aquí
      name: document.getElementById("display-name").value,
      surname1: document.getElementById("display-surname1").value,
      surname2: document.getElementById("display-surname2").value,
      email: document.getElementById("display-email").value,
      // NOTA: La contraseña se maneja aparte en un entorno real. Aquí la mantenemos igual o capturamos nuevo valor si fue cambiado.
      password: document.getElementById("display-password").value,
    };

    // 2. Guardar los nuevos datos en localStorage (Simulación)
    localStorage.setItem("currentUser", JSON.stringify(newUserData));

    // 3. Actualizar el Handle (por si acaso el username se pudiera editar en el futuro, aunque aquí no lo hemos hecho)
    document.getElementById(
      "display-handle"
    ).textContent = `@${newUserData.username}`;

    // 4. Feedback al usuario
    alert("Perfil actualitzat correctament!");

    // El header se actualizará automáticamente si se cambia el username (si no se refresca la página)
    // Pero para asegurar que el dropdown del header se actualice si se cambia el nombre:
    // window.location.reload(); // Descomentar si es necesario un refresh completo.
  }

  // ----------------------------------------------------
  // 4. Lógica para Mostrar/Ocultar Contraseña
  // ----------------------------------------------------
  const passwordField = document.getElementById("display-password");
  const togglePasswordBtn = document.getElementById("togglePassword");

  if (togglePasswordBtn) {
    togglePasswordBtn.addEventListener("click", () => {
      const isPassword = passwordField.type === "password";
      passwordField.type = isPassword ? "text" : "password";

      // Cambiar icono del botón (opcional, requiere iconos SVG con clases)
      const icon = togglePasswordBtn.querySelector("svg");
      if (icon) {
        // Alternar icono de ojo
        icon.classList.toggle("bi-eye-fill", !isPassword);
        icon.classList.toggle("bi-eye-slash-fill", isPassword);
        // NOTA: Para que esto funcione, necesitas el icono de eye-slash en el HTML de perfil.html
      }
    });
  }

  // Inicializar: asegurarse de que los campos no están en modo edición al cargar
  toggleEditMode(false);
});
