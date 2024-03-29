import navbar from "../components/navbar.js";

document.getElementById("navbar").innerHTML = navbar();

// Helper function to get the value of a cookie by name
function getJWTFromCookie(name) {
  const cookieArray = document.cookie.split(";");
  console.log("Cookie : ", document.cookie.split(";"));
  for (const cookie of cookieArray) {
    const [cookieName, cookieValue] = cookie.trim().split("=");
    if (cookieName === name) {
      return cookieValue;
    }
  }
  return null;
}

function getUsernameFromCookie(loggedInUsername) {
  const cookieArray = document.cookie.split(";");
  for (const cookie of cookieArray) {
    const [cookieName, cookieValue] = cookie.trim().split("=");
    if (cookieName === loggedInUsername) {
      return cookieValue;
    }
  }
  return null;
}

function getExpirationDate(expirationDate) {
  const cookieArray = document.cookie.split(";");
  for (const cookie of cookieArray) {
    const [cookieName, cookieValue] = cookie.trim().split("=");
    if (cookieName === expirationDate) {
      return cookieValue;
    }
  }
  return null;
}

function getIsExpired(isExpired) {
  const cookieArray = document.cookie.split(";");
  for (const cookie of cookieArray) {
    const [cookieName, cookieValue] = cookie.trim().split("=");
    if (cookieName === isExpired) {
      return cookieValue;
    }
  }
  return null;
}

// Example usage in another JavaScript file:
const jwtToken = getJWTFromCookie("jwtToken");

if (jwtToken) {
  // Use the JWT token as needed
  console.log("JWT Token:", jwtToken);
  const loggedInUsername = getUsernameFromCookie("loggedInUsername");
  const expiryDate = getExpirationDate("expirationDate");
  const isExpired = getIsExpired("isExpired");
  let currDate = new Date().toUTCString();
  console.log(
    "Curr : ",
    currDate,
    "Expiry : ",
    new Date(expiryDate).toUTCString()
  );
  console.log("expiryDate : ", expiryDate);
  console.log("isExpired : ", isExpired);
  console.log("Username : ", loggedInUsername);
} else {
  console.log("JWT Token not found.");
}
