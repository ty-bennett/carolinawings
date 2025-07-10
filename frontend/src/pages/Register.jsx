import NavBar from "../components/NavBar";
import Footer from "../components/Footer";
import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from "react-router-dom";



function Register() 
{
    const [formData, setFormData] = useState({
      email: '',
      password: '',
    });

    const [success, setSuccess] = useState(false);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const handleChange = (e) => {
      setFormData(prev => ({
        ...prev,
        [e.target.name]: e.target.value
      }));
    };

    const handleSubmit = async (e) => {
      e.preventDefault();
      setLoading(true);
      setError(null);
      setSuccess(false);

      try {
        await axios.post('http://localhost:8080/api/auth/register', formData, {
          headers: {
            'Content-Type': 'application/json'
          }
        });
        setSuccess(true);
        setFormData({ email: '', password: '' });
        navigate("/login");
      } catch (err) {
        console.error(err);
        setError('Failed to register user.');
      } finally {
        setLoading(false);
      }
    }

  return (
    <>
      <main>
          <NavBar />
            <div className="p-4 max-w-md mx-auto mt-6 bg-white rounded shadow">
              <h2 className="text-2xl font-bold mb-4">Register</h2>
              <form onSubmit={handleSubmit} className="space-y-4">
                <input
                  className="w-full border p-2 rounded"
                  type="email"
                  name="email"
                  placeholder="Email"
                  value={formData.email}
                  onChange={handleChange}
                  required
                />
                <input
                  className="w-full border p-2 rounded"
                  type="password"
                  name="password"
                  placeholder="Password"
                  value={formData.password}
                  onChange={handleChange}
                  required
                />
                <button
                  type="submit"
                  className="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700 flex items-center justify-center"
                  disabled={loading}
                >
                  {loading ? (
                    <span className="w-5 h-5 border-2 border-white border-t-transparent rounded-full animate-spin cursor-pointer"></span>
                  ) : (
                    'Register'
                  )}
                </button>
              </form>
            {success && <p className="text-green-600 mt-2">User registered successfully!</p>}
            {error && <p className="text-red-600 mt-2">{error}</p>}
          </div>
        <Footer />
      </main>
    </>
  )
} 

export default Register;