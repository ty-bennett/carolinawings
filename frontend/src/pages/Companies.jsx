import CompanyForm from "../components/CompanyForm";
import { useEffect } from "react";
import { useNavigate } from "react-router-dom";

function Companies() {
  const navigate = useNavigate();

  useEffect(() => {
    const role = "ADMIN";
    

    if (!role) {
      // User is not logged in
      navigate("/login");
    } else if (role !== "ADMIN") {
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