document.getElementById('loginForm').addEventListener('submit', async (event) => {
    event.preventDefault();

    const email = document.getElementById('login-email').value;
    const password = document.getElementById('login-password').value;

    const response = await fetch('http://localhost:8080/auth/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ email, password })
    });
    

    if (response.ok) {
        alert('Login successful!');
        window.location.href = 'index.html';
    } else {
        alert('Login erroneo. Pon bien las credenciales.');
        const errorData = await response.text();
        console.error('Error details:', errorData);
    }

});