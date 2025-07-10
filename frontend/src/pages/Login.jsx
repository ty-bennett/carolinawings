import NavBar from "../components/NavBar.jsx";
import Footer from "../components/Footer.jsx";
import { Link, useNavigate } from "react-router-dom";
import { useState } from "react";




function Login() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError("");

    try {
      const res = await fetch("http://localhost:8080/api/auth/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ username, password }),
      });

      if (!res.ok) {
        throw new Error("Invalid credentials");
      }

      const data = await res.json();

      // Store in localStorage (adjust keys based on your response format)
      localStorage.setItem("token", data.token);
      localStorage.setItem("role", data.role);
      localStorage.setItem("username", data.username);

      navigate("/"); // Redirect on success
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

    return (
    <>
      <main>
        <NavBar />
        {/* Centered login form */}
        <div className="max-w-sm mx-auto mt-20 p-6 bg-white shadow-md rounded-lg">
        <h2 className="text-2xl font-bold mb-6 text-center">Login</h2>
        {error && <p className="text-red-500 text-sm mb-4">{error}</p>}
        <form onSubmit={handleLogin}>
            <div className="mb-4">
            <label className="block text-gray-600 mb-1">Email</label>
            <input
                type="username"
                className="w-full px-4 py-2 border rounded"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                required
            />
            </div>

            <div className="mb-6">
            <label className="block text-gray-600 mb-1">Password</label>
            <input
                type="password"
                className="w-full px-4 py-2 border rounded"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
            />
            </div>

            <button
            type="submit"
            className="w-full bg-blue-600 text-white py-2 rounded hover:bg-blue-700 transition"
            disabled={loading}
            >
            {loading ? (
                <div className="flex justify-center items-center">
                <svg
                    className="animate-spin h-5 w-5 text-white"
                    viewBox="0 0 24 24"
                >
                    <circle
                    className="opacity-25"
                    cx="12"
                    cy="12"
                    r="10"
                    stroke="currentColor"
                    strokeWidth="4"
                    fill="none"
                    ></circle>
                    <path
                    className="opacity-75"
                    fill="currentColor"
                    d="M4 12a8 8 0 018-8v4l3-3-3-3v4a8 8 0 00-8 8h4z"
                    ></path>
                </svg>
                </div>
            ) : (
                "Login"
            )}
            </button>
        </form>
        </div>

        <Footer />
      </main>
    </>
  )
}

export default Login;