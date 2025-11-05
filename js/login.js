document
  .getElementById("loginForm")
  .addEventListener("submit", async (event) => {
    event.preventDefault();

    const emailUs = document.getElementById("login-email").value;
    const clauPas = document.getElementById("login-password").value;

    const response = await fetch("http://localhost:8080/api/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ emailUs, clauPas }),
    });

    if (response.ok) {
      alert("Login successful!");
      window.location.href = "index.html";
    } else {
      alert("Login erroneo. Pon bien las credenciales.");
      const errorData = await response.text();
      console.error("Error details:", errorData);
    }
  });
