import NavBar from "../components/NavBar";
import Footer from "../components/Footer";
import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from "react-router-dom";



function Register() 
{
    const [formData, setFormData] = useState({
      name: '',
      username: '',
      password: '',
      phoneNumber: '',
      newsletterMember: false
    });

    const [success, setSuccess] = useState(false);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const handleChange = (e) => {
      const{ name, type, checked, value } = e.target;

      setFormData((prev) => ({
        ...prev,
        [name]: type === 'checkbox' ? checked : value 
      }));
    };

    const handleSubmit = async (e) => {
      e.preventDefault();
      setLoading(true);
      setFormData(formData);
      setError(null);
      setSuccess(false);

      try {
        await axios.post('http://localhost:8080/api/auth/register', formData, {
          headers: {
            'Content-Type': 'application/json'
          }
        });
        setSuccess(true);
        setTimeout(() => {
          navigate("/login")
        }, 2000);
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
        <div className="bg-[url(/backgroundImages/backgroundimage.jpg)] bg-no-repeat bg-center bg-cover min-h-screen flex items-center">
          
          <div className="max-w-sm mx-auto p-5 bg-darkred shadow-md rounded-lg">
          <img className="" src="/carolinawingslogo.png"></img>
          <h2 className="text-2xl font-bold mt-6 text-center text-white ">Register Account</h2>
          {error && <p className="text-red-500 text-sm mb-4">{error}</p>}
            
            <form className="p-2" onSubmit={handleSubmit}>
              <div className="my-2">
                <label className="block text-white mb-1 w-full">Name</label>
                <input
                  className="w-full p-2 py-2 border rounded bg-white mx-auto"
                  type="text"
                  name="name"
                  placeholder="Name"
                  value={formData.name}
                  onChange={handleChange}
                  autoComplete="name"
                  required
                />
              </div>
              <div className="my-2">
                <label className="block text-white mb-1 w-full">Email</label> 
                <input
                  className="w-full border p-2 rounded py-2 bg-white mx-auto"
                  type="email"
                  name="username"
                  placeholder="Email"
                  value={formData.username}
                  onChange={handleChange}
                  autoComplete="email"
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
                  autoComplete="current-password"
                  required
                />
              </div> 
              <div className="my-2">
                <label className="block text-white mb-1 w-full">Phone Number</label> 
                <input
                  className="w-full border p-2 rounded py-2 bg-white mx-auto"
                  type="tel"
                  name="phoneNumber"
                  placeholder="Ex: 8034084000"
                  value={formData.phoneNumber}
                  onChange={handleChange}
                  autoComplete="tel"
                  required
                />
              </div>
              <div className="flex flex-row">
              <div className="my-2">
                <label className="text-white mb-1 pr-1">Do you want to sign up for the newsletter?</label>
                <input
                  className="border p-2 rounded py-2 bg-white mx-auto flex-end"
                  type="checkbox"
                  name="newsletterMember"
                  value={formData.newsletterMember}
                  onChange={handleChange}
                />
              </div>
              </div>
              <div className="w-full justify-center flex">
                <button
                  type="submit"
                  className="w-full bg-[#be9514] text-white px-4 py-2 my-4 mt-6 rounded hover:bg-green-700 flex items-center justify-center cursor-pointer"
                  disabled={loading}
                >
                  {loading ? (
                    <span className="w-5 h-5 border-2 border-white text-center border-t-transparent rounded-full animate-spin cursor-pointer"></span>
                  ) : (
                    'Register'
                  )}
                </button>
                </div>
              </form>
                  {success && <p className="text-white-600 mt-2 text-center bg-green-500">User registered successfully!</p>}
                  {error && <p className="text-red-600 bg-white mt-2">{error}</p>}
            </div>
          </div>
        <Footer />
      </main>
    </>
  )
} 

export default Register;