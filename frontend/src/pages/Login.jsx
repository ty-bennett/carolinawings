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
        body: JSON.stringify({ username: username, password: password }),
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
      <main className=""> 
          <div className="bg-[url(/backgroundImages/backgroundimage.jpg)] bg-no-repeat bg-center bg-cover min-h-screen ">
          <NavBar />
          <div className="max-w-sm mx-auto mt-20 p-4 bg-darkred shadow-md rounded-lg">
          <img className="pl-4" src="/carolinawingslogo.png"></img>
          <h2 className="text-2xl font-bold mt-6 text-center text-white ">Login</h2>
          {error && <p className="text-red-500 text-sm mb-4">{error}</p>}
          <form onSubmit={handleLogin}>
              <div className="mb-4 py-5">
              <label className="block text-white mb-1 w-full">Email</label>
              <input
                  type="username"
                  className="w-full px-4 py-2 border rounded bg-white"
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
                  required
              />
              </div>

              <div className="mb-6">
              <label className="block text-white mb-1">Password</label>
              <input
                  type="password"
                  className="w-full px-4 py-2 border rounded bg-white"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  required
              />
              </div>

              <button
              type="submit"
              className="w-full bg-[#be9514] text-white py-2 rounded hover:bg-blue-700 transition cursor-pointer"
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
            <div className="mt-6 text-center">
              <p className="text-white text-sm">
                Don't have an account?{" "}
                <Link to="/register" className="text-blue-400 hover:text-sky-200 underline font-sans"> 
                  Sign up here
                </Link>
              </p>
            </div> 
          </form>
        </div>
      </div>
      </main>
      <div className="flex flex-col flex-end mt-5">
        <Footer />
      </div>
    </>
  )
}

export default Login;