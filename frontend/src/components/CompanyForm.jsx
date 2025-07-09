import React, { useState } from 'react';
import axios from 'axios';

function CompanyForm() {
  const [formData, setFormData] = useState({
    name: '',
    address: '',
    industry: '',
    phoneNumber: '',
  });

  const [companies, setCompanies] = useState([]);
  const [submitted, setSubmitted] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleChange = (e) => {
    setFormData(prev => ({
      ...prev,
      [e.target.name]: e.target.value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSubmitted(false);
    setLoading(true);
    setError(null);

    try {
      await axios.post('http://localhost:8080/platform-admin/companies', formData, {
        headers: {
          'Content-Type': 'application/json'
        }
      });
      setSubmitted(true);
      await fetchCompanies();
    } catch (err) {
      console.error(err);
      setError('Failed to create company.');
    } finally {
      setLoading(false);
    }
  };

  const fetchCompanies = async () => {
    try {
      const response = await axios.get('http://localhost:8080/platform-admin/companies');
      setCompanies(response.data);
    } catch (err) {
      console.error(err);
      setError('Failed to fetch companies.');
    }
  };

  return (
    <div className="p-4 max-w-xl mx-auto">
      <h2 className="text-2xl font-bold mb-4">Create a Company</h2>
      <form onSubmit={handleSubmit} className="space-y-4 bg-white shadow-md rounded p-4">
        <input
          className="w-full border p-2 rounded"
          name="name"
          placeholder="Company Name"
          value={formData.name}
          onChange={handleChange}
          required
        />
        <input
          className="w-full border p-2 rounded"
          name="address"
          placeholder="Address"
          value={formData.address}
          onChange={handleChange}
          required
        />
        <input
          className="w-full border p-2 rounded"
          name="industry"
          placeholder="Industry"
          value={formData.industry}
          onChange={handleChange}
          required
        />
        <input
          className="w-full border p-2 rounded"
          name="phoneNumber"
          placeholder="Phone Number"
          value={formData.phoneNumber}
          onChange={handleChange}
          required
        />
        <button
          type="submit"
          className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
          disabled={loading}
        >
          {loading ? 'Submitting...' : 'Submit'}
        </button>
      </form>

      {submitted && !error && <p className="text-green-600 mt-2">Company created successfully!</p>}
      {error && <p className="text-red-600 mt-2">{error}</p>}

      {companies.length > 0 && (
        <div className="mt-6">
          <h3 className="text-xl font-semibold mb-2">Companies</h3>
          <ul className="space-y-2">
            {companies.map((company, idx) => (
              <li key={idx} className="border p-2 rounded">
                <strong>{company.name}</strong> - {company.industry} <br />
                {company.address} | {company.phoneNumber}
              </li>
            ))}
          </ul>
        </div>
      )}
    </div>
  );
};

export default CompanyForm;
