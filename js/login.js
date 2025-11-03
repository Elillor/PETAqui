document.getElementById('loginForm').addEventListener('submit', async (event) => {
    event.preventDefault();

    const email = document.getElementById('login-email').value;
    const password = document.getElementById('login-password').value;

    try {
        const response = await fetch('http://localhost:8080/api/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ email, password })
        });
        
        // El proceso de login fue exitoso (código 200-299)
        if (response.ok) {
            // 1. OBTENER LOS DATOS DEL USUARIO DEL SERVIDOR
            const userData = await response.json(); 
            
            // 2. CONEXIÓN CRÍTICA: GUARDAR EN localStorage 
            // Esto activa el mecanismo de auth.js y permite a perfil.js cargar los datos.
            localStorage.setItem('currentUser', JSON.stringify(userData)); // La clave 'currentUser' es leída por auth.js y perfil.js
            
            alert('Login exitoso!');
            
            // Redirigir al perfil o a la página principal como usuario logeado
            window.location.href = 'perfil.html'; // Redirigimos al perfil para confirmar
        } else {
            // Error de credenciales o de servidor (ej: 401, 404, 500)
            const errorData = await response.text();
            alert('Login erróneo. Por favor, revisa tus credenciales.');
            console.error('Error details:', errorData);
        }
    } catch (error) {
        // Error de red (el servidor no responde)
        alert('Error de conexión. Inténtalo de nuevo más tarde.');
        console.error('Fetch error:', error);
    }

});