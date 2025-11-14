document.addEventListener("DOMContentLoaded", () => {
  // Contenedor donde se insertará el botón/dropdown de login/perfil
  const loginOrProfileBtnContainer = document.getElementById(
    "login-or-profile-btn"
  );

  // ===============================================
  // Lógica de Pestañas (Tabs) - AÑADIDA AQUÍ
  // ===============================================
  const loginBtn = document.getElementById("loginBtn");
  const registerBtn = document.getElementById("registerBtn");
  const loginForm = document.getElementById("loginForm");
  const registerForm = document.getElementById("registerForm");

  // 1. Función para mostrar el formulario de Login
  function showLoginForm() {
    if (loginBtn && registerBtn && loginForm && registerForm) {
      loginBtn.classList.add("active");
      registerBtn.classList.remove("active");
      loginForm.classList.add("active");
      registerForm.classList.remove("active");
    }
  }

  // 2. Función para mostrar el formulario de Registro
  function showRegisterForm() {
    if (loginBtn && registerBtn && loginForm && registerForm) {
      registerBtn.classList.add("active");
      loginBtn.classList.remove("active");
      registerForm.classList.add("active");
      loginForm.classList.remove("active");
    }
  }

  // 3. Asignar los eventos (solo si estamos en login.html y existen los botones)
  if (loginBtn && registerBtn) {
    loginBtn.addEventListener("click", showLoginForm);
    registerBtn.addEventListener("click", showRegisterForm);
  }

  // ===============================================
  // Lógica de Sesión
  // ===============================================

  // Función de Logout (Salir) - Cierra sesión y redirige a la página de inicio (index.html)
  window.simulateLogout = function () {
    localStorage.removeItem("currentUser");
    localStorage.removeItem("dadesPerfil");
    window.location.href = "index.html";
  };

  // Función para Cambiar Usuario - Cierra sesión y redirige a la página de Login (login.html)
  window.simulateChangeUser = function () {
    localStorage.removeItem("currentUser");
    localStorage.removeItem("dadesPerfil");
    window.location.href = "login.html";
  };

  // Función de simulación de Login (para usar en login.html)
  // NOTA: Esta función no se usa directamente para el login HTTP, pero se mantiene si se usa en otro lugar.
  /*window.simulateLogin = function(username, name, surname1, surname2, email, password) {
        const userData = { username, name, surname1, surname2, email, password };
        localStorage.setItem('currentUser', JSON.stringify(userData));
        window.location.href = 'perfil.html'; 
    };*/

  // Función para actualizar el botón en el header
  function updateHeaderLoginButton() {
    const currentUser = localStorage.getItem("currentUser");

    if (currentUser) {
      // USUARIO LOGEADO: Mostrar un Dropdown (funciona con CLIC nativo de Bootstrap)
      const userData = JSON.parse(currentUser);
      // Si el backend proporciona un 'username' se usa, si no, se puede usar el 'name'
      const displayName =
        userData.nomUs || userData.emailUs?.split("@")[0] || "Usuari";

      loginOrProfileBtnContainer.innerHTML = `
                <div class="dropdown">
                    <button class="btn btn-primary cta-login dropdown-toggle" type="button" 
                            id="userDropdown" data-bs-toggle="dropdown" aria-expanded="false" 
                            style="margin-left: 1.5rem;">
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-person-fill me-2" viewBox="0 0 16 16">
                            <path d="M3 14s-1 0-1-1 1-4 6-4 6 3 6 4-1 1-1 1H3zm5-6a3 3 0 1 0 0-6 3 3 0 0 0 0 6"/>
                        </svg>
                        ${displayName}
                    </button>
                    
                    <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="userDropdown">
                        <li><a class="dropdown-item" href="perfil.html">El meu Perfil</a></li>
                        <li><a class="dropdown-item" href="#" onclick="simulateChangeUser()">Canviar Usuari</a></li>
                        <li><hr class="dropdown-divider"></li>
                        <li><a class="dropdown-item" href="#" onclick="simulateLogout()">Tancar Sessió</a></li>
                    </ul>
                </div>
            `;
    } else {
      // USUARIO NO LOGEADO: mostrar "Login / Registre"
      loginOrProfileBtnContainer.innerHTML = `
                <a href="login.html" class="btn btn-primary ms-lg-3 cta-login">Login</a>
            `;
    }
  }

  // ===============================================
    // Lógica para Botón de "Veure Tots els Animals" en index.html
    // ===============================================
    const viewAllAnimalsBtn = document.getElementById('viewAllAnimalsBtn');
    const isLoggedIn = localStorage.getItem('currentUser') !== null; // Comprueba si existe la sesión de usuario
    
    if (viewAllAnimalsBtn) {
        if (isLoggedIn) {
            // Si está logeado, el botón va a la página de búsqueda
            viewAllAnimalsBtn.href = 'cerca-animal.html'; 
        } else {
            // Si NO está logeado, gestionamos el click
            viewAllAnimalsBtn.href = '#'; // Quitar la URL directa
            viewAllAnimalsBtn.addEventListener('click', (event) => {
                event.preventDefault(); // Evita la navegación por defecto
                alert('Has d\'iniciar sessió per cercar animals.');
            });

            // Opcional: Deshabilitar visualmente el botón
            viewAllAnimalsBtn.classList.add('disabled');
            viewAllAnimalsBtn.style.opacity = '0.7'; 
        }
    }

  // ===============================================
  // Función de Protección de Páginas
  // ===============================================
  function requireLogin(redirectUrl = "login.html") {
    // Intentamos cargar los datos del perfil (que es la fuente de verdad)
    const currentUserJSON = localStorage.getItem("dadesPerfil");

    if (!currentUserJSON) {
      // No hay sesión, mostramos alerta y redirigimos
      if (window.location.pathname.endsWith("perfil.html")) {
        // No alertamos en perfil.html ya que perfil.js ya lo hace al cargar
        window.location.href = redirectUrl;
      } else if (
        window.location.pathname.endsWith("cerca-animal.html") ||
        window.location.pathname.endsWith("cerca-protectora.html")
      ) {
        alert("Has d'iniciar sessió per accedir a la cerca d'animals.");
        window.location.href = redirectUrl;
      } else {
        // Para cualquier otra página que pueda requerir protección
        window.location.href = redirectUrl;
      }
      return false;
    }
    return true;
  }

  // Ejecutar al cargar la página
  updateHeaderLoginButton();

  window.addEventListener("storage", updateHeaderLoginButton);
});
