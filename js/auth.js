// AÑADIDO: Función global para verificar el estado de la sesión (usada por index.js)
window.isUserLoggedIn = function() {
    return localStorage.getItem('currentUser') !== null;
};

document.addEventListener("DOMContentLoaded", () => {
  
  const loginOrProfileBtnContainer = document.getElementById(
    "login-or-profile-btn"
  );

  // ===============================================
  // Lógica de Pestañas (Se mantiene)
  // ===============================================
  const loginBtn = document.getElementById("loginBtn");
  const registerBtn = document.getElementById("registerBtn");
  const loginForm = document.getElementById("loginForm");
  const registerForm = document.getElementById("registerForm");

  function showLoginForm() {
    if (loginBtn && registerBtn && loginForm && registerForm) {
      loginBtn.classList.add("active");
      registerBtn.classList.remove("active");
      loginForm.classList.add("active");
      registerForm.classList.remove("active");
    }
  }

  function showRegisterForm() {
    if (loginBtn && registerBtn && loginForm && registerForm) {
      registerBtn.classList.add("active");
      loginBtn.classList.remove("active");
      registerForm.classList.add("active");
      loginForm.classList.remove("active");
    }
  }

  if (loginBtn && registerBtn) {
    loginBtn.addEventListener("click", showLoginForm);
    registerBtn.addEventListener("click", showRegisterForm);
  }

  // ===============================================
  // Lógica de Sesión y Perfil (Se mantiene)
  // ===============================================

  window.simulateLogout = function () {
    localStorage.removeItem("currentUser");
    localStorage.removeItem("dadesPerfil");
    window.location.href = "index.html";
  };

  window.simulateChangeUser = function () {
    localStorage.removeItem("currentUser");
    localStorage.removeItem("dadesPerfil");
    window.location.href = "login.html";
  };

  function updateHeaderLoginButton() {
    const currentUser = localStorage.getItem("currentUser");

    if (currentUser) {
      // USUARIO LOGEADO: Mostrar un Dropdown
      const userData = JSON.parse(currentUser);
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
                        <li><a id="admin" class="dropdown-item d-none" href="admin.html">Administració</a></li>
                        <li><a class="dropdown-item" href="#" onclick="simulateChangeUser()">Canviar Usuari</a></li>
                        <li><hr class="dropdown-divider"></li>
                        <li><a class="dropdown-item" href="#" onclick="simulateLogout()">Tancar Sessió</a></li>
                    </ul>
                </div>
            `;
        if(userData.rolUs === "ADMIN"){
            document.getElementById("admin").classList.remove("d-none");
        }
    } else {
      // USUARIO NO LOGEADO: mostrar "Login"
      loginOrProfileBtnContainer.innerHTML = `
                <a href="login.html" class="btn btn-primary ms-lg-3 cta-login">Login</a>
            `;
    }
  }

  // ===============================================
  // Lógica para Botón de "Veure Tots els Animals" (ACTUALIZADO CON CORRECCIÓN)
  // ===============================================
  
  function handleProtectedAccess(event) {
      event.preventDefault(); 
      alert('Necessites iniciar sessió per accedir veure tots els animals.'); 
  }

  function setupViewAllAnimalsButton() {
      const viewAllAnimalsBtn = document.getElementById('viewAllAnimalsBtn');
      const isLoggedIn = window.isUserLoggedIn(); 
      
      if (!viewAllAnimalsBtn) return;

      viewAllAnimalsBtn.removeEventListener('click', handleProtectedAccess);

      if (isLoggedIn) {
          viewAllAnimalsBtn.href = 'cerca-animal.html'; 
          viewAllAnimalsBtn.classList.remove('disabled');
          viewAllAnimalsBtn.style.opacity = '1';
      } else {
          viewAllAnimalsBtn.href = '#'; 
          // Solución: Quitamos la clase 'disabled' para que el evento click funcione
          viewAllAnimalsBtn.classList.remove('disabled'); 
          viewAllAnimalsBtn.style.opacity = '0.7'; 

          viewAllAnimalsBtn.addEventListener('click', handleProtectedAccess);
      }
  }

  // ===============================================
  // Lógica de Protección de Páginas (Se mantiene)
  // ===============================================
  function requireLogin(redirectUrl = "login.html") {
    const currentUserJSON = localStorage.getItem("dadesPerfil");

    if (!currentUserJSON) {
      if (window.location.pathname.endsWith("perfil.html")) {
        window.location.href = redirectUrl;
      } else if (
        window.location.pathname.endsWith("cerca-animal.html") ||
        window.location.pathname.endsWith("cerca-protectora.html")
      ) {
        alert("Has d'iniciar sessió per accedir a la cerca d'animals.");
        window.location.href = redirectUrl;
      } else {
        window.location.href = redirectUrl;
      }
      return false;
    }
    return true;
  }

  // Ejecutar al cargar la página
  updateHeaderLoginButton();
  setupViewAllAnimalsButton(); 

  window.addEventListener("storage", () => {
    updateHeaderLoginButton();
    setupViewAllAnimalsButton();
  });
});