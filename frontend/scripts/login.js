let loginForm = document.getElementById("login-form");

loginForm.addEventListener("submit", (event) => {
  event.preventDefault();

  let loginFormData = new FormData(event.target);

  let username = loginFormData.get("username");
  let password = loginFormData.get("password");

  let credentials = {
    username: "someusername",
    password: "Somepassword@123",
  };

  credentials.username = username;
  credentials.password = password;

  console.log("credentials : ", credentials);

  authenticate(credentials);
});

let authenticate = (credentials) => {
  const options = {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(credentials),
  };

  fetch("http://localhost:8888/authentication/authenticate", options)
    .then(async (response) => {
      if (!response.ok) {
        const error = await response.json();
        throw new Error(JSON.stringify(error));
      }
      return response.json();
    })
    .then((response) => {
      window.alert(`Login success...! \nWelcome: ${response.username}`);
      const loggedInUserUsername = response.username;
      const jwtToken = response.jwt;
      const expirationDateStr = response.expirationDate;
      const expirationDate = new Date(expirationDateStr);
      const expirationDateUTCStr = expirationDate.toUTCString();
      const isExpired = response.isExpired;
      setCookie(
        jwtToken,
        expirationDateUTCStr,
        isExpired,
        loggedInUserUsername
      );
      window.location.href = "/html/index.html";
    })
    .catch((error) => {
      console.error("Error during authentication: ", error);

      try {
        const parsedError = JSON.parse(error.message);
        if (parsedError.message == "Validation failed.") {
          window.alert(parsedError.description);
        } else if (parsedError.message == "Invalid username or password.") {
          window.alert(parsedError.message);
        } else {
          window.alert("An error occurred during authentication.");
        }
      } catch (parseError) {
        window.alert("An error occurred during authentication.");
      }
    });
};

function setCookie(
  value,
  expirationDateUTCStr,
  isExpired,
  loggedInUserUsername
) {
  const loggedInUser = `loggedInUsername=${loggedInUserUsername}`;
  const setExpired = `isExpired=${isExpired}`;
  const expiryDate = `expirationDate=${expirationDateUTCStr}`;
  const jwt = `jwtToken=${value}`;
  document.cookie = `${jwt};`;
  document.cookie = `${loggedInUser};`;
  document.cookie = `${setExpired};`;
  document.cookie = `${expiryDate};`;
}
