import NavBar from "../components/NavBar";
import Footer from "../components/Footer";
import { Link, useNavigate } from "react-router-dom";
import { useState } from "react";
import { useAuth } from "../context/AuthContext";



function Login() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const navigate = useNavigate();
  const { login } = useAuth();

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError("");

    try {
      const data = await login(username, password);

      if (data.roles.includes("RESTAURANT_ADMIN") || data.roles.includes("MANAGER")) {
        navigate("/admin/restaurants/dashboard");
      } else if (data.roles.includes("ADMIN")) {
        navigate("/admin/companies");
      } else {
        navigate("/");
      }
    } catch (err: any) {
      console.error(err);
      setError(err.response?.data?.message || "Invalid credentials");
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      <NavBar />
      <main>
        <div className="bg-[url(/backgroundImages/backgroundimage.jpg)] bg-no-repeat bg-center bg-cover h-screen flex items-center ">

          <div className="max-w-sm mx-auto p-5 bg-darkred shadow-md rounded-lg">
            <img className="" src="/carolinawingslogo.png"></img>
            <h2 className="text-2xl font-bold mt-6 text-center text-white ">Login</h2>
            {error && <p className="text-red-500 text-sm mb-4">{error}</p>}
            <form onSubmit={handleLogin}>
              <div className="py-5">
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
        <Footer />
      </main>
    </>
  )
}

export default Login;
