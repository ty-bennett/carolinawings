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
        setFormData({ name: '', phoneNumber: '', email: '', password: '', newsletterMember: ''});
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
        <div className="bg-[url(/backgroundImages/backgroundimage.jpg)] bg-no-repeat bg-center bg-cover min-h-screen">
          <NavBar />
          <div className="max-w-sm mx-auto p-4 bg-darkred shadow-md rounded-lg">
          <img className="pl-4" src="/carolinawingslogo.png"></img>
          <h2 className="text-2xl font-bold mt-6 text-center text-white ">Login</h2>
          {error && <p className="text-red-500 text-sm mb-4">{error}</p>}
            
            <form className="p-2">
              <div className="my-2">
                <label className="block text-white mb-1 w-full">Name</label>
                <input
                  className="w-full p-2 py-2 border rounded bg-white mx-auto"
                  type="text"
                  name="name"
                  placeholder="Name"
                  value={formData.name}
                  onChange={handleChange}
                  required
                />
              </div>
              <div className="my-2">
                <label className="block text-white mb-1 w-full">Email</label> 
                <input
                  className="w-full border p-2 rounded py-2 bg-white mx-auto"
                  type="email"
                  name="email"
                  placeholder="Email"
                  value={formData.email}
                  onChange={handleChange}
                  required
                />
              </div>
              <div className="my-2">
                <label className="block text-white mb-1 w-full">Password</label> 
                <input
                  className="w-full border p-2 py-2 rounded bg-white mx-auto"
                  type="password"
                  name="password"
                  placeholder="Password"
                  value={formData.password}
                  onChange={handleChange}
                  required
                />
              </div> 
              <div className="my-2">
                <label className="block text-white mb-1 w-full">Phone Number</label> 
                <input
                  className="w-full border p-2 rounded py-2 bg-white mx-auto"
                  type="number"
                  name="phoneNumber"
                  placeholder="Ex: +18084084000"
                  value={formData.phoneNumber}
                  onChange={handleChange}
                  required
                />
              </div>
              <div className="flex flex-row">
              <div className="my-2">
                <label className="block text-white mb-1 w-full">Do you want to sign up for the newsletter?</label>
                <input
                  className="w-full border p-2 rounded py-2 bg-white mx-auto flex-end"
                  type="checkbox"
                  name="newsletterMember"
                  value={formData.newsletterMember}
                  onChange={handleChange}
                  required
                />
              </div>
              </div>
                <button
                  type="submit"
                  className="bg-green-600 text-white px-4 py-2 my-4 mt-6 rounded hover:bg-green-700 flex items-center justify-center"
                  disabled={loading}
                >
                  {loading ? (
                    <span className="w-5 h-5 border-2 border-white border-t-transparent rounded-full animate-spin cursor-pointer"></span>
                  ) : (
                    'Register'
                  )}
                </button>
              </form>
            </div>
          </div>
            {success && <p className="text-white-600 mt-2 text-center bg-green-500">User registered successfully!</p>}
            {error && <p className="text-red-600 mt-2">{error}</p>}
        <Footer />
      </main>
    </>
  )
} 

export default Register;