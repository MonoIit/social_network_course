const BASE = "http://localhost:8080/api/v1";

export async function validateToken(token) {
  const res = await fetch(`${BASE}/auth/validate`, {
    headers: { "auth-token": token }
  });

  if (res.status === 401) {
    throw new Error("UNAUTHORIZED");
  }

  return res.json();
}

export async function login(login, password) {
    const response = await fetch(`${BASE}/auth/login`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ login, password })
    });

    if (response.status === 401) {
        throw new Error("INVALID_CREDENTIALS");
    }

    if (!response.ok) {
        throw new Error("SERVER_ERROR");
    }

    return response.json();
}

export async function register({ login, password, email }) {
    const response = await fetch(`${BASE}/auth/register`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ login, password, email })
    });

    if (response.status === 409) {
        throw new Error("USER_EXISTS");
    }

    if (response.status === 401) {
        throw new Error("INVALID_DATA");
    }

    if (!response.ok) {
        throw new Error("SERVER_ERROR");
    }
}