const loginForm = document.getElementById("loginForm");
const registerForm = document.getElementById("registerForm");

if (loginForm) {
  loginForm.addEventListener("submit", async (event) => {
    event.preventDefault();

    const email = document.getElementById("login-email").value;
    const password = document.getElementById("login-password").value;

    try {
      const response = await fetch("http://localhost:8080/api/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          email,
          password,
        }),
      });

      // El proceso de login fue exitoso (código 200-299)
      if (response.ok) {
        // 1. OBTENER LOS DATOS DEL USUARIO DEL SERVIDOR
        const userData = await response.json();

        // 2. CONEXIÓN CRÍTICA: GUARDAR EN localStorage
        // Esto activa el mecanismo de auth.js y permite a perfil.js cargar los datos.
        localStorage.setItem(
          "currentUser",
          JSON.stringify({
            nomUs: userData.nomUs,
            emailUs: userData.emailUs,
            rolUs: userData.rolUs,
          })
        ); // La clave 'currentUser' es leída por auth.js y perfil.js

        localStorage.setItem(
          "dadesPerfil",
          JSON.stringify({
            nomUs: userData.nomUs,
            cognom1: userData.cognom1,
            cognom2: userData.cognom2,
            emailUs: userData.emailUs,
          })
        );
        alert("Login exitós!");

        // Redirigir al perfil o a la página principal como usuario logeado
        window.location.href = "index.html"; // Redirigimos al perfil para confirmar
      } else {
        // Error de credenciales o de servidor (ej: 401, 404, 500)
        const errorData = await response.text();
        alert("Credencials incorrectes");
        console.error("Error details:", errorData);
      }
    } catch (error) {
      // Error de red (el servidor no responde)
      alert("Error de connexió. Intenta-ho novament més tard.");
      console.error("Fetch error:", error);
    }
  });
}
if (registerForm) {
  registerForm.addEventListener("submit", async (event) => {
    event.preventDefault();

    const password = document.getElementById("registerPassword").value;
    const confirmPassword = document.getElementById(
      "registerConfirmPassword"
    ).value;

    if (password !== confirmPassword) {
      alert("Les contrassenyes no coincideixen");
      return;
    }

    const userData = {
      nomUs: document.getElementById("registerName").value,
      cognom1: document.getElementById("registerSurname1").value,
      cognom2: document.getElementById("registerSurname2").value || null,
      emailUs: document.getElementById("registerUsername").value,
      clauPas: password,
    };

    try {
      const response = await fetch("http://localhost:8080/api/register", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(userData),
      });

      if (response.ok) {
        alert("Registre exitós! Ara pots iniciar sessió.");
        window.location.href = "login.html"; // Redirigir a la página de login
      } else {
        const errorData = await response.text();
        alert("Error en el registre. Si us plau, intenta-ho novament.");
        console.error("Error details:", errorData);
      }
    } catch (error) {
      alert("Error de connexió. Intenta-ho novament més endevant.");
      console.error("Fetch error:", error);
    }
  });

  // Lógica para 'Has oblidat la contrasenya?

  function mostrarAlertaContrasenyaOblidada(event) {
    // 1. Prevenir la acción por defecto del enlace (evita la recarga de la página)
    if (event) {
      event.preventDefault();
    }

    // 2. Mostrar la alerta (popup)
    alert("Contacta amb l'administrador.");
  }
}
