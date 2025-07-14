import CompanyForm from "../components/CompanyForm";
import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";

function Companies() {
  const navigate = useNavigate();
  const [roles, setRoles] = useState([]);

  useEffect(() => {
    const roles = JSON.parse(localStorage.getItem("roles"));
    setRoles(roles || []);

    

    if (!roles) {
      // User is not logged in
      navigate("/login");
    } else if (
      roles.includes("ROLE_RESTAURANT_ADMIN") ||
      roles.includes("ROLE_MANAGER") ||
      roles.includes("ROLE_USER")) {
      // Logged in but wrong role
      navigate("/unauthorized");
    }
  }, [navigate]);

  return (
    <>
      <div className="flex">
        <div className="bg-stone-200 w-1/6 h-screen">
          <h2 className="bg-darkred text-white text-2xl text-center">Admin Panel</h2>
          <button className="bg-darkred text-white text-xl w-full mt-6 rounded-sm cursor-pointer py-2"> Companies</button>

        </div>
                <div className="w-5/6 p-6">
          <CompanyForm />
        </div>
      </div> 
    </>
  );
}


export default Companies;